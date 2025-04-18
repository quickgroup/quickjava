/*
 * Copyright (c) 2021.
 * More Info to http://www.quickjava.org
 */

package org.quickjava.web.framework;

import org.quickjava.web.common.utils.Md5Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Qlo1062-(QloPC-zs)
 * #date 2021/2/1 11:40
 */
public class Lang {
    private static final Logger logger = LoggerFactory.getLogger(Lang.class);

    private static String language = "zh-cn";

    private static Map<String, String> tran = new LinkedHashMap<>();

    public static void init()
    {
        String language = Kernel.config().getDict("app").getString("lang");
        Lang.language = language.toLowerCase();
        // 默认语言包
        tran.put(Md5Utils.encrypt("App start ..."), "应用启动中 ...");

        logger.debug(Lang.to("Lang init Complete."));
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
