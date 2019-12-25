package com.my.blog.website.common.page;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BasePageUtils {

    public List<Long> getPageList(Page page) {
        List<Long> pageList = new ArrayList<>();
        Long pages = page.getPages();
        for (int i = 1; i <= pages; i++) {
            pageList.add(Long.valueOf(i));
        }
        return pageList;
    }

}
