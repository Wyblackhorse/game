package com.oxo.ball.interceptor;

import com.oxo.ball.bean.dao.BallAdmin;
import com.oxo.ball.bean.dto.queue.MessageQueueDTO;
import com.oxo.ball.bean.dto.queue.MessageQueueOper;
import com.oxo.ball.bean.dto.resp.BaseResponse;
import com.oxo.ball.contant.LogsContant;
import com.oxo.ball.service.IMessageQueueService;
import com.oxo.ball.service.admin.AuthService;
import com.oxo.ball.service.admin.BallAdminService;
import com.oxo.ball.utils.IpUtil;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.List;

@ControllerAdvice
public class AdminOperResponseHandler implements ResponseBodyAdvice<BaseResponse> {

    private Logger logger = LoggerFactory.getLogger(LogsContant.API_LOG);

    @Autowired
    IMessageQueueService messageQueueService;

    @Autowired
    BallAdminService adminService;

    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        Method method = methodParameter.getMethod();
        return method.isAnnotationPresent(SubOper.class);
    }

    @Override
    public BaseResponse beforeBodyWrite(BaseResponse restResult, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        if(BaseResponse.SUCCESS.getCode().equals(restResult.getCode())){
            HttpHeaders headers = serverHttpRequest.getHeaders();
            List<String> token = headers.get("token");
            BallAdmin currentUser = adminService.getCurrentUser(token.get(0));
            Method method = methodParameter.getMethod();
            String subOper = method.getAnnotation(SubOper.class).value();
            Class<?> cls = method.getDeclaringClass();
            String mainOper = cls.getDeclaredAnnotation(MainOper.class).value();
            logger.info("{}调用方法={}，模块={},备注={}",currentUser.getUsername(),subOper,mainOper,restResult.getRemark());
            messageQueueService.putMessage(MessageQueueDTO.builder()
                    .type(MessageQueueDTO.TYPE_LOG_OPER)
                    .data(MessageQueueOper.builder()
                            .mainOper(mainOper)
                            .subOper(subOper)
                            .remark(restResult.getRemark())
                            .username(currentUser.getUsername())
                            .ip(IpUtil.getIpAddress(serverHttpRequest))
                            .build())
                    .build());
        }
        restResult.setRemark(null);
        return restResult;
    }
}
