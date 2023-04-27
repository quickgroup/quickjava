package org.quickjava.www;

import org.quickjava.www.pack.Child;
import org.quickjava.www.pack.Parent;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 * @author Qlo1062-(QloPC-zs)
 * @date 2021/1/22 15:20
 */
public class TestClass {

    @Test
    public void test1()
    {
        Child child = Parent._get(Child.class);
        System.out.println(child);
        System.out.println(child._name);

        System.out.println( Base64.getEncoder().encodeToString("qwe".getBytes()) );

        List<String> names = new ArrayList<>();
        names.add("Google");
        names.add("Runoob");
        names.add("Taobao");
        names.forEach(System.out::println);
    }
}
