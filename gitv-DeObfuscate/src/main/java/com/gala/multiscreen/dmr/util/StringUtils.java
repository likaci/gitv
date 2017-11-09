package com.gala.multiscreen.dmr.util;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
    public static boolean isAllChar(String str) {
        return Pattern.compile("^[A-Za-z]+$").matcher(str).matches();
    }

    public static boolean isEquals(String str, String splitStr) {
        try {
            String[] splitChars = splitStr.split(",");
            for (Object equals : splitChars) {
                if (str.equals(equals)) {
                    return true;
                }
            }
        } catch (Exception e) {
        }
        return false;
    }

    public static String matchFuzzyValue(String message, List<String> keys) {
        String matchKey = null;
        String result = null;
        for (String key : keys) {
            String str1;
            String str2;
            if (key.length() > message.length()) {
                str1 = key;
                str2 = message;
            } else {
                str1 = message;
                str2 = key;
            }
            if (str1.toLowerCase().contains(str2.toLowerCase()) && (matchKey == null || str2.length() > matchKey.length())) {
                matchKey = str2;
                result = key;
            }
        }
        return result;
    }

    public static String matchEntirelyValue(String message, List<String> keys) {
        for (String key : keys) {
            if (message.toLowerCase().equals(key.toLowerCase())) {
                return key;
            }
        }
        return null;
    }

    public static boolean isContains(String str, String replyValue) {
        String[] arr = replyValue.split(",");
        for (String toLowerCase : arr) {
            if (str.toLowerCase().contains(toLowerCase.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    public static boolean isAllContains(String str, String replyValue) {
        String[] arr = replyValue.split(",");
        for (String toLowerCase : arr) {
            if (!str.toLowerCase().contains(toLowerCase.toLowerCase())) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNullAfterA(String str, String splitStr) {
        try {
            String[] splitChars = splitStr.split(",");
            for (String afterAString : splitChars) {
                String result = getAfterAString(str, afterAString);
                if (result == null || result.equals("")) {
                    return true;
                }
            }
        } catch (Exception e) {
        }
        return false;
    }

    public static String getAfterAString(String str, String splitStr) {
        try {
            String[] splitChars = splitStr.split(",");
            for (int i = 0; i < splitChars.length; i++) {
                if (Pattern.compile(splitChars[i]).matcher(str).find()) {
                    str = getAfterA(str, splitChars[i]);
                    break;
                }
            }
        } catch (Exception e) {
        }
        return str;
    }

    public static String getBeforeAString(String str, String splitStr) {
        try {
            String[] splitChars = splitStr.split(",");
            for (int i = 0; i < splitChars.length; i++) {
                if (Pattern.compile(splitChars[i]).matcher(str).find()) {
                    str = getBeforeA(str, splitChars[i]);
                    break;
                }
            }
        } catch (Exception e) {
        }
        return str;
    }

    public static String filter(String value, String filterValue) {
        try {
            String[] splitChars = filterValue.split(",");
            for (String toLowerCase : splitChars) {
                value = value.replace(toLowerCase.toLowerCase(), "");
            }
        } catch (Exception e) {
        }
        return value;
    }

    public static String getBetweenAandB(String value, String a, String b) {
        try {
            Matcher m = Pattern.compile(a + "(.*?)" + b).matcher(value);
            if (m.find()) {
                return filter(m.group(0), a + "," + b);
            }
        } catch (Exception e) {
        }
        return value;
    }

    private static String getBeforeA(String value, String a) {
        try {
            value = getBetweenAandB("#" + value, "#", a.toLowerCase());
        } catch (Exception e) {
        }
        return value;
    }

    private static String getAfterA(String value, String a) {
        try {
            value = getBetweenAandB(value + "#", a.toLowerCase(), "#");
        } catch (Exception e) {
        }
        return value;
    }

    public static int parseInt(String value) {
        int result = -1;
        try {
            result = Integer.parseInt(value);
        } catch (Exception e) {
        }
        return result;
    }

    public static String filterBeginning(String value, String filterValue) {
        if (value != null && filterValue != null) {
            try {
                String[] splitChars = filterValue.split(",");
                for (String filterStr : splitChars) {
                    if (value.toLowerCase().indexOf(filterStr.toLowerCase()) == 0) {
                        value = value.substring(filterStr.length(), value.length());
                        break;
                    }
                }
            } catch (Exception e) {
            }
        }
        return value;
    }
}
