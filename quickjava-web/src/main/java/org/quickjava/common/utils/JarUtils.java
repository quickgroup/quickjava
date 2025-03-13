/*
 * Copyright (c) 2020~2021 http://www.quickjava.org All rights reserved.
 * +-------------------------------------------------------------------
 * Org: QuickJava
 * +-------------------------------------------------------------------
 * Author: Qlo1062
 * +-------------------------------------------------------------------
 * File: JarUtils.java
 * +-------------------------------------------------------------------
 * Date: 2021/12/28 16:03:28
 * +-------------------------------------------------------------------
 * License: Apache Licence 2.0
 * +-------------------------------------------------------------------
 *
 */

package org.quickjava.common.utils;

/**
 * Jar包文件读取助手
 */
public class JarUtils {

    private String path = null;

    public JarUtils(String path) {
        this.path = path;
        init();
    }

    private void init()
    {
        System.out.println("初始化");
    }

    /**
     * #quickLang 扫描文件
     */
    public void scanFiles()
    {
    }

}
