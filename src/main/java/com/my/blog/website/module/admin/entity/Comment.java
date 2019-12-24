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
@TableName(value = "t_comments")
public class Comment {
    /**
     * 必须：ID唯一标识 @GeneratedValue 动态生成UUID策略
     */
    @TableId(type = IdType.UUID)
    @TableField("coid")
    private String coid;
    /**
     * post表主键,关联字段
     */
    @TableField("cid")
    private String cid;
    /**
     * 评论生成时的GMT unix时间戳
     */
    @TableField("created")
    private Integer created;
    /**
     * 评论作者
     */
    @TableField("author")
    private String author;
    /**
     * 评论所属用户id
     */
    @TableField("author_id")
    private String authorId;
    /**
     * 评论所属内容作者id
     */
    @TableField("owner_id")
    private String ownerId;
    /**
     * 评论者邮件
     */
    @TableField("mail")
    private String mail;
    /**
     * 评论者网址
     */
    @TableField("url")
    private String url;
    /**
     * 评论者ip地址
     */
    @TableField("ip")
    private String ip;
    /**
     * 评论者客户端
     */
    @TableField("agent")
    private String agent;
    /**
     * 评论类型
     */
    @TableField("type")
    private String type;
    /**
     * 评论状态
     */
    @TableField("status")
    private String status;
    /**
     * 父级评论
     */
    @TableField("parent")
    private String parent;
    /**
     * 评论内容
     */
    @TableField("content")
    private String content;
}