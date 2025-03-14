/*
 * Copyright (c) 2021.
 * More Info to http://www.quickjava.org
 */

package org.quickjava.www.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.quickjava.orm.query.enums.OrderByType;
import org.quickjava.web.common.QuickUtil;
import org.quickjava.web.framework.QuickJavaRunner;
import org.quickjava.orm.query.QuerySet;
import org.quickjava.orm.query.enums.Operator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
        int result = QuerySet.table("qj_user").insertAll(rows);
        System.out.println("INSERT.return=" + result);

        System.out.println("耗时=" + QuickUtil.endNanoTimeMS(startTime) + "ms");
    }

    /**
     * 测试数据库ORM操作类
     */
    @Test
    public void testSelect() {
        testGenerateData();
        logger.info("result: \n{}", QuerySet.table("qj_user").where("id", 1).select());
        logger.info("result: \n{}", QuerySet.table("qj_user").where("id", 1).find());

        logger.info("result: \n{}", QuerySet.table("qj_user").where("id", Operator.GTE, 0).select());

        logger.info("result: \n{}", QuerySet.table("qj_user")
                .where("id", Operator.GTE, 0)
                .where("status", Operator.EQ, 1)
                .select()
        );

        logger.info("result: \n{}", QuerySet.table("qj_user")
                .where("id", Operator.GTE, 0)
                .between("status", 1, 2)
                .select()
        );

        // 全条件查询
        QuerySet querySet = QuerySet.table("qj_user")
                .where("id", Operator.GTE, 0)
                .between("status", 1, 2)
                .order("id DESC")
                .order("id", OrderByType.DESC)
                .order("id", "DESC")
                .order("id", true)
                .order("age")
                .order("status ASC");
        logger.info("result: \n{}", querySet.select());
        logger.info("result: \n{}", querySet.page(1).select());
        logger.info("result: \n{}", querySet.page(1, 20).select());
        logger.info("result: \n{}", querySet.pagination(1));
        logger.info("result: \n{}", querySet.pagination(1, 20));
    }

    /**
     * 测试 内存泄漏
     */
    @Test
    public void testQuerySetOOM() {
        for (int i = 0; i < 3000; i++) {
            System.out.println("第" + i + "次");
            testSelect();
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
        ExecutorService executor = Executors.newFixedThreadPool(30);
        for (int i = 0; i < 300; i++) {
            int finalI = i;
            executor.submit(() -> {
                System.out.println("第" + finalI + "次");
                testTransaction();
            });
        }
        executor.shutdown();

        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
