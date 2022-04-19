package com.oxo.ball.controller.admin;

import com.oxo.ball.bean.dao.BallSystemNotice;
import com.oxo.ball.bean.dto.resp.BaseResponse;
import com.oxo.ball.bean.dto.resp.SearchResponse;
import com.oxo.ball.service.admin.IBallSystemNoticeService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 系统公告 前端控制器
 * </p>
 *
 * @author oxo_jy
 * @since 2022-04-13
 */
@RestController
@RequestMapping("/ball/operation/notice")
public class BallSystemNoticeController {
    @Resource
    IBallSystemNoticeService noticeService;
    @PostMapping
    public Object index(BallSystemNotice query,
                        @RequestParam(defaultValue = "1")Integer pageNo,
                        @RequestParam(defaultValue = "20") Integer pageSize){
        SearchResponse<BallSystemNotice> search = noticeService.search(query, pageNo, pageSize);
        return BaseResponse.successWithData(search);
    }
    @PostMapping("add")
    public Object add(@RequestBody BallSystemNotice sysUserRequest){
        BallSystemNotice insert = new BallSystemNotice();
        try {
            insert = noticeService.insert(sysUserRequest);
        }catch (Exception ex){
            ex.printStackTrace();
        }

        return insert.getId()!=null?BaseResponse.successWithData(insert):BaseResponse.failedWithMsg("增加失败~");
    }
    @PostMapping("edit")
    public Object editSave(@RequestBody BallSystemNotice ballPlayer){
        Boolean aBoolean = noticeService.edit(ballPlayer);
        return  aBoolean?BaseResponse.SUCCESS:BaseResponse.failedWithMsg("修改失败~");
    }
    @PostMapping("status")
    public Object status(@RequestBody BallSystemNotice ballPlayer){
        Boolean aBoolean = noticeService.status(ballPlayer);
        return  aBoolean?BaseResponse.SUCCESS:BaseResponse.failedWithMsg("修改失败~");
    }
    @GetMapping("del")
    public Object del(@RequestParam("id") Long id){
        Boolean delete = noticeService.delete(id);
        return delete?BaseResponse.SUCCESS:BaseResponse.failedWithMsg("删除失败~");
    }
}
