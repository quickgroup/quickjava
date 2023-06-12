/*
 * Copyright (c) 2021.
 * More Info to http://www.quickjava.org
 */

package org.quickjava.orm.contain;

import java.util.List;

public class WhereOr extends WhereBase {

    public WhereOr(String field, Operator operator, Object value) {
        super(2, field, operator, value);
    }

    public WhereOr(List<WhereBase> wheres) {
        super(2, wheres);
    }

}
