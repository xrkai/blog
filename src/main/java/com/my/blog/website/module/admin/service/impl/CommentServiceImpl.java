package com.my.blog.website.module.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.my.blog.website.common.exception.TipException;
import com.my.blog.website.common.utils.DateKit;
import com.my.blog.website.common.utils.TaleUtils;
import com.my.blog.website.module.admin.entity.Comment;
import com.my.blog.website.module.admin.entity.Content;
import com.my.blog.website.module.admin.mapper.CommentMapper;
import com.my.blog.website.module.admin.service.ICommentService;
import com.my.blog.website.module.admin.service.IContentService;
import com.my.blog.website.module.admin.vo.CommentVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by BlueT on 2017/3/16.
 */
@Service
@Slf4j
public class CommentServiceImpl implements ICommentService {

    @Resource
    private CommentMapper commentMapper;

    @Resource
    private IContentService contentService;

    @Override
    public void insertComment(Comment comments) {
        if (null == comments) {
            throw new TipException("评论对象为空");
        }
        if (StringUtils.isEmpty(comments.getAuthor())) {
            comments.setAuthor("热心网友");
        }
        if (StringUtils.isNotEmpty(comments.getMail()) && !TaleUtils.isEmail(comments.getMail())) {
            throw new TipException("请输入正确的邮箱格式");
        }
        if (StringUtils.isEmpty(comments.getContent())) {
            throw new TipException("评论内容不能为空");
        }
        if (comments.getContent().length() < 5 || comments.getContent().length() > 2000) {
            throw new TipException("评论字数在5-2000个字符");
        }
        if (null == comments.getCid()) {
            throw new TipException("评论文章不能为空");
        }
        Content contents = contentService.getContents(comments.getCid());
        if (null == contents) {
            throw new TipException("不存在的文章");
        }
        comments.setOwnerId(contents.getAuthorId());
        comments.setCreated(DateKit.getCurrentUnixTime());
        if (StringUtils.isEmpty(comments.getParent())) {
            comments.setParent("0");
        }
        commentMapper.insert(comments);

        Content temp = new Content();
        temp.setCid(contents.getCid());
        temp.setCommentsNum(contents.getCommentsNum() + 1);
        contentService.updateContentByCid(temp);
    }

    @Override
    public Page<CommentVO> getComments(String cid, int page, int limit) {
        if (null != cid) {
            QueryWrapper<Comment> commentQueryWrapper = new QueryWrapper<>();
            commentQueryWrapper.lambda().eq(Comment::getCid, cid).eq(Comment::getParent, 0);
            commentQueryWrapper.orderByDesc("coid");
            List<Comment> parents = commentMapper.selectList(commentQueryWrapper);

            Page<Comment> commentPaginator = new Page<>();
            commentPaginator.setRecords(parents);
            Page<CommentVO> returnBo = copyPage(commentPaginator);
            if (parents.size() != 0) {
                List<CommentVO> comments = new ArrayList<>(parents.size());
                parents.forEach(parent -> {
                    CommentVO comment = new CommentVO(parent);
                    comments.add(comment);
                });
                returnBo.setRecords(comments);
            }
            return returnBo;
        }
        return null;
    }

    @Override
    public IPage<Comment> getCommentsWithPage(String userId, int page, int limit) {
        QueryWrapper<Comment> commentQueryWrapper = new QueryWrapper<>();
        commentQueryWrapper.lambda().ne(Comment::getAuthorId, userId);
        commentQueryWrapper.orderByDesc("coid");
        return commentMapper.selectPage(new Page<>(page, limit), commentQueryWrapper);
    }

    @Override
    public void update(Comment comments) {
        if (null != comments && null != comments.getCoid()) {
            commentMapper.updateById(comments);
        }
    }

    @Override
    public void delete(String coid, String cid) {
        if (null == coid) {
            throw new TipException("主键为空");
        }
        commentMapper.deleteById(coid);
        Content contents = contentService.getContents(cid + "");
        if (null != contents && contents.getCommentsNum() > 0) {
            Content temp = new Content();
            temp.setCid(cid);
            temp.setCommentsNum(contents.getCommentsNum() - 1);
            contentService.updateContentByCid(temp);
        }
    }

    @Override
    public Comment getCommentById(String coid) {
        if (null != coid) {
            return commentMapper.selectById(coid);
        }
        return null;
    }

    /**
     * copy原有的分页信息，除数据
     *
     * @param ordinal
     * @param <T>
     * @return
     */
    private <T> Page<T> copyPage(Page ordinal) {
        Page<T> returnBo = new Page<T>();
//        returnBo.setPageSize(ordinal.getPageSize());
//        returnBo.setPageNum(ordinal.getPageNum());
//        returnBo.setEndRow(ordinal.getEndRow());
//        returnBo.setTotal(ordinal.getTotal());
//        returnBo.setHasNextPage(ordinal.isHasNextPage());
//        returnBo.setHasPreviousPage(ordinal.isHasPreviousPage());
//        returnBo.setIsFirstPage(ordinal.isIsFirstPage());
//        returnBo.setIsLastPage(ordinal.isIsLastPage());
//        returnBo.setNavigateFirstPage(ordinal.getNavigateFirstPage());
//        returnBo.setNavigateLastPage(ordinal.getNavigateLastPage());
//        returnBo.setNavigatepageNums(ordinal.getNavigatepageNums());
//        returnBo.setSize(ordinal.getSize());
//        returnBo.setPrePage(ordinal.getPrePage());
//        returnBo.setNextPage(ordinal.getNextPage());
        return ordinal;
    }
}
