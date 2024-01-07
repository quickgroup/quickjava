package org.quickjava.orm.callback;

import org.quickjava.orm.QuerySet;
import org.quickjava.orm.contain.Where;

public interface WhereOptCallback extends QuerySetCallback{
    void call(Where where, QuerySet querySet, Object userData);
}
