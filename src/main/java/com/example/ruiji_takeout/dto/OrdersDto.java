package com.example.ruiji_takeout.dto;

import com.example.ruiji_takeout.app.pojo.OrderDetail;
import com.example.ruiji_takeout.app.pojo.Orders;
import lombok.Data;
import java.util.List;

@Data
public class OrdersDto extends Orders {

    private String userName;

    private String phone;

    private String address;

    private String consignee;

    private List<OrderDetail> orderDetails;
	
}
