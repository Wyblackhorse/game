package com.oxo.ball.service.admin;


import com.oxo.ball.bean.dao.BallMenu;

import java.util.List;

/**
 * @author flooming
 */
public interface BallMenuService {
    BallMenu findById(Long id);
    List<BallMenu> findByRole(Long roleId);
    List<String> findPathsByRole(Long roleId);
    List<Long> findAuthIdByRole(Long roleId);
    List<BallMenu> findAll();
}
