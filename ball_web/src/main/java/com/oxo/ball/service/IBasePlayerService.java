package com.oxo.ball.service;

import com.oxo.ball.bean.dao.BallPlayer;

public interface IBasePlayerService {
    BallPlayer findById(Long id);
    BallPlayer findByIdNoCache(Long id);
    BallPlayer findByUsername(String username);
    BallPlayer findByInvitationCode(String invitationCode);
    boolean editAndClearCache(BallPlayer edit,BallPlayer db);

    void editMultGroupNum(String treeIds,int quantity);
}
