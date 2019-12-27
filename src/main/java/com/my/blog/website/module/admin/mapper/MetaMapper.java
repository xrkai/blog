package com.my.blog.website.module.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.my.blog.website.module.admin.entity.Meta;
import com.my.blog.website.module.admin.entity.MetaDto;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public interface MetaMapper extends BaseMapper<Meta> {

    List<MetaDto> selectFromSql(Map<String, Object> paraMap);

    MetaDto selectDtoByNameAndType(@Param("name") String name, @Param("type") String type);

    Integer countWithSql(@Param("mid") String mid);
}