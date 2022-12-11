package com.sgming.a11;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;

public class CGLibProxyDemo {
    static class Target {
        public String foo() {
            System.out.println("target foo");
            return "foo";
        }
    }

    public static void main(String[] args) {
        Target target = new Target();
        Target p = (Target) Enhancer.create(Target.class, (MethodInterceptor) (proxy, method, args1, methodProxy) -> {
            System.out.println("before");
//            Object result = method.invoke(target, args1);
            // methodProxy内部是避免了反射的，需要目标，spring使用的方式
//            Object result = methodProxy.invoke(target, args1);
            //内部没有用反射，需要代理， 可以不需要Target对象
            Object result = methodProxy.invokeSuper(proxy, args1);
            System.out.println("after");
            return result.toString() + "-CGLibProxy";
        });
        System.out.println(p.foo());
        System.out.println("代理对象是否继承Target: " + (p instanceof Target));
    }
}
