package com.example.ruiji_takeout.backstage.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.ruiji_takeout.backstage.mapper.DishFlavorMapper;
import com.example.ruiji_takeout.backstage.pojo.Category;
import com.example.ruiji_takeout.backstage.pojo.Dish;
import com.example.ruiji_takeout.backstage.pojo.DishFlavor;
import com.example.ruiji_takeout.backstage.service.CategoryService;
import com.example.ruiji_takeout.backstage.service.DishService;
import com.example.ruiji_takeout.common.R;
import com.example.ruiji_takeout.dto.DishDto;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * @author Jmd
 * @create 2023-03-2023/3/7-8:57
 * @Description：
 */

@Controller
@RequestMapping("/dish")
public class DishController {

    @Resource
    private DishService dishService;

    @Resource
    private CategoryService categoryService;

    @RequestMapping("/page")
    public @ResponseBody R<Page<DishDto>> page(@RequestParam("page") Integer pageNum, Integer pageSize,String name){
        Page<Dish> dishPage = new Page<>(pageNum, pageSize);
        Page<DishDto> dishDtoPage = new Page<>();

        dishPage.addOrder(OrderItem.asc("sort"),OrderItem.asc("name"));

        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(!(name == null || name.isEmpty()),Dish::getName, name);
        dishService.page(dishPage,queryWrapper);

        BeanUtils.copyProperties(dishPage,dishDtoPage,"records");



        List<DishDto> dishDtoList = dishPage.getRecords().stream().map(dish -> {
            /*
            *list中存放的是对象的引用，而非对象；如果将对象定义在循环外部，虽然每次都修改了对象的属性
            * 但存放到数组中的所有元素（即引用），指向的地址都是同一个。
            * 因此循环到最后，数组中所有元素指向的对象是同一个,里面的属性是最后修改的那一次的
            * */
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(dish, dishDto);
            dishDto.setCategoryName(categoryService.getById(dish.getCategoryId()).getName());

            return dishDto;
        }).collect(Collectors.toList());

        dishDtoPage.setRecords(dishDtoList);

        return R.success(dishDtoPage);
    }

    @PostMapping
    public @ResponseBody R addDish(@RequestBody Map<String,Object> paramMap){


        dishService.addDish(paramMap);
        return R.success(null);
    }

    @GetMapping("/{id}")
    public @ResponseBody R<DishDto> getDishById(@PathVariable Long id){

        // Dish中没有DishFlavor相关的属性，再创建一个DTO,继承自DishFlavor，并多出几个属性。专门给前端返回dish相关的数据使用
        DishDto dishDto = dishService.getDishById(id);

        return R.success(dishDto);
    }

    @PutMapping
    public @ResponseBody R updateDish(@RequestBody DishDto dishDto){

        boolean b = dishService.updateDish(dishDto);
        if (b){
            return R.success(null);
        }
        return R.error("更新失败");
    }

    @PostMapping("/status/{status}")
    public @ResponseBody R updateStatus(@PathVariable Integer status, Long[] ids){

        R r = dishService.updateStatus(status,ids);

        return r;
    }

    @DeleteMapping
    public @ResponseBody R deleteDish(Long[] ids){
        R r = dishService.deleteDish(ids);

        return r;
    }

    @GetMapping("/list")
    public @ResponseBody R<List<Dish>> listByCategoryId(Long categoryId){
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Dish::getCategoryId,categoryId);

        List<Dish> dishList = dishService.list(queryWrapper);

        return R.success(dishList);
    }



}
