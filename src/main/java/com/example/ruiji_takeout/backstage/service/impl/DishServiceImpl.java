package com.example.ruiji_takeout.backstage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.ruiji_takeout.backstage.mapper.DishFlavorMapper;
import com.example.ruiji_takeout.backstage.mapper.DishMapper;
import com.example.ruiji_takeout.backstage.mapper.SetmealDishMapper;
import com.example.ruiji_takeout.backstage.mapper.SetmealMapper;
import com.example.ruiji_takeout.backstage.pojo.Dish;
import com.example.ruiji_takeout.backstage.pojo.DishFlavor;
import com.example.ruiji_takeout.backstage.pojo.Setmeal;
import com.example.ruiji_takeout.backstage.pojo.SetmealDish;
import com.example.ruiji_takeout.backstage.service.DishService;
import com.example.ruiji_takeout.common.R;
import com.example.ruiji_takeout.dto.DishDto;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Consumer;

/**
 * @author Jmd
 * @create 2023-03-2023/3/6-20:34
 * @Description：
 */
@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Resource
    private DishFlavorMapper dishFlavorMapper;

    @Resource
    private SetmealDishMapper setmealDishMapper;


    @Override
    @Transactional
    public boolean addDish(Map<String, Object> paramMap) {
        // 封装菜品数据
        Dish dish = new Dish();
        String name = (String) paramMap.get("name");
        dish.setName(name);
        dish.setCategoryId(Long.parseLong((String) paramMap.get("categoryId")) );
        dish.setDescription((String) paramMap.get("description"));
        dish.setImage((String) paramMap.get("image"));
        dish.setPrice(((Integer)paramMap.get("price")).doubleValue());
        dish.setCode(UUID.randomUUID().toString());

        int insert = baseMapper.insert(dish); // dish原本没有id，经过这一步后，dish的ID被Mybatis-Plus自动赋值了，也就有Id了，且和数据库中存储的对应
        
        if (insert == 1){

            // 获取插入后的dishId
            /*LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Dish::getName,name);
            Dish one = baseMapper.selectOne(queryWrapper);
            Long dishId = one.getId();*/
            Long dishId = dish.getId();
            
            // 封装这个dish对应的dishFlavor数据
            List<Map<String,String>> flavorsMapList = (List<Map<String,String>>) paramMap.get("flavors");

            // 获取list的流再使用Lambda表达式，简洁美观
            flavorsMapList.stream().forEach(dishFlavorMap -> {
                String flavorName = dishFlavorMap.get("name");
                String flavorValue = dishFlavorMap.get("value");
                DishFlavor dishFlavor = new DishFlavor();
                dishFlavor.setDishId(dishId);
                dishFlavor.setName(flavorName);
                dishFlavor.setValue(flavorValue);
                // 封装好后插入
                dishFlavorMapper.insert(dishFlavor);
            });

            return true;
        }

        
        
        return false;
    }

    @Override
    public DishDto getDishById(Long id) {

        Dish dish = baseMapper.selectById(id);
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish,dishDto);

        LambdaQueryWrapper<DishFlavor> dishFlavorQueryWrapper = new LambdaQueryWrapper<>();
        dishFlavorQueryWrapper.eq(DishFlavor::getDishId,id);

        List<DishFlavor> flavors = dishFlavorMapper.selectList(dishFlavorQueryWrapper);
        dishDto.setFlavors(flavors);

        return dishDto;
    }

    @Override
    @Transactional
    public boolean updateDish(DishDto dishDto) {
        Dish dish = new Dish();
        List<DishFlavor> flavors = dishDto.getFlavors();

        /*BeanUtils.copyProperties(dishDto,dish);
        int update = baseMapper.updateById(dish);*/

        // 这里可以直接将dishDto传到baseMapper的方法,因为DishDto继承了Dish，baseMapper会将其当做Dish来操作，且只操作dish中的基本数据，不会操作DishDto的拓展数据
        int update = baseMapper.updateById(dishDto);

        Long dishId = dish.getId();
        if (update == 1){
            // 先把这个dish原本的所有口味查询出来
            LambdaQueryWrapper<DishFlavor> dishFlavorQueryWrapper = new LambdaQueryWrapper<>();
            dishFlavorQueryWrapper.eq(DishFlavor::getDishId,dishId);
            List<DishFlavor> originalFlavors = dishFlavorMapper.selectList(dishFlavorQueryWrapper);

            // 一一比对，如果存在于更新后的flavors，则将其从更新后的flavors移除（接下来也无需出入）,否则直接删除
            originalFlavors.stream().forEach( dishFlavor -> {

                if (flavors.contains(dishFlavor)){
                    flavors.remove(dishFlavor);
                }else dishFlavorMapper.deleteById(dishFlavor.getId());

            });

            // 插入新的flavors
            flavors.forEach(new Consumer<DishFlavor>() {
                @Override
                public void accept(DishFlavor dishFlavor) {
                    dishFlavor.setDishId(dishId);
                    dishFlavorMapper.insert(dishFlavor);
                }
            });
            return true;
        }

        return false;
    }

    @Transactional
    @Override
    public R updateStatus(Integer status, Long[] ids) {

        QueryWrapper<SetmealDish> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("dish_id",ids);
        Integer count = setmealDishMapper.selectCount(queryWrapper);
        if (count>0 && status == 0){
            // 查询食品-套餐关系表中是否有dish_id在ids中的记录，如果有，且修改是停售，则无法更新
            return  R.error("当前要停售的菜肴中，有包含于套餐内的，无法停售，请重新操作");
        }

        Arrays.stream(ids).forEach(new Consumer<Long>() {
            @Override
            public void accept(Long id) {
                LambdaUpdateWrapper<Dish> updateWrapper = new LambdaUpdateWrapper<>();
                updateWrapper.eq(Dish::getId,id);
                updateWrapper.set(Dish::getStatus,status);

                baseMapper.update(null ,updateWrapper);
            }
        });

        return R.success(null);
    }

    @Override
    public R deleteDish(Long[] ids) {
        QueryWrapper<SetmealDish> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("dish_id",ids);
        Integer count = setmealDishMapper.selectCount(queryWrapper);
        if (count>0 ){
            // 查询食品-套餐关系表中是否有dish_id在ids中的记录，如果有，则无法删除
            return  R.error("当前要删除的菜肴中，有包含于套餐内的，无法删除，请重新操作");
        }

        // 如果没有,批量删除
        baseMapper.deleteBatchIds(Arrays.asList(ids));

        return R.success(null);
    }
}
