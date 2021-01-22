package org.demo.www;

import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;
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
