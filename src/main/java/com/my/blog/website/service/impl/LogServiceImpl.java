package com.my.blog.website.service.impl;

import com.my.blog.website.constant.WebConst;
import com.my.blog.website.modal.Vo.LogVo;
import com.my.blog.website.modal.Vo.LogVoExample;
import com.my.blog.website.module.admin.mapper.LogVoMapper;
import com.my.blog.website.service.ILogService;
import com.my.blog.website.utils.DateKit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by BlueT on 2017/3/4.
 */
@Service
@Slf4j
public class LogServiceImpl implements ILogService {

    @Resource
    private LogVoMapper logDao;

    @Override
    public void insertLog(LogVo logVo) {
        logDao.insert(logVo);
    }

    @Override
    public void insertLog(String action, String data, String ip, Integer authorId) {
        LogVo logs = new LogVo();
        logs.setAction(action);
        logs.setData(data);
        logs.setIp(ip);
        logs.setAuthorId(authorId);
        logs.setCreated(DateKit.getCurrentUnixTime());
        logDao.insert(logs);
    }

    @Override
    public List<LogVo> getLogs(int page, int limit) {
        log.debug("Enter getLogs method:page={},linit={}", page, limit);
        if (page <= 0) {
            page = 1;
        }
        if (limit < 1 || limit > WebConst.MAX_POSTS) {
            limit = 10;
        }
        LogVoExample logVoExample = new LogVoExample();
        logVoExample.setOrderByClause("id desc");
        List<LogVo> logVos = logDao.selectByExample(logVoExample);
        log.debug("Exit getLogs method");
        return logVos;
    }
}
