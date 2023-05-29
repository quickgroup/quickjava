package org.quickjava.framework.response;

import org.quickjava.framework.bean.Dict;
import org.quickjava.framework.http.Http;
import org.quickjava.framework.http.Request;
import org.quickjava.framework.http.Response;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Qlo1062-QloPC-zs
 * @date 2021/1/18 15:51
 */
public class JsonResponse extends QuickResponse {

    /**
     * @langCn 输出数据
     */
    public Dict data = new Dict();

    /**
     * @langCn Map对象，解析为JSON输出
     * @param data
     */
    public JsonResponse(Map data) {
        this.data = new Dict(data);
    }

    /**
     * @langCn
     * @param data
     */
    public JsonResponse(Dict data) {
        this.data = data;
    }

    @Override
    public byte[] render(Request request, Response response) {
        return null;
    }

    @Override
    public void outputWrite(Request request, Response response) {
        super.outputWrite(request, response);

        // 设置返回头
        this.setContentType(Http.ContentType.JSON);
    }
}
