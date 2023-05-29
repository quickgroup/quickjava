package org.quickjava.common.utils;

import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * Copyright (c) 2020~2023 http://www.quickjava.org All rights reserved.
 * +-------------------------------------------------------------------
 * Organization: QuickJava
 * +-------------------------------------------------------------------
 * Author: Qlo1062
 * +-------------------------------------------------------------------
 * File: ComUtil
 * +-------------------------------------------------------------------
 * Date: 2023-3-14 11:52
 * +-------------------------------------------------------------------
 * License: Apache Licence 2.0
 * +-------------------------------------------------------------------
 */
public abstract class ComUtil {

    private static final Pattern UNDERLINE_PATTERN = Pattern.compile("_([a-z0-9])");

    /**
     * 判断对象是否为空
     * @param obj 对象
     * @return 结果
     */
    public static boolean isEmpty(Object obj) {
        if (obj == null) {
            return true;
        } else if (obj instanceof CharSequence && ((CharSequence) obj).length() == 0) {
            return true;
        } else if (obj instanceof Map && ((Map<?, ?>) obj).isEmpty()) {
            return true;
        } else if (obj instanceof Iterable && !((Iterable<?>) obj).iterator().hasNext()) {
            return true;
        } else if (obj instanceof Iterator && !((Iterator<?>) obj).hasNext()) {
            return true;
        } else {
            return obj.getClass().isArray() && Array.getLength(obj) == 0;
        }
    }

    public static boolean isNotEmpty(Object obj) {
        return !isEmpty(obj);
    }

    /**
     * 转为驼峰名称，如：userType，和 {@link #toUnderlineCase} 相反用法
     *
     * @param str 字符串
     * @return 结果
     */
    public static String toCamelCase(String str) {
        Matcher matcher = UNDERLINE_PATTERN.matcher(str);
        StringBuffer sb = new StringBuffer(str);
        if (matcher.find()) {
            sb = new StringBuffer();
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
            matcher.appendTail(sb);
        } else {
            return sb.toString().replaceAll("_", "");
        }
        return toCamelCase(sb.toString());
    }

    /**
     * 转为下划线字段名称，如：user_type，和 {@link #toCamelCase} 相反用法
     *
     * @param str 字符串
     * @return 结果
     */
    public static String toUnderlineCase(String str) {
        return toSymbolCase(str, '_');
    }

    public static String toSymbolCase(CharSequence str, char symbol)
    {
        if (str == null) {
            return null;
        } else {
            int length = str.length();
            StringBuilder sb = new StringBuilder();

            for(int i = 0; i < length; ++i) {
                char c = str.charAt(i);
                Character preChar = i > 0 ? str.charAt(i - 1) : null;
                if (Character.isUpperCase(c)) {
                    Character nextChar = i < str.length() - 1 ? str.charAt(i + 1) : null;
                    if (null != preChar && Character.isUpperCase(preChar)) {
                        sb.append(c);
                    } else if (null != nextChar && !Character.isLowerCase(nextChar)) {
                        if (null != preChar && symbol != preChar) {
                            sb.append(symbol);
                        }

                        sb.append(c);
                    } else {
                        if (null != preChar && symbol != preChar) {
                            sb.append(symbol);
                        }

                        sb.append(Character.toLowerCase(c));
                    }
                } else {
                    if (symbol != c && sb.length() > 0 && Character.isUpperCase(sb.charAt(-1)) && Character.isLowerCase(c)) {
                        sb.append(symbol);
                    }

                    sb.append(c);
                }
            }

            return sb.toString();
        }
    }
}
