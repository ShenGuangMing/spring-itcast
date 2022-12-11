package com.sgming.a02;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigUtils;
import org.springframework.context.annotation.Bean;

import javax.annotation.Resource;

@Slf4j
public class TestBeanFactory {
    public static void main(String[] args) {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        /*
        我们需要告诉beanFactory，bean的定义，控制反转。
        bean的定义:(bean的描述信息)
            - class
            - scope
            - destroy method
            - init method
            ...
         */
        AbstractBeanDefinition beanDefinition = BeanDefinitionBuilder.genericBeanDefinition(Config.class)
                .setScope("singleton")//设置模式
                .getBeanDefinition();//构建出bean定义
        beanFactory.registerBeanDefinition("config", beanDefinition);//将bean的定义注册进容器中
        System.out.println("添加注册注解配置处理器前：");
        for (String name : beanFactory.getBeanDefinitionNames()) {//遍历容器中所有被定义的bean的名称
            System.out.println(name);
        }
        /*
        通过上面打印了只有config，说明我们的bean1和bean2并没有被注册进容器，也就是我们@Bean没有被解析，并且这个功能不是BeanFactory提供的
        需要我们给BeanFactory添加这些功能
         */
        //给 BeanFactory 添加一些注册注解配置处理器
        AnnotationConfigUtils.registerAnnotationConfigProcessors(beanFactory);
        System.out.println("\n添加一些注册注解配置处理器后：");
        for (String name : beanFactory.getBeanDefinitionNames()) {//遍历容器中所有被定义的bean的名称
            System.out.println(name);
        }
        //让添加的处理器中有关BeanFactory后处理器(BeanFactoryPostProcessor)工作
        System.out.println("\n添加了哪些BeanFactory后处理器工作:");
        beanFactory.getBeansOfType(BeanFactoryPostProcessor.class).values().forEach(beanFactoryPostProcessor -> {
            System.out.println("添加了：" + beanFactoryPostProcessor.getClass().getName() + " 后处理器");
            beanFactoryPostProcessor.postProcessBeanFactory(beanFactory);//让BeanFactory后处理器工作
        });
        System.out.println("\n添加注册注解配置处理器并让BeanFactory后处理器工作后：");
        for (String name : beanFactory.getBeanDefinitionNames()) {//遍历容器中所有被定义的bean的名称
            System.out.println(name);
        }
        //如果添加了Bean后处理一定要注释下面获取Bean1，因为Bean1是单例的，我们去获取它，它就会被实例且只有一份，添加Bean后处理后，就没有效果了，获取的仍是这个不完整的Bean1
        //System.out.println("容器获取bean1中的属性bean2:" + beanFactory.getBean(Bean1.class).getBean2());

        //Bean 后处理器，针对 Bean 的生命周期的各个阶段提供扩展，例如 @Autowired @Resource...
        //添加运行Bean后处理(BeanPostProcessor)
        System.out.println("\n添加了哪些Bean后处理器工作:");
//        beanFactory.getBeansOfType(BeanPostProcessor.class).values().forEach(beanPostProcessor -> {
//            System.out.println("添加了：" + beanPostProcessor.getClass().getName() + " 后处理器");
//            beanFactory.addBeanPostProcessor(beanPostProcessor);
//        });
        //Bean后处理器进行排序后
        beanFactory.getBeansOfType(BeanPostProcessor.class).values().stream()
                .sorted(beanFactory.getDependencyComparator())
                .forEach(beanPostProcessor -> {
                    System.out.println("添加了：" + beanPostProcessor.getClass().getName() + " 后处理器");
                    beanFactory.addBeanPostProcessor(beanPostProcessor);
                });

        System.out.println("\n添加Bean后处理器后：");
        beanFactory.preInstantiateSingletons();
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        System.out.println("容器获取bean1中的属性bean2:" + beanFactory.getBean(Bean1.class).getBean2());

        System.out.println("容器中Bean1的Inter的具体注入的是：" + beanFactory.getBean(Bean1.class).getBean3());
    }
    static class Config {
        @Bean
        public Bean1 bean1() {
            return new Bean1();
        }
        @Bean
        public Bean2 bean2() {
            return new Bean2();
        }
        @Bean
        public Bean3 bean3() {
            return new Bean3();
        }
        @Bean
        public Bean4 bean4() {
            return new Bean4();
        }
    }
    interface Inter {
    }
    static class Bean3 implements Inter {
        public Bean3() {
            log.info("构造 Bean3");
        }
    }
    static class Bean4 implements Inter {
        public Bean4() {
            log.info("构造 Bean4");
        }
    }
    static class Bean1 {
        public Bean1() {
            log.info("Bean1的构造方法");
        }
        @Autowired
        private Bean2 bean2;
        //Autowired：如果一个类型有多个Bean，Autowired会根据成员变量名进行匹配
        //Resource：如果一个类型有多个Bean，Resource会根据name属性的value进行匹配，如果name没有被赋值，则会按照成员变量名匹配
        @Autowired
        @Resource(name = "bean4")
        private Inter bean3;

        public Bean2 getBean2() {
            return bean2;
        }

        public Inter getBean3() {
            return bean3;
        }
    }
    static class Bean2 {
        public Bean2() {
            log.info("Bean2的构造方法");
        }
    }
}
