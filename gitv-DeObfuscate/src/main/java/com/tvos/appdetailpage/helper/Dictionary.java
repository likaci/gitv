package com.tvos.appdetailpage.helper;

import com.tvos.appdetailpage.client.Constants;

public class Dictionary {
    public static String platformIDFromVer4(String pf, String p, String p1, String p2) {
        if (pf.equals("2") && p.equals("21") && p1.equals(Constants.PINGBACK_4_0_P1_ANDROID_TABLET_APP)) {
            return "11";
        }
        if (pf.equals("2") && p.equals(Constants.PINGBACK_4_0_P_PHONE_APP) && p1.equals(Constants.PINGBACK_4_0_P1_ANDROID_PHONE_APP)) {
            return Constants.PINGBACK_3_0_PLATFORM_ANDROID_PHONE;
        }
        if (pf.equals("3") && p.equals(Constants.PINGBACK_4_0_P_TV_APP) && p1.equals("312")) {
            return "5201";
        }
        return null;
    }

    public static String[] platformAndProductIDFromVer3(String platform) {
        String[] s = new String[4];
        if (platform.equals("11")) {
            s[0] = "2";
            s[1] = "21";
            s[2] = Constants.PINGBACK_4_0_P1_ANDROID_TABLET_APP;
        } else if (platform.equals(Constants.PINGBACK_3_0_PLATFORM_ANDROID_PHONE)) {
            s[0] = "2";
            s[1] = Constants.PINGBACK_4_0_P_PHONE_APP;
            s[2] = Constants.PINGBACK_4_0_P1_ANDROID_PHONE_APP;
        } else if (platform.equals("5201")) {
            s[0] = "3";
            s[1] = Constants.PINGBACK_4_0_P_TV_APP;
            s[2] = "312";
        }
        return s;
    }

    public static String searchPlatformIDFromVer3(String platform) {
        String s = "";
        if (platform.equals("11")) {
            return "5";
        }
        if (platform.equals(Constants.PINGBACK_3_0_PLATFORM_ANDROID_PHONE)) {
            return "7";
        }
        if (platform.equals("5201")) {
            return "9";
        }
        return s;
    }

    public static String recocommendPlatformIDFromVer3(String platform) {
        String s = "";
        if (platform.equals("11")) {
            return "5";
        }
        if (platform.equals(Constants.PINGBACK_3_0_PLATFORM_ANDROID_PHONE)) {
            return "7";
        }
        if (platform.equals("5201")) {
            return "9";
        }
        return s;
    }
}
