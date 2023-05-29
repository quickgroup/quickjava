package org.quickjava.www;

import org.quickjava.common.utils.ComUtil;
import org.quickjava.framework.QuickJavaBoot;
import org.quickjava.framework.annotation.ApplicationQuickBoot;

@ApplicationQuickBoot
public class ApplicationBoot {

    public static void main(String[] args)
    {
        QuickJavaBoot.start(ApplicationBoot.class, args);
        System.out.println("user_type=" + ComUtil.toCamelCase("userTypeText"));
        System.out.println("user_type=" + ComUtil.toCamelCase("user_type_Text"));

        System.out.println("user_type=" + ComUtil.toUnderlineCase("userTypeText"));
        System.out.println("user_type=" + ComUtil.toUnderlineCase("user_type_Text"));
    }

}
