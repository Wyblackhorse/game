package com.oxo.ball.config;

import com.oxo.ball.auth.AuthException;
import com.oxo.ball.auth.PlayerEnabledException;
import com.oxo.ball.auth.TokenInvalidedException;
import com.oxo.ball.bean.dto.resp.BaseResponse;
import com.oxo.ball.service.player.AuthPlayerService;
import io.undertow.util.StatusCodes;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @ExceptionHandler(PlayerEnabledException.class)
    @ResponseBody
    public BaseResponse exceptionHandler(PlayerEnabledException e) {
        return new BaseResponse(AuthPlayerService.PLAYER_INVALID, "账号已停用");
    }
    @ExceptionHandler(TokenInvalidedException.class)
    @ResponseBody
    public BaseResponse exceptionHandler(TokenInvalidedException e) {
        return new BaseResponse(TOKEN_INVALID, "登录失效或已过期");
    }
    @ExceptionHandler(BindException.class)
    @ResponseBody
    public BaseResponse bindExceptionHandler(BindException e) {
        BindingResult bindingResult = e.getBindingResult();
        List<ObjectError> allErrors = bindingResult.getAllErrors();
        List<Map<String,Object>> errorList = new ArrayList<>();
        for(ObjectError objectError:allErrors){
            if(objectError instanceof FieldError){
                FieldError error = (FieldError)objectError;
                Map<String,Object> data = new HashMap<>();
                data.put("name", error.getField());
                data.put("msg", error.getDefaultMessage());
                errorList.add(data);
            }
        }
        return BaseResponse.failedWithData(BaseResponse.FAIL_FORM_SUBMIT,errorList);
    }

}
