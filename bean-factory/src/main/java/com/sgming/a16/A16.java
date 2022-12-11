package com.sgming.a16;

import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.StaticMethodMatcherPointcut;
import org.springframework.core.annotation.MergedAnnotation;
import org.springframework.core.annotation.MergedAnnotations;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Method;

public class A16 {
    public static void main(String[] args) throws NoSuchMethodException {
        AspectJExpressionPointcut pt1 = new AspectJExpressionPointcut();
        pt1.setExpression("execution(* bar())");
        //按照方法匹配我们的execution表达式
        boolean fooIsMatch = pt1.matches(T1.class.getMethod("foo"), T1.class);
        boolean barIsMatch = pt1.matches(T1.class.getMethod("bar"), T1.class);
        System.out.println("pt1 T1.foo方法是否匹配: " + fooIsMatch);
        System.out.println("pt1 T1.bar方法是否匹配: " + barIsMatch);

        AspectJExpressionPointcut pt2 = new AspectJExpressionPointcut();
        //按照方法注解是否有对应的注解匹配
        pt2.setExpression("@annotation(org.springframework.transaction.annotation.Transactional)");
        fooIsMatch = pt2.matches(T1.class.getMethod("foo"), T1.class);
        barIsMatch = pt2.matches(T1.class.getMethod("bar"), T1.class);
        System.out.println("pt2 T1.foo方法是否匹配: " + fooIsMatch);
        System.out.println("pt2 T1.bar方法是否匹配: " + barIsMatch);
        /*
            那@Transactional的底层就是我们上面这样的吗？
            不是，因为@Transactional可以加到类、方法、接口上，都可以达到事务增强的效果
         */

        StaticMethodMatcherPointcut pt3 = new StaticMethodMatcherPointcut() {
            @Override
            public boolean matches(Method method, Class<?> targetClass) {
                //检查方法上加了对应的注解
                MergedAnnotations annotations = MergedAnnotations.from(method);
                if (annotations.isPresent(Transactional.class)) {
                    return true;
                }
                //类上是否右对应的注解 from只会查找本类上的注解, TYPE_HIERARCHY设置递归查询
                annotations  = MergedAnnotations.from(targetClass, MergedAnnotations.SearchStrategy.TYPE_HIERARCHY);
                if (annotations.isPresent(Transactional.class)) {
                    return true;
                }
                return false;
            }
        };
        boolean b1 = pt3.matches(T1.class.getMethod("foo"), T1.class);
        boolean b2 = pt3.matches(T1.class.getMethod("bar"), T1.class);
        System.out.println("pt3 T1.foo方法是否匹配: " + b1);
        System.out.println("pt3 T1.bar方法是否匹配: " + b2);

        boolean b3 = pt3.matches(T2.class.getMethod("foo"), T2.class);
        System.out.println("pt3 T2.foo方法是否匹配: " + b3);
        boolean b4 = pt3.matches(T3.class.getMethod("foo"), T3.class);
        System.out.println("pt3 T3.foo方法是否匹配: " + b4);
    }
    static class T1 {
        @Transactional
        public void foo() {
        }
        public void bar() {
        }
    }
    @Transactional
    static class T2 {
        public void foo() {
        }
    }
    @Transactional
    interface I3 {
        void foo();
    }
    static class T3 implements I3 {
        @Override
        public void foo() {

        }
    }
}
