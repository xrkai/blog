package com.my.blog.website.module.admin.vo;

import com.my.blog.website.module.admin.entity.Content;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 13 on 2017/2/23.
 */
@Data
public class ArchiveVO implements Serializable {

    private String date;

    private String count;

    private List<Content> articles;

}
