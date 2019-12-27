package com.my.blog.website.module.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.my.blog.website.common.utils.DateKit;
import com.my.blog.website.module.admin.entity.Attach;
import com.my.blog.website.module.admin.mapper.AttachMapper;
import com.my.blog.website.module.admin.service.IAttachService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by wangq on 2017/3/20.
 */
@Service
@Slf4j
public class AttachServiceImpl implements IAttachService {

    @Resource
    private AttachMapper attachMapper;

    @Override
    public IPage<Attach> getAttachs(Integer page, Integer limit) {
        QueryWrapper<Attach> attachQueryWrapper = new QueryWrapper<>();
        attachQueryWrapper.orderByDesc("id");
        return attachMapper.selectPage(new Page<>(page, limit), attachQueryWrapper);
    }

    @Override
    public Attach selectById(Integer id) {
        if (null != id) {
            return attachMapper.selectById(id);
        }
        return null;
    }

    @Override
    public void save(String fname, String fkey, String ftype, String author) {
        Attach attach = new Attach();
        attach.setFname(fname);
        attach.setAuthorId(author);
        attach.setFkey(fkey);
        attach.setFtype(ftype);
        attach.setCreated(DateKit.getCurrentUnixTime());
        attachMapper.insert(attach);
    }

    @Override
    public void deleteById(Integer id) {
        if (null != id) {
            attachMapper.deleteById(id);
        }
    }
}
