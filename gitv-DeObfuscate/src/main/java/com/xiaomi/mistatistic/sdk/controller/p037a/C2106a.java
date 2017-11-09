package com.xiaomi.mistatistic.sdk.controller.p037a;

import android.content.Context;
import android.os.Build;
import android.os.Build.VERSION;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import com.gala.video.app.epg.ui.albumlist.constant.IAlbumConfig;
import com.gala.video.app.epg.ui.setting.SettingConstants;
import com.gala.video.lib.share.common.configs.WebConstants;
import com.xiaomi.mistatistic.sdk.controller.C2105e;
import com.xiaomi.mistatistic.sdk.controller.C2111a;
import com.xiaomi.mistatistic.sdk.controller.C2116g;
import com.xiaomi.mistatistic.sdk.controller.C2122m;
import com.xiaomi.mistatistic.sdk.controller.C2128s;
import com.xiaomi.mistatistic.sdk.controller.C2129t;
import com.xiaomi.mistatistic.sdk.data.C2140c;
import com.xiaomi.mistatistic.sdk.data.C2141e;
import java.util.Locale;

public class C2106a implements C2105e {
    public void mo4534a() {
        Context a = C2111a.m1779a();
        if (!C2128s.m1853a(a, "basic_info_reported")) {
            m1770a(a);
            C2128s.m1854b(a, "basic_info_reported", 1);
            new C2129t().m1866a();
        }
        String e = C2111a.m1784e();
        Object a2 = C2128s.m1852a(a, "basic_info_version", "");
        if (!(TextUtils.isEmpty(a2) || a2.equals(e))) {
            C2122m.m1837a(new C2140c("mistat_basic", SettingConstants.UPGRADE));
        }
        C2128s.m1856b(a, "basic_info_version", e);
    }

    public void m1770a(Context context) {
        C2122m.m1837a(new C2140c("mistat_basic", IAlbumConfig.BUY_SOURCE_NEW));
        C2122m.m1837a(new C2141e("mistat_basic", "model", Build.MODEL));
        C2122m.m1837a(new C2141e("mistat_basic", "OS", "android" + VERSION.SDK_INT));
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
        if (!TextUtils.isEmpty(telephonyManager.getNetworkOperatorName())) {
            C2122m.m1837a(new C2141e("mistat_basic", "operator", telephonyManager.getSimOperator()));
        }
        Object b = C2116g.m1810b(context);
        if (!TextUtils.isEmpty(b)) {
            C2122m.m1837a(new C2141e("mistat_basic", "IMEI", b));
        }
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        if (displayMetrics != null) {
            int i = displayMetrics.widthPixels;
            int i2 = displayMetrics.heightPixels;
            C2122m.m1837a(new C2141e("mistat_basic", "resolution", i < i2 ? i + WebConstants.PARAM_KEY_X + i2 : i2 + WebConstants.PARAM_KEY_X + i));
        }
        C2122m.m1837a(new C2141e("mistat_basic", "locale", Locale.getDefault().toString()));
    }
}
