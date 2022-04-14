package com.oxo.ball.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.oxo.ball.bean.dao.BallAdmin;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * @author flooming
 */
@Mapper
@Component
public interface BallAdminMapper extends BaseMapper<BallAdmin> {
}
