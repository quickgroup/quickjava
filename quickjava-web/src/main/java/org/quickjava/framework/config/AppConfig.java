package org.quickjava.framework.config;

import org.quickjava.common.QuickLog;
import org.quickjava.common.QuickUtil;
import org.quickjava.common.utils.FileUtils;
import org.quickjava.framework.bean.Dict;
import org.yaml.snakeyaml.Yaml;

import java.util.Map;

/**
 * configuration
 * @author Qlo1062-(QloPC-Msi)
 * @date 2021/1/17 20:52
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
//            QuickLog.debug("dictDefault: " + dictDefault);
            // User config
            Map<String, Object> resultUser = yaml.load(content);
            Dict dictUser = new Dict(resultUser);
//            QuickLog.debug("dictUser: " + dictUser);
            // merger
            dictUser = Dict.putAll(dictDefault, dictUser);

            QuickLog.debug("dictUser: " + dictUser);

            return dictUser;
        }

        private static Dict loadDefaultConfig(Yaml yaml)
        {
            String configContent;
            String filename = "default.yml";

            if (QuickUtil.isClassMode()) {
                /**
                 * @langCn 多模块开发模式下，框架资源文件需要特殊读取
                 */
                String packageName = AppConfig.class.getPackage().getName();
                packageName = "/" + packageName.replaceAll("\\.", "/");
                String filePath = QuickUtil.getRootPath() + "/quickjava-web/target/classes" + packageName + "/" + filename;
                configContent = FileUtils.getFileContents(filePath);

            } else {
                configContent = FileUtils.getPackageFileContent(
                        AppConfig.class.getPackage().getName(), filename);
            }

            Map<String, Object> resultDefault = yaml.load(configContent);

            return new Dict(resultDefault);
        }
    }
}
