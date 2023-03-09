package com.example.ruiji_takeout.config;

import com.example.ruiji_takeout.backstage.Interceptor.LoginInterceptor;
import com.example.ruiji_takeout.backstage.Interceptor.UserLoginInterceptor;
import com.example.ruiji_takeout.common.JacksonObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @author Jmd
 * @create 2023-03-2023/3/2-23:02
 * @Description：
 */
@Slf4j
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        log.info("静态资源开始映射......");
        // 静态资源映射
        registry.addResourceHandler("/backend/**").addResourceLocations("classpath:/backend/");
        registry.addResourceHandler("/front/**").addResourceLocations("classpath:/front/");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        /*
        * 我们允许这个拦截器访问所有的静态资源以及控制器方法中的登录和登出
        *
        * */
        registry.addInterceptor(new LoginInterceptor()).
                addPathPatterns("/**").excludePathPatterns("/backend/**",
                        "/front/**", "/employee/login","/employee/logout","/user/**",
                        "/category/**", "/shoppingCart/**", "/error/**","/dish/**",
                        "/common/**","/setmeal/**","/addressBook/**","/order/**"
                        );

        /*registry.addInterceptor(new UserLoginInterceptor()).addPathPatterns("/app/**").excludePathPatterns("/app/sendMsg")
                .excludePathPatterns("/app/login").excludePathPatterns("/backend/**", "/front/**");*/
    }


    // 消息转换器，将控制器方法的返回值转换成前端所需的类型，或者将前端的参数，转换成控制器方法的形参
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        // 创建消息转换器
        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();

        // 设置消息转换器的Mapper映射类型
        messageConverter.setObjectMapper(new JacksonObjectMapper());

        // 将消息转换器追加到springmvc的消息转换器集合
        converters.add(0,messageConverter); // 要放到第一位
    }
}
