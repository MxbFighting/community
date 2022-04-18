package com.mxb.community.controller.interceptor;

import com.mxb.community.annotation.LoginRequired;
import com.mxb.community.utils.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * 防止用户没有登陆, 却能访问登录才能访问的方法
 * @author Mxb
 */
@Component
public class LoginRequiredInterceptor implements HandlerInterceptor {

    @Autowired
    private HostHolder hostHolder;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //如果handler是HandlerMethod的实例对象
        if (handler instanceof HandlerMethod) {
            //把handler强转为HandlerMethod
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            //获得方法
            Method method = handlerMethod.getMethod();
            //获得方法里特定注解的对象
            LoginRequired loginRequired = method.getAnnotation(LoginRequired.class);
            //如果这个对象不为空说明需要登录才能访问, 如果从hostHolder获得的User是空的, 说明没有登陆, 重定向到登录页面且拦截请求
            if (loginRequired != null && hostHolder.getUser() == null) {
                response.sendRedirect(request.getContextPath() + "/login");
                return false;
            }
        }
        return true;
    }
}
