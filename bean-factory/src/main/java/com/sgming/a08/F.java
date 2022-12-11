package com.sgming.a08;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class F {
    @Autowired
    private E e;

    @Autowired
    private ObjectFactory<E> e1;

    public E getE() {
        return e;
    }

    public void setE(E e) {
        this.e = e;
    }

    public E getE1() {
        return e1.getObject();
    }
}
