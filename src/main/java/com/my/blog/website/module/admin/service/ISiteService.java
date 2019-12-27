package com.my.blog.website.module.admin.service;

import com.my.blog.website.common.result.BackResponse;
import com.my.blog.website.module.admin.entity.Comment;
import com.my.blog.website.module.admin.entity.Content;
import com.my.blog.website.module.admin.entity.MetaDto;
import com.my.blog.website.module.admin.vo.ArchiveVO;
import com.my.blog.website.module.admin.vo.StatisticsVO;

import java.util.List;

/**
 * 站点服务
 * <p>
 * Created by 13 on 2017/2/23.
 */
public interface ISiteService {


    /**
     * 最新收到的评论
     *
     * @param limit
     * @return
     */
    List<Comment> recentComments(int limit);

    /**
     * 最新发表的文章
     *
     * @param limit
     * @return
     */
    List<Content> recentContents(int limit);

    /**
     * 查询一条评论
     *
     * @param coid
     * @return
     */
    Comment getComment(Integer coid);

    /**
     * 系统备份
     *
     * @param bk_type
     * @param bk_path
     * @param fmt
     * @return
     */
    BackResponse backup(String bk_type, String bk_path, String fmt) throws Exception;


    /**
     * 获取后台统计数据
     *
     * @return
     */
    StatisticsVO getStatistics();

    /**
     * 查询文章归档
     *
     * @return
     */
    List<ArchiveVO> getArchives();

    /**
     * 获取分类/标签列表
     *
     * @return
     */
    List<MetaDto> metas(String type, String orderBy, int limit);

}
