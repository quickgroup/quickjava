package org.quickjava.web.framework.view.engine;

import java.util.Map;

/**
 * @author Qlo1062-(QloPC-Msi)
 * @date 2021/1/21 17:33
 * @projectName quickjava
 */
public interface Engine {

    /**
     * 模板路径
     * @param template
     */
    void setTemplate(String template);

    void initEngine();

    void setData(Map<String, Object> data);

    String render();
}
