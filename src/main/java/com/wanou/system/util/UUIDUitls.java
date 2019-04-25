package com.wanou.system.util;

import java.util.UUID;

public class UUIDUitls {
    public static String getShortUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
