package org.demo.www;

import org.quickjava.QuickJavaBoot;
import org.quickjava.core.annotation.QuickBoot;

@QuickBoot("org.demo.www.application")
public class ApplicationBoot {

    public static void main(String[] args)
    {
        QuickJavaBoot.run(ApplicationBoot.class, args);
    }

}