/*
 * Copyright (c) 2021.
 * More Info to http://www.quickjava.org
 */

package org.quickjava.framework.database.drive;

import org.quickjava.framework.bean.DbConfig;
import org.quickjava.framework.database.Query;

import java.util.List;
import java.util.Map;

/**
 * @author Qlo1062-(QloPC-zs)
 * @date 2021/1/25 15:54
 */
public interface Drive {

    public Query connect(DbConfig config);

    public String pretreatment(Query query);

    public String assemble(Drive drive);

    public Object executeSql(String sql);
}
