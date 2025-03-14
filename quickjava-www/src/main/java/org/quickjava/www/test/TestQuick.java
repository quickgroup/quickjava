package org.quickjava.www.test;

import org.junit.runner.RunWith;
import org.quickjava.web.framework.QuickJavaRunner;
import org.quickjava.www.application.index.model.UserModel;
import org.junit.Test;
import org.quickjava.web.common.QuickUtil;
import org.quickjava.web.framework.http.Pathinfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.*;

/**
 * @author QloPC-Msi
 * #date
 */
@RunWith(QuickJavaRunner.class)
public class TestQuick {
    private static final Logger logger = LoggerFactory.getLogger(TestQuick.class);

    @Test
    public void test1()
    {
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
//        try {
//            String configYmlContent = QuickFileUtil.getPackageFileContent("", "application.yml");
//            AppConfig.Factory.loadFormYml(configYmlContent);
//
//        } catch (Exception exc) {
//            exc.printStackTrace();
//        }

        System.out.println(java.nio.charset.Charset.defaultCharset().toString());
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
        UserModel user = (new UserModel()).where("id", "=", "1").find();
        List<UserModel> users = (new UserModel()).where("id", "=", "1").select();
        logger.info(user.table, user);
    }

    @Test
    public void test7()
    {
        System.out.println(QuickUtil.isClassMode());
        System.out.println(QuickUtil.isJarMode());
    }

    @Test
    public void test8()
    {
        URL url = TestClass.class.getClassLoader().getResource("application.yml");
        URL url2 = TestClass.class.getClassLoader().getResource("application.yml");
        System.out.println("url: " + url);
        System.out.println("url2: " + url2);

        TestQuick.getPackageResource("");
    }

    public static String getPackageResource(String packageName)
    {
        String rootPath = QuickUtil.getClassesPath();
        System.out.println("rootPath: " + rootPath);
        if (QuickUtil.isClassMode()) {
            System.out.println("isClassMode");
        }
        else if (QuickUtil.isJarMode()) {
            System.out.println("isJarMode");
        }
        return null;
    }

    public static String getPackageResource(Package packageItem)
    {
        return null;
    }
}
