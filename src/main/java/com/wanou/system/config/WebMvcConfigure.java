package com.wanou.system.config;

import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.EventListener;

@Configuration
public class WebMvcConfigure extends WebMvcConfigurerAdapter {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/index").setViewName("index");
        registry.addViewController("/").setViewName("index");

    }

    @Bean
    public ServletListenerRegistrationBean<EventListener> getDemoListener() {
        ServletListenerRegistrationBean<EventListener> registrationBean
                = new ServletListenerRegistrationBean<>();
        registrationBean.setListener(new listenerConfigure());
        return registrationBean;
    }
}
