package com.my.blog.website.module.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.my.blog.website.module.admin.entity.Content;
import com.my.blog.website.module.admin.vo.ArchiveVO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface ContentMapper extends BaseMapper<Content> {

    List<ArchiveVO> findReturnArchiveBo();

    List<Content> findByCatalog(String mid);
}