package org.quickjava.orm.modelQuery;

import org.quickjava.orm.utils.ModelUtil;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import java.beans.Introspector;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class FunctionReflectionUtil {

    private static final Map<Function<?, ?>, Field> cache = new ConcurrentHashMap<>();

    public static <T, R> String getFieldName(Function<T, R> function) {
        Field field = FunctionReflectionUtil.getField(function);
        return field.getName();
    }

    public static <T, R> Field getField(Function<T, R> function) {
        return cache.computeIfAbsent(function, FunctionReflectionUtil::findField);
    }

    public static <T, R> Field findField(Function<T, R> function) {
        try {
            String fieldName = null;
            // 第1步 获取SerializedLambda
            Method method = function.getClass().getDeclaredMethod("writeReplace");
            method.setAccessible(Boolean.TRUE);
            SerializedLambda serializedLambda = (SerializedLambda) method.invoke(function);
            // 第2步 implMethodName 即为Field对应的Getter方法名
            String implMethodName = serializedLambda.getImplMethodName();
            if (implMethodName.startsWith("get") && implMethodName.length() > 3) {
                fieldName = Introspector.decapitalize(implMethodName.substring(3));

            } else if (implMethodName.startsWith("is") && implMethodName.length() > 2) {
                fieldName = Introspector.decapitalize(implMethodName.substring(2));
            } else if (implMethodName.startsWith("lambda$")) {
                throw new IllegalArgumentException("SerializableFunction不能传递lambda表达式,只能使用方法引用");

            } else {
                throw new IllegalArgumentException(implMethodName + "不是Getter方法引用");
            }
            // 第3步 获取的Class是字符串，并且包名是“/”分割，需要替换成“.”，才能获取到对应的Class对象
            String declaredClass = serializedLambda.getImplClass().replace("/", ".");
            Class<?> aClass = Class.forName(declaredClass, false, ClassUtils.getDefaultClassLoader());

            // 第4步  Spring 中的反射工具类获取Class中定义的Field
            return ReflectionUtils.findField(aClass, fieldName);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static <T, R> String findFieldName(Function<T, R> function) {
        Field field = findField(function);
        return ModelUtil.toUnderlineCase(field.getName());
    }

}
