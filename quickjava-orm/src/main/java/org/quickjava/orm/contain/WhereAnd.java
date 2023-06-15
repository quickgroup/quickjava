/*
 * Copyright (c) 2021.
 * More Info to http://www.quickjava.org
 */

package org.quickjava.orm.contain;

import org.quickjava.orm.enums.Operator;

import java.util.List;

public class WhereAnd extends Where {

    public WhereAnd(String field, Operator operator, Object value) {
        super(1, field, operator, value);
    }

    public WhereAnd(List<Where> wheres) {
        super(1, wheres);
    }

}
