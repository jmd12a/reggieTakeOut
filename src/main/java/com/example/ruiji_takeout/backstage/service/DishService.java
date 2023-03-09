package com.example.ruiji_takeout.backstage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.ruiji_takeout.backstage.pojo.Dish;
import com.example.ruiji_takeout.common.R;
import com.example.ruiji_takeout.dto.DishDto;

import java.util.List;
import java.util.Map;

/**
 * @author Jmd
 * @create 2023-03-2023/3/6-20:34
 * @Description：
 */
public interface DishService extends IService<Dish> {

    boolean addDish(Map<String,Object> paramMap);

    DishDto getDishById(Long id);

    boolean updateDish(DishDto dishDto);

    R updateStatus(Integer status, Long[] ids);

    R deleteDish(Long[] ids);
}
