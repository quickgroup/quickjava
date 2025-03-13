package org.quickjava.web.framework.config;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Qlo1062-(QloPC-Msi)
 * @date 2021/1/17 20:52
 * @ProjectName quickjava
 */
public class RouteConfig {

    private String type = "default";

    private Map<String, String> list = new LinkedHashMap<>();

    @Override
    public String toString() {
        return "RouteConfig{" +
                "type='" + type + '\'' +
                ", list=" + list +
                '}';
    }
}
