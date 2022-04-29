package com.oxo.ball.mapper;

import com.oxo.ball.bean.dao.BallPlayer;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * <p>
 * 玩家账号 Mapper 接口
 * </p>
 *
 * @author oxo_jy
 * @since 2022-04-13
 */
@Mapper
public interface BallPlayerMapper extends BaseMapper<BallPlayer> {

    @Update("update ball_player set group_size=group_size+${t} where id in (${ids})")
    void updateTreeGroupNum(@Param("ids") String treeIds,@Param("t") int quantity);
}
