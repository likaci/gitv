package com.tvos.apps.utils;

import android.annotation.SuppressLint;
import android.util.Base64;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formatter;
import java.util.Locale;
import java.util.regex.Pattern;
import org.cybergarage.xml.XML;

public class StringUtils {
    private static StringBuilder mFormatBuilder = new StringBuilder();
    private static Formatter mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());

    public static boolean isPureEnglishStr(String str) {
        if (str == null || str.length() == 0 || Pattern.compile("[^\\u0041-\\u005A]").matcher(str.toUpperCase()).find()) {
            return false;
        }
        return true;
    }

    public static String ToHalfStr(String input) {
        if (isEmpty(input)) {
            return null;
        }
        char[] changeStr = input.replaceAll("\r|\n", "").toCharArray();
        int i = 0;
        while (i < changeStr.length) {
            if (changeStr[i] == '　') {
                changeStr[i] = ' ';
            } else if (changeStr[i] > '＀' && changeStr[i] < '｟') {
                changeStr[i] = (char) (changeStr[i] - 65248);
            }
            i++;
        }
        return new String(changeStr);
    }

    public static String fetchAppFileName(String mAppDownloadUrl) {
        if (isEmpty(mAppDownloadUrl)) {
            return null;
        }
        return mAppDownloadUrl.substring(mAppDownloadUrl.lastIndexOf("/") + 1, mAppDownloadUrl.length());
    }

    @SuppressLint({"SimpleDateFormat"})
    public static String TimeStampDate(String timestampString) {
        return new SimpleDateFormat(DateUtil.PATTERN_STANDARD10H).format(new Date(parseLong(timestampString)));
    }

    @SuppressLint({"DefaultLocale"})
    public static String ByteToTrillion(String byteString) {
        if (isEmpty(byteString)) {
            return "未知";
        }
        if (Float.valueOf(byteString).floatValue() > 1048576.0f) {
            return new StringBuilder(String.valueOf(String.format("%.1f", new Object[]{Float.valueOf(Float.valueOf(byteString).floatValue() / 1048576.0f)}))).append("MB").toString();
        }
        return new StringBuilder(String.valueOf(String.format("%.1f", new Object[]{Float.valueOf(Float.valueOf(byteString).floatValue() / 1024.0f)}))).append("KB").toString();
    }

    public static boolean isEmpty(String... strs) {
        if (strs == null) {
            return true;
        }
        for (String str : strs) {
            if (str != null && !str.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public static boolean hasEmpty(String... strs) {
        if (strs == null) {
            return true;
        }
        for (String str : strs) {
            if (str == null || str.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public static int parseInt(String str) {
        if (str != null) {
            try {
                return Integer.parseInt(str.trim());
            } catch (Exception e) {
            }
        }
        return 0;
    }

    public static long parseLong(String str) {
        if (str != null) {
            try {
                return Long.parseLong(str.trim());
            } catch (Exception e) {
            }
        }
        return 0;
    }

    public static String base64(String str) {
        try {
            return Base64.encodeToString(str.getBytes(XML.CHARSET_UTF8), 1).toString().trim();
        } catch (Exception e) {
            return "";
        }
    }

    public static String md5(String str) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(str.getBytes("UTF-8"));
            byte[] bytes = messageDigest.digest();
            StringBuffer sbuffer = new StringBuffer();
            for (int i = 0; i < bytes.length; i++) {
                if (Integer.toHexString(bytes[i] & 255).length() == 1) {
                    sbuffer.append("0").append(Integer.toHexString(bytes[i] & 255));
                } else {
                    sbuffer.append(Integer.toHexString(bytes[i] & 255));
                }
            }
            return sbuffer.toString();
        } catch (Exception e) {
            return "";
        }
    }

    public static String replaceBlank(String src) {
        return Pattern.compile("\\s*|\t|\r|\n").matcher(src).replaceAll("");
    }

    public static String md5MultScreen(String str) {
        String md5str = md5(str);
        if (md5str == null || md5str.length() == 0) {
            return "";
        }
        String result = "";
        if (md5str.length() != 32) {
            return result;
        }
        StringBuffer sbuffer = new StringBuffer();
        String str1 = md5str.substring(0, 6);
        String str2 = md5str.substring(6, 16);
        String str3 = md5str.substring(16, 26);
        String str4 = md5str.substring(26, md5str.length());
        sbuffer.append(str1);
        sbuffer.append(str4);
        sbuffer.append(str3);
        sbuffer.append(str2);
        return md5(sbuffer.reverse().substring(4, 15));
    }

    public static String formatLongToTimeStr(Long l) {
        int minute = 0;
        int second = l.intValue() / 1000;
        if (second > 60) {
            minute = second / 60;
            second %= 60;
        }
        return new StringBuilder(String.valueOf(minute)).append("分").toString();
    }

    public static String stringForTime(int timeMs, boolean isFull) {
        int totalSeconds = timeMs / 1000;
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;
        mFormatBuilder.setLength(0);
        if (isFull) {
            return mFormatter.format("%02d:%02d:%02d", new Object[]{Integer.valueOf(hours), Integer.valueOf(minutes), Integer.valueOf(seconds)}).toString();
        } else if (hours > 0) {
            return mFormatter.format("%d:%02d:%02d", new Object[]{Integer.valueOf(hours), Integer.valueOf(minutes), Integer.valueOf(seconds)}).toString();
        } else {
            return mFormatter.format("%02d:%02d", new Object[]{Integer.valueOf(minutes), Integer.valueOf(seconds)}).toString();
        }
    }

    public static String stringForTime(int timeMs) {
        return stringForTime(timeMs, false);
    }

    public static String filterSuffix(String str) {
        return str;
    }

    public static boolean isMailAddress(String mail) {
        if (isEmpty(mail)) {
            return false;
        }
        return Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*").matcher(mail).matches();
    }

    public static boolean checkPassword(String input_password) {
        int length = input_password.length();
        if (length < 4 || length > 20) {
            return false;
        }
        return Pattern.compile("[a-z0-9A-Z]+").matcher(input_password).matches();
    }

    public static boolean isMobileNO(String mobiles) {
        if (isEmpty(mobiles)) {
            return false;
        }
        return Pattern.compile("^\\d{11}$").matcher(mobiles).matches();
    }
}
