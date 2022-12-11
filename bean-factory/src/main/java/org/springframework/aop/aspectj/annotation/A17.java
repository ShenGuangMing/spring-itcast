package org.springframework.aop.aspectj.annotation;

import org.aopalliance.intercept.MethodInterceptor;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ConfigurationClassPostProcessor;
import org.springframework.context.support.GenericApplicationContext;

public class A17 {
    public static void main(String[] args) {
        //一个干净的容器，没有添加后处理器
        GenericApplicationContext context = new GenericApplicationContext();
        //注册配置类
        context.registerBean("a17Aspect1", A17Aspect1.class);
        context.registerBean("a17Config", A17Config.class);
        //ConfigurationClassPostProcessor用于解析@Configuration注解，和@Bean注解
        context.registerBean(ConfigurationClassPostProcessor.class);
        /*
            注册Annotation Aware Aspect J 自动代理创建器
            在Bean的生命周期他会根据高级切面和低级切面，自动创建代理
            它实现了BeanPostProcessor接口
                生命周期：创建 -> (在依赖注入之前对调用它的功能创建代理) 依赖注入 -> 初始化  (在初始化之后对调用它的功能创建代理)
         */
        context.registerBean(AnnotationAwareAspectJAutoProxyCreator.class);
        context.refresh();
        for (String name : context.getBeanDefinitionNames()) {
            System.out.println(name);
        }
        /*
            第一个重要的方法findEligibleAdvisors找到有【资格】的Advisors
                - 有【资格】的advisor一部分是低级的，可以由自己编写，如下例的advisor3

            因为方法的权限是受保护的，所以请在org.springframework.aop.aspectj.annotation下查看
         */
        AnnotationAwareAspectJAutoProxyCreator creator = context.getBean(AnnotationAwareAspectJAutoProxyCreator.class);
    }

    static class A17Target1 {
        public void foo() {
            System.out.println("A17Target1 foo");
        }
    }
    static class A17Target2 {
        public void bar() {
            System.out.println("A17Target2 bar");
        }
    }

    //高级的切面类，这里说的高级是指使用的角度
    @Aspect
    static class A17Aspect1 {
        @Before("execution(* foo())")
        public void before() {
            System.out.println("Aspect1 before");
        }
        @After("execution(* foo())")
        public void after() {
            System.out.println("Aspect2 after");
        }
    }
    static class A17Config {
        //低级的切面advisor3
        @Bean
        public Advisor advisor3(MethodInterceptor advice3) {
            AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
            pointcut.setExpression("execution(* foo())");
            return new DefaultPointcutAdvisor(pointcut, advice3);
        }
        //环绕通知advice3
        @Bean
        public MethodInterceptor advice3() {
            return invocation -> {
                System.out.println("advice3 before");
                //执行被增强的原方法
                Object result = invocation.proceed();
                System.out.println("advice3 after");
                return result;
            };
        }
    }
}
