package com.example.ruiji_takeout.common;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Jmd
 * @create 2023-03-2023/3/3-10:44
 * @Description：
 */

@Data
public class R<T>{

    // 状态码，成功或者失败
    private Integer code;

    // 返回前端的数据
    private T data;

    // 返回前端的响应信息
    private String msg;

    // 动态数据
    private Map map ;

    // 成功时调用这个静态方法返回一个成功的r对象，这个r对象不需要动态数据
    public static  <T> R<T> success(T data){
        R<T> r = new R<>();
        r.setCode(1);
        r.setData(data);

        return r;
    }

    // 成功时调用这个静态方法返回一个成功的r对象，这个r对象需要动态数据
    public static <T> R<T> success(T data,Map map){
        R<T> r = new R<>();
        r.setCode(1);
        r.setData(data);
        r.setMap(map);

        return r;
    }

    // 失败时调用这个方法返回一个失败的r对象
    public static R error(String message){
        R<Object> r = new R<>();

        r.setCode(0);
        r.setMsg(message);

        return r;
    }
}
