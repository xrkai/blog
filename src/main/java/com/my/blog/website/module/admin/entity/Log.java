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
@TableName(value = "t_logs")
public class Log {
    /**
     * 必须：ID唯一标识 @GeneratedValue 动态生成UUID策略
     */
    @TableId(type = IdType.UUID)
    @TableField("id")
    private String id;
    /**
     * 产生的动作
     */
    @TableField("action")
    private String action;
    /**
     * 产生的数据
     */
    @TableField("data")
    private String data;
    /**
     * 发生人id
     */
    @TableField("author_id")
    private String authorId;
    /**
     * 日志产生的ip
     */
    @TableField("ip")
    private String ip;
    /**
     * 日志创建时间
     */
    @TableField("created")
    private Integer created;
}