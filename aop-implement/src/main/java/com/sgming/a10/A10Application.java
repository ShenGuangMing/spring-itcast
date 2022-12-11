package com.sgming.a10;

import com.sgming.a10.service.MyService;
import org.springframework.boot.autoconfigure.SpringBootApplication;

public class A10Application {
    public static void main(String[] args) {
        new MyService().foo();
    }
}
