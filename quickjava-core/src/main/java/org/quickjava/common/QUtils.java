package org.quickjava.common;

import java.util.Date;

/**
 * @author Qlo1062-(QloPC-Msi)
 * @date 2021/1/17 23:01
 * @ProjectName quickjava
 */
public class QUtils {

    /**
     * Gets the current work environment path
     * @langCn 获取当前工作环境路径
     * @return String
     */
    public static String getRootPath()
    {
        return System.getProperty("user.dir");
    }

    /**
     * Gets a millisecond timestamp
     * @langCn 获取毫秒级时间戳
     * @return Long
     */
    public static Long getTimestamp() {
        return new Date().getTime();
    }

    /**
     * Gets the second timestamp
     * @langCn 获取秒时间戳
     * @return Long
     */
    public static Long getSecondTimestamp()
    {
        return getTimestamp() / 1000;
    }

    /**
     * @langCn 判断字符串为null 或 空
     * @param s
     * @return
     */
    public static Boolean stringIsEmpty(String s) {
        return (s == null || "".equals(s)) ? true : false;
    }

    /**
     * @langCn 获取指定范围内的随机整数
     * @param min
     * @param max
     * @return
     */
    public static Integer randInt(int min, int max)
    {
        return (int) Math.floor(Math.random() * (max - min) + min);
    }
}
