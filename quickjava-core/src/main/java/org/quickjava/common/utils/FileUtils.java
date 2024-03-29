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

package org.quickjava.common.utils;

import org.quickjava.common.QuickUtil;

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
     * @langCn 从输入流读取内容
     * @param inputStream
     * @return
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
            exc.printStackTrace();
        }
        return stringBuffer.toString();
    }

    /**
     * @langCn 按环境读取文件
     * @param packageName
     * @param filename
     * @return
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
                if (!"".equals(packageName)) {
                    packageName += "/";
                }
                String filePath = packageName + filename;
                InputStream inputStream = FileUtils.class.getClassLoader().getResourceAsStream(filePath);
                content = FileUtils.getInputStreamContent(inputStream);

            }
        } catch (Exception exc) {
            exc.printStackTrace();
        }
        return content;
    }

}
