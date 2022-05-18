package com.oxo.ball.service.impl.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.oxo.ball.bean.dao.*;
import com.oxo.ball.bean.dto.resp.BaseResponse;
import com.oxo.ball.bean.dto.resp.SearchResponse;
import com.oxo.ball.mapper.BallBetMapper;
import com.oxo.ball.service.IBasePlayerService;
import com.oxo.ball.service.admin.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oxo.ball.service.player.IPlayerBetService;
import com.oxo.ball.service.player.IPlayerGameService;
import com.oxo.ball.utils.BigDecimalUtil;
import com.oxo.ball.utils.ThreadPoolUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 下注 服务实现类
 * </p>
 *
 * @author oxo_jy
 * @since 2022-04-13
 */
@Service
public class BallBetServiceImpl extends ServiceImpl<BallBetMapper, BallBet> implements IBallBetService {

    @Autowired
    IPlayerBetService playerBetService;
    @Autowired
    IBasePlayerService basePlayerService;
    @Autowired
    IBallGameService gameService;
    @Autowired
    IPlayerGameService playerGameService;
    @Autowired
    IBallGameLossPerCentService lossPerCentService;
    @Autowired
    private IBallBalanceChangeService ballBalanceChangeService;

    @Override
    public SearchResponse<BallBet> search(BallBet queryParam, Integer pageNo, Integer pageSize) {
        SearchResponse<BallBet> response = new SearchResponse<>();
        Page<BallBet> page = new Page<>(pageNo, pageSize);
        QueryWrapper<BallBet> query = new QueryWrapper<>();
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
    public BallBet findById(Long id) {
        //TODO 按ID查询订单缓存
        return getById(id);
    }

    @Override
    public List<BallBet> findByGameId(Long id,int status) {
        /**
         * 0 查询未结算
         * 1 查询已结算
         */
        QueryWrapper query = new QueryWrapper();
        query.eq("game_id", id);
        //只查询未结算的
        query.eq("status", status);
        return list(query);
    }

    @Override
    public Boolean edit(BallBet ballBet) {
        return updateById(ballBet);
    }

    @Override
    public BaseResponse undo(BallBet query) {
        BallBet bet = findById(query.getId());
        BallPlayer player = basePlayerService.findById(bet.getPlayerId());
        BaseResponse unbet = playerBetService.unbet(query.getId(), player);
        return unbet;
    }

    @Override
    public BaseResponse info(BallBet query) {
        BallBet bet = findById(query.getId());
        BallPlayer player = basePlayerService.findById(bet.getPlayerId());
        BaseResponse response = playerBetService.betInfo(bet.getId(), player);
        Map<String, Object> data = (Map<String, Object>) response.getData();
        data.put("player", player);
        return response;
    }

    @Override
    public void betOpen(Long id) {
        //结算
        BallGame game = playerGameService.findById(id);
        //查询当前游戏所有下注
        List<BallBet> betsList = findByGameId(game.getId(),1);
        if (betsList != null && !betsList.isEmpty()) {
            Map<Long, BallGameLossPerCent> lossPerCentMap = new HashMap<>();
            for (BallBet bet : betsList) {
                BallGameLossPerCent lossPerCent = lossPerCentMap.get(bet.getGameLossPerCentId());
                if (lossPerCent == null) {
                    lossPerCent = lossPerCentService.findById(bet.getGameLossPerCentId());
                    lossPerCentMap.put(bet.getGameLossPerCentId(), lossPerCent);
                }
                if (lossPerCent.getStatus() != 1) {
                    //如果赔率不可用，订单无法结算
                    continue;
                }
                ThreadPoolUtil.exec(new OpenBetThread(lossPerCent, bet, playerBetService,
                        game, basePlayerService, ballBalanceChangeService));
            }
        }
    }

    @Override
    public BaseResponse betRecount(Long id) {
        //重算
        BallGame game = playerGameService.findById(id);
        //查询当前游戏所有下注
        List<BallBet> betsList = findByGameId(game.getId(),2);
        if (betsList != null && !betsList.isEmpty()) {
            Map<Long, BallGameLossPerCent> lossPerCentMap = new HashMap<>();
            for (BallBet bet : betsList) {
                BallGameLossPerCent lossPerCent = lossPerCentMap.get(bet.getGameLossPerCentId());
                if (lossPerCent == null) {
                    lossPerCent = lossPerCentService.findById(bet.getGameLossPerCentId());
                    lossPerCentMap.put(bet.getGameLossPerCentId(), lossPerCent);
                }
                if (lossPerCent==null || lossPerCent.getStatus() != 1) {
                    //如果赔率不可用，订单无法结算
                    continue;
                }
                ThreadPoolUtil.exec(new BetRecountThread(lossPerCent, bet, playerBetService,
                        game, basePlayerService, ballBalanceChangeService));
            }
        }
        gameService.edit(BallGame.builder()
                .settlementType(3)
                .id(id)
                .build());
        return BaseResponse.successWithMsg("ok");
    }

    @Override
    public BaseResponse betRollback(Long id) {
        //回滚是指本期投注全部撤消
        //余额找回,累加数据减出,增加账变数据
        BallGame game = playerGameService.findById(id);
        //查询当前游戏所有下注
        List<BallBet> betsList = findByGameId(game.getId(), 2);
        if (betsList != null && !betsList.isEmpty()) {
            for (BallBet bet : betsList) {
                ThreadPoolUtil.exec(new BetRollbackThread(bet, playerBetService,
                        game, basePlayerService, ballBalanceChangeService));
            }
        }
        gameService.edit(BallGame.builder()
                .settlementType(2)
                .id(id)
                .build());
        return BaseResponse.successWithMsg("ok");
    }

    private static Long arithmeticBingo(Long betMoney, String oddsStr) {
        Double odds = Double.valueOf(oddsStr);
        double div = BigDecimalUtil.div(odds, 100);
        long bingo = Double.valueOf(BigDecimalUtil.mul(div, betMoney)).longValue();
        return bingo;
    }

    public static class OpenBetThread implements Runnable {


        private BallGameLossPerCent lossPerCent;
        private BallBet bet;
        private IPlayerBetService betService;
        private BallGame game;
        private IBasePlayerService playerService;
        private IBallBalanceChangeService ballBalanceChangeService;

        public OpenBetThread(BallGameLossPerCent lossPerCent, BallBet bet,
                             IPlayerBetService betService, BallGame game,
                             IBasePlayerService playerService,
                             IBallBalanceChangeService ballBalanceChangeService) {
            this.lossPerCent = lossPerCent;
            this.bet = bet;
            this.betService = betService;
            this.game = game;
            this.playerService = playerService;
            this.ballBalanceChangeService = ballBalanceChangeService;
        }


        @Override
        public void run() {
//是否中奖,正波需要匹配，反波是只要不是比分就中
            boolean bingo = false;
            String odds = null;
            if (bet.getBetType() == 1) {
                //下正波,带*,只需要比较一方分数
                if (lossPerCent.getScoreHome().equals("*")) {
                    //主场带*
                    bingo = lossPerCent.getScoreAway().equals(game.getGuestFull().toString());
                } else if (lossPerCent.getScoreAway().equals("*")) {
                    //客场带*
                    bingo = lossPerCent.getScoreHome().equals(game.getHomeFull().toString());
                } else {
                    //不带*
                    bingo = (lossPerCent.getScoreAway().equals(game.getGuestFull().toString())
                            && lossPerCent.getScoreHome().equals(game.getHomeFull().toString()));
                }
                odds = lossPerCent.getLossPerCent();
            } else {
                //下反波,只要和比分任意不一样就中
                if (lossPerCent.getScoreHome().equals("*")) {
                    //主场带*,只要主比分未下中,就中
                    bingo = !lossPerCent.getScoreAway().equals(game.getGuestFull().toString());
                } else if (lossPerCent.getScoreAway().equals("*")) {
                    //客场带*,只要主场比分未下中,就中
                    bingo = !lossPerCent.getScoreHome().equals(game.getHomeFull().toString());
                } else {
                    //不带*,任意未下中,就中
                    bingo = (!lossPerCent.getScoreAway().equals(game.getGuestFull().toString())
                            || !lossPerCent.getScoreHome().equals(game.getHomeFull().toString()));
                }
                odds = lossPerCent.getAntiPerCent();
            }
            BallBet edit = BallBet.builder()
                    .status(2)
                    .build();
            edit.setId(bet.getId());
            if (bingo) {
                //计算中奖金额 ,下注+下注*赔率%
                Long oddsLong = arithmeticBingo(bet.getBetMoney(), odds);
                edit.setWinningAmount(oddsLong);
            } else {
                edit.setWinningAmount(0L);
            }
            boolean isSucc = betService.edit(edit);
            if (isSucc && bingo) {
                //更新账号余额+累计中奖金额
                //TODO 中奖返佣
                BallPlayer player = playerService.findById(bet.getPlayerId());
                while (true) {
                    BallPlayer editPlayer = BallPlayer.builder()
                            .balance(player.getBalance() + edit.getWinningAmount())
                            .version(player.getVersion())
                            .cumulativeWinning(player.getCumulativeWinning()+edit.getWinningAmount())
                            .build();
                    editPlayer.setId(player.getId());
                    boolean isSuccess = playerService.editAndClearCache(editPlayer, player);
                    if (isSuccess) {
                        //账变记录
                        ballBalanceChangeService.insert(BallBalanceChange.builder()
                                .playerId(player.getId())
                                .initMoney(player.getBalance())
                                .changeMoney(edit.getWinningAmount())
                                .dnedMoney(editPlayer.getBalance())
                                //key recharge_self
                                .createdAt(System.currentTimeMillis())
                                .balanceChangeType(4)
                                .build());
                        break;
                    }
                    player = playerService.findById(player.getId());
                }
            }
        }
    }

    public static class BetRecountThread implements Runnable {


        private BallGameLossPerCent lossPerCent;
        private BallBet bet;
        private IPlayerBetService betService;
        private BallGame game;
        private IBasePlayerService playerService;
        private IBallBalanceChangeService ballBalanceChangeService;

        public BetRecountThread(BallGameLossPerCent lossPerCent, BallBet bet,
                                IPlayerBetService betService, BallGame game,
                                IBasePlayerService playerService,
                                IBallBalanceChangeService ballBalanceChangeService) {
            this.lossPerCent = lossPerCent;
            this.bet = bet;
            this.betService = betService;
            this.game = game;
            this.playerService = playerService;
            this.ballBalanceChangeService = ballBalanceChangeService;
        }


        @Override
        public void run() {
            //是否中奖,正波需要匹配，反波是只要不是比分就中
            boolean bingo = false;
            String odds = null;
            if (bet.getBetType() == 1) {
                //下正波,带*,只需要比较一方分数
                if (lossPerCent.getScoreHome().equals("*")) {
                    //主场带*
                    bingo = lossPerCent.getScoreAway().equals(game.getGuestFull().toString());
                } else if (lossPerCent.getScoreAway().equals("*")) {
                    //客场带*
                    bingo = lossPerCent.getScoreHome().equals(game.getHomeFull().toString());
                } else {
                    //不带*
                    bingo = (lossPerCent.getScoreAway().equals(game.getGuestFull().toString())
                            && lossPerCent.getScoreHome().equals(game.getHomeFull().toString()));
                }
                odds = lossPerCent.getLossPerCent();
            } else {
                //下反波,只要和比分任意不一样就中
                if (lossPerCent.getScoreHome().equals("*")) {
                    //主场带*,只要主比分未下中,就中
                    bingo = !lossPerCent.getScoreAway().equals(game.getGuestFull().toString());
                } else if (lossPerCent.getScoreAway().equals("*")) {
                    //客场带*,只要主场比分未下中,就中
                    bingo = !lossPerCent.getScoreHome().equals(game.getHomeFull().toString());
                } else {
                    //不带*,任意未下中,就中
                    bingo = (!lossPerCent.getScoreAway().equals(game.getGuestFull().toString())
                            || !lossPerCent.getScoreHome().equals(game.getHomeFull().toString()));
                }
                odds = lossPerCent.getAntiPerCent();
            }
            BallBet edit = BallBet.builder()
                    .status(2)
                    .build();
            edit.setId(bet.getId());

            if (bingo) {
                //计算中奖金额 ,下注+下注*赔率%
                Long oddsLong = arithmeticBingo(bet.getBetMoney(), odds);
                edit.setWinningAmount(oddsLong);
            } else {
                edit.setWinningAmount(0L);
            }
            //重新算的差额
            long recountMoney = bet.getWinningAmount()-edit.getWinningAmount();
            //重算无变化,不需要有操作
            if(recountMoney==0){
                return;
            }
            boolean isSucc = betService.edit(edit);
            if (isSucc) {
                //更新账号余额+中奖金额
                // 1.中奖金额与原中奖金额的对比变化
                //    - 小于,扣除余额,增加账变
                //    - 大于,增加余额,增加账变
                BallPlayer player = playerService.findById(bet.getPlayerId());
                while (true) {
                    BallPlayer editPlayer = BallPlayer.builder()
                            .balance(player.getBalance() + recountMoney)
                            .cumulativeWinning(player.getCumulativeWinning()+recountMoney)
                            .version(player.getVersion())
                            .build();
                    editPlayer.setId(player.getId());
                    boolean isSuccess = playerService.editAndClearCache(editPlayer, player);
                    if (isSuccess) {
                        //账变记录
                        ballBalanceChangeService.insert(BallBalanceChange.builder()
                                .playerId(player.getId())
                                .initMoney(player.getBalance())
                                .changeMoney(recountMoney)
                                .dnedMoney(editPlayer.getBalance())
                                //key recharge_self
                                .createdAt(System.currentTimeMillis())
                                //盈利为4,扣除为9
                                .balanceChangeType(recountMoney>0?4:9)
                                .build());
                        break;
                    }
                    player = playerService.findById(player.getId());
                }
            }
        }
    }

    public static class BetRollbackThread implements Runnable {


        private BallBet bet;
        private IPlayerBetService betService;
        private BallGame game;
        private IBasePlayerService playerService;
        private IBallBalanceChangeService ballBalanceChangeService;

        public BetRollbackThread(BallBet bet,
                                IPlayerBetService betService, BallGame game,
                                IBasePlayerService playerService,
                                IBallBalanceChangeService ballBalanceChangeService) {
            this.bet = bet;
            this.betService = betService;
            this.game = game;
            this.playerService = playerService;
            this.ballBalanceChangeService = ballBalanceChangeService;
        }


        @Override
        public void run() {
            BallBet edit = BallBet.builder()
                    .status(4)
                    .winningAmount(0L)
                    .build();
            edit.setId(bet.getId());
            //加回投注金额减去中奖金额
            long rollbackMoney = Math.abs(bet.getBetMoney())-bet.getWinningAmount();
            boolean isSucc = betService.edit(edit);
            if (isSucc) {
                //更新账号余额,中奖金额
                BallPlayer player = playerService.findById(bet.getPlayerId());
                while (true) {
                    BallPlayer editPlayer = BallPlayer.builder()
                            .balance(player.getBalance() + rollbackMoney)
                            .cumulativeWinning(player.getCumulativeWinning()-bet.getWinningAmount())
                            .version(player.getVersion())
                            .build();
                    editPlayer.setId(player.getId());
                    boolean isSuccess = playerService.editAndClearCache(editPlayer, player);
                    if (isSuccess) {
                        //加回投注账变
                        ballBalanceChangeService.insert(BallBalanceChange.builder()
                                .playerId(player.getId())
                                .initMoney(player.getBalance())
                                .changeMoney(bet.getBetMoney())
                                .dnedMoney(player.getBalance()+bet.getBetMoney())
                                .createdAt(System.currentTimeMillis())
                                .balanceChangeType(14)
                                .build());
                        //如果中奖增加扣除账变
                        if(bet.getWinningAmount()>0){
                            ballBalanceChangeService.insert(BallBalanceChange.builder()
                                    .playerId(player.getId())
                                    .initMoney(player.getBalance()+bet.getBetMoney())
                                    .changeMoney(-bet.getWinningAmount())
                                    .dnedMoney(player.getBalance()+bet.getBetMoney()-bet.getWinningAmount())
                                    .createdAt(System.currentTimeMillis())
                                    .balanceChangeType(9)
                                    .build());
                        }
                        break;
                    }
                    player = playerService.findById(player.getId());
                }
            }
        }
    }
}
