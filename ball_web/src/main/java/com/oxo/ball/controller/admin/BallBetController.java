package com.oxo.ball.controller.admin;

import com.oxo.ball.bean.dao.BallBet;
import com.oxo.ball.bean.dto.resp.BaseResponse;
import com.oxo.ball.bean.dto.resp.SearchResponse;
import com.oxo.ball.service.admin.IBallBetService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 下注 前端控制器
 * </p>
 *
 * @author oxo_jy
 * @since 2022-04-13
 */
@RestController
@RequestMapping("/ball/bets")
public class BallBetController {

    @Resource
    IBallBetService betService;

    @PostMapping
    public Object index(BallBet query,
                        @RequestParam(defaultValue = "1")Integer pageNo,
                        @RequestParam(defaultValue = "20") Integer pageSize){
        SearchResponse<BallBet> search = betService.search(query, pageNo, pageSize);
        return BaseResponse.successWithData(search);
    }

    @PostMapping("undo")
    public Object undo(@RequestBody BallBet query){
        BaseResponse res = betService.undo(query);
        return res;
    }
    @PostMapping("info")
    public Object info(@RequestBody BallBet query){
        BaseResponse res = betService.info(query);
        return res;
    }


}
