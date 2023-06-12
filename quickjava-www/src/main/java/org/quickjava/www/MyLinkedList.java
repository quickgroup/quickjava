package org.quickjava.www;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.LinkedList;

public class MyLinkedList<T> {
    private LinkedList<T> linkedList;

    public MyLinkedList() {
        linkedList = new LinkedList<>();
    }

    public void add(T element) {
        linkedList.add(element);
    }

    public Class<?> getGenericType() {
        Type genericType = getClass().getGenericSuperclass();

        if (genericType instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) genericType;
            Type[] typeArguments = parameterizedType.getActualTypeArguments();

            if (typeArguments.length > 0) {
                Type typeArgument = typeArguments[0];
                if (typeArgument instanceof Class) {
                    return (Class<?>) typeArgument;
                }
            }
        }

        return null;
    }

    public static void main(String[] args) {
        MyLinkedList<String> myLinkedList = new MyLinkedList<>();
        myLinkedList.add("Hello");
        myLinkedList.add("World");

        Class<?> genericType = myLinkedList.getGenericType();
        if (genericType != null) {
            System.out.println("泛型类: " + genericType.getName());
        }
    }
}
