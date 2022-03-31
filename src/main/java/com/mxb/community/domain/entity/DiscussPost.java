package com.mxb.community.domain.entity;

import java.util.Date;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * (DiscussPost)表实体类
 *
 * @author Mxb
 * type 0-普通; 1-置顶;
 * status 0-正常; 1-精华; 2-拉黑;
 *
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("discuss_post")
public class DiscussPost  {
    @TableId
    private Integer id;

    private String userId;
    
    private String title;
    
    private String content;

    private Integer type;

    private Integer status;
    
    private Date createTime;
    
    private Integer commentCount;
    
    private Double score;
}

