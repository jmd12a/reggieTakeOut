package com.example.ruiji_takeout.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.example.ruiji_takeout.Util.CookieUtil;
import com.example.ruiji_takeout.app.pojo.User;
import com.example.ruiji_takeout.backstage.pojo.Employee;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @author Jmd
 * @create 2023-03-2023/3/3-20:48
 * @Description：
 */
@Component // 存到ioc容器中，给特定注解标注的实体类中的属性赋值

// 这个类是元数据对象处理器，可以对实体类中所有标注了@TableField的类进行填充,根据相关方法中设置好的fieldName和对应的值
public class MybatisPlusMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        Employee employee = CookieUtil.getEmployeeOfSession();
        Long id ;
        if (employee != null){
            id = employee.getId();
        }else {
            User user = CookieUtil.getUserOfSession();
            id = user.getId();
        }

        this.setFieldValByName("createTime",LocalDateTime.now(), metaObject);
        this.setFieldValByName("updateTime",LocalDateTime.now(), metaObject);
        this.setFieldValByName("createUser",id,metaObject);
        this.setFieldValByName("updateUser",0L,metaObject);

    }

    @Override
    public void updateFill(MetaObject metaObject) {

        Employee employee = CookieUtil.getEmployeeOfSession();
        Long id ;
        if (employee != null){
            id = employee.getId();
        }else {
            User user = CookieUtil.getUserOfSession();
            id = user.getId();
        }


        this.setFieldValByName("updateTime",LocalDateTime.now(), metaObject);
        this.setFieldValByName("updateUser",id,metaObject);
    }
}
