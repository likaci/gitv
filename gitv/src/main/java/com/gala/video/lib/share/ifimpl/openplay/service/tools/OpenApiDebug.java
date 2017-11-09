package com.gala.video.lib.share.ifimpl.openplay.service.tools;

import android.os.Bundle;
import com.gala.video.app.epg.ui.albumlist.utils.AlbumEnterFactory;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.qiyi.tv.client.data.UserTags;
import org.cybergarage.upnp.std.av.server.object.SearchCriteria;

public class OpenApiDebug {
    public static void dumpBundle(String tag, String msg, Bundle bundle) {
        if (bundle != null) {
            LogUtils.d(tag, msg + " bundle size=" + bundle.size());
            for (String key : bundle.keySet()) {
                LogUtils.d(tag, msg + " key[" + key + "]=" + bundle.get(key));
            }
            return;
        }
        LogUtils.d(tag, msg + " Null Bundle");
    }

    public static String toString(UserTags bundle) {
        if (bundle == null) {
            return "Null@Bundle";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Bundle@[size=").append(bundle.size());
        for (String key : bundle.keySet()) {
            sb.append(",");
            sb.append(key);
            sb.append(SearchCriteria.EQ);
            sb.append(bundle.get(key));
            sb.append(AlbumEnterFactory.SIGN_STR);
        }
        return sb.toString();
    }
}
