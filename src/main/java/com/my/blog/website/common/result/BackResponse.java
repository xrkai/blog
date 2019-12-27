package com.my.blog.website.common.result;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by 13 on 2017/2/25.
 */
@Data
public class BackResponse implements Serializable {

    private String attachPath;

    private String themePath;

    private String sqlPath;

}
