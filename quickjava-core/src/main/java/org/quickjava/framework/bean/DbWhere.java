package org.quickjava.framework.bean;

/**
 * @author Qlo1062-(QloPC-Msi)
 * @date 2021/1/23 23:19
 * @projectName quickjava
 */
public class DbWhere {

    public String field = null;

    public String operator = null;

    public String value = null;

    public DbWhere(String field, String operator, String value) {
        this.field = field;
        this.operator = operator;
        this.value = value;
    }

    public static DbWhere _new(String field, String operator, String value )
    {
        return new DbWhere(field, operator, value);
    }

    public static DbWhere _new(String field, String value )
    {
        return new DbWhere(field, "=", value);
    }
}
