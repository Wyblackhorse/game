package com.oxo.ball.service.player;


import com.oxo.ball.bean.dao.BallAdmin;
import com.oxo.ball.bean.dao.BallPlayer;

/**
 * @author flooming
 */
public interface AuthPlayerService {
    int TOKEN_INVALID = 402;
    int PLAYER_INVALID = 403;
    String buildToken(BallPlayer user);
    int checkAuth(String token, String path);
    void clearAuth(BallPlayer user);
    void clearCurrentUser();
}
