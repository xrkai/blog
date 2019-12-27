package com.my.blog.website.common.interceptor;

import com.my.blog.website.common.constant.Types;
import com.my.blog.website.common.constant.WebConst;
import com.my.blog.website.common.page.BasePageUtils;
import com.my.blog.website.common.utils.*;
import com.my.blog.website.module.admin.entity.User;
import com.my.blog.website.module.admin.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 自定义拦截器
 * Created by BlueT on 2017/3/9.
 */
@Component
@Slf4j
public class BaseInterceptor implements HandlerInterceptor {
    private static final String USER_AGENT = "user-agent";

    @Resource
    private IUserService userService;

    private MapCache cache = MapCache.single();

    @Resource
    private Commons commons;

    @Resource
    private AdminCommons adminCommons;

    @Resource
    private BasePageUtils basePageUtils;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        String uri = request.getRequestURI();

        log.info("UserAgent: {}", request.getHeader(USER_AGENT));
        log.info("用户访问地址: {}, 来路地址: {}", uri, IPKit.getIpAddrByRequest(request));

        //请求拦截处理
        User user = TaleUtils.getLoginUser(request);
        if (null == user) {
            Integer uid = TaleUtils.getCookieUid(request);
            if (null != uid) {
                //这里还是有安全隐患,cookie是可以伪造的
                user = userService.queryUserById(uid);
                request.getSession().setAttribute(WebConst.LOGIN_SESSION_KEY, user);
            }
        }
        if (uri.startsWith("/admin") && !uri.startsWith("/admin/login") && null == user) {
            response.sendRedirect(request.getContextPath() + "/admin/login");
            return false;
        }
        //设置get请求的token
        if ("GET".equals(request.getMethod())) {
            String csrfToken = AbstractUUID.UU64();
            // 默认存储30分钟
            cache.hset(Types.CSRF_TOKEN.getType(), csrfToken, uri, 30 * 60);
            request.setAttribute("csrfToken", csrfToken);
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) {
        //一些工具类和公共方法
        httpServletRequest.setAttribute("commons", commons);
        httpServletRequest.setAttribute("adminCommons", adminCommons);
        httpServletRequest.setAttribute("basePageUtils", basePageUtils);
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {

    }
}
