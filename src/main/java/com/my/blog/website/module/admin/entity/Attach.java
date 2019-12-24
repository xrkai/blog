package com.my.blog.website.module.admin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "t_attach")
public class Attach {

    /**
     * 必须：ID唯一标识 @GeneratedValue 动态生成UUID策略
     */
    @TableId(type = IdType.UUID)
    @TableField("id")
    private String id;
    /**
     * 模型名称
     */
    @TableField(value = "fname")
    private String fname;
    /**
     * 模型名称
     */
    @TableField(value = "ftype")
    private String ftype;
    /**
     * 模型名称
     */
    @TableField(value = "fkey")
    private String fkey;
    /**
     * 模型名称
     */
    @TableField(value = "author_id")
    private String authorId;

    /**
     * 模型名称
     */
    @TableField(value = "created")
    private Integer created;


}