package org.quickjava.orm;

import org.quickjava.common.utils.GenericsUtils;

public class ModelQuery<T> extends ModelQueryWrapper<T, ModelFunction<T, ?>> {

    public ModelQuery() {
        Class<?> modelClass = GenericsUtils.getSuperClassGenericsType(this.getClass(), 0);
        try {
            this.model = (Model) modelClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
