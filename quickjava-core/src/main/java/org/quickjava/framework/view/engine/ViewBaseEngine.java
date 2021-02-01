package org.quickjava.framework.view.engine;

import java.util.Map;

/**
 * @author Qlo1062-(QloPC-Msi)
 * @date 2021/1/21 17:33
 * @projectName quickjava
 */
public abstract class ViewBaseEngine {

    public String name = "ViewBaseEngine";

    protected String view = null;

    public void setView(String view)
    {
        this.view = view;
    }

    public abstract void setData(Map<String, Object> data);

    public abstract String output();
}
