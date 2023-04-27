package org.quickjava.framework;

import org.quickjava.common.QuickUtil;
import org.quickjava.framework.http.Request;

/**
 * @author QloPC-Msi
 * @date 2021/0108
 */
public class Helper extends QuickUtil {

    /**
     * @langCn 获取资源文件
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
