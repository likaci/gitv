package com.gala.video.lib.framework.core.utils;

import android.util.Base64;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;
import org.cybergarage.xml.XML;

public class StringUtils {
    private static final String CUSTOMER_PKGNAME_SPLIT = ";";
    private static final String[] EMPTY_STRING_ARRAY = new String[0];
    private static StringBuilder mFormatBuilder = new StringBuilder();
    private static Formatter mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());

    public static boolean isEmpty(CharSequence str) {
        return str == null || str.length() == 0;
    }

    public static String append(String str1, String str2) {
        boolean empty1 = isEmpty((CharSequence) str1);
        boolean empty2 = isEmpty((CharSequence) str2);
        if (empty1 && !empty2) {
            return str2;
        }
        if (!empty1 && !empty2) {
            return str1 + str2;
        }
        if (empty1 || !empty2) {
            return null;
        }
        return str1;
    }

    public static int getLength(CharSequence str) {
        return isEmpty(str) ? 0 : str.length();
    }

    public static boolean isTrimEmpty(String text) {
        if (text == null || text.trim().equals("")) {
            return true;
        }
        return false;
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

    public static int parse(String str, int defaultValue) {
        int value = defaultValue;
        try {
            value = Integer.parseInt(str);
        } catch (NumberFormatException e) {
            LogUtils.d("", "parse exception input = " + str, e);
        }
        return value;
    }

    public static long parse(String str, long defaultValue) {
        long value = defaultValue;
        try {
            value = Long.parseLong(str);
        } catch (NumberFormatException e) {
        }
        return value;
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
        return minute + "分";
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

    public static List<String> parseStringtoList(String string) {
        String[] StringArray = string.split(CUSTOMER_PKGNAME_SPLIT);
        List<String> stringList = new ArrayList();
        if (StringArray != null) {
            int i = 0;
            int size = StringArray.length;
            while (i < size) {
                if (!(StringArray[i] == null || "".equals(StringArray[i].trim()))) {
                    stringList.add(StringArray[i]);
                }
                i++;
            }
        }
        return stringList;
    }

    public static boolean isMailAddress(String mail) {
        if (isEmpty((CharSequence) mail)) {
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
        if (isEmpty((CharSequence) mobiles)) {
            return false;
        }
        return Pattern.compile("^\\d{11}$").matcher(mobiles).matches();
    }

    public static boolean equals(CharSequence cs1, CharSequence cs2) {
        if (cs1 == null) {
            return cs2 == null;
        } else {
            return cs1.equals(cs2);
        }
    }

    public static boolean isBlank(CharSequence cs) {
        if (cs == null) {
            return true;
        }
        int strLen = cs.length();
        if (strLen == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static String[] split(String str, String separatorChars) {
        return splitWorker(str, separatorChars, -1, false);
    }

    private static String[] splitWorker(String str, String separatorChars, int max, boolean preserveAllTokens) {
        if (str == null) {
            return null;
        }
        int len = str.length();
        if (len == 0) {
            return EMPTY_STRING_ARRAY;
        }
        List<String> list = new ArrayList();
        int i = 0;
        int start = 0;
        boolean match = false;
        boolean lastMatch = false;
        int sizePlus1;
        int sizePlus12;
        if (separatorChars == null) {
            sizePlus1 = 1;
            while (i < len) {
                if (Character.isWhitespace(str.charAt(i))) {
                    if (match || preserveAllTokens) {
                        lastMatch = true;
                        sizePlus12 = sizePlus1 + 1;
                        if (sizePlus1 == max) {
                            i = len;
                            lastMatch = false;
                        }
                        list.add(str.substring(start, i));
                        match = false;
                    } else {
                        sizePlus12 = sizePlus1;
                    }
                    i++;
                    start = i;
                    sizePlus1 = sizePlus12;
                } else {
                    lastMatch = false;
                    match = true;
                    i++;
                }
            }
        } else if (separatorChars.length() == 1) {
            char sep = separatorChars.charAt(0);
            sizePlus1 = 1;
            while (i < len) {
                if (str.charAt(i) == sep) {
                    if (match || preserveAllTokens) {
                        lastMatch = true;
                        sizePlus12 = sizePlus1 + 1;
                        if (sizePlus1 == max) {
                            i = len;
                            lastMatch = false;
                        }
                        list.add(str.substring(start, i));
                        match = false;
                    } else {
                        sizePlus12 = sizePlus1;
                    }
                    i++;
                    start = i;
                    sizePlus1 = sizePlus12;
                } else {
                    lastMatch = false;
                    match = true;
                    i++;
                }
            }
            sizePlus12 = sizePlus1;
        } else {
            sizePlus1 = 1;
            while (i < len) {
                if (separatorChars.indexOf(str.charAt(i)) >= 0) {
                    if (match || preserveAllTokens) {
                        lastMatch = true;
                        sizePlus12 = sizePlus1 + 1;
                        if (sizePlus1 == max) {
                            i = len;
                            lastMatch = false;
                        }
                        list.add(str.substring(start, i));
                        match = false;
                    } else {
                        sizePlus12 = sizePlus1;
                    }
                    i++;
                    start = i;
                    sizePlus1 = sizePlus12;
                } else {
                    lastMatch = false;
                    match = true;
                    i++;
                }
            }
        }
        if (match || (preserveAllTokens && lastMatch)) {
            list.add(str.substring(start, i));
        }
        return (String[]) list.toArray(new String[list.size()]);
    }
}
