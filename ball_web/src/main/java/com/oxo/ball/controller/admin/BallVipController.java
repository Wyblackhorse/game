package com.oxo.ball.controller.admin;

import com.oxo.ball.bean.dao.BallVip;
import com.oxo.ball.bean.dto.resp.BaseResponse;
import com.oxo.ball.bean.dto.resp.SearchResponse;
import com.oxo.ball.service.admin.IBallVipService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author oxo_jy
 * @since 2022-04-24
 */
@RestController
@RequestMapping("/ball/merchant/vip")
public class BallVipController {

    @Resource
    IBallVipService vipService;
    @PostMapping
    public Object index(BallVip query,
                        @RequestParam(defaultValue = "1")Integer pageNo,
                        @RequestParam(defaultValue = "20") Integer pageSize){
        SearchResponse<BallVip> search = vipService.search(query, pageNo, pageSize);
        return BaseResponse.successWithData(search);
    }
    @PostMapping("add")
    public Object add(@RequestBody BallVip ballVip){
        BallVip insert = new BallVip();
        try {
            insert = vipService.insert(ballVip);
        }catch (Exception ex){
            ex.printStackTrace();
        }

        return insert.getId()!=null?BaseResponse.successWithData(insert):BaseResponse.failedWithMsg("增加失败~");
    }
    @PostMapping("edit")
    public Object editSave(@RequestBody BallVip ballVip){
        Boolean aBoolean = vipService.edit(ballVip);
        return  aBoolean?BaseResponse.SUCCESS:BaseResponse.failedWithMsg("修改失败~");
    }
    @PostMapping("status")
    public Object status(@RequestBody BallVip ballVip){
        Boolean aBoolean = vipService.status(ballVip);
        return  aBoolean?BaseResponse.SUCCESS:BaseResponse.failedWithMsg("修改失败~");
    }
    @GetMapping("del")
    public Object del(@RequestParam("id") Long id){
        Boolean delete = vipService.delete(id);
        return delete?BaseResponse.SUCCESS:BaseResponse.failedWithMsg("删除失败~");
    }
}
