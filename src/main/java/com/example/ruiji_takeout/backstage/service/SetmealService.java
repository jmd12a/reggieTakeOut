package com.example.ruiji_takeout.backstage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.ruiji_takeout.backstage.pojo.Setmeal;
import com.example.ruiji_takeout.common.R;
import com.example.ruiji_takeout.dto.SetmealDto;

/**
 * @author Jmd
 * @create 2023-03-2023/3/6-20:38
 * @Description：
 */
public interface SetmealService extends IService<Setmeal> {
    boolean saveSetmeal(SetmealDto setmealDto);

    SetmealDto getSetmealById(Long id);

    boolean saveSetmealWithSetmealDish(SetmealDto setmealDto);

    boolean updateStatusBatch(Integer status, Long[] ids);

    boolean deleteSetmealBitchByIds(Long[] ids);
}
