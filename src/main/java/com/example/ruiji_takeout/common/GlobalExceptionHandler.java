package com.example.ruiji_takeout.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.sql.SQLIntegrityConstraintViolationException;

/**
 * @author Jmd
 * @create 2023-03-2023/3/4-11:22
 * @Description：
 */
@ControllerAdvice(annotations = {Controller.class, RestController.class})
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public @ResponseBody R exceptionHandler(SQLIntegrityConstraintViolationException e){
        log.error(e.getMessage());
        String[] values = e.getMessage().split("'", 3);
        e.printStackTrace();
        return R.error(" '"+values[1] + "' 已存在");
    }

    @ExceptionHandler(IOException.class)
    public @ResponseBody R iOException(IOException e){
        log.error(e.getMessage());
        e.printStackTrace();
        return R.error("IO异常");
    }
}
