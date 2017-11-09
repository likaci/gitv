package com.gala.video.app.player.data;

import com.gala.pingback.IPingbackContext;
import com.gala.sdk.performance.IPerformanceDataProvider;
import com.gala.sdk.performance.IPerformanceDataProvider.IPerformanceDataReadyListener;
import com.gala.video.lib.framework.core.pingback.PingBack;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.common.model.multiscreen.MultiScreenParams;
import com.gala.video.lib.share.pingback.PingBackParams;
import com.gala.video.lib.share.pingback.PingBackParams.Keys;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class JobPerfMonitor {
    private static final String EC_DATA_ERROR = "315008";
    private static final String TAG = "JobPerfMonitor";
    private static JobPerfMonitor sInstance;
    private IPerformanceDataReadyListener mDataListener = new C14111();
    private WeakReference<IPingbackContext> mPingbackContextRef;

    class C14111 implements IPerformanceDataReadyListener {
        C14111() {
        }

        public void onPerformanceDataReady(IPerformanceDataProvider provider) {
            String requestName = provider.getRequestName();
            String requestId = provider.getRequestId();
            long totalTimeSpan = provider.getTotalTimeSpan();
            List<Long> individualTimeSpans = provider.getIndividualTimeSpans();
            long jsonParseTimeSpan = provider.getJsonParseTimeSpan();
            boolean isSuccess = provider.isSuccess();
            boolean isMainRoutine = provider.isMainRoutine();
            String errorCode = provider.getErrorCode();
            String st = isSuccess ? "0" : MultiScreenParams.DLNA_PHONE_CONTROLL_ERROR;
            String pfec = !isSuccess ? errorCode : "";
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(JobPerfMonitor.TAG, "onPerformanceDataReady: data={name:" + requestName + ", id:" + requestId + ", total:" + totalTimeSpan + ", indiv:" + individualTimeSpans + ", json:" + jsonParseTimeSpan + ", success:" + isSuccess + ", main:" + isMainRoutine + ", errCode:" + errorCode);
            }
            JobPerfMonitor.this.sendRequestPingback(requestName, requestId, totalTimeSpan, st, pfec, individualTimeSpans, jsonParseTimeSpan, "", isMainRoutine);
        }
    }

    public static synchronized JobPerfMonitor instance() {
        JobPerfMonitor jobPerfMonitor;
        synchronized (JobPerfMonitor.class) {
            if (sInstance == null) {
                sInstance = new JobPerfMonitor();
            }
            jobPerfMonitor = sInstance;
        }
        return jobPerfMonitor;
    }

    private JobPerfMonitor() {
    }

    public void registerPingbackContext(IPingbackContext context) {
        if (context != null) {
            this.mPingbackContextRef = new WeakReference(context);
        }
    }

    private String getValueFromContext(String key) {
        IPingbackContext context = this.mPingbackContextRef != null ? (IPingbackContext) this.mPingbackContextRef.get() : null;
        if (context == null) {
            if (LogUtils.mIsDebug) {
                LogUtils.m1577w(TAG, "getValueFromContext: no available context!");
            }
            return "";
        }
        try {
            return context.getItem(key).getValue();
        } catch (RuntimeException e) {
            if (LogUtils.mIsDebug) {
                LogUtils.m1578w(TAG, "getValueFromContext: exception happened", e);
            }
            return "";
        }
    }

    public void registerDataProvider(IPerformanceDataProvider... providers) {
        if (providers != null) {
            for (IPerformanceDataProvider p : providers) {
                if (LogUtils.mIsDebug) {
                    LogUtils.m1568d(TAG, "registerDataProvider: " + p);
                }
                p.setPerformanceDataReadyListener(this.mDataListener);
            }
        }
    }

    private void sendRequestPingback(String requestName, String requestId, long time, String st, String pfec, List<Long> requestTimes, long jsonParseTime, String playerType, boolean isMainRoute) {
        String ec = "";
        if (!StringUtils.isEmpty((CharSequence) pfec)) {
            ec = "315008";
        }
        String tm1 = "";
        String tm2 = "";
        String tm3 = "";
        if (jsonParseTime > 0) {
            tm3 = String.valueOf(jsonParseTime);
        }
        if (!ListUtils.isEmpty((List) requestTimes)) {
            tm1 = String.valueOf(requestTimes.get(0));
            if (requestTimes.size() > 1) {
                tm2 = String.valueOf(requestTimes.get(1));
            }
        }
        PingBackParams params = new PingBackParams();
        params.add(Keys.f2035T, "11").add("ct", "150619_request").add(Keys.RI, requestName).add("r", requestId).add("e", getValueFromContext("e")).add("td", String.valueOf(time)).add(Keys.TM1, tm1).add("tm2", tm2).add("tm3", tm3).add("st", st).add("ec", ec).add("pfec", pfec).add("localtime", getFormatTime()).add("player", playerType).add(Keys.IS_PLAY_PATH, isMainRoute ? "1" : "0").add("hcdn", getValueFromContext("hcdn"));
        PingBack.getInstance().postPingBackToLongYuan(params.build());
    }

    private static String getFormatTime() {
        String result = "";
        result = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss:SS", Locale.US).format(new Date());
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, ">>getFormatTime:" + result);
        }
        return result;
    }
}
