package com.gala.sdk.utils;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import java.security.MessageDigest;
import java.util.Formatter;
import java.util.Locale;
import java.util.regex.Pattern;

public class Utils {
    private static final StringBuilder a = new StringBuilder();
    private static final Formatter f363a = new Formatter(a, Locale.getDefault());

    private Utils() {
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

    public static String stringForTime(int timeMs, boolean alwaysHour) {
        int i = timeMs / 1000;
        int i2 = i % 60;
        int i3 = (i / 60) % 60;
        i /= 3600;
        a.setLength(0);
        if (alwaysHour) {
            return f363a.format("%02d:%02d:%02d", new Object[]{Integer.valueOf(i), Integer.valueOf(i3), Integer.valueOf(i2)}).toString();
        } else if (i > 0) {
            return f363a.format("%d:%02d:%02d", new Object[]{Integer.valueOf(i), Integer.valueOf(i3), Integer.valueOf(i2)}).toString();
        } else {
            return f363a.format("%02d:%02d", new Object[]{Integer.valueOf(i3), Integer.valueOf(i2)}).toString();
        }
    }

    public static String stringForTime(int timeMs) {
        return stringForTime(timeMs, false);
    }

    public static boolean isMailAddress(String mail) {
        if (StringUtils.isEmpty(mail)) {
            return false;
        }
        return Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*").matcher(mail).matches();
    }

    public static boolean isValidPassowrd(String password) {
        int length = password.length();
        if (length < 4 || length > 20) {
            return false;
        }
        return Pattern.compile("[a-z0-9A-Z]+").matcher(password).matches();
    }

    public static boolean isMobileNO(String mobiles) {
        if (StringUtils.isEmpty(mobiles)) {
            return false;
        }
        return Pattern.compile("^\\d{11}$").matcher(mobiles).matches();
    }

    public static boolean contain(int[] list, int value) {
        boolean z = false;
        if (list != null) {
            for (int i : list) {
                if (value == i) {
                    z = true;
                    break;
                }
            }
        }
        if (Log.DEBUG) {
            Log.d("PlayerUtils/Utils", "contain() return " + z);
        }
        return z;
    }

    public static boolean equals(String str1, String str2) {
        boolean z = (str1 == null && str2 == null) || (str1 != null && str1.equals(str2));
        if (Log.DEBUG) {
            Log.d("PlayerUtils/Utils", "equal(" + str1 + ", " + str2 + ") return " + z);
        }
        return z;
    }

    @SuppressLint({"NewApi"})
    public static LayoutParams copyLayoutParams(LayoutParams params) {
        if (params instanceof RadioGroup.LayoutParams) {
            return new RadioGroup.LayoutParams(params);
        }
        if (params instanceof TableLayout.LayoutParams) {
            return new TableLayout.LayoutParams(params);
        }
        if (params instanceof TableRow.LayoutParams) {
            return new TableRow.LayoutParams(params);
        }
        if (params instanceof LinearLayout.LayoutParams) {
            return new LinearLayout.LayoutParams(params);
        }
        if (params instanceof FrameLayout.LayoutParams) {
            return new FrameLayout.LayoutParams(params);
        }
        if (params instanceof RelativeLayout.LayoutParams) {
            return new RelativeLayout.LayoutParams(params);
        }
        if (params instanceof ActionBar.LayoutParams) {
            return new ActionBar.LayoutParams(params);
        }
        if (params instanceof GridLayout.LayoutParams) {
            return new GridLayout.LayoutParams(params);
        }
        if (params instanceof MarginLayoutParams) {
            return new MarginLayoutParams(params);
        }
        return new LayoutParams(params);
    }

    @SuppressLint({"NewApi"})
    public static LayoutParams createNewLayoutParamsByType(LayoutParams params) {
        if (params instanceof RadioGroup.LayoutParams) {
            return new RadioGroup.LayoutParams(0, 0);
        }
        if (params instanceof TableLayout.LayoutParams) {
            return new TableLayout.LayoutParams(0, 0);
        }
        if (params instanceof TableRow.LayoutParams) {
            return new TableRow.LayoutParams(0, 0);
        }
        if (params instanceof LinearLayout.LayoutParams) {
            return new LinearLayout.LayoutParams(0, 0);
        }
        if (params instanceof FrameLayout.LayoutParams) {
            return new FrameLayout.LayoutParams(0, 0);
        }
        if (params instanceof RelativeLayout.LayoutParams) {
            return new RelativeLayout.LayoutParams(0, 0);
        }
        if (params instanceof ActionBar.LayoutParams) {
            return new ActionBar.LayoutParams(0, 0);
        }
        if (params instanceof GridLayout.LayoutParams) {
            return new GridLayout.LayoutParams();
        }
        if (params instanceof MarginLayoutParams) {
            return new MarginLayoutParams(0, 0);
        }
        return new LayoutParams(params);
    }

    public static LayoutParams createEmptyLayoutParamsWithSameType(LayoutParams srcParams) {
        if (srcParams == null) {
            return null;
        }
        LayoutParams layoutParams;
        Class cls = srcParams.getClass();
        try {
            layoutParams = (LayoutParams) cls.getConstructor(new Class[]{Integer.TYPE, Integer.TYPE}).newInstance(new Object[]{Integer.valueOf(0), Integer.valueOf(0)});
        } catch (Throwable e) {
            if (Log.DEBUG) {
                Log.e("PlayerUtils/Utils", "createEmptyLayoutParamsWithSameType: exception happened with (int, int) constructor:", e);
            }
            try {
                layoutParams = (LayoutParams) cls.newInstance();
            } catch (Throwable e2) {
                if (Log.DEBUG) {
                    Log.e("PlayerUtils/Utils", "createEmptyLayoutParamsWithSameType: exception happened with default constructor:", e2);
                }
                layoutParams = null;
            }
        }
        if (layoutParams == null) {
            return new LayoutParams(srcParams);
        }
        return layoutParams;
    }

    public static String getObjectDescription(Object obj) {
        if (obj == null) {
            return "NULL";
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(obj.getClass().getSimpleName()).append("@").append(Integer.toHexString(obj.hashCode()));
        return stringBuilder.toString();
    }
}
