package org.quickjava.www;

import org.quickjava.common.utils.ComUtil;
import org.quickjava.framework.QuickJavaBoot;
import org.quickjava.framework.annotation.ApplicationQuickBoot;

@ApplicationQuickBoot
public class ApplicationBoot {

    public static void main(String[] args)
    {
        QuickJavaBoot.start(ApplicationBoot.class, args);
        System.out.println("userTypeText=" + ComUtil.toCamelCase("userTypeText"));
        System.out.println("USER_TYPE_TEXT=" + ComUtil.toCamelCase("USER_TYPE_TEXT"));
        System.out.println("user_type_Text=" + ComUtil.toCamelCase("user_Type_text"));
        System.out.println("user_type_text=" + ComUtil.toCamelCase("user_type_text"));

        System.out.println("userTypeText=" + ComUtil.toUnderlineCase("userTypeText"));
        System.out.println("USER_TYPE_TEXT=" + ComUtil.toUnderlineCase("USER_TYPE_TEXT"));
        System.out.println("user_type_Text=" + ComUtil.toUnderlineCase("user_type_Text"));
    }

}
