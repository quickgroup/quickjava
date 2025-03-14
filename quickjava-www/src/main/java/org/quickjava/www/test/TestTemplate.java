package org.quickjava.www.test;

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
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_30);
        configuration.setObjectWrapper(new DefaultObjectWrapper(Configuration.VERSION_2_3_30));
        configuration.setTemplateLoader(new ClassTemplateLoader(TestTemplate.class, "/template"));
        try {
            Template template = configuration.getTemplate("test.html");
            StringWriter writer = new StringWriter();

            // 数据
            Map<String, Object> context = new HashMap<>();
            context.put("message", "来自代码的消息");

            template.process(context, writer);
            System.out.println(writer.toString());

        } catch (TemplateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
