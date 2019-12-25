package com.my.blog.website.module.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.my.blog.website.constant.WebConst;
import com.my.blog.website.dto.MetaDto;
import com.my.blog.website.dto.Types;
import com.my.blog.website.exception.TipException;
import com.my.blog.website.modal.Bo.ArchiveBo;
import com.my.blog.website.modal.Bo.BackResponseBo;
import com.my.blog.website.modal.Bo.StatisticsBo;
import com.my.blog.website.module.admin.controller.AttachController;
import com.my.blog.website.module.admin.entity.Comment;
import com.my.blog.website.module.admin.entity.Content;
import com.my.blog.website.module.admin.entity.Meta;
import com.my.blog.website.module.admin.mapper.AttachMapper;
import com.my.blog.website.module.admin.mapper.CommentMapper;
import com.my.blog.website.module.admin.mapper.ContentMapper;
import com.my.blog.website.module.admin.mapper.MetaMapper;
import com.my.blog.website.module.admin.service.ISiteService;
import com.my.blog.website.utils.DateKit;
import com.my.blog.website.utils.TaleUtils;
import com.my.blog.website.utils.ZipUtils;
import com.my.blog.website.utils.backup.Backup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;

/**
 * Created by BlueT on 2017/3/7.
 */
@Service
@Slf4j
public class SiteServiceImpl implements ISiteService {

    @Resource
    private CommentMapper commentMapper;

    @Resource
    private ContentMapper contentMapper;

    @Resource
    private AttachMapper attachMapper;

    @Resource
    private MetaMapper metaMapper;

    @Override
    public List<Comment> recentComments(int limit) {
        log.debug("Enter recentComments method:limit={}", limit);
        if (limit < 0 || limit > 10) {
            limit = 10;
        }
        QueryWrapper<Comment> commentQueryWrapper = new QueryWrapper<>();
        commentQueryWrapper.orderByDesc("created");
        log.debug("Exit recentComments method");
        IPage<Comment> list = commentMapper.selectPage(new Page<>(1, limit), commentQueryWrapper);
        return commentMapper.selectList(commentQueryWrapper);
    }

    @Override
    public List<Content> recentContents(int limit) {
        log.debug("Enter recentContents method");
        if (limit < 0 || limit > 10) {
            limit = 10;
        }
        QueryWrapper<Content> contentQueryWrapper = new QueryWrapper<>();
        contentQueryWrapper.orderByDesc("created");
        IPage<Content> list = contentMapper.selectPage(new Page<>(1, limit), contentQueryWrapper);
        log.debug("Exit recentContents method");
        return list.getRecords();
    }

    @Override
    public BackResponseBo backup(String bk_type, String bk_path, String fmt) throws Exception {
        BackResponseBo backResponse = new BackResponseBo();
        if (bk_type.equals("attach")) {
            if (StringUtils.isEmpty(bk_path)) {
                throw new TipException("请输入备份文件存储路径");
            }
            if (!(new File(bk_path)).isDirectory()) {
                throw new TipException("请输入一个存在的目录");
            }
            String bkAttachDir = AttachController.CLASSPATH + "upload";
            String bkThemesDir = AttachController.CLASSPATH + "templates/themes";

            String fname = DateKit.dateFormat(new Date(), fmt) + "_" + TaleUtils.getRandomNumber(5) + ".zip";

            String attachPath = bk_path + "/" + "attachs_" + fname;
            String themesPath = bk_path + "/" + "themes_" + fname;

            ZipUtils.zipFolder(bkAttachDir, attachPath);
            ZipUtils.zipFolder(bkThemesDir, themesPath);

            backResponse.setAttachPath(attachPath);
            backResponse.setThemePath(themesPath);
        }
        if (bk_type.equals("db")) {

            String bkAttachDir = AttachController.CLASSPATH + "upload/";
            if (!(new File(bkAttachDir)).isDirectory()) {
                File file = new File(bkAttachDir);
                if (!file.exists()) {
                    file.mkdirs();
                }
            }
            String sqlFileName = "tale_" + DateKit.dateFormat(new Date(), fmt) + "_" + TaleUtils.getRandomNumber(5) + ".sql";
            String zipFile = sqlFileName.replace(".sql", ".zip");

            Backup backup = new Backup(TaleUtils.getNewDataSource().getConnection());
            String sqlContent = backup.execute();

            File sqlFile = new File(bkAttachDir + sqlFileName);
            write(sqlContent, sqlFile, Charset.forName("UTF-8"));

            String zip = bkAttachDir + zipFile;
            ZipUtils.zipFile(sqlFile.getPath(), zip);

            if (!sqlFile.exists()) {
                throw new TipException("数据库备份失败");
            }
            sqlFile.delete();

            backResponse.setSqlPath(zipFile);

            // 10秒后删除备份文件
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    new File(zip).delete();
                }
            }, 10 * 1000);
        }
        return backResponse;
    }

    @Override
    public Comment getComment(Integer coid) {
        if (null != coid) {
            return commentMapper.selectById(coid);
        }
        return null;
    }

    @Override
    public StatisticsBo getStatistics() {
        log.debug("Enter getStatistics method");
        StatisticsBo statistics = new StatisticsBo();
        QueryWrapper<Content> contentQueryWrapper = new QueryWrapper<>();
        contentQueryWrapper.lambda()
                .eq(Content::getType, Types.ARTICLE.getType())
                .eq(Content::getStatus, Types.PUBLISH.getType());

        Integer articles = contentMapper.selectCount(contentQueryWrapper);

        Integer comments = commentMapper.selectCount(null);

        Integer attachs = attachMapper.selectCount(null);

        QueryWrapper<Meta> metaQueryWrapper = new QueryWrapper<>();
        metaQueryWrapper.lambda().eq(Meta::getType, Types.LINK.getType());
        Integer links = metaMapper.selectCount(metaQueryWrapper);

        statistics.setArticles(articles);
        statistics.setComments(comments);
        statistics.setAttachs(attachs);
        statistics.setLinks(links);
        log.debug("Exit getStatistics method");
        return statistics;
    }

    @Override
    public List<ArchiveBo> getArchives() {
        log.debug("Enter getArchives method");
        List<ArchiveBo> archives = contentMapper.findReturnArchiveBo();
        if (null != archives) {
            archives.forEach(archive -> {
                String date = archive.getDate();
                Date sd = DateKit.dateFormat(date, "yyyy年MM月");
                int start = DateKit.getUnixTimeByDate(sd);
                int end = DateKit.getUnixTimeByDate(DateKit.dateAdd(DateKit.INTERVAL_MONTH, sd, 1)) - 1;
                QueryWrapper<Content> contentQueryWrapper = new QueryWrapper<>();
                contentQueryWrapper.lambda().eq(Content::getType, Types.ARTICLE.getType())
                        .gt(Content::getCreated, start)
                        .lt(Content::getCreated, end);
                contentQueryWrapper.orderByDesc("created");
                List<Content> contentss = contentMapper.selectList(contentQueryWrapper);
                archive.setArticles(contentss);
            });
        }
        log.debug("Exit getArchives method");
        return archives;
    }

    @Override
    public List<MetaDto> metas(String type, String orderBy, int limit) {
        log.debug("Enter metas method:type={},order={},limit={}", type, orderBy, limit);
        List<MetaDto> retList = null;
        if (StringUtils.isNotEmpty(type)) {
            if (StringUtils.isEmpty(orderBy)) {
                orderBy = "count desc, a.mid desc";
            }
            if (limit < 1 || limit > WebConst.MAX_POSTS) {
                limit = 10;
            }
            Map<String, Object> paraMap = new HashMap<>();
            paraMap.put("type", type);
            paraMap.put("order", orderBy);
            paraMap.put("limit", limit);
            retList = metaMapper.selectFromSql(paraMap);
        }
        log.debug("Exit metas method");
        return retList;
    }


    private void write(String data, File file, Charset charset) {
        FileOutputStream os = null;
        try {
            os = new FileOutputStream(file);
            os.write(data.getBytes(charset));
        } catch (IOException var8) {
            throw new IllegalStateException(var8);
        } finally {
            if (null != os) {
                try {
                    os.close();
                } catch (IOException var2) {
                    var2.printStackTrace();
                }
            }
        }

    }

}
