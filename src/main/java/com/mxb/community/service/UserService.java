package com.mxb.community.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mxb.community.domain.entity.User;

import java.util.Map;

/**
 * @author mxb
 */
public interface UserService extends IService<User> {
    public Map<String, Object> register(User user);

    public int activation(int userId, String code);
}
