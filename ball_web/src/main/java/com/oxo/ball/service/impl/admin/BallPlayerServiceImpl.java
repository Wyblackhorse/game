package com.oxo.ball.service.impl.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.oxo.ball.bean.dao.BallBalanceChange;
import com.oxo.ball.bean.dao.BallPlayer;
import com.oxo.ball.bean.dto.resp.SearchResponse;
import com.oxo.ball.mapper.BallPlayerMapper;
import com.oxo.ball.service.admin.IBallBalanceChangeService;
import com.oxo.ball.service.admin.IBallPlayerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oxo.ball.service.impl.BasePlayerService;
import com.oxo.ball.utils.BigDecimalUtil;
import com.oxo.ball.utils.PasswordUtil;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 玩家账号 服务实现类
 * </p>
 *
 * @author oxo_jy
 * @since 2022-04-13
 */
@Service
public class BallPlayerServiceImpl extends ServiceImpl<BallPlayerMapper, BallPlayer> implements IBallPlayerService {

    @Resource
    BasePlayerService playerService;
    @Resource
    IBallBalanceChangeService ballBalanceChangeService;

    @Override
    public SearchResponse<BallPlayer> search(BallPlayer paramQuery, Integer pageNo, Integer pageSize) {
        SearchResponse<BallPlayer> response = new SearchResponse<>();

        Page<BallPlayer> page = new Page<>(pageNo, pageSize);
        QueryWrapper<BallPlayer> query = new QueryWrapper<>();

        IPage<BallPlayer> pages = page(page, query);
        response.setPageNo(pages.getCurrent());
        response.setPageSize(pages.getSize());
        response.setTotalCount(pages.getTotal());
        response.setTotalPage(pages.getPages());
        response.setResults(pages.getRecords());

        return response;
    }

    @Override
    public BallPlayer insert(BallPlayer ballPlayer) {
        return null;
    }

    @Override
    public Boolean edit(BallPlayer ballPlayer) {
        BallPlayer byId = playerService.findById(ballPlayer.getId());
        boolean b = playerService.editAndClearCache(ballPlayer, byId);
        return b;
    }

    @Override
    public Boolean editPwd(BallPlayer ballPlayer) {
        BallPlayer byId = playerService.findById(ballPlayer.getId());
        BallPlayer edit = BallPlayer.builder()
                .password(PasswordUtil.genPasswordMd5(ballPlayer.getEditPwd()))
                .build();
        edit.setId(ballPlayer.getId());
        boolean b = playerService.editAndClearCache(edit, byId);
        return b;
    }

    @Override
    public Boolean editPayPwd(BallPlayer ballPlayer) {
//        BallPlayer byId = playerService.findById(ballPlayer.getId());
//        BallPlayer edit = BallPlayer.builder()
//                .pa(PasswordUtil.genPasswordMd5(ballPlayer.getPassword()))
//                .build();
//        edit.setId(ballPlayer.getId());
//        boolean b = playerService.editAndClearCache(edit, byId);
        return false;
    }

    @Override
    public Boolean editStatus(BallPlayer ballPlayer) {
        BallPlayer byId = playerService.findById(ballPlayer.getId());
        BallPlayer edit = BallPlayer.builder()
                .status(ballPlayer.getStatus()==1?2:1)
                .build();
        edit.setId(ballPlayer.getId());
        boolean b = playerService.editAndClearCache(edit, byId);
        return b;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean editAddBalance(BallPlayer ballPlayer) {
        BallPlayer byId = playerService.findById(ballPlayer.getId());
        BallPlayer edit = BallPlayer.builder()
                .balance(byId.getBalance()+ballPlayer.getBalance()*BigDecimalUtil.PLAYER_MONEY_UNIT)
                .build();
        edit.setId(ballPlayer.getId());
        while(true){
            boolean b = playerService.editAndClearCache(edit, byId);
            if(b){
                //插入账变
                ballBalanceChangeService.insert(BallBalanceChange.builder()
                        .playerId(byId.getId())
                        .initMoney(byId.getBalance())
                        .changeMoney(ballPlayer.getBalance()*BigDecimalUtil.PLAYER_MONEY_UNIT)
                        .dnedMoney(edit.getBalance())
                        .remark("管理员上分")
                        .createdAt(System.currentTimeMillis())
                        .balanceChangeType(6)
                        .build());
                return b;
            }else{
                //更新失败再次判定余额是否足够,
                byId = playerService.findById(ballPlayer.getId());
                edit.setVersion(byId.getVersion());
                edit.setBalance(byId.getBalance()+ballPlayer.getBalance()*BigDecimalUtil.PLAYER_MONEY_UNIT);
            }
        }
    }

    @Override
    public Boolean editCaptchaPass(BallPlayer ballPlayer) {
//        BallPlayer byId = playerService.findById(ballPlayer.getId());
//        BallPlayer edit = BallPlayer.builder()
//                .status(ballPlayer.getStatus()==1?2:1)
//                .build();
//        edit.setId(ballPlayer.getId());
//        boolean b = playerService.editAndClearCache(edit, byId);
        return false;
    }



    @Override
    public Boolean info(BallPlayer ballPlayer) {
        return null;
    }

    @Override
    public SearchResponse<BallBalanceChange> log(BallBalanceChange balanceChange, Integer pageNo, Integer pageSize) {
        return null;
    }


}
