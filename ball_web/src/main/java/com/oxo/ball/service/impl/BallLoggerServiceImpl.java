package com.oxo.ball.service.impl;

import com.oxo.ball.bean.dao.BallLogger;
import com.oxo.ball.mapper.BallLoggerMapper;
import com.oxo.ball.service.IBallLoggerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 日志表 服务实现类
 * </p>
 *
 * @author oxo_jy
 * @since 2022-04-13
 */
@Service
public class BallLoggerServiceImpl extends ServiceImpl<BallLoggerMapper, BallLogger> implements IBallLoggerService {

}
