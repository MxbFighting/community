package com.mxb.community.utils;

import com.mxb.community.domain.entity.User;
import org.springframework.stereotype.Component;

/**
 * 因为浏览器对服务器的访问是多线程的, 就需要有一个容器来代替session来持有用户信息
 * @author Mxb
 */
@Component
public class HostHolder {
    /**
     * ThreadLocal是实现了线程隔离的, 他通过当前线程存或取数据
     */
    private ThreadLocal<User> users = new ThreadLocal<>();

    public void setUsers(User user) {
        users.set(user);
    }

    public User getUser() {
        return users.get();
    }

    public void clear() {
        users.remove();
    }



}
