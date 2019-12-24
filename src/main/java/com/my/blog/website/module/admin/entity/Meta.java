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
@TableName(value = "t_metas")
public class Meta {
    /**
     * 必须：ID唯一标识 @GeneratedValue 动态生成UUID策略
     */
    @TableId(type = IdType.UUID)
    @TableField("mid")
    private String mid;
    /**
     * 名称
     */
    @TableField("name")
    private String name;
    /**
     * 项目缩略名
     */
    @TableField("slug")
    private String slug;
    /**
     * 项目类型
     */
    @TableField("type")
    private String type;
    /**
     * 选项描述
     */
    @TableField("description")
    private String description;
    /**
     * 项目排序
     */
    @TableField("sort")
    private Integer sort;

    @TableField("parent")
    private String parent;

}