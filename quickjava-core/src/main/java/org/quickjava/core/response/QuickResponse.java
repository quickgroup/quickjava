package org.quickjava.core.response;

import org.quickjava.core.http.Response;

/**
 * @author Qlo1062-QloPC-zs
 * @date 2021/1/18 15:51
 */
public class QuickResponse {

    /**
     * @langCn 返回内容
     */
    protected Object data;

    public QuickResponse() {
    }

    /**
     * @langCn 文本
     * @param data
     */
    public QuickResponse(String data) {
        this.data = data;
    }

    /**
     * @langCn 输出
     * @param response
     * @return
     */
    public String output(Response response) {
        return data.toString();
    }
}
