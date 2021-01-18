package org.quickjava.core;

import org.junit.Test;
import org.quickjava.core.bean.Module;
import org.quickjava.core.config.AppConfig;
import org.quickjava.core.utils.QFileUtils;
import org.quickjava.common.QUtils;

import java.io.File;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * @author QloPC-Msi
 * @date
 */
public class TestQuick {

    @Test
    public void test1()
    {
        try {
            String rootPath = QUtils.getRootPath();
            String appPackages = "org.demo.www.application";
            String packagePath = appPackages.replace(".", "/");

            System.out.println("appPackages => " + appPackages + ", packagePath => " + packagePath);

            ClassLoader loader = Thread.currentThread().getContextClassLoader();

            Enumeration<URL> enumeration = loader.getResources(packagePath);
            if (enumeration == null) {
                throw new ClassNotFoundException(appPackages + " package not found");
            }

            // 控制器列表
            Map<String, Module> moduleList = new HashMap<String, Module>();
            AppConfig baseConfig;

            if (enumeration.hasMoreElements()) {
                URL item = enumeration.nextElement();

                if (item.getProtocol().equals("file")) {
                    // 文件形式
                    File appFile = new File(item.getFile());
                    File[] moduleFiles = appFile.listFiles();
                    for (File file : moduleFiles) {
                        System.out.println("file=" + file);

                        if (file.isDirectory()) {
                            /**
                             * 目录统统视为模块
                             */
                            Module module = new Module(file.getName(), file.getAbsolutePath());
                            moduleList.put(module.getName(), module);

                        } else if (file.isFile()){
                            String fileName = file.getName();
                            System.out.println("fileName=" + fileName);
                            if (fileName.contains(".yml") || fileName.contains(".yaml")) {
                                /**
                                 * YAML 配置文件
                                 */
                                System.out.println("fileName=" + fileName);
                                if (fileName.equals("config.yml")) {
                                    baseConfig = AppConfig.Factory.loadFormYml("");
                                }

                            } else if (file.getName().contains(".xml")) {
                                /**
                                 * XML配置文件
                                 */

                            }
                        }
                    }

                    // 加载配置文件
                    URL configYmlUrl = loader.getResource("config.yml");
                    String configYmlContent = QFileUtils.getFileContents(configYmlUrl.getPath());
                    baseConfig = AppConfig.Factory.loadFormYml(configYmlContent);

                    System.out.println("baseConfig=" + baseConfig);

                } else if (item.getProtocol().equals("jar")) {
                    // jar包

                }

                // 打印
                System.out.println("moduleList=" + moduleList.toString());
            }


        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void test2()
    {
        int min = -10;
        int max = 3;
        int num = (int) Math.floor(Math.random() * (max - min) + min);

        System.out.println("num=" + num);
    }

    @Test
    public void test3()
    {
        try {
            URL configYmlUrl = Thread.currentThread().getContextClassLoader().getResource("config.yml");
            String configYmlContent = QFileUtils.getFileContents(configYmlUrl.getPath());
            AppConfig.Factory.loadFormYml(configYmlContent);

        } catch (Exception exc) {
            exc.printStackTrace();
        }

    }
}
