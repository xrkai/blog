package com.my.blog.website.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.my.blog.website.dao.CommentVoMapper;
import com.my.blog.website.exception.TipException;
import com.my.blog.website.modal.Bo.CommentBo;
import com.my.blog.website.modal.Vo.CommentVo;
import com.my.blog.website.modal.Vo.CommentVoExample;
import com.my.blog.website.modal.Vo.ContentVo;
import com.my.blog.website.service.ICommentService;
import com.my.blog.website.service.IContentService;
import com.my.blog.website.utils.DateKit;
import com.my.blog.website.utils.TaleUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by BlueT on 2017/3/16.
 */
@Service
public class CommentServiceImpl implements ICommentService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommentServiceImpl.class);

    @Resource
    private CommentVoMapper commentDao;

    @Resource
    private IContentService contentService;

    @Override
    public void insertComment(CommentVo comments) {
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
        ContentVo contents = contentService.getContents(String.valueOf(comments.getCid()));
        if (null == contents) {
            throw new TipException("不存在的文章");
        }
        comments.setOwnerId(contents.getAuthorId());
        comments.setCreated(DateKit.getCurrentUnixTime());
        commentDao.insertSelective(comments);

        ContentVo temp = new ContentVo();
        temp.setCid(contents.getCid());
        temp.setCommentsNum(contents.getCommentsNum() + 1);
        contentService.updateContentByCid(temp);
    }

    @Override
    public Page<CommentBo> getComments(Integer cid, int page, int limit) {

        if (null != cid) {
            CommentVoExample commentVoExample = new CommentVoExample();
            commentVoExample.createCriteria().andCidEqualTo(cid).andParentEqualTo(0);
            commentVoExample.setOrderByClause("coid desc");
            List<CommentVo> parents = commentDao.selectByExampleWithBLOBs(commentVoExample);
            Page<CommentVo> commentPaginator = new Page<>();
            commentPaginator.setRecords(parents);
            Page<CommentBo> returnBo = copyPage(commentPaginator);
            if (parents.size() != 0) {
                List<CommentBo> comments = new ArrayList<>(parents.size());
                parents.forEach(parent -> {
                    CommentBo comment = new CommentBo(parent);
                    comments.add(comment);
                });
                returnBo.setRecords(comments);
            }
            return returnBo;
        }
        return null;
    }

    @Override
    public Page<CommentVo> getCommentsWithPage(CommentVoExample commentVoExample, int page, int limit) {
        List<CommentVo> commentVos = commentDao.selectByExampleWithBLOBs(commentVoExample);
        Page<CommentVo> Page = new Page<>();
        return Page.setRecords(commentVos);
    }

    @Override
    public void update(CommentVo comments) {
        if (null != comments && null != comments.getCoid()) {
            commentDao.updateByPrimaryKeyWithBLOBs(comments);
        }
    }

    @Override
    public void delete(Integer coid, Integer cid) {
        if (null == coid) {
            throw new TipException("主键为空");
        }
        commentDao.deleteByPrimaryKey(coid);
        ContentVo contents = contentService.getContents(cid + "");
        if (null != contents && contents.getCommentsNum() > 0) {
            ContentVo temp = new ContentVo();
            temp.setCid(cid);
            temp.setCommentsNum(contents.getCommentsNum() - 1);
            contentService.updateContentByCid(temp);
        }
    }

    @Override
    public CommentVo getCommentById(Integer coid) {
        if (null != coid) {
            return commentDao.selectByPrimaryKey(coid);
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
