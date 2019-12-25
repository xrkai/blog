package com.my.blog.website.module.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.my.blog.website.module.admin.entity.Content;

/**
 * Created by Administrator on 2017/3/13 013.
 */
public interface IContentService {


    /**
     * 发布文章
     *
     * @param contents
     */
    void publish(Content contents);

    /**
     * 查询文章返回多条数据
     *
     * @param p     当前页
     * @param limit 每页条数
     * @return ContentVo
     */
    IPage<Content> getContents(Integer p, Integer limit);


    /**
     * 根据id或slug获取文章
     *
     * @param id id
     * @return ContentVo
     */
    Content getContents(String id);

    /**
     * 根据主键更新
     *
     * @param contentVo contentVo
     */
    void updateContentByCid(Content contentVo);


    /**
     * 查询分类/标签下的文章归档
     *
     * @param mid   mid
     * @param page  page
     * @param limit limit
     * @return ContentVo
     */
    Page<Content> getArticles(String mid, int page, int limit);

    /**
     * 搜索、分页
     *
     * @param keyword keyword
     * @param page    page
     * @param limit   limit
     * @return ContentVo
     */
    IPage<Content> getArticles(String keyword, Integer page, Integer limit);


    /**
     * @param page
     * @param limit
     * @param type
     * @return
     */
    IPage<Content> getArticlesWithPage(Integer page, Integer limit, String type);

    /**
     * 根据文章id删除
     *
     * @param cid
     */
    void deleteByCid(String cid);

    /**
     * 编辑文章
     *
     * @param contents
     */
    void updateArticle(Content contents);


    /**
     * 更新原有文章的category
     *
     * @param ordinal
     * @param newCatefory
     */
    void updateCategory(String ordinal, String newCatefory);
}
