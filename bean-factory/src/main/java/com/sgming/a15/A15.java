package com.sgming.a15;

import org.aopalliance.intercept.MethodInterceptor;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;

public class A15 {
    @Aspect
    static class MyAspect {
        @Before("execution(* foo())")
        public void before() {
            System.out.println("前置增强");
        }

        @After("execution(* foo())")
        public void after() {
            System.out.println("后置增强 ");
        }
    }

    public static void main(String[] args) {
        /*
            两个切面概念
            aspect =
                通知1(advice) + 切点1(pointcut)
                通知2(advice) + 切点2(pointcut)
                通知3(advice) + 切点3(pointcut)
                ...
            advisor = 更细粒度，包含一个通知和切点
         */
        //1.备好切点
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression("execution(* foo())");
        //2.备好通知
        MethodInterceptor advice = invocation -> {
            System.out.println("before...");
            Object result = invocation.proceed();
            System.out.println("after...");
            return result;
        };
        //3.备好切面
        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor(pointcut, advice);
        /*
            4.创建代理
                a. proxyTargetClass = false, 目标实现的接口，用jdk实现
                b. proxyTargetClass = false, 目标没有实现接口，用cglib实现
                b. proxyTargetClass = true, 总是使用cglib实现

         */
        Target1 target = new Target1();
//        Target2 target = new Target2();
        ProxyFactory factory = new ProxyFactory();
        factory.setTarget(target);
        //加入切面
        factory.addAdvisor(advisor);
        //设置实现的接口
        factory.setInterfaces(target.getClass().getInterfaces());
        factory.setProxyTargetClass(false);
//        I1 proxy = (I1) factory.getProxy();
        I1 proxy = (I1) factory.getProxy();
        System.out.println("代理类的类型：" + proxy.getClass());
        proxy.foo();
        proxy.bar();
        /*
            学到了什么：
                a. Spring  的代理选择规则
                b. 底层的切点实现
                c. 底层通知实现
                d. ProxyFactory 是用来创建代理的核心实现，用 AopProxyFactory 选择具体的代理实现
                    - JdkDynamicAopProxy
                    - ObjenesisCglibAopProxy
         */

    }
    interface I1 {
        void foo();
        void bar();
    }

    static class Target1 implements I1 {

        @Override
        public void foo() {
            System.out.println("target1 foo");
        }

        @Override
        public void bar() {
            System.out.println("target1 bar");
        }
    }
    static class Target2 {

        public void foo() {
            System.out.println("target1 foo");
        }

        public void bar() {
            System.out.println("target1 bar");
        }
    }
}
