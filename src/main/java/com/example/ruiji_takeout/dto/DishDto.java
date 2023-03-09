package com.example.ruiji_takeout.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.example.ruiji_takeout.backstage.pojo.Dish;
import com.example.ruiji_takeout.backstage.pojo.DishFlavor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Jmd
 * @create 2023-03-2023/3/7-15:29
 * @Description：
 */

// DTO是数据传输对象，一般用于控制层和展示层的数据传输
@Data
public class DishDto extends Dish {

    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;

    private Integer copies;

}
