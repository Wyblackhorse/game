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
import com.oxo.ball.service.admin.IBallGameLossPerCentService;
import com.oxo.ball.service.admin.IBallSystemConfigService;
import com.oxo.ball.service.impl.BasePlayerService;
import com.oxo.ball.service.player.IPlayerBetService;
import com.oxo.ball.service.player.IPlayerGameService;
import com.oxo.ball.service.player.IPlayerService;
import com.oxo.ball.utils.BigDecimalUtil;
import com.oxo.ball.utils.RedisUtil;
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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse gameBet(BetRequest betRequest, BallPlayer player) throws SQLException {
        //查询游戏
        BallGame game = gameService.findById(betRequest.getGameId());
        if(game==null){
            return BaseResponse.failedWithMsg("赛事不存在~");
        }
        BallGameLossPerCent odds = gameLossPerCentService.findById(betRequest.getOddsId());
        if(odds==null){
            return BaseResponse.failedWithMsg("赔率不存在~无法下注~");
        }
        //查询赔率
        //查询玩家
        betRequest.setMoney(betRequest.getMoney()*BigDecimalUtil.PLAYER_MONEY_UNIT);
        //判定账户余额
        if(player.getBalance()<betRequest.getMoney()){
            return BaseResponse.failedWithMsg("账号余额不足,无法下注~");
        }
        //判定赛事是否可用

        if(game.getStatus()==2 ){
            return BaseResponse.failedWithMsg("赛事已关闭~,无法下注~");
        }
        if(game.getGameStatus()==2){
            return BaseResponse.failedWithMsg("赛事已开始,无法下注~");
        }
        if(game.getGameStatus()==3){
            return BaseResponse.failedWithMsg("赛事已结束,无法下注~");
        }

        //扣队账号余额
        BallPlayer edit = BallPlayer.builder()
                .version(player.getVersion())
                .balance(player.getBalance()-betRequest.getMoney())
                .build();
        edit.setId(player.getId());
        while(true){
            boolean b = basePlayerService.editAndClearCache(edit, player);
            if(b){
                break;
            }else{
                //更新失败再次判定余额是否足够,
                player = basePlayerService.findById(player.getId());
                edit.setVersion(player.getVersion());
                if(player.getBalance()<betRequest.getMoney()){
                    return BaseResponse.failedWithMsg("账号余额不足,无法下注~");
                }
            }
        }
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
                .orderNo(Long.parseLong(TimeUtil.dateFormat(new Date(),TimeUtil.TIME_DB_YY_MM_DD)+getDayOrderNo()))
                .build();
        save.setCreatedAt(System.currentTimeMillis());
        boolean isSucc = save(save);
        //TODO 下注后账变日志
        BallBalanceChange balanceChange = BallBalanceChange.builder()
                .playerId(player.getId())
                .createdAt(System.currentTimeMillis())
                .changeMoney(betRequest.getMoney())
                .initMoney(player.getBalance())
                .dnedMoney(edit.getBalance())
                .balanceChangeType(3)
                .remark(game.getAllianceName() + ":"
                        + game.getMainName()
                        + ":" + game.getGuestName()
                        + ":" + odds.getScore()
                        + ":投注[" + betRequest.getMoney() / BigDecimalUtil.PLAYER_MONEY_UNIT + "]")
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
        if(!isSucc){
            throw new SQLException();
        }
        return BaseResponse.successWithMsg("下注成功~");
    }


    @Override
    public SearchResponse<BallBet> search(PlayerBetRequest queryParam, BallPlayer ballPlayer, Integer pageNo, Integer pageSize) {
        SearchResponse<BallBet> response = new SearchResponse<>();
        Page<BallBet> page = new Page<>(pageNo, pageSize);
        QueryWrapper<BallBet> query = new QueryWrapper<>();
        /**
         * 过滤条件
         * 一.玩家ID
         */
        query.eq("player_id",ballPlayer.getId());
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
        if(redisUtil.get(RedisKeyContant.BET_ORDER_NO)==null){
            redisUtil.set(RedisKeyContant.BET_ORDER_NO,1000000);
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
        Map<String,Object> data = new HashMap<>();
        data.put("balance",currentPlayer.getBalance());
        //手续费
        BallSystemConfig systemConfig = systemConfigService.getSystemConfig();
        data.put("betHandMoneyRate",systemConfig.getBetHandMoneyRate());
        data.put("fastMoney",systemConfig.getFastMoney());
        //报酬率
        //TODO 报酬率先返回赔率了
        BallGameLossPerCent gameLossPerCent = gameLossPerCentService.findById(betRequest.getOddsId());
        data.put("rateOfReturn",gameLossPerCent.getLossPerCent());
        //赛事
        data.put("game",gameService.findById(betRequest.getGameId()));
        //赔率
        data.put("lossPerCent",gameLossPerCent);
        //TODO 预备下注说明,生产时删除
        data.put("explain","字段说明:[betHandMoneyRate:手续费,需要除100?1000?],[rateOfReturn:报酬率,不懂先返回赔率了],[game:游戏],[lossPerCent]赔率");
        return BaseResponse.successWithData(data);
    }
}
