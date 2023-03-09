package com.example.ruiji_takeout;

import com.example.ruiji_takeout.backstage.pojo.Dish;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;

@SpringBootTest
class RuijiTakeoutApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    public void test1(){

        ArrayList<Dish> list = new ArrayList<>();
        /*
        * list中存放的是对象的引用，而非对象；如果将对象定义在循环外部，虽然每次都修改了状态码，但是数组中元素指向的对象是同一个，最后数组中元素的状态码是相同的
        * */
        Dish dish = new Dish();
        for (int i = 0; i < 3; i++) {
            dish.setStatus(i);
            list.add(dish);
        }

        list.stream().forEach(dish1 -> System.out.println(dish1));
    }

}
