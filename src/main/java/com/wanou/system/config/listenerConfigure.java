package com.wanou.system.config;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class listenerConfigure implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("==>Listener启动");

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
