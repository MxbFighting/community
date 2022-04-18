package com.mxb.community.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * status 0有效 1无效
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("login_ticket")
public class LoginTicket {
    @TableId
    private int id;

    @TableField("user_id")
    private int userId;

    private String ticket;
    private int status;
    private Date expired;
}
