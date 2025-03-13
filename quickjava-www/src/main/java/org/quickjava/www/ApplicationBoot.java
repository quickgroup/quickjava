package org.quickjava.www;

import org.quickjava.web.framework.QuickJavaBoot;
import org.quickjava.web.framework.annotation.ApplicationQuickBoot;

@ApplicationQuickBoot
public class ApplicationBoot {

    public static void main(String[] args)
    {
//        Kernel.init(ApplicationBoot.class, args);
//        new TestDb().testQueryOOM();
//        new TestDb().testTransactionOOM();
        QuickJavaBoot.start(ApplicationBoot.class, args);
    }

}
