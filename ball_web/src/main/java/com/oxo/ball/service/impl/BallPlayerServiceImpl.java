package com.oxo.ball.service.impl;

import com.oxo.ball.bean.dao.BallPlayer;
import com.oxo.ball.mapper.BallPlayerMapper;
import com.oxo.ball.service.IBallPlayerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 玩家账号 服务实现类
 * </p>
 *
 * @author oxo_jy
 * @since 2022-04-13
 */
@Service
public class BallPlayerServiceImpl extends ServiceImpl<BallPlayerMapper, BallPlayer> implements IBallPlayerService {

}
