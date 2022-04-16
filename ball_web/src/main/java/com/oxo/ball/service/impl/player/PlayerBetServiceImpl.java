package com.oxo.ball.service.impl.player;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oxo.ball.bean.dao.*;
import com.oxo.ball.bean.dto.req.player.BetRequest;
import com.oxo.ball.bean.dto.resp.BaseResponse;
import com.oxo.ball.mapper.BallBetMapper;
import com.oxo.ball.service.admin.IBallBalanceChangeService;
import com.oxo.ball.service.admin.IBallGameLossPerCentService;
import com.oxo.ball.service.impl.BasePlayerService;
import com.oxo.ball.service.player.IPlayerBetService;
import com.oxo.ball.service.player.IPlayerGameService;
import com.oxo.ball.service.player.IPlayerService;
import com.oxo.ball.utils.BigDecimalUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.SQLException;

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
                .build();
        save.setCreatedAt(System.currentTimeMillis());
        boolean isSucc = save(save);
        //TODO 下注后账变日志
        ballBalanceChangeService.save(BallBalanceChange.builder()
                .playerId(player.getId())
                .createdAt(System.currentTimeMillis())
                .changeMoney(betRequest.getMoney())
                .initMoney(player.getBalance())
                .dnedMoney(edit.getBalance())
                .balanceChangeType(3)
                .remark(game.getAllianceName()+":"
                        +game.getMainName()
                        +":"+game.getGuestName()
                        +":"+odds.getScore()
                        +":投注["+betRequest.getMoney()/BigDecimalUtil.PLAYER_MONEY_UNIT+"]")
                .build());
        if(!isSucc){
            throw new SQLException();
        }
        return BaseResponse.successWithMsg("下注成功~");
    }
}
