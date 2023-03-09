package com.example.ruiji_takeout.app.service;

/**
 * @author Jmd
 * @create 2023-03-2023/3/8-12:10
 * @Description：
 */
public interface MailService {

    String sendEmailForLogin(String email);

    String randomCode();

}
