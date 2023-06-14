package org.quickjava.common.utils;

import org.quickjava.common.enums.DatetimeCurrType;
import org.quickjava.common.enums.DatetimeRangeType;

import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/*
 * Copyright (c) 2020~2023 http://www.quickjava.org All rights reserved.
 * +-------------------------------------------------------------------
 * Organization: QuickJava
 * +-------------------------------------------------------------------
 * Author: Qlo1062
 * +-------------------------------------------------------------------
 * File: DateUtil
 * +-------------------------------------------------------------------
 * Date: 2023-5-30 17:44
 * +-------------------------------------------------------------------
 * License: Apache Licence 2.0
 * +-------------------------------------------------------------------
 */
public class DatetimeUtil {

    public static final String FORMAT_DEF = "yyyy-MM-dd HH:mm:ss";

    public static final String FORMAT_YEAR = "yyyy";
    public static final String FORMAT_MONTH = "MM";
    public static final String FORMAT_DAY = "dd";
    public static final String FORMAT_YEAR_MONTH = "yyyy-MM";
    public static final String FORMAT_DATE = "yyyy-MM-dd";

    public static final String FORMAT_HOUR = "HH";
    public static final String FORMAT_MINUTE = "mm";
    public static final String FORMAT_SECOND = "ss";
    public static final String FORMAT_HOUR_MINUTE = "HH:mm";
    public static final String FORMAT_TIME = "HH:mm:ss";

    public static final DateTimeFormatter FORMATTER_DEF = DateTimeFormatter.ofPattern(FORMAT_DEF);

    public static final DateTimeFormatter FORMATTER_YEAR = DateTimeFormatter.ofPattern(FORMAT_YEAR);
    public static final DateTimeFormatter FORMATTER_MONTH = DateTimeFormatter.ofPattern(FORMAT_MONTH);
    public static final DateTimeFormatter FORMATTER_DAY = DateTimeFormatter.ofPattern(FORMAT_DAY);
    public static final DateTimeFormatter FORMATTER_YEAR_MONTH = DateTimeFormatter.ofPattern(FORMAT_YEAR_MONTH);
    public static final DateTimeFormatter FORMATTER_DATE = DateTimeFormatter.ofPattern(FORMAT_DATE);

    public static final DateTimeFormatter FORMATTER_HOUR = DateTimeFormatter.ofPattern(FORMAT_HOUR);
    public static final DateTimeFormatter FORMATTER_MINUTE = DateTimeFormatter.ofPattern(FORMAT_MINUTE);
    public static final DateTimeFormatter FORMATTER_SECOND = DateTimeFormatter.ofPattern(FORMAT_SECOND);
    public static final DateTimeFormatter FORMATTER_HOUR_MINUTE = DateTimeFormatter.ofPattern(FORMAT_HOUR_MINUTE);
    public static final DateTimeFormatter FORMATTER_TIME = DateTimeFormatter.ofPattern(FORMAT_TIME);

    public static String formatDateTime(Date date) {
        return formatDateTime(date, FORMAT_DEF);
    }

    public static String formatDateTime(Date date, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }

    public static<T> T rangeType(DatetimeRangeType type) {
        LocalDate today;
        YearMonth currentYearMonth;
        LocalDateTime startDateTime;
        LocalDateTime endDateTime;
        Object[] ret = null;
        switch (type) {
            case TODAY:
                today = LocalDate.now();
                startDateTime = LocalDateTime.of(today, LocalTime.MIN);
                endDateTime = LocalDateTime.of(today, LocalTime.MAX);
                ret = new String[]{startDateTime.format(FORMATTER_DEF), endDateTime.format(FORMATTER_DEF)};
                break;
            case YESTERDAY:
                LocalDate yesterday = LocalDate.now().minusDays(1);
                startDateTime = LocalDateTime.of(yesterday, LocalTime.MIN);
                endDateTime = LocalDateTime.of(yesterday, LocalTime.MAX);
                ret = new String[]{startDateTime.format(FORMATTER_DEF), endDateTime.format(FORMATTER_DEF)};
                break;
            case WEEK:
                today = LocalDate.now();
                LocalDate monday = today.with(DayOfWeek.MONDAY);
                LocalDate sunday = today.with(DayOfWeek.SUNDAY);
                startDateTime = LocalDateTime.of(monday, LocalTime.MIN);
                endDateTime = LocalDateTime.of(sunday, LocalTime.MAX);
                ret = new String[]{startDateTime.format(FORMATTER_DEF), endDateTime.format(FORMATTER_DEF)};
                break;
            case LAST_WEEK:
                today = LocalDate.now();
                LocalDate lastMonday = today.minusWeeks(1).with(DayOfWeek.MONDAY);
                LocalDate lastSunday = today.minusWeeks(1).with(DayOfWeek.SUNDAY);
                startDateTime = LocalDateTime.of(lastMonday, LocalTime.MIN);
                endDateTime = LocalDateTime.of(lastSunday, LocalTime.MAX);
                ret = new String[]{startDateTime.format(FORMATTER_DEF), endDateTime.format(FORMATTER_DEF)};
                break;
            case MONTH:
                currentYearMonth = YearMonth.now();
                LocalDate firstDayOfMonth = currentYearMonth.atDay(1);
                LocalDate lastDayOfMonth = currentYearMonth.atEndOfMonth();
                startDateTime = LocalDateTime.of(firstDayOfMonth, LocalTime.MIN);
                endDateTime = LocalDateTime.of(lastDayOfMonth, LocalTime.MAX);
                ret = new String[]{startDateTime.format(FORMATTER_DEF), endDateTime.format(FORMATTER_DEF)};
                break;
            case LAST_MONTH:
                currentYearMonth = YearMonth.now().minusMonths(1);
                LocalDate firstDayOfLastMonth = currentYearMonth.atDay(1);
                LocalDate lastDayOfLastMonth = currentYearMonth.atEndOfMonth();
                startDateTime = LocalDateTime.of(firstDayOfLastMonth, LocalTime.MIN);
                endDateTime = LocalDateTime.of(lastDayOfLastMonth, LocalTime.MAX);
                ret = new String[]{startDateTime.format(FORMATTER_DEF), endDateTime.format(FORMATTER_DEF)};
                break;
            case YEAR:
                int currentYear = LocalDate.now().getYear();
                LocalDate firstDayOfYear = LocalDate.of(currentYear, Month.JANUARY, 1);
                LocalDate lastDayOfYear = LocalDate.of(currentYear, Month.DECEMBER, 31);
                startDateTime = LocalDateTime.of(firstDayOfYear, LocalTime.MIN);
                endDateTime = LocalDateTime.of(lastDayOfYear, LocalTime.MAX);
                ret = new String[]{startDateTime.format(FORMATTER_DEF), endDateTime.format(FORMATTER_DEF)};
                break;
            case LAST_YEAR:
                int lastYear = LocalDate.now().getYear() - 1;
                LocalDate firstDayOfLastYear = LocalDate.of(lastYear, Month.JANUARY, 1);
                LocalDate lastDayOfLastYear = LocalDate.of(lastYear, Month.DECEMBER, 31);
                startDateTime = LocalDateTime.of(firstDayOfLastYear, LocalTime.MIN);
                endDateTime = LocalDateTime.of(lastDayOfLastYear, LocalTime.MAX);
                ret = new String[]{startDateTime.format(FORMATTER_DEF), endDateTime.format(FORMATTER_DEF)};
                break;
        }
        return (T) ret;
    }

    public static String currType(DatetimeCurrType type) {
        switch (type) {
            case YEAR: return String.valueOf(LocalDate.now().getYear());
            case MONTH: return LocalDate.now().format(FORMATTER_MONTH);
            case DAY: return LocalDate.now().format(FORMATTER_DAY);
            case YEAR_MONTH: return YearMonth.now().format(FORMATTER_YEAR_MONTH);
            case DATE: return YearMonth.now().format(FORMATTER_DATE);
            case HOUR: return LocalDate.now().format(FORMATTER_HOUR);
            case MINUTE: return LocalDate.now().format(FORMATTER_MINUTE);
            case SECOND: return LocalDate.now().format(FORMATTER_SECOND);
            case HOUR_MINUTE: return LocalDate.now().format(FORMATTER_HOUR_MINUTE);
            case TIME: return LocalDate.now().format(FORMATTER_TIME);
            case DATETIME: return LocalDate.now().format(FORMATTER_DEF);
        }
        return null;
    }

    public static Integer currTypeInt(DatetimeCurrType type) {
        String ret = currType(type);
        return ret == null ? null : Integer.valueOf(ret.replaceAll("-", "").replaceAll(":", ""));
    }

}
