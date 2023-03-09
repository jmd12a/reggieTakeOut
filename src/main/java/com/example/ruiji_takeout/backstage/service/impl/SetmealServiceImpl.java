package com.example.ruiji_takeout.backstage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.ruiji_takeout.backstage.mapper.SetmealDishMapper;
import com.example.ruiji_takeout.backstage.mapper.SetmealMapper;
import com.example.ruiji_takeout.backstage.pojo.Setmeal;
import com.example.ruiji_takeout.backstage.pojo.SetmealDish;
import com.example.ruiji_takeout.backstage.service.SetmealService;
import com.example.ruiji_takeout.common.R;
import com.example.ruiji_takeout.dto.SetmealDto;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * @author Jmd
 * @create 2023-03-2023/3/6-20:38
 * @Description：
 */
@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Resource
    private SetmealDishMapper setmealDishMapper;

    @Override
    @Transactional
    public boolean saveSetmeal(SetmealDto setmealDto) {
        baseMapper.insert(setmealDto);

        Long setmealId = setmealDto.getId();

        List<SetmealDish> setmealDishList = setmealDto.getSetmealDishes();

        setmealDishList.stream().forEach(setmealDish -> {
            setmealDish.setSetmealId(setmealId);
            setmealDishMapper.insert(setmealDish);
        });


        return true;
    }

    @Override
    public SetmealDto getSetmealById(Long id) {
        Setmeal setmeal = baseMapper.selectById(id);
        SetmealDto setmealDto = new SetmealDto();

        QueryWrapper<SetmealDish> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("setmeal_id",id);
        List<SetmealDish> setmealDishList = setmealDishMapper.selectList(queryWrapper);

        BeanUtils.copyProperties(setmeal,setmealDto);
        setmealDto.setSetmealDishes(setmealDishList);
        return setmealDto;
    }

    @Override
    @Transactional
    public boolean saveSetmealWithSetmealDish(SetmealDto setmealDto) {
        int insert = baseMapper.updateById(setmealDto);

        if (insert == 1){
            // 删除setmeal原本对应的所有setmeal_dish记录
            Long setmealDtoId = setmealDto.getId();
            setmealDishMapper.delete(new LambdaQueryWrapper<SetmealDish>().eq(SetmealDish::getSetmealId, setmealDtoId));

            List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
            setmealDishes.stream().forEach(setmealDish -> {
                setmealDish.setSetmealId(setmealDtoId);
                setmealDishMapper.insert(setmealDish);
            });

            return true;
        }
        return false;
    }

    @Override
    public boolean updateStatusBatch(Integer status, Long[] ids) {
        LambdaUpdateWrapper<Setmeal> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(Setmeal::getId,ids);
        updateWrapper.set(Setmeal::getStatus,status);
        int update = baseMapper.update(null, updateWrapper);
        if (update > 0){
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public boolean deleteSetmealBitchByIds(Long[] ids) {
        int i = baseMapper.deleteBatchIds(Arrays.asList(ids));
        if (i>= 0){
            LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.in(SetmealDish::getSetmealId,ids);
            int delete = setmealDishMapper.delete(queryWrapper);
            return true;
        }

        return false;
    }


}
