/*
 * Copyright (c) 2020~2021 http://www.quickjava.org All rights reserved.
 * +-------------------------------------------------------------------
 * @author: Qlo1062
 * +-------------------------------------------------------------------
 * File: Hook.java
 * +-------------------------------------------------------------------
 * Date: 2021/06/01 18:02:01
 * +-------------------------------------------------------------------
 *
 */
package org.quickjava.framework;

import javafx.util.Callback;
import org.quickjava.common.QuickLog;

import java.util.*;


public class Hook {

    private static Map<String, List<Object>> hooks = null;

    public static void init()
    {
        Hook.hooks = new LinkedHashMap<>();
        QuickLog.debug(Lang.to("Hook init Complete."));
    }

    public static void listen(String key, String className)
    {
        add(key, new String[]{className});
    }

    public static void add(String key, Callback object)
    {
        add(key, new Object[]{object});
    }

    public static void add(String key, Object[] classNames)
    {
        hookKeyInit(key);

        for (Object target : classNames) {
            if (hooks.get(key).indexOf(target) > -1)
                continue;
            hooks.get(key).add(target);
        }
    }

    public static Object call(String key, Object ... args)
    {
        hookKeyInit(key);

        Map<String, Object> result = null;
        hooks.get(key).forEach(item -> {
            if (item instanceof String) {
                callClassMethod((String)item, key, args);

            } else if (item instanceof Callback) {
                Callback callback = (Callback) item;
                callback.call(args);

            }

        });

        return result;
    }

    private static Object callClassMethod(String className, String method, Object ... args)
    {
        return null;
    }

    private static void hookKeyInit(String key) {
        if (!hooks.containsKey(key)) {
            hooks.put(key, new LinkedList<>());
        }
    }

}

