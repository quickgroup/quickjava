package org.quickjava.orm.drive;

import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/*
 * Copyright (c) 2020~2023 http://www.quickjava.org All rights reserved.
 * +-------------------------------------------------------------------
 * Organization: QuickJava
 * +-------------------------------------------------------------------
 * Author: Qlo1062
 * +-------------------------------------------------------------------
 * File: SpringAutoConfiguration
 * +-------------------------------------------------------------------
 * Date: 2023-4-28 15:39
 * +-------------------------------------------------------------------
 * License: Apache Licence 2.0
 * +-------------------------------------------------------------------
 */
@Configuration
@ConditionalOnSingleCandidate(DataSource.class)
public class SpringAutoConfiguration implements InitializingBean {

    private static ApplicationContext applicationContext;

    public SpringAutoConfiguration(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
