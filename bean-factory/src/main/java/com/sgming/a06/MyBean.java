package com.sgming.a06;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;

import javax.annotation.PostConstruct;

@Slf4j
public class MyBean implements BeanNameAware, ApplicationContextAware, InitializingBean {
    @Override
    public void setBeanName(String name) {
        log.info("当前Bean {} 的名字是：{}",this, name);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        log.info("使用Aware接口，当前Bean {} 的容器是：{}", this, applicationContext);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("当前Bean {} 初始化", this);
    }
    @Autowired
    public void testApplicationContext(ApplicationContext applicationContext) {
        log.info("使用 @Autowired 注解，当前Bean {} 的容器是：{}", this, applicationContext);
    }

    @PostConstruct
    public void init() {
        log.info("当前Bean {} 使用 @PostConstruct ", this);
    }
}
