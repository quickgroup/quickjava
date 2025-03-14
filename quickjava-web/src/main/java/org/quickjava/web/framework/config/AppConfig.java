package org.quickjava.web.framework.config;

import org.quickjava.web.common.utils.FileUtils;
import org.quickjava.web.framework.bean.Dict;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * configuration
 * @author Qlo1062-(QloPC-Msi)
 * #date 2021/1/17 20:52
 * @ProjectName quickjava
 */
public class AppConfig {

    public Boolean isJar = false;   // 是否打包模式

    public Boolean debug = false;

    public String version = null;

    public String lang = null;

    public static class Factory {

        public static Dict loadFormYml(String yml) throws Exception {
            return loadFormYaml(yml);
        }

        public static Dict loadFormYaml(String content)
        {
            Yaml yaml = new Yaml();
            // Default config
            Dict dictDefault = loadDefaultConfig(yaml);
            // User config
            Map<String, Object> resultUser = yaml.load(content);
            Dict dictUser = new Dict(resultUser);
            // merger
            Dict ret = Dict.merge(dictDefault, dictUser);
            return ret;
        }

        /**
         * 加载默认配置文件
         */
        private static Dict loadDefaultConfig(Yaml yaml)
        {
            try (InputStream in = AppConfig.class.getResourceAsStream("/quickjava-web/config/application.yml")) {
                String content = FileUtils.getInputStreamContent(in);
                Map<String, Object> resultDefault = yaml.load(content);
                return new Dict(resultDefault);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
