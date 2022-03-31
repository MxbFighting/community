package com.mxb.community.controller;

import com.mxb.community.domain.entity.DiscussPost;
import com.mxb.community.domain.entity.Page;
import com.mxb.community.domain.entity.User;
import com.mxb.community.service.DiscussPostService;
import com.mxb.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Controller
public class HomeController {
    @Autowired
    private UserService userService;

    @Autowired
    private DiscussPostService discussPostService;

    @GetMapping("/index")
    public String getIndexPage(Model model, Page page) {
        page.setRows(discussPostService.selectDiscussPostRows(null));
        page.setPath("/index");

        List<DiscussPost> discussPosts = discussPostService.selectDiscussPosts(null, page.getCurrent(), page.getLimit());
        List<Map<String, Object>> list = new LinkedList<>();
        if (discussPosts != null) {
            for (DiscussPost discussPost : discussPosts) {
                Map<String, Object> map = new HashMap<>();
                map.put("post", discussPost);
                User user = userService.getById(discussPost.getUserId());
                map.put("user", user);
                list.add(map);
            }
        }

        model.addAttribute("discussPosts", list);
        return "/index";
    }
}
