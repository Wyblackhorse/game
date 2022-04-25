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

        BaseResponse res = (insert.getId() != null ? BaseResponse.successWithData(insert) : BaseResponse.failedWithMsg("增加失败~"));
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
            return BaseResponse.failedWithMsg("不能编辑超级管理员账号~");
        }
        Boolean aBoolean = ballAdminService.edit(sysUserEditRequest);
        BaseResponse response = (aBoolean ? BaseResponse.SUCCESS : BaseResponse.failedWithMsg("修改账号信息失败~"));
        response.setRemark("修改账号"+sysUserEditRequest.getId());
        return response;
    }
    @GetMapping("edit")
    @SubOper("修改用户")
    public Object resetGoogle(Long id){
        SysUserEditRequest edit = new SysUserEditRequest();
        edit.setId(id);
        edit.setGoogleCode("");
        Boolean aBoolean = ballAdminService.edit(edit);
        BaseResponse response = (aBoolean ? BaseResponse.SUCCESS : BaseResponse.failedWithMsg("重置失败~"));
        response.setRemark("重置账号"+id+"的google验证码~");
        return response;
    }
    @GetMapping("del")
    @SubOper("删除用户")
    public Object del(@RequestParam("id") Long id){
        if(id == 1){
            return BaseResponse.failedWithMsg("不能删除超级管理员账号~");
        }
        Boolean delete = ballAdminService.delete(id);
        BaseResponse res = delete ? BaseResponse.SUCCESS : BaseResponse.failedWithMsg("删除失败~");
        res.setRemark("删除用户"+id);
        return res;
    }


}
