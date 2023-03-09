package com.example.ruiji_takeout.backstage.Interceptor;

import com.alibaba.fastjson.JSON;
import com.example.ruiji_takeout.app.pojo.User;
import com.example.ruiji_takeout.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Jmd
 * @create 2023-03-2023/3/8-15:43
 * @Description：
 */
@Slf4j
@Component
public class UserLoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        User user =(User) request.getSession().getAttribute("user");
        if (user == null){
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Cache-Control", "no-cache");
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            response.getWriter().println(JSON.toJSONString(R.error("NOTLOGIN")));
            response.getWriter().flush();

            log.info("用户未登录，跳转到登录界面");
            return false; // 不放行返回false
        }


        log.info("客户端用户已登录");
        return true;
    }
}
