package com.my.blog.website.module.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.my.blog.website.constant.WebConst;
import com.my.blog.website.dto.Types;
import com.my.blog.website.exception.TipException;
import com.my.blog.website.module.admin.entity.Content;
import com.my.blog.website.module.admin.mapper.ContentMapper;
import com.my.blog.website.module.admin.mapper.MetaMapper;
import com.my.blog.website.module.admin.service.IContentService;
import com.my.blog.website.module.admin.service.IMetaService;
import com.my.blog.website.module.admin.service.IRelationshipService;
import com.my.blog.website.utils.DateKit;
import com.my.blog.website.utils.TaleUtils;
import com.vdurmont.emoji.EmojiParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Administrator on 2017/3/13 013.
 */
@Service
@Slf4j
public class ContentServiceImpl implements IContentService {

    @Resource
    private ContentMapper contentMapper;

    @Resource
    private MetaMapper metaMapper;

    @Resource
    private IRelationshipService relationshipService;

    @Resource
    private IMetaService metasService;

    @Override
    public void publish(Content contents) {
        if (null == contents) {
            throw new TipException("文章对象为空");
        }
        if (StringUtils.isEmpty(contents.getTitle())) {
            throw new TipException("文章标题不能为空");
        }
        if (StringUtils.isEmpty(contents.getContent())) {
            throw new TipException("文章内容不能为空");
        }
        int titleLength = contents.getTitle().length();
        if (titleLength > WebConst.MAX_TITLE_COUNT) {
            throw new TipException("文章标题过长");
        }
        int contentLength = contents.getContent().length();
        if (contentLength > WebConst.MAX_TEXT_COUNT) {
            throw new TipException("文章内容过长");
        }
        if (null == contents.getAuthorId()) {
            throw new TipException("请登录后发布文章");
        }
        if (StringUtils.isNotEmpty(contents.getSlug())) {
            if (contents.getSlug().length() < 5) {
                throw new TipException("路径太短了");
            }
            if (!TaleUtils.isPath(contents.getSlug())) {
                throw new TipException("您输入的路径不合法");
            }
            QueryWrapper<Content> contentQueryWrapper = new QueryWrapper<>();
            contentQueryWrapper.lambda().eq(Content::getType, contents.getType()).eq(Content::getStatus, contents.getSlug());
            long count = contentMapper.selectCount(contentQueryWrapper);
            if (count > 0) {
                throw new TipException("该路径已经存在，请重新输入");
            }
        } else {
            contents.setSlug(null);
        }

        contents.setContent(EmojiParser.parseToAliases(contents.getContent()));

        int time = DateKit.getCurrentUnixTime();
        contents.setCreated(time);
        contents.setModified(time);
        contents.setHits(0);
        contents.setCommentsNum(0);
        String tags = contents.getTags();
        String categories = contents.getCategories();
        contentMapper.insert(contents);
        String cid = contents.getCid();

        metasService.saveMetas(cid, tags, Types.TAG.getType());
        metasService.saveMetas(cid, categories, Types.CATEGORY.getType());
    }

    @Override
    public IPage<Content> getContents(Integer pageSize, Integer limit) {
        log.debug("Enter getContents method");
        QueryWrapper<Content> contentQueryWrapper = new QueryWrapper<>();
        contentQueryWrapper.lambda().eq(Content::getType, Types.ARTICLE.getType()).eq(Content::getStatus, Types.PUBLISH.getType());
        contentQueryWrapper.orderByDesc("created");
        Page<Content> page = new Page<>(pageSize, limit);
        log.debug("Exit getContents method");
        return contentMapper.selectPage(page, contentQueryWrapper);
    }

    @Override
    public Content getContents(String id) {
        if (StringUtils.isEmpty(id)) {
            return null;
        }
        Content content = contentMapper.selectById(id);
        if (content != null) {
            log.info("浏览次数: {}", content.getHits());
            content.setHits(content.getHits() + 1);
            contentMapper.updateById(content);
        }
        return content;
    }

    @Override
    public void updateContentByCid(Content contentVo) {
        if (null != contentVo && null != contentVo.getCid()) {
            contentMapper.updateById(contentVo);
        }
    }

    @Override
    public Page<Content> getArticles(String mid, int page, int limit) {
        int total = metaMapper.countWithSql(mid);
        List<Content> list = contentMapper.findByCatalog(mid);
        Page<Content> paginator = new Page<>();
        paginator.setTotal(total);
        paginator.setRecords(list);
        return paginator;
    }

    @Override
    public IPage<Content> getArticles(String keyword, Integer page, Integer limit) {
        QueryWrapper<Content> contentQueryWrapper = new QueryWrapper<>();
        contentQueryWrapper.lambda()
                .eq(Content::getType, Types.ARTICLE.getType())
                .eq(Content::getStatus, Types.PUBLISH.getType())
                .like(Content::getTitle, "%" + keyword + "%");
        contentQueryWrapper.orderByDesc("created");
        return contentMapper.selectPage(new Page<Content>(page, limit), contentQueryWrapper);
    }

    @Override
    public IPage<Content> getArticlesWithPage(Integer page, Integer limit, String type) {
        QueryWrapper<Content> contentQueryWrapper = new QueryWrapper<>();
        contentQueryWrapper.lambda().eq(Content::getType, type);
        contentQueryWrapper.orderByDesc("created");
        return contentMapper.selectPage(new Page<>(page, limit), contentQueryWrapper);
    }

    @Override
    public void deleteByCid(String cid) {
        Content contents = this.getContents(cid + "");
        if (null != contents) {
            contentMapper.deleteById(cid);
            relationshipService.deleteById(cid, null);
        }
    }

    @Override
    public void updateCategory(String ordinal, String newCategory) {
        Content contentVo = new Content();
        contentVo.setCategories(newCategory);
        QueryWrapper<Content> contentQueryWrapper = new QueryWrapper<>();
        contentQueryWrapper.lambda().eq(Content::getCategories, ordinal);
        contentMapper.update(contentVo, contentQueryWrapper);
    }

    @Override
    public void updateArticle(Content contents) {
        if (null == contents || null == contents.getCid()) {
            throw new TipException("文章对象不能为空");
        }
        if (StringUtils.isEmpty(contents.getTitle())) {
            throw new TipException("文章标题不能为空");
        }
        if (StringUtils.isEmpty(contents.getContent())) {
            throw new TipException("文章内容不能为空");
        }
        if (contents.getTitle().length() > 200) {
            throw new TipException("文章标题过长");
        }
        if (contents.getContent().length() > 65000) {
            throw new TipException("文章内容过长");
        }
        if (null == contents.getAuthorId()) {
            throw new TipException("请登录后发布文章");
        }
        if (StringUtils.isEmpty(contents.getSlug())) {
            contents.setSlug(null);
        }
        int time = DateKit.getCurrentUnixTime();
        contents.setModified(time);
        String cid = contents.getCid();
        contents.setContent(EmojiParser.parseToAliases(contents.getContent()));

        contentMapper.updateById(contents);
        relationshipService.deleteById(cid, null);
        metasService.saveMetas(cid, contents.getTags(), Types.TAG.getType());
        metasService.saveMetas(cid, contents.getCategories(), Types.CATEGORY.getType());
    }
}
