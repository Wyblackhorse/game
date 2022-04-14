package com.oxo.ball.service.impl;

import com.oxo.ball.bean.dao.BallGame;
import com.oxo.ball.mapper.BallGameMapper;
import com.oxo.ball.service.IBallGameService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 游戏赛事 服务实现类
 * </p>
 *
 * @author oxo_jy
 * @since 2022-04-13
 */
@Service
public class BallGameServiceImpl extends ServiceImpl<BallGameMapper, BallGame> implements IBallGameService {

}
