package com.oxo.ball.service.impl.admin;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.oxo.ball.bean.dao.BallAdmin;
import com.oxo.ball.bean.dto.req.SysUserEditRequest;
import com.oxo.ball.bean.dto.req.SysUserInsertRequest;
import com.oxo.ball.bean.dto.resp.SearchResponse;
import com.oxo.ball.mapper.BallAdminMapper;
import com.oxo.ball.service.admin.BallAdminService;
import com.oxo.ball.utils.PasswordUtil;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author flooming
 */
@Service
public class BallAdminServiceImpl implements BallAdminService {
    @Resource
    BallAdminMapper ballAdminMapper;

    @Resource
    BallAdminService ballAdminService;
    @Override
    @Cacheable(value = "ball_sys_user", key = "'_ID_' + #id", unless = "#result == null")
    public BallAdmin findById(Long id) {
        return ballAdminMapper.selectById(id);
    }

    @Override
    public BallAdmin findByUsername(String username) {
        QueryWrapper<BallAdmin> query = new QueryWrapper<>();
        query.eq("admin_name", username);
        BallAdmin ballAdminDAO = ballAdminMapper.selectOne(query);
        return ballAdminDAO;
    }


    @Override
    public List<BallAdmin> findByPuser(Long userId) {
        QueryWrapper<BallAdmin> query = new QueryWrapper<>();
        query.eq("puser_id",userId);
        List<BallAdmin> ballAdminDAOS = ballAdminMapper.selectList(query);
        return ballAdminDAOS;
    }

    @Override
    @CachePut(value = "ball_sys_user", key = "'_ID_' + #result.getId()")
    public BallAdmin insert(SysUserInsertRequest sysUserRequest) {
        BallAdmin build = BallAdmin.builder()
                .username(sysUserRequest.getUsername())
                .password(PasswordUtil.genPasswordMd5(sysUserRequest.getPassword()))
                .roleId(sysUserRequest.getRoleId())
                .nickname(sysUserRequest.getNickname())
                .status(1)
                .token("")
                .build();
        build.setCreatedAt(System.currentTimeMillis());
        build.setUpdatedAt(0L);
        ballAdminMapper.insert(build);
        return build;
    }

    @Override
    @CacheEvict(value = "ball_sys_user", key = "'_ID_' + #id")
    public Boolean delete(Long id) {
        return ballAdminMapper.deleteById(id) == 1;
    }

    @Override
    public SearchResponse<BallAdmin> search(BallAdmin currentUser, Integer pageNo, Integer pageSize) {
        SearchResponse<BallAdmin> response = new SearchResponse<>();

        Page<BallAdmin> page = new Page<>(pageNo, pageSize);
        QueryWrapper<BallAdmin> query = new QueryWrapper<>();
        if(currentUser != null) {
            if(currentUser.getId()!=null){
                    query.and(Wrapper -> Wrapper.eq("id", currentUser.getId()).or().like("username", currentUser.getUsername()));
            } else if(!StringUtils.isEmpty(currentUser.getUsername())){
                query.like("admin_name",currentUser.getUsername());
            }
        }

        IPage<BallAdmin> pages = ballAdminMapper.selectPage(page, query);

        response.setPageNo(pages.getCurrent());
        response.setPageSize(pages.getSize());
        response.setTotalCount(pages.getTotal());
        response.setTotalPage(pages.getPages());
        response.setResults(pages.getRecords());

        return response;
    }

    @Override
    @CacheEvict(value = "ball_sys_user", key = "'_ID_' + #id")
    public Boolean editPwd(Long id, String pwd) {
        UpdateWrapper<BallAdmin> wrapper = new UpdateWrapper<>();
        wrapper.eq("id", id);
        BallAdmin dao = new BallAdmin();
        dao.setPassword(pwd);

        if(ballAdminMapper.update(dao, wrapper) == 0) {
            return false;
        }

        return true;
    }

    @CacheEvict(value = "ball_sys_user", key = "'_ID_' + #sysUserEditRequest.getId()")
    @Override
    public Boolean edit(SysUserEditRequest sysUserEditRequest) {
        UpdateWrapper<BallAdmin> wrapper = new UpdateWrapper<>();
        wrapper.eq("id", sysUserEditRequest.getId());
        BallAdmin dao = new BallAdmin();
        if(!StringUtils.isEmpty(sysUserEditRequest.getPassword())){
            dao.setPassword(PasswordUtil.genPasswordMd5(sysUserEditRequest.getPassword()));
        }
        if(sysUserEditRequest.getRoleId()!=null){
            dao.setRoleId(sysUserEditRequest.getRoleId());
        }
        if(!StringUtils.isEmpty(sysUserEditRequest.getNickname())){
            dao.setNickname(sysUserEditRequest.getNickname());
        }
        if(sysUserEditRequest.getGoogleCode()!=null){
            dao.setGoogleCode(sysUserEditRequest.getGoogleCode());
        }

        if(ballAdminMapper.update(dao, wrapper) == 0) {
            return false;
        }

        return true;
    }

    @Override
    public BallAdminMapper getMapper() {
        return ballAdminMapper;
    }

    @Override
    public BallAdmin getCurrentUser(String token) {
        Long userId;
        try {
            List<String> audience = JWT.decode(token).getAudience();
            userId = Long.parseLong(audience.get(0));
            BallAdmin byId = ballAdminService.findById(userId);
            return byId;
        } catch (JWTDecodeException j) {
            throw new RuntimeException("internal error");
        } catch (Exception ex){
            return null;
        }
    }

    @Override
    public Long getCurrentUserId(HttpServletRequest request) {
        Long userId;
        try {
            List<String> audience = JWT.decode(request.getHeader("token")).getAudience();
            userId = Long.parseLong(audience.get(0));
            return userId;
        } catch (JWTDecodeException j) {
            return 0L;
        } catch (Exception ex){
            return 0L;
        }
    }
}
