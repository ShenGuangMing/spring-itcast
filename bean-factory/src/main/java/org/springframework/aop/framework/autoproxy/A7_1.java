package org.springframework.aop.framework.autoproxy;

import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.aspectj.annotation.AnnotationAwareAspectJAutoProxyCreator;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.beans.factory.config.BeanDefinitionCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.CommonAnnotationBeanPostProcessor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ConfigurationClassPostProcessor;
import org.springframework.context.support.GenericApplicationContext;

import javax.annotation.PostConstruct;

public class A7_1 {
    public static void main(String[] args) {
        GenericApplicationContext context = new GenericApplicationContext();
        context.registerBean(ConfigurationClassPostProcessor.class);
        context.registerBean(Config.class);
        context.refresh();
        context.close();
        /*
            创建代理的时间会在下面的两个*位置
            创建 -> (*) 依赖注入 -> 初始化 (*)
            当然不会重复创建，只会在上面两个中其中一种情况创建

            学会了：
                代理创建的时机
                    - 初始化之后（没有循环依赖的情况下）
                    - 依赖注入之前（有循环依赖的情况下）并暂存在二级缓存
                依赖注入和初始化不应该被增强，仍应被施加原始对象
                > 意思就是在Bean没有被初始化之前，调用的set方法应该是原始对象的
         */
    }

    @Configuration
    static class Config {
        @Bean //解析@Aspect，产生代理
        public AnnotationAwareAspectJAutoProxyCreator annotationAwareAspectJAutoProxyCreator() {
            return new AnnotationAwareAspectJAutoProxyCreator();
        }
        @Bean //解析@Autowired
        public AutowiredAnnotationBeanPostProcessor autowiredAnnotationBeanPostProcessor() {
            return new AutowiredAnnotationBeanPostProcessor();
        }
        @Bean //解析@PostConstruct
        public CommonAnnotationBeanPostProcessor commonAnnotationBeanPostProcessor() {
            return new CommonAnnotationBeanPostProcessor();
        }

        @Bean
        public Advisor advisor(MethodInterceptor advice) {
            AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
            pointcut.setExpression("execution(* foo())");
            return new DefaultPointcutAdvisor(pointcut, advice);
        }
        @Bean
        public MethodInterceptor advice() {
            return invocation -> {
                System.out.println("advice before");
                Object result = invocation.proceed();
                System.out.println("advice after");
                return result;
            };
        }
        @Bean
        public Bean1 bean1() {
            return new Bean1();
        }
        @Bean
        public Bean2 bean2() {
            return new Bean2();
        }
    }
    static class Bean1 {
        public void foo() {
        }
        //加入这一部分的就会有循环依赖，这里的Bean2一定是一个代理的Bean2，不然就会抛出异常
        //会导致Bean1的代理类会提前创建就是，依赖注入之前
        @Autowired
        public void setBean2(Bean2 bean2) {
            System.out.println("Bean1 setBean2(bean2) class is: " + bean2.getClass());
        }
        public Bean1() {
            System.out.println("Bean1()");
        }

        @PostConstruct
        public void init() {
            System.out.println("Bean1() init");
        }
    }
    static class Bean2 {
        public Bean2() {
            System.out.println("Bean2()");
        }
        @Autowired
        public void setBean1(Bean1 bean1) {
            System.out.println("Bean2 setBean1(bean1) class is: " + bean1.getClass());
        }
        @PostConstruct
        public void init() {
            System.out.println("Bean2 init()");
        }
    }
}
