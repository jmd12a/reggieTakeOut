package com.example.ruiji_takeout.backstage.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.conditions.AbstractChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.ruiji_takeout.backstage.pojo.Employee;
import com.example.ruiji_takeout.backstage.service.EmployeeService;
import com.example.ruiji_takeout.common.R;
import com.sun.deploy.net.HttpResponse;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.*;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Jmd
 * @create 2023-03-2023/3/3-0:25
 * @Description：
 */

@Controller
@RequestMapping("/employee")
public class EmployeeController {

    @Resource
    private EmployeeService employeeService;

    @PostMapping("/login") // application/json的数据只能用RequestBody接收
    public @ResponseBody R<Employee> login(@RequestBody Employee employee, HttpSession session, HttpServletResponse response){

        String password = employee.getPassword();

        // 对密码进行md5加密
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        // 创建一个包装类查询employee
        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<>();

        // 给这个sql包装类定义一个等值查询，查询username = employee.getName()的数据封装到Employee中
        LambdaQueryWrapper<Employee> eq = wrapper.eq(Employee::getUsername, employee.getUsername());

        // 查询数据库中的数据并封装到这个类中
        Employee employee1 = employeeService.getOne(eq);

        String msg;
        if (employee1 == null){
            msg = "用户名不存在";
        }else if(!password.equals(employee1.getPassword())){
            msg = "密码错误";
        }else if ("0".equals(employee1.getStatus())){
            msg = "用户状态已被锁定";
        }else {
            // 登录成功

            // 在session域中存储employee对象
            session.setAttribute("employee",employee1);
            // 添加cookie
            Cookie cookie1 = new Cookie("username", employee.getUsername());
            cookie1.setMaxAge(60*60*24*10);
            cookie1.setPath("/");
            Cookie cookie2 = new Cookie("password", employee.getPassword());
            cookie2.setMaxAge(60*60*24*10);
            cookie2.setPath("/");
            response.addCookie(cookie1);
            response.addCookie(cookie2);

            return R.success(employee);
        }

        // 如果没有登录成功，就会走到这里，直接返回相应的msg即可
        return R.error(msg);
    }

    @PostMapping("/logout")
    public @ResponseBody R logout(HttpServletRequest request, HttpServletResponse response){

        // 注销session
        request.getSession().invalidate();

        // 销毁所有cookie
        for (Cookie cookie : request.getCookies()) {
            String cookieName = cookie.getName();
            Cookie cookie1 = new Cookie(cookieName, "");
            cookie1.setMaxAge(0);
            cookie1.setPath("/"); // cookie的路径和名字同时相同，才会被认为是同一个cookie
            response.addCookie(cookie1);
        }

        // 返回一个退出成功，即R.code=1
        return R.success(null);
    }

    @GetMapping("/page") // 直接把mybatis提供的page类，作为R的data属性返回给浏览器
    public @ResponseBody R<Page<Employee>> page(@RequestParam("page") Integer pageNum, Integer pageSize, String name){

        /*
        这样构造比较繁琐
        Page<Employee> page = new Page<>();
        page.setCurrent((pageNum-1)*pageSize);
        page.setSize(pageSize);*/
        Page<Employee> page = new Page<>(pageNum, pageSize);
        page = page.addOrder(OrderItem.desc("create_time"));


        if (!(name == null || name.isEmpty())){  // 如果字符串不等于空
            LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
            LambdaQueryWrapper<Employee> likeWrapper = queryWrapper.like(Employee::getName, name);
            // page放到这个查询方法中，page是引用类型，把地址传到了方法中，方法中对page进行的操作，是对page所在地址的类进行了操作。无需再接受返回值，传入的引用
            // 和返回值指向的地址相同
            employeeService.page(page, likeWrapper);
        }else {
            employeeService.page(page);
        }


        // 我们直接把这个page返回到前端，里面包括了很多分页信息以及数据记录，方便前端进行操作。
        return R.success(page);
    }

    @PostMapping // 类上的路径已将加了 /employee, 可以直接找到这个方法 无需加路径
    public @ResponseBody R addEmployee(@RequestBody Employee employee){

        // 创建一个包装类查询employee
        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<>();

        // 给这个sql包装类定义一个等值查询，查询username = employee.getName()的数据封装到Employee中
        LambdaQueryWrapper<Employee> eq = wrapper.eq(Employee::getUsername, employee.getUsername());

        // 查询数据库中的数据并封装到这个类中
        Employee employee1 = employeeService.getOne(eq);

        R r = new R();

        if (employee1 != null){
            r = R.error("用户名'"+employee.getUsername()+"'已存在");
            return r; // 直接结束方法
        }

        // 设置初始密码：123456，存储到数据库的是加密处理的
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));

        boolean b = employeeService.save(employee);

        if (b){
            r = R.success(null);
        }else r = R.error("创建失败");

        return r;
    }

    @PutMapping
    public @ResponseBody R updateEmployee(@RequestBody Employee employee,@SessionAttribute("employee") Employee user){

        if (user.getId().longValue() != 1L){
            employee.setStatus(null);
        }

        boolean update = employeeService.updateById(employee);



        if (update){
            return R.success(null);
        }else return R.error("修改失败");
    }


    @GetMapping("/{id}") // 使用{id}代表路径中传来的id值，然后使用@PathVariable注解接收
    public @ResponseBody R<Employee> queryEmployeeById(@PathVariable("id") Long id){
        Employee employee = employeeService.getById(id);

        if (employee == null){
            return R.error("用户ID不存在");
        }

        return R.success(employee);
    }

}
