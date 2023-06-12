package org.quickjava.orm.modelQuery;

import java.io.Serializable;
import java.util.function.Function;

@FunctionalInterface
public interface ModelFunction<T, R> extends Function<T, R>, Serializable {
}
