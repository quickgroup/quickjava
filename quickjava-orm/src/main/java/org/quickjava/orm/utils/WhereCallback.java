package org.quickjava.orm.utils;

import org.quickjava.orm.QuerySet;

public interface WhereCallback {
    void call(QuerySet query);
}
