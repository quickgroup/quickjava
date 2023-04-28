/*
 * Copyright (c) 2021.
 * More Info to http://www.quickjava.org
 */

package org.quickjava.orm.contain;

public class WhereOr extends WhereBase {

    public WhereOr(String field, String operator, Object value) {
        super(2, field, operator, value);
    }
}
