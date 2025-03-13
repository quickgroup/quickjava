/*
 * Copyright (c) 2020~2021 http://www.quickjava.org All rights reserved.
 * +-------------------------------------------------------------------
 * Org: QuickJava
 * +-------------------------------------------------------------------
 * Author: Qlo1062
 * +-------------------------------------------------------------------
 * File: QuickFileUtil.java
 * +-------------------------------------------------------------------
 * Date: 2021/11/17 16:19:17
 * +-------------------------------------------------------------------
 * License: Apache Licence 2.0
 * +-------------------------------------------------------------------
 *
 */

package org.quickjava.web.common.utils;

import org.quickjava.web.common.QuickUtil;

import java.io.*;

/**
 * @author Qlo1062-(QloPC-Msi)
 * @date 2021/1/17 22:54
 * @ProjectName quickjava
 */
public class FileUtils {

    public static String getFileContents(String name)
    {
        return getFileContents(new File(name), "UTF-8");
    }

    public static String getFileContents(File file, String charset)
    {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            byte[] contentByte = new byte[Long.valueOf(file.length()).intValue()];
            int ret = fis.read(contentByte);
            return new String(contentByte, charset);

        } catch (Throwable tr){
            tr.printStackTrace();
        } finally {
            if (fis != null) {
                try{
                    fis.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * #quickLang 从输入流读取内容
     */
    public static String getInputStreamContent(InputStream inputStream)
    {
        StringBuffer stringBuffer = new StringBuffer();
        try {
            int num = 0;
            byte[] buf = new byte[512];
            while ((num = inputStream.read(buf)) != -1) {
                stringBuffer.append(new String(buf, 0, num));
            }
        } catch (Exception exc) {
            throw new RuntimeException(exc);
        }
        return stringBuffer.toString();
    }

    /**
     * #quickLang 按环境读取文件
     * @param packageName 包名
     * @param filename 文件名
     * @return 文件内容
     */
    public static String getPackageFileContent(String packageName, String filename)
    {
        packageName = packageName.replaceAll("\\.", "/");
        String content = null;
        try {
            if (QuickUtil.isClassMode()) {
                String filePath = QuickUtil.getClassesPath() + "/" + packageName + "/" + filename;
                content = FileUtils.getFileContents(filePath);

            } else if (QuickUtil.isJarMode()) {
                if (!packageName.isEmpty()) {
                    packageName += "/";
                }
                String filePath = packageName + filename;
                InputStream inputStream = FileUtils.class.getClassLoader().getResourceAsStream(filePath);
                content = FileUtils.getInputStreamContent(inputStream);

            }
        } catch (Exception exc) {
            throw new RuntimeException(exc);
        }
        return content;
    }

}
