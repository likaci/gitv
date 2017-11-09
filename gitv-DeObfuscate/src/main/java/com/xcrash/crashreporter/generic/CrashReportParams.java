package com.xcrash.crashreporter.generic;

public class CrashReportParams {
    private boolean anrEnable;
    private String aqyid;
    private String bv;
    private String crplg = "";
    private String crplgv = "";
    private String crpo;
    private boolean isRn = false;
    private boolean isWebview = false;
    private boolean mAutoSendLog;
    private ICrashCallback mCb;
    private int mCrashLimit;
    private boolean mDebug;
    private boolean mDisableLogcat;
    private boolean mDisableNativeReport;
    private boolean mEnableFullLog;
    private int mLogSize;
    private String mProcessName;
    private String mkey;
    private String f2131p;
    private String p1;
    private String pf;
    private String pu;
    private String qyid;
    private String qyidv2;
    private boolean raiseSignal;
    private String soPath;
    private String f2132u;
    private String f2133v;

    public boolean isAnrEnable() {
        return this.anrEnable;
    }

    public CrashReportParams(CrashReportParamsBuilder builder) {
        this.pf = builder.getPf();
        this.f2131p = builder.getP();
        this.p1 = builder.getP1();
        this.pu = builder.getPu();
        this.mkey = builder.getMkey();
        this.mProcessName = builder.getProcessName();
        this.f2133v = builder.getV();
        this.crpo = builder.getCrpo();
        this.mDisableNativeReport = builder.isDisableNativeReport();
        this.mCb = builder.getCallback();
        this.mEnableFullLog = builder.isFullLogEnabled();
        this.mLogSize = builder.getLogSize();
        this.mCrashLimit = builder.getCrashLimit();
        this.mDisableLogcat = builder.isDisableLogcat();
        this.mAutoSendLog = builder.isAutoSendLog();
        this.mDebug = builder.isDebug();
        this.bv = builder.getBv();
        this.raiseSignal = builder.isRaiseSignal();
        this.anrEnable = builder.isAnrEnable();
        this.f2132u = builder.getU();
        this.qyid = "";
        this.aqyid = "";
        this.qyidv2 = "";
        this.soPath = builder.getSoPath();
    }

    public void setmProcessName(String processName) {
        this.mProcessName = processName;
    }

    public String getPf() {
        return this.pf;
    }

    public String getP() {
        return this.f2131p;
    }

    public String getP1() {
        return this.p1;
    }

    public String getPu() {
        return this.pu;
    }

    public String getMkey() {
        return this.mkey;
    }

    public String getProcessName() {
        return this.mProcessName;
    }

    public String getV() {
        return this.f2133v;
    }

    public String getCrpo() {
        return this.crpo;
    }

    public int getLogSize() {
        return this.mLogSize;
    }

    public int getCrashLimit() {
        return this.mCrashLimit;
    }

    public boolean isNativeReportDisabled() {
        return this.mDisableNativeReport;
    }

    public boolean isFullLogEnabled() {
        return this.mEnableFullLog;
    }

    public boolean isLogcatDisabled() {
        return this.mDisableLogcat;
    }

    public void enableFullLog(boolean enable) {
        this.mEnableFullLog = enable;
    }

    public ICrashCallback getCallback() {
        return this.mCb;
    }

    public String getU() {
        return this.f2132u;
    }

    public String getCrplg() {
        return this.crplg;
    }

    public String getCrplgv() {
        return this.crplgv;
    }

    public boolean isAutoSendLog() {
        return this.mAutoSendLog;
    }

    public boolean isDebug() {
        return this.mDebug;
    }

    public String getBuildVersion() {
        return this.bv;
    }

    public boolean isRn() {
        return this.isRn;
    }

    public boolean isWebview() {
        return this.isWebview;
    }

    public boolean isRaiseSignal() {
        return this.raiseSignal;
    }

    public String getQyid() {
        return this.qyid;
    }

    public String getAqyid() {
        return this.aqyid;
    }

    public String getQyidv2() {
        return this.qyidv2;
    }

    public String getSoPath() {
        return this.soPath;
    }

    public void setPu(String pu) {
        this.pu = pu;
    }

    public void setU(String u) {
        this.f2132u = u;
    }

    public void setMkey(String mkey) {
        this.mkey = mkey;
    }

    public void setCrpo(String crpo) {
        this.crpo = crpo;
    }

    public void setCrplg(String crplg) {
        this.crplg = crplg;
    }

    public void setCrplgv(String crplgv) {
        this.crplgv = crplgv;
    }

    public void setRn(boolean rn) {
        this.isRn = rn;
    }

    public void setWebview(boolean webview) {
        this.isWebview = webview;
    }

    public void setQyid(String qyid) {
        this.qyid = qyid;
    }

    public void setAqyid(String aqyid) {
        this.aqyid = aqyid;
    }

    public void setQyidv2(String qyidv2) {
        this.qyidv2 = qyidv2;
    }
}
