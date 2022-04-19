package com.oxo.ball.controller.admin;

import com.oxo.ball.bean.dao.BallDepositPolicy;
import com.oxo.ball.bean.dto.resp.BaseResponse;
import com.oxo.ball.bean.dto.resp.SearchResponse;
import com.oxo.ball.service.admin.IBallDepositPolicyService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 存款策略 前端控制器
 * </p>
 *
 * @author oxo_jy
 * @since 2022-04-13
 */
@RestController
@RequestMapping("/ball/tactics/inout")
public class BallDepositPolicyController {
    @Resource
    IBallDepositPolicyService depositPolicyService;
    @PostMapping
    public Object index(BallDepositPolicy query,
                        @RequestParam(defaultValue = "1")Integer pageNo,
                        @RequestParam(defaultValue = "20") Integer pageSize){
        SearchResponse<BallDepositPolicy> search = depositPolicyService.search(query, pageNo, pageSize);
        return BaseResponse.successWithData(search);
    }
    @PostMapping("add")
    public Object add(@RequestBody BallDepositPolicy sysUserRequest){
        BallDepositPolicy insert = new BallDepositPolicy();
        try {
            insert = depositPolicyService.insert(sysUserRequest);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return insert.getId()!=null?BaseResponse.successWithData(insert):BaseResponse.failedWithMsg("增加失败~");
    }
    @PostMapping("edit")
    public Object editSave(@RequestBody BallDepositPolicy ballPlayer){
        Boolean aBoolean = depositPolicyService.edit(ballPlayer);
        return  aBoolean?BaseResponse.SUCCESS:BaseResponse.failedWithMsg("修改失败~");
    }
    @PostMapping("status")
    public Object status(@RequestBody BallDepositPolicy ballPlayer){
        Boolean aBoolean = depositPolicyService.status(ballPlayer);
        return  aBoolean?BaseResponse.SUCCESS:BaseResponse.failedWithMsg("修改失败~");
    }
    @GetMapping("del")
    public Object del(@RequestParam("id") Long id){
        Boolean delete = depositPolicyService.delete(id);
        return delete?BaseResponse.SUCCESS:BaseResponse.failedWithMsg("删除失败~");
    }
}
