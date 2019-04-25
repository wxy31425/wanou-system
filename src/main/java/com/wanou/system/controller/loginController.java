package com.wanou.system.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class loginController {
    Logger log = LoggerFactory.getLogger(loginController.class);

    /*跳转首页*/
    @RequestMapping("toIndex")
    public String toIndex() {
         log.info("index");
         return "index";
    }
}
