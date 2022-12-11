package com.sgming.a06;

import com.sgming.a06.config.MyConfig1;
import com.sgming.a06.config.MyConfig2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.context.annotation.CommonAnnotationBeanPostProcessor;
import org.springframework.context.annotation.ConfigurationClassPostProcessor;
import org.springframework.context.support.GenericApplicationContext;

@Slf4j
public class A06Application {
    public static void main(String[] args) {
        /*
        1.Aware 接口用于注入一些于容器相关的信息，例如
            - BeanNameWare 注入 bean 的名字
            - BeanFactoryWare 注入 BeanFactory 容器
            - ApplicationContextAware 注入 ApplicationContext 容器
            - EmbeddedValueResolverAware ${}
         */
        GenericApplicationContext context = new GenericApplicationContext();
        context.registerBean("myConfig2", MyConfig2.class);
        /*
        2.上面说的功能用 @AutoWired 也可以实现，为什么还要用 Aware 接口
        简单来说：
            - @Autowired 的注解需要用到 BeanFactory 后处理器，属于扩展功能
            - 而 Aware 接口属于内置功能，不需要任何扩展，Spring 就能识别
        某些情况下，扩展功能会失效，但内置功能不会失效

        例1：你会发现用 Aware 接口注入 ApplicationContext 成功，而 @Autowired 注入 ApplicationContext 失败
         */
        //加入后处理器，就可以看到注入了
        context.registerBean(AutowiredAnnotationBeanPostProcessor.class);
        context.registerBean(CommonAnnotationBeanPostProcessor.class);
        //用于解析@Bean @ConfigurationScan...注解
        context.registerBean(ConfigurationClassPostProcessor.class);
        /*
        context.refresh() 执行的主要顺序
        1.执行 BeanFactory 后处理器，来补充 Bean 的定义
        2.添加 Bean 后处理器，添加扩展功能
        3.初始化单例
         */
        context.refresh();
        context.close();

        /*
        学到了：
            - Aware 接口提供了一种【内置】的注入手段，可以注入 BeanFactory， Application
            - InitializingBean 接口提供一种【内置】初始化手段
            - 内置的注入和初始化不受扩展功能影响，总会被执行，因此 Spring 矿建内部的类常用到它们
         */

    }
}
