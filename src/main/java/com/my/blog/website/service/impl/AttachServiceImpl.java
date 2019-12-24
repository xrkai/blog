package com.my.blog.website.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.my.blog.website.modal.Vo.AttachVo;
import com.my.blog.website.modal.Vo.AttachVoExample;
import com.my.blog.website.module.admin.mapper.AttachVoMapper;
import com.my.blog.website.service.IAttachService;
import com.my.blog.website.utils.DateKit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by wangq on 2017/3/20.
 */
@Service
@Slf4j
public class AttachServiceImpl implements IAttachService {

    @Resource
    private AttachVoMapper attachDao;

    @Override
    public Page<AttachVo> getAttachs(Integer page, Integer limit) {
        AttachVoExample attachVoExample = new AttachVoExample();
        attachVoExample.setOrderByClause("id desc");
        List<AttachVo> attachVos = attachDao.selectByExample(attachVoExample);
        Page<AttachVo> pageList = new Page<>();
        return pageList.setRecords(attachVos);
    }

    @Override
    public AttachVo selectById(Integer id) {
        if (null != id) {
            return attachDao.selectByPrimaryKey(id);
        }
        return null;
    }

    @Override
    public void save(String fname, String fkey, String ftype, Integer author) {
        AttachVo attach = new AttachVo();
        attach.setFname(fname);
        attach.setAuthorId(author);
        attach.setFkey(fkey);
        attach.setFtype(ftype);
        attach.setCreated(DateKit.getCurrentUnixTime());
        attachDao.insertSelective(attach);
    }

    @Override
    public void deleteById(Integer id) {
        if (null != id) {
            attachDao.deleteByPrimaryKey(id);
        }
    }
}
