package org.quickjava.orm;

import org.quickjava.orm.utils.ModelUtil;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.function.Function;

public abstract class ModelQueryWrapper<T, R extends Function<T, ?>> {

    protected T model;

    public Model model() {
        return (Model) model;
    }

    public ModelQueryWrapper<T, R> eq(R column, Object val)
    {
        System.out.println("column=" + column);
        System.out.println("column=" + column.apply(model));

        System.out.println("function.getDeclaredFields()=" + Arrays.toString(column.getClass().getDeclaredFields()));
        System.out.println("function.getDeclaredMethods()=" + Arrays.toString(column.getClass().getDeclaredMethods()));

        Method[] methods = column.getClass().getDeclaredMethods();
        for (Method method : methods) {
            if (method.getName().equals("apply")) {
                System.out.println("Method name: " + method.getName());
                break;
            }
        }

        model().eq(ModelUtil.getFunctionConvFieldName(column), val);
        return this;
    }

}
