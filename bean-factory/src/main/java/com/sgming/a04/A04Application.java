package com.sgming.a04;

import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.boot.context.properties.ConfigurationPropertiesBindingPostProcessor;
import org.springframework.context.annotation.CommonAnnotationBeanPostProcessor;
import org.springframework.context.annotation.ContextAnnotationAutowireCandidateResolver;
import org.springframework.context.support.GenericApplicationContext;

public class A04Application {
    public static void main(String[] args) {
        // GenericApplicationContext 是一个【干净】的容器，没有添加 BeanFactory 后处理器和 Bean 后处理器
        GenericApplicationContext context = new GenericApplicationContext();

        // 给容器注册四个Bean
        context.registerBean("bean1", Bean1.class);
        context.registerBean("bean2", Bean2.class);
        context.registerBean("bean3", Bean3.class);
        context.registerBean("bean4", Bean4.class);

        //添加解析 @Autowired @Value 注解的后处理器
        context.registerBean(AutowiredAnnotationBeanPostProcessor.class);
        //设置 Autowired 候选解析器，它默认的解析器不能解析 值注入（@Value），所有使用 ContextAnnotationAutowireCandidateResolver解析器
        context.getDefaultListableBeanFactory().setAutowireCandidateResolver(new ContextAnnotationAutowireCandidateResolver());

        //添加解析 javax.annotation 包下的注解
        context.registerBean(CommonAnnotationBeanPostProcessor.class);

        //添加 ConfigurationPropertiesBindingPostProcessor 处理器，解析：ConfigurationProperties注解
        ConfigurationPropertiesBindingPostProcessor.register(context.getDefaultListableBeanFactory());

        // 初始化容器
        context.refresh(); // // 执行添加的 BeanFactory 后处理器， 添加的 Bean 后处理器，初始化所有的单例

        //获取Bean4的属性
        System.out.println(context.getBean(Bean4.class));


        //销毁容器
        context.close();
    }
}
