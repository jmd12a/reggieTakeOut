package com.example.ruiji_takeout;

import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Slf4j // 为这个类提供log属性来输出日志
@SpringBootApplication
@MapperScan({"com.example.ruiji_takeout.backstage.mapper","com.example.ruiji_takeout.app.mapper"})
@EnableTransactionManagement // 开启事务自动管理
public class RuijiTakeoutApplication {

    public static void main(String[] args) {
        SpringApplication.run(RuijiTakeoutApplication.class, args);
        log.info("项目启动成功......");
    }

}
