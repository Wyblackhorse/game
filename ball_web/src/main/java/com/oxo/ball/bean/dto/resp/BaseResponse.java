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

    /**
     * 登录时密码错误次数
     */
    public static final int FAIL_LOGIN_ERROR_COUNT = 101;
    /**
     * 登录时密码输入错误上限,不能再次请求
     */
    public static final int FAIL_LOGIN_ERROR_MAX = 102;
    /**
     * 表单请求时字段格式未通过验证
     */
    public static final int FAIL_FORM_SUBMIT = 103;



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
    public static <T>BaseResponse failedWithData(T data){
        return new BaseResponse(StatusCodes.INTERNAL_SERVER_ERROR,data);
    }
    public static <T>BaseResponse failedWithData(int code,T data){
        return new BaseResponse(code,data);
    }
}
