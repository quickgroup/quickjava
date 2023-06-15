package org.quickjava.orm.callback;

import org.quickjava.orm.QuerySet;

public interface WhereCallback {
    void call(QuerySet query);
}
