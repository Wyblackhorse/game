package com.oxo.ball.controller.admin;

import com.oxo.ball.bean.dao.BallGroup;
import com.oxo.ball.bean.dto.resp.BaseResponse;
import com.oxo.ball.bean.dto.resp.SearchResponse;
import com.oxo.ball.service.admin.BallGroupService;
import com.oxo.ball.service.admin.BallMenuService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/ball/group")
public class BallGroupController {
    @Resource
    BallGroupService ballGroupService;
    @Resource
    BallMenuService ballMenuService;

    @PostMapping
    public Object index(BallGroup query, @RequestParam(defaultValue = "1")Integer pageNo, @RequestParam(defaultValue = "20") Integer pageSize){
        SearchResponse<BallGroup> search = ballGroupService.search(query, pageNo, pageSize);
        return BaseResponse.successWithData(search);
    }

    @PostMapping("add")
    public Object add(@RequestBody BallGroup sysUserRequest){
        BallGroup insert = new BallGroup();
        try {
            insert = ballGroupService.insert(sysUserRequest);
        }catch (Exception ex){
            ex.printStackTrace();
        }

        return insert.getId()!=null?BaseResponse.successWithData(insert):BaseResponse.failedWithMsg("增加失败~");
    }

    @GetMapping("edit/{id}")
    public Object edit(@PathVariable Long id){
        BallGroup byId = ballGroupService.findById(id);
        return new BaseResponse<>(byId);
    }
    @PostMapping("edit")
    public Object editSave(@Validated @RequestBody BallGroup editBallGroup){
        Boolean aBoolean = ballGroupService.edit(editBallGroup);
        return  aBoolean?BaseResponse.SUCCESS:BaseResponse.failedWithMsg("修改角色失败~");
    }

    /**
     * 查找当前角色已授权
     * @param roleId
     * @return
     */
    @GetMapping("edit")
    public Object getRoleAuth(@RequestParam Long roleId) {
        return BaseResponse.successWithData(ballMenuService.findAuthIdByRole(roleId));
    }
    @GetMapping("del")
    public Object del(@RequestParam("id") Long id){
        Boolean delete = ballGroupService.delete(id);
        return delete?BaseResponse.SUCCESS:BaseResponse.failedWithMsg("删除失败~");
    }


}
