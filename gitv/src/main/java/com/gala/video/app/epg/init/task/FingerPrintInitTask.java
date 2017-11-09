package com.gala.video.app.epg.init.task;

import android.content.Context;
import android.os.Build;
import android.os.Build.VERSION;
import com.gala.video.lib.framework.core.pingback.PingBack;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.pingback.PingBackParams;
import java.util.Arrays;
import tv.gitv.ptqy.security.fingerprint.FingerPrintManager;
import tv.gitv.ptqy.security.fingerprint.callback.FingerPrintCallBack;
import tv.gitv.ptqy.security.fingerprint.exception.FingerPrintException;

public class FingerPrintInitTask implements Runnable {
    private static final String TAG = "FingerPrintInitTask";
    private Context mContext;

    public FingerPrintInitTask(Context context) {
        this.mContext = context;
    }

    public void run() {
        try {
            LogUtils.d(TAG, "evninfo=" + FingerPrintManager.getInstance().getEnvInfo(this.mContext));
            FingerPrintManager.getInstance().getFingerPrint(this.mContext, new FingerPrintCallBack() {
                public void onSuccess(String fingerPrint) {
                    LogUtils.d(FingerPrintInitTask.TAG, "FingerPrintManager:success, length = " + fingerPrint.length() + " " + fingerPrint);
                }

                public void onFailed(String message) {
                    LogUtils.d(FingerPrintInitTask.TAG, "FingerPrintManager:failure, " + message);
                }
            });
        } catch (FingerPrintException e) {
            LogUtils.d(TAG, "FingerPrintManager:FingerPrintException, " + e.getMessage());
            e.printStackTrace();
        } catch (Throwable e2) {
            String abi;
            if (VERSION.SDK_INT >= 21) {
                abi = Arrays.toString(Build.SUPPORTED_ABIS);
            } else {
                abi = Arrays.toString(new String[]{Build.CPU_ABI, Build.CPU_ABI2});
            }
            PingBackParams pingBackParams = new PingBackParams();
            pingBackParams.add("fingerprintcrash", abi);
            PingBack.getInstance().postPingBackToLongYuan(pingBackParams.build());
            LogUtils.d(TAG, "FingerPrintManager:FingerPrintException, " + e2.getMessage());
            e2.printStackTrace();
        }
    }
}
