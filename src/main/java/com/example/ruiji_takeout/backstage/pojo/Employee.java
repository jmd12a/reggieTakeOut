package com.example.ruiji_takeout.backstage.pojo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Jmd
 * @create 2023-03-2023/3/2-23:17
 * @Description：
 */

@Data
public class Employee implements Serializable {

    // serialVersionUID叫做流标识符，即类的版本定义，作用是在序列化时保持版本的兼容性,设置为1L，在类进行版本更新时只要标识符不变，类的传输就不会报错
    private static final long serialVersionUID = 1L;

    // Long型的数据，在浏览器展示，如果长度过长，可能会丢失精度
    private Long id;

    private String name;

    private String username;

    private String password;

    private String phone;

    private Character sex;

    private String idNumber;

    // Character类型的属性可以接收前端传来的单个字符串，但是不能接收前端传来的整型
    private Integer status;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    // @TableField(fill = FieldFill.INSERT) // 会根据配置类对字段进行自动填充
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    // @TableField(fill = FieldFill.INSERT_UPDATE)
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;
}
