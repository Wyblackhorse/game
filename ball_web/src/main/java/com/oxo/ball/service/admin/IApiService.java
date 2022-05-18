package com.oxo.ball.service.admin;

public interface IApiService {
    //刷新联赛
    void refreshLeagues();
    //刷新团队
    void refreshFixtures();
    void refreshFixturesAll();
    //刷新赔率
    void refreshOdds();
}
