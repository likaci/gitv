package tv.gitv.ptqy.security.fingerprint;

import android.content.Context;
import android.util.Base64;
import tv.gitv.ptqy.security.fingerprint.action.DataCollector;
import tv.gitv.ptqy.security.fingerprint.action.LocalFingerPrintCacheHelper;
import tv.gitv.ptqy.security.fingerprint.action.RequestDFPTask;
import tv.gitv.ptqy.security.fingerprint.callback.FingerPrintCallBack;
import tv.gitv.ptqy.security.fingerprint.exception.FingerPrintException;
import tv.gitv.ptqy.security.fingerprint.pingback.PingBackAgent;

public class FingerPrintManager implements IFingerPrint {
    private static FingerPrintManager fingerPrintManager = new FingerPrintManager();
    public static boolean isExecuting = false;

    public static FingerPrintManager getInstance() {
        return fingerPrintManager;
    }

    private FingerPrintManager() {
    }

    public String getFingerPrint(Context context, FingerPrintCallBack callBack) throws FingerPrintException {
        PingBackAgent.sendErrorPingbackAsync(context);
        if (callBack == null) {
            PingBackAgent.saveFetchFingerprintError(context, "FingerPrintCallBack is Null");
            return null;
        }
        try {
            String cache = new LocalFingerPrintCacheHelper(context).readFingerPrintLocalCache();
            if (cache != null) {
                callBack.onSuccess(cache);
                return cache;
            }
            new RequestDFPTask(context).execute(new FingerPrintCallBack[]{callBack});
            return null;
        } catch (Exception e) {
            new RequestDFPTask(context).execute(new FingerPrintCallBack[]{callBack});
            return null;
        }
    }

    public String getEnvInfo(Context context, boolean internal) {
        try {
            DataCollector collector = new DataCollector(context);
            collector.collect(internal);
            return Base64.encodeToString(collector.getJsonString().getBytes("UTF-8"), 2);
        } catch (Exception e) {
            LogMgr.m1899i("getEnvinfo: " + e.toString());
            PingBackAgent.saveEnvInfoError(context, "getEnvinfo: " + e.toString());
            return "";
        }
    }

    public String getEnvInfo(Context context) {
        return getEnvInfo(context, false);
    }
}
