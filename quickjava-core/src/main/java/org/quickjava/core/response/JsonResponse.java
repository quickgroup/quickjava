package org.quickjava.core.response;

import org.quickjava.core.http.Response;

import java.util.Map;

/**
 * @author Qlo1062-QloPC-zs
 * @date 2021/1/18 15:51
 */
public class JsonResponse extends QuickResponse {

    @Override
    public String output(Response response) {
        return null;
    }

    /**
     * @langCn Map对象，解析为JSON输出
     * @param data
     */
    public JsonResponse(Map data) {
        this.data = data;
    }
}
