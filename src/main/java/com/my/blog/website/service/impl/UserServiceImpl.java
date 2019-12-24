package com.my.blog.website.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.my.blog.website.exception.TipException;
import com.my.blog.website.module.admin.entity.User;
import com.my.blog.website.module.admin.mapper.UserMapper;
import com.my.blog.website.service.IUserService;
import com.my.blog.website.utils.TaleUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by BlueT on 2017/3/3.
 */
@Service
public class UserServiceImpl implements IUserService {


    @Resource
    private UserMapper userMapper;

    @Override
    public String insertUser(User userVo) {
        Integer uid = null;
        if (StringUtils.isNotEmpty(userVo.getUsername()) && StringUtils.isNotEmpty(userVo.getEmail())) {
//            用户密码加密
            String encodePwd = TaleUtils.MD5encode(userVo.getUsername() + userVo.getPassword());
            userVo.setPassword(encodePwd);
            userMapper.insert(userVo);
        }
        return userVo.getUid();
    }

    @Override
    public User queryUserById(Integer uid) {
        User userVo = null;
        if (uid != null) {
            userVo = userMapper.selectById(uid);
        }
        return userVo;
    }

    @Override
    public User login(String username, String password) {
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            throw new TipException("用户名和密码不能为空");
        }
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.lambda().eq(User::getUsername, username);
        Integer count = userMapper.selectCount(userQueryWrapper);
        if (count < 1) {
            throw new TipException("不存在该用户");
        }
        String pwd = TaleUtils.MD5encode(username + password);
        userQueryWrapper.lambda().eq(User::getPassword, pwd);
        List<User> userVos = userMapper.selectList(userQueryWrapper);
        if (userVos.size() != 1) {
            throw new TipException("用户名或密码错误");
        }
        return userVos.get(0);
    }

    @Override
    public void updateByUid(User user) {
        if (null == user || null == user.getUid()) {
            throw new TipException("userVo is null");
        }
        int i = userMapper.updateById(user);
        if (i != 1) {
            throw new TipException("update user by uid and retrun is not one");
        }
    }
}
