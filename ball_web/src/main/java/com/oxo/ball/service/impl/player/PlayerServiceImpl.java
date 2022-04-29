package com.oxo.ball.service.impl.player;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oxo.ball.auth.PlayerDisabledException;
import com.oxo.ball.auth.TokenInvalidedException;
import com.oxo.ball.bean.dao.BallBalanceChange;
import com.oxo.ball.bean.dao.BallPlayer;
import com.oxo.ball.bean.dao.BallSystemConfig;
import com.oxo.ball.bean.dto.queue.MessageQueueDTO;
import com.oxo.ball.bean.dto.queue.MessageQueueLogin;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            if(byId.getStatus()==2){
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
    public boolean editPwd(Long id, String password) {
        UpdateWrapper<BallPlayer> wrapper = new UpdateWrapper<>();
        wrapper.eq("id", id);
        BallPlayer dao = new BallPlayer();
        dao.setPassword(password);
        return update(dao, wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse registPlayer(PlayerRegistRequest registRequest, String ipAddress) {
        Long parentPlayerId = 0L;
        String superTree = "";
        String parentPlayerName = "";
        BallPlayer parentPlayer=null;
        //先判断验证码是否正确
        BaseResponse baseResponse = checkVerifyCode(registRequest.getVerifyKey(), registRequest.getCode());
        if (baseResponse.getCode() != StatusCodes.OK) {
            return baseResponse;
        }
        //检查数据库里面是否有用户名
        BallPlayer ballPlayer = basePlayerService.findByUsername(registRequest.getUsername());
        if (ballPlayer != null) {
            return BaseResponse.failedWithMsg("账号已存在~");
        }
        //判断两次密码是否相等
        if (!registRequest.getPassword().equals(registRequest.getTwoPassword())) {
            return BaseResponse.failedWithMsg("两次密码不对");
        }
        //是否有输入邀请码,判定当前系统是否开启了邀请码
        BallSystemConfig systemConfig = systemConfigService.getSystemConfig();
        if (systemConfig.getRegisterIfNeedVerificationCode() == 1) {
            //开启了邀请码,必须输入邀请码
            if (StringUtils.isEmpty(registRequest.getInvitationCode())) {
                //没有邀请码
                List<Map<String, Object>> errorList = new ArrayList<>();
                Map<String, Object> data = new HashMap<>();
                data.put("name", "invitationCode");
                data.put("msg", "必须输入邀请码");
                errorList.add(data);
                return BaseResponse.failedWithData(BaseResponse.FAIL_FORM_SUBMIT,errorList);
            } else {
                //邀请码是否正确
                parentPlayer = basePlayerService.findByInvitationCode(registRequest.getInvitationCode());
                if (parentPlayer == null) {
                    List<Map<String, Object>> errorList = new ArrayList<>();
                    Map<String, Object> data = new HashMap<>();
                    data.put("name", "invitationCode");
                    data.put("msg", "邀请码不正确");
                    errorList.add(data);
                    return BaseResponse.failedWithData(BaseResponse.FAIL_FORM_SUBMIT,errorList);
                } else {
                    parentPlayerName = parentPlayer.getUsername();
                    //邀请码正确,注册账号上级为邀请码关联账号
                    parentPlayerId = parentPlayer.getId();
                    //tree
                    superTree = (StringUtils.isEmpty(parentPlayer.getSuperTree())?"":parentPlayer.getSuperTree())+parentPlayer.getId()+"_";
                }
            }
        } else {
            //TODO 当前没有指定默认邀请码
            //没有指定邀请码就是顶端tree path
            superTree = "_";
        }
        String invitationCode = "";
        while (true){
            invitationCode = String.valueOf(TimeUtil.getRandomNum(1234567, 9876543));
            BallPlayer byInvitationCode = basePlayerService.findByInvitationCode(invitationCode);
            if(byInvitationCode==null){
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
                .theLastIp(ipAddress)
                .superiorId(parentPlayerId)
                .superTree(superTree)
                .accountType(2)
                .status(1)
                //TODO 玩家注册送888888
                .balance(888888*BigDecimalUtil.PLAYER_MONEY_UNIT)
                .superiorName(parentPlayerName)
                .build();
        MapUtil.setCreateTime(save);

        boolean res = save(save);
        if (res) {
            //如果保存成功，并且有上级，重新计算上级的团队和下属计数
            if(save.getSuperiorId()!=0){
                //上级直属下级+1,团队人数+1
                while (true){
                    BallPlayer parentPlayerEdit = BallPlayer.builder()
                            .version(parentPlayer.getVersion())
                            .directlySubordinateNum(parentPlayer.getDirectlySubordinateNum()+1)
                            .groupSize(parentPlayer.getGroupSize()+1)
                            .build();
                    parentPlayerEdit.setId(parentPlayerId);
                    boolean isSucc = basePlayerService.editAndClearCache(parentPlayerEdit, parentPlayer);
                    if(isSucc){
                        break;
                    }else{
                        parentPlayer = basePlayerService.findById(parentPlayerId);
                    }
                }
                //上级的上上上。。级团队人数+1
                String treePath = parentPlayer.getSuperTree();
                if(!StringUtils.isEmpty(treePath)&&!treePath.equals("_")){
                    String ids = StringUtils.join(treePath.split("_"), ",").substring(1);
                    basePlayerService.editMultGroupNum(ids,1);
                }

            }
            redisUtil.del(RedisKeyContant.VERIFY_KEY+registRequest.getVerifyKey());
        }
        return res ? BaseResponse.successWithMsg("注册成功~") : BaseResponse.failedWithMsg("注册失败~");
    }


    @Override
    public BaseResponse login(PlayerAuthLoginRequest req, HttpServletRequest request) {
        //判定账号密码输入错误次数
        String redisKey = RedisKeyContant.PLAYER_LOGIN_FAIL_COUNT + req.getUsername();
        String ip = IpUtil.getIpAddress(request);
        Object failCountRedis = redisUtil.get(redisKey);
        int failCount = 0;
        if(failCountRedis!=null){
            failCount = Integer.parseInt(failCountRedis.toString());
            BallSystemConfig systemConfig = systemConfigService.getSystemConfig();
            if(failCount==systemConfig.getPasswordMaxErrorTimes()){
                Map<String, Object> data = MapUtil.newMap("time", redisUtil.getExpire(redisKey));
                data.put("msg","密码错误次数已达上限~");
                data.put("failCount",failCount);
                return BaseResponse.failedWithData(BaseResponse.FAIL_LOGIN_ERROR_MAX,data);
            }
        }
        BaseResponse baseResponse = checkVerifyCode(req.getVerifyKey(), req.getCode());
        if (baseResponse.getCode() != StatusCodes.OK) {
            return baseResponse;
        }
        BallPlayer ballPlayer = null;
        try {
            ballPlayer = basePlayerService.findByUsername(req.getUsername());
            if(ballPlayer.getStatus()==2){
                return BaseResponse.failedWithMsg(IPlayerService.STATUS_DISABLED,"");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (ballPlayer == null || !ballPlayer.getPassword().equals(PasswordUtil.genPasswordMd5(req.getPassword()))) {
            //缓存当前输入错误次数
            BallSystemConfig systemConfig = systemConfigService.getSystemConfig();
            redisUtil.incr(redisKey,1);
            redisUtil.expire(redisKey,systemConfig.getPasswordErrorLockTime());
            failCount++;
            Map<String, Object> respData = MapUtil.newMap("failCount", failCount);
            if(failCount == systemConfig.getPasswordMaxErrorTimes()){
                //指定秒后解锁
                redisUtil.expire(redisKey,systemConfig.getPasswordErrorLockTime());
                respData.put("time",systemConfig.getPasswordErrorLockTime());
                respData.put("msg","密码错误次数已达上限~");
                return BaseResponse.failedWithData(BaseResponse.FAIL_LOGIN_ERROR_MAX,respData);
            }
            respData.put("msg","密码错误次数"+failCount+"次");
            return BaseResponse.failedWithData(BaseResponse.FAIL_LOGIN_ERROR_COUNT,respData);
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
        redisUtil.set(RedisKeyContant.VERIFY_KEY+key, code, VerifyCodeUtils.TIME_OUT);
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
        Object hget = redisUtil.get(RedisKeyContant.VERIFY_KEY+verifyKey);
        if (hget == null) {
            return BaseResponse.failedWithMsg("验证码已过期~");
        }
        if (hget.equals(code)) {
            return BaseResponse.successWithMsg("");
        }
        return BaseResponse.failedWithMsg("验证码不正确~");
    }

    @Override
    public BaseResponse recharge(BallPlayer currentUser, Long money) {
        BallPlayer byId = basePlayerService.findById(currentUser.getId());
        BallPlayer edit = BallPlayer.builder()
                .balance(byId.getBalance()+money*BigDecimalUtil.PLAYER_MONEY_UNIT)
                .build();
        edit.setId(currentUser.getId());
        while(true){
            boolean b = basePlayerService.editAndClearCache(edit, byId);
            if(b){
                //插入账变
                ballBalanceChangeService.insert(BallBalanceChange.builder()
                        .playerId(byId.getId())
                        .initMoney(byId.getBalance())
                        .changeMoney(money*BigDecimalUtil.PLAYER_MONEY_UNIT)
                        .dnedMoney(edit.getBalance())
                        .remark("充值")
                        .createdAt(System.currentTimeMillis())
                        .balanceChangeType(1)
                        .build());
                return BaseResponse.successWithMsg("充值成功~");
            }else{
                //更新失败再次判定余额是否足够,
                byId = basePlayerService.findById(currentUser.getId());
                edit.setVersion(byId.getVersion());
                edit.setBalance(byId.getBalance()+money*BigDecimalUtil.PLAYER_MONEY_UNIT);
            }
        }
    }

    @Override
    public BaseResponse withdrawal(BallPlayer currentUser, Long money) {
        BallPlayer byId = basePlayerService.findById(currentUser.getId());
        money = money*BigDecimalUtil.PLAYER_MONEY_UNIT;
        if(byId.getBalance()-money<0){
            return BaseResponse.failedWithMsg("余额不足,不能提现");
        }
        BallPlayer edit = BallPlayer.builder()
                .balance(byId.getBalance()-money)
                .build();
        edit.setId(currentUser.getId());
        while(true){
            boolean b = basePlayerService.editAndClearCache(edit, byId);
            if(b){
                //插入账变
                ballBalanceChangeService.insert(BallBalanceChange.builder()
                        .playerId(byId.getId())
                        .initMoney(byId.getBalance())
                        .changeMoney(money)
                        .dnedMoney(edit.getBalance())
                        .remark("提现")
                        .createdAt(System.currentTimeMillis())
                        .balanceChangeType(2)
                        .build());
                return BaseResponse.successWithMsg("提现成功~");
            }else{
                //更新失败再次判定余额是否足够,
                byId = basePlayerService.findById(currentUser.getId());
                if(byId.getBalance()-money<0){
                    return BaseResponse.failedWithMsg("余额不足,不能提现");
                }
                edit.setVersion(byId.getVersion());
                edit.setBalance(byId.getBalance()-money);
            }
        }
    }

}
