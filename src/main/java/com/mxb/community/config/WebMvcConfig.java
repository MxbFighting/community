package com.mxb.community.config;

import com.mxb.community.controller.interceptor.LoginRequiredInterceptor;
import com.mxb.community.controller.interceptor.LoginTicketInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 对拦截器进行配置
 * @author Mxb
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private LoginTicketInterceptor loginTicketInterceptor;

    @Autowired
    private LoginRequiredInterceptor loginRequiredInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
       registry.addInterceptor(loginTicketInterceptor)
               //除了这些静态资源, 其他的都拦截
               .excludePathPatterns("/**/*.css", "/**/*.js", "/**/*.png","/**/*.jpg", "/**/*.jpeg");

        registry.addInterceptor(loginRequiredInterceptor)
                //除了这些静态资源, 其他的都拦截
                .excludePathPatterns("/**/*.css", "/**/*.js", "/**/*.png","/**/*.jpg", "/**/*.jpeg");

    }
}
