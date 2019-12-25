package com.my.blog.website.service.impl;

import com.my.blog.website.module.admin.entity.Option;
import com.my.blog.website.module.admin.mapper.OptionMapper;
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
    private OptionMapper optionMapper;

    @Override
    public void insertOption(String name, String value) {
        log.debug("Enter insertOption method:name={},value={}", name, value);
        Option option = new Option();
        option.setName(name);
        option.setValue(value);
        if (optionMapper.selectList(null).size() == 0) {
            optionMapper.insert(option);
        } else {
            optionMapper.updateById(option);
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
    public List<Option> getOptions() {
        return optionMapper.selectList(null);
    }
}
