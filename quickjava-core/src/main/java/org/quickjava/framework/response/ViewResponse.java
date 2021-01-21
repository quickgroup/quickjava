package org.quickjava.framework.response;

import org.quickjava.common.QLog;
import org.quickjava.common.QUtils;
import org.quickjava.common.utils.QFileUtils;
import org.quickjava.framework.exception.QuickException;
import org.quickjava.framework.http.Request;
import org.quickjava.framework.http.Response;
import org.quickjava.framework.view.engine.FreeMarkerEngine;
import org.quickjava.framework.view.engine.ViewEngine;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Qlo1062-QloPC-zs
 * @date 2021/1/18 15:51
 */
public class ViewResponse extends QuickResponse {

    public Map<String, Object> data = new LinkedHashMap<>();

    public String template = null;

    public ViewEngine engine = null;

    public ViewResponse() {
        this.engine = new FreeMarkerEngine();
    }

    public ViewResponse(String template) {
        this.template = template;
        this.engine = new FreeMarkerEngine();
    }

    public ViewResponse(String template, Map data) {
        this.template = template;
        this.data = data;
        this.engine = new FreeMarkerEngine();
    }

    public ViewResponse(File file, Map data) {
        this.template = QFileUtils.getFileContents(file);
        this.data = data;
        this.engine = new FreeMarkerEngine();
    }

    @Override
    public String output(Request request, Response response)
    {
        if (template == null)
            throw new QuickException("template is null");

        if (data == null)
            data = new LinkedHashMap<>();

        Long startTime = QUtils.getTimestamp();

        engine.setView(template);
        engine.setData(data);
        String result = engine.output();

        QLog.debug("Template-"+engine.name+": " + (QUtils.getTimestamp() - startTime) + "ms");

        return result;
    }
}
