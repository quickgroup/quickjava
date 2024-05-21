package org.quickjava.orm.model;/*
 * Copyright (c) 2020~2024 http://www.quickjava.org All rights reserved.
 * +-------------------------------------------------------------------
 * Organization: QuickJava
 * +-------------------------------------------------------------------
 * Author: Qlo1062
 * +-------------------------------------------------------------------
 * File: ModelFieldHepler
 * +-------------------------------------------------------------------
 * Date: 2024/5/21 17:28
 * +-------------------------------------------------------------------
 * License: Apache Licence 2.0
 * +-------------------------------------------------------------------
 */

import org.quickjava.orm.model.contain.ModelFieldMeta;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

public class ModelFieldHelper {

    /**
     * 常见数据类型填充
     */
    public static Object fillValue(ModelFieldMeta fieldMeta) {
        if (Date.class.isAssignableFrom(fieldMeta.getField().getType())) {
            return new Date();
        } else if (java.sql.Date.class.isAssignableFrom(fieldMeta.getField().getType())) {
            return new java.sql.Date(System.currentTimeMillis());
        }  else if (java.sql.Timestamp.class.isAssignableFrom(fieldMeta.getField().getType())) {
            return new java.sql.Timestamp(System.currentTimeMillis());
        } else if (LocalDate.class.isAssignableFrom(fieldMeta.getField().getType())) {
            return LocalDate.now();
        } else if (LocalDateTime.class.isAssignableFrom(fieldMeta.getField().getType())) {
            return LocalDateTime.now();
        } else if (LocalTime.class.isAssignableFrom(fieldMeta.getField().getType())) {
            return LocalTime.now();
        } else if (Integer.class.isAssignableFrom(fieldMeta.getField().getType())) {
            return 1;
        } else if (Long.class.isAssignableFrom(fieldMeta.getField().getType())) {
            return 1;
        } else if (Float.class.isAssignableFrom(fieldMeta.getField().getType())) {
            return 1.0;
        } else if (Double.class.isAssignableFrom(fieldMeta.getField().getType())) {
            return 1.0;
        } else if (String.class.isAssignableFrom(fieldMeta.getField().getType())) {
            return "1";
        } else if (Boolean.class.isAssignableFrom(fieldMeta.getField().getType())) {
            return true;
        }
        return null;
    }
}
