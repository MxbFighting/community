package com.mxb.community.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mxb.community.domain.entity.DiscussPost;
import com.mxb.community.mapper.DiscussPostMapper;
import com.mxb.community.service.DiscussPostService;

import com.mxb.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Mxb
 */
@Service
public class DiscussPostServiceImpl extends ServiceImpl<DiscussPostMapper, DiscussPost> implements DiscussPostService{

    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Autowired
    private UserService userService;

    @Override
    public List<DiscussPost> selectDiscussPosts(Integer userId, int pageNum, int pageSize) {
        LambdaQueryWrapper<DiscussPost> queryWrapper = new LambdaQueryWrapper<>();

        //1. status不为2(拉黑状态)
        queryWrapper.ne(DiscussPost::getStatus, 2);

        //2. 对userId进行判断, 若为空,不做处理, 若不为空,拼接为sql
        if (userId != null) {
            queryWrapper.eq(DiscussPost::getUserId, userId);
        }

        //3. 帖子有置顶功能, 所以要对帖子进行排序, 且创建时间新的在前面
        queryWrapper.orderByDesc(DiscussPost::getType);
        queryWrapper.orderByDesc(DiscussPost::getCreateTime);

        //4. 进行分页
        Page<DiscussPost> page = new Page<>(pageNum, pageSize);
        page(page, queryWrapper);
        List<DiscussPost> records = page.getRecords();
        return records;

    }

    @Override
    public int selectDiscussPostRows(Integer userId) {
        LambdaQueryWrapper<DiscussPost> queryWrapper = new LambdaQueryWrapper<>();

        //1. status不为2(拉黑状态)
        queryWrapper.ne(DiscussPost::getStatus, 2);

        //2. 对userId进行判断, 若为空,不做处理, 若不为空,拼接为sql
        if (userId != null) {
            queryWrapper.eq(DiscussPost::getUserId, userId);
        }

        //3. 查询共有多少条记录
        Integer count = discussPostMapper.selectCount(queryWrapper);

        return count;


    }


}
