package org.quickjava.spring;

import cn.hutool.extra.spring.EnableSpringUtil;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/*
 * Copyright (c) 2020~2023 http://www.quickjava.org All rights reserved.
 * +-------------------------------------------------------------------
 * Organization: QuickJava
 * +-------------------------------------------------------------------
 * Author: Qlo1062
 * +-------------------------------------------------------------------
 * File: SsoClientTestApplication
 * +-------------------------------------------------------------------
 * Date: 2023/10/23 17:32
 * +-------------------------------------------------------------------
 * License: Apache Licence 2.0
 * +-------------------------------------------------------------------
 */
@EnableSpringUtil
@SpringBootApplication
public class SpringApplication {

    public static void main(String[] args) {
        org.springframework.boot.SpringApplication.run(SpringApplication.class, args);
        System.out.println("### ClientTest 启动成功 ###");
    }

}
