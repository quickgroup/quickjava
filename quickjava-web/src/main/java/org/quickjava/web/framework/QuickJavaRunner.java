/*
 * Copyright (c) 2020~2021 http://www.quickjava.org All rights reserved.
 * +-------------------------------------------------------------------
 * Org: QuickJava
 * +-------------------------------------------------------------------
 * Author: Qlo1062
 * +-------------------------------------------------------------------
 * File: QuickJavaTestBoot.java
 * +-------------------------------------------------------------------
 * Date: 2021/11/17 10:19:17
 * +-------------------------------------------------------------------
 * License: Apache Licence 2.0
 * +-------------------------------------------------------------------
 *
 */

package org.quickjava.web.framework;

import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;
import org.quickjava.web.framework.annotation.ApplicationQuickBoot;

@ApplicationQuickBoot
public class QuickJavaRunner extends BlockJUnit4ClassRunner {

    public QuickJavaRunner(Class<?> clazz) throws InitializationError {
        super(clazz);
    }

    @Override
    public void run(RunNotifier runNotifier) {
        System.out.println("run");
        Kernel.init(QuickJavaRunner.class, new String[]{});
        super.run(runNotifier);
    }
}
