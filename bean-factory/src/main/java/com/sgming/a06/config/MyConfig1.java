package com.sgming.a06.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
@Slf4j
public class MyConfig1 {
    @Autowired
    public void setApplicationContext(ApplicationContext context) {
        log.info("注入 ApplicationContext");
    }
    @PostConstruct
    public void init() {
        log.info("初始化");
    }
    //添加一个 BeanFactory 后处理器
    @Bean
    public BeanFactoryPostProcessor processor1() {
        return beanFactory -> {
            log.info("执行 processor1");
        };
    }
}
