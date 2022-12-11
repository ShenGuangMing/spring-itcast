package com.sgming.a09;

import com.sgming.a09.service.MyService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class A09Application {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(A09Application.class, args);
        MyService service = context.getBean(MyService.class);
        System.out.println("service class: " + service.getClass());
        service.service();
//        new MyService().service();
    }
}
