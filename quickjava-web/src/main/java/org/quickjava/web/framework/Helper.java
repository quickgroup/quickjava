package org.quickjava.web.framework;

import org.quickjava.web.common.QuickUtil;
import org.quickjava.web.framework.http.Request;

/**
 * @author QloPC-Msi
 * #date 2021/0108
 */
public class Helper extends QuickUtil {

    /**
     * #quickLang 获取资源文件
     * @return
     */
    public static String getResource(String name)
    throws Exception
    {
        Request request = Kernel.getCurrentRequest();
        return request.servletContext.getResource(name).getPath();
    }

    public static void setCurrentRequest(Request request) {
        Kernel.setCurrentRequest(request);
    }

    public static Request getCurrentRequest() {
        return Kernel.getCurrentRequest();
    }

}
