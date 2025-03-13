package org.quickjava.www;

import org.quickjava.framework.QuickJavaBoot;
import org.quickjava.framework.annotation.ApplicationQuickBoot;
import org.quickjava.orm.example.Article;
import org.quickjava.orm.query.QuerySet;
import org.quickjava.orm.query.enums.Operator;

@ApplicationQuickBoot
public class ApplicationBoot {

    public static void main(String[] args)
    {

        // 测试字段名称转换
//        System.out.println("userTypeText=" + ComUtil.toCamelCase("userTypeText"));
//        System.out.println("USER_TYPE_TEXT=" + ComUtil.toCamelCase("USER_TYPE_TEXT"));
//        System.out.println("user_type_Text=" + ComUtil.toCamelCase("user_Type_text"));
//        System.out.println("user_type_text=" + ComUtil.toCamelCase("user_type_text"));
//        System.out.println("userTypeText=" + ComUtil.toUnderlineCase("userTypeText"));
//        System.out.println("USER_TYPE_TEXT=" + ComUtil.toUnderlineCase("USER_TYPE_TEXT"));
//        System.out.println("user_type_Text=" + ComUtil.toUnderlineCase("user_type_Text"));

        // 测试方法引用传递
//        System.out.println("article=" + new ModelQuery<>(Article.class).eq(Article::getId, 1));
//        System.out.println("article=" + ModelQuery.lambda(Article.class).eq(Article::getId, 1));
//        System.out.println("article=" + ModelQuery.lambda(Article.class).eq(Article::getId, 1).eq(Article::user, 1));

        // querySet闭包查询
        System.out.println("article=" + QuerySet.table("article").where("status", 1)
                .where("remark", Operator.LIKE,"%备注内容%") .where(query -> {
                    query.where("id", 20).where("user_id", 10);
                }).whereOr(query -> {
                    query.where("id", 21).where("user_id", 11);
                }).buildSql());

        // 模型闭包查询
//        System.out.println("article.select.sql=" + new Article().where("status", 1).where(query -> {
//            query.where("id", 200).where("user_id", 100);
//        }).whereOr(query -> {
//            query.where("id", 210).where("user_id", 110);
//            query.whereOr(query1 -> {
//                query1.where("id", 211).where("user_id", 111);
//            });
//            query.whereOr(query1 -> {
//                query1.where("id", 212).where("user_id", 112);
//            });
//        }).fetchSql(true).select());
//
//        System.out.println("article.find.sql=" + new Article().where("status", 1).where(query -> {
//            query.where("id", 200).where("user_id", 100);
//        }).whereOr(query -> {
//            query.where("id", 210).where("user_id", 110);
//            query.whereOr(query1 -> {
//                query1.where("id", 211).where("user_id", 111);
//            });
//            query.whereOr(query1 -> {
//                query1.where("id", 212).where("user_id", 112);
//            });
//        }).fetchSql(true).find());
//
//        System.out.println("insert.sql=" + new Article().fetchSql(true).insert());

        System.out.println("update.sql=" + new Article().where("id", 211).data("id", 123).fetchSql(true).update());

        System.out.println("update.sql=" + new Article().where("id", 211).data("id", 123).fetchSql(true).pagination(1, 10));

        QuickJavaBoot.start(ApplicationBoot.class, args);
    }

}
