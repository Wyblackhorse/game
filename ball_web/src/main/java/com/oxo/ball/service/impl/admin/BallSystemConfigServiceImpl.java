package com.oxo.ball.service.impl.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.oxo.ball.bean.dao.BallSystemConfig;
import com.oxo.ball.mapper.BallSystemConfigMapper;
import com.oxo.ball.service.admin.IBallSystemConfigService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 系统配置 服务实现类
 * </p>
 *
 * @author oxo_jy
 * @since 2022-04-13
 */
@Service
public class BallSystemConfigServiceImpl extends ServiceImpl<BallSystemConfigMapper, BallSystemConfig> implements IBallSystemConfigService {

    @Override
    @Cacheable(value = "ball_sys_config", key = "'one'", unless = "#result == null")
    public BallSystemConfig getSystemConfig() {
        BallSystemConfig one = getOne(new QueryWrapper<>());
        return one;
    }

    @Override
    public void init() {
        BallSystemConfig systemConfig = getSystemConfig();
        if(systemConfig==null){
            BallSystemConfig save = BallSystemConfig.builder()
                    .registerIfNeedVerificationCode(1)
                    .verificationCodeLayout(1)
                    .passwordErrorLockTime(60)
                    .passwordMaxErrorTimes(5)
                    .build();
            save(save);
        }
    }
}
