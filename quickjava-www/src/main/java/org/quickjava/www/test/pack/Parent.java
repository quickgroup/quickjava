package org.quickjava.www.test.pack;

/**
 * @author Qlo1062-(QloPC-zs)
 * @date 2021/1/22 15:23
 */
public abstract class Parent<T> {

    public String _name = null;

    public static<T> T _get(Class<? extends Parent> type) {
        try {
            return (T) type.getDeclaredConstructor().newInstance();
        } catch (Exception exc) {
            exc.printStackTrace();
        }
        return null;
    }
}
