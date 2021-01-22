package org.quickjava.common;

import java.io.*;

/**
 * @author Qlo1062-(QloPC-Msi)
 * @date 2021/1/17 22:54
 * @ProjectName quickjava
 */
public class QFileUtils {

    public static String getFileContents(String name)
    {
        return getFileContents(new File(name));
    }

    public static String getFileContents(File file)
    {
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
            StringBuffer stringBuffer = new StringBuffer();
            int num = 0;
            byte[] buf = new byte[512];
            while((num = fileInputStream.read(buf))!= -1) {
                stringBuffer.append(new String(buf,0,num));
            }

            return stringBuffer.toString();

        } catch (Throwable tr){
            tr.printStackTrace();
        } finally {
            if (fileInputStream != null) {
                try{
                    fileInputStream.close();
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
            if (QUtils.isClassMode()) {
                String filePath = QUtils.getClassesPath() + "/" + packageName + "/" + filename;
                content = QFileUtils.getFileContents(filePath);

            } else if (QUtils.isJarMode()) {
                if (!"".equals(packageName)) {
                    packageName += "/";
                }
                String filePath = packageName + filename;
                InputStream inputStream = QFileUtils.class.getClassLoader().getResourceAsStream(filePath);
                content = QFileUtils.getInputStreamContent(inputStream);

            }
        } catch (Exception exc) {
            exc.printStackTrace();
        }
        return content;
    }

}
