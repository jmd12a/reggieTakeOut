package com.example.ruiji_takeout.backstage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.ruiji_takeout.backstage.pojo.Category;
import com.example.ruiji_takeout.backstage.pojo.Employee;
import com.fasterxml.jackson.databind.cfg.MapperBuilder;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Jmd
 * @create 2023-03-2023/3/6-17:55
 * @Description：
 */
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {

}
