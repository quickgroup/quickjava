package org.quickjava.www;

import org.quickjava.common.utils.ComUtil;
import org.quickjava.framework.QuickJavaBoot;
import org.quickjava.framework.annotation.ApplicationQuickBoot;
import org.quickjava.orm.Model;
import org.quickjava.orm.ModelQuery;
import org.quickjava.orm.example.Article;

import java.util.function.Supplier;

@ApplicationQuickBoot
public class ApplicationBoot {

    public static void main(String[] args)
    {
        QuickJavaBoot.start(ApplicationBoot.class, args);

        // 测试字段名称转换
//        System.out.println("userTypeText=" + ComUtil.toCamelCase("userTypeText"));
//        System.out.println("USER_TYPE_TEXT=" + ComUtil.toCamelCase("USER_TYPE_TEXT"));
//        System.out.println("user_type_Text=" + ComUtil.toCamelCase("user_Type_text"));
//        System.out.println("user_type_text=" + ComUtil.toCamelCase("user_type_text"));
//        System.out.println("userTypeText=" + ComUtil.toUnderlineCase("userTypeText"));
//        System.out.println("USER_TYPE_TEXT=" + ComUtil.toUnderlineCase("USER_TYPE_TEXT"));
//        System.out.println("user_type_Text=" + ComUtil.toUnderlineCase("user_type_Text"));

        // 测试方法引用传递
        System.out.println("article=" + new Article().where("id", 1));
//        System.out.println("article=" + new Article().eq(Article::getId, 1).find());
        System.out.println("article=" + new ModelQuery<Article>().eq(Article::getId, 1));
    }

}
