package com.oxo.ball.mapper;

import com.oxo.ball.bean.dao.BallLoggerLogin;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 日志表 Mapper 接口
 * </p>
 *
 * @author oxo_jy
 * @since 2022-04-13
 */
@Mapper
public interface BallLoggerMapper extends BaseMapper<BallLoggerLogin> {

}
