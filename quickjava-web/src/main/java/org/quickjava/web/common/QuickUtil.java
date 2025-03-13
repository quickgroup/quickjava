package org.quickjava.web.common;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Qlo1062-(QloPC-Msi)
 * @date 2021/1/17 23:01
 * @ProjectName quickjava
 */
public class QuickUtil {

    /**
     * Gets the current environment path
     * #quickLang 获取环境路径
     * @return String
     */
    public static String getRootPath()
    {
        return System.getProperty("user.dir");
    }

    /**
     * Gets the current classes environment path
     * #quickLang 获取当前classes路径
     * @return String
     */
    public static String getClassesPath()
    {
        String path = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        if (path.indexOf("WEB-INF") != -1) {
            return path.substring(1, path.indexOf("WEB-INF"));
        }
        return path.substring(1, path.length() - 1);
    }

    /**
     * #quickLang 获取纳秒（不是时间戳
     * @return Long
     */
    public static Long getNanoTime() {
        return System.nanoTime();
    }

    /**
     * #quickLang 获取纳秒间隔时间
     * @return Long
     */
    public static double endNanoTimeMS(Long startTime) {
        Long endTime = System.nanoTime();
        return ((double) (endTime - startTime)) / 1000000;
    }

    /**
     * Gets a millisecond timestamp
     * #quickLang 获取毫秒级时间戳
     * @return Long
     */
    public static Long getTimestamp() {
        return new Date().getTime();
    }

    /**
     * Gets the second timestamp
     * #quickLang 获取秒时间戳
     * @return Long
     */
    public static Long getSecondTimestamp()
    {
        return getTimestamp() / 1000;
    }

    /**
     * #quickLang 获取格式化时间
     * @return
     */
    public static String getDateTime(String format, Date date)
    {
        return new SimpleDateFormat(format).format(date);
    }

    public static String getDateTime()
    {
        return getDateTime("YYYY-MM-dd HH:mm:ss.SSS", new Date());
    }

    /**
     * #quickLang 判断字符串为null 或 空
     * @param s
     * @return
     */
    public static Boolean stringIsEmpty(String s) {
        return (s == null || "".equals(s)) ? true : false;
    }

    /**
     * #quickLang 获取指定范围内的随机整数
     * @param min
     * @param max
     * @return
     */
    public static Integer randInt(int min, int max)
    {
        return (int) Math.floor(Math.random() * (max - min) + min);
    }

    /**
     * #quickLang 获取调用者类名+方法名+行数
     * @return
     */
    public static String getCallClassMethod()
    {
        StackTraceElement trace = new Exception().getStackTrace()[3];   // 3
        return trace.getClassName() + "." + trace.getMethodName() + ":" + trace.getLineNumber();
    }

    /**
     * #quickLang 获取调用者类名+方法名+行数
     * @return
     */
    public static String getSimpleCallClassMethod()
    {
        StackTraceElement trace = new Exception().getStackTrace()[3];   // 3
        String className = trace.getClassName();
        className = className.substring(className.lastIndexOf(".") + 1, className.length());
        return className + "." + trace.getMethodName() + ":" + trace.getLineNumber();
    }

    public static void exit()
    {
        System.exit(0);
    }

    /**
     * 获取运行模式
     * @return
     */
    public static String getRunMode()
    {
        try {
            String path = QuickUtil.class.getResource("QuickUtil.class").toString();
            return new URI(path).getScheme();
        } catch (Exception exc) {
            exc.printStackTrace();
        }
        return "";
    }

    public static Boolean isClassMode() {
        return "file".equals(getRunMode()) ? true : false;
    }

    public static Boolean isJarMode() {
        return "jar".equals(getRunMode()) ? true : false;
    }

    /**
     * #quickLang 堆栈数组转string数组
     * @param stackTraceElements
     * @return
     */
    public static String[] stackTraceArrToStringArr(StackTraceElement[] stackTraceElements)
    {
        String[] strings = new String[stackTraceElements.length];
        for (int fi = 0; fi < stackTraceElements.length; fi++) {
            strings[fi] = stackTraceElements[fi].toString();
        }
        return strings;
    }

    public static String stackTraceArrToString(StackTraceElement[] stackTraceElements)
    {
        String[] strings = new String[stackTraceElements.length];
        for (int fi = 0; fi < stackTraceElements.length; fi++) {
            strings[fi] = stackTraceElements[fi].toString();
        }
        return String.join("\n", strings);
    }
}
