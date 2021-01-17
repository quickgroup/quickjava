package org.quickjava.core.config;

import java.io.File;

/**
 * configuration
 * @author Qlo1062-(QloPC-Msi)
 * @date 2021/1/17 20:52
 * @ProjectName quickjava
 */
public class BaseConfig {

    private Type type = Type.YAML;

    private String path = null;

    private Boolean debug = false;

    private String version = null;

    public enum Type {
        YAML,
    }

    private BaseConfig() {
    }

    public static class Factory {

        public static BaseConfig loadFormYml(String yml) {
            return new BaseConfig();
        }
    }

}
