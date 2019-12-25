package com.my.blog.website.module.admin.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.my.blog.website.module.admin.entity.Relationship;
import com.my.blog.website.module.admin.mapper.RelationshipMapper;
import com.my.blog.website.module.admin.service.IRelationshipService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by BlueT on 2017/3/18.
 */
@Service
@Slf4j
public class RelationshipServiceImpl implements IRelationshipService {

    @Resource
    private RelationshipMapper relationshipMapper;

    @Override
    public void deleteById(String cid, String mid) {
        relationshipMapper.delete(getQueryWrapperByCidAndMid(cid, mid));
    }

    @Override
    public List<Relationship> getRelationshipById(String cid, String mid) {
        return relationshipMapper.selectList(getQueryWrapperByCidAndMid(cid, mid));
    }

    @Override
    public void insertVo(Relationship relationship) {
        relationshipMapper.insert(relationship);
    }

    @Override
    public Long countById(String cid, String mid) {
        log.debug("Enter countById method:cid={},mid={}", cid, mid);
        long num = relationshipMapper.selectCount(getQueryWrapperByCidAndMid(cid, mid));
        log.debug("Exit countById method return num={}", num);
        return num;
    }

    private QueryWrapper<Relationship> getQueryWrapperByCidAndMid(String cid, String mid) {
        QueryWrapper<Relationship> relationshipQueryWrapper = new QueryWrapper<>();
        if (cid != null) {
            relationshipQueryWrapper.lambda().eq(Relationship::getCid, cid);
        }
        if (mid != null) {
            relationshipQueryWrapper.lambda().eq(Relationship::getMid, mid);
        }
        return relationshipQueryWrapper;
    }
}
