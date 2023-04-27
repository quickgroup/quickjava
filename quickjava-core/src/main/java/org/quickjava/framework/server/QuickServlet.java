package org.quickjava.framework.server;

import org.quickjava.common.QuickLog;
import org.quickjava.framework.Dispatch;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class QuickServlet extends HttpServlet {

    private static final long serialVersionUID = -1L;

    public QuickServlet() {
        QuickLog.debug("QuickServlet instantiation.");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Dispatch.get().exec(request, response);
    }
}
