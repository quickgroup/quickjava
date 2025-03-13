package org.quickjava.www;

import org.quickjava.framework.Kernel;
import org.quickjava.framework.annotation.ApplicationQuickBoot;
import org.quickjava.www.test.TestDb;

@ApplicationQuickBoot
public class ApplicationBoot {

    public static void main(String[] args)
    {
        Kernel.init(ApplicationBoot.class, args);
        new TestDb().testTransactionMemoryLeakage();
//        QuickJavaBoot.start(ApplicationBoot.class, args);
    }

}
