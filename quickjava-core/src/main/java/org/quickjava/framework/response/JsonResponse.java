package org.quickjava.framework.response;

import org.quickjava.framework.bean.Dict;
import org.quickjava.framework.http.Request;
import org.quickjava.framework.http.Response;

import java.util.Map;

/**
 * @author Qlo1062-QloPC-zs
 * @date 2021/1/18 15:51
 */
public class JsonResponse extends QuickResponse {

    @Override
    public String output(Request request, Response response) {
        return null;
    }

    /**
     * @langCn Map对象，解析为JSON输出
     * @param data
     */
    public JsonResponse(Map data) {
        this.data = data;
    }

    /**
     * @langCn
     * @param data
     */
    public JsonResponse(Dict data) {
        this.data = data;
    }
}
