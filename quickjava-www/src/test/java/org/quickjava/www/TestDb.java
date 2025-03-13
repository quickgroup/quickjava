/*
 * Copyright (c) 2021.
 * More Info to http://www.quickjava.org
 */

package org.quickjava.www;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.quickjava.common.QuickUtil;
import org.quickjava.framework.QuickJavaRunner;
import org.quickjava.orm.query.QuerySet;
import org.quickjava.orm.query.enums.Operator;

import java.util.*;

@RunWith(QuickJavaRunner.class)
public class TestDb {

    /**
     * 测试数据库ORM操作类
     */
    @Test
    public void test1()
    {
        // 先删除所有数据
        QuerySet.table("qj_user").where("id", Operator.RAW, "IS NOT NULL").delete();

        // TODO::查询
        QuerySet.table("qj_user").field("id").select();

        Long startTime = QuickUtil.getNanoTime();

        // TODO::新增
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("username", "123456");
        data.put("password", "pwd123");
        data.put("name", "longlong");
        data.put("age", 12);
        data.put("email", "123456@qq.com");
        data.put("create_time", new Date());
        data.put("update_time", new Date());
        Long result = QuerySet.table("qj_user").insert(data);

        System.out.println("INSERT.return=" + result);
        System.out.println("耗时=" + QuickUtil.endNanoTimeMS(startTime) + "ms");
        startTime = QuickUtil.getNanoTime();

        // TODO::删除
        Integer number = QuerySet.table("qj_user").where("id", 1).where("status", 1).delete();

        System.out.println("DELETE.return=" + number);
        System.out.println("耗时=" + QuickUtil.endNanoTimeMS(startTime) + "ms");
        startTime = QuickUtil.getNanoTime();

        Map<String, Object> updateData = new LinkedHashMap<>();
        updateData.put("name", "xiaolong");
        updateData.put("age", 15);

        // TODO::更新
        number = QuerySet.table("qj_user").where("name", "longlong").update(updateData);

        System.out.println("UPDATE.return=" + number);
        System.out.println("耗时=" + QuickUtil.endNanoTimeMS(startTime) + "ms");

        // 查
        List<Map<String, Object>> rows = QuerySet.table("qj_user").where("name", "xiaolong").order("create_time", "DESC").select();
        System.out.println("SELECT.return=" + rows);
    }

    /**
     * 测试事物操作
     */
    @Test
    public void testTransaction() {
        QuerySet.transaction(() -> {
            // TODO::查询
            QuerySet.table("qj_user").field("id").select();
        });

        QuerySet.transaction(() -> {
            // TODO::查询
            QuerySet.table("qj_user").field("id").select();
            // TODO::更新
            Map<String, Object> updateData = new LinkedHashMap<>();
            updateData.put("name", "xiaolong");
            updateData.put("age", 15);
            QuerySet.table("qj_user").where("id", "longlong").update(updateData);
            // TODO::查询
            QuerySet.table("qj_user").field("id").select();
        });
    }

}
