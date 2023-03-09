package com.example.ruiji_takeout.app.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.ruiji_takeout.app.mapper.AddressBookMapper;
import com.example.ruiji_takeout.app.mapper.OrderDetailMapper;
import com.example.ruiji_takeout.app.mapper.OrdersMapper;
import com.example.ruiji_takeout.app.mapper.ShoppingCartMapper;
import com.example.ruiji_takeout.app.pojo.*;
import com.example.ruiji_takeout.app.service.OrdersService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Jmd
 * @create 2023-03-2023/3/9-9:32
 * @Description：
 */
@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements OrdersService {

    @Resource
    private AddressBookMapper addressBookMapper;

    @Resource
    private ShoppingCartMapper shoppingCartMapper;

    @Resource
    private OrderDetailMapper orderDetailMapper;

    @Override
    @Transactional
    public Boolean saveOrder(Orders orders) {
        // 1.封装orders
        orders.setOrderTime(LocalDateTime.now());
        orders.setCheckoutTime(LocalDateTime.now());
        AddressBook addressBook = addressBookMapper.selectById(orders.getAddressBookId());
        orders.setAddress(addressBook.getDetail());
        orders.setPhone(addressBook.getPhone());
        orders.setConsignee(addressBook.getConsignee());

        // 根据UserId查询所有的购物车记录

        Long userId = orders.getUserId();
        List<ShoppingCart> shoppingCarts =
                shoppingCartMapper.selectList(new LambdaQueryWrapper<ShoppingCart>().eq(ShoppingCart::getUserId, userId));

        // 求订单价格
        double orderAmount = shoppingCarts.stream().mapToDouble(shoppingCart -> {
            // 算购物车的每条记录的的价格时，要使用amount*double而不是单独的amount
            Double amount = (shoppingCart.getAmount().doubleValue())*shoppingCart.getNumber();
            return amount;
        }).sum();
        orders.setAmount(new BigDecimal(orderAmount));

        int insert = baseMapper.insert(orders);

        Long ordersId = orders.getId();
        if (insert == 1){
            // 封装OrderDetail
            List<OrderDetail> orderDetails = shoppingCarts.stream().map(shoppingCart -> {
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setOrderId(ordersId);
                BeanUtils.copyProperties(shoppingCart, orderDetail, "id");
                return orderDetail;
            }).collect(Collectors.toList());

            // 插入OrderDetail
            orderDetails.stream().forEach(orderDetail -> orderDetailMapper.insert(orderDetail));

            // 都插入完毕后删除购物车记录
            int delete = shoppingCartMapper.delete(new LambdaQueryWrapper<ShoppingCart>().eq(ShoppingCart::getUserId, userId));

            if (delete>0) return true;
        }

        return false;
    }
}
