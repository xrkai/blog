package com.my.blog.website.module.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.my.blog.website.exception.TipException;
import com.my.blog.website.modal.Bo.RestResponseBo;
import com.my.blog.website.module.admin.entity.Comment;
import com.my.blog.website.module.admin.entity.User;
import com.my.blog.website.module.admin.service.ICommentService;
import com.my.blog.website.module.blog.controller.BaseController;
import com.my.blog.website.utils.TaleUtils;
import com.vdurmont.emoji.EmojiParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by 13 on 2017/2/26.
 */
@Controller
@RequestMapping("admin/comments")
@Slf4j
public class CommentController extends BaseController {


    @Resource
    private ICommentService commentsService;

    /**
     * 评论列表
     *
     * @param page
     * @param limit
     * @param request
     * @return
     */
    @GetMapping(value = "")
    public String index(@RequestParam(value = "page", defaultValue = "1") int page,
                        @RequestParam(value = "limit", defaultValue = "15") int limit, HttpServletRequest request) {
        User users = this.user(request);
        IPage<Comment> commentsPaginator = commentsService.getCommentsWithPage(users.getUid(), page, limit);
        request.setAttribute("comments", commentsPaginator);
        return "admin/comment_list";
    }

    /**
     * 删除一条评论
     *
     * @param coid
     * @return
     */
    @PostMapping(value = "delete")
    @ResponseBody
    @Transactional(rollbackFor = TipException.class)
    public RestResponseBo delete(@RequestParam String coid) {
        try {
            Comment comments = commentsService.getCommentById(coid);
            if (null == comments) {
                return RestResponseBo.fail("不存在该评论");
            }
            commentsService.delete(coid, comments.getCid());
        } catch (Exception e) {
            String msg = "评论删除失败";
            if (e instanceof TipException) {
                msg = e.getMessage();
            } else {
                log.error(msg, e);
            }
            return RestResponseBo.fail(msg);
        }
        return RestResponseBo.ok();
    }

    @PostMapping(value = "status")
    @ResponseBody
    @Transactional(rollbackFor = TipException.class)
    public RestResponseBo delete(@RequestParam String coid, @RequestParam String status) {
        try {
            Comment comments = new Comment();
            comments.setCoid(coid);
            comments.setStatus(status);
            commentsService.update(comments);
        } catch (Exception e) {
            String msg = "操作失败";
            if (e instanceof TipException) {
                msg = e.getMessage();
            } else {
                log.error(msg, e);
            }
            return RestResponseBo.fail(msg);
        }
        return RestResponseBo.ok();
    }

    /**
     * 回复评论
     *
     * @param coid
     * @param content
     * @param request
     * @return
     */
    @PostMapping(value = "")
    @ResponseBody
    @Transactional(rollbackFor = TipException.class)
    public RestResponseBo reply(@RequestParam String coid, @RequestParam String content, HttpServletRequest request) {
        if (null == coid || StringUtils.isEmpty(content)) {
            return RestResponseBo.fail("请输入完整后评论");
        }

        if (content.length() > 2000) {
            return RestResponseBo.fail("请输入2000个字符以内的回复");
        }
        Comment c = commentsService.getCommentById(coid);
        if (null == c) {
            return RestResponseBo.fail("不存在该评论");
        }
        User users = this.user(request);
        content = TaleUtils.cleanXSS(content);
        content = EmojiParser.parseToAliases(content);

        Comment comments = new Comment();
        comments.setAuthor(users.getUsername());
        comments.setAuthorId(users.getUid());
        comments.setCid(c.getCid());
        comments.setIp(request.getRemoteAddr());
        comments.setUrl(users.getHomeUrl());
        comments.setContent(content);
        comments.setMail(users.getEmail());
        comments.setParent(coid);
        try {
            commentsService.insertComment(comments);
            return RestResponseBo.ok();
        } catch (Exception e) {
            String msg = "回复失败";
            if (e instanceof TipException) {
                msg = e.getMessage();
            } else {
                log.error(msg, e);
            }
            return RestResponseBo.fail(msg);
        }
    }

}
