package com.oxo.ball.service.impl.player;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oxo.ball.bean.dao.*;
import com.oxo.ball.bean.dto.queue.MessageQueueBet;
import com.oxo.ball.bean.dto.queue.MessageQueueDTO;
import com.oxo.ball.bean.dto.req.player.BetPreRequest;
import com.oxo.ball.bean.dto.req.player.BetRequest;
import com.oxo.ball.bean.dto.req.player.PlayerBetRequest;
import com.oxo.ball.bean.dto.resp.BaseResponse;
import com.oxo.ball.bean.dto.resp.SearchResponse;
import com.oxo.ball.contant.RedisKeyContant;
import com.oxo.ball.mapper.BallBetMapper;
import com.oxo.ball.service.IMessageQueueService;
import com.oxo.ball.service.admin.IBallBalanceChangeService;
import com.oxo.ball.service.admin.IBallBetService;
import com.oxo.ball.service.admin.IBallGameLossPerCentService;
import com.oxo.ball.service.admin.IBallSystemConfigService;
import com.oxo.ball.service.impl.BasePlayerService;
import com.oxo.ball.service.player.IPlayerBetService;
import com.oxo.ball.service.player.IPlayerGameService;
import com.oxo.ball.service.player.IPlayerService;
import com.oxo.ball.utils.BigDecimalUtil;
import com.oxo.ball.utils.RedisUtil;
import com.oxo.ball.utils.ResponseMessageUtil;
import com.oxo.ball.utils.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class PlayerBetServiceImpl extends ServiceImpl<BallBetMapper, BallBet> implements IPlayerBetService {

    @Resource
    IPlayerGameService gameService;
    @Resource
    IBallGameLossPerCentService gameLossPerCentService;
    @Resource
    IPlayerService playerService;
    @Resource
    BasePlayerService basePlayerService;
    @Resource
    IBallBalanceChangeService ballBalanceChangeService;
    @Resource
    RedisUtil redisUtil;
    @Resource
    IMessageQueueService messageQueueService;
    @Autowired
    IBallSystemConfigService systemConfigService;
    @Autowired
    IBallBetService ballBetService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse gameBet(BetRequest betRequest, BallPlayer player) throws SQLException {
        //查询游戏
        BallGame game = gameService.findById(betRequest.getGameId());
        if (game == null) {
            return BaseResponse.failedWithData(BaseResponse.FAIL_FORM_SUBMIT,
                    ResponseMessageUtil.responseMessage("", "gameNotFound"));
//            return BaseResponse.failedWithMsg("赛事不存在~");
        }
        BallGameLossPerCent odds = gameLossPerCentService.findById(betRequest.getOddsId());
        if (odds == null) {
//            return BaseResponse.failedWithMsg("赔率不存在~无法下注~");
            return BaseResponse.failedWithData(BaseResponse.FAIL_FORM_SUBMIT,
                    ResponseMessageUtil.responseMessage("", "oddsNotFound"));
        }
        //查询赔率
        //查询玩家
        betRequest.setMoney(betRequest.getMoney() * BigDecimalUtil.PLAYER_MONEY_UNIT);
        //判定账户余额
        if (player.getBalance() < betRequest.getMoney()) {
//            return BaseResponse.failedWithMsg("账号余额不足,无法下注~");
            return BaseResponse.failedWithData(BaseResponse.FAIL_FORM_SUBMIT,
                    ResponseMessageUtil.responseMessage("", "balanceNotEnough"));
        }
        //判定赛事是否可用

        if (game.getStatus() == 2) {
//            return BaseResponse.failedWithMsg("赛事已关闭~,无法下注~");
            return BaseResponse.failedWithData(BaseResponse.FAIL_FORM_SUBMIT,
                    ResponseMessageUtil.responseMessage("", "gameClosed"));
        }
        if (game.getGameStatus() == 2) {
//            return BaseResponse.failedWithMsg("赛事已开始,无法下注~");
            return BaseResponse.failedWithData(BaseResponse.FAIL_FORM_SUBMIT,
                    ResponseMessageUtil.responseMessage("", "gameStarted"));
        }
        if (game.getGameStatus() == 3) {
//            return BaseResponse.failedWithMsg("赛事已结束,无法下注~");
            return BaseResponse.failedWithData(BaseResponse.FAIL_FORM_SUBMIT,
                    ResponseMessageUtil.responseMessage("", "gameFinished"));
        }

        //扣队账号余额
        BallSystemConfig systemConfig = systemConfigService.getSystemConfig();
        BallPlayer edit = BallPlayer.builder()
                .version(player.getVersion())
                .balance(player.getBalance() - betRequest.getMoney())
                //累计投注
                .accumulativeBet((player.getAccumulativeBet() == null ? 0 : player.getAccumulativeBet()) + player.getAccumulativeBet())
                .build();
        edit.setId(player.getId());
        if (systemConfig.getRegisterIfNeedVerificationCode() != null && systemConfig.getRegisterIfNeedVerificationCode() > 0) {
            //累计打码量,查询配置,是否需要*比例
            edit.setCumulativeQr((player.getCumulativeQr() == null ? 0 : player.getCumulativeQr()) + betRequest.getMoney() * systemConfig.getRechargeCodeConversionRate());
        } else {
            edit.setCumulativeQr((player.getCumulativeQr() == null ? 0 : player.getCumulativeQr()) + betRequest.getMoney());
        }
        if (systemConfig.getCaptchaThreshold() != null && systemConfig.getCaptchaThreshold() > 0) {
            //离下次提现所需captchaThreshold要打码量,如果当前打码量>设置的量,则为0,否则为相差数
            if (edit.getCumulativeQr() > systemConfig.getCaptchaThreshold()) {
                edit.setNeedQr(0L);
            } else {
                edit.setNeedQr(systemConfig.getCaptchaThreshold() - edit.getCumulativeQr());
            }
        }

        while (true) {
            boolean b = basePlayerService.editAndClearCache(edit, player);
            if (b) {
                break;
            } else {
                //更新失败再次判定余额是否足够,
                player = basePlayerService.findById(player.getId());
                edit.setVersion(player.getVersion());
                if (player.getBalance() < betRequest.getMoney()) {
//                    return BaseResponse.failedWithMsg("账号余额不足,无法下注~");
                    return BaseResponse.failedWithData(BaseResponse.FAIL_FORM_SUBMIT,
                            ResponseMessageUtil.responseMessage("", "balanceNotEnough"));
                }
            }
        }
        String remark = game.getAllianceName() + ":"
                + game.getMainName()
                + ":" + game.getGuestName()
                + ":" + odds.getScoreHome() + "-" + odds.getScoreAway()
                + ":[" + (betRequest.getMoney() / BigDecimalUtil.PLAYER_MONEY_UNIT) + "]";
        //保存下注数据
        BallBet save = BallBet.builder()
                .gameId(betRequest.getGameId())
                .betMoney(betRequest.getMoney())
                .playerId(player.getId())
                .gameLossPerCentId(betRequest.getOddsId())
                //TODO 下注手续费
                .handMoney(0L)
                .winningAmount(0L)
                .status(1)
                .betType(betRequest.getType())
                .orderNo(Long.parseLong(TimeUtil.dateFormat(new Date(), TimeUtil.TIME_DB_YY_MM_DD) + getDayOrderNo()))
                .remark(remark)
                .build();
        save.setCreatedAt(System.currentTimeMillis());
        boolean isSucc = save(save);
        //TODO 下注后账变日志
        BallBalanceChange balanceChange = BallBalanceChange.builder()
                .playerId(player.getId())
                .createdAt(System.currentTimeMillis())
                .changeMoney(-betRequest.getMoney())
                .initMoney(player.getBalance())
                .dnedMoney(edit.getBalance())
                .balanceChangeType(3)
                .remark(remark)
                .build();
        ballBalanceChangeService.insert(balanceChange);
        //下注日志
        messageQueueService.putMessage(MessageQueueDTO.builder()
                .type(MessageQueueDTO.TYPE_LOG_BET)
                .data(MessageQueueBet.builder()
                        .ballPlayer(player)
                        .betContent(balanceChange.getRemark())
                        .ip(player.getIp())
                        .orderId(save.getOrderNo())
                        .build())
                .build());
        if (!isSucc) {
            throw new SQLException();
        }
        return BaseResponse.successWithMsg("ok");
    }


    @Override
    public SearchResponse<BallBet> search(PlayerBetRequest queryParam, BallPlayer ballPlayer, Integer pageNo, Integer pageSize) {
        SearchResponse<BallBet> response = new SearchResponse<>();
        Page<BallBet> page = new Page<>(pageNo, pageSize);
        QueryWrapper<BallBet> query = new QueryWrapper<>();
        /**
         * 过滤条件
         * 一.玩家ID
         * 二.时间 今天，昨天，近7日，近10日，近30日
         * 三.类型
         */
        if (queryParam.getTime() != null) {
            switch (queryParam.getTime()) {
                case 1:
                    query.ge("created_at", TimeUtil.getDayBegin().getTime());
                    query.le("created_at", TimeUtil.getDayEnd().getTime());
                    break;
                case 2:
                    query.ge("created_at", TimeUtil.getBeginDayOfYesterday().getTime());
                    query.le("created_at", TimeUtil.getEndDayOfYesterday().getTime());
                    break;
                case 3:
                    query.ge("created_at", TimeUtil.getDayBegin().getTime()-7*TimeUtil.TIME_ONE_DAY);
                    query.le("created_at", TimeUtil.getDayEnd().getTime());
                    break;
                case 4:
                    query.ge("created_at", TimeUtil.getDayBegin().getTime()-10*TimeUtil.TIME_ONE_DAY);
                    query.le("created_at", TimeUtil.getDayEnd().getTime());
                    break;
                case 5:
                    query.ge("created_at", TimeUtil.getDayBegin().getTime()-30*TimeUtil.TIME_ONE_DAY);
                    query.le("created_at", TimeUtil.getDayEnd().getTime());
                    break;
                default:
                    break;
            }
        }
        if(queryParam.getType()!=null){
            if(queryParam.getType()==2){
                query.eq("bet_type",queryParam.getType());
            }
        }
        query.eq("player_id", ballPlayer.getId());
        //先ID降序再top升序
        query.orderByDesc("id");
        IPage<BallBet> pages = page(page, query);
        response.setPageNo(pages.getCurrent());
        response.setPageSize(pages.getSize());
        response.setTotalCount(pages.getTotal());
        response.setTotalPage(pages.getPages());
        response.setResults(pages.getRecords());
        return response;
    }

    @Override
    public Long getDayOrderNo() {
        if (redisUtil.get(RedisKeyContant.BET_ORDER_NO) == null) {
            redisUtil.set(RedisKeyContant.BET_ORDER_NO, 1000000);
        }
        long incr = redisUtil.incr(RedisKeyContant.BET_ORDER_NO, 1);
        return incr;
    }

    @Override
    public void clearDayOrderNo() {
        redisUtil.del(RedisKeyContant.BET_ORDER_NO);
    }

    @Override
    public BaseResponse gameBetPrepare(BetPreRequest betRequest, BallPlayer currentPlayer) {
        //账户余额
        Map<String, Object> data = new HashMap<>();
        data.put("balance", currentPlayer.getBalance());
        //手续费
        BallSystemConfig systemConfig = systemConfigService.getSystemConfig();
        data.put("betHandMoneyRate", systemConfig.getBetHandMoneyRate());
        data.put("fastMoney", systemConfig.getFastMoney());
        //报酬率
        //TODO 报酬率先返回赔率了
        BallGameLossPerCent gameLossPerCent = gameLossPerCentService.findById(betRequest.getOddsId());
        data.put("rateOfReturn", gameLossPerCent.getLossPerCent());
        //赛事
        data.put("game", gameService.findById(betRequest.getGameId()));
        //赔率
        data.put("lossPerCent", gameLossPerCent);
        //TODO 预备下注说明,生产时删除
        data.put("explain", "字段说明:[betHandMoneyRate:手续费,需要除100?1000?],[rateOfReturn:报酬率,不懂先返回赔率了],[game:游戏],[lossPerCent]赔率");
        return BaseResponse.successWithData(data);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse unbet(Long betId, BallPlayer currentUser) {
        BallSystemConfig systemConfig = systemConfigService.getSystemConfig();
        //撤消订单,账户加余额,减打码量
        BallBet byId = ballBetService.findById(betId);
        if(byId==null){
            return BaseResponse.failedWithData(BaseResponse.FAIL_FORM_SUBMIT,
                    ResponseMessageUtil.responseMessage("", "orderNotFound"));
        }
        if(byId.getStatus()!=1){
            return BaseResponse.failedWithData(BaseResponse.FAIL_FORM_SUBMIT,
                    ResponseMessageUtil.responseMessage("", "orderCatUnbet"));
        }
        //设置为撤消
        BallBet edit = BallBet.builder()
                .status(3)
                .build();
        edit.setId(byId.getId());
        ballBetService.edit(edit);

        BallPlayer editPlayer = BallPlayer.builder()
                .version(currentUser.getVersion())
                .balance(currentUser.getBalance()+byId.getBetMoney())
                //累计下注撤回
                .accumulativeBet(currentUser.getAccumulativeBet() - byId.getBetMoney())
                .build();
        editPlayer.setId(currentUser.getId());
        //打码量撤回
        if(systemConfig.getRegisterIfNeedVerificationCode()!=null&&systemConfig.getRegisterIfNeedVerificationCode()>0){
            //累计打码量,查询配置,是否需要*比例
            editPlayer.setCumulativeQr(currentUser.getCumulativeQr()-byId.getBetMoney()*systemConfig.getRechargeCodeConversionRate());
        }else{
            editPlayer.setCumulativeQr(currentUser.getCumulativeQr()-byId.getBetMoney());
        }
        //距离下次打码量加回
        if (systemConfig.getCaptchaThreshold() != null && systemConfig.getCaptchaThreshold() > 0) {
            //离下次提现所需captchaThreshold要打码量,如果当前打码量>设置的量,则为0,否则为相差数
            if (currentUser.getCumulativeQr() > systemConfig.getCaptchaThreshold()) {
                currentUser.setNeedQr(0L);
            } else {
                currentUser.setNeedQr(systemConfig.getCaptchaThreshold() - currentUser.getCumulativeQr());
            }
        }
        while(true){
            boolean b = basePlayerService.editAndClearCache(editPlayer, currentUser);
            if(b){
                //插入账变
                ballBalanceChangeService.insert(BallBalanceChange.builder()
                        .playerId(currentUser.getId())
                        .initMoney(currentUser.getBalance())
                        .changeMoney(byId.getBetMoney())
                        .dnedMoney(editPlayer.getBalance())
                        .createdAt(System.currentTimeMillis())
                        //撤单状态
                        .balanceChangeType(7)
                        .build());
                return BaseResponse.successWithMsg("ok");
            }else{
                //更新失败再次更新
                currentUser = basePlayerService.findById(currentUser.getId());
                editPlayer.setVersion(currentUser.getVersion());
                editPlayer.setBalance(currentUser.getBalance()+byId.getBetMoney());
            }
        }
    }

    @Override
    public BaseResponse betInfo(Long betId, BallPlayer currentUser) {
        BallBet byId = ballBetService.findById(betId);
        if(byId==null){
            return BaseResponse.failedWithData(BaseResponse.FAIL_FORM_SUBMIT,
                    ResponseMessageUtil.responseMessage("", "orderNotFound"));
        }
        Map<String,Object> data = new HashMap<>();
        data.put("betinfo",byId);
        //查赔率
        BallGameLossPerCent gameLossPerCent = gameLossPerCentService.findById(byId.getGameLossPerCentId());
        data.put("odds",gameLossPerCent);
        if(gameLossPerCent!=null){
            BallGame game = gameService.findById(gameLossPerCent.getGameId());
            data.put("game",game);
        }
        return BaseResponse.successWithData(data);
    }

    @Override
    public boolean edit(BallBet edit) {
        return updateById(edit);
    }
}
