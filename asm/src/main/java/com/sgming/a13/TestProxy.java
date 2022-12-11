package com.sgming.a13;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/*
JDK底层是通过生成 $Proxy0Dump 来生成代理类的字节码文件，也就是说，代理类不会有.java文件，而是将代理类的字节码文件生成到内存（虚拟机中）
 */
public class TestProxy {
    public static void main(String[] args) throws Exception {
        byte[] dump = $Proxy0Dump.dump();
        //生成class文件
//        FileOutputStream os = new FileOutputStream("$Proxy0.class");
//        os.write(dump, 0, dump.length);
//        os.close();
        //将对应的class文件加载
        ClassLoader loader = new ClassLoader() {
            @Override
            protected Class<?> findClass(String name) throws ClassNotFoundException {
                return super.defineClass(name, dump, 0, dump.length);
            }
        };
        Class<?> proxyClass = loader.loadClass("com.sgming.$Proxy0");

        Constructor<?> constructor = proxyClass.getConstructor(InvocationHandler.class);
        Foo proxy = (Foo) constructor.newInstance(new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                System.out.println("before...");
                System.out.println("调用目标");
                return null;
            }
        });
        proxy.foo();
    }
}
