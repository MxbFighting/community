package com.mxb.community.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Mxb
 * 是在方法上写的注解
 * 是在程序运行时有效的
 * 这个注解里面没有内容, 只是起一个标识的作用, 需要登录才能访问的方法需要打上这个注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LoginRequired {
}
