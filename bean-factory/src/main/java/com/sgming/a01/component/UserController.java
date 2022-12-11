package com.sgming.a01.component;

import com.sgming.a01.event.UserRegisterEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UserController {
    //事件发布器
    @Autowired
    private ApplicationEventPublisher publisher;

    public void register() {
        log.info("用户注册");
        //发布事件：可以是邮箱，可以是短信， 发布源是User
        publisher.publishEvent(new UserRegisterEvent(this));
    }
}
