package org.quickjava.web.common;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Qlo1062-(QloPC-zs)
 * @date 2021/1/20 12:14
 */
public class Console {

    public static class Color {
        public static int black = 30;
        public static int red = 31;
        public static int green = 32;
        public static int yellow = 33;
        public static int blue = 34;
        public static int violet_red = 35;  // 紫红色
        public static int nordic_blue = 36; // 青蓝色
        public static int white = 37;

        public static String output(String str, int color)
        {
            return "\033[1;" + color + "m" + str + "\033[0m";
        }

        public static String output(String str, String level)
        {
            Integer color = levelColor.containsKey(level) ? levelColor.get(level) : black;
            String result = (color == null) ? level : "\033[1;" + color + "m" + str + "\033[0m";
            return result;
        }

        private static Map<String, Integer> levelColor = new HashMap<>();

        static {
            levelColor.put("DEBUG", null);
            levelColor.put("INFO", Console.Color.green);
            levelColor.put("WARN", Console.Color.yellow);
            levelColor.put("ERROR", Console.Color.red);
            levelColor.put("FATAL", Console.Color.violet_red);
        }
    }

    /**
     * 判断看着他支持ANSI
     * @return
     */
    public static Boolean isANSI() {
        System.out.println(System.getenv().get("TERM"));
        return (System.console() != null && System.getenv().get("TERM") != null);
    }
}
