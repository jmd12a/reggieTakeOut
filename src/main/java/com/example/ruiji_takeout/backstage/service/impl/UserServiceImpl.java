package com.example.ruiji_takeout.backstage.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.ruiji_takeout.app.mapper.UserMapper;
import com.example.ruiji_takeout.app.pojo.User;
import com.example.ruiji_takeout.app.service.UserService;
import org.springframework.stereotype.Service;

/**
 * @author Jmd
 * @create 2023-03-2023/3/8-15:57
 * @Description：
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
