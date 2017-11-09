package com.gala.video.app.epg.ui.applist;

import android.content.Context;
import android.content.Intent;
import com.gala.video.lib.share.utils.PageIOUtils;
import com.push.mqttv3.internal.ClientDefaults;

public class AppListUtils {
    public static final String KEY_RPAGE = "KEY_RPAGE";

    public static void startAppListActivity(Context activity, String rpage) {
        Intent intent = new Intent(activity, AppListActivity.class);
        intent.putExtra(KEY_RPAGE, rpage);
        intent.addFlags(ClientDefaults.MAX_MSG_SIZE);
        PageIOUtils.activityIn(activity, intent);
    }
}
