package org.quickjava.www;

import org.quickjava.common.utils.ComUtil;
import org.quickjava.framework.QuickJavaBoot;
import org.quickjava.framework.annotation.ApplicationQuickBoot;
import org.quickjava.orm.Model;
import org.quickjava.orm.ModelQuery;
import org.quickjava.orm.example.Article;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.function.Supplier;

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
        System.out.println("article=" + new Article().where("id", 1));

        System.out.println("article=" + new ModelQuery<>(Article.class).eq(Article::getId, 1));
        System.out.println("article=" + ModelQuery.lambda(Article.class).eq(Article::getId, 1));

        QuickJavaBoot.start(ApplicationBoot.class, args);
    }

    public static Class<?> getGenericType(Object obj) {
        Type genericSuperclass = obj.getClass().getGenericSuperclass();
        if (genericSuperclass instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;
            Type[] typeArguments = parameterizedType.getActualTypeArguments();
            if (typeArguments.length > 0) {
                Type typeArgument = typeArguments[0];
                if (typeArgument instanceof Class) {
                    return (Class<?>) typeArgument;
                }
            }
        }
        return null;
    }

}
