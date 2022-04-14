package com.oxo.ball.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.oxo.ball.bean.dao.BallGroup;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author flooming
 */
@Mapper
@Component
public interface BallGroupMapper extends BaseMapper<BallGroup> {
    void clearAuthOfRole(@Param("roleId") Long roleId);
    void addAuthOfRole(@Param("roleId") Long roleId,@Param("auths") Long[] auths);
}
