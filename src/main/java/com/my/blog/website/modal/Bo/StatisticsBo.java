package com.my.blog.website.modal.Bo;

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
public class StatisticsBo implements Serializable {

    private Integer articles;
    private Integer comments;
    private Integer links;
    private Integer attachs;

}
