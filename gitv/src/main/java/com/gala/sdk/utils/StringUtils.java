package com.gala.sdk.utils;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class StringUtils {
    private static final String[] a = new String[0];

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

    public static int parseInt(String str, int defaultValue) {
        if (!(str == null || str.isEmpty())) {
            try {
                defaultValue = Integer.parseInt(str.trim());
            } catch (Exception e) {
            }
        }
        return defaultValue;
    }

    public static long parseLong(String str, long defaultValue) {
        if (!(str == null || str.isEmpty())) {
            try {
                defaultValue = Long.parseLong(str.trim());
            } catch (Exception e) {
            }
        }
        return defaultValue;
    }

    public static int parseInt(String str) {
        return parseInt(str, 0);
    }

    public static long parseLong(String str) {
        return parseLong(str, 0);
    }

    public static String replaceBlank(String src) {
        return Pattern.compile("\\s*|\t|\r|\n").matcher(src).replaceAll("");
    }

    public static boolean equals(CharSequence cs1, CharSequence cs2) {
        if (cs1 == null) {
            return cs2 == null;
        } else {
            return cs1.equals(cs2);
        }
    }

    public static boolean equals(String cs1, String cs2) {
        if (cs1 == null) {
            return cs2 == null;
        } else {
            return cs1.equals(cs2);
        }
    }

    public static boolean isBlank(CharSequence cs) {
        if (cs != null) {
            int length = cs.length();
            if (length != 0) {
                for (int i = 0; i < length; i++) {
                    if (!Character.isWhitespace(cs.charAt(i))) {
                        return false;
                    }
                }
                return true;
            }
        }
        return true;
    }

    public static String[] split(String str, String separatorChars) {
        if (str == null) {
            return null;
        }
        int length = str.length();
        if (length == 0) {
            return a;
        }
        int i;
        int i2;
        List arrayList = new ArrayList();
        int i3;
        int i4;
        int i5;
        if (separatorChars == null) {
            i = 0;
            i3 = 0;
            i2 = 0;
            i4 = 1;
            while (i2 < length) {
                if (Character.isWhitespace(str.charAt(i2))) {
                    if (i != 0) {
                        i = i4 + 1;
                        if (i4 == -1) {
                            i2 = length;
                        }
                        arrayList.add(str.substring(i3, i2));
                        i4 = i;
                        i = i2;
                        i2 = 0;
                    } else {
                        i5 = i;
                        i = i2;
                        i2 = i5;
                    }
                    i3 = i + 1;
                    i = i2;
                    i2 = i3;
                } else {
                    i2++;
                    i = 1;
                }
            }
            length = i2;
            i2 = i3;
        } else if (separatorChars.length() == 1) {
            char charAt = separatorChars.charAt(0);
            i = 0;
            i3 = 0;
            i2 = 0;
            i4 = 1;
            while (i2 < length) {
                if (str.charAt(i2) == charAt) {
                    if (i != 0) {
                        i = i4 + 1;
                        if (i4 == -1) {
                            i2 = length;
                        }
                        arrayList.add(str.substring(i3, i2));
                        i4 = i;
                        i = i2;
                        i2 = 0;
                    } else {
                        i5 = i;
                        i = i2;
                        i2 = i5;
                    }
                    i3 = i + 1;
                    i = i2;
                    i2 = i3;
                } else {
                    i2++;
                    i = 1;
                }
            }
            length = i2;
            i2 = i3;
        } else {
            i = 0;
            i3 = 0;
            i2 = 0;
            i4 = 1;
            while (i2 < length) {
                if (separatorChars.indexOf(str.charAt(i2)) >= 0) {
                    if (i != 0) {
                        i = i4 + 1;
                        if (i4 == -1) {
                            i2 = length;
                        }
                        arrayList.add(str.substring(i3, i2));
                        i4 = i;
                        i = i2;
                        i2 = 0;
                    } else {
                        i5 = i;
                        i = i2;
                        i2 = i5;
                    }
                    i3 = i + 1;
                    i = i2;
                    i2 = i3;
                } else {
                    i2++;
                    i = 1;
                }
            }
            length = i2;
            i2 = i3;
        }
        if (i != 0) {
            arrayList.add(str.substring(i2, length));
        }
        return (String[]) arrayList.toArray(new String[arrayList.size()]);
    }

    public static String md5(String str) {
        try {
            MessageDigest instance = MessageDigest.getInstance("MD5");
            instance.reset();
            instance.update(str.getBytes("UTF-8"));
            byte[] digest = instance.digest();
            StringBuffer stringBuffer = new StringBuffer();
            for (int i = 0; i < digest.length; i++) {
                if (Integer.toHexString(digest[i] & 255).length() == 1) {
                    stringBuffer.append("0").append(Integer.toHexString(digest[i] & 255));
                } else {
                    stringBuffer.append(Integer.toHexString(digest[i] & 255));
                }
            }
            return stringBuffer.toString();
        } catch (Exception e) {
            return "";
        }
    }
}
