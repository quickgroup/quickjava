package org.quickjava.framework.response;

import org.quickjava.framework.http.Response;
import org.quickjava.framework.view.View;

/**
 * @author Qlo1062-QloPC-zs
 * @date 2021/1/18 15:51
 */
public class ViewResponse extends QuickResponse {

    public View view = null;

    public ViewResponse(View view) {
        this.view = view;
    }

    @Override
    public String output(Response response) {
        return null;
    }
}
