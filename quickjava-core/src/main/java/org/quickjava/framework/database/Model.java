package org.quickjava.framework.database;

/**
 * @author Qlo1062-(QloPC-zs)
 * @date 2021/1/19 10:17
 */
public class Model<T> {

    public String name;

    public static Model _get() {
        return new Model();
    }

    /**
     * @langCn WHERE 条件
     * @param field
     * @param operator
     * @param value
     * @return
     */
    final public T where(String field, String operator, String value) {
        return (T) this;
    }

    final public T find() {
        return (T) this;
    }

    @Override
    public String toString() {
        return "Model{" +
                "name='" + name + '\'' +
                '}';
    }
}
