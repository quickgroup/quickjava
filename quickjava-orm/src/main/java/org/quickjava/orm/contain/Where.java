/*
 * Copyright (c) 2021.
 * More Info to http://www.quickjava.org
 */

package org.quickjava.orm.contain;

public class Where extends WhereBase {

    public Where(String field, String operator, Object value) {
        super(1, field, operator, value);
    }

}
