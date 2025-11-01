package com.coinsensor.common.config;

import com.coinsensor.user.entity.User;
import com.coinsensor.user.filter.UserFilter;
import com.coinsensor.user.repository.UserRepository;
import jakarta.servlet.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

@Configuration
@RequiredArgsConstructor
public class FilterConfig {
    private final UserRepository userRepository;
    
    @Bean
    public FilterRegistrationBean<UserFilter> UserFilter() {
        FilterRegistrationBean<UserFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new UserFilter(userRepository));
        registrationBean.addUrlPatterns("/api/*", "/ws/*");
        registrationBean.setOrder(1);
        return registrationBean;
    }
}