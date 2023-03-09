package com.example.ruiji_takeout.app.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.ruiji_takeout.app.pojo.AddressBook;
import com.example.ruiji_takeout.app.pojo.OrderDetail;
import com.example.ruiji_takeout.app.pojo.Orders;
import com.example.ruiji_takeout.app.pojo.User;
import com.example.ruiji_takeout.app.service.AddressBookService;
import com.example.ruiji_takeout.app.service.OrderDetailService;
import com.example.ruiji_takeout.app.service.OrdersService;
import com.example.ruiji_takeout.common.R;
import com.example.ruiji_takeout.dto.OrdersDto;
import com.sun.org.apache.xpath.internal.operations.Or;
import com.sun.prism.impl.BaseContext;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Jmd
 * @create 2023-03-2023/3/9-9:38
 * @Description：
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    @Resource
    private OrderDetailService orderDetailService;

    @Resource
    private OrdersService ordersService;

    @Resource
    private AddressBookService addressBookService;

    @PostMapping("/submit")
    public R saveOrder(@RequestBody Orders orders, @SessionAttribute User user){

        orders.setUserId(user.getId());
        orders.setUserName(user.getName());
        Boolean save = ordersService.saveOrder(orders);

        if (save){
            return R.success(null);
        }
        return R.error("下单失败");
    }

    @GetMapping("/userPage")
    public R<Page<OrdersDto>> getPageByUserId(@RequestParam("page") Integer pageNum, Integer pageSize, @SessionAttribute User user){

        Long userId = user.getId();
        String userName = user.getName();


        // 分页查询orders表
        Page<Orders> ordersPage = new Page<>(pageNum,pageSize);
        ordersPage.addOrder(OrderItem.desc("order_time"));
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<Orders>().eq(Orders::getUserId, userId);

        ordersPage = ordersService.page(ordersPage, queryWrapper);

        List<Orders> ordersList = ordersPage.getRecords();

        List<OrdersDto> ordersDtoList = ordersList.stream().map(orders -> {
            // 新建并封装orderDto
            OrdersDto ordersDto = new OrdersDto();
            BeanUtils.copyProperties(orders,ordersDto);
            AddressBook addressBook = addressBookService.getById(orders.getAddressBookId());
            ordersDto.setPhone(addressBook.getPhone());
            ordersDto.setConsignee(addressBook.getConsignee());
            ordersDto.setAddress(addressBook.getDetail());
            ordersDto.setUserName(userName);

            List<OrderDetail> orderDetailList =
                    orderDetailService.list(new LambdaQueryWrapper<OrderDetail>().eq(OrderDetail::getOrderId, orders.getId()));
            ordersDto.setOrderDetails(orderDetailList);
            return ordersDto;
        }).collect(Collectors.toList());

        Page<OrdersDto> ordersDtoPage = new Page<>();
        BeanUtils.copyProperties(ordersPage,ordersDtoPage,"records");
        ordersDtoPage.setRecords(ordersDtoList);

        return R.success(ordersDtoPage);
    }
}
