package com.my.blog.website.service.impl;

import com.my.blog.website.modal.Vo.OptionVo;
import com.my.blog.website.modal.Vo.OptionVoExample;
import com.my.blog.website.module.admin.mapper.OptionVoMapper;
import com.my.blog.website.service.IOptionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * options表的service
 * Created by BlueT on 2017/3/7.
 */
@Service
@Slf4j
public class OptionServiceImpl implements IOptionService {

    @Resource
    private OptionVoMapper optionDao;

    @Override
    public void insertOption(OptionVo optionVo) {
        log.debug("Enter insertOption method:optionVo={}", optionVo);
        optionDao.insertSelective(optionVo);
        log.debug("Exit insertOption method.");
    }

    @Override
    public void insertOption(String name, String value) {
        log.debug("Enter insertOption method:name={},value={}", name, value);
        OptionVo optionVo = new OptionVo();
        optionVo.setName(name);
        optionVo.setValue(value);
        if (optionDao.selectByExample(new OptionVoExample()).size() == 0) {
            optionDao.insertSelective(optionVo);
        } else {
            optionDao.updateByPrimaryKeySelective(optionVo);
        }
        log.debug("Exit insertOption method.");
    }

    @Override
    public void saveOptions(Map<String, String> options) {
        if (null != options && !options.isEmpty()) {
            options.forEach(this::insertOption);
        }
    }

    @Override
    public List<OptionVo> getOptions() {
        return optionDao.selectByExample(new OptionVoExample());
    }
}
