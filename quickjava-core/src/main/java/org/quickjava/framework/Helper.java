package org.quickjava.framework;

import org.quickjava.common.QUtils;
import org.quickjava.framework.http.Request;

/**
 * @author QloPC-Msi
 * @date 2021/0108
 */
public class Helper extends QUtils {

    /**
     * @langCn 获取资源文件
     * @return
     */
    public static String getResource(String name)
    throws Exception
    {
        Request request = App.getCurrentRequest();
        return request.servletContext.getResource(name).getPath();
    }

    public static void setCurrentRequest(Request request) {
        App.setCurrentRequest(request);
    }

    public static Request getCurrentRequest() {
        return App.getCurrentRequest();
    }

}
