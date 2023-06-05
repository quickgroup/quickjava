package org.quickjava.common.utils;

import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.List;
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

    public static boolean isAllUpperCase(String str) {
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (Character.isLetter(c) && !Character.isUpperCase(c)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 转为驼峰名称，如：userType，和 {@link #toUnderlineCase} 相反用法
     *
     * @param inputString 字符串
     * @return 结果
     */
    public static String toCamelCase(String inputString) {
        if (!inputString.contains("_")) {
            // 全大写字符串且不含分隔符，直接全部转小写，兼容ORACLE
            if (isAllUpperCase(inputString)) {
                return inputString.toLowerCase();
            }
            return inputString;
        }

        StringBuilder result = new StringBuilder();
        boolean capitalizeNextChar = false;
        // 遍历输入字符串中的每个字符
        for (int i = 0; i < inputString.length(); i++) {
            char currentChar = inputString.charAt(i);
            if (currentChar == '_') {
                capitalizeNextChar = true;
            } else if (capitalizeNextChar) {
                result.append(Character.toUpperCase(currentChar));
                capitalizeNextChar = false;
            } else {
                result.append(Character.toLowerCase(currentChar));
            }
        }

        return result.toString();
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
                    if (symbol != c && sb.length() > 0 && Character.isUpperCase(sb.charAt(sb.length()-1)) && Character.isLowerCase(c)) {
                        sb.append(symbol);
                    }

                    sb.append(c);
                }
            }

            return sb.toString();
        }
    }

    public static String firstUpper(String str)
    {
        if (isEmpty(str)) {
            return str;
        } else if (str.length() == 1) {
            return str.substring(0, 1).toUpperCase();
        }
        str = str.substring(0, 1).toUpperCase() + str.substring(1);
        return str;
    }

    /**
     * 去除字符串数组中每个元素左右空格
     * @param array 目标数组
     * @return 结果
     */
    public static String[] trimArray(String[] array) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] != null) {
                array[i] = array[i].trim();
            }
        }
        return array;
    }

    /**
     * 去除字符串list中每个元素左右空格
     * @param list 目标数组
     * @return 结果
     */
    public static List<String> trimList(List<String> list) {
        for (int i = 0; i < list.size(); i++) {
            String str = list.get(i);
            if (str != null) {
                list.set(i, str.trim());
            }
        }
        return list;
    }
}
