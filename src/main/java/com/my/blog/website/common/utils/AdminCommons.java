package com.my.blog.website.common.utils;


import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.my.blog.website.module.admin.entity.Meta;
import org.springframework.stereotype.Component;

/**
 * 后台公共函数
 * <p>
 * Created by 13 on 2017/2/21.
 */
@Component
public final class AdminCommons {

    private static final String[] COLORS = {"default", "primary", "success", "info", "warning", "danger", "inverse", "purple", "pink"};

    /**
     * 判断category和cat的交集
     *
     * @param cats
     * @return
     */
    public static boolean exist_cat(Meta category, String cats) {
        String[] arr = StringUtils.split(cats, ",");
        if (null != arr && arr.length > 0) {
            for (String c : arr) {
                if (c.trim().equals(category.getName())) {
                    return true;
                }
            }
        }
        return false;
    }

    public static String rand_color() {
        int r = Tools.rand(0, COLORS.length - 1);
        return COLORS[r];
    }

}
