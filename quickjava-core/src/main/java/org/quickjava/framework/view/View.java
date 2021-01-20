package org.quickjava.framework.view;

import org.quickjava.common.QUtils;
import org.quickjava.common.utils.QFileUtils;
import org.quickjava.framework.exception.QuickException;
import org.quickjava.framework.exception.QuickExceptionCode;

import java.io.File;

/**
 * @author Qlo1062-(QloPC-Msi)
 * @date 2021/1/20 20:56
 * @projectName quickjava
 */
public class View {

    private String template;

    private String output;

    public View(String template)
    {
        this.template = template;
    }

    public View(File file)
    {
        try {
            if (!file.exists()) {
                throw new QuickException(QuickExceptionCode.ERROR.setMsg("文件不存在！"));
            }
            this.template = QFileUtils.getFileContents(file);
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }

}
