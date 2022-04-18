package com.oxo.ball.controller.admin;

import com.oxo.ball.bean.dao.BallSlideshow;
import com.oxo.ball.bean.dto.resp.BaseResponse;
import com.oxo.ball.bean.dto.resp.SearchResponse;
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
@RequestMapping("/ball/operation/swiper")
public class BallSlideshowController {
    @Resource
    IBallSlideshowService slideshowService;
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
        return insert.getId()!=null?BaseResponse.successWithData(insert):BaseResponse.failedWithMsg("增加失败~");
    }
    @PostMapping("edit")
    public Object editSave(@RequestBody BallSlideshow ballPlayer){
        Boolean aBoolean = slideshowService.edit(ballPlayer);
        return  aBoolean?BaseResponse.SUCCESS:BaseResponse.failedWithMsg("修改失败~");
    }
    @GetMapping("del")
    public Object del(@RequestParam("id") Long id){
        Boolean delete = slideshowService.delete(id);
        return delete?BaseResponse.SUCCESS:BaseResponse.failedWithMsg("删除失败~");
    }
}
