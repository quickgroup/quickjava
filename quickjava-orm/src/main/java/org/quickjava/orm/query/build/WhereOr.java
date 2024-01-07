/*
 * Copyright (c) 2021.
 * More Info to http://www.quickjava.org
 */

package org.quickjava.orm.query.build;

import org.quickjava.orm.query.enums.Operator;

import java.util.List;

public class WhereOr extends Where {

    public WhereOr(String column, Operator operator, Object value) {
        super(2, column, operator, value);
    }

    public WhereOr(List<Where> wheres) {
        super(2, wheres);
    }

}
