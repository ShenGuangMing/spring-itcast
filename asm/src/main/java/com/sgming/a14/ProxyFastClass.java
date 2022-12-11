package com.sgming.a14;

import org.springframework.cglib.core.Signature;
public class ProxyFastClass {
    //获取代理类中，带原始方法的编号
    /*
        Proxy:
            saveSuper()     0
            saveSuper(int)  1
            saveSuper(long) 2
        这里的方法必须是带原始功能的方法，如果使用增强方法，就会陷入死循环
     */
    static Signature s0 = new Signature("saveSuper", "()V");
    static Signature s1 = new Signature("saveSuper", "(I)V");
    static Signature s2 = new Signature("saveSuper", "(J)V");
    public int getIndex(Signature signature) {
        if (s0.equals(signature)) {
            return 0;
        } else if (s1.equals(signature)) {
            return 1;
        } else if (s2.equals(signature)) {
            return 2;
        }
        //没有匹配到就返回-1
        return -1;
    }

    //根据方法编号，正常调用目标对象的方法
    public Object invoke(int index, Object proxy, Object[] args) {
        Proxy t = (Proxy) proxy;
        if (index == 0) {
            t.saveSuper();
            return null;
        } else if (index == 1) {
            t.saveSuper((Integer) args[0]);
        } else if (index == 2) {
            t.saveSuper((Long) args[0]);
        } else {
            throw new RuntimeException("没有找到对应的方法");
        }
        return null;
    }

    public static void main(String[] args) {
        ProxyFastClass fastClass = new ProxyFastClass();
        int index = fastClass.getIndex(new Signature("saveSuper", "()V"));
        fastClass.invoke(index, new Proxy(), new Object[0]);
    }

}
