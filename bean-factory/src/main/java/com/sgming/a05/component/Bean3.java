package com.sgming.a05.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

@Controller
@Slf4j
public class Bean3 {
    public Bean3() {
        log.info(" =========== Bean3 被Spring管理了");
    }
}
