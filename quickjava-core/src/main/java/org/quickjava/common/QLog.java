package org.quickjava.common;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Qlo1062-(QloPC-zs)
 * @date 2021/1/20 10:33
 */
public class QLog {


    public static void log(String level, Object ... params)
    {
        String logstr = String.format("%s\t%s [%s]\t%s",
                QUtils.getDateTime(),
                Console.Color.output( level, level),
                QUtils.getCallClassMethod(),
                (params.length == 1 ? params[0].toString() : Arrays.toString(params)) );
        System.out.println(logstr);
        // 写入到文件
    }

    public static void info(Object ... params) {
        log("INFO", params);
    }

    public static void debug(Object ... params) {
        log("DEBUG", params);
    }

    public static void warn(Object ... params) {
        log("WARN", params);
    }

    public static void error(Object ... params) {
        log("ERROR", params);
    }

    public static void error(Throwable exc) {
        error(exc.getStackTrace()[0].getClassName() + ": " + exc.getMessage());
    }

    public static void fatal(Object ... params) {
        log("FATAL", params);
    }
}
