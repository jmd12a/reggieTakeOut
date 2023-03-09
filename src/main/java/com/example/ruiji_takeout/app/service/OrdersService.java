package com.example.ruiji_takeout.app.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.ruiji_takeout.app.pojo.Orders;

/**
 * @author Jmd
 * @create 2023-03-2023/3/9-9:32
 * @Description：
 */
public interface OrdersService extends IService<Orders> {
    Boolean saveOrder(Orders orders);
}
