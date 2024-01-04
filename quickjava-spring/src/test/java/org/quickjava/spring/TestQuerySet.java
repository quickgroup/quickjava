/*
 * Copyright (c) 2021.
 * More Info to http://www.quickjava.org
 */

package org.quickjava.spring;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.quickjava.common.utils.TimeUtils;
import org.quickjava.orm.QuerySet;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestQuerySet {

    /**
     * 测试数据库ORM操作类
     */
    @Test
    public void test1()
    {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("account", "123456");
        data.put("password", "pwd123");
        data.put("name", "longlong");
        data.put("age", 12);
        data.put("email", "123456@qq.com");
        data.put("create_time", new Date());
        data.put("update_time", new Date());

        // TODO::查询
        QuerySet.table("user").field("id").select();

        Long startTime = TimeUtils.getNanoTime();

        // TODO::新增
        Long result = QuerySet.table("user").insert(data);

        System.out.println("INSERT.return=" + result);
        System.out.println("耗时=" + TimeUtils.endNanoTime(startTime) + "ms");
        startTime = TimeUtils.getNanoTime();

        // TODO::删除
        Integer number = QuerySet.table("user").where("id", 1).where("status", 1).delete();

        System.out.println("DELETE.return=" + number);
        System.out.println("耗时=" + TimeUtils.endNanoTime(startTime) + "ms");
        startTime = TimeUtils.getNanoTime();

        Map<String, Object> updateData = new LinkedHashMap<>();
        updateData.put("name", "xiaolong");
        updateData.put("age", 15);

        // TODO::更新
        number = QuerySet.table("user").where("name", "longlong").update(updateData);

        System.out.println("UPDATE.return=" + number);
        System.out.println("耗时=" + TimeUtils.endNanoTime(startTime) + "ms");

        // 查
        List<Map<String, Object>> rows = QuerySet.table("user").where("name", "xiaolong").order("create_time", "DESC").select();
        System.out.println("SELECT.return=" + rows);
    }

    /**
     * 表别名
     * JOIN查询
     */
    @Test
    public void test2()
    {
        // JOIN
//        Q.table("user")
//                .where("account", "longlong")
//                .where("status", 0)
//                .join("user_address", "user_address.user_id = user.id", "LEFT")
//                .join("user_order", "user_order.user_id = user.id", "LEFT")
//                .order("create_time", "DESC")
//                .select();
    }
}
