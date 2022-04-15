package com.oxo.ball.config;

import com.oxo.ball.auth.AuthException;
import com.oxo.ball.auth.TokenInvalidedException;
import com.oxo.ball.bean.dto.resp.BaseResponse;
import io.undertow.util.StatusCodes;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import static com.oxo.ball.service.admin.AuthService.HAVE_NO_AUTH;
import static com.oxo.ball.service.admin.AuthService.TOKEN_INVALID;


/**
 * @author flooming
 */
@ControllerAdvice
public class ExceptionAdvice {
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public BaseResponse exceptionHandler(Exception e) {
        e.printStackTrace();
        return new BaseResponse(StatusCodes.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    @ExceptionHandler(AuthException.class)
    @ResponseBody
    public BaseResponse exceptionHandler(AuthException e) {
        return new BaseResponse(HAVE_NO_AUTH, "没有权限");
    }

    @ExceptionHandler(TokenInvalidedException.class)
    @ResponseBody
    public BaseResponse exceptionHandler(TokenInvalidedException e) {
        return new BaseResponse(TOKEN_INVALID, "登录失效或已过期");
    }

}
