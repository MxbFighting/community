package com.mxb.community.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mxb.community.domain.entity.LoginTicket;
import com.mxb.community.domain.entity.User;

import java.util.Map;

/**
 * @author mxb
 */
public interface UserService extends IService<User> {
    Map<String, Object> register(User user);

    int activation(int userId, String code);

    Map<String, Object> login(String username, String password, long expiredSeconds);

    void logout(String ticket);

    LoginTicket findLoginTicket(String ticket);

    int updateHeader(int userId, String headerUrl);
}
