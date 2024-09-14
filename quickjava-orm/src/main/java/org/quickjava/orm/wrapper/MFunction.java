package org.quickjava.orm.wrapper;

import cn.hutool.core.text.CharSequenceUtil;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;
import java.util.function.Function;

@FunctionalInterface
public interface MFunction<M, R> extends Function<M, R>, Serializable {

    default String getFieldName() {
        String methodName = getMethodName();
        if (methodName.startsWith("get")) {
            methodName = methodName.substring(3);
        }
        return CharSequenceUtil.lowerFirst(methodName);
    }

    /**
     * From: {@link #getFieldName()}
     */
    default String name() {
        return getFieldName();
    }

    default String getMethodName() {
        return getSerializedLambda().getImplMethodName();
    }

    default Class<?> getFieldClass() {
        return getReturnType();
    }

    default SerializedLambda getSerializedLambda() {
        try {
            Method method = getClass().getDeclaredMethod("writeReplace");
            method.setAccessible(true);
            return (SerializedLambda) method.invoke(this);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    default Class<?> getReturnType() {
        try {
            SerializedLambda lambda = getSerializedLambda();
            Class<?> className = Class.forName(lambda.getImplClass().replace("/", "."));
            Method method = className.getMethod(getMethodName());
            return method.getReturnType();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
