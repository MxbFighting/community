package com.mxb.community.domain.entity;

import java.util.Date;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 *  type 0-普通用户; 1-超级管理员; 2-版主;
 *  status 0-未激活; 1-已激活;
 *  SuppressWarnings("serial") 忽略在 serializable 类中没有声明 serialVersionUID 变量
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("user")
public class User  {
    @TableId
    private Integer id;

    private String username;

    private String password;

    private String salt;

    private String email;

    private Integer type;

    private Integer status;

    private String activationCode;

    private String headerUrl;

    private Date createTime;

}

