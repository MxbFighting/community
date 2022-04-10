package com.mxb.community.controller;

import com.mxb.community.domain.entity.User;
import com.mxb.community.service.UserService;
import com.mxb.community.utils.CommunityConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Map;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

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



}
