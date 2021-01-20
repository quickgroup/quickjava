package org.demo.www;

import org.demo.www.index.model.UserModel;
import org.junit.Test;
import org.quickjava.common.QLog;
import org.quickjava.framework.controller.Module;
import org.quickjava.framework.config.AppConfig;
import org.quickjava.framework.http.Pathinfo;
import org.quickjava.framework.utils.QFileUtils;
import org.quickjava.common.QUtils;

import java.io.File;
import java.net.URL;
import java.util.*;

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

                        }
                    }

                    // 加载配置文件
                    URL configYmlUrl = loader.getResource("config.yml");
                    String configYmlContent = QFileUtils.getFileContents(configYmlUrl.getPath());
                    AppConfig.Factory.loadFormYml(configYmlContent);

                    System.out.println("config=" + AppConfig.config);

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

    @Test
    public void test4()
    {
//        String path = "/asd/qwe/zxc?1231231=asd/asdd";
        String path = "/asd/qwe/czx/ccc/123";
        // 拆分query参数
        String[] pathInfoArr = path.split("\\?");
        String pathDir = pathInfoArr[0];
        String pathQuery = (pathInfoArr.length >= 2) ? pathInfoArr[1] : "";

        // 路径映射路由

        // 路径解化成控制器
        String[] pathArray = pathDir.split("/");
        System.out.println("pathArray:" + Arrays.toString(pathArray));
        List<String> pathFullArray = new ArrayList<String>();
        if (pathArray.length < 4) {
            System.out.println("pathArray.length=" + pathArray.length);
            if (pathArray.length <= 1) {
                pathFullArray.add("");
                pathFullArray.add("index");
                pathFullArray.add("index");
                pathFullArray.add("index");
            } else if (pathArray.length == 2) {
                pathFullArray.add(pathArray[0]);
                pathFullArray.add(pathArray[1]);
                pathFullArray.add("index");
                pathFullArray.add("index");
            } else if (pathArray.length == 3) {
                pathFullArray.add(pathArray[0]);
                pathFullArray.add(pathArray[1]);
                pathFullArray.add(pathArray[2]);
                pathFullArray.add("index");
            }
            pathArray = pathFullArray.toArray(new String[pathFullArray.size()]);
        }

        // 转字符串
        String pathNew = String.join("/", pathArray);

        System.out.println("pathFullArray: " + Arrays.toString(pathArray));
        System.out.println("pathNew: " + pathNew);

        // 参数解化
    }

    @Test
    public void test5()
    {
        String path = "http://www.www.com/asd/qwe/zxc?1231231=asd/asdd&word=123";
        Pathinfo pathinfo = Pathinfo.parseFromUrl(path);
        System.out.println("pathinfo: " + pathinfo);
    }

    @Test
    public void test6()
    {
        UserModel userModel = UserModel.get().where("id", "=", "1").find();
        QLog.info(userModel.name, userModel);
    }
}
