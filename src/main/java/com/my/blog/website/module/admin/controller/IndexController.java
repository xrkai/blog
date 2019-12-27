package com.my.blog.website.module.admin.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.my.blog.website.common.constant.LogActions;
import com.my.blog.website.common.constant.WebConst;
import com.my.blog.website.common.exception.TipException;
import com.my.blog.website.common.result.RestResponse;
import com.my.blog.website.common.utils.TaleUtils;
import com.my.blog.website.module.admin.entity.Comment;
import com.my.blog.website.module.admin.entity.Content;
import com.my.blog.website.module.admin.entity.Log;
import com.my.blog.website.module.admin.entity.User;
import com.my.blog.website.module.admin.service.ILogService;
import com.my.blog.website.module.admin.service.ISiteService;
import com.my.blog.website.module.admin.service.IUserService;
import com.my.blog.website.module.admin.vo.StatisticsVO;
import com.my.blog.website.module.blog.controller.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * 后台管理首页
 * Created by Administrator on 2017/3/9 009.
 */
@Controller("adminIndexController")
@RequestMapping("/admin")
@Transactional(rollbackFor = TipException.class)
@Slf4j
public class IndexController extends BaseController {

    @Resource
    private ISiteService siteService;

    @Resource
    private ILogService logService;

    @Resource
    private IUserService userService;

    /**
     * 页面跳转
     *
     * @return
     */
    @GetMapping(value = {"", "/index"})
    public String index(HttpServletRequest request) {
        log.info("Enter admin index method");
        List<Comment> comments = siteService.recentComments(5);
        List<Content> contents = siteService.recentContents(5);
        StatisticsVO statistics = siteService.getStatistics();
        // 取最新的20条日志
        List<Log> logs = logService.getLogs(1, 10);

        request.setAttribute("comments", comments);
        request.setAttribute("articles", contents);
        request.setAttribute("statistics", statistics);
        request.setAttribute("logs", logs);
        log.info("Exit admin index method");
        return "admin/index";
    }

    /**
     * 个人设置页面
     */
    @GetMapping(value = "profile")
    public String profile() {
        return "admin/profile";
    }

    /**
     * admin 退出登录
     *
     * @return
     */
    @GetMapping(value = "logout")
    public String logout() {
        System.out.println("index-----------logout");
        return "admin/login";
    }


    /**
     * 保存个人信息
     */
    @PostMapping(value = "/profile")
    @ResponseBody
    @Transactional(rollbackFor = TipException.class)
    public RestResponse saveProfile(@RequestParam String screenName, @RequestParam String email, HttpServletRequest request, HttpSession session) {

        User users = this.user(request);
        if (StringUtils.isNotEmpty(screenName) && StringUtils.isNotEmpty(email)) {
            User temp = new User();
            temp.setUid(users.getUid());
            temp.setScreenName(screenName);
            temp.setEmail(email);
            userService.updateByUid(temp);
            logService.insertLog(LogActions.UP_INFO.getAction(), JSONObject.toJSONString(temp), request.getRemoteAddr(), this.getUid(request));

            //更新session中的数据
            User original = (User) session.getAttribute(WebConst.LOGIN_SESSION_KEY);
            original.setScreenName(screenName);
            original.setEmail(email);
            session.setAttribute(WebConst.LOGIN_SESSION_KEY, original);
        }
        return RestResponse.ok();
    }

    /**
     * 修改密码
     */
    @PostMapping(value = "/password")
    @ResponseBody
    @Transactional(rollbackFor = TipException.class)
    public RestResponse upPwd(@RequestParam String oldPassword, @RequestParam String password, HttpServletRequest request, HttpSession session) {
        User users = this.user(request);
        if (StringUtils.isEmpty(oldPassword) || StringUtils.isEmpty(password)) {
            return RestResponse.fail("请确认信息输入完整");
        }

        if (!users.getPassword().equals(TaleUtils.MD5encode(users.getUsername() + oldPassword))) {
            return RestResponse.fail("旧密码错误");
        }
        if (password.length() < 6 || password.length() > 14) {
            return RestResponse.fail("请输入6-14位密码");
        }

        try {
            User temp = new User();
            temp.setUid(users.getUid());
            String pwd = TaleUtils.MD5encode(users.getUsername() + password);
            temp.setPassword(pwd);
            userService.updateByUid(temp);
            logService.insertLog(LogActions.UP_PWD.getAction(), null, request.getRemoteAddr(), this.getUid(request));

            //更新session中的数据
            User original = (User) session.getAttribute(WebConst.LOGIN_SESSION_KEY);
            original.setPassword(pwd);
            session.setAttribute(WebConst.LOGIN_SESSION_KEY, original);
            return RestResponse.ok();
        } catch (Exception e) {
            String msg = "密码修改失败";
            if (e instanceof TipException) {
                msg = e.getMessage();
            } else {
                log.error(msg, e);
            }
            return RestResponse.fail(msg);
        }
    }
}
