/*
 * Copyright (c) 2021.
 * More Info to http://www.quickjava.org
 */

package org.quickjava.orm.query.build;

import org.quickjava.orm.query.enums.Operator;

import java.util.List;

public class WhereAnd extends Where {

    public WhereAnd(String table, String column, Operator operator, Object value) {
        super(1, table, column, operator, value);
    }

    public WhereAnd(String column, Operator operator, Object value) {
        super(1, column, operator, value);
    }

    public WhereAnd(List<Where> wheres) {
        super(1, wheres);
    }

}
