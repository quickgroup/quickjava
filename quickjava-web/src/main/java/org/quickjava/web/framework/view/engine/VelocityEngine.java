/*
 * Copyright (c) 2020~2021 http://www.quickjava.org All rights reserved.
 * +-------------------------------------------------------------------
 * Org: QuickJava
 * +-------------------------------------------------------------------
 * Author: Qlo1062
 * +-------------------------------------------------------------------
 * File: VelocityEngine.java
 * +-------------------------------------------------------------------
 * Date: 2021/06/21 18:23:21
 * +-------------------------------------------------------------------
 * License: Apache Licence 2.0
 * +-------------------------------------------------------------------
 *
 */

package org.quickjava.web.framework.view.engine;

import org.apache.velocity.app.Velocity;
import org.apache.velocity.runtime.VelocityEngineVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author Qlo1062-(QloPC-Msi)
 */
public class VelocityEngine implements Engine {
    private static final Logger logger = LoggerFactory.getLogger(VelocityEngine.class);

    private String template = null;

    Map<String, Object> dataMap = new HashMap<>();

    @Override
    public void setTemplate(String template) {
        this.template = template;
    }

    @Override
    public void initEngine() {
        logger.info("View engine: VelocityEngine" + VelocityEngineVersion.VERSION);

        Properties properties = new Properties();
        //从类路径加载模板文件 设置velocity资源加载方式为class
        properties.setProperty("resource.loader", "class");
        //设置velocity资源加载方式为class时的处理类
        properties.setProperty("class.resource.loader.class","org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        properties.setProperty(Velocity.ENCODING_DEFAULT, "UTF-8");
        properties.setProperty(Velocity.INPUT_ENCODING, "UTF-8");

        //实例化一个VelocityEngine对象
        org.apache.velocity.app.VelocityEngine engine = new org.apache.velocity.app.VelocityEngine(properties);
        engine.init();
    }

    @Override
    public void setData(Map<String, Object> data) {
        this.dataMap = data;
    }

    @Override
    public String render()
    {
        return null;
    }
}
