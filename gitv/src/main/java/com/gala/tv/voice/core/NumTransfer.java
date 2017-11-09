package com.gala.tv.voice.core;

public class NumTransfer {
    private static final String[] a = new String[]{"yi", "er", "san", "si", "wu", "liu", "qi", "ba", "jiu", "shi", "shiyi", "shier"};
    private static final String[] b = new String[]{"一", "二", "三", "四", "五", "六", "七", "八", "九"};

    public static int transferHanzi2Num(String str) {
        int i = 0;
        if (VoiceUtils.isEmpty(str)) {
            return -1;
        }
        int i2;
        String b;
        int i3;
        int i4;
        for (i2 = 0; i2 < a.length; i2++) {
            if (a[i2].equals(str)) {
                return i2 + 1;
            }
        }
        if (str.contains("百")) {
            String a = StringUtils.a(str, "百");
            if (!VoiceUtils.isEmpty(a)) {
                i2 = 0;
                while (i2 < b.length) {
                    if (b[i2].equals(a)) {
                        break;
                    }
                    i2++;
                }
            }
            i2 = -1;
            b = StringUtils.b(str, "百零");
            a = StringUtils.b(str, "百");
            if (!VoiceUtils.isEmpty(b)) {
                str = b;
            } else if (!VoiceUtils.isEmpty(a)) {
                str = a;
            }
        } else {
            i2 = -1;
        }
        if (str.contains("十")) {
            b = StringUtils.a(str, "十");
            if (!VoiceUtils.isEmpty(b)) {
                i3 = 0;
                while (i3 < b.length) {
                    if (b[i3].equals(b)) {
                        break;
                    }
                    i3++;
                }
            }
            i3 = -1;
            if (i3 == -1) {
                i3 = 0;
            }
            b = StringUtils.b(str, "十");
            if (!VoiceUtils.isEmpty(b)) {
                str = b;
            }
        } else {
            i3 = -1;
        }
        if (!VoiceUtils.isEmpty(str)) {
            i4 = 0;
            while (i4 < b.length) {
                if (b[i4].equals(str)) {
                    break;
                }
                i4++;
            }
        }
        i4 = -1;
        if (i2 == -1 && i3 == -1 && i4 == -1) {
            return -1;
        }
        int i5 = i2 >= 0 ? (i2 + 1) * 100 : 0;
        if (i3 >= 0) {
            i2 = (i3 + 1) * 10;
        } else {
            i2 = 0;
        }
        if (i4 >= 0) {
            i = i4 + 1;
        }
        return (i2 + i5) + i;
    }
}
