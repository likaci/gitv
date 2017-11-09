package com.xcrash.crashreporter;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build.VERSION;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import com.gala.video.lib.share.ifimpl.imsg.utils.IMsgUtils.DBColumns;
import com.xcrash.crashreporter.core.ANRHandler;
import com.xcrash.crashreporter.core.CrashHandler;
import com.xcrash.crashreporter.core.CrashInfo;
import com.xcrash.crashreporter.core.NativeCrashHandler;
import com.xcrash.crashreporter.generic.CrashReportParams;
import com.xcrash.crashreporter.utils.CrashConst;
import com.xcrash.crashreporter.utils.DebugLog;
import com.xcrash.crashreporter.utils.DeliverConst;
import com.xcrash.crashreporter.utils.JobManager;
import com.xcrash.crashreporter.utils.Utility;
import java.util.Random;
import org.json.JSONException;
import org.json.JSONObject;

public final class CrashReporter {
    private static final int MODE_AGAIN = 2;
    private static final int MODE_NEW_INSTALL = 0;
    private static final int MODE_UPDATE = 1;
    public static final String TAG = "xcrash.CrashReporter";
    private static CrashReporter instance;
    private int mAnrSwitch = 0;
    private int mBizErrorSampleBase = 100;
    private int mBizErrorSampleRate = 0;
    private Context mContext;
    private CrashInfo mCrashInfo = new CrashInfo();
    private int mLaunchMode = -1;
    private int mLogSize = 200;
    private int mMaxCount = 50;
    private CrashReportParams mParams;
    private String mPatchVersion = "";
    private int mPolicy = 5;

    class C20861 implements Runnable {
        C20861() {
        }

        public void run() {
            CrashHandler.getInstance().clearLaunchCrashCount();
            NativeCrashHandler.getInstance().clearLaunchCrashCount();
        }
    }

    private CrashReporter() {
    }

    public static synchronized CrashReporter getInstance() {
        CrashReporter crashReporter;
        synchronized (CrashReporter.class) {
            if (instance == null) {
                instance = new CrashReporter();
            }
            crashReporter = instance;
        }
        return crashReporter;
    }

    public CrashReportParams getReportParams() {
        return this.mParams;
    }

    public void init(Context context, CrashReportParams params) {
        this.mParams = params;
        this.mLogSize = params.getLogSize();
        this.mAnrSwitch = params.isAnrEnable() ? 1 : 0;
        this.mMaxCount = params.getCrashLimit();
        if (params.isDebug()) {
            DebugLog.enable();
        }
        String processName = TextUtils.isEmpty(params.getProcessName()) ? Utility.getCurrentProcessName(context) : params.getProcessName();
        params.setmProcessName(processName);
        initCrashHandler(context, processName);
    }

    private void initCrashHandler(Context context, String processName) {
        long begin = SystemClock.elapsedRealtime();
        if (this.mContext != null) {
            DebugLog.log(TAG, "initCrashReporter: crash reporter already initialized!");
        } else if (context != null) {
            Context ctx = context.getApplicationContext();
            if (ctx != null) {
                context = ctx;
            }
            this.mContext = context;
            if (this.mLogSize <= 0) {
                DebugLog.enableLogBuffer(false);
            } else {
                DebugLog.setLogSize(this.mLogSize);
            }
            CrashHandler.getInstance().init(this.mContext, processName, this.mMaxCount, this.mLogSize, this.mParams);
            NativeCrashHandler.getInstance().init(this.mContext, processName, this.mPolicy, this.mMaxCount, this.mLogSize, this.mParams);
            if (processName.equals(this.mContext.getPackageName())) {
                if (VERSION.SDK_INT > 20) {
                    ANRHandler.getInstance().init(this.mContext, processName, this.mAnrSwitch, this.mMaxCount, this.mLogSize, this.mParams);
                }
                updateCrashInfo();
                updateLaunchMode();
                new Handler(Looper.getMainLooper()).postDelayed(new C20861(), 10000);
                if (this.mParams.isAutoSendLog() && !this.mParams.getCallback().disableUploadCrash()) {
                    sendCrashReport();
                }
            } else {
                this.mLaunchMode = getLaunchMode();
            }
            long cost = SystemClock.elapsedRealtime() - begin;
            Log.i(TAG, "xcrash inited: V1.6.6");
            DebugLog.log(TAG, "Crash reporter inited: cost ", Long.valueOf(cost), ", launch mode:", Integer.valueOf(this.mLaunchMode));
        }
    }

    public void setInited(String inited) {
        DeliverConst.inited = inited;
    }

    public void appInited() {
        DeliverConst.inited = "1";
    }

    public void enableFullLog(boolean enabled) {
        if (this.mParams != null) {
            this.mParams.enableFullLog(enabled);
        }
    }

    public void sendCrashReport() {
        DebugLog.log(TAG, "send crash report");
        CrashHandler.getInstance().sendCrashReportBackground();
        if (!this.mParams.isNativeReportDisabled()) {
            NativeCrashHandler.getInstance().sendCrashReportBackground();
        }
        ANRHandler.getInstance().sendAnrTracesBackground();
    }

    public void randomReportException(String message, int percent) {
        if (this.mContext != null) {
            final Exception e = new Exception(message);
            int seed = new Random().nextInt(100);
            DebugLog.log(TAG, "seed ", Integer.valueOf(seed));
            if (seed < percent) {
                JobManager.getInstance().postRunnable(new Runnable() {
                    public void run() {
                        CrashHandler.getInstance().handleException(e);
                    }
                });
            }
        }
    }

    public void reportBizError(Throwable throwable, final String bizInfo) {
        try {
            if (new Random().nextInt(this.mBizErrorSampleBase) >= this.mBizErrorSampleRate) {
                DebugLog.log(TAG, "ignore report biz error");
                return;
            }
            if (throwable == null) {
                throwable = new Exception("unknown biz error");
            }
            final Thread thread = Thread.currentThread();
            final Throwable th = throwable;
            JobManager.getInstance().postRunnable(new Runnable() {
                public void run() {
                    CrashHandler.getInstance().reportBizError(th, bizInfo, thread);
                }
            });
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void reportBizError(Throwable throwable, String module, String tag, String level, String detail) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("module", module);
            jsonObject.put("tag", tag);
            jsonObject.put(DBColumns.LEVEL, level);
            jsonObject.put("detail", detail);
            reportBizError(throwable, jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void randomReportException(String message) {
        randomReportException(message, 1);
    }

    public String getLastNativeCrashFileName() {
        return NativeCrashHandler.getInstance().getLastCrashFileName();
    }

    public String getLastJavaCrashFileName() {
        return CrashHandler.getInstance().getLastCrashFileName();
    }

    public void setFinishLaunchFlag() {
        CrashHandler.getInstance().setFinishLaunchFlag();
        NativeCrashHandler.getInstance().setFinishLaunchFlag();
    }

    public void clearLaunchCrashCount() {
        CrashHandler.getInstance().clearLaunchCrashCount();
        NativeCrashHandler.getInstance().clearLaunchCrashCount();
    }

    public CrashInfo getCrashInfo() {
        return this.mCrashInfo;
    }

    public void reportJsException(String msg, String stack, String addr) {
        DebugLog.log(TAG, "repot js exception");
        CrashHandler.getInstance().reportJsException(msg, stack, addr);
    }

    public void reportJsWarning(String msg, String stack, String addr, String plgt, String plgid) {
        DebugLog.log(TAG, "repot js warnning exception");
        CrashHandler.getInstance().reportJsWarning(msg, stack, addr, plgt, plgid);
    }

    public void setPatchVersion(String patchVersion) {
        this.mPatchVersion = patchVersion;
    }

    public String getPatchVersion() {
        return this.mPatchVersion;
    }

    private void updateCrashInfo() {
        this.mCrashInfo.javaLaunchCrash = CrashHandler.getInstance().getLaunchCrashCount();
        this.mCrashInfo.nativeLaunchCrash = NativeCrashHandler.getInstance().getLaunchCrashCount();
    }

    public void setBizErrorSampleRate(String sampleRate) {
        try {
            int[] res = analysisDecimal(sampleRate);
            this.mBizErrorSampleRate = res[0];
            this.mBizErrorSampleBase = res[1];
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private void updateLaunchMode() {
        if (this.mContext != null) {
            String thisVersion;
            SharedPreferences sp = this.mContext.getSharedPreferences(CrashConst.SP_CRASH_REPORTER, 4);
            Editor editor = sp.edit();
            String lastVersion = sp.getString("version", "");
            if (TextUtils.isEmpty(this.mParams.getV())) {
                thisVersion = Utility.getVersionName(this.mContext);
            } else {
                thisVersion = this.mParams.getV();
            }
            if (TextUtils.isEmpty(lastVersion)) {
                this.mLaunchMode = 0;
                editor.putString("version", thisVersion);
            } else if (thisVersion.equals(lastVersion)) {
                this.mLaunchMode = 2;
            } else {
                this.mLaunchMode = 1;
                editor.putString("version", thisVersion);
            }
            editor.putInt(CrashConst.KEY_LAUNCH_MODE, this.mLaunchMode);
            editor.apply();
        }
    }

    private int[] analysisDecimal(String decStr) {
        int[] res = new int[]{0, 100};
        try {
            double dec = Double.parseDouble(decStr);
            if (dec > 1.0d) {
                dec = 1.0d;
            } else if (dec < 0.0d) {
                dec = 0.0d;
            }
            if (Math.abs(dec - 1.0d) < 1.0E-7d) {
                res[0] = 100;
                res[1] = 100;
            } else if (Math.abs(dec) < 1.0E-7d) {
                res[0] = 0;
                res[1] = 100;
            } else {
                int decimalPlaces = (decStr.length() - decStr.indexOf(".")) - 1;
                if (decimalPlaces <= 0) {
                    res[0] = 0;
                    res[1] = 100;
                } else {
                    res[1] = (int) Math.pow(10.0d, (double) decimalPlaces);
                    res[0] = (int) (((double) res[1]) * dec);
                }
            }
        } catch (Exception e) {
            res[0] = 0;
            res[1] = 100;
        }
        return res;
    }

    public int getLaunchMode() {
        if (this.mLaunchMode != -1) {
            return this.mLaunchMode;
        }
        return this.mContext.getSharedPreferences(CrashConst.SP_CRASH_REPORTER, 4).getInt(CrashConst.KEY_LAUNCH_MODE, -1);
    }
}
