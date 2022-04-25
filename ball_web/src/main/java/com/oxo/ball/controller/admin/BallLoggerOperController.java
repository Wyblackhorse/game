package com.oxo.ball.controller.admin;

import com.oxo.ball.bean.dao.BallLoggerBet;
import com.oxo.ball.bean.dao.BallLoggerOper;
import com.oxo.ball.bean.dto.resp.BaseResponse;
import com.oxo.ball.bean.dto.resp.SearchResponse;
import com.oxo.ball.service.admin.IBallLoggerBetService;
import com.oxo.ball.service.admin.IBallLoggerOperService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 日志表 前端控制器
 * </p>
 *
 * @author oxo_jy
 * @since 2022-04-13
 */
@RestController
@RequestMapping("/ball/log/operate")
public class BallLoggerOperController {
    @Resource
    IBallLoggerOperService loggerService;
    @PostMapping
    public Object index(BallLoggerOper query,
                        @RequestParam(defaultValue = "1")Integer pageNo,
                        @RequestParam(defaultValue = "20") Integer pageSize){
        SearchResponse<BallLoggerOper> search = loggerService.search(query, pageNo, pageSize);
        return BaseResponse.successWithData(search);
    }
}
