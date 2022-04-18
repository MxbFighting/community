package com.mxb.community;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mxb.community.domain.entity.DiscussPost;
import com.mxb.community.domain.entity.LoginTicket;
import com.mxb.community.domain.entity.User;
import com.mxb.community.mapper.DiscussPostMapper;
import com.mxb.community.mapper.LoginTicketMapper;
import com.mxb.community.mapper.UserMapper;
import com.mxb.community.service.DiscussPostService;
import com.mxb.community.utils.MailClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;

@SpringBootTest
class CommunityApplicationTests {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Autowired
    private LoginTicketMapper loginTicketMapper;

    @Autowired
    private DiscussPostService discussPostService;

    @Test
    public void test01() {

        List<DiscussPost> discussPosts = discussPostService.selectDiscussPosts(149, 0, 10);
        for (DiscussPost discussPost : discussPosts) {
            System.out.println(discussPost);
        }

        int i = discussPostService.selectDiscussPostRows(149);
        System.out.println(i);

    }
    @Test
    public void test02() {
        int i = discussPostService.selectDiscussPostRows(149);
        System.out.println(i);
    }

    @Autowired
    private MailClient mailClient;

    @Test
    public void testSendMail() {
        mailClient.sendMail("1711293190@qq.com", "测试springboot发邮寄", "springboot发邮件");
    }


    @Test
    public void testInsertLoginTicket() {
        LoginTicket loginTicket = new LoginTicket( );
        loginTicket.setTicket("qweqwe");
        loginTicket.setStatus(1);
        loginTicket.setExpired(new Date());
        loginTicket.setUserId(1);
        loginTicketMapper.insertLoginTicket(loginTicket);

    }
    @Test
    public void testInsertAndUpdateTicket() {
        LoginTicket loginTicket = loginTicketMapper.selectByTicket("qweqwe");
        System.out.println(loginTicket);

        System.out.println();
        loginTicketMapper.updateStatus("qweqwe", 1);
        loginTicket = loginTicketMapper.selectByTicket("qweqwe");
        System.out.println(loginTicket);

    }

    @Test
    public void test10() {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, "lcy");
        User user = userMapper.selectOne(queryWrapper);
        System.out.println(user.getPassword());

    }






}
