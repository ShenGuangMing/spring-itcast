package com.sgming.a08;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class A08Application {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(A08Application.class, args);
        F bean = context.getBean(F.class);
        System.out.println(bean.getE());
        System.out.println("第一个：" + bean.getE().getClass());
        System.out.println(bean.getE());
        System.out.println("第二个：" + bean.getE().getClass());
        System.out.println(bean.getE());
        System.out.println("第三个：" + bean.getE().getClass());
        System.out.println("--------------------------------------");
        System.out.println(bean.getE1());
        System.out.println("第一个：" + bean.getE1().getClass());
        System.out.println(bean.getE1());
        System.out.println("第二个：" + bean.getE1().getClass());
        System.out.println(bean.getE1());
        System.out.println("第三个：" + bean.getE1().getClass());
    }
}
