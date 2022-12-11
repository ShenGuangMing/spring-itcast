package com.sgming.a14;

public class A14 {
    public static void main(String[] params) {
        Target target = new Target();
        Proxy proxy = new Proxy();
        proxy.setMethodInterceptor((o, method, args, methodProxy) -> {
            System.out.println("before");
//            return method.invoke(target, args);//反射调用
//            return methodProxy.invoke(target, args);//非反射调用，但是使用了目标对象
            return methodProxy.invokeSuper(o, args);//非凡调用，没有使用目标对象
        });
        proxy.save();
        System.out.println();
        proxy.save(1);
        System.out.println();
        proxy.save(2L);
    }
}
