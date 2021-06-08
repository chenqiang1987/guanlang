package com.twc.guanlang.shiro;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

/**
 * @Author
 * @Date 2020/7/14 14:12
 * @Version 版本号
 * @Description 解决SameSite=Lax导致前端无法携带Cookie的坑
 */
@Configuration
public class SpringSessionConfig {

    public SpringSessionConfig() {
    }

    @Bean
    public CookieSerializer httpSessionIdResolver() {
        DefaultCookieSerializer cookieSerializer = new DefaultCookieSerializer();
        // 取消仅限同一站点设置
        cookieSerializer.setSameSite(null);
        return cookieSerializer;
    }
}
