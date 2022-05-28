package com.xiamu.gateway.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class XiamuCorsConfiguration {

    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilter(){
        //初始化cors配置对象
        CorsConfiguration configuration = new CorsConfiguration();
        //允许跨域的域名，如果要携带cookid，不能写*。*：代表所有域名都可以跨域访问
        configuration.addAllowedOrigin("http://manage.xiamu.com");
        configuration.addAllowedOrigin("http://xiamu.com");
        configuration.addAllowedOrigin("http://api.xiamu.com");
        configuration.setAllowCredentials(true);//允许携带cookie
        //configuration.addAllowedMethod("*");//代表所有的请求方法：GET、PUT、POST、DELETE。。
        configuration.addAllowedMethod("OPTIONS");
        configuration.addAllowedMethod("HEAD");
        configuration.addAllowedMethod("GET");
        configuration.addAllowedMethod("PUT");
        configuration.addAllowedMethod("POST");
        configuration.addAllowedMethod("DELETE");
        configuration.addAllowedMethod("PATCH");
        configuration.setMaxAge(3600L);
        configuration.addAllowedHeader("*");//允许携带任何头信息
        //初始化cors配置源对象
        UrlBasedCorsConfigurationSource corsConfigurationSource=new UrlBasedCorsConfigurationSource();
        corsConfigurationSource.registerCorsConfiguration("/**",configuration);
        //反悔CorsFilter实例，参数：cors配置源对象
        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(corsConfigurationSource));
        bean.setOrder(0);
        return bean;
    }

}
