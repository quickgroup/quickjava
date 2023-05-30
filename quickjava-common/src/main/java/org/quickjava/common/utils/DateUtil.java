package org.quickjava.common.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/*
 * Copyright (c) 2020~2023 http://www.quickjava.org All rights reserved.
 * +-------------------------------------------------------------------
 * Organization: QuickJava
 * +-------------------------------------------------------------------
 * Author: Qlo1062
 * +-------------------------------------------------------------------
 * File: DateUtil
 * +-------------------------------------------------------------------
 * Date: 2023-5-30 17:44
 * +-------------------------------------------------------------------
 * License: Apache Licence 2.0
 * +-------------------------------------------------------------------
 */
public class DateUtil {

    public static final String FORMAT_DEF = "yyyy-MM-dd HH:mm:ss";

    public static final String FORMAT_DATE = "yyyy-MM-dd";
    public static final String FORMAT_YEAR = "yyyy";
    public static final String FORMAT_MONTH = "MM";
    public static final String FORMAT_YEAR_MONTH = "yyyy-MM";
    public static final String FORMAT_DAY = "dd";

    public static final String FORMAT_TIME = "HH:mm:ss";
    public static final String FORMAT_HOUR = "HH";
    public static final String FORMAT_MINUTE = "mm";
    public static final String FORMAT_HOUR_MINUTE = "HH:mm";
    public static final String FORMAT_SECOND = "ss";

    public static String formatDateTime(Date date) {
        return formatDateTime(date, FORMAT_DEF);
    }

    public static String formatDateTime(Date date, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }

}
