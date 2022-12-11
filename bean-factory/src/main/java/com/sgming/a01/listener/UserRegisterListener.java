package com.sgming.a01.listener;

import com.sgming.a01.event.UserRegisterEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UserRegisterListener {

    @EventListener
    public void userRegister(UserRegisterEvent event) {
        log.info("event: {}", event);
        log.info("发短信#");
    }
}
