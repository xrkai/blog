package com.my.blog.website.module.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.my.blog.website.modal.Bo.ArchiveBo;
import com.my.blog.website.module.admin.entity.Content;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface ContentMapper extends BaseMapper<Content> {

    List<ArchiveBo> findReturnArchiveBo();

    List<Content> findByCatalog(String mid);
}