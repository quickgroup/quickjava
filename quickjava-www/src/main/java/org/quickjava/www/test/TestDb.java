/*
 * Copyright (c) 2021.
 * More Info to http://www.quickjava.org
 */

package org.quickjava.www.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.quickjava.web.common.QuickUtil;
import org.quickjava.web.framework.QuickJavaRunner;
import org.quickjava.orm.query.QuerySet;
import org.quickjava.orm.query.enums.Operator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RunWith(QuickJavaRunner.class)
public class TestDb {
    protected static final Logger logger = LoggerFactory.getLogger(TestDb.class);

    /**
     * 测试数据库ORM操作类
     */
    @Test
    public void testGenerateData() {
        // 先删除所有数据
        QuerySet.table("qj_user").where("ID IS NOT NULL", Operator.RAW).delete();

        // NOTE::查询
        QuerySet.table("qj_user").select();

        Long startTime = QuickUtil.getNanoTime();

        // NOTE::新增
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("username", "123456");
        data.put("password", "pwd123");
        data.put("name", "longlong");
        data.put("age", 12);
        data.put("email", "123456@qq.com");
        data.put("create_time", new Date());
        data.put("update_time", new Date());

        for (int i = 1; i <= 10; i++) {
            data.put("id", i);
//            int result = QuerySet.table("qj_user").insert(data);
            Long result = QuerySet.table("qj_user").insertGetId(data);
            System.out.println("INSERT.return=" + result);
        }

        System.out.println("耗时=" + QuickUtil.endNanoTimeMS(startTime) + "ms");
    }

    /**
     * 测试数据库ORM操作类
     */
    @Test
    public void testQuery() {
        // 先删除所有数据
        QuerySet.table("qj_user").whereRaw("id IS NOT NULL").delete();

        // NOTE::查询
        QuerySet.table("qj_user").field("id").select();

        Long startTime = QuickUtil.getNanoTime();

        // NOTE::新增
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("username", "123456");
        data.put("password", "pwd123");
        data.put("name", "longlong");
        data.put("age", 12);
        data.put("email", "123456@qq.com");
        data.put("create_time", new Date());
        data.put("update_time", new Date());
        Integer result = QuerySet.table("qj_user").insert(data);
        System.out.println("INSERT.return=" + result);

        System.out.println("耗时=" + QuickUtil.endNanoTimeMS(startTime) + "ms");
        startTime = QuickUtil.getNanoTime();

        // NOTE::删除
        Integer number = QuerySet.table("qj_user").where("id", 1).where("status", 1).delete();

        System.out.println("DELETE.return=" + number);
        System.out.println("耗时=" + QuickUtil.endNanoTimeMS(startTime) + "ms");
        startTime = QuickUtil.getNanoTime();

        Map<String, Object> updateData = new LinkedHashMap<>();
        updateData.put("name", "xiaolong");
        updateData.put("age", 15);

        // NOTE::更新
        number = QuerySet.table("qj_user").where("name", "longlong").update(updateData);

        System.out.println("UPDATE.return=" + number);
        System.out.println("耗时=" + QuickUtil.endNanoTimeMS(startTime) + "ms");

        // 查
        List<Map<String, Object>> rows = QuerySet.table("qj_user").where("name", "xiaolong")
                .order("create_time", "DESC")
                .select();
        System.out.println("SELECT.return=" + rows);
    }

    /**
     * 测试 内存泄漏
     */
    @Test
    public void testQueryOOM() {
        for (int i = 0; i < 3000; i++) {
            System.out.println("第" + i + "次");
            testQuery();
        }
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 测试事务操作
     */
    @Test
    public void testTransaction() {
        QuerySet.transaction(() -> {
            // NOTE::查询
            QuerySet.table("qj_user").where("id", Operator.GTE, 0).select();
        });

        // 事务提交
        QuerySet.transaction(() -> {
            // NOTE::查询
            QuerySet.table("qj_user").where("id", Operator.GTE, 0).select();
            // NOTE::更新
            Map<String, Object> updateData = new LinkedHashMap<>();
            updateData.put("name", "xiaolong");
            updateData.put("age", 15);
            QuerySet.table("qj_user").where("id", 5).update(updateData);
            // NOTE::查询
            QuerySet.table("qj_user").field("id").select();
            // NOTE::查询
            QuerySet.table("qj_user").field("id").select();
        });

        // 事务回滚
        try {
            QuerySet.transaction(() -> {
                // NOTE::查询
                QuerySet.table("qj_user").field("id").select();
                // NOTE::更新
                Map<String, Object> updateData = new LinkedHashMap<>();
                updateData.put("name", "xiaolong");
                updateData.put("age", 15);
                QuerySet.table("qj_user").where("id", 1).update(updateData);
                // NOTE::查询
                QuerySet.table("qj_user").field("id").select();

                throw new RuntimeException("打断事物提交");
            });
        } catch (Exception e) {
            logger.info("测试出异常: {}", e.getMessage());
        }
    }

    /**
     * 测试 内存泄漏
     */
    @Test
    public void testTransactionOOM() {
        for (int i = 0; i < 3000; i++) {
            System.out.println("第" + i + "次");
            testTransaction();
        }
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
