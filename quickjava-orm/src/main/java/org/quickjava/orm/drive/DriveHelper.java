package org.quickjava.orm.drive;

import org.quickjava.common.utils.ReflectUtil;
import org.quickjava.orm.QuerySet;
import org.quickjava.orm.contain.Action;
import org.quickjava.orm.contain.DriveConfigure;
import org.quickjava.orm.contain.WhereBase;

import java.util.List;
import java.util.Map;

public class DriveHelper {

    private QuerySet query;

    private DriveConfigure config;

    public DriveHelper(QuerySet query, DriveConfigure config) {
        this.query = query;
        this.config = config;
    }

    public Action getAction() {
        return ReflectUtil.invoke(query, "__Action");
    }

    public static Action getAction(QuerySet query) {
        return ReflectUtil.invoke(query, "__Action");
    }

    public List<String> getFieldList() {
        return ReflectUtil.invoke(query, "__FieldList");
    }

    public String getTable() {
        return ReflectUtil.invoke(query, "__Table");
    }

    public List<String[]> getJoinList() {
        return ReflectUtil.invoke(query, "__JoinList");
    }

    public List<Map<String, Object>> getDataList() {
        return ReflectUtil.invoke(query, "__DataList");
    }

    public List<WhereBase> __WhereList() {
        return ReflectUtil.invoke(query, "__WhereList");
    }

    public List<String> __Orders() {
        return ReflectUtil.invoke(query, "__Orders");
    }

    public Integer __Limit() {
        return ReflectUtil.invoke(query, "__Limit");
    }

    public Integer __LimitSize() {
        return ReflectUtil.invoke(query, "__LimitSize");
    }

}
