package com.oxo.ball.bean.dto.resp.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiFixtures {
    private ApiPaging paging;
    private List<FixtureReponse> response;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class FixtureReponse{
        //比赛信息
        private ApiFixture fixture;
        //参赛队伍
        private ApiTeams teams;
        //比分
        private ApiGoals goals;
        //比分详情
        private ApiScore score;
        //联赛数据
        private ApiLeague league;
    }
}
