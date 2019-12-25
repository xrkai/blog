package com.my.blog.website.module.admin.service;

import com.my.blog.website.module.admin.entity.Relationship;

import java.util.List;

/**
 * Created by BlueT on 2017/3/18.
 */
public interface IRelationshipService {
    /**
     * 按住键删除
     *
     * @param cid
     * @param mid
     */
    void deleteById(String cid, String mid);

    /**
     * 按主键统计条数
     *
     * @param cid
     * @param mid
     * @return 条数
     */
    Long countById(String cid, String mid);


    /**
     * 保存對象
     *
     * @param relationshipVoKey
     */
    void insertVo(Relationship relationshipVoKey);

    /**
     * 根据id搜索
     *
     * @param cid
     * @param mid
     * @return
     */
    List<Relationship> getRelationshipById(String cid, String mid);
}
