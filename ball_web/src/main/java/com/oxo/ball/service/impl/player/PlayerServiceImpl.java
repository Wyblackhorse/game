package com.oxo.ball.service.impl.player;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oxo.ball.auth.PlayerDisabledException;
import com.oxo.ball.auth.TokenInvalidedException;
import com.oxo.ball.bean.dao.BallBalanceChange;
import com.oxo.ball.bean.dao.BallPlayer;
import com.oxo.ball.bean.dao.BallSystemConfig;
import com.oxo.ball.bean.dto.queue.MessageQueueDTO;
import com.oxo.ball.bean.dto.queue.MessageQueueLogin;
import com.oxo.ball.bean.dto.req.player.DataCenterRequest;
import com.oxo.ball.bean.dto.req.player.PlayerAuthLoginRequest;
import com.oxo.ball.bean.dto.req.player.PlayerRegistRequest;
import com.oxo.ball.bean.dto.resp.AuthLoginResponse;
import com.oxo.ball.bean.dto.resp.BaseResponse;
import com.oxo.ball.contant.RedisKeyContant;
import com.oxo.ball.mapper.BallPlayerMapper;
import com.oxo.ball.service.IBasePlayerService;
import com.oxo.ball.service.IMessageQueueService;
import com.oxo.ball.service.admin.IBallBalanceChangeService;
import com.oxo.ball.service.admin.IBallLoggerService;
import com.oxo.ball.service.admin.IBallSystemConfigService;
import com.oxo.ball.service.player.AuthPlayerService;
import com.oxo.ball.service.player.IPlayerService;
import com.oxo.ball.utils.*;
import io.undertow.util.StatusCodes;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sun.plugin.services.BrowserService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

/**
 * <p>
 * 玩家账号 服务实现类
 * </p>
 *
 * @author oxo_jy
 * @since 2022-04-13
 */
@Service
public class PlayerServiceImpl extends ServiceImpl<BallPlayerMapper, BallPlayer> implements IPlayerService {

    @Resource
    private RedisUtil redisUtil;
    @Resource
    AuthPlayerService authService;

    @Resource
    IBallSystemConfigService systemConfigService;
    @Resource
    IBasePlayerService basePlayerService;
    @Resource
    IBallBalanceChangeService ballBalanceChangeService;
    @Resource
    IBallLoggerService loggerService;
    @Resource
    IMessageQueueService messageQueueService;


    @Override
    public BallPlayer getCurrentUser(HttpServletRequest request) throws TokenInvalidedException, PlayerDisabledException {
        Long userId;
        try {
            List<String> audience = JWT.decode(request.getHeader("token")).getAudience();
            userId = Long.parseLong(audience.get(0));
            BallPlayer byId = basePlayerService.findById(userId);
            byId.setIp(IpUtil.getIpAddress(request));
            if (byId.getStatus() == 2) {
                throw new PlayerDisabledException();
            }
            return byId;
        } catch (JWTDecodeException j) {
            throw new TokenInvalidedException();
        } catch (PlayerDisabledException e) {
            throw new PlayerDisabledException();
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse registPlayer(PlayerRegistRequest registRequest, String ipAddress) {
        Long parentPlayerId = 0L;
        String superTree = "";
        String parentPlayerName = "";
        BallPlayer parentPlayer = null;
        //先判断验证码是否正确
        BaseResponse baseResponse = checkVerifyCode(registRequest.getVerifyKey(), registRequest.getCode());
        if (baseResponse.getCode() != StatusCodes.OK) {
            return baseResponse;
        }
        //检查数据库里面是否有用户名
        BallPlayer ballPlayer = basePlayerService.findByUsername(registRequest.getUsername());
        if (ballPlayer != null) {
            return BaseResponse.failedWithData(BaseResponse.FAIL_FORM_SUBMIT,
                    ResponseMessageUtil.responseMessage("username", "nameExists"));
        }
        //判断两次密码是否相等
        if (!registRequest.getPassword().equals(registRequest.getTwoPassword())) {
            return BaseResponse.failedWithData(BaseResponse.FAIL_FORM_SUBMIT,
                    ResponseMessageUtil.responseMessage("password", "passwordDiff"));
        }
        //是否有输入邀请码,判定当前系统是否开启了邀请码
        BallSystemConfig systemConfig = systemConfigService.getSystemConfig();
        if (systemConfig.getRegisterIfNeedVerificationCode() == 1) {
            //开启了邀请码,必须输入邀请码
            if (StringUtils.isEmpty(registRequest.getInvitationCode())) {
                //没有邀请码
                return BaseResponse.failedWithData(BaseResponse.FAIL_FORM_SUBMIT,
                        ResponseMessageUtil.responseMessage("invitationCode", "invitationCodeIsEmpty"));
            } else {
                //邀请码是否正确
                parentPlayer = basePlayerService.findByInvitationCode(registRequest.getInvitationCode());
                if (parentPlayer == null) {
                    return BaseResponse.failedWithData(BaseResponse.FAIL_FORM_SUBMIT,
                            ResponseMessageUtil.responseMessage("invitationCode", "invitationCodeError"));
                } else {
                    parentPlayerName = parentPlayer.getUsername();
                    //邀请码正确,注册账号上级为邀请码关联账号
                    parentPlayerId = parentPlayer.getId();
                    //tree
                    superTree = (StringUtils.isEmpty(parentPlayer.getSuperTree()) ? "" : parentPlayer.getSuperTree()) + parentPlayer.getId() + "_";
                }
            }
        } else {
            //TODO 当前没有指定默认邀请码
            //没有指定邀请码就是顶端tree path
            superTree = "_";
        }
        String invitationCode = "";
        while (true) {
            invitationCode = String.valueOf(TimeUtil.getRandomNum(1234567, 9876543));
            BallPlayer byInvitationCode = basePlayerService.findByInvitationCode(invitationCode);
            if (byInvitationCode == null) {
                break;
            }
        }
        BallPlayer save = BallPlayer.builder()
                .version(1L)
                .username(registRequest.getUsername())
                .password(PasswordUtil.genPasswordMd5(registRequest.getPassword()))
                .invitationCode(invitationCode)
                .token("")
                .theNewIp(ipAddress)
                .theNewLoginTime(System.currentTimeMillis())
                .theLastIp(ipAddress)
                .superiorId(parentPlayerId)
                .superTree(superTree)
                .accountType(2)
                .status(1)
                //TODO 玩家注册送888888
                .balance(888888 * BigDecimalUtil.PLAYER_MONEY_UNIT)
                .superiorName(parentPlayerName)
                .build();
        MapUtil.setCreateTime(save);

        boolean res = save(save);
        if (res) {
            //如果保存成功，并且有上级，重新计算上级的团队和下属计数
            if (save.getSuperiorId() != 0) {
                //上级直属下级+1,团队人数+1
                while (true) {
                    BallPlayer parentPlayerEdit = BallPlayer.builder()
                            .version(parentPlayer.getVersion())
                            .directlySubordinateNum(parentPlayer.getDirectlySubordinateNum() + 1)
                            .groupSize(parentPlayer.getGroupSize() + 1)
                            .build();
                    parentPlayerEdit.setId(parentPlayerId);
                    boolean isSucc = basePlayerService.editAndClearCache(parentPlayerEdit, parentPlayer);
                    if (isSucc) {
                        break;
                    } else {
                        parentPlayer = basePlayerService.findById(parentPlayerId);
                    }
                }
                //上级的上上上。。级团队人数+1
                String treePath = parentPlayer.getSuperTree();
                if (!StringUtils.isEmpty(treePath) && !treePath.equals("_")) {
                    String ids = StringUtils.join(treePath.split("_"), ",").substring(1);
                    basePlayerService.editMultGroupNum(ids, 1);
                }

            }
            redisUtil.del(RedisKeyContant.VERIFY_KEY + registRequest.getVerifyKey());
        }
        return res ? BaseResponse.successWithMsg("ok") : BaseResponse.failedWithMsg("failed");
    }


    @Override
    public BaseResponse login(PlayerAuthLoginRequest req, HttpServletRequest request) {
        //判定账号密码输入错误次数
        String redisKey = RedisKeyContant.PLAYER_LOGIN_FAIL_COUNT + req.getUsername();
        String ip = IpUtil.getIpAddress(request);
        Object failCountRedis = redisUtil.get(redisKey);
        int failCount = 0;
        if (failCountRedis != null) {
            failCount = Integer.parseInt(failCountRedis.toString());
            BallSystemConfig systemConfig = systemConfigService.getSystemConfig();
            if (failCount == systemConfig.getPasswordMaxErrorTimes()) {
                List<Map<String, Object>> errorList = ResponseMessageUtil.responseMessage("password", "pwdErrorMax");
                ResponseMessageUtil.responseMessage(errorList, "time", String.valueOf(redisUtil.getExpire(redisKey)));
                return BaseResponse.failedWithData(BaseResponse.FAIL_FORM_SUBMIT,
                        errorList);
            }
        }
        BaseResponse baseResponse = checkVerifyCode(req.getVerifyKey(), req.getCode());
        if (baseResponse.getCode() != StatusCodes.OK) {
            return baseResponse;
        }
        BallPlayer ballPlayer = null;
        try {
            ballPlayer = basePlayerService.findByUsername(req.getUsername());
            if (ballPlayer.getStatus() == 2) {
                return BaseResponse.failedWithMsg(IPlayerService.STATUS_DISABLED, "");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (ballPlayer == null || !ballPlayer.getPassword().equals(PasswordUtil.genPasswordMd5(req.getPassword()))) {
            //缓存当前输入错误次数
            BallSystemConfig systemConfig = systemConfigService.getSystemConfig();
            redisUtil.incr(redisKey, 1);
            redisUtil.expire(redisKey, systemConfig.getPasswordErrorLockTime());
            failCount++;
            if (failCount == systemConfig.getPasswordMaxErrorTimes()) {
                //指定秒后解锁
                redisUtil.expire(redisKey, systemConfig.getPasswordErrorLockTime());
                List<Map<String, Object>> errorList = ResponseMessageUtil.responseMessage("password", "pwdErrorMax");
                ResponseMessageUtil.responseMessage(errorList, "time", String.valueOf(redisUtil.getExpire(redisKey)));
                ResponseMessageUtil.responseMessageValue(errorList, "failCount", String.valueOf(failCount));
                return BaseResponse.failedWithData(BaseResponse.FAIL_FORM_SUBMIT,
                        errorList);
            }
            List<Map<String, Object>> errorList = ResponseMessageUtil.responseMessage("password", "pwdErrorCount");
            ResponseMessageUtil.responseMessageValue(errorList, "failCount", String.valueOf(failCount));
            return BaseResponse.failedWithData(BaseResponse.FAIL_FORM_SUBMIT,
                    errorList);
        }

        AuthLoginResponse userBean = new AuthLoginResponse();
        userBean.setId(ballPlayer.getId());
        userBean.setUserName(ballPlayer.getUsername());

        Object hget = redisUtil.hget(PlayerAuthServiceImpl.REDIS_PLAYER_AUTH_KEY, ballPlayer.getId().toString());
        if (hget == null || StringUtils.isEmpty(hget.toString())) {
            userBean.setToken(authService.buildToken(ballPlayer));
        } else {
            userBean.setToken(hget.toString());
        }
        messageQueueService.putMessage(MessageQueueDTO.builder()
                .type(MessageQueueDTO.TYPE_LOG_LOGIN)
                .data(MessageQueueLogin.builder()
                        .ballPlayer(ballPlayer)
                        .ip(ip)
                        .device("web browser")
                        .build())
                .build());
        //登录日志和修改账号登录IP
        return new BaseResponse<>(userBean);
    }

    @Override
    public BaseResponse getVerifyCode() throws IOException {
        //使用验证码工具类生成4位验证码
        String code = VerifyCodeUtils.generateVerifyCode(4);
        //将验证码放入redis
        String key = UUIDUtil.getUUID();
        redisUtil.set(RedisKeyContant.VERIFY_KEY + key, code, VerifyCodeUtils.TIME_OUT);
        //将验证码放入图片
        String imageBase64 = VerifyCodeUtils.getImageBase64(220, 60, code);
        Map<String, Object> data = new HashMap<>();
        data.put("verifyKey", key);
        data.put("time", VerifyCodeUtils.TIME_OUT);
        data.put("img", imageBase64);
        return BaseResponse.successWithData(data);
    }

    @Override
    public BaseResponse checkVerifyCode(String verifyKey, String code) {
        Object hget = redisUtil.get(RedisKeyContant.VERIFY_KEY + verifyKey);
        if (hget == null) {
            return BaseResponse.failedWithData(BaseResponse.FAIL_FORM_SUBMIT,
                    ResponseMessageUtil.responseMessage("code", "codeTimeOut"));
        }
        if (hget.equals(code)) {
            return BaseResponse.successWithMsg("");
        }
        return BaseResponse.failedWithData(BaseResponse.FAIL_FORM_SUBMIT,
                ResponseMessageUtil.responseMessage("code", "codeError"));
    }

    @Override
    public BaseResponse recharge(BallPlayer currentUser, Long money) {
        BallPlayer byId = basePlayerService.findById(currentUser.getId());
        Long realMoney = money * BigDecimalUtil.PLAYER_MONEY_UNIT;
        //TODO 充值优惠
        boolean hasDiscount = false;
        Long discountQuota = 0L;
        BallSystemConfig systemConfig = systemConfigService.getSystemConfig();
        BallPlayer edit = BallPlayer.builder()
                .balance(byId.getBalance() + realMoney)
                //累计充值
                .cumulativeTopUp((byId.getCumulativeTopUp() == null ? 0 : byId.getCumulativeTopUp()) + realMoney)
                //线上充值金额
                .onLineTopUp((byId.getOnLineTopUp() == null ? 0 : byId.getOnLineTopUp()) + realMoney)
                .build();
        if (byId.getFirstTopUp() == null || byId.getFirstTopUp() == 0) {
            //首充
            edit.setFirstTopUp(realMoney);
            edit.setFirstTopUpTime(TimeUtil.getNowTimeMill());
        }
        if (byId.getMaxTopUp() == null || byId.getMaxTopUp() == 0) {
            // 最大充值金额
            edit.setMaxTopUp(realMoney);
        } else if (byId.getMaxTopUp() < realMoney) {
            // 本次比上次大
            edit.setMaxTopUp(realMoney);
        }

        if (systemConfig.getRegisterIfNeedVerificationCode() != null && systemConfig.getRegisterIfNeedVerificationCode() > 0) {
            //累计打码量,查询配置,是否需要*比例
            edit.setCumulativeQr((byId.getCumulativeQr() == null ? 0 : byId.getCumulativeQr()) + realMoney * systemConfig.getRechargeCodeConversionRate());
        } else {
            edit.setCumulativeQr((byId.getCumulativeQr() == null ? 0 : byId.getCumulativeQr()) + realMoney);
        }
        if (systemConfig.getCaptchaThreshold() != null && systemConfig.getCaptchaThreshold() > 0) {
            //离下次提现所需captchaThreshold要打码量,如果当前打码量>设置的量,则为0,否则为相差数
            if (edit.getCumulativeQr() > systemConfig.getCaptchaThreshold()) {
                edit.setNeedQr(0L);
            } else {
                edit.setNeedQr(systemConfig.getCaptchaThreshold() - edit.getCumulativeQr());
            }
        }

        edit.setId(currentUser.getId());
        edit.setVersion(currentUser.getVersion());

        while (true) {
            boolean b = basePlayerService.editAndClearCache(edit, byId);
            if (b) {
                //插入账变
                ballBalanceChangeService.insert(BallBalanceChange.builder()
                        .playerId(byId.getId())
                        .initMoney(byId.getBalance())
                        .changeMoney(realMoney)
                        .dnedMoney(edit.getBalance())
                        //key recharge_self
//                        .remark("充值")
//                        .remark("recharge_self")
                        .createdAt(System.currentTimeMillis())
                        .balanceChangeType(1)
                        .build());
                return BaseResponse.successWithMsg("ok");
            } else {
                //更新失败再次更新
                byId = basePlayerService.findById(currentUser.getId());
                edit.setVersion(byId.getVersion());
                edit.setBalance(byId.getBalance() + realMoney);
            }
        }
    }

    @Override
    public BaseResponse withdrawal(BallPlayer currentUser, Long money) {
        BallPlayer byId = basePlayerService.findById(currentUser.getId());
        money = money * BigDecimalUtil.PLAYER_MONEY_UNIT;
        if (byId.getBalance() - money < 0) {
//            return BaseResponse.failedWithMsg("余额不足,不能提现");
            return BaseResponse.failedWithData(BaseResponse.FAIL_FORM_SUBMIT,
                    ResponseMessageUtil.responseMessage("", "balanceNotFound"));
        }
        BallPlayer edit = BallPlayer.builder()
                .balance(byId.getBalance() - money)
                .version(byId.getVersion())
                .build();
        edit.setId(currentUser.getId());
        while (true) {
            boolean b = basePlayerService.editAndClearCache(edit, byId);
            if (b) {
                //插入账变
                ballBalanceChangeService.insert(BallBalanceChange.builder()
                        .playerId(byId.getId())
                        .initMoney(byId.getBalance())
                        .changeMoney(-money)
                        .dnedMoney(edit.getBalance())
//                        .remark("提现")
//                        .remark("withdrawal_self")
                        .createdAt(System.currentTimeMillis())
                        .balanceChangeType(2)
                        .build());
                return BaseResponse.successWithMsg("ok~");
            } else {
                //更新失败再次判定余额是否足够,
                byId = basePlayerService.findById(currentUser.getId());
                if (byId.getBalance() - money < 0) {
                    return BaseResponse.failedWithData(BaseResponse.FAIL_FORM_SUBMIT,
                            ResponseMessageUtil.responseMessage("", "balanceNotFound"));
                }
                edit.setVersion(byId.getVersion());
                edit.setBalance(byId.getBalance() - money);
            }
        }
    }

    @Override
    @Cacheable(cacheNames = REDIS_DATA_CENTER, key = "#currPlayer.getId()+'_'+#dataCenterRequest.getTime()+'_'+#dataCenterRequest.getUsername()", unless = "#result==null")
    public Map<String, Object> dataCenter(BallPlayer currPlayer, DataCenterRequest dataCenterRequest) {
        // 统计: 团队余额(all) 团队充值(time) 团队提现(time) 净利润(time)
        // 团队人数(all) 新增注册 投注金额(time) 投注人数(time)
        // 中奖金额(time) 活动奖励(time) 活跃人数 未登录人数 优惠金额(time)
        Map<String, Object> data = new HashMap<>();
        QueryWrapper<BallPlayer> queryWrapper = new QueryWrapper();
        queryWrapper.likeRight("super_tree", currPlayer.getSuperTree() + currPlayer.getId()+"_");
        if(!StringUtils.isEmpty(currPlayer.getUsername())){
            queryWrapper.eq("username",dataCenterRequest.getUsername());
        }
        List<BallPlayer> list = list(queryWrapper);
        if (list == null) {
            list = new ArrayList<>();
        }
        list.add(currPlayer);
        data.put("playerCount", list.size());
        long totalBalance = 0L;
        long totalRecharge = 0L;
        long totalWithdrawal = 0L;
        long cumulativeWinning = 0L;
        int newPlayer = 0;
        long totalBetBalance = 0L;
        int playerActive = 0;
        int playerOffline = 0;
        long cumulativeDiscount = 0L;
        long cumulativeActivity = 0L;
        List<Long> ids = new ArrayList<>();
        for (BallPlayer player : list) {
            totalBalance += player.getBalance();
            //2天不在线算未上线人数
            if (System.currentTimeMillis() - player.getTheNewLoginTime() > TimeUtil.TIME_ONE_DAY * 2) {
                playerOffline++;
            } else {
                playerActive++;
            }
            //2天内注册算新人
            if (System.currentTimeMillis() - player.getCreatedAt() < TimeUtil.TIME_ONE_DAY * 2) {
                newPlayer++;
            }
            ids.add(player.getId());
        }
        //统计账变数据
        List<BallBalanceChange> balanceChanges = getBallBalanceChanges(dataCenterRequest, ids);
        Set<Long> betCount = new HashSet<>();
        for (BallBalanceChange balanceChange : balanceChanges) {
            switch (balanceChange.getBalanceChangeType()) {
                case 1:
                case 11:
                case 6:
                    totalRecharge += balanceChange.getChangeMoney();
                    break;
                case 2:
                    totalWithdrawal += Math.abs(balanceChange.getChangeMoney());
                    break;
                case 4:
                    cumulativeWinning += balanceChange.getChangeMoney();
                    break;
                case 3:
                    totalBetBalance += Math.abs(balanceChange.getChangeMoney());
                    betCount.add(balanceChange.getPlayerId());
                    break;
                case 18:
                    cumulativeActivity += balanceChange.getChangeMoney();
                    break;
                case 15:
                case 19:
                    cumulativeDiscount += balanceChange.getChangeMoney();
                    break;
                default:
                    break;
            }
        }
        //净利润 = 累计中奖-累计投注
        data.put("netProfit", cumulativeWinning - totalBetBalance);
        data.put("totalBalance", totalBalance);
        data.put("totalRecharge", totalRecharge);
        data.put("totalWithdrawal", totalWithdrawal);
        data.put("cumulativeWinning", cumulativeWinning);
        data.put("newPlayer", newPlayer);
        data.put("totalBetBalance", totalBetBalance);
        data.put("totalBetPlayer", betCount.size());
        data.put("playerActive", playerActive);
        data.put("playerOffline", playerOffline);
        data.put("cumulativeDiscount", cumulativeDiscount);
        data.put("cumulativeActivity", cumulativeActivity);
        return data;
    }

    @Override
    @Cacheable(cacheNames = REDIS_DATA_CENTER_DETAIL, key = "#player.getId()+'_'+#dataCenterRequest.getTime()", unless = "#result==null")
    public List<Map<String, Object>> dataCenterDetail(BallPlayer player, DataCenterRequest dataCenterRequest) {
        Map<Integer,Map<String, Object>> data = new HashMap<>();
        // 下级层级 新增 充值 提现
        QueryWrapper<BallPlayer> queryWrapper = new QueryWrapper();
        queryWrapper.likeRight("super_tree", player.getSuperTree() + player.getId()+"_");
        //按时间查
        if (dataCenterRequest.getTime() != null) {
            switch (dataCenterRequest.getTime()) {
                case 1:
                    queryWrapper.ge("created_at", TimeUtil.getDayBegin().getTime());
                    queryWrapper.le("created_at", TimeUtil.getDayEnd().getTime());
                    break;
                case 2:
                    queryWrapper.ge("created_at", TimeUtil.getBeginDayOfYesterday().getTime());
                    queryWrapper.le("created_at", TimeUtil.getEndDayOfYesterday().getTime());
                    break;
                case 3:
                    queryWrapper.ge("created_at", TimeUtil.getDayBegin().getTime() - 7 * TimeUtil.TIME_ONE_DAY);
                    queryWrapper.le("created_at", TimeUtil.getDayEnd().getTime());
                    break;
                case 4:
                    queryWrapper.ge("created_at", TimeUtil.getDayBegin().getTime() - 10 * TimeUtil.TIME_ONE_DAY);
                    queryWrapper.le("created_at", TimeUtil.getDayEnd().getTime());
                    break;
                case 5:
                    queryWrapper.ge("created_at", TimeUtil.getDayBegin().getTime() - 30 * TimeUtil.TIME_ONE_DAY);
                    queryWrapper.le("created_at", TimeUtil.getDayEnd().getTime());
                    break;
                default:
                    break;
            }
        }
        List<BallPlayer> list = list(queryWrapper);
        if (list == null || list.isEmpty()) {
            return null;
        }
        //下级层级 新增 充值 提现
        for(BallPlayer p:list){
            String substring = p.getSuperTree().substring(player.getSuperTree().length());
            String[] level = substring.split("_");
            Map<String, Object> item = getItem(data, level.length);
            if(item.get("levelType")==null){
                item.put("levelType",level.length);
                item.put("newPlayer",0);
                item.put("recharge",0);
                item.put("cumulativeReflect",0);
            }
            if(System.currentTimeMillis()-p.getCreatedAt()<TimeUtil.TIME_ONE_DAY*2){
                Object newPlayer = item.get("newPlayer");
                item.put("newPlayer",Integer.parseInt(newPlayer.toString())+1);
            }
            if(p.getCumulativeTopUp()>0){
                Object recharge = item.get("recharge");
                item.put("recharge",Integer.parseInt(recharge.toString())+1);
            }
            if(p.getCumulativeReflect()>0){
                Object cumulativeReflect = item.get("cumulativeReflect");
                item.put("cumulativeReflect",Integer.parseInt(cumulativeReflect.toString())+1);
            }
        }
        return new ArrayList<>(data.values());
    }
    private Map<String,Object> getItem(Map<Integer,Map<String,Object>> data,int level){
        Map<String, Object> map = data.get(level);
        if(map==null){
            map = new HashMap<>();
            data.put(level,map);
        }
        return map;
    }

    private List<BallBalanceChange> getBallBalanceChanges(DataCenterRequest dataCenterRequest, List<Long> ids) {
        //统计账变
        QueryWrapper<BallBalanceChange> changeQueryWrapper = new QueryWrapper<>();
        if(ids!=null){
            changeQueryWrapper.in("id", ids);
        }
        if (dataCenterRequest.getTime() != null) {
            switch (dataCenterRequest.getTime()) {
                case 1:
                    changeQueryWrapper.ge("created_at", TimeUtil.getDayBegin().getTime());
                    changeQueryWrapper.le("created_at", TimeUtil.getDayEnd().getTime());
                    break;
                case 2:
                    changeQueryWrapper.ge("created_at", TimeUtil.getBeginDayOfYesterday().getTime());
                    changeQueryWrapper.le("created_at", TimeUtil.getEndDayOfYesterday().getTime());
                    break;
                case 3:
                    changeQueryWrapper.ge("created_at", TimeUtil.getDayBegin().getTime() - 7 * TimeUtil.TIME_ONE_DAY);
                    changeQueryWrapper.le("created_at", TimeUtil.getDayEnd().getTime());
                    break;
                case 4:
                    changeQueryWrapper.ge("created_at", TimeUtil.getDayBegin().getTime() - 10 * TimeUtil.TIME_ONE_DAY);
                    changeQueryWrapper.le("created_at", TimeUtil.getDayEnd().getTime());
                    break;
                case 5:
                    changeQueryWrapper.ge("created_at", TimeUtil.getDayBegin().getTime() - 30 * TimeUtil.TIME_ONE_DAY);
                    changeQueryWrapper.le("created_at", TimeUtil.getDayEnd().getTime());
                    break;
                default:
                    break;
            }
        }
        return ballBalanceChangeService.list(changeQueryWrapper);
    }

}
