package com.oxo.ball.service.admin;


import com.oxo.ball.bean.dao.BallAdmin;
import com.oxo.ball.bean.dto.req.SysUserEditRequest;
import com.oxo.ball.bean.dto.req.SysUserInsertRequest;
import com.oxo.ball.bean.dto.resp.SearchResponse;
import com.oxo.ball.mapper.BallAdminMapper;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author flooming
 */
public interface BallAdminService {
    BallAdmin findById(Long id);
    BallAdmin findByUsername(String username);
    List<BallAdmin> findByPuser(Long userId);
    SearchResponse<BallAdmin> search(BallAdmin keyword, Integer pageNo, Integer pageSize);
    BallAdmin insert(SysUserInsertRequest sysUserRequest);
    Boolean delete(Long id);
    Boolean edit(SysUserEditRequest sysUserEditRequest);
    Boolean editPwd(Long id, String pwd);
    BallAdminMapper getMapper();
    BallAdmin getCurrentUser(String token);
    Long  getCurrentUserId(HttpServletRequest request);

}
