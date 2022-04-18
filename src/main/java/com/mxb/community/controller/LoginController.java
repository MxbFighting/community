package com.mxb.community.controller;

import com.google.code.kaptcha.Producer;
import com.mxb.community.domain.entity.User;
import com.mxb.community.service.UserService;
import com.mxb.community.utils.CommunityConstant;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.imageio.ImageIO;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private Producer kaptchaProducer;

    @Value("${server.servlet.context-path}")
    private String contextPath;




    @GetMapping("/register")
    public String getRegisterPage() {
        return "/site/register";
    }

    @GetMapping("/login")
    public String getLoginPage() {
        return "/site/login";
    }


    @PostMapping("/register")
    public String register(Model model, User user) {
        Map<String, Object> map = userService.register(user);
        //如果map为null或者map里面为空, 相当于用户注册成功, 跳转到首页
        if (map == null || map.isEmpty()) {
            model.addAttribute("msg", "注册成功, 我们已经向您的邮箱发送了一封激活邮件, 请尽快激活");
            model.addAttribute("target", "/index");
            //跳转到第三页面, 提示用户注册成功
            return "/site/operate-result";
        } else {
            //没成功, 就向model中添加错误信息, 然后跳转到注册页面
            model.addAttribute("usernameMsg", map.get("usernameMsg"));
            model.addAttribute("passwordMsg", map.get("passwordMsg"));
            model.addAttribute("emailMsg", map.get("emailMsg"));
            return "/site/register";

        }
    }

    @GetMapping("/activation/{userId}/{code}")
    public String activation(Model model, @PathVariable("userId") int userId, @PathVariable("code") String code) {
        int activationCode = userService.activation(userId, code);
        if (activationCode == CommunityConstant.ACTIVATION_SUCCESS) {
            model.addAttribute("msg", "您的账号已经可以正常使用了");
            model.addAttribute("target", "/login");
        } else if (activationCode == CommunityConstant.ACTIVATION_REPEAT) {
            model.addAttribute("msg", "操作无效, 该账号已经被激活");
            model.addAttribute("target", "/index");
        } else {
            model.addAttribute("msg", "您提供的激活码不正确");
            model.addAttribute("target", "/index");
        }
        return "/site/operate-result";
    }

    @GetMapping("/kaptcha")
    public void getKaptcha(HttpServletResponse response, HttpSession session) {
        //生成验证码
        String text = kaptchaProducer.createText();
        BufferedImage image = kaptchaProducer.createImage(text);

        //将验证码存入session
        session.setAttribute("kaptcha", text);

        //将图片输出给浏览器
        response.setContentType("image/png");

        try {
            OutputStream outputStream = response.getOutputStream();
            ImageIO.write(image, "png", outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     *
     * @param username 账号
     * @param password 密码
     * @param code 验证码
     * @param rememberMe 记住我选项, 为true则过期时间长一点
     * @param model
     * @param session 取出之前生成的验证码与用户写入的验证码进行对比
     * @param response 让客户端保存ticket, 用cookie保存
     * @return
     */
    @PostMapping("/login")
    public String login(String username, String password, String code, Boolean rememberMe,
                        Model model, HttpSession session, HttpServletResponse response) {
        //将之前存入的验证码取出
        String kaptcha = (String) session.getAttribute("kaptcha");

        //判断验证码  如果存入或者取出的验证码为空 或者两个验证码在忽略大小的情况下不相等
        if (StringUtils.isBlank(kaptcha) || StringUtils.isBlank(code) || !code.equalsIgnoreCase(kaptcha)) {
            model.addAttribute("codeMsg", "验证码不正确");
            //不对的话, 继续跳转到登录页面
            return "/site/login";
        }
        //通过判断用户是否选择remember来给expiredSeconds赋值
        int expiredSeconds;
        if (rememberMe == null) {
            expiredSeconds = CommunityConstant.DEFAULT_EXPIRED_SECONDS;
        } else {
            expiredSeconds = CommunityConstant.REMEMBER_EXPIRED_SECONDS;
        }

        //通过service层进行登录
        Map<String, Object> map = userService.login(username, password, expiredSeconds);
        if (map.containsKey("ticket")) {
            Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
            //给cookie设置有效路径, 应该是包含整个项目的
            cookie.setPath(contextPath);
            //给cookie设置过期时间
            cookie.setMaxAge(expiredSeconds);
            //发cookie到客户端
            response.addCookie(cookie);
            //然后直接重定向到/index, 至于为什么不是直接 return "/index" 是因为当前的请求是登录, 却给浏览器返回了首页的模板, 逻辑不正确
            //重定向是重新发起了一个请求, 浏览器的地址发生了改变, 逻辑是严谨的
            return "redirect:/index";
        } else {
            //不包含ticket就肯定是出错了
            //向model中添加错误信息
            model.addAttribute("usernameMsg", map.get("usernameMsg"));
            model.addAttribute("passwordMsg", map.get("passwordMsg"));
            return "/site/login";
        }

    }

    @GetMapping("/logout")
    public String logout(@CookieValue("ticket") String ticket) {
        userService.logout(ticket);
        return "redirect:/login";
    }

}
