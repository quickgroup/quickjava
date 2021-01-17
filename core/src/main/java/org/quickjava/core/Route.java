package org.quickjava.core;

import java.io.File;
import java.util.*;

/**
 * @author Qlo1062
 * @date 2021/01/17
 */
public class Route {

    private static Route route = new Route();

    /**
     * 用户定义的拦截器
     */
    private static Map<String, Object> routeList = new HashMap<String, Object>();

    public static Route get() {
        return route;
    }

    public static void init(String[] args)
            throws Exception
    {
        get().scanPackages();
    }

    /**
     * 扫描包下面的控制器-方法(public)
     * 格式：
     *      application/{module}/controller/{controller}
     *      /{module}/{controller}/{method}
     */
    private void scanPackages()
        throws Exception
    {
        String basePackages = Env.getString("basePackages");
        String packagePath = basePackages.replace(".", "/");
        System.out.println("packagePath => " + packagePath);

        ClassLoader loader = Thread.currentThread().getContextClassLoader();

        File packageFile = new File(packagePath);
        if (!packageFile.exists()) {
            throw new ClassNotFoundException(basePackages + " package not found");
        }

        File[] moduleFiles = packageFile.listFiles();


//        File dir = new File(this.getClass().getName());
//        System.out.println("dir => " + dir.list());
//
//        final boolean recursive = true;
//        // 如果存在 就获取包下的所有文件 包括目录
//        File[] dirfiles = dir.listFiles();
//
//        System.out.println("dirfiles => " + dirfiles);
//
//        for (File file : dirfiles) {
//            System.out.println("file => " + file.getName());
//        }

    }
}
