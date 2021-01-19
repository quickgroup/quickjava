package org.quickjava.framework;

import org.quickjava.framework.App;

/**
 * 启动类
 */
public class QuickJavaBoot {

    /**
     * 启动入口
     * @param args
     */
    public static void run(Class clazz, String[] args)
    {
        App.startup(clazz, args);
    }

}