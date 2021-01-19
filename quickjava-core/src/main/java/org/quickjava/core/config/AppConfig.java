package org.quickjava.core.config;

import lombok.Data;
import org.quickjava.core.bean.Dict;
import org.quickjava.core.utils.MapUtils;
import org.yaml.snakeyaml.Yaml;

import java.util.Map;

/**
 * configuration
 * @author Qlo1062-(QloPC-Msi)
 * @date 2021/1/17 20:52
 * @ProjectName quickjava
 */
@Data
public class AppConfig {

    private Type type = Type.YAML;

    public Boolean isJar = false;   // 是否打包模式

    public Boolean debug;

    public String domin;

    public String version;

    public String lang;

    public static Dict config = null;

    public enum Type {
        YAML,
    }

    public static class Factory {

        public static Dict loadFormYml(String yml) throws Exception {
            return loadFormYaml(yml);
        }

        public static Dict loadFormYaml(String content)
        {
            Yaml yaml = new Yaml();
            Map<String, Object> result = yaml.load(content);
            return config = new Dict(result);
        }
    }

    @Override
    public String toString() {
        return "AppConfig{" +
                "type=" + type +
                ", debug=" + debug +
                ", domin='" + domin + '\'' +
                ", version='" + version + '\'' +
                ", config=" + config +
                '}';
    }
}
