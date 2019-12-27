package com.my.blog.website;

import com.my.blog.website.common.utils.TaleUtils;
import com.my.blog.website.module.admin.entity.User;

/**
 * Created by shuaihan on 2017/4/2.
 */
public class Pwdtest {
    public static void main(String args[]) {
        User user = new User();
        user.setUsername("admin");
        user.setPassword("asdfasdfs");
        String encodePwd = TaleUtils.MD5encode(user.getUsername() + user.getPassword());
        System.out.println(encodePwd);
    }
}
