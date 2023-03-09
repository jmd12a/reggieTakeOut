package com.example.ruiji_takeout.app.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.ruiji_takeout.app.pojo.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Jmd
 * @create 2023-03-2023/3/8-15:57
 * @Description：
 */

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
