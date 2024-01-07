package org.quickjava.orm.callback;

import org.quickjava.orm.contain.OrderBy;

public interface OrderByOptCallback extends QuerySetCallback {
    void call(OrderBy orderBy, Object userData);
}
