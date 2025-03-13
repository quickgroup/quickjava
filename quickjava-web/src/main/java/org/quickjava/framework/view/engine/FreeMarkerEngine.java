package org.quickjava.framework.view.engine;

import freemarker.cache.ClassTemplateLoader;
import freemarker.template.*;
import org.quickjava.common.utils.FileUtils;
import org.quickjava.framework.Cache;
import org.quickjava.framework.Kernel;
import org.quickjava.framework.Lang;
import org.quickjava.framework.exception.QuickException;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

/**
 * @author Qlo1062-(QloPC-Msi)
 * @date 2021/1/21 17:30
 * @projectName quickjava
 */
public class FreeMarkerEngine implements Engine {

    private String template = null;

    private Configuration configuration = null;

    private TimeZone timeZone = null;

    private Map<String, Object> dataMap = new HashMap<>();

    @Override
    public void setTemplate(String template) {
        this.template = template;
    }

    @Override
    public void initEngine() {
        try {
            configuration = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
            configuration.setObjectWrapper(new DefaultObjectWrapper(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS));
            configuration.setTemplateLoader(new ClassTemplateLoader(FreeMarkerEngine.class, ""));
            configuration.setCacheStorage(new freemarker.cache.MruCacheStorage(20, 999999));
            configuration.setSetting(Configuration.CACHE_STORAGE_KEY, "strong:20, soft:999999");

            timeZone = TimeZone.getTimeZone(Kernel.config().getDict("app").getString("timeZone", "GMT+8"));

        } catch (Exception e) {
            throw new QuickException(Lang.to("模板引擎初始化失败：" + e), e);
        }
    }

    @Override
    public void setData(Map<String, Object> data) {
        this.dataMap = data;
    }

    @Override
    public String render()
    {
        try {
            /*
             * 文件缓存
             * 1. 读取缓存文件最后一次修改时间
             * 2. 不存在就新建，并保存修改时间
             * 3. 存在但修改时间未变，继续使用
             * 3-2. 存在但修改时间已修改，替换文件内容，并保存修改时间
             * 模板对象缓存
             * 1. 使用Cache，失效时间1小时，或者文件内容引发更新
             */
            String templateCK = String.format("__QUICK_VIEW_%s", this.template);
            Template template = (Template) Cache.get(templateCK);
            if (template == null) {
                String tplContent = FileUtils.getFileContents(this.template);
                template = new Template(this.template, tplContent, configuration);
                template.setTimeZone(this.timeZone);
                Cache.set(templateCK, template);
            }

            StringWriter writer = new StringWriter();
            template.process(dataMap, writer);        // NOTE::渲染
            return writer.toString();

        } catch (TemplateException | IOException e) {
            e.printStackTrace();
            throw new QuickException(e);
        }
    }
}
