package org.demo.www;

import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.demo.www.application.index.model.UserModel;
import org.junit.Test;
import org.quickjava.common.QLog;
import org.quickjava.common.QUtils;
import org.quickjava.common.utils.QFileUtils;
import org.quickjava.framework.config.AppConfig;
import org.quickjava.framework.controller.Module;
import org.quickjava.framework.http.Pathinfo;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URL;
import java.util.*;

/**
 * @author QloPC-Msi
 * @date
 */
public class TestTemplate {

    @Test
    public void test1()
    {
        Configuration configuration = new Configuration();
        configuration.setObjectWrapper(new DefaultObjectWrapper());
        configuration.setTemplateLoader(new ClassTemplateLoader(Test.class, "/template"));
        try {
            Template template = configuration.getTemplate("test.html");
            StringWriter writer = new StringWriter();

            // 数据
            Map<String, Object> context = new HashMap<>();
            context.put("message", "第一个Maven_FreeMarker程序");

            template.process(context, writer);
            System.out.println(writer.toString());

        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        }
    }
}
