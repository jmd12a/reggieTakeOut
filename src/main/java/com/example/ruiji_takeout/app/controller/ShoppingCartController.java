package com.example.ruiji_takeout.app.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.ruiji_takeout.app.pojo.Orders;
import com.example.ruiji_takeout.app.pojo.ShoppingCart;
import com.example.ruiji_takeout.app.pojo.User;
import com.example.ruiji_takeout.app.service.ShoppingCartService;
import com.example.ruiji_takeout.backstage.pojo.Setmeal;
import com.example.ruiji_takeout.backstage.service.SetmealService;
import com.example.ruiji_takeout.common.R;
import com.example.ruiji_takeout.dto.OrdersDto;
import com.sun.prism.impl.BaseContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Jmd
 * @create 2023-03-2023/3/8-17:06
 * @Description：
 */
@RestController
@RequestMapping("/shoppingCart")
@Slf4j
public class ShoppingCartController {

    @Resource
    private ShoppingCartService shoppingCartService;

    @Resource
    private SetmealService setmealService;


    @GetMapping("/list")
    public R<List<ShoppingCart>> getShoppingCart(@SessionAttribute User user){
        log.info(Thread.currentThread().toString());
        // 查询当前用户的所有购物车记录
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,user.getId());

        List<ShoppingCart> shoppingCartList = shoppingCartService.list(queryWrapper);

        return R.success(shoppingCartList);
    }

    @PostMapping("/add")
    public R addShoppingCart(@RequestBody ShoppingCart shoppingCart, @SessionAttribute("user") User user){
        log.info(Thread.currentThread().toString());
        Long userId = user.getId();
        shoppingCart.setUserId(userId);

        // 查询套餐表中是否有id这个的套餐
        QueryWrapper<Setmeal> queryWrapper = new QueryWrapper<>();
        Integer count = setmealService.count(queryWrapper.eq("id",shoppingCart.getSetmealId()));

        // 如果没有，则添加的是Dish,把dishId改为前端传来的setmealId,setmealId改为空
        if (count != 1){
            shoppingCart.setDishId(shoppingCart.getSetmealId()); // 将setmealId设置为DishId，因为前端传来的无论是那个都当做是setmealId了
            shoppingCart.setSetmealId(null);
        }

        // 查询购物车中，是否有该用户的这份套餐
        ShoppingCart one = shoppingCartService.getOne(new LambdaQueryWrapper<ShoppingCart>()
                .eq(ShoppingCart::getName, shoppingCart.getName())
                .eq(ShoppingCart::getUserId, userId));

        if (one != null){  // 如果购物车中已经有一份了,增加份数
            Integer number = one.getNumber();
            one.setNumber(++number);
            shoppingCartService.updateById(one);
        }else shoppingCartService.save(shoppingCart); // 保存记录



        return R.success(shoppingCart);
    }

    @DeleteMapping("/clean")
    public R deleteShoppingCartByUserId(@SessionAttribute User user){
        log.info(Thread.currentThread().toString());
        Long id = user.getId();
        if (id != null){
            LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<ShoppingCart>().eq(ShoppingCart::getUserId, id);
            boolean remove = shoppingCartService.remove(queryWrapper);
            if (remove) {
                return R.success(null);
            }
        }
        return R.error("删除失败");
    }



}
