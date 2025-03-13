package org.quickjava.www;

import org.quickjava.web.framework.Kernel;
import org.quickjava.web.framework.annotation.ApplicationQuickBoot;
import org.quickjava.www.test.TestDb;

@ApplicationQuickBoot
public class ApplicationBoot {

    public static void main(String[] args)
    {
        Kernel.init(ApplicationBoot.class, args);
        new TestDb().testQueryOOM();
//        new TestDb().testTransactionOOM();
//        QuickJavaBoot.start(ApplicationBoot.class, args);
    }

}
