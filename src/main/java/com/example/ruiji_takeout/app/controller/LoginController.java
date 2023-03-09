package com.example.ruiji_takeout.app.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.ruiji_takeout.app.pojo.User;
import com.example.ruiji_takeout.app.service.MailService;
import com.example.ruiji_takeout.app.service.UserService;
import com.example.ruiji_takeout.common.R;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @author Jmd
 * @create 2023-03-2023/3/8-12:26
 * @Description：
 */
@RestController
@RequestMapping("/user")
public class LoginController {

    @Resource
    private MailService mailService;

    @Resource
    private UserService userService;

    @GetMapping("/sendMsg")
    public R sendCode(@RequestBody User user, HttpSession session){

        String email = user.getPhone(); // 获取充当手机号的email

        String code = mailService.sendEmailForLogin(email);
        R<Object> r = R.success(null);
        r.setMsg("发送成功");
        session.setAttribute("toEmail",email);
        session.setAttribute("code",code);
        return r;
    }

    @RequestMapping("/login1")
    public R login1(@RequestBody Map<String,String> map,
                   @SessionAttribute("toEmail") String email, @SessionAttribute("code") String code, HttpSession session){

        String unverifiedEmail = map.get("phone");
        String UnverifiedCode = map.get("code");

        // 手机号验证码登录换成了邮箱验证码登录，把邮箱@之前的截取下来，充当手机号
        String phone = unverifiedEmail.substring(0, unverifiedEmail.lastIndexOf("@"));

        // 从数据库中查询这个邮箱对应的用户
        User user = userService.getOne(new LambdaQueryWrapper<User>().eq(User::getPhone, phone));
        String msg = "";
        if (user == null){ // 数据库中没有对应用户
            // 移除session，防止占用空间
            session.removeAttribute("toEmail");
            session.removeAttribute("code");

            msg = "您输入的邮箱未注册，请注册";
        }else if (email.equals(unverifiedEmail) && !(code.equals(UnverifiedCode))){ // 邮箱正确，但验证码错误
            // 验证码错误的情况下，用户可能会重新输入验证码，先不移除session
            msg = "您输入的验证码错误,请重试";
        }else if (email.equals(unverifiedEmail) && code.equals(UnverifiedCode)){ // 邮箱与验证码都正确

            session.removeAttribute("toEmail");
            session.removeAttribute("code");

            if (user.getStatus() == 1){ // 用户状态正常，则登录成功

                session.setAttribute("user",user);
                R<Object> r = R.success(null);
                r.setMsg("登录成功");
                return r;
            }else  msg = "您的账号已被锁定，请联系管理员";


        }

        return R.error(msg);
    }

    @PostMapping("/login")
    public R login(@RequestBody Map<String,String> phoneMap, HttpSession session){
        String phone = phoneMap.get("phone");
        User user = userService.getOne(new LambdaQueryWrapper<User>().eq(User::getPhone, phone));

        session.setAttribute("user",user);

        return R.success(user);
    }

}
