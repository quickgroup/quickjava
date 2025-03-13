/*
 * Copyright (c) 2020~2021 http://www.quickjava.org All rights reserved.
 * +-------------------------------------------------------------------
 * Org: QuickJava
 * +-------------------------------------------------------------------
 * Author: Qlo1062
 * +-------------------------------------------------------------------
 * File: QuickDefaultServlet.java
 * +-------------------------------------------------------------------
 * Date: 2021/06/17 17:58:17
 * +-------------------------------------------------------------------
 * License: Apache Licence 2.0
 * +-------------------------------------------------------------------
 *
 */

package org.quickjava.framework.server;

import org.apache.catalina.servlets.DefaultServlet;
import org.quickjava.common.QuickLog;
import org.quickjava.framework.Dispatch;

import javax.servlet.DispatcherType;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * #quickLang 未找到路径对应操作，交由本Servlet再处理
 */
public class QuickDefaultServlet extends DefaultServlet {

    private static final long serialVersionUID = -1L;

    public QuickDefaultServlet() {
        QuickLog.debug("QuickDefaultServlet instantiation.");
    }

    /**
     * 增加本地路径映射，实现热更新资源文件
     * @param urlPath s
     * @param filePath 文件或目录
     */
    private void addResourceLocations(String urlPath, String filePath)
    {
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        this.serveResource(request, response, true, this.fileEncoding);
    }

}
