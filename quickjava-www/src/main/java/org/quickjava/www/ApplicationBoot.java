package org.quickjava.www;

import org.quickjava.framework.QuickJavaBoot;
import org.quickjava.framework.annotation.ApplicationQuickBoot;
import org.quickjava.orm.QuerySet;
import org.quickjava.orm.modelQuery.ModelQuery;
import org.quickjava.orm.example.Article;
import org.quickjava.orm.utils.WhereCallback;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

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

        // 闭包查询
        System.out.println("article=" + QuerySet.table("article").where("status", 1).where(query -> {
            query.where("id", 20).where("user_id", 10);
        }).whereOr(query -> {
            query.where("id", 21).where("user_id", 11);
        }).fetchSql());

        QuickJavaBoot.start(ApplicationBoot.class, args);
    }

}
