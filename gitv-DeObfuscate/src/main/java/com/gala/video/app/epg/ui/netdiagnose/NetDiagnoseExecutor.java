package com.gala.video.app.epg.ui.netdiagnose;

import android.util.Log;
import com.gala.video.app.epg.ui.netdiagnose.provider.NDBaseProvider;
import com.gala.video.app.epg.ui.netdiagnose.provider.NDBaseProvider.INetDiagnoseResultListener;
import com.gala.video.app.epg.ui.netdiagnose.provider.PingNslookupProvider;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.utils.ResourceUtil;

public class NetDiagnoseExecutor {
    private static final String TAG = "NetDiagnoseExecutor";
    private INetDiagnoseResultListener mNetDiagnoseResultListener = new C09371();

    class C09371 implements INetDiagnoseResultListener {
        C09371() {
        }

        public void onReslut(String info) {
            LogUtils.m1574i(NetDiagnoseExecutor.TAG, "onReslut info: " + info);
        }
    }

    public void submit() {
        Log.i(TAG, "submit: ");
        submit(new PingNslookupProvider(ResourceUtil.getContext()));
    }

    public void submit(NDBaseProvider provider) {
        Log.i(TAG, "submit provider: ");
        provider.setNetDiagnoseResultListener(this.mNetDiagnoseResultListener);
        provider.startCheckEx();
    }
}
