package com.xcrash.crashreporter.generic;

import android.content.Context;
import android.text.TextUtils;
import com.xcrash.crashreporter.utils.Utility;

public class CrashReportParamsBuilder {
    private static final int LOG_SIZE = 200;
    private boolean anrEnable = false;
    private String bv = "";
    private String crpo = "0";
    private boolean mAutoSendLog = true;
    private Context mContext;
    private int mCrashLimit = 50;
    private boolean mDebug = false;
    private boolean mDisableLogcat = false;
    private boolean mDisableNativeReport = false;
    private boolean mEnableFullLog = false;
    private int mLogSize = 200;
    private String mProcessName = null;
    private ICrashCallback mcb;
    private String mkey = "";
    private String f2134p = "";
    private String p1 = "";
    private String pf = "";
    private String pu = "";
    private boolean raiseSignal = true;
    private String soPath;
    private String f2135u;
    private String f2136v = "";

    public CrashReportParamsBuilder(Context context) {
        this.mContext = context;
    }

    public boolean isDisableNativeReport() {
        return this.mDisableNativeReport;
    }

    public CrashReportParamsBuilder disableNativeReport(boolean disable) {
        this.mDisableNativeReport = disable;
        return this;
    }

    public boolean isDisableLogcat() {
        return this.mDisableLogcat;
    }

    public CrashReportParamsBuilder disableLogcat(boolean disable) {
        this.mDisableLogcat = disable;
        return this;
    }

    public CrashReportParamsBuilder enableFullLog(boolean enabled) {
        this.mEnableFullLog = enabled;
        return this;
    }

    public boolean isFullLogEnabled() {
        return this.mEnableFullLog;
    }

    public String getPf() {
        return this.pf;
    }

    public String getP() {
        return this.f2134p;
    }

    public String getP1() {
        return this.p1;
    }

    public String getMkey() {
        return this.mkey;
    }

    public String getPu() {
        return this.pu;
    }

    public String getProcessName() {
        return this.mProcessName;
    }

    public String getV() {
        return this.f2136v;
    }

    public String getU() {
        return this.f2135u;
    }

    public String getCrpo() {
        return this.crpo;
    }

    public ICrashCallback getCallback() {
        return this.mcb;
    }

    public int getLogSize() {
        return this.mLogSize;
    }

    public boolean isAutoSendLog() {
        return this.mAutoSendLog;
    }

    public boolean isDebug() {
        return this.mDebug;
    }

    public String getBv() {
        return this.bv;
    }

    public boolean isRaiseSignal() {
        return this.raiseSignal;
    }

    public boolean isAnrEnable() {
        return this.anrEnable;
    }

    public int getCrashLimit() {
        return this.mCrashLimit;
    }

    public String getSoPath() {
        return this.soPath;
    }

    public CrashReportParamsBuilder setLogSize(int mLogSize) {
        this.mLogSize = mLogSize;
        return this;
    }

    public CrashReportParamsBuilder setCallback(ICrashCallback mcb) {
        this.mcb = mcb;
        return this;
    }

    public CrashReportParamsBuilder setV(String v) {
        this.f2136v = v;
        return this;
    }

    public CrashReportParamsBuilder setU(String u) {
        this.f2135u = u;
        return this;
    }

    public CrashReportParamsBuilder setCrpo(String crpo) {
        this.crpo = crpo;
        return this;
    }

    public CrashReportParamsBuilder setP(String p) {
        this.f2134p = p;
        return this;
    }

    public CrashReportParamsBuilder setPf(String pf) {
        this.pf = pf;
        return this;
    }

    public CrashReportParamsBuilder setP1(String p1) {
        this.p1 = p1;
        return this;
    }

    public CrashReportParamsBuilder setPu(String pu) {
        this.pu = pu;
        return this;
    }

    public CrashReportParamsBuilder setMkey(String mkey) {
        this.mkey = mkey;
        return this;
    }

    public CrashReportParamsBuilder setAutoSendLog(boolean mAutoSendLog) {
        this.mAutoSendLog = mAutoSendLog;
        return this;
    }

    public CrashReportParamsBuilder setDebug(boolean mDebug) {
        this.mDebug = mDebug;
        return this;
    }

    public CrashReportParamsBuilder setBv(String bv) {
        this.bv = bv;
        return this;
    }

    public CrashReportParamsBuilder setRaiseSignal(boolean raiseSignal) {
        this.raiseSignal = raiseSignal;
        return this;
    }

    public CrashReportParamsBuilder setAnrEnable(boolean anrEnable) {
        this.anrEnable = anrEnable;
        return this;
    }

    public CrashReportParamsBuilder setProcessName(String mProcessName) {
        this.mProcessName = mProcessName;
        return this;
    }

    public CrashReportParamsBuilder setCrashLimit(int limit) {
        this.mCrashLimit = limit;
        return this;
    }

    public CrashReportParamsBuilder setSoPath(String path) {
        this.soPath = path;
        return this;
    }

    public CrashReportParams build() {
        if (this.mcb == null) {
            this.mcb = new DefaultCrashCallback();
        }
        if (TextUtils.isEmpty(this.f2136v) && this.mContext != null) {
            this.f2136v = Utility.getVersionName(this.mContext);
        }
        return new CrashReportParams(this);
    }
}
