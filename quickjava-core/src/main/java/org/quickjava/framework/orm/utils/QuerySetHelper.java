package org.quickjava.framework.orm.utils;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import org.quickjava.framework.orm.Q;
import org.quickjava.framework.orm.QuerySet;
import org.quickjava.framework.orm.contain.TableOri;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

// 用户querySet的增强和助手方法
public class QuerySetHelper {

    // 表信息缓存
    private static Map<String, TableOri> tableOriMap = new LinkedHashMap<>();

    public static boolean existTableOri(String name) {
        return tableOriMap.containsKey(name);
    }

    public static TableOri getTableOri(String name) {
        return tableOriMap.get(name);
    }

    public static TableOri setTableOri(String name, TableOri tableOri) {
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
            if (ObjectUtil.isEmpty(name) || ObjectUtil.isEmpty(value) || (ObjectUtil.isEmpty(field) && ObjectUtil.isEmpty(condition))) {
                continue;
            }
            field = "`"+field+"`";
            if ("LIKE".equals(condition)) {
                querySet.where(field, condition, "%" + value + "%");
            } else if ("LIKE_RAW".equals(condition)) {
                querySet.where(field, condition.replace("_RAW",""), value);        // LIKE原生查询
            } else if ("IN".equals(condition)) {      // in查询
                querySet.where(field, condition, parseQueryValueArray(value));
            } else if ("NOT_IN".equals(condition)) {      // not in查询
                querySet.where(field, condition, parseQueryValueArray(value));
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
            } else if (ObjectUtil.isNotEmpty(field) && ObjectUtil.isNotEmpty(value)) {
                querySet.where(field, condition, value);
            }
        }
        return querySet;
    }

    /**
     * 查询条件处理
     * */
    public static QuerySet loadQuery(String table, Map<String, Object> query) {
        return loadQuery(Q.table(table), query);
    }

    public static String[] parseQueryField(String name) {
        if (name.contains("__")) {
            String[] nameArr = name.split("__");
            nameArr[0] = StrUtil.toUnderlineCase(nameArr[0]);   // 驼峰属性名称转数据下划线字段
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

}
