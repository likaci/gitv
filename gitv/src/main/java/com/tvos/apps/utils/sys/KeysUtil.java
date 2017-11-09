package com.tvos.apps.utils.sys;

import android.util.Log;
import com.tvos.apps.utils.PropUtil;

public class KeysUtil {
    public static final String OFFICIAL_PAY_KEY = "079554007591cb3c5c4e2e2e36795e85";
    public static final String OFFICIAL_VIP_KEY = "206f743e495db98469b11976e71a2eac";
    public static final String PAY_KEY;
    private static final String PROP_SDK_TVKEY = "persist.sdk.tvkey";
    private static final String TAG = KeysUtil.class.getSimpleName();
    public static final String TEST_PAY_KEY = "535d95fbf57d43209506c5b9adcb6383";
    public static final String TEST_VIP_KEY = "6d21bb3e64b79ad31b3504e50fc2649c";
    public static final String VIP_KEY;

    static {
        boolean official = true;
        try {
            if (Integer.parseInt(PropUtil.getProp(PROP_SDK_TVKEY)) == 1) {
                official = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i(TAG, "official is " + official);
        if (official) {
            VIP_KEY = OFFICIAL_VIP_KEY;
            PAY_KEY = OFFICIAL_PAY_KEY;
            return;
        }
        VIP_KEY = TEST_VIP_KEY;
        PAY_KEY = TEST_PAY_KEY;
    }
}
