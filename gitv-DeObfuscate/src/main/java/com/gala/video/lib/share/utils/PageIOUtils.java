package com.gala.video.lib.share.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import com.gala.video.lib.share.C1632R;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;

public class PageIOUtils {
    private static final String TAG = "PageIOUtils";

    public static void activityIn(Context context, Intent intent) {
        callStart(context, intent, -1);
    }

    public static void activityIn(Activity currActivity, Intent intent) {
        activityIn(currActivity, intent, -1);
    }

    public static void activityIn(Context context, Intent intent, int requestCode) {
        callStart(context, intent, requestCode);
    }

    public static void activityIn(Activity a, Intent intent, int requestCode) {
        callStart(a, intent, requestCode);
    }

    public static void activityInTransition(Activity a) {
        a.overridePendingTransition(0, 0);
    }

    private static void callStart(Context context, Intent intent, int requestCode) {
        GetInterfaceTools.getIActionManager().startActivity(context, intent, requestCode);
    }

    public static void activityOut(Activity a) {
        a.finish();
        a.overridePendingTransition(0, C1632R.anim.share_page_exit);
    }

    public static void activityOutAnim(Activity a) {
        a.overridePendingTransition(0, C1632R.anim.share_page_exit);
    }
}
