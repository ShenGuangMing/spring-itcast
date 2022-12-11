package com.sgming.a11;

import java.lang.reflect.Proxy;

public class JdkProxyDemo {
    interface Foo {
        String foo();
    }
    //它可以是final，因为代理对象和它都实现了Foo接口，他们是兄弟关系
    static final class Target implements Foo {

        @Override
        public String foo() {
            System.out.println("target foo");
            return "foo";
        }
    }
    public static void main(String[] args) {
        ClassLoader loader = JdkProxyDemo.class.getClassLoader();//运行期间，动态生成字节码文件，我们这里需要它生成代理类的字节码文件
        Target target = new Target();
        /*
        new Class[]{Foo.class}:实现的接口有哪些
        (proxy, method, args1) -> {}:代理方法实现了接口后的具体行为
            - proxy:代理类
            - method:正在执行的方法
            - args:正在执行的方法的参数
         */
        Foo p = (Foo) Proxy.newProxyInstance(loader, new Class[]{Foo.class}, (proxy, method, args1) -> {
            System.out.println("before...");
            /*
            method.invoke(target, args1):执行被代理类的方法
                - target：被代理对象
                - args1：参数
             */
            Object result = method.invoke(target, args1);
            System.out.println("after...");
            return result.toString() + "-JDKProxy";
        });
        System.out.println(p.foo());
        System.out.println("代理对象是否继承Target: " + (p instanceof Target));
    }
}
