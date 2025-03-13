///*
// * Copyright (c) 2021.
// * More Info to http://www.quickjava.org
// */
//
//package org.quickjava.spring;
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.quickjava.common.utils.TimeUtils;
//import org.quickjava.orm.contain.DataMap;
//import org.quickjava.orm.model.Model;
//import org.quickjava.orm.model.callback.WhereClosure;
//import org.quickjava.orm.model.enums.RelationType;
//import org.quickjava.orm.query.QuerySet;
//import org.quickjava.orm.query.enums.OrderByType;
//import org.quickjava.spring.domain.Article;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.util.*;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class TestModel {
//
//    /**
//     * 测试数据库ORM操作类
//     */
//    @Test
//    public void test1()
//    {
//        Map<String, Object> data = new LinkedHashMap<>();
//        data.put("account", "123456");
//        data.put("password", "pwd123");
//        data.put("name", "longlong");
//        data.put("age", 12);
//        data.put("email", "123456@qq.com");
//        data.put("create_time", new Date());
//        data.put("update_time", new Date());
//
//        // NOTE::查询
//        QuerySet.table("user").field("id").select();
//
//        Long startTime = TimeUtils.getNanoTime();
//
//        // NOTE::新增
//        Long result = QuerySet.table("user").insert(data);
//
//        System.out.println("INSERT.return=" + result);
//        System.out.println("耗时=" + TimeUtils.endNanoTime(startTime) + "ms");
//        startTime = TimeUtils.getNanoTime();
//
//        // NOTE::删除
//        Integer number = QuerySet.table("user").where("id", 1).where("status", 1).delete();
//
//        System.out.println("DELETE.return=" + number);
//        System.out.println("耗时=" + TimeUtils.endNanoTime(startTime) + "ms");
//        startTime = TimeUtils.getNanoTime();
//
//        Map<String, Object> updateData = new LinkedHashMap<>();
//        updateData.put("name", "xiaolong");
//        updateData.put("age", 15);
//
//        // NOTE::更新
//        number = QuerySet.table("user").where("name", "longlong").update(updateData);
//
//        System.out.println("UPDATE.return=" + number);
//        System.out.println("耗时=" + TimeUtils.endNanoTime(startTime) + "ms");
//
//        // 查
//        List<Map<String, Object>> rows = QuerySet.table("user").where("name", "xiaolong")
//                .order("create_time", OrderByType.DESC).select();
//        System.out.println("SELECT.return=" + rows);
//    }
//
//    @Test
//    public void test2()
//    {
//        Object ret = new Model()
//                .eq("1", 1)
//                .neq("1", 1)
//                .gt("1", 1)
//                .gte("1", 1)
//                .lt("1", 1)
//                .lte("1", 1)
//                .in("1", 1)
//                .notIn("1", 1)
//                .isNull("1")
//                .isNotNull("1")
//                .between("1", 1, 2)
//                .find();
//
//        ret = new Model()
//                .where(query -> { })
//                .whereOr(query -> { })
//                .find();
//
//        ret = new Model().save();
//        ret = new Model().create(new LinkedHashMap<>());
//        ret = new Model().create(new DataMap());
//
//        ret = new Model().update();
//        ret = new Model().updateById();
//        ret = new Model().find();
//        ret = new Model().find(1);
//        ret = new Model().select();
//        ret = new Model().limit(1L, 20L).select();
//        ret = new Model().limit(1L).select();
//        ret = new Model().page(1L, 20L).select();
//        ret = new Model().page(1L).select();
//        ret = new Model().pagination(1L, 20L);
//        ret = new Model().pagination();
//        ret = new Model().order("1").find();
//        ret = new Model().order("1", OrderByType.DESC).find();
//        ret = new Model().order("1", false).find();
//        ret = new Model().order(new LinkedList<>()).find();
//        ret = new Model().order(new String[]{}).find();
//
//        ret = new Model().lock(true).find();
//        ret = new Model().distinct(true).fetchSql(true);
//
//        ret = new Model().pk();
//        ret = new Model().pkVal();
//
//        ret = new Model().group("a").find();
//        ret = new Model().having("a").find();
//        ret = new Model().union("a").find();
//        ret = new Model().union(new String[]{}).find();
//
//        ret = new Model().relation(Model.class, RelationType.OneToOne, "id", "appId").find();
//        ret = new Article().relation(Article.class, RelationType.OneToOne, "id", "appId").find();
//        ret = new Model().relation(Model.class.getName(), RelationType.OneToOne, "id", "appId");
//
//        new Model().delete();
//
//        System.out.println("test");
//    }
//
//}
