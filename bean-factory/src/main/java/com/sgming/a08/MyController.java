package com.sgming.a08;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Slf4j
public class MyController {

    @Autowired
    @Lazy
    private BeanForApplication beanForApplication;
    @Lazy
    @Autowired
    private BeanForRequest beanForRequest;

    @Lazy
    @Autowired
    private BeanForSession beanForSession;

    /*
    运行时可能会抛出，非法访问异常，这个是因为我们加了@Lazy，其实他注入的是一些代理对象
    ，我们打印对象其实调的就是代理对象的toString方法，而toString被代理对象调用后，会调用Object的同String方法、
    ，反射代理JDK的类，就可以能会抛出非法访问异常

    解决：
        - 运行添加（JVM）参数：--add-options java.base/java.long=ALL-UNNAMED
        - 重写同String方法
     */
    @GetMapping("/test")
    public String index() {
        log.info("beanForRequest: {}", beanForRequest);
        log.info("beanForRequest的代理类: {}", beanForRequest.getClass());
        log.info("beanForSession: {}", beanForSession);
        log.info("beanForSession的代理类: {}", beanForRequest.getClass());
        log.info("beanForApplication: {}", beanForApplication);
        log.info("beanForApplication的代理类: {}", beanForRequest.getClass());
        return "beanForRequest:" + beanForRequest + "<br>beanForSession" + beanForSession + "<br>beanForApplication" + beanForApplication;
    }

}
