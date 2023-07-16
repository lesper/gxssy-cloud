package top.latke.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import top.latke.constant.CommonConstant;
import top.latke.util.TokenParseUtil;
import top.latke.vo.LoginUserInfo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用户身份统一登录拦截
 */
@Slf4j
@Component
public class LoginUserInfoInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //先尝试从 http header 中拿到 Token
        String token = request.getHeader(CommonConstant.JWT_USER_INFO_KEY);
        LoginUserInfo loginUserInfo = null;

        try{
            loginUserInfo = TokenParseUtil.parseUserInfoFromToken(token);
        }catch (Exception ex){
            log.error("parse login user info error: [{}]",ex.getMessage(),ex);
        }

        if (null == loginUserInfo) {
            throw new RuntimeException("can not parse current login user");
        }

        log.info("set login user info: [{}]",request.getRequestURI());

        //设置当前上下文,填充用户信息
        AccessContext.setLoginUserInfo(loginUserInfo);

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    /**
     * 在请求完全结束后调用，常用于清理资源等工作
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        if (null != AccessContext.getLoginUserInfo()) {
            AccessContext.clearLoginUserInfo();
        }
    }
}
