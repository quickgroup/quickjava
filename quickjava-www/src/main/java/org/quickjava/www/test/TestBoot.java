/*
 * Copyright (c) 2021.
 * More Info to http://www.quickjava.org
 */

package org.quickjava.www.test;

import org.junit.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestBoot {
    private static final Logger logger = LoggerFactory.getLogger(TestBoot.class);

    @BeforeClass
    public static void start()
    {
        logger.info("TestBoot");
        logger.info("Init App");
    }
}
