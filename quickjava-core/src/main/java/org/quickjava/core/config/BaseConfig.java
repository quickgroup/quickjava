package org.quickjava.core.config;

import org.yaml.snakeyaml.Yaml;

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
            return loadFormYaml(yml);
        }

        public static BaseConfig loadFormYaml(String content) {
            Yaml yaml = new Yaml();
            Object object = yaml.load(content);
            System.out.println("object=" + object);
            return new BaseConfig();
        }
    }

}
