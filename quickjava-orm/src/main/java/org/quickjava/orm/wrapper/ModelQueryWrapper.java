package org.quickjava.orm.wrapper;

import org.quickjava.orm.Model;

public class ModelQueryWrapper<M extends Model> extends AbstractModelWrapper<ModelQueryWrapper<M>, M, MFunction<M, ?>> {

    public static<M extends Model> ModelQueryWrapper<M> lambda(Class<M> tClass) {
        return new ModelQueryWrapper<>(tClass);
    }

    public ModelQueryWrapper(Class<M> tClass) {
        try {
            this.model = tClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
