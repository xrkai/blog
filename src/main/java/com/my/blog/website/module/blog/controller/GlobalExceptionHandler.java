package com.my.blog.website.module.blog.controller;

import com.my.blog.website.common.exception.TipException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Created by BlueT on 2017/3/4.
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(value = TipException.class)
    public String tipException(Exception e) {
        log.error("find exception:e={}", e.getMessage());
        e.printStackTrace();
        return "comm/error_500";
    }


    @ExceptionHandler(value = Exception.class)
    public String exception(Exception e) {
        log.error("find exception:e={}", e.getMessage());
        e.printStackTrace();
        return "comm/error_404";
    }
}
