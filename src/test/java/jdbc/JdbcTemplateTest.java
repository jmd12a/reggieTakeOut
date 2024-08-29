package jdbc;


import com.baomidou.mybatisplus.core.conditions.ISqlSegment;
import com.example.ruiji_takeout.RuijiTakeoutApplication;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.StatementCallback;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

@SpringBootTest(classes = RuijiTakeoutApplication.class)
@RunWith(SpringRunner.class)
@Slf4j
public class JdbcTemplateTest {

    @Resource
    private JdbcTemplate jdbcTemplateOne;

    // @Resource(name = "JdbcTemplateSecond")
    private JdbcTemplate jdbcTemplateTwo;

    @Test
    public void testTwoConnection() {

        Map<String, Object> stringObjectMapOne = jdbcTemplateOne.queryForMap("select * from employees where employee_id = 101");

        Map<String, Object> stringObjectMapTwo = jdbcTemplateTwo.queryForMap("select * from sys_user where user_id = 1");

        System.out.println("--------------------------------------------\\\n");

        log.info("{}\\\n", stringObjectMapOne);
        log.info(stringObjectMapTwo.toString());
        System.out.println("--------------------------------------------\\\n");
    }

    @Test
    public void testCreateTable() {

        String createTable = "CREATE TABLE company (ID INT NOT NULL AUTO_INCREMENT, Name VARCHAR(100), Position VARCHAR(100), Salary DECIMAL(10, 2), PRIMARY KEY (ID))";

        jdbcTemplateOne.execute(createTable); // jdbc.JdbcTemplateTest : 建表花费时间：392

        // 这是一个同步方法，可以根据返回结果判断建表是否成功
        long start = System.currentTimeMillis();
        Boolean execute = jdbcTemplateTwo.execute((StatementCallback<Boolean>) stmt -> stmt.execute(createTable));
        long end = System.currentTimeMillis();
        log.info("建表所花费时间：{}ms", end - start); // 115ms
    }

    @Test
    public void testCreateTableDetail() {
        // 包括设置主键自增、设置索引、设置UNIQUE、Date类型和备注
        String createTable = "CREATE TABLE `students`  (`student_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '学生ID，主键，自增',`first_mame` varchar(50) CHARACTER SET gb2312 COLLATE gb2312_chinese_ci NOT NULL COMMENT '学生的名',`last_name` varchar(50) CHARACTER SET gb2312 COLLATE gb2312_chinese_ci NOT NULL COMMENT '学生的姓',`email` varchar(100) CHARACTER SET gb2312 COLLATE gb2312_chinese_ci NOT NULL COMMENT '学生邮箱，唯一', `age` int(11) NULL DEFAULT 1 COMMENT '学生年龄',`enrollment_date` datetime NULL DEFAULT NULL COMMENT '注册时间', PRIMARY KEY (`student_id`, `first_mame`) USING BTREE, UNIQUE INDEX `email`(`email`) USING BTREE, INDEX `idx_lastname`(`last_name`) USING BTREE COMMENT '姓氏索引') ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = gb2312 COLLATE = gb2312_chinese_ci COMMENT = '学生信息表' ROW_FORMAT = Compact;";
        // 这是一个同步方法，可以根据返回结果判断建表是否成功
        long start = System.currentTimeMillis();
        Boolean execute = jdbcTemplateOne.execute((StatementCallback<Boolean>) stmt -> stmt.execute(createTable));
        long end = System.currentTimeMillis();
        log.info("建表所花费时间：{}ms", end - start); // 366ms
    }

}
