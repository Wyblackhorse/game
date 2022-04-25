package com.oxo.ball.controller.admin;

import com.oxo.ball.bean.dao.BallAnnouncement;
import com.oxo.ball.bean.dto.resp.BaseResponse;
import com.oxo.ball.bean.dto.resp.SearchResponse;
import com.oxo.ball.interceptor.MainOper;
import com.oxo.ball.interceptor.SubOper;
import com.oxo.ball.service.admin.IBallAnnouncementService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 轮播公告 前端控制器
 * </p>
 *
 * @author oxo_jy
 * @since 2022-04-13
 */
@RestController
@RequestMapping("/ball/operation/swiper")
@MainOper("运营管理-轮播广告")
public class BallAnnouncementController {
    @Resource
    IBallAnnouncementService announcementService;
    @PostMapping
    public Object index(BallAnnouncement query,
                        @RequestParam(defaultValue = "1")Integer pageNo,
                        @RequestParam(defaultValue = "20") Integer pageSize){
        SearchResponse<BallAnnouncement> search = announcementService.search(query, pageNo, pageSize);
        return BaseResponse.successWithData(search);
    }
    @SubOper("添加")
    @PostMapping("add")
    public Object add(@RequestBody BallAnnouncement sysUserRequest){
        BallAnnouncement insert = new BallAnnouncement();
        try {
            insert = announcementService.insert(sysUserRequest);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return insert.getId()!=null?BaseResponse.successWithData(insert):BaseResponse.failedWithMsg("增加失败~");
    }
    @SubOper("修改")
    @PostMapping("edit")
    public Object editSave(@RequestBody BallAnnouncement ballPlayer){
        Boolean aBoolean = announcementService.edit(ballPlayer);
        BaseResponse res = (aBoolean ? BaseResponse.SUCCESS : BaseResponse.failedWithMsg("修改失败~"));
        res.setRemark("id"+ballPlayer.getId());
        return res;
    }
    @SubOper("修改状态")
    @PostMapping("status")
    public Object status(@RequestBody BallAnnouncement ballPlayer){
        Boolean aBoolean = announcementService.status(ballPlayer);
        BaseResponse res = (aBoolean ? BaseResponse.SUCCESS : BaseResponse.failedWithMsg("修改失败~"));
        res.setRemark("id"+ballPlayer.getId()+"状态设置为"+ballPlayer.getStatus());
        return  res;
    }
    @SubOper("删除")
    @GetMapping("del")
    public Object del(@RequestParam("id") Long id){
        Boolean delete = announcementService.delete(id);
        return delete?BaseResponse.SUCCESS:BaseResponse.failedWithMsg("删除失败~");
    }
}
