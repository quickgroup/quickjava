/*
 * Copyright (c) 2021.
 * More Info to http://www.quickjava.org
 */

package org.quickjava.web.common.utils;

import java.util.List;

public class StringUtils {

    public static String join(List<?> list, String separator)
    {
        Object[] objects = new Object[list.size()];
        return join(list.toArray(objects), separator);
    }

    public static String join(Object[] objects, String separator)
    {
        StringBuffer stringBuffer = new StringBuffer();
        for (int index = 0; index < objects.length; index++) {
            if (index > 0) {
                stringBuffer.append(separator);
            }
            stringBuffer.append(objects[index].toString());
        }
        return stringBuffer.toString();
    }
}
