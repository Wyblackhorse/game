package com.oxo.ball.service.admin;

import com.oxo.ball.bean.dao.BallSystemConfig;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.cache.annotation.Cacheable;

/**
 * <p>
 * 系统配置 服务类
 * </p>
 *
 * @author oxo_jy
 * @since 2022-04-13
 */
public interface IBallSystemConfigService extends IService<BallSystemConfig> {
    BallSystemConfig getSystemConfig();
    void init();
    Boolean edit(BallSystemConfig systemConfig);
}
