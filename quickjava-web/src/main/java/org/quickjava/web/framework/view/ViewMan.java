/*
 * Copyright (c) 2020~2021 http://www.quickjava.org All rights reserved.
 * +-------------------------------------------------------------------
 * Org: QuickJava
 * +-------------------------------------------------------------------
 * Author: Qlo1062
 * +-------------------------------------------------------------------
 * File: Engine.java
 * +-------------------------------------------------------------------
 * Date: 2021/06/21 18:04:21
 * +-------------------------------------------------------------------
 * License: Apache Licence 2.0
 * +-------------------------------------------------------------------
 *
 */

package org.quickjava.web.framework.view;

import org.quickjava.web.common.QuickLog;
import org.quickjava.web.framework.Kernel;
import org.quickjava.web.framework.view.engine.Engine;
import org.quickjava.web.framework.view.engine.FreeMarkerEngine;

public abstract class ViewMan {

    public static Engine engine = null;   // 使用的模板引擎

    public static void init()
    {
        String name = Kernel.config().getDict("view").getString("engine").toLowerCase();
        if ("default".equals(name) || "freemarker".equals(name)) {
        } else {
        }

        // 初始化引擎
        engine = new FreeMarkerEngine();
        engine.initEngine();

        QuickLog.info("ViewMan init complete");
    }

}
