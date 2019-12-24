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
@TableName(value = "t_contents")
public class Content {
    /**
     * 必须：ID唯一标识 @GeneratedValue 动态生成UUID策略
     */
    @TableId(type = IdType.UUID)
    @TableField("cid")
    private String cid;
    /**
     * 内容标题
     */
    @TableField("title")
    private String title;
    /**
     * 内容缩略名
     */
    @TableField("slug")
    private String slug;
    /**
     * 内容生成时的GMT unix时间戳
     */
    @TableField("created")
    private Integer created;
    /**
     * 内容更改时的GMT unix时间戳
     */
    @TableField("modified")
    private Integer modified;
    /**
     * 内容所属用户id
     */
    @TableField("author_id")
    private String authorId;
    /**
     * 内容类别
     */
    @TableField("type")
    private String type;
    /**
     * 内容状态
     */
    @TableField("status")
    private String status;
    /**
     * 标签列表
     */
    @TableField("tags")
    private String tags;
    /**
     * 分类列表
     */
    @TableField("categories")
    private String categories;
    /**
     * 点击次数
     */
    @TableField("hits")
    private String hits;
    /**
     * 内容所属评论数
     */
    @TableField("comments_num")
    private Integer commentsNum;
    /**
     * 是否允许评论
     */
    @TableField("allow_comment")
    private Boolean allowComment;
    /**
     * 是否允许ping
     */
    @TableField("allow_ping")
    private Boolean allowPing;
    /**
     * 允许出现在聚合中
     */
    @TableField("allow_feed")
    private Boolean allowFeed;
    /**
     * 内容文字
     */
    @TableField("content")
    private String content;
}