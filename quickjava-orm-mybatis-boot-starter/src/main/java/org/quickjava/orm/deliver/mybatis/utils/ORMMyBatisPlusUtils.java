package org.quickjava.orm.deliver.mybatis.utils;/*
 * Copyright (c) 2020~2024 http://www.quickjava.org All rights reserved.
 * +-------------------------------------------------------------------
 * Organization: QuickJava
 * +-------------------------------------------------------------------
 * Author: Qlo1062
 * +-------------------------------------------------------------------
 * File: ORMMyBatisPlusUtils
 * +-------------------------------------------------------------------
 * Date: 2024/5/22 9:53
 * +-------------------------------------------------------------------
 * License: Apache Licence 2.0
 * +-------------------------------------------------------------------
 */

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.quickjava.orm.contain.IPagination;
import org.quickjava.orm.contain.Pagination;

/**
 * MyBatisPlus助手方法
 */
public class ORMMyBatisPlusUtils {

    /**
     * IPage 转 Pagination
     */
    public static<T> IPagination<T> convPagination(IPage<T> page) {
        return new Pagination<>(page.getCurrent(), page.getSize(), page.getTotal(), page.getRecords());
    }
}
