package org.quickjava.orm.utils;

import org.quickjava.common.utils.ReflectUtil;
import org.quickjava.orm.Q;
import org.quickjava.orm.QuerySet;
import org.quickjava.orm.contain.Action;
import org.quickjava.orm.enums.Operator;
import org.quickjava.orm.contain.TableMeta;
import org.quickjava.orm.contain.WhereBase;

import java.util.*;

// 用户querySet的增强和助手方法
public class QuerySetHelper {

    // 表信息缓存
    private static Map<String, TableMeta> tableOriMap = new LinkedHashMap<>();

    public static boolean existTableOri(String name) {
        return tableOriMap.containsKey(name);
    }

    public static TableMeta getTableOri(String name) {
        return tableOriMap.get(name);
    }

    public static TableMeta setTableOri(String name, TableMeta tableOri) {
        tableOriMap.put(name, tableOri);
        return tableOri;
    }

    public static QuerySet loadQuery(QuerySet querySet, Map<String, Object> query)
    {
        for (Map.Entry<String, Object> entry : query.entrySet()) {
            String name = entry.getKey();
            Object value = entry.getValue();
            String[] nameArr = parseQueryField(name);
            String field = nameArr[0], condition = nameArr[1];
            if (SqlUtil.isEmpty(name) || SqlUtil.isEmpty(value) || (SqlUtil.isEmpty(field) && SqlUtil.isEmpty(condition))) {
                continue;
            }
            field = "`"+field+"`";
            if ("LIKE".equals(condition)) {
                querySet.where(field, Operator.LIKE, "%" + value + "%");
            } else if ("LIKE_RAW".equals(condition)) {
                querySet.where(field, Operator.LIKE, value);        // LIKE原生查询
            } else if ("IN".equals(condition)) {      // in查询
                querySet.where(field, Operator.IN, parseQueryValueArray(value));
            } else if ("NOT_IN".equals(condition)) {      // not in查询
                querySet.where(field, Operator.NOT_IN, parseQueryValueArray(value));
            }else if ("BETWEEN".equals(condition)) {      // BETWEEN 查询
                ArrayList<?> valueArr = (ArrayList<?>) value;
                if (valueArr != null && valueArr.size() == 2) {
                    querySet.between(field, valueArr.get(0), valueArr.get(1));
                }
            } else if ("RANGE".equals(condition)) {      // 范围查询
                ArrayList<?> valueArr = (ArrayList<?>) value;
                if (valueArr != null && valueArr.size() == 2) {
                    querySet.between(field, valueArr.get(0), valueArr.get(1));
                }
            } else if (SqlUtil.isNotEmpty(field) && SqlUtil.isNotEmpty(value)) {
                querySet.where(field, Operator.valueOf(condition), value);
            }
        }
        return querySet;
    }

    /**
     * 查询条件处理
     * @param table 表名
     * @param query 查询条件（双下划线格式
     * @return 查询器
     * */
    public static QuerySet loadQuery(String table, Map<String, Object> query) {
        return loadQuery(Q.table(table), query);
    }

    public static String[] parseQueryField(String name) {
        if (name.contains("__")) {
            String[] nameArr = name.split("__");
            nameArr[0] = SqlUtil.toUnderlineCase(nameArr[0]);   // 驼峰属性名称转数据下划线字段
            nameArr[1] = nameArr[1].toUpperCase();
            return nameArr;
        }
        return new String[]{"", ""};
    }

    // 支持数组、集合、逗号连接
    public static Object[] parseQueryValueArray(Object value) {
        Object[] valArr = new Object[]{value};
        if (value instanceof String) {
            if (((String) value).contains("ζ")) {
                valArr = ((String) value).split("ζ");
            } else if (((String) value).contains("Ψ")) {
                valArr = ((String) value).split("Ψ");
            } else {
                valArr = ((String) value).split(",");
            }
        } else if (value instanceof Object[]) {
            valArr = (String[]) value;
        } else if (value instanceof Collection) {
            valArr = ((Collection<?>) value).toArray(new Object[0]);
        }
        return valArr;
    }

    // 获取QuerySet数据
    public static Action __Action(QuerySet query) {
        return ReflectUtil.invoke(query, "__Action");
    }

    public static List<String> __FieldList(QuerySet query) {
        return ReflectUtil.invoke(query, "__FieldList");
    }

    public static String __Table(QuerySet query) {
        return ReflectUtil.invoke(query, "__Table");
    }

    public static List<String[]> __JoinList(QuerySet query) {
        return ReflectUtil.invoke(query, "__JoinList");
    }

    public static List<Map<String, Object>> __DataList(QuerySet query) {
        return ReflectUtil.invoke(query, "__DataList");
    }

    public static List<WhereBase> __WhereList(QuerySet query) {
        return ReflectUtil.invoke(query, "__WhereList");
    }

    public static List<String> __Orders(QuerySet query) {
        return ReflectUtil.invoke(query, "__Orders");
    }

    public static Integer __Limit(QuerySet query) {
        return ReflectUtil.invoke(query, "__Limit");
    }

    public static Integer __LimitSize(QuerySet query) {
        return ReflectUtil.invoke(query, "__LimitSize");
    }

}
