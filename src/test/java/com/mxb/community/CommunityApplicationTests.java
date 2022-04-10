package com.mxb.community;

import com.mxb.community.domain.entity.DiscussPost;
import com.mxb.community.mapper.DiscussPostMapper;
import com.mxb.community.mapper.UserMapper;
import com.mxb.community.service.DiscussPostService;
import com.mxb.community.utils.MailClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class CommunityApplicationTests {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DiscussPostMapper discussPostMapper;

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




}
