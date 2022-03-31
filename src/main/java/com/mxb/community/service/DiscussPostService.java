package com.mxb.community.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mxb.community.domain.entity.DiscussPost;

import java.util.List;

/**
 * @author Mxb
 */
public interface DiscussPostService extends IService<DiscussPost> {
    /**
     * 查询文章
     * @param userId 用户id
     * @param pageNum 当前页
     * @param pageSize 每页多少条数据
     * @return 返回文章
     */
    List<DiscussPost> selectDiscussPosts(Integer userId, int pageNum, int pageSize);

    /**
     * 若userId不为空, 则查询对应userId文章数量
     * 若userId为空, 则查询所有文章的数量
     * @param userId 用户id
     * @return 返回文章数量
     */
    int selectDiscussPostRows(Integer userId);
}
