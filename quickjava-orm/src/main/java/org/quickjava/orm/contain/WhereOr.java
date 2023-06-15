/*
 * Copyright (c) 2021.
 * More Info to http://www.quickjava.org
 */

package org.quickjava.orm.contain;

import org.quickjava.orm.enums.Operator;

import java.util.List;

public class WhereOr extends Where {

    public WhereOr(String field, Operator operator, Object value) {
        super(2, field, operator, value);
    }

    public WhereOr(List<Where> wheres) {
        super(2, wheres);
    }

}
