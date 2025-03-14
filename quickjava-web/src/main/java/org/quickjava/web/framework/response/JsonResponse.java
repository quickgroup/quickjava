package org.quickjava.web.framework.response;

import org.quickjava.web.framework.bean.Dict;
import org.quickjava.web.framework.http.Http;
import org.quickjava.web.framework.http.Request;
import org.quickjava.web.framework.http.Response;

import java.util.Map;

/**
 * @author Qlo1062-QloPC-zs
 * #date 2021/1/18 15:51
 */
public class JsonResponse extends QuickResponse {

    /**
     * #quickLang 输出数据
     */
    public Dict data = new Dict();

    /**
     * #quickLang Map对象，解析为JSON输出
     * @param data
     */
    public JsonResponse(Map data) {
        this.data = new Dict(data);
    }

    /**
     * #quickLang
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
