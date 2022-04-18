package com.mxb.community.utils;

import org.springframework.stereotype.Component;

/**
 * 全局常量类
 * ACTIVATION_SUCCESS : 激活成功
 * ACTIVATION_REPEAT : 重复激活
 * ACTIVATION_FAILURE : 激活失败
 * DEFAULT_EXPIRED_SECONDS 12小时
 * REMEMBER_EXPIRED_SECONDS 100天
 */
public class CommunityConstant {

    public static final int ACTIVATION_SUCCESS = 0;

    public static final int ACTIVATION_REPEAT = 1;

    public static final int ACTIVATION_FAILURE = 2;

    public static final int DEFAULT_EXPIRED_SECONDS = 3600 * 12;

    public static final int REMEMBER_EXPIRED_SECONDS = 3600 * 24 * 100;

}
