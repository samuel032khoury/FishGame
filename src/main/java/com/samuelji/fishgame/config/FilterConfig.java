package com.samuelji.fishgame.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.samuelji.fishgame.security.DynamicRateLimiter;

@Configuration
public class FilterConfig {

    // @Bean
    // public FilterRegistrationBean<APITimestampFilter> apiTimestampFilter() {
    // FilterRegistrationBean<APITimestampFilter> registrationBean = new
    // FilterRegistrationBean<>();
    // registrationBean.setFilter(new APITimestampFilter());
    // registrationBean.addUrlPatterns("/user/*", "/fish/*", "/shop/*");
    // return registrationBean;
    // }

    @Bean
    public FilterRegistrationBean<DynamicRateLimiter> dynamicRateLimiter() {
        FilterRegistrationBean<DynamicRateLimiter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new DynamicRateLimiter());
        registrationBean.addUrlPatterns("/*");
        return registrationBean;
    }

}
