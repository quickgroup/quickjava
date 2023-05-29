package org.quickjava.framework.response;

import org.quickjava.common.QuickLog;
import org.quickjava.common.QuickUtil;
import org.quickjava.framework.exception.QuickException;
import org.quickjava.framework.http.Request;
import org.quickjava.framework.http.Response;
import org.quickjava.framework.view.ViewMan;
import org.quickjava.framework.view.engine.Engine;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Qlo1062-QloPC-zs
 * @date 2021/1/18 15:51
 */
public class ViewResponse extends QuickResponse {

    public Map<String, Object> data = new LinkedHashMap<>();

    public String templatePath = null;

    public String template = null;

    public Engine engine = null;

    public ViewResponse() {
        this.engine = ViewMan.engine;
    }

    public ViewResponse(File file, Map data) {
        this.template = file.getPath();
        this.data = data;
        this.engine = ViewMan.engine;
    }

    @Override
    public byte[] render(Request request, Response response)
    {
        if (template == null) {
            throw new QuickException("template is null");
        }
        if (data == null) {
            data = new LinkedHashMap<>();
        }

        long startNanoTime = System.nanoTime();

        engine.setTemplate(template);
        engine.setData(data);
        String result = engine.render();

//        QuickLog.debug("Engine " + engine.getClass().getSimpleName() + ": " + QuickUtil.endNanoTimeMS(startNanoTime) + "ms");
        return this.content = result.getBytes();
    }
}
