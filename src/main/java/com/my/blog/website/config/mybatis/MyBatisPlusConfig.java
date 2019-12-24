package com.my.blog.website.config.mybatis;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * @description: TODO
 * @className: MyBatisPageConfig
 * @date: 2019/10/31 13:18
 * @author: Xincan Jiang
 * @version: 1.0
 */
@Configuration
@MapperScan("com.my.blog.website.module.**.mapper")
public class MyBatisPlusConfig {

    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }

}
