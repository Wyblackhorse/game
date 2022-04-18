package com.oxo.ball.controller.admin;

import com.oxo.ball.bean.dao.BallGameLossPerCent;
import com.oxo.ball.bean.dto.resp.BaseResponse;
import com.oxo.ball.bean.dto.resp.SearchResponse;
import com.oxo.ball.service.admin.IBallGameLossPerCentService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 游戏赔率 前端控制器
 * </p>
 *
 * @author oxo_jy
 * @since 2022-04-13
 */
@RestController
@RequestMapping("/ball/odds")
public class BallGameLossPerCentController {
    @Resource
    IBallGameLossPerCentService gameLossPerCentService;
    @PostMapping
    public Object index(BallGameLossPerCent query,
                        @RequestParam(defaultValue = "1")Integer pageNo,
                        @RequestParam(defaultValue = "20") Integer pageSize){
        SearchResponse<BallGameLossPerCent> search = gameLossPerCentService.search(query, pageNo, pageSize);
        return BaseResponse.successWithData(search);
    }
}
