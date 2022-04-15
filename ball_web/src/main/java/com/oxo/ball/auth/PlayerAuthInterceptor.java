package com.oxo.ball.auth;

import com.oxo.ball.service.player.AuthPlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.oxo.ball.service.admin.AuthService.HAVE_NO_AUTH;
import static com.oxo.ball.service.admin.AuthService.TOKEN_INVALID;

/**
 * @author flooming
 */
public class PlayerAuthInterceptor implements HandlerInterceptor {
    @Autowired
    private AuthPlayerService authService;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String servletPath = request.getServletPath();

        int token = authService.checkAuth(request.getHeader("token"), servletPath);
        switch (token){
            case 1:
                return true;
            case TOKEN_INVALID:
                throw new TokenInvalidedException();
            case HAVE_NO_AUTH:
                throw new AuthException();
            default:
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        authService.clearCurrentUser();
    }
}
