package com.sgming.a12;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class A12 {
    interface Foo {
        String foo();
        Integer bar(Integer n);
    }
    interface InvocationHandler {
        Object invoke(Object proxy, Method method, Object[] args);
    }
    static class Target implements Foo{
        @Override
        public String foo() {
            System.out.println("target foo");
            return "target";
        }
        @Override
        public Integer bar(Integer n) {
            System.out.println("target bar");
            return n * 10;
        }
    }
    public static void main(String[] params) {
        Foo p = new $Proxy0((proxy, method, args) -> {
            //1.功能的增强
            System.out.println("before...");
            Object invoke = null;
            try {
                //2.调用目标方法返回值
                invoke = method.invoke(new Target(), args);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            }
            System.out.println("after");
            return invoke;
        });
        //先调用被代理类的中的对应的方法
        System.out.println("p.foo(): " + p.foo());
        System.out.println("p.p.bar(10): " + p.bar(10));
    }
}
