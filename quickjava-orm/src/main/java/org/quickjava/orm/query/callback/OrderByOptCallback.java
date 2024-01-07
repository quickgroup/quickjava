package org.quickjava.orm.query.callback;

import org.quickjava.orm.query.build.OrderBy;

public interface OrderByOptCallback extends QuerySetCallback {
    void call(OrderBy orderBy, Object userData);
}
