package com.oxo.ball.controller.player;

import com.oxo.ball.auth.TokenInvalidedException;
import com.oxo.ball.bean.dao.*;
import com.oxo.ball.bean.dto.req.player.BalanceChangeRequest;
import com.oxo.ball.bean.dto.req.player.PlayerBetRequest;
import com.oxo.ball.bean.dto.resp.BaseResponse;
import com.oxo.ball.bean.dto.resp.SearchResponse;
import com.oxo.ball.service.IBasePlayerService;
import com.oxo.ball.service.admin.*;
import com.oxo.ball.service.player.AuthPlayerService;
import com.oxo.ball.service.player.IPlayerBetService;
import com.oxo.ball.service.player.IPlayerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;


/**
 * <p>
 * 玩家账号 前端控制器
 * </p>
 *
 * @author oxo_jy
 * @since 2022-04-13
 */
@RestController
@RequestMapping("/player/home")
@Api(tags = "玩家 - 首页")
public class PlayerHomeController {

    @Resource
    IPlayerService playerService;
    @Resource
    IBasePlayerService basePlayerService;
    @Resource
    IBallBalanceChangeService ballBalanceChangeService;
    @Resource
    IPlayerBetService betService;
    @Resource
    IBallSlideshowService slideshowService;
    @Resource
    IBallSystemNoticeService noticeService;
    @Resource
    IBallGameService gameService;
    @Resource
    IBallAnnouncementService announcementService;


    @ApiOperation(
            value = "轮播图",
            notes = "轮播图" ,
            httpMethod = "GET")
    @GetMapping("slider")
    public Object slider(HttpServletRequest request) {
        SearchResponse<BallSlideshow> search = slideshowService.search(BallSlideshow.builder()
                .status(1)
                .build(), 1, 20);
        List<BallSlideshow> results = search.getResults();
        return BaseResponse.successWithData(results);
    }

    @ApiOperation(
            value = "滚动广告",
            notes = "滚动广告" ,
            httpMethod = "GET")
    @ApiImplicitParams({
    })
    @GetMapping("swiper")
    public Object swiper(HttpServletRequest request) throws TokenInvalidedException {
        SearchResponse<BallAnnouncement> search = announcementService.search(BallAnnouncement.builder()
                .status(1)
                .build(),1, 20);
        return BaseResponse.successWithData(search.getResults());
    }
    @ApiOperation(
            value = "系统公告",
            notes = "系统公告" ,
            httpMethod = "GET")
    @ApiImplicitParams({
    })
    @GetMapping("notice")
    public Object notice(HttpServletRequest request) throws TokenInvalidedException {
        SearchResponse<BallSystemNotice> search = noticeService.search(BallSystemNotice.builder()
                .status(1)
                .build(),1, 20);
        return BaseResponse.successWithData(search.getResults());
    }

    @ApiOperation(
            value = "热门赛事",
            notes = "热门赛事" ,
            httpMethod = "GET")
    @ApiImplicitParams({
    })
    @GetMapping("hot")
    public Object bets(HttpServletRequest request) throws TokenInvalidedException {
        SearchResponse<BallGame> search = gameService.search(BallGame.builder()
                .hot(1)
                .build(), 1, 20);
        return BaseResponse.successWithData(search.getResults());
    }
}
