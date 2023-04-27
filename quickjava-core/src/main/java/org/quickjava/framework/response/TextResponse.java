/*
 * Copyright (c) 2021.
 * More Info to http://www.quickjava.org
 */

package org.quickjava.framework.response;

import org.quickjava.framework.http.Request;
import org.quickjava.framework.http.Response;

/**
 * @author Qlo1062-QloPC-zs
 * @date 2021/1/18 15:51
 */
public class TextResponse extends QuickResponse {

    public TextResponse() {
    }

    public TextResponse(byte[] content) {
        this.content = content;
    }

    public TextResponse(String contentString) {
        this.content = stringToBytes(contentString);
    }

    /**
     * @langCn 输出
     * @param response 输出对象
     * @return 渲染后的字节码
     */
    public byte[] render(Request request, Response response)
    {
        return content;
    }
}
