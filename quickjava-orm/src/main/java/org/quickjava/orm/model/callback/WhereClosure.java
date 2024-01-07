package org.quickjava.orm.model.callback;

import org.quickjava.orm.query.QuerySet;

public interface WhereClosure {
    void call(QuerySet query);
}
