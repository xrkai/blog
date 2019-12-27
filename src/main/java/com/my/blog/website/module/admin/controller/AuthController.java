package com.my.blog.website.module.admin.controller;

import com.my.blog.website.common.constant.LogActions;
import com.my.blog.website.common.constant.WebConst;
import com.my.blog.website.common.exception.TipException;
import com.my.blog.website.common.result.RestResponse;
import com.my.blog.website.common.utils.Commons;
import com.my.blog.website.common.utils.TaleUtils;
import com.my.blog.website.module.admin.entity.User;
import com.my.blog.website.module.admin.service.ILogService;
import com.my.blog.website.module.admin.service.IUserService;
import com.my.blog.website.module.blog.controller.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 用户后台登录/登出
 * Created by BlueT on 2017/3/11.
 */
@Controller
@RequestMapping("/admin")
@Transactional(rollbackFor = TipException.class)
@Slf4j
public class AuthController extends BaseController {

    @Resource
    private IUserService usersService;

    @Resource
    private ILogService logService;

    @GetMapping(value = "/login")
    public String login() {
        return "admin/login";
    }

    /**
     * 管理后台登录
     *
     * @param username
     * @param password
     * @param rememberMe
     * @param request
     * @param response
     * @return
     */
    @PostMapping(value = "login")
    @ResponseBody
    public RestResponse doLogin(@RequestParam String username,
                                @RequestParam String password,
                                @RequestParam(required = false) String rememberMe,
                                HttpServletRequest request,
                                HttpServletResponse response) {

        Integer errorCount = cache.get("login_error_count");
        try {
            User user = usersService.login(username, password);
            request.getSession().setAttribute(WebConst.LOGIN_SESSION_KEY, user);
            if (!StringUtils.isEmpty(rememberMe)) {
                TaleUtils.setCookie(response, user.getUid());
            }
            logService.insertLog(LogActions.LOGIN.getAction(), null, request.getRemoteAddr(), user.getUid());
        } catch (Exception e) {
            errorCount = null == errorCount ? 1 : errorCount + 1;
            if (errorCount > 3) {
                return RestResponse.fail("您输入密码已经错误超过3次，请10分钟后尝试");
            }
            cache.set("login_error_count", errorCount, 10 * 60);
            String msg = "登录失败";
            if (e instanceof TipException) {
                msg = e.getMessage();
            } else {
                log.error(msg, e);
            }
            return RestResponse.fail(msg);
        }
        return RestResponse.ok();
    }

    /**
     * 注销
     *
     * @param session
     * @param response
     */
    @RequestMapping("/logout")
    public void logout(HttpSession session, HttpServletResponse response, HttpServletRequest request) {
        session.removeAttribute(WebConst.LOGIN_SESSION_KEY);
        Cookie cookie = new Cookie(WebConst.USER_IN_COOKIE, "");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        try {
            //response.sendRedirect(Commons.site_url());
            response.sendRedirect(Commons.site_login());
        } catch (IOException e) {
            e.printStackTrace();
            log.error("注销失败", e);
        }
    }
}
