package org.quickjava.www;

import org.quickjava.web.framework.QuickJavaBoot;
import org.quickjava.web.framework.annotation.ApplicationQuickBoot;

@ApplicationQuickBoot
public class ApplicationBoot {

    public static void main(String[] args)
    {
        QuickJavaBoot.start(ApplicationBoot.class, args);
    }

}
