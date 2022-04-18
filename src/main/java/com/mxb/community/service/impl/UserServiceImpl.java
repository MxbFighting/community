package com.mxb.community.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mxb.community.domain.entity.LoginTicket;
import com.mxb.community.domain.entity.User;
import com.mxb.community.mapper.LoginTicketMapper;
import com.mxb.community.mapper.UserMapper;
import com.mxb.community.service.UserService;
import com.mxb.community.utils.CommunityConstant;
import com.mxb.community.utils.CommunityUtil;
import com.mxb.community.utils.MailClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import sun.security.krb5.internal.Ticket;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author Mxb
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private LoginTicketMapper loginTicketMapper;

    @Autowired
    private MailClient mailClient;

    @Autowired
    private TemplateEngine templateEngine;

    @Value("${community.path.domain}")
    private String domain;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Override
    public Map<String, Object> register(User user) {
        Map<String, Object> map = new HashMap<>();
        //对空值进行判断, 若user为空, 抛出异常
        if (user == null) {
            throw new IllegalArgumentException("参数不能为空");
        }

        //对user的username进行判断, 如果为空, 则向map中存提示信息然后直接返回, 密码, 邮箱同理
        if (StringUtils.isBlank(user.getUsername())) {
            map.put("usernameMsg", "账号不能为空!");
            return map;
        }
        if (StringUtils.isBlank(user.getPassword())) {
            map.put("passwordMsg", "密码不能为空!");
            return map;
        }
        if (StringUtils.isBlank(user.getEmail())) {
            map.put("emailMsg", "邮箱不能为空!");
            return map;
        }

        //验证账号, 通过username从库里查询user, 如果账号存在, 则向map中存提示信息后直接返回, 邮箱同理
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, user.getUsername());
        User u = userMapper.selectOne(queryWrapper);
        if (u != null) {
            map.put("usernameMsg", "该账号已经存在!");
            return map;
        }

        queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getEmail, user.getEmail());
        User u1 = userMapper.selectOne(queryWrapper);
        if (u1 != null) {
            map.put("emailMsg", "该账号已经被注册!");
            return map;
        }

        //没问题了,现在可以开始注册
        //先生成salt, 然后对密码进行 password + salt , 在进行md5加密
        user.setSalt(CommunityUtil.generateUUID().substring(0,5));
        user.setPassword(CommunityUtil.md5(user.getPassword() + user.getSalt()));

        //对用户的状态进行设置, Type为0->普通用户  Status为0->没有激活
        user.setType(0);
        user.setStatus(0);

        //对用户激活码进行设置
        user.setActivationCode(CommunityUtil.generateUUID());

        //对用户的头像进行设置, 使用牛客网提供的1000个默认头像, 格式为 :http://images.nowcoder.com/head/%dt.png
        user.setHeaderUrl(String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));
        //对user的创建时间进行设置
        user.setCreateTime(new Date());
        //插入用户
        userMapper.insert(user);

        //之后进行激活, 给用户发送激活邮件, 给邮件模板设置值, Context中的值可以在页面动态显示
        Context context = new Context();
        context.setVariable("email", user.getEmail());
        //携带的地址是: http://localhost:8080/community/activation/101(id)/code(激活码)
        String url = domain + contextPath + "/activation/" + user.getId() + "/" + user.getActivationCode();
        context.setVariable("url", url);
        String content = templateEngine.process("/mail/activation", context);

        //然后发送邮件
        mailClient.sendMail(user.getEmail(), "激活账号", content);


        return map;
    }

    /**
     * 判断通过userid查询用户, 判断其的激活状态和code是否正确, 返回相应的状态码
     * @param userId
     * @param code
     * @return
     */
    @Override
    public int activation(int userId, String code) {
        User user = userMapper.selectById(userId);
        if (user.getStatus() == CommunityConstant.ACTIVATION_REPEAT) {
            return CommunityConstant.ACTIVATION_REPEAT;
        } else if (user.getActivationCode().equals(code)) {
            userMapper.updateStatus(1, userId);
            return CommunityConstant.ACTIVATION_SUCCESS;
        } else {
            return CommunityConstant.ACTIVATION_FAILURE;
        }
    }

    @Override
    public Map<String, Object> login(String username, String password, long expiredSeconds) {
        Map<String, Object> map = new HashMap<>();

        //对传入的username, password进行判断, 如果为空的话进行处理然后直接返回
        if (StringUtils.isBlank(username)) {
            map.put("usernameMsg", "账号不能为空");
            return map;
        }

        if (StringUtils.isBlank(password)) {
            map.put("usernameMsg", "账号不能为空");
            return map;
        }

        //验证账号
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, username);
        User user = userMapper.selectOne(queryWrapper);
        if (user == null) {
            map.put("usernameMsg", "该账号不存在");
            return map;
        }

        //验证状态
        if (user.getStatus() == 0) {
            map.put("usernameMsg", "该账号未激活");
            return map;
        }

        //验证密码
        password = CommunityUtil.md5(password + user.getSalt());
        if (!password.equals(user.getPassword())) {
            map.put("passwordMsg", "密码错误");
            return map;
        }

        //到这一步已经没问题就可以生成 登录凭证了
        //对登录凭证的属性进行设置, ticket是generateUUID产生的随机字符串
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(user.getId());
        loginTicket.setStatus(0);
        String ticket = CommunityUtil.generateUUID();
        loginTicket.setTicket(ticket);
        loginTicket.setExpired(new Date(System.currentTimeMillis() + expiredSeconds * 1000));

        //存到数据库
        loginTicketMapper.insertLoginTicket(loginTicket);

        //把tikcet字符串存到map里以便给浏览器发过去, 让浏览器记住你
        map.put("ticket", ticket);
        return map;
    }

    @Override
    public void logout(String ticket) {
        loginTicketMapper.updateStatus(ticket, 1);
    }

    @Override
    public LoginTicket findLoginTicket(String ticket) {
        return loginTicketMapper.selectByTicket(ticket);
    }

    @Override
    public int updateHeader(int userId, String headerUrl) {
        return userMapper.updateHeader(userId, headerUrl);
    }
}

