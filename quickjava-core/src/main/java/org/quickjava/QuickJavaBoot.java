package org.quickjava;

import org.quickjava.core.App;

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