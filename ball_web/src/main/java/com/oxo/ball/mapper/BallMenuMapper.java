package com.oxo.ball.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.oxo.ball.bean.dao.BallMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author flooming
 */
@Mapper
@Component
public interface BallMenuMapper extends BaseMapper<BallMenu> {
    @Select("select ba.* from ball_menu ba , ball_group_menu bra where ba.id = bra.auth_id and bra.role_id = #{roleId}")
    List<BallMenu> findByRole(@Param("roleId") Long roleId);
}
