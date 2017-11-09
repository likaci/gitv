package com.gala.video.lib.share.network;

import android.content.Context;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.utils.DeviceUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.C1632R;
import com.gala.video.lib.share.common.configs.AppClientUtils;

public class QLocalHostUtils {
    public static String getLoaclHostInfo(Context context) {
        String enter = "\n";
        String version = AppClientUtils.getClientVersion();
        String mac = DeviceUtils.getMacAddr();
        String ip = AppRuntimeEnv.get().getDeviceIp();
        String dns = DeviceUtils.getDNS();
        version = String.format(context.getString(C1632R.string.feed_back_version), new Object[]{version + enter});
        mac = String.format(context.getString(C1632R.string.feed_back_mac), new Object[]{mac + enter});
        if (StringUtils.isEmpty((CharSequence) dns)) {
            ip = String.format(context.getString(C1632R.string.feed_back_ip), new Object[]{ip});
        } else {
            ip = String.format(context.getString(C1632R.string.feed_back_ip), new Object[]{ip + enter});
            dns = String.format(context.getString(C1632R.string.feed_back_dns), new Object[]{dns});
        }
        return version + mac + ip + dns;
    }
}
