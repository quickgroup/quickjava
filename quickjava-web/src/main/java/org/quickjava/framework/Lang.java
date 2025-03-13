/*
 * Copyright (c) 2021.
 * More Info to http://www.quickjava.org
 */

package org.quickjava.framework;

import org.quickjava.common.QuickLog;
import org.quickjava.common.utils.Md5Utils;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Qlo1062-(QloPC-zs)
 * @date 2021/2/1 11:40
 */
public class Lang {

    private static String language = "zh-cn";

    private static Map<String, String> tran = new LinkedHashMap<>();

    public static void init()
    {
        String language = Kernel.config().get("app").getString("lang");
        Lang.language = language.toLowerCase();
        // 默认语言包
        tran.put(Md5Utils.encrypt("App start ..."), "应用启动中 ...");

        QuickLog.debug(Lang.to("Lang init Complete."));
    }

    /**
     * #quickLang 获取对应输出语言
     * @param text
     * @param language
     * @return
     */
    public static String to(String text, String language, Object ... args)
    {
        return tran.getOrDefault(Md5Utils.encrypt(text), text);
    }

    public static String to(String text)
    {
        return to(text, language);
    }
}
