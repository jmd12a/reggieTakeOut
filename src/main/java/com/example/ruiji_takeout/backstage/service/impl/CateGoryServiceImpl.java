package com.example.ruiji_takeout.backstage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.ruiji_takeout.backstage.mapper.CategoryMapper;
import com.example.ruiji_takeout.backstage.pojo.Category;
import com.example.ruiji_takeout.backstage.pojo.Employee;
import com.example.ruiji_takeout.backstage.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Jmd
 * @create 2023-03-2023/3/6-17:58
 * @Description：
 */
@Service // 继承ServiceImpl时指定了mapper，无需在把mapper定义到属性中了
public class CateGoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Override
    public List<Category> getCategoryListByType(Integer type) {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(type != null,Category::getType,type);
        List<Category> categoryList = baseMapper.selectList(queryWrapper);
        return categoryList;
    }
}
