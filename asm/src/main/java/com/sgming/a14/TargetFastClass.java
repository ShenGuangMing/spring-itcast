package com.sgming.a14;

import org.springframework.cglib.core.Signature;

//FastClass对象在MethodProxy内部
//这是和目标对象配合使用的FastClass，methodProxy.invoke(target, args);
public class TargetFastClass {

    /*
    方法作用：通过前面获取目标方法的编号
    Signature signature：签名
    Target中的方法与编号对应
            save()      0
            save(int)   1
            save(long)  2
    signature包括：方法的名字，参数名，返回值

    每个MethodProxy对象被创建，就会生成FastClass，所以知道自己的方法名，参数，返回值，我们这里直接生成好了的
     */
    static Signature s0 = new Signature("save", "()V");
    static Signature s1 = new Signature("save", "(I)V");
    static Signature s2 = new Signature("save", "(J)V");
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
    public Object invoke(int index, Object target, Object[] args) {
        Target t = (Target) target;
        if (index == 0) {
            t.save();
            return null;
        } else if (index == 1) {
            t.save((Integer) args[0]);
        } else if (index == 2) {
            t.save((Long) args[0]);
        } else {
            throw new RuntimeException("没有找到对应的方法");
        }
        return null;
    }

    public static void main(String[] args) {
        TargetFastClass fastClass = new TargetFastClass();
        int index = fastClass.getIndex(new Signature("save", "(I)V"));
        fastClass.invoke(index, new Target(), new Object[]{10});
    }

}
