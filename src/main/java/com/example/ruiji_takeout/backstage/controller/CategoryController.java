package com.example.ruiji_takeout.backstage.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.ruiji_takeout.backstage.pojo.Category;
import com.example.ruiji_takeout.backstage.pojo.Dish;
import com.example.ruiji_takeout.backstage.pojo.Setmeal;
import com.example.ruiji_takeout.backstage.service.CategoryService;
import com.example.ruiji_takeout.backstage.service.DishService;
import com.example.ruiji_takeout.backstage.service.SetmealService;
import com.example.ruiji_takeout.common.R;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sun.awt.geom.AreaOp;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Jmd
 * @create 2023-03-2023/3/6-18:01
 * @Description：
 */
@Controller
@RequestMapping("/category")
public class CategoryController {

    @Resource
    private CategoryService categoryService;

    @Resource
    private DishService dishService;

    @Resource
    private SetmealService setmealService;

    @Resource
    private RedisTemplate redisTemplate;

    @RequestMapping("/page")
    public @ResponseBody R<Page<Category>> page(@RequestParam("page") Integer pageNum, Integer pageSize){
        Page<Category> page = new Page<>(pageNum, pageSize);

        page.addOrder(OrderItem.asc("sort")); //排序方式

        categoryService.page(page);

        return R.success(page);
    }

    @PostMapping
    public @ResponseBody R addCategory(@RequestBody Category category){
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Category::getSort,category.getSort());
        Category one = categoryService.getOne(queryWrapper);

        if (one != null){
            return R.error("您输入的排序值 '"+category.getSort()+"' 已存在");
        }

        boolean save = categoryService.save(category);

        if (save){
            return R.success(null);
        }else return R.error("添加失败");
    }

    @PutMapping
    public @ResponseBody R updateCategory(@RequestBody Category category){
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Category::getSort,category.getSort());
        Category one = categoryService.getOne(queryWrapper);

        // 如果新排序存在且不属于原本的套餐排序
        if (one != null && one.getId().longValue() != category.getId().longValue()){
            return R.error("您输入的排序值 '"+category.getSort()+"' 已存在");
        }

        boolean update = categoryService.updateById(category);

        if (update){
            return R.success(null);
        }else return R.error("更新失败");
    }

    @DeleteMapping
    public @ResponseBody R deleteCategory(@RequestParam("ids") Long id) {
        Category category = categoryService.getById(id);
        Long categoryId = category.getId();

        if (category.getType() == 1) {
            LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Dish::getCategoryId, categoryId);
            int count = dishService.count(queryWrapper);

            if (count != 0) {
                return R.error("该菜品分类有相关联的菜品，请先删除相关菜品");
            }

        } else if (category.getType() == 2) {
            LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Setmeal::getCategoryId, categoryId);
            int count = setmealService.count(queryWrapper); // 查询这个查询包装类有多少条结果
            if (count != 0) {
                return R.error("该套餐分类有相关联的套餐，请先删除相关套餐");
            }
        }

        boolean remove = categoryService.removeById(id);

        if (remove) {
            return R.success(null);
        } else return R.error("更新失败");
    }

    @GetMapping("/list")
    public @ResponseBody R getCategoryListByType(Integer type){


        String key = "Category::Type"+type;

        List<Category> categories = (List<Category>) redisTemplate.opsForValue().get(key);

        if (categories == null){
            // categories = categoryService.getCategoryListByType(type);
            categories = getCategories(type);


            redisTemplate.opsForValue().set(key,categories);
        }

        return R.success(categories);
    }

    private List<Category> getCategories(Integer type) {
        ArrayList<Category> categories = new ArrayList<>();
        if (type == 1){
            Category category = new Category();
            category.setId(123l);
            category.setName("酸菜鱼");

            Category category1 = new Category();
            category1.setName("芒果汁");
            category1.setId(456l);
            categories.add(category);
            categories.add(category1);
        }else if (type == 2){
            Category category = new Category();
            category.setId(101l);
            category.setName("卷凉皮");
            Category category1 = new Category();
            category1.setName("粉蒸肉");
            category.setId(102l);
            categories.add(category);
            categories.add(category1);
        }

        return categories;
    }


}
