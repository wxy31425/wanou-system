package com.wanou.system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class mapController {
    /**
     *  地图跳转页面
     * @return
     */
    @RequestMapping("toMap")
    public String toMap() {
        return "map/map_list";
    }
}
