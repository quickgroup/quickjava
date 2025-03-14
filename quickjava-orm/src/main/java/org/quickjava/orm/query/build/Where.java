/*
 * Copyright (c) 2021.
 * More Info to http://www.quickjava.org
 */

package org.quickjava.orm.query.build;

import org.quickjava.orm.domain.DriveConfigure;
import org.quickjava.orm.enums.Logic;
import org.quickjava.orm.query.enums.Operator;
import org.quickjava.orm.utils.SqlUtil;

import java.util.*;
import java.util.stream.Collectors;

public class Where extends TableColumn {

    /**
     * 逻辑类型：1=AND,2=OR,3=RAW
     */
    private int logic = 1;

    private Operator operator = null;

    private Object value = null;
    private String valueTable = null;
    private String valueColumn = null;

    private List<Where> children = null;

    public Where(int logic, String table, String column, Operator operator, Object value, List<Where> children) {
        super(table, column);
        this.logic = logic;
        this.operator = operator;
        this.value = value;
        this.children = children;
    }

    public Where(int logic, String table, String column, Operator operator, Object value) {
        this(logic, table, column, operator, value, null);
    }

    public Where(int logic, String table, String column, Operator operator, String valueTable, String valueColumn) {
        super(table, column);
        this.logic = logic;
        this.operator = operator;
        this.valueTable = valueTable;
        this.valueColumn = valueColumn;
    }

    public Where(int logic, String column, Operator operator, Object value) {
        this(logic, null, column, operator, value, null);
    }

    public Where(int logic, List<Where> wheres) {
        super(null);
        this.logic = logic;
        this.children = wheres;
    }

    public Where() {
        super(null);
    }

    public int getLogic() {
        return logic;
    }

    public String getLogicSql() {
        // 如果字段带有logic就返回空字符串
        if (this.column != null && this.operator == Operator.RAW) {
            String fieldClear = this.column.toUpperCase().trim();
            if (fieldClear.startsWith(Logic.AND.sql()) || fieldClear.startsWith(Logic.OR.sql())) {
                return "";
            }
        }
        return logic == 1 ? Logic.AND.sql() : Logic.OR.sql();
    }

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
        OpMap.put("BETWEEN", "BETWEEN");
        OpMap.put("NOT_BETWEEN", "NOT BETWEEN");
        OpMap.put("LIKE", "LIKE");
        OpMap.put("NOT_LIKE", "NOT LIKE");
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
        return ValueConv.getConv(config).convWrap(value);
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public List<Where> getChildren() {
        return children;
    }

    @Override
    public String toString() {
        return getLogicSql() + " " + (operator == Operator.RAW ? column : column + " " + operator + " " + value);
    }

    public String toSql(DriveConfigure cfg) {
        this.driveConfigure = cfg;
        // 嵌套查询
        if (children != null && !children.isEmpty()) {
            List<String> sqlList = children.stream().map(it -> it.toSql(cfg)).collect(Collectors.toList());
            return getLogicSql() + " (" + cutFirstLogic(SqlUtil.collJoin(" ", sqlList)) + ")";
        }
        ValueConv conv = ValueConv.getConv(cfg);
        // 右边为表字段
        if (valueColumn != null) {
            String right = driveConfigure.tableColumn(valueTable, valueColumn);
            return getLogicSql() + " " + getColumnSql() + " " + getOperatorSql() + " " + right;
        }
        // 右边为值
        switch (operator) {
            case RAW:
                return column;
            case LIKE_LEFT:
                return getLogicSql() + " " + getColumnSql() + " LIKE " + conv.valueStringWrap("%" + conv.convValue(getValue()));
            case LIKE_RIGHT:
                return getLogicSql() + " " + getColumnSql() + " LIKE " + conv.valueStringWrap(conv.convValue(getValue()) + "%");
            case LIKE_LR:
                return getLogicSql() + " " + getColumnSql() + " LIKE " + conv.valueStringWrap("%" + conv.convValue(getValue()) + "%");
            case NOT_LIKE_LR:
                return getLogicSql() + " " + getColumnSql() + " NOT LIKE " + conv.valueStringWrap("%" + conv.convValue(getValue()) + "%");
            case IS_NULL:
            case IS_NOT_NULL:
                return getLogicSql() + " " + getColumnSql() + " " + getOperatorSql();
            case IN:
            case NOT_IN:
                return getLogicSql() + " " + getColumnSql() + " " + getOperatorSql() + " (" + getValueSql(cfg) + ")";
            case BETWEEN:
            case NOT_BETWEEN:
                Object[] arr = new Object[]{null, null};
                if (value.getClass().isArray()) {
                    arr = (Object[]) value;
                } else if (value instanceof List) {
                    List<?> list = (List<?>) value;
                    arr = new Object[]{list.get(0), list.get(1)};
                } else if (value instanceof String) {
                    arr = ((String) value).split(",");
                }
                return getLogicSql() + " " + getColumnSql() + " "+getOperatorSql()+" " + conv.convWrap(arr[0]) + " AND " + conv.convWrap(arr[1]);
        }
        return getLogicSql() + " " + getColumnSql() + " " + getOperatorSql() + " " + getValueSql(cfg);
    }

    public static String collectSql(List<Where> wheres, DriveConfigure config) {
        List<String> sql = new LinkedList<>();
        wheres.forEach(where-> sql.add(where.toSql(config)));
        return SqlUtil.collJoin(" ", sql);
    }

    /**
     * 去掉查询条件第一个logic
     * */
    public static String cutFirstLogic(String whereSql) {
        String sqlSimple = whereSql.substring(0, 10).toUpperCase();
        if (sqlSimple.startsWith(Logic.AND.sql() + " ")) {
            return whereSql.substring(4);
        } else if (sqlSimple.startsWith(Logic.OR.sql() + " ")) {
            return whereSql.substring(3);
        }
        return whereSql;
    }
}
