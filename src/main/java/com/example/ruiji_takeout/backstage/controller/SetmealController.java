package com.example.ruiji_takeout.backstage.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.ruiji_takeout.backstage.pojo.Setmeal;
import com.example.ruiji_takeout.backstage.service.CategoryService;
import com.example.ruiji_takeout.backstage.service.SetmealService;
import com.example.ruiji_takeout.common.R;
import com.example.ruiji_takeout.dto.SetmealDto;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Jmd
 * @create 2023-03-2023/3/7-22:44
 * @Description：
 */

@RestController// 相当于Controller和ResponseBody
@RequestMapping("/setmeal")
public class SetmealController {

    @Resource
    private SetmealService setmealService;

    @Resource
    private CategoryService categoryService;


    @GetMapping("page")
    public R<Page<SetmealDto>> page(@RequestParam("page") Integer pageNum, Integer pageSize, String name){
        Page<Setmeal> page = new Page<>(pageNum, pageSize);
        page.addOrder(OrderItem.desc("update_time"), OrderItem.asc("name"));

        // 分页查询出setmeal信息并获取setmealList
        setmealService.page(page);
        List<Setmeal> setmealList = page.getRecords();

        // 使用lambda表达式，通过setmealList获取SetmealDtoList
        List<SetmealDto> setmealDtoList = setmealList.stream().map(setmeal -> {
            Long categoryId = setmeal.getCategoryId();
            String categoryName = categoryService.getById(categoryId).getName();

            SetmealDto setmealDto = new SetmealDto();
            setmealDto.setCategoryName(categoryName);
            BeanUtils.copyProperties(setmeal,setmealDto);
            return setmealDto;
        }).collect(Collectors.toList());

        // page的属性赋值给setmealDtoPage,并使用SetmealDtoList赋值给setmealDtoPage的records
        Page<SetmealDto> setmealDtoPage = new Page<>();
        BeanUtils.copyProperties(page,setmealDtoPage,"records");
        setmealDtoPage.setRecords(setmealDtoList);

        return R.success(setmealDtoPage);
    }

    @PostMapping
    public R saveSetmeal(@RequestBody SetmealDto setmealDto){

        boolean b = setmealService.saveSetmeal(setmealDto);

        return R.success(null);
    }

    @GetMapping("/{id}")
    public R<SetmealDto> getSetmeal(@PathVariable("id") Long id){
        SetmealDto setmealDto = setmealService.getSetmealById(id);

        return R.success(setmealDto);
    }

    @PutMapping
    public R putSetmeal(@RequestBody SetmealDto setmealDto){

        boolean b = setmealService.saveSetmealWithSetmealDish(setmealDto);

        if (b){
            return R.success(null);
        }

        return R.error("更新失败");
    }

    @DeleteMapping
    public R deleteSetmealBitchByIds(Long[] ids){
        boolean b = setmealService.deleteSetmealBitchByIds(ids);

        if (b){
            return R.success(null);
        }

        return R.error("删除失败");
    }

    @PostMapping("/status/{status}")
    public R updateStatus(@PathVariable Integer status,Long[] ids){
        boolean b = setmealService.updateStatusBatch(status,ids);
        if (b) {
            return R.success(null);
        }

        return R.error("修改状态失败");
    }

    @GetMapping("/list")
    public R<List<Setmeal>> getSetmealListByCategoryId(Long categoryId, Integer status){
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Setmeal::getCategoryId,categoryId);
        queryWrapper.eq(Setmeal::getStatus,status);
        List<Setmeal> setmealList = setmealService.list(queryWrapper);

        return R.success(setmealList);
    }


}
