package com.oxo.ball.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.oxo.ball.bean.dao.BallAdmin;
import com.oxo.ball.bean.dao.BallGroup;
import com.oxo.ball.bean.dto.req.SysUserEditRequest;
import com.oxo.ball.bean.dto.req.SysUserInsertRequest;
import com.oxo.ball.bean.dto.resp.BaseResponse;
import com.oxo.ball.bean.dto.resp.SearchResponse;
import com.oxo.ball.service.admin.BallAdminService;
import com.oxo.ball.service.admin.BallGroupService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/ball/admin")
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
        QueryWrapper query = new QueryWrapper();
        query.eq("status",1);
        List<BallGroup> list = ballGroupService.list(query);
        return BaseResponse.successWithData(list);
    }

    @PostMapping("add")
    public Object add(@RequestBody SysUserInsertRequest sysUserRequest){
        BallAdmin insert = new BallAdmin();
        try {
            insert = ballAdminService.insert(sysUserRequest);
        }catch (Exception ex){
            ex.printStackTrace();
        }

        return insert.getId()!=null?BaseResponse.successWithData(insert):BaseResponse.failedWithMsg("增加失败~");
    }
    @GetMapping("edit/{id}")
    public Object edit(@PathVariable Long id){
        BallAdmin byId = ballAdminService.findById(id);
        return new BaseResponse<>(byId);
    }
    @PostMapping("edit")
    public Object editSave(@Validated @RequestBody SysUserEditRequest sysUserEditRequest){
        if(sysUserEditRequest.getId() == 1){
            return BaseResponse.failedWithMsg("不能编辑超级管理员账号~");
        }
        Boolean aBoolean = ballAdminService.edit(sysUserEditRequest);
        return  aBoolean?BaseResponse.SUCCESS:BaseResponse.failedWithMsg("修改账号信息失败~");
    }
    @GetMapping("del")
    public Object del(@RequestParam("id") Long id){
        if(id == 1){
            return BaseResponse.failedWithMsg("不能删除超级管理员账号~");
        }
        Boolean delete = ballAdminService.delete(id);
        return delete?BaseResponse.SUCCESS:BaseResponse.failedWithMsg("删除失败~");
    }


}
