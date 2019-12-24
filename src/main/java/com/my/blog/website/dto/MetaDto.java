package com.my.blog.website.dto;

import com.my.blog.website.module.admin.entity.Meta;
import lombok.Data;

@Data
public class MetaDto extends Meta {

    private int count;

}
