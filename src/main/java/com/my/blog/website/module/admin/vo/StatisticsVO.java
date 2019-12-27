package com.my.blog.website.module.admin.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 后台统计对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatisticsVO implements Serializable {

    private Integer articles;

    private Integer comments;

    private Integer links;

    private Integer attachs;

}
