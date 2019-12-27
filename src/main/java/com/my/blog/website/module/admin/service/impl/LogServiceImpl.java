package com.my.blog.website.module.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.my.blog.website.common.constant.WebConst;
import com.my.blog.website.common.utils.DateKit;
import com.my.blog.website.module.admin.entity.Log;
import com.my.blog.website.module.admin.mapper.LogMapper;
import com.my.blog.website.module.admin.service.ILogService;
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
    private LogMapper logMapper;

    @Override
    public void insertLog(Log logVo) {
        logMapper.insert(logVo);
    }

    @Override
    public void insertLog(String action, String data, String ip, String authorId) {
        Log logs = new Log();
        logs.setAction(action);
        logs.setData(data);
        logs.setIp(ip);
        logs.setAuthorId(authorId);
        logs.setCreated(DateKit.getCurrentUnixTime());
        logMapper.insert(logs);
    }

    @Override
    public List<Log> getLogs(int page, int limit) {
        log.debug("Enter getLogs method:page={},linit={}", page, limit);
        if (page <= 0) {
            page = 1;
        }
        if (limit < 1 || limit > WebConst.MAX_POSTS) {
            limit = 10;
        }
        QueryWrapper<Log> logQueryWrapper = new QueryWrapper<>();
        logQueryWrapper.orderByDesc("id");
        IPage<Log> logVos = logMapper.selectPage(new Page<>(page, limit), logQueryWrapper);
        log.debug("Exit getLogs method");
        return logVos.getRecords();
    }
}
