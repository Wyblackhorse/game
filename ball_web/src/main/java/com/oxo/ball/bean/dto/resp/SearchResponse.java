package com.oxo.ball.bean.dto.resp;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author none
 */
@Data
public class SearchResponse<T> implements Serializable {
    private static final long serialVersionUID = 4593254494165302020L;

    private Long pageNo;
    private Long pageSize;
    private Long totalCount;
    private Long totalPage;
    private List<T> results;

}
