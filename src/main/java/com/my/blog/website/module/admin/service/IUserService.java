package com.my.blog.website.module.admin.service;


import com.my.blog.website.module.admin.entity.User;

/**
 * Created by BlueT on 2017/3/3.
 */
public interface IUserService {

    /**
     * 保存用户数据
     *
     * @param User 用户数据
     * @return 主键
     */

    String insertUser(User User);

    /**
     * 通过uid查找对象
     *
     * @param uid
     * @return
     */
    User queryUserById(Integer uid);

    /**
     * 用戶登录
     *
     * @param username
     * @param password
     * @return
     */
    User login(String username, String password);

    /**
     * 根据主键更新user对象
     *
     * @param User
     * @return
     */
    void updateByUid(User User);
}
