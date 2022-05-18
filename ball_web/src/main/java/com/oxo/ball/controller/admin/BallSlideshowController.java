package com.oxo.ball.controller.admin;

import com.oxo.ball.bean.dao.BallCommissionStrategy;
import com.oxo.ball.bean.dao.BallDepositPolicy;
import com.oxo.ball.bean.dao.BallSlideshow;
import com.oxo.ball.bean.dto.resp.BaseResponse;
import com.oxo.ball.bean.dto.resp.SearchResponse;
import com.oxo.ball.service.admin.IBallCommissionStrategyService;
import com.oxo.ball.service.admin.IBallDepositPolicyService;
import com.oxo.ball.service.admin.IBallSlideshowService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 轮播图 前端控制器
 * </p>
 *
 * @author oxo_jy
 * @since 2022-04-13
 */
@RestController
@RequestMapping("/ball/operation/banner")
public class BallSlideshowController {
    @Resource
    IBallSlideshowService slideshowService;
    @Resource
    IBallDepositPolicyService depositPolicyService;
    @Resource
    IBallCommissionStrategyService commissionStrategyService;
    @PostMapping
    public Object index(BallSlideshow query,
                        @RequestParam(defaultValue = "1")Integer pageNo,
                        @RequestParam(defaultValue = "20") Integer pageSize){
        SearchResponse<BallSlideshow> search = slideshowService.search(query, pageNo, pageSize);
        return BaseResponse.successWithData(search);
    }
    @PostMapping("add")
    public Object add(@RequestBody BallSlideshow sysUserRequest){
        BallSlideshow insert = new BallSlideshow();
        try {
            insert = slideshowService.insert(sysUserRequest);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return insert.getId()!=null?BaseResponse.successWithData(insert):BaseResponse.failedWithMsg("add error");
    }
    @PostMapping("edit")
    public Object editSave(@RequestBody BallSlideshow ballPlayer){
        Boolean aBoolean = slideshowService.edit(ballPlayer);
        return  aBoolean?BaseResponse.SUCCESS:BaseResponse.failedWithMsg("edit error");
    }
    @PostMapping("status")
    public Object status(@RequestBody BallSlideshow ballPlayer){
        Boolean aBoolean = slideshowService.status(ballPlayer);
        return  aBoolean?BaseResponse.SUCCESS:BaseResponse.failedWithMsg("edit error");
    }
    @GetMapping("del")
    public Object del(@RequestParam("id") Long id){
        Boolean delete = slideshowService.delete(id);
        return delete?BaseResponse.SUCCESS:BaseResponse.failedWithMsg("del error");
    }

    /**
     *查询存款策略
     * @return
     */
    @GetMapping
    public Object deposit(){
        SearchResponse<BallDepositPolicy> search = depositPolicyService.search(BallDepositPolicy.builder()
                .status(1)
                .build(), 1, 200);
        return BaseResponse.successWithData(search.getResults());
    }

    /**
     * 查询反佣策略
     * @return
     */
    @PutMapping()
    public Object commission(){
        SearchResponse<BallCommissionStrategy> search = commissionStrategyService.search(BallCommissionStrategy.builder()
                .status(1)
                .build(), 1, 200);
        return BaseResponse.successWithData(search.getResults());
    }
}
