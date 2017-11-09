package com.tvos.apps.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
    public static final String PATTERN_STANDARD08W = "yyyyMMdd";
    public static final String PATTERN_STANDARD10H = "yyyy-MM-dd";
    public static final String PATTERN_STANDARD10X = "yyyy/MM/dd";
    public static final String PATTERN_STANDARD12W = "yyyyMMddHHmm";
    public static final String PATTERN_STANDARD14W = "yyyyMMddHHmmss";
    public static final String PATTERN_STANDARD16H = "yyyy-MM-dd HH:mm";
    public static final String PATTERN_STANDARD16X = "yyyy/MM/dd HH:mm";
    public static final String PATTERN_STANDARD17W = "yyyyMMddHHmmssSSS";
    public static final String PATTERN_STANDARD19H = "yyyy-MM-dd HH:mm:ss";
    public static final String PATTERN_STANDARD19X = "yyyy/MM/dd HH:mm:ss";

    public static String date2String(Date date, String pattern) {
        if (date == null) {
            throw new IllegalArgumentException("timestamp null illegal");
        }
        if (pattern == null || pattern.equals("")) {
            pattern = PATTERN_STANDARD19H;
        }
        return new SimpleDateFormat(pattern).format(date);
    }

    public static Date string2Date(String strDate, String pattern) {
        if (strDate == null || strDate.equals("")) {
            throw new RuntimeException("strDate is null");
        }
        if (pattern == null || pattern.equals("")) {
            pattern = PATTERN_STANDARD19H;
        }
        try {
            return new SimpleDateFormat(pattern).parse(strDate);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String getCurrentTime(String format) {
        return new SimpleDateFormat(format).format(new Date());
    }

    public static String getWantDate(String dateStr, String wantFormat) {
        if (!("".equals(dateStr) || dateStr == null)) {
            String pattern = PATTERN_STANDARD14W;
            switch (dateStr.length()) {
                case 8:
                    pattern = PATTERN_STANDARD08W;
                    break;
                case 10:
                    pattern = dateStr.contains("-") ? PATTERN_STANDARD10H : PATTERN_STANDARD10X;
                    break;
                case 12:
                    pattern = PATTERN_STANDARD12W;
                    break;
                case 14:
                    pattern = PATTERN_STANDARD14W;
                    break;
                case 16:
                    pattern = dateStr.contains("-") ? PATTERN_STANDARD16H : PATTERN_STANDARD16X;
                    break;
                case 17:
                    pattern = PATTERN_STANDARD17W;
                    break;
                case 19:
                    pattern = dateStr.contains("-") ? PATTERN_STANDARD19H : PATTERN_STANDARD19X;
                    break;
                default:
                    pattern = PATTERN_STANDARD14W;
                    break;
            }
            try {
                dateStr = new SimpleDateFormat(wantFormat).format(new SimpleDateFormat(pattern).parse(dateStr));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return dateStr;
    }

    public static String getAfterTime(String dateStr, int minute) {
        String returnStr = "";
        try {
            String pattern = PATTERN_STANDARD14W;
            switch (dateStr.length()) {
                case 8:
                    pattern = PATTERN_STANDARD08W;
                    break;
                case 10:
                    pattern = PATTERN_STANDARD10H;
                    break;
                case 12:
                    pattern = PATTERN_STANDARD12W;
                    break;
                case 14:
                    pattern = PATTERN_STANDARD14W;
                    break;
                case 16:
                    pattern = PATTERN_STANDARD16H;
                    break;
                case 17:
                    pattern = PATTERN_STANDARD17W;
                    break;
                case 19:
                    pattern = PATTERN_STANDARD19H;
                    break;
                default:
                    pattern = PATTERN_STANDARD14W;
                    break;
            }
            SimpleDateFormat formatDate = new SimpleDateFormat(pattern);
            return formatDate.format(new Date(formatDate.parse(dateStr).getTime() + ((long) (60000 * minute))));
        } catch (Exception e) {
            returnStr = dateStr;
            e.printStackTrace();
            return returnStr;
        }
    }

    public static String getBeforeTime(String dateStr, int minute) {
        String returnStr = "";
        try {
            String pattern = PATTERN_STANDARD14W;
            switch (dateStr.length()) {
                case 8:
                    pattern = PATTERN_STANDARD08W;
                    break;
                case 10:
                    pattern = PATTERN_STANDARD10H;
                    break;
                case 12:
                    pattern = PATTERN_STANDARD12W;
                    break;
                case 14:
                    pattern = PATTERN_STANDARD14W;
                    break;
                case 16:
                    pattern = PATTERN_STANDARD16H;
                    break;
                case 17:
                    pattern = PATTERN_STANDARD17W;
                    break;
                case 19:
                    pattern = PATTERN_STANDARD19H;
                    break;
                default:
                    pattern = PATTERN_STANDARD14W;
                    break;
            }
            SimpleDateFormat formatDate = new SimpleDateFormat(pattern);
            return formatDate.format(new Date(formatDate.parse(dateStr).getTime() - ((long) (60000 * minute))));
        } catch (Exception e) {
            returnStr = dateStr;
            e.printStackTrace();
            return returnStr;
        }
    }
}
