package org.quickjava.framework;

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
    public T where(String field, String operator, String value) {
        return (T) this;
    }

    public T find() {
        return (T) this;
    }

    @Override
    public String toString() {
        return "Model{" +
                "name='" + name + '\'' +
                '}';
    }
}
