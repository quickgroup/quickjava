package org.quickjava.framework.server;

import org.quickjava.common.QLog;
import org.quickjava.framework.Dispatch;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class TomcatServlet extends HttpServlet {

    private static final long serialVersionUID = -1L;

    public TomcatServlet() {
        QLog.info("HttpServlet instantiation.");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Dispatch.get().exec(req, resp);
    }
}
