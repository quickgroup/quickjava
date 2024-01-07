package org.quickjava.orm.query.callback;

import org.quickjava.orm.query.QuerySet;
import org.quickjava.orm.query.build.Where;

public interface WhereOptCallback extends QuerySetCallback{
    void call(Where where, QuerySet querySet, Object userData);
}
