package com.oxo.ball.bean.dao;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

/**
 *
 */
@Data
public class BaseDAO implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private Long createdAt;
    private Long updatedAt;
}
