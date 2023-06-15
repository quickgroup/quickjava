package org.quickjava.orm.wrapper;

import org.quickjava.orm.Model;

public class ModelQuery<M extends Model> extends ModelQueryWrapper<M, ModelFunction<M, ?>> {

    public static<M extends Model> ModelQuery<M> lambda(Class<M> tClass) {
        return new ModelQuery<>(tClass);
    }

    public ModelQuery(Class<M> tClass) {
        try {
            this.model = tClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
