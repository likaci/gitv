package com.gala.video.app.epg.ui.search;

import android.content.Context;
import android.content.Intent;
import com.gala.video.app.epg.R;
import com.gala.video.lib.framework.core.utils.NetworkUtils;
import com.gala.video.lib.share.common.widget.QToast;
import com.gala.video.lib.share.utils.PageIOUtils;

public class SearchEnterUtils {
    public static void startSearchActivity(Context context, int channelId, String channelName, int flag) {
        if (context != null) {
            Intent intent = new Intent(context, QSearchActivity.class);
            intent.putExtra("channel_id", channelId);
            intent.putExtra(ISearchConstant.CHANNEL_NAME, channelName);
            intent.putExtra("tvsrchsource", channelName);
            intent.setFlags(flag);
            PageIOUtils.activityIn(context, intent);
        }
    }

    public static void startSearchActivity(Context context, int flag) {
        if (context != null) {
            Intent intent = new Intent(context, QSearchActivity.class);
            intent.putExtra("tvsrchsource", ISearchConstant.TVSRCHSOURCE_MSG);
            intent.setFlags(flag);
            PageIOUtils.activityIn(context, intent);
        }
    }

    public static void startSearchActivity(Context context) {
        if (context != null) {
            Intent intent = new Intent(context, QSearchActivity.class);
            intent.putExtra("tvsrchsource", "tab");
            PageIOUtils.activityIn(context, intent);
        }
    }

    public static void startSearchActivityOpenApi(Context context, int channelId, String channelName) {
        if (context != null) {
            Intent intent = new Intent(context, QSearchActivity.class);
            intent.putExtra("channel_id", channelId);
            intent.putExtra(ISearchConstant.CHANNEL_NAME, channelName);
            intent.putExtra("from_openapi", true);
            intent.putExtra("tvsrchsource", ISearchConstant.TVSRCHSOURCE_OTHER);
            PageIOUtils.activityIn(context, intent);
        }
    }

    public static void startSearchActivityOpenApi(Context context, int flags) {
        if (context != null) {
            Intent intent = new Intent(context, QSearchActivity.class);
            intent.setFlags(flags);
            intent.putExtra("from_openapi", true);
            intent.putExtra("tvsrchsource", ISearchConstant.TVSRCHSOURCE_OTHER);
            PageIOUtils.activityIn(context, intent);
        }
    }

    public static boolean checkNetWork(Context context) {
        if (context == null) {
            return false;
        }
        if (NetworkUtils.isNetworkAvaliable()) {
            return true;
        }
        QToast.makeTextAndShow(context, R.string.result_no_net, 2000);
        return false;
    }
}
