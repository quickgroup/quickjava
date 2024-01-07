/*
 * Copyright (c) 2021.
 * More Info to http://www.quickjava.org
 */

package org.quickjava.orm.contain;

import org.quickjava.orm.drive.DefaultDrive;
import org.quickjava.orm.enums.Operator;
import org.quickjava.orm.utils.SqlUtil;

import java.util.*;
import java.util.stream.Collectors;

public abstract class Where {

    private int logic = 1;

    private String table = null;

    private String field = null;

    private Operator operator = null;

    private Object value = null;

    private List<Where> children = null;

    private DriveConfigure driveConfigure = DefaultDrive.CONFIGURE;

    public Where(int logic, String table, String field, Operator operator, Object value, List<Where> children) {
        this.logic = logic;
        this.table = table;
        this.field = field;
        this.operator = operator;
        this.value = value;
        this.children = children;
    }

    public Where(int logic, String table, String field, Operator operator, Object value) {
        this(logic, table, field, operator, value, null);
    }

    public Where(int logic, String field, Operator operator, Object value) {
        this(logic, null, field, operator, value, null);
    }

    public Where(int logic, List<Where> wheres) {
        this.logic = logic;
        this.children = wheres;
    }

    public int getLogic() {
        return logic;
    }

    public String getLogicSql() {
        // 如果字段带有logic就返回空字符串
        if (this.field != null && this.operator == Operator.RAW) {
            String fieldClear = this.field.toUpperCase().trim();
            if (fieldClear.startsWith(LOGIC_AND) || fieldClear.startsWith(LOGIC_OR)) {
                return "";
            }
        }
        return logic == 1 ? LOGIC_AND : LOGIC_OR;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getField() {
        return field;
    }

    public String getFieldSql() {
        String fieldSql = field;
        if (driveConfigure.columnLeft != null) {
            fieldSql = driveConfigure.columnLeft + fieldSql;
        }
        if (driveConfigure.columnRight != null) {
            fieldSql = fieldSql + driveConfigure.columnRight;
        }
        return table == null ? fieldSql : SqlUtil.tableColumn(table, fieldSql);
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

    public Operator getOperator() {
        return operator;
    }

    public String getOperatorSql() {
        return OpMap.getOrDefault(operator.name(), "=");
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    public Object getValueSql(DriveConfigure config) {
        return ValueConv.getConv(config).conv(value);
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public List<Where> getChildren() {
        return children;
    }

    @Override
    public String toString() {
        return getLogicSql() + " " + field + " " + operator + " " + value;
    }

    public String toSql(DriveConfigure cfg) {
        this.driveConfigure = cfg;
        // 嵌套查询
        if (children != null && !children.isEmpty()) {
            List<String> sqlList = children.stream().map(it -> it.toSql(cfg)).collect(Collectors.toList());
            return getLogicSql() + " (" + cutFirstLogic(SqlUtil.collJoin(" ", sqlList)) + ")";
        }
        ValueConv valueConv = ValueConv.getConv(cfg);
        // 输出
        switch (operator) {
            case RAW: return field;
            case IS_NULL:
            case IS_NOT_NULL:
                return getLogicSql() + " " + getFieldSql() + " " + getOperatorSql();
            case IN:
            case NOT_IN:
                return getLogicSql() + " " + getFieldSql() + " " + getOperatorSql() + " (" + getValueSql(cfg) + ")";
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
                return getLogicSql() + " " + getFieldSql() + " BETWEEN " + valueConv.conv(arr[0]) + " AND " + valueConv.conv(arr[1]);
        }
        return getLogicSql() + " " + getFieldSql() + " " + getOperatorSql() + " " + getValueSql(cfg);
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
