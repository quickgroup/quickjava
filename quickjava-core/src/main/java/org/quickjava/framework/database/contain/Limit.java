/*
 * Copyright (c) 2021.
 * More Info to http://www.quickjava.org
 */

package org.quickjava.framework.database.contain;

public class Limit {
    private Integer limit = 0;
    private Integer count;

    public Limit(Integer limit, Integer count) {
        this.limit = limit;
        this.count = count;
    }

    public Limit(Integer count) {
        this.count = count;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
