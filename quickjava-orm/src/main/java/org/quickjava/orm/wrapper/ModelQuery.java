package org.quickjava.orm.wrapper;

public class ModelQuery<T> extends ModelQueryWrapper<T, ModelFunction<T, ?>> {

    public static<T> ModelQuery<T> lambda(Class<T> tClass) {
        return new ModelQuery<>(tClass);
    }

    public ModelQuery(Class<T> tClass) {
        try {
            this.model = tClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
