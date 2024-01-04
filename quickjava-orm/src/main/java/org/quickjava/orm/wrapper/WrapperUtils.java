package org.quickjava.orm.wrapper;

import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import java.beans.Introspector;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class WrapperUtils {

    private static final Map<Function<?, ?>, Field> cache = new ConcurrentHashMap<>();

    public static <T, R> String getFieldName(Function<T, R> function) {
        Field field = WrapperUtils.getField(function);
        return field.getName();
    }

    public static <T, R> Field getField(Function<T, R> function) {
        return cache.computeIfAbsent(function, WrapperUtils::findField);
    }

    public static <T, R> Field findField(Function<T, R> function) {
        try {
            String fieldName;

            Method method = function.getClass().getDeclaredMethod("writeReplace");
            method.setAccessible(Boolean.TRUE);
            SerializedLambda serializedLambda = (SerializedLambda) method.invoke(function);

            String implMethodName = serializedLambda.getImplMethodName();
            if (implMethodName.startsWith("get") && implMethodName.length() > 3) {
                fieldName = Introspector.decapitalize(implMethodName.substring(3));
            } else if (implMethodName.startsWith("is") && implMethodName.length() > 2) {
                fieldName = Introspector.decapitalize(implMethodName.substring(2));
            } else if (implMethodName.startsWith("lambda$")) {
                throw new IllegalArgumentException("不不支持传递lambda表达式，只能使用方法引用");
            } else {
                fieldName = Introspector.decapitalize(implMethodName);
            }

            String declaredClass = serializedLambda.getImplClass().replace("/", ".");
            Class<?> aClass = Class.forName(declaredClass, false, ClassUtils.getDefaultClassLoader());
            return ReflectionUtils.findField(aClass, fieldName);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

}
