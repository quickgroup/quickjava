package org.quickjava.orm.example;

import org.quickjava.orm.query.QuerySet;

import java.util.Map;

/*
 * Copyright (c) 2020~2023 http://www.quickjava.org All rights reserved.
 * +-------------------------------------------------------------------
 * Organization: QuickJava
 * +-------------------------------------------------------------------
 * Author: Qlo1062
 * +-------------------------------------------------------------------
 * File: Test
 * +-------------------------------------------------------------------
 * Date: 2023-3-8 16:56
 * +-------------------------------------------------------------------
 * License: Apache Licence 2.0
 * +-------------------------------------------------------------------
 */
public class Test {

    public void testQuery() {
        Map row = QuerySet.table("article").where("id", 1).find();
        System.out.println("row=" + row);

        // join
        Map row2 = QuerySet.table("article")
                .join("user", "user.id = article.user_id")
                .where("article.id", 1)
                .find();
        System.out.println("row2=" + row2);
    }

    public void testModel() {
        // 预载入查询
        Article article3 = new Article()
                .with("user,comments,tags")
                .where("id", 1)
                .find();
        System.out.println("article3=" + article3);
        System.out.println("article3.user=" + article3.getUser());
        System.out.println("article3.getComments=" + article3.getComments());
        System.out.println("article3.getTags=" + article3.getTags());

        // 预载入查询
//        List<Article> article4 = new Article()
//                .with("user,comments,tags")
//                .select();
//        System.out.println("article4=" + article4);
//
//        // 预载入分页
//        Pagination<Article> pagination = new Article()
//                .with("user,comments,tags")
//                .pagination();
//        System.out.println("pagination=" + pagination);
    }

    public void test()
    {
        testModel();

        // 查询用户
//        User user1 = userModel.where().find();

        // 查询文章
//        Article article1 = new Article().find();
//        System.out.println("article1=" + article1);
//
//        // 懒加载 v1版本
//        Article article2 = new Article().where("id", 1).find();
//        System.out.println("article2=" + article2);
//        System.out.println("article2.user=" + article2.user().find());
//        System.out.println("article2.users=" + article2.users().select());

//        // 预载入查询
//        Article article3 = new Article().with("user").where("id", 1).find();
//        System.out.println("article3=" + article3);
//        System.out.println("article3.user=" + article3.getUser());
//        System.out.println("article3.users=" + article3.getUsers());
    }

}
