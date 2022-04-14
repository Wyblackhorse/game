package com.oxo.ball.bean.dto.resp;

import io.undertow.util.StatusCodes;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author flooming
 * @date 2019-08-25 19:02
 */
@Data
public class BaseResponse<T> implements Serializable {
    private static final long serialVersionUID = -5565210554130093478L;

    public static BaseResponse SUCCESS = new BaseResponse(StatusCodes.OK, null);


    private Integer code;
    private String msg;
    private T data;

    public BaseResponse(T data) {
        this.code = StatusCodes.OK;
        this.data = data;
    }

    public BaseResponse(@NotNull Integer code, @NotNull String msg) {
        this.code = code;
        this.msg = msg;
    }
    public BaseResponse( Integer code, T data) {
        this.code = code;
        this.data = data;
    }
    public static BaseResponse successWithMsg(String msg){
        return new BaseResponse(StatusCodes.OK,msg);
    }
    public static <T>BaseResponse successWithData(T data){
        return new BaseResponse(StatusCodes.OK,data);
    }
    public static BaseResponse failedWithMsg(String msg){
        return new BaseResponse(StatusCodes.INTERNAL_SERVER_ERROR,msg);
    }
    public static BaseResponse failedWithMsg(int code,String msg){
        return new BaseResponse(code,msg);
    }
}
