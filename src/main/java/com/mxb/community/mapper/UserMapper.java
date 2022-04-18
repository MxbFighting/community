package com.mxb.community.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mxb.community.domain.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author mxb
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    /**
     * 通过id改变状态
     * @param status
     * @param id
     * @return
     */
    int updateStatus(int status, Integer id);

    /**
     * 通过id改变头像路径
     * @param userId
     * @param headerUrl
     * @return
     */
    int updateHeader(int userId, String headerUrl);

}
