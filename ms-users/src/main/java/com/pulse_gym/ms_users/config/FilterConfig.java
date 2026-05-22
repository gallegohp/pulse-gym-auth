package com.pulse_gym.ms_users.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

import com.pulse_gym.ms_users.filter.JwtContextFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class FilterConfig {

    private final JwtContextFilter jwtContextFilter;

    @Bean
    public FilterRegistrationBean<JwtContextFilter> jwtContextFilterRegistration() {
        FilterRegistrationBean<JwtContextFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(jwtContextFilter);
        registrationBean.addUrlPatterns("/api/*");
        registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return registrationBean;
    }
}