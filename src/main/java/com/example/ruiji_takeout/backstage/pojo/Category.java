package com.example.ruiji_takeout.backstage.pojo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Jmd
 * @create 2023-03-2023/3/6-17:53
 * @Description：
 */

@Data
public class Category {
    private Long id;

    private Integer type;

    private String name;

    private Integer sort;

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
