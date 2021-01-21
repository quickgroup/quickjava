package org.quickjava.framework.view.engine;

import java.util.Map;

/**
 * @author Qlo1062-(QloPC-Msi)
 * @date 2021/1/21 17:30
 * @projectName quickjava
 */
public class QuickEngine extends ViewEngine {

    public String name = "QuickEngine";

    @Override
    public void setData(Map<String, Object> data) {

    }

    @Override
    public String output() {
        return "FreeMarkerEngine";
    }
}
