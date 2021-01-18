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

    private DatabaseConfig database = null;

    private RouteConfig route = null;

    public enum Type {
        YAML,
    }

    public static class Factory {

        public static AppConfig loadFormYml(String yml) throws Exception {
            return loadFormYaml(yml);
        }

        public static AppConfig loadFormYaml(String content)
            throws Exception
        {
            Yaml yaml = new Yaml();
            Map<String, Object> result = yaml.load(content);

            Dict dict = new Dict(result);
            System.out.println("dict:" + dict);
            System.out.println("dict:" + dict.get("app").getBoolean("debug"));

            if (true) return null;

            AppConfig appConfig = null;
            for (Map.Entry<String, Object> entry : result.entrySet()) {
                String idx_1_key = entry.getKey();
                Map<String, Object> appData = (Map<String, Object>) entry.getValue();
                if (idx_1_key.equals("app")) {
                    appConfig = (AppConfig) MapUtils.map2Bean(AppConfig.class, appData);

                } else if (idx_1_key.equals("database")) {
                    appConfig.database = (DatabaseConfig) MapUtils.map2Bean(DatabaseConfig.class, appData);

                } else if (idx_1_key.equals("route")) {
                    appConfig.route = (RouteConfig) MapUtils.map2Bean(RouteConfig.class, appData);

                }
            }

            return appConfig;
        }
    }

    @Override
    public String toString() {
        return "AppConfig{" +
                "type=" + type +
                ", debug=" + debug +
                ", domin='" + domin + '\'' +
                ", version='" + version + '\'' +
                ", database=" + database +
                ", route=" + route +
                '}';
    }
}
