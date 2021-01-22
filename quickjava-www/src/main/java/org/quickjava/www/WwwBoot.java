package org.quickjava.www;

import org.quickjava.common.QFileUtils;

/**
 * @author QloPC-Msi
 * @date
 */
public class WwwBoot {

    public static void main(String[] args) {
        System.out.println("test");
        test8();
    }


    public static void test8()
    {
        String content = QFileUtils.getPackageFileContent(WwwBoot.class.getPackage().getName(), "asdf.text");
        System.out.println("content: " + content);
    }

}
