/*
 * Copyright (c) 2021.
 * More Info to http://www.quickjava.org
 */

package org.quickjava.orm.contain;

import java.util.List;

public class Where extends WhereBase {

    public Where(String field, Operator operator, Object value) {
        super(1, field, operator, value);
    }

    public Where(List<WhereBase> wheres) {
        super(1, wheres);
    }

}
