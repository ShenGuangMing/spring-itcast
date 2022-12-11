package com.sgming.a14;

import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.cglib.proxy.UndeclaredThrowableException;

import java.lang.reflect.Method;

public class Proxy extends Target{

    private MethodInterceptor methodInterceptor;

    private static final Method save0;
    private static final Method save1;
    private static final Method save2;
    private static final MethodProxy save0Proxy;
    private static final MethodProxy save1Proxy;
    private static final MethodProxy save2Proxy;

    static {
        try {
            save0 = Target.class.getMethod("save");
            save1 = Target.class.getMethod("save", int.class);
            save2 = Target.class.getMethod("save", long.class);
            /*
            Class c1：目标类型
            Class c2：代理类型
            String desc：参数和返回值
            String name1：
            String name2：
             */
            save0Proxy = MethodProxy.create(Target.class, Proxy.class, "()V", "save", "saveSuper");
            save1Proxy = MethodProxy.create(Target.class, Proxy.class, "(I)V", "save", "saveSuper");
            save2Proxy = MethodProxy.create(Target.class, Proxy.class, "(J)V", "save", "saveSuper");
        } catch (NoSuchMethodException e) {
            //为什么抛出错误，因为静态代码块是随着类加载而加载，如果上面代码出错，上面类就存在问题，所以是一个错误
            throw new NoSuchMethodError(e.getMessage());
        }
    }

    public void setMethodInterceptor(MethodInterceptor methodInterceptor) {
        this.methodInterceptor = methodInterceptor;
    }
    //原始功能的方法
    public void saveSuper() {
        super.save();
    }
    public void saveSuper(int i) {
        super.save(i);
    }
    public void saveSuper(long l) {
        super.save(l);
    }


    //下面的方法都是带增强功能的方法
    @Override
    public void save() {
        try {
            methodInterceptor.intercept(this, save0, new Object[0], save0Proxy);
        } catch (Throwable e) {
            throw new UndeclaredThrowableException(e);
        }
    }

    @Override
    public void save(int i) {
        try {
            methodInterceptor.intercept(this, save1, new Object[]{i}, save1Proxy);
        } catch (Throwable e) {
            throw new UndeclaredThrowableException(e);
        }
    }

    @Override
    public void save(long l) {
        try {
            methodInterceptor.intercept(this, save2, new Object[]{l}, save2Proxy);
        } catch (Throwable e) {
            throw new UndeclaredThrowableException(e);
        }
    }
}
