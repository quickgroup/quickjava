package org.quickjava.orm.wrapper;

import org.quickjava.orm.model.Model;

public class ModelWrapper<M extends Model> extends AbstractModelWrapper<ModelWrapper<M>, M, MFunction<M, ?>> {

    public static<M extends Model> ModelWrapper<M> lambda(Class<M> tClass) {
        return new ModelWrapper<>(tClass);
    }

    public ModelWrapper(Class<M> tClass) {
        try {
            this.model = tClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
