package org.demo.www;

import org.quickjava.framework.QuickJavaBoot;
import org.quickjava.framework.annotation.QuickBoot;

@QuickBoot("org.demo.www.application")
public class ApplicationBoot {

    public static void main(String[] args)
    {
        QuickJavaBoot.run(ApplicationBoot.class, args);
    }

}