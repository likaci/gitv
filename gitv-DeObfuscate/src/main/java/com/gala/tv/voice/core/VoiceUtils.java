package com.gala.tv.voice.core;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.Process;
import android.view.View;
import com.gala.video.app.epg.ui.albumlist.utils.AlbumEnterFactory;
import java.util.ArrayList;
import java.util.List;

public class VoiceUtils {
    public static final int INTENT_FLAG_DEFAULT = 335544320;
    public static final int INTENT_FLAG_INVALID = -1;

    private VoiceUtils() {
    }

    public static void copyBundle(Bundle bundle, Bundle bundle2) {
        dumpBundle("copyBundle()", " target=", bundle);
        dumpBundle("copyBundle()", " source=", bundle2);
        if (!(bundle == null || bundle2 == null)) {
            bundle.putAll(bundle2);
        }
        dumpBundle("copyBundle()", "final=", bundle);
    }

    public static void dumpBundle(String str, String str2, Bundle bundle) {
        if (bundle != null) {
            Log.m525d(str, str2 + " bundle size=" + bundle.size());
            for (String str3 : bundle.keySet()) {
                Log.m525d(str, str2 + " key[" + str3 + "]=" + bundle.get(str3));
            }
            return;
        }
        Log.m525d(str, str2 + " Null Bundle");
    }

    public static void dumpIntent(String str, Intent intent) {
        if (intent != null) {
            Log.m525d("Utils", "dumpIntent() " + str + ": action=" + intent.getAction());
            Log.m525d("Utils", "dumpIntent() " + str + ": packageName=" + intent.getPackage());
            Log.m525d("Utils", "dumpIntent() " + str + ": categories=" + intent.getCategories());
            Log.m525d("Utils", "dumpIntent() " + str + ": flag=" + intent.getFlags() + "[" + Integer.toBinaryString(intent.getFlags()) + AlbumEnterFactory.SIGN_STR);
            dumpBundle(str, "dumpIntent()", intent.getExtras());
            return;
        }
        Log.m525d("Utils", "dumpIntent() " + str + ": Null Intent");
    }

    public static Bundle createResultBundle(int i) {
        Bundle bundle = new Bundle();
        ParamsHelper.setResultCode(bundle, i);
        return bundle;
    }

    public static <T extends Parcelable> Bundle createResultBundle(int i, ArrayList<T> arrayList) {
        Bundle bundle = new Bundle();
        ParamsHelper.setResultCode(bundle, i);
        ParamsHelper.setResultData(bundle, (ArrayList) arrayList);
        return bundle;
    }

    public static Bundle createResultBundle(int i, boolean z) {
        Bundle bundle = new Bundle();
        ParamsHelper.setResultCode(bundle, i);
        ParamsHelper.setResultData(bundle, z);
        return bundle;
    }

    public static void assertTrue(boolean z, String str) {
        if (!z) {
            Log.m529w("Utils", "assertTrue() fail! message is: " + str);
            throw new IllegalArgumentException(str);
        }
    }

    public static boolean isEmpty(List<?> list) {
        return list == null || list.isEmpty();
    }

    public static boolean isEmpty(String str) {
        return str == null || str.trim().equals("");
    }

    public static boolean equal(String str, String str2) {
        if (isEmpty(str) && isEmpty(str2)) {
            return true;
        }
        if (isEmpty(str) || isEmpty(str2) || !str.equals(str2)) {
            return false;
        }
        return true;
    }

    public static String dumpViewState(View view) {
        if (view == null) {
            return "NULL@View";
        }
        return "View@[" + view + "](isClickable=" + view.isClickable() + ", isEnabled=" + view.isEnabled() + ", isFocusable=" + view.isFocusable() + ", isActivated=" + view.isActivated() + ", isShown=" + view.isShown() + ", isFocused=" + view.isFocused() + ", isSelected=" + view.isSelected();
    }

    public static boolean contain(String str, String str2) {
        if (isEmpty(str) && isEmpty(str2)) {
            return true;
        }
        if (!(isEmpty(str) || isEmpty(str2))) {
            if (str.equals(str2)) {
                return true;
            }
            if (str2.length() <= str.length()) {
                String str3 = str;
                str = str2;
                str2 = str3;
            }
            if (str2.contains(str)) {
                return true;
            }
        }
        return false;
    }

    public static boolean match(String str, String str2) {
        if (isEmpty(str) && isEmpty(str2)) {
            return true;
        }
        if (!(isEmpty(str) || isEmpty(str2))) {
            if (str.equals(str2)) {
                return true;
            }
            int transferHanzi2Num = NumTransfer.transferHanzi2Num(StringUtils.m532a(str, "第", "集"));
            if (transferHanzi2Num == -1) {
                transferHanzi2Num = NumTransfer.transferHanzi2Num(StringUtils.m531a(str, "集"));
            }
            boolean z = transferHanzi2Num != -1 && ("第" + transferHanzi2Num + "集").equals(str2);
            if (z) {
                return true;
            }
            transferHanzi2Num = NumTransfer.transferHanzi2Num(StringUtils.m532a(str, "第", "个"));
            if (transferHanzi2Num == -1) {
                transferHanzi2Num = NumTransfer.transferHanzi2Num(StringUtils.m531a(str, "个"));
            }
            z = transferHanzi2Num != -1 && ("第" + transferHanzi2Num + "个").equals(str2);
            if (z) {
                return true;
            }
        }
        return false;
    }

    public static String getCurProcessName(Context context) {
        String str = "";
        int myPid = Process.myPid();
        for (RunningAppProcessInfo runningAppProcessInfo : ((ActivityManager) context.getSystemService("activity")).getRunningAppProcesses()) {
            String str2;
            if (runningAppProcessInfo.pid == myPid) {
                str2 = runningAppProcessInfo.processName;
            } else {
                str2 = str;
            }
            str = str2;
        }
        Log.m525d("Utils", "getCurProcessName() = " + str);
        return str;
    }
}
