package com.example.ruiji_takeout.Util;

import com.example.ruiji_takeout.app.pojo.User;
import com.example.ruiji_takeout.backstage.pojo.Employee;
import com.example.ruiji_takeout.common.R;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.Request;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author Jmd
 * @create 2023-03-2023/3/3-20:56
 * @Description：
 */
@Component
@Slf4j
public class CookieUtil {

    // 通过RequestContextHolder得到Request对象

    public static HttpServletRequest getRequest(){
        log.info("通过RequestContextHolder在静态方法中获取到当前的request对象");
        HttpServletRequest request =((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        return request;
    }

    // 通过RequestContextHolder得到Session对象
    public static HttpSession getSession(){
        log.info("通过RequestContextHolder在静态方法中获取到当前的session对象");
        HttpSession session =((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getSession();
        return session;
    }

    // 通过RequestContextHolder得到Session对象中的employee属性(登录成功时存储的)
    public static Employee getEmployeeOfSession(){

        log.info("通过RequestContextHolder在静态方法中获取到当前的session对象中的employee对象");
        HttpSession session =((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getSession();
        Employee employee = (Employee) session.getAttribute("employee");
        return employee;
    }

    public static User getUserOfSession(){

        log.info("通过RequestContextHolder在静态方法中获取到当前的session对象中的user对象");
        HttpSession session =((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getSession();
        User user = (User) session.getAttribute("user");
        return user;
    }

}
