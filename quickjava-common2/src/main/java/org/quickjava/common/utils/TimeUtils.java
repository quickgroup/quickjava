package org.quickjava.common.utils;

/*
 * Copyright (c) 2020~2024 http://www.quickjava.org All rights reserved.
 * +-------------------------------------------------------------------
 * Organization: QuickJava
 * +-------------------------------------------------------------------
 * Author: Qlo1062
 * +-------------------------------------------------------------------
 * File: TimeUtils
 * +-------------------------------------------------------------------
 * Date: 2024/1/4 15:44
 * +-------------------------------------------------------------------
 * License: Apache Licence 2.0
 * +-------------------------------------------------------------------
 */
public class TimeUtils {

    /**
     * 获取纳秒（不是纳秒级时间戳
     */
    public static Long getNanoTime() {
        return System.nanoTime();
    }

    /**
     * 获取纳秒间隔时间
     */
    public static double endNanoTime(Long startTime) {
        Long endTime = System.nanoTime();
        return ((double) (endTime - startTime)) / 1000000;
    }

}
