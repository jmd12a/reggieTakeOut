package com.example.ruiji_takeout.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.sun.javafx.binding.StringFormatter;
import com.sun.mail.smtp.DigestMD5;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.DigestUtils;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class JdbcTemplateFactory {

    private static final Map<String, JdbcTemplate> jdbcTemplateMap = new HashMap<String, JdbcTemplate>();
    private static final String jdbcDriver = "com.mysql.cj.jdbc.Driver";
    private static Integer num = 0;


    public static JdbcTemplate getJdbcTemplate(String jdbcUrl,
                                               String username,
                                               String password) {

        log.info("现在是创建jdbcTemplate的次数为：{}" ,num);
        String key = getKey(jdbcUrl, username, password);
        if (!jdbcTemplateMap.containsKey(key)) {
            num += 1;
            DruidDataSource dataSource = DruidDataSourceBuilder.create().build();
            dataSource.setDriverClassName(jdbcDriver);
            dataSource.setUrl(jdbcUrl);
            dataSource.setUsername(username);
            dataSource.setPassword(password);

            JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
            jdbcTemplateMap.put(key, jdbcTemplate);
            return jdbcTemplate;
        }
        return jdbcTemplateMap.get(key);
    }


    private static String getKey(String jdbcUrl,
                                 String username,
                                 String password) {
        String keyString = StringFormatter.format("%s#%s#%s", jdbcUrl, username, password).getValueSafe();
        return DigestUtils.md5DigestAsHex(keyString.getBytes());
    }

}
