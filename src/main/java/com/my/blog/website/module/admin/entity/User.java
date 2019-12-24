package com.my.blog.website.module.admin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "t_users")
public class User {
    /**
     * 必须：ID唯一标识 @GeneratedValue 动态生成UUID策略
     */
    @TableId(type = IdType.UUID)
    @TableField("uid")
    private String uid;
    /**
     * 用户名称
     */
    @TableField("username")
    private String username;
    /**
     * 用户密码
     */
    @TableField("password")
    private String password;
    /**
     * 用户的邮箱
     */
    @TableField("email")
    private String email;
    /**
     * 用户的主页
     */
    @TableField("home_url")
    private String homeUrl;
    /**
     * 用户显示的名称
     */
    @TableField("screen_name")
    private String screenName;
    /**
     * 用户注册时的GMT unix时间戳
     */
    @TableField("created")
    private Integer created;
    /**
     * 最后活动时间
     */
    @TableField("activated")
    private Integer activated;
    /**
     * 上次登录最后活跃时间
     */
    @TableField("logged")
    private Integer logged;
    /**
     * 用户组
     */
    @TableField("group_name")
    private String groupName;
}