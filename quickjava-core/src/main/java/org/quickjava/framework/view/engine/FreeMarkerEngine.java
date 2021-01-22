package org.quickjava.framework.view.engine;

import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.quickjava.common.QLog;
import org.quickjava.common.QUtils;
import org.quickjava.framework.exception.QuickException;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Qlo1062-(QloPC-Msi)
 * @date 2021/1/21 17:30
 * @projectName quickjava
 */
public class FreeMarkerEngine extends ViewEngine {

    public String name = "FreeMarkerEngine";

    Map<String, Object> dataModel = new HashMap<>();

    @Override
    public void setData(Map<String, Object> data) {
        this.dataModel = data;
    }

    @SuppressWarnings("deprecation")
    @Override
    public String output()
    {
        Configuration configuration = new Configuration();
        configuration.setObjectWrapper(new DefaultObjectWrapper());
        configuration.setTemplateLoader(new ClassTemplateLoader(FreeMarkerEngine.class, "/template"));

        try {
            QLog.debug("view: " + this.view);

            Template template = new Template("Template", this.view, configuration);

            StringWriter writer = new StringWriter();
            template.process(dataModel, writer);

//            System.out.println(writer.toString());
            return writer.toString();

        } catch (TemplateException | IOException e) {
            e.printStackTrace();
            throw new QuickException(e);
        }
    }
}
