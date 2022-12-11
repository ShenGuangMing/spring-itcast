package com.sgming.a12;
import com.sgming.a12.A12.InvocationHandler;
import com.sgming.a12.A12.Foo;

import java.lang.reflect.Method;
import java.lang.reflect.UndeclaredThrowableException;

public class $Proxy0 implements A12.Foo{
    private final InvocationHandler invocationHandler;
    private static final Method foo;
    private static final Method bar;
    static {
        try {
            foo = Foo.class.getDeclaredMethod("foo");
            bar = Foo.class.getDeclaredMethod("bar", Integer.class);
        } catch (NoSuchMethodException e) {
            //静态代码块，程序就不要运行了，所有抛出一个错误
            throw new NoSuchMethodError(e.getMessage());
        }
    }


    public $Proxy0(InvocationHandler invocationHandler) {
        this.invocationHandler = invocationHandler;
    }

    @Override
    public String foo() {
        Object result = null;
        try {
            //被代理的方法对象
            result = invocationHandler.invoke(this, foo, new Object[0]);
            return (String) result;
        } catch (RuntimeException | Error e) {
            throw e;
        } catch (Throwable e) {
            throw new UndeclaredThrowableException(e);
        }
    }

    @Override
    public Integer bar(Integer n) {
        Object result = null;
        try {
            //调用用户需要增强的代码，参数注意
            result = invocationHandler.invoke(this, bar, new Object[]{n});
            return (Integer) result;
        } catch (RuntimeException | Error e) {
            throw e;
        } catch (Throwable e) {
            throw new UndeclaredThrowableException(e);
        }

    }
}
