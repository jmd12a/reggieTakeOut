package com.example.ruiji_takeout.app.service.impl;

import com.example.ruiji_takeout.app.service.MailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Random;

/**
 * @author Jmd
 * @create 2023-03-2023/3/8-12:12
 * @Description：
 */
@Service
public class MailServiceImpl implements MailService {

    @Resource
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String fromEmailName;

    @Override
    public String sendEmailForLogin(String toEmail) {
        String code = randomCode();
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setFrom(fromEmailName);
        mailMessage.setTo(toEmail);
        mailMessage.setText("[瑞吉外卖] 您的登录验证是："+code);
        javaMailSender.send(mailMessage);

        return code;
    }

    @Override
    public String randomCode() {

        StringBuilder str = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 6; i++) {
            int s = random.nextInt(10);
            str.append(s);
        }

        return str.toString();
    }
}
