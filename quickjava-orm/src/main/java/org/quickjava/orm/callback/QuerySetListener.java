package org.quickjava.orm.callback;

import org.quickjava.orm.QuerySet;

public interface QuerySetListener {

    void insert(QuerySet query);

    void delete(QuerySet query);

    void update(QuerySet query);

    void select(QuerySet query);
}
