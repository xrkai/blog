package com.my.blog.website.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.my.blog.website.constant.WebConst;
import com.my.blog.website.dto.MetaDto;
import com.my.blog.website.dto.Types;
import com.my.blog.website.exception.TipException;
import com.my.blog.website.module.admin.entity.Content;
import com.my.blog.website.module.admin.entity.Meta;
import com.my.blog.website.module.admin.entity.Relationship;
import com.my.blog.website.module.admin.mapper.MetaMapper;
import com.my.blog.website.service.IContentService;
import com.my.blog.website.service.IMetaService;
import com.my.blog.website.service.IRelationshipService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by BlueT on 2017/3/17.
 */
@Service
@Slf4j
public class MetaServiceImpl implements IMetaService {

    @Resource
    private MetaMapper metaMapper;

    @Resource
    private IRelationshipService relationshipService;

    @Resource
    private IContentService contentService;

    @Override
    public MetaDto getMeta(String type, String name) {
        if (StringUtils.isNotEmpty(type) && StringUtils.isNotEmpty(name)) {
            return metaMapper.selectDtoByNameAndType(name, type);
        }
        return null;
    }

    @Override
    public Integer countMeta(String mid) {
        return metaMapper.countWithSql(mid);
    }

    @Override
    public List<Meta> getMetas(String types) {
        if (StringUtils.isNotEmpty(types)) {
            QueryWrapper<Meta> metaQueryWrapper = new QueryWrapper<>();
            metaQueryWrapper.lambda().eq(Meta::getType, types);
            metaQueryWrapper.orderByDesc("sort", "mid");
            return metaMapper.selectList(metaQueryWrapper);
        }
        return null;
    }

    @Override
    public List<MetaDto> getMetaList(String type, String orderby, int limit) {
        if (StringUtils.isNotEmpty(type)) {
            if (StringUtils.isEmpty(orderby)) {
                orderby = "count desc, a.mid desc";
            }
            if (limit < 1 || limit > WebConst.MAX_POSTS) {
                limit = 10;
            }
            Map<String, Object> paraMap = new HashMap<>(3);
            paraMap.put("type", type);
            paraMap.put("order", orderby);
            paraMap.put("limit", limit);
            return metaMapper.selectFromSql(paraMap);
        }
        return null;
    }

    @Override
    public void delete(String mid) {
        Meta metas = metaMapper.selectById(mid);
        if (null != metas) {
            String type = metas.getType();
            String name = metas.getName();

            metaMapper.deleteById(mid);
            List<Relationship> rlist = relationshipService.getRelationshipById(null, mid);
            if (null != rlist) {
                for (Relationship r : rlist) {
                    Content contents = contentService.getContents(String.valueOf(r.getCid()));
                    if (null != contents) {
                        Content temp = new Content();
                        temp.setCid(r.getCid());
                        if (type.equals(Types.CATEGORY.getType())) {
                            temp.setCategories(reMeta(name, contents.getCategories()));
                        }
                        if (type.equals(Types.TAG.getType())) {
                            temp.setTags(reMeta(name, contents.getTags()));
                        }
                        contentService.updateContentByCid(temp);
                    }
                }
            }
            relationshipService.deleteById(null, mid);
        }
    }

    @Override
    public void saveMeta(String type, String name, String mid) {
        if (StringUtils.isNotEmpty(type) && StringUtils.isNotEmpty(name)) {
            QueryWrapper<Meta> metaQueryWrapper = new QueryWrapper<>();
            metaQueryWrapper.lambda().eq(Meta::getType, type).eq(Meta::getName, name);
            List<Meta> metaVos = metaMapper.selectList(metaQueryWrapper);
            Meta metas;
            if (metaVos.size() != 0) {
                throw new TipException("已经存在该项");
            } else {
                metas = new Meta();
                metas.setName(name);
                if (null != mid) {
                    Meta original = metaMapper.selectById(mid);
                    metas.setMid(mid);
                    metaMapper.updateById(metas);
//                    更新原有文章的categories
                    contentService.updateCategory(original.getName(), name);
                } else {
                    metas.setType(type);
                    metaMapper.insert(metas);
                }
            }
        }
    }

    @Override
    public void saveMetas(String cid, String names, String type) {
        if (null == cid) {
            throw new TipException("项目关联id不能为空");
        }
        if (StringUtils.isNotEmpty(names) && StringUtils.isNotEmpty(type)) {
            String[] nameArr = StringUtils.split(names, ",");
            for (String name : nameArr) {
                this.saveOrUpdate(cid, name, type);
            }
        }
    }

    private void saveOrUpdate(String cid, String name, String type) {

        QueryWrapper<Meta> metaQueryWrapper = new QueryWrapper<>();
        metaQueryWrapper.lambda().eq(Meta::getType, type).eq(Meta::getName, name);

        List<Meta> metaList = metaMapper.selectList(metaQueryWrapper);
        String mid;
        Meta metas;
        if (metaList.size() == 1) {
            metas = metaList.get(0);
            mid = metas.getMid();
        } else if (metaList.size() > 1) {
            throw new TipException("查询到多条数据");
        } else {
            metas = new Meta();
            metas.setSlug(name);
            metas.setName(name);
            metas.setType(type);
            metaMapper.insert(metas);
            mid = metas.getMid();
        }
        if (!"0".equals(mid)) {
            Long count = relationshipService.countById(cid, mid);
            if (count == 0) {
                Relationship relationships = new Relationship();
                relationships.setCid(cid);
                relationships.setMid(mid);
                relationshipService.insertVo(relationships);
            }
        }
    }


    private String reMeta(String name, String metas) {
        String[] ms = StringUtils.split(metas, ",");
        StringBuilder sbuf = new StringBuilder();
        for (String m : ms) {
            if (!name.equals(m)) {
                sbuf.append(",").append(m);
            }
        }
        if (sbuf.length() > 0) {
            return sbuf.substring(1);
        }
        return "";
    }

    @Override
    public void saveMeta(Meta metas) {
        if (null != metas) {
            metaMapper.insert(metas);
        }
    }

    @Override
    public void update(Meta metas) {
        if (null != metas && null != metas.getMid()) {
            metaMapper.updateById(metas);
        }
    }
}
