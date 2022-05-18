package com.oxo.ball.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.oxo.ball.bean.dao.BallAdmin;
import com.oxo.ball.bean.dao.BallGroup;
import com.oxo.ball.bean.dto.req.SysUserEditRequest;
import com.oxo.ball.bean.dto.req.SysUserInsertRequest;
import com.oxo.ball.bean.dto.resp.BaseResponse;
import com.oxo.ball.bean.dto.resp.SearchResponse;
import com.oxo.ball.interceptor.MainOper;
import com.oxo.ball.interceptor.SubOper;
import com.oxo.ball.service.admin.BallAdminService;
import com.oxo.ball.service.admin.BallGroupService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/ball/admin")
@MainOper("系统管理-用户管理")
public class BallAdminController {
    @Resource
    BallAdminService ballAdminService;
    @Resource
    private BallGroupService ballGroupService;

    @PostMapping
    public Object index(BallAdmin query, @RequestParam(defaultValue = "1")Integer pageNo, @RequestParam(defaultValue = "20") Integer pageSize){
        SearchResponse<BallAdmin> search = ballAdminService.search(query, pageNo, pageSize);
        return BaseResponse.successWithData(search);
    }
    @GetMapping()
    public Object listAll(){
        List<BallGroup> list = ballGroupService.findAll();
        return BaseResponse.successWithData(list);
    }

    @PostMapping("add")
    @SubOper("添加用户")
    public Object add(@RequestBody SysUserInsertRequest sysUserRequest){
        BallAdmin insert = new BallAdmin();
        try {
            insert = ballAdminService.insert(sysUserRequest);
        }catch (Exception ex){
            ex.printStackTrace();
        }

        BaseResponse res = (insert.getId() != null ? BaseResponse.successWithData(insert) : BaseResponse.failedWithMsg("add error"));
        res.setRemark("添加用户"+insert.getUsername());
        return res;
    }
    @GetMapping("edit/{id}")
    public Object edit(@PathVariable Long id){
        BallAdmin byId = ballAdminService.findById(id);
        BaseResponse<BallAdmin> ballAdminBaseResponse = new BaseResponse<>(byId);
        return ballAdminBaseResponse;
    }
    @PostMapping("edit")
    @SubOper("修改用户")
    public Object editSave(@Validated @RequestBody SysUserEditRequest sysUserEditRequest){
        if(sysUserEditRequest.getId() == 1){
            return BaseResponse.failedWithMsg("cat not edit superadmin");
        }
        Boolean aBoolean = ballAdminService.edit(sysUserEditRequest);
        BaseResponse response = (aBoolean ? BaseResponse.SUCCESS : BaseResponse.failedWithMsg("edit error"));
        response.setRemark("edit"+sysUserEditRequest.getId());
        return response;
    }
    @GetMapping("edit")
    @SubOper("修改用户")
    public Object resetGoogle(Long id){
        SysUserEditRequest edit = new SysUserEditRequest();
        edit.setId(id);
        edit.setGoogleCode("");
        Boolean aBoolean = ballAdminService.edit(edit);
        BaseResponse response = (aBoolean ? BaseResponse.SUCCESS : BaseResponse.failedWithMsg("reset failed"));
        response.setRemark("reset "+id+"'s google verification code");
        return response;
    }
    @GetMapping("del")
    @SubOper("删除用户")
    public Object del(@RequestParam("id") Long id){
        if(id == 1){
            return BaseResponse.failedWithMsg("cat not del superadmin");
        }
        Boolean delete = ballAdminService.delete(id);
        BaseResponse res = delete ? BaseResponse.SUCCESS : BaseResponse.failedWithMsg("del error");
        res.setRemark("del user "+id);
        return res;
    }


}
