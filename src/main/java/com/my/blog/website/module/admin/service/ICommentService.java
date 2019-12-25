package com.my.blog.website.module.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.my.blog.website.modal.Bo.CommentBo;
import com.my.blog.website.module.admin.entity.Comment;

/**
 * Created by BlueT on 2017/3/16.
 */
public interface ICommentService {

    /**
     * 保存对象
     *
     * @param commentVo
     */
    void insertComment(Comment commentVo);

    /**
     * 获取文章下的评论
     *
     * @param cid
     * @param page
     * @param limit
     * @return CommentBo
     */
    Page<CommentBo> getComments(String cid, int page, int limit);

    /**
     * 获取文章下的评论
     *
     * @param userId
     * @param page
     * @param limit
     * @return CommentVo
     */
    IPage<Comment> getCommentsWithPage(String userId, int page, int limit);


    /**
     * 根据主键查询评论
     *
     * @param coid
     * @return
     */
    Comment getCommentById(String coid);


    /**
     * 删除评论，暂时没用
     *
     * @param coid
     * @param cid
     * @throws Exception
     */
    void delete(String coid, String cid);

    /**
     * 更新评论状态
     *
     * @param comments
     */
    void update(Comment comments);

}
