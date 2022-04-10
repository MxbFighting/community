package com.mxb.community.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import java.util.UUID;

@Component
public class CommunityUtil {
    /**
     * 生成随机字符串
     * @return
     */
    public static String generateUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 对密码进行MD5加密
     * 在密码后面加点salt, 就是随机字符串, 不容易被破解
     *   eg:  hello->abc123
     *        hello + 3e4ea -> abc123abc   这个3e4ea就是salt
     * @param key 要加密的字符串
     * @return
     */
    public static String md5(String key) {
        if (StringUtils.isBlank(key)) {
            return null;
        }
        return DigestUtils.md5DigestAsHex(key.getBytes());
    }
}
