/*
 * Copyright (c) 2021.
 * More Info to http://www.quickjava.org
 */

package org.quickjava.framework;

/**
 * @author Qlo1062-(QloPC-zs)
 * @date 2021/2/1 11:40
 */
public class Lang {

    private static String language = "zh-cn";

    public static void init(String language)
    {
        Lang.language = language.toLowerCase();
    }

    /**
     * @langCn 获取对应输出语言
     * @param text
     * @param language
     * @return
     */
    public static String to(String text, String language, Object ... args)
    {
        return text;
    }

    public static String to(String text)
    {
        return to(text, language);
    }
}
