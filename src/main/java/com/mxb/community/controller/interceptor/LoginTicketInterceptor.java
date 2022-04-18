package com.mxb.community.controller.interceptor;

import com.mxb.community.domain.entity.LoginTicket;
import com.mxb.community.domain.entity.User;
import com.mxb.community.service.UserService;
import com.mxb.community.utils.CookieUtil;
import com.mxb.community.utils.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * @author Mxb
 */
@Component
public class LoginTicketInterceptor implements HandlerInterceptor {

    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //从浏览器中取出想要的cookie, 并取出里面存的登录凭证
        String ticket = CookieUtil.getValue(request, "ticket");

        if (ticket != null) {
            //通过ticket查询对应的loginTicket
            LoginTicket loginTicket = userService.findLoginTicket(ticket);
            //如果查询出的loginTicket 不为空 & 状态为1(有效)  & 没过时
            if (loginTicket != null && loginTicket.getStatus() == 0 && loginTicket.getExpired().after(new Date())) {
                //查询loginTicket里的userid对应的user
                User user = userService.getById(loginTicket.getUserId());
                //把user存入到hostHolder
                hostHolder.setUsers(user);
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        User user = hostHolder.getUser();
        if (user != null && modelAndView != null) {
            modelAndView.addObject("loginUser", user);
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        hostHolder.clear();
    }
}
