package com.mxb.community.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mxb.community.domain.entity.User;
import com.mxb.community.mapper.UserMapper;
import com.mxb.community.service.UserService;
import org.springframework.stereotype.Service;

/**
 * @author Mxb
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
