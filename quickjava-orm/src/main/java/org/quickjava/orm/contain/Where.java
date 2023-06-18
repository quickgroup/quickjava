/*
 * Copyright (c) 2021.
 * More Info to http://www.quickjava.org
 */

package org.quickjava.orm.contain;

import org.quickjava.orm.enums.Operator;
import org.quickjava.orm.utils.SqlUtil;

import java.util.*;
import java.util.stream.Collectors;

public abstract class Where {

    private int logic = 1;

    private String field = null;

    private Operator operator = null;

    private Object value = null;

    private List<Where> children = null;

    public Where(int logic, String field, Operator operator, Object value) {
        this.logic = logic;
        this.setField(field);
        this.setOperator(operator);
        this.setValue(value);
    }

    public Where(int logic, List<Where> wheres) {
        this.logic = logic;
        this.children = wheres;
    }

    public int getLogic() {
        return logic;
    }

    public String getLogicStr() {
        // 如果字段带有logic就返回空字符串
        if (this.field != null && this.operator == Operator.RAW) {
            String fieldClear = this.field.toUpperCase().trim();
            if (fieldClear.startsWith(LOGIC_AND) || fieldClear.startsWith(LOGIC_OR)) {
                return "";
            }
        }
        return logic == 1 ? LOGIC_AND : LOGIC_OR;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public static final String LOGIC_OR = "OR";

    public static final String LOGIC_AND = "AND";

    public static Map<String, String> OpMap = new LinkedHashMap<>();

    static {
        OpMap.put("EQ", "=");
        OpMap.put("NEQ", "!=");
        OpMap.put("GT", ">");
        OpMap.put("GTE", ">=");
        OpMap.put("EGT", ">=");
        OpMap.put("LT", "<");
        OpMap.put("LTE", "<=");
        OpMap.put("ELT", "<=");
        // 扩展
        OpMap.put("LIKE", "LIKE");
        OpMap.put("IN", "IN");
        OpMap.put("NOT_IN", "NOT IN");
        OpMap.put("NOT IN", "NOT IN");
        OpMap.put("IS_NULL", "IS NULL");
        OpMap.put("IS NULL", "IS NULL");
        OpMap.put("IS_NOT_NULL", "IS NOT NULL");
        OpMap.put("IS NOT NULL", "IS NOT NULL");
    }

    public String getOperator() {
        return OpMap.getOrDefault(operator.name(), "=");
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    public Object getValue(DriveConfigure config) {
        return Value.pretreatment(value, config);
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public List<Where> getChildren() {
        return children;
    }

    @Override
    public String toString() {
        return getLogicStr() + " " + field + " " + operator + " " + value;
    }

    public String toSql(DriveConfigure cfg) {
        // 嵌套查询
        if (children != null) {
            List<String> sqlList = children.stream().map(it -> it.toSql(cfg)).collect(Collectors.toList());
            return getLogicStr() + " (" + cutFirstLogic(SqlUtil.collJoin(" ", sqlList)) + ")";
        }
        // 输出
        switch (operator) {
            case RAW: return field;
            case IS_NULL:
            case IS_NOT_NULL:
                return getLogicStr() + " " + getField() + " " + getOperator();
            case IN:
            case NOT_IN:
                return getLogicStr() + " " + getField() + " " + getOperator() + " (" + getValue(cfg) + ")";
            case BETWEEN:
                Object[] arr = new Object[]{null, null};
                if (value.getClass().isArray()) {
                    arr = (Object[]) value;
                } else if (value instanceof List) {
                    List<?> list = (List<?>) value;
                    arr = new Object[]{list.get(0), list.get(1)};
                } else if (value instanceof String) {
                    arr = ((String) value).split(",");
                }
                return getLogicStr() + " " + getField() + " BETWEEN " + Value.pretreatment(arr[0], cfg) + " AND " + Value.pretreatment(arr[1], cfg);
        }
        return getLogicStr() + " " + getField() + " " + getOperator() + " " + getValue(cfg);
    }

    public static String collectSql(List<Where> wheres, DriveConfigure config) {
        List<String> sql = new LinkedList<>();
        wheres.forEach(where-> sql.add(where.toSql(config)));
        return SqlUtil.collJoin(" ", sql);
    }

    /**
     * 去掉查询条件第一个logic
     * @param whereSql 查询语句
     * @return 连接符AND、OR
     * */
    public static String cutFirstLogic(String whereSql) {
        if (whereSql.toUpperCase().startsWith("AND ")) {
            return whereSql.substring(4);
        } else if (whereSql.toUpperCase().startsWith("OR ")) {
            return whereSql.substring(3);
        }
        return whereSql;
    }
}
