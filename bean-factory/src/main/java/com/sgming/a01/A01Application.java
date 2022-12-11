package com.sgming.a01;

import com.sgming.a01.component.UserController;
import com.sgming.a01.event.UserRegisterEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.DefaultSingletonBeanRegistry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.Resource;

import java.lang.reflect.Field;
import java.util.Locale;
import java.util.Map;

@SpringBootApplication
@Slf4j
public class A01Application {
    public static void main(String[] args) throws Exception {
        /*
        我们经常写这样的引导类，但这个run方法是有返回值的ConfigurableApplicationContext即可配置的Application
        BeanFactory是什么
            - Application的父接口
            - 它在是spring的核心容器，主要的ApplicationContext的实现都【组合】了它的功能
         */
        ConfigurableApplicationContext context = SpringApplication.run(A01Application.class, args);
        /*
        BeanFactory能干什么
            - 表面只是getBean
            - 实际上控制反转、基本的依赖注入、直至Bean的生命周期的各个功能，都由它的实现类提供，也就是：`DefaultListableBeanFactory`
         */
        Field singletonObjects = DefaultSingletonBeanRegistry.class.getDeclaredField("singletonObjects");
        singletonObjects.setAccessible(true);//因为是私有属性
        //因为singletonObjects属性是DefaultListableBeanFactory所有的，所以需要获取BeanFactory
        ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();
        Map<String, Object> map = (Map<String, Object>) singletonObjects.get(beanFactory);
        map.entrySet().stream() //转化为流
                .filter(e -> e.getKey().startsWith("component"))//进行过滤，必须是component开头
                .forEach(e -> {//遍历
                    System.out.println(e.getKey() + "=" + e.getValue());
                });

        /*
        Application 比 BeanFactory多点啥
         */
        //getMessage()，处理国际化能力
        System.out.println("\n======================getMessage()，处理国际化能力======================");
        System.out.println("中文：" + context.getMessage("hi", null, Locale.CHINA));
        System.out.println("英文：" + context.getMessage("hi", null, Locale.ENGLISH));
        System.out.println("日文：" + context.getMessage("hi", null, Locale.JAPAN));
        //getResources()，根据通配符获取多个资源
        System.out.println("\n===================getResources()，根据通配符获取多个资源================");
        Resource[] resources = context.getResources("classpath*:META-INF/spring.factories");
        for (Resource resource : resources) {
            System.out.println(resource);
        }
        //getEnvironment()，获取环境配置信息
        System.out.println("\n====================getEnvironment()，获取环境配置信息==================");
        System.out.println(context.getEnvironment().getProperty("java_home"));//不区分大小写
        System.out.println(context.getEnvironment().getProperty("server.port"));//不区分大小写

        //publicEvent()，发布事件
        System.out.println("\n=========================publicEvent()，发布事件=======================");
        //发布事件
        //context.publishEvent(new UserRegisterEvent(context));//事件源是context
        context.getBean(UserController.class).register();//用户注册事件发布
    }
}
