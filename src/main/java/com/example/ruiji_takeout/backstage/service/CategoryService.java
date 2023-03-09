package com.example.ruiji_takeout.backstage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.ruiji_takeout.backstage.pojo.Category;
import com.example.ruiji_takeout.backstage.pojo.Employee;

import java.util.List;

/**
 * @author Jmd
 * @create 2023-03-2023/3/6-17:57
 * @Description：
 */
public interface CategoryService extends IService<Category> {

    List<Category> getCategoryListByType(Integer type);
}
