package com.sgming.a01.event;

import org.springframework.context.ApplicationEvent;
public class UserRegisterEvent extends ApplicationEvent {
    //接收事件
    public UserRegisterEvent(Object source) {//事件源
        super(source);
    }
}
