/*
 * Copyright (c) 2021.
 * More Info to http://www.quickjava.org
 */

package org.quickjava.www.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.quickjava.orm.query.enums.Operator;
import org.quickjava.orm.wrapper.ModelWrapper;
import org.quickjava.web.common.QuickUtil;
import org.quickjava.web.framework.QuickJavaRunner;
import org.quickjava.www.application.common.model.UserModel;

import java.util.*;

@RunWith(QuickJavaRunner.class)
public class TestWrapper {

    @Test
    public void testGenerateData() {
        // 先删除所有数据
        ModelWrapper.lambda(UserModel.class).where("id IS NOT NULL", Operator.RAW).delete();

        Long startTime = QuickUtil.getNanoTime();
        // NOTE::新增
        List<Map<String, Object>> rows = new LinkedList<>();
        for (int i = 1; i <= 10; i++) {
            Map<String, Object> data = new LinkedHashMap<>();
            data.put("id", i);
            data.put("username", "123456");
            data.put("password", "pwd123");
            data.put("name", "longlong");
            data.put("age", 12);
            data.put("email", "123456@qq.com");
            data.put("status", 1);
            data.put("create_time", new Date());
            data.put("update_time", new Date());
            rows.add(data);
        }
        int result = ModelWrapper.lambda(UserModel.class).insertAll(rows);
        System.out.println("INSERT.return=" + result);

        System.out.println("耗时=" + QuickUtil.endNanoTimeMS(startTime) + "ms");
    }

}
