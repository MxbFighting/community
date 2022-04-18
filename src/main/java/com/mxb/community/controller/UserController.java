package com.mxb.community.controller;

import com.mxb.community.annotation.LoginRequired;
import com.mxb.community.domain.entity.User;
import com.mxb.community.mapper.UserMapper;
import com.mxb.community.service.UserService;
import com.mxb.community.utils.CommunityUtil;
import com.mxb.community.utils.HostHolder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * @author Mxb
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Value("${community.path.upload}")
    private String uploadPath;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Value("${community.path.domain}")
    private String domain;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private UserService userService;



    @LoginRequired
    @GetMapping("/setting")
    public String getSettingPage() {
        return "/site/setting";
    }

    /**
     *
     * @param headerImage 是MVC提供的上传文件的一个对象
     * @param model Model是要在页面上显示一些数据
     * @return 返回页面路径
     */
    @LoginRequired
    @PostMapping("/upload")
    public String uploadHeader(MultipartFile headerImage, Model model) {

        if (headerImage == null) {
            model.addAttribute("error", "您还没有选择图片");
            return "/site/setting";
        }

        //获取文件的原始名字
        String fileName = headerImage.getOriginalFilename();
        //获取文件的后缀
        //如果fileName为null, 则抛出异常
        assert fileName != null;
        //截取文件最后一个.之后的内容获得后缀
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        if (StringUtils.isBlank(suffix)) {
            model.addAttribute("error", "文件的格式不正确");
            return "/site/setting";
        }

        //生成随机文件名
        fileName = CommunityUtil.generateUUID() + suffix;
        //确定文件的存放的路径
        File dest = new File(uploadPath + "/" + fileName);
        //存储文件
        try {
            headerImage.transferTo(dest);
        } catch (IOException e) {
            throw new RuntimeException("上传文件失败, 服务器发生异常", e);
        }

        //更新当前用户的头像的路径, 是Web访问路径
        // http://localhost:8080/community/user/header/文件名.xxx(png)

        //获取当前登录的用户
        User user = hostHolder.getUser();
        //动态的拼出头像路径
        String url = domain + contextPath + "/user/header/" + fileName;
        //更改用户头像路径
        userService.updateHeader(user.getId(), url);
        //重定向到首页
        return "redirect:/index";

    }

    /**
     * 获取头像的方法, 这个方法是向浏览器响应一个二进制的图片文件, 所以需要流
     * @param fileName 文件名
     * @param response 向浏览器响应东西
     */
    @GetMapping("/header/{fileName}")
    public void getHeader(@PathVariable("fileName") String fileName, HttpServletResponse response) {
        //服务器存放路径
        fileName = uploadPath + "/" + fileName;
        //文件后缀
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        //响应图片, 图片的格式是固定的: image/后缀
        response.setContentType("image/" + suffix);

        //开始二进制文件的传输
        //在try后()中声明的变量都会自动调用close()方法, 前提是有close方法
        try (
                FileInputStream fs = new FileInputStream(fileName);
                OutputStream os = response.getOutputStream();
        ) {
            //缓冲区
            byte [] buffer = new byte[1024];
            //设置游标
            int cursor = 0;
            //如果cursor != -1, 就说明读到了数据
            while ((cursor = fs.read(buffer)) != -1) {
                os.write(buffer, 0, cursor);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
