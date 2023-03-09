package com.example.ruiji_takeout.backstage.Interceptor;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSON;
import com.example.ruiji_takeout.Util.CookieUtil;
import com.example.ruiji_takeout.backstage.pojo.Employee;
import com.example.ruiji_takeout.common.R;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.Interceptor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author Jmd
 * @create 2023-03-2023/3/2-22:44
 * @Description：
 */
@Slf4j
@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("拦截到请求 {}",request.getRequestURI());
        HttpSession session = request.getSession();
        Employee employee = (Employee) session.getAttribute("employee");

        if (employee == null){

            /*response.sendRedirect("/backend/page/login/login.html");
            * 一开始思路错误了，使用了response的重定向。
            * 但是我们设置的对于静态资源是放行的，所以访问静态资源的请求是不会被拦截的
            * 只有静态页面中的ajax请求的地址会被拦截
            * 那么这个重定向起作用的也就是ajax的动态请求地址，而对于浏览器页面的地址栏（静态资源请求）无效
            * 所以页面不会跳转，response的重定向会导致动态请求无法获取数据，但不会导致浏览器页面跳转
            * */


            /*
            *   如果这个employee等于空，那么说明用户还没有进行登录，此时我们就给前端的页面返回一个R.error("NOTLOGIN")，
            * 当前端检测到code=0且msg="NOTLOGIN"时，就会进行一个页面的跳转，跳转到登录页面。
            * */
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Cache-Control", "no-cache");
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            response.getWriter().println(JSON.toJSONString(R.error("NOTLOGIN")));
            response.getWriter().flush();
            // 这里我们不放行返回false，那么DispatchServlet就不会把这个方法跳转到控制器方法，但这里我们让response返回了json信息，前端检测到信息时会跳转

            log.info("用户未登录，跳转到登录界面");
            return false;
        }

        log.info("用户已登录");
        return true;
    }
}
