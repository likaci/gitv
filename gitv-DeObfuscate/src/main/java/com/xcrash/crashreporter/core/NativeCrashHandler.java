package com.xcrash.crashreporter.core;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build.VERSION;
import android.os.Process;
import android.text.TextUtils;
import android.util.Log;
import com.gala.android.dlna.sdk.stddmrcontroller.Util;
import com.mcto.ads.internal.common.JsonBundleConstants;
import com.tvos.apps.utils.DateUtil;
import com.xcrash.crashreporter.CrashReporter;
import com.xcrash.crashreporter.bean.NativeCrashStatistics;
import com.xcrash.crashreporter.bean.RnCrashStatistics;
import com.xcrash.crashreporter.generic.CrashReportParams;
import com.xcrash.crashreporter.generic.ICrashCallback;
import com.xcrash.crashreporter.utils.CommonUtils;
import com.xcrash.crashreporter.utils.CrashConst;
import com.xcrash.crashreporter.utils.DebugLog;
import com.xcrash.crashreporter.utils.DeliverUtils;
import com.xcrash.crashreporter.utils.LogParser;
import com.xcrash.crashreporter.utils.NetworkUtil;
import com.xcrash.crashreporter.utils.Utility;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;
import org.cybergarage.http.HTTP;
import org.cybergarage.soap.SOAP;
import org.json.JSONException;
import org.json.JSONObject;

public final class NativeCrashHandler {
    private static final String CHAR_SET = "UTF-8";
    public static final String CRASH_DIR = "crash";
    public static final String CRASH_TAG = "native_crash_info_";
    public static final String LAST_CRASH_NAME = "native_crash_last";
    public static final int MAX_CRASH_TIMES = 3;
    private static final int MAX_PLAYERLOG_SIZE = 102400;
    public static final int MICRODUMP = 2;
    public static final int NO_REPORT = 0;
    public static final int REPORT_NO_LOG = 1;
    public static final String TAG = "xcrash.NCrashHandler";
    public static final int XCRASH = 5;
    private static NativeCrashHandler instance;
    private int crashCount = -1;
    private final DateFormat formatter = new SimpleDateFormat(DateUtil.PATTERN_STANDARD10H);
    private Date lastCrashTime = null;
    private Context mContext;
    private String mCrashPath;
    private boolean mFinishLaunch = false;
    private int mLogSize = 200;
    private CrashReportParams mParams;
    private String mProcessName;
    private int mReportLimit = 50;
    private int mReportType = 5;
    private Date mStartTime;
    private final DateFormat timeFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

    class C20961 implements Runnable {
        C20961() {
        }

        public void run() {
            NativeCrashHandler.this.sendCrashReport();
            NativeCrashHandler.this.delCrashInfoFile(NativeCrashHandler.this.mCrashPath);
        }
    }

    class C20972 implements Comparator<File> {
        C20972() {
        }

        public int compare(File file, File newfile) {
            if (file.lastModified() < newfile.lastModified()) {
                return -1;
            }
            if (file.lastModified() == newfile.lastModified()) {
                return 0;
            }
            return 1;
        }
    }

    public native void enableRaiseSignal(boolean z);

    public native int initNative(int i, String str, String str2, int i2, String str3, boolean z, boolean z2, int i3, String str4, String str5);

    private NativeCrashHandler() {
    }

    public static synchronized NativeCrashHandler getInstance() {
        NativeCrashHandler nativeCrashHandler;
        synchronized (NativeCrashHandler.class) {
            if (instance == null) {
                instance = new NativeCrashHandler();
            }
            nativeCrashHandler = instance;
        }
        return nativeCrashHandler;
    }

    public synchronized void init(Context ctx, String processName, int policy, int maxCount, int logSize, CrashReportParams params) {
        this.mReportLimit = maxCount;
        this.mReportType = policy;
        this.mLogSize = logSize;
        this.mParams = params;
        init(ctx, processName);
    }

    public synchronized void init(Context ctx, String processName) {
        if (this.mContext != null) {
            DebugLog.log(TAG, "initCrashReporter: crash reporter already initialized!");
        } else if (ctx != null) {
            if (this.mReportType > 0) {
                this.mContext = ctx;
                this.mCrashPath = CommonUtils.getCrashDirectory(this.mContext);
                this.mStartTime = new Date();
                String crashLib = "xcrash";
                int apiLevel = VERSION.SDK_INT;
                if (apiLevel >= 21) {
                    crashLib = "xcrash_unwind";
                }
                try {
                    System.loadLibrary(crashLib);
                } catch (Throwable err) {
                    err.printStackTrace();
                    DebugLog.m1741e(TAG, "init nativeCrashHandler fail");
                }
                this.mProcessName = processName;
                String version = TextUtils.isEmpty(this.mParams.getV()) ? Utility.getVersionName(this.mContext) : this.mParams.getV();
                boolean dumpStackMemory = false;
                if (this.mLogSize > 100) {
                    dumpStackMemory = true;
                }
                try {
                    initNative(apiLevel, this.mCrashPath + File.separator, this.mContext.getFilesDir().getParent() + File.separator + "lib" + File.separator, this.mReportLimit, version + "-" + this.mProcessName, this.mParams.isRaiseSignal(), dumpStackMemory, this.mLogSize, null, "nativeCallback");
                    DebugLog.log(TAG, "init nativeCrashHandler for ", packageInfo);
                } catch (Throwable th) {
                    DebugLog.m1741e(TAG, "initNative not found");
                }
            }
        }
    }

    public synchronized void sendCrashReportBackground() {
        DebugLog.log(TAG, "scan native crash log");
        if (this.mContext == null) {
            DebugLog.m1741e(TAG, "NativeCrashHandler not initialized");
        } else if (NetworkUtil.isWifiOrEthernetOn(this.mContext)) {
            new Thread(new C20961(), "CrashReporter Thread").start();
        } else {
            DebugLog.log(TAG, "sendCrashReport: not in wifi or ethernet status");
        }
    }

    public void setFinishLaunchFlag() {
        this.mFinishLaunch = true;
    }

    public void clearLaunchCrashCount() {
        Editor editor = this.mContext.getSharedPreferences(CrashConst.SP_CRASH_REPORTER, 4).edit();
        editor.putInt(CrashConst.KEY_NATIVE_COUNTER, 0);
        editor.apply();
    }

    public int getLaunchCrashCount() {
        return this.mContext.getSharedPreferences(CrashConst.SP_CRASH_REPORTER, 4).getInt(CrashConst.KEY_NATIVE_COUNTER, 0);
    }

    private void addLaunchCrashCount() {
        if (this.mProcessName.equals(this.mContext.getPackageName())) {
            SharedPreferences sp = this.mContext.getSharedPreferences(CrashConst.SP_CRASH_REPORTER, 4);
            int count = sp.getInt(CrashConst.KEY_NATIVE_COUNTER, 0);
            if (count == 3) {
                clearLaunchCrashCount();
                count = 0;
            }
            count++;
            Editor editor = sp.edit();
            editor.putInt(CrashConst.KEY_NATIVE_COUNTER, count);
            editor.apply();
        }
    }

    public boolean getFinishLaunchFlag() {
        return this.mFinishLaunch;
    }

    public String getLastCrashFileName() {
        return this.mCrashPath + File.separator + LAST_CRASH_NAME;
    }

    public void sendCrashReport() {
        FileInputStream fin;
        IOException e;
        String fileName;
        String process;
        JSONObject appJson;
        Throwable th;
        List<File> list = getSortedDmpFiles();
        Date current = new Date();
        if (list != null && list.size() > 0) {
            getCrashInfo();
            long lastCrash = 0;
            for (File file : list) {
                boolean isFrequentCrash = false;
                boolean isOldCrash = false;
                boolean needBackup = false;
                long crashTime = file.lastModified();
                if (lastCrash != 0 && Math.abs(crashTime - lastCrash) < 15000) {
                    isFrequentCrash = true;
                }
                if (Math.abs(current.getTime() - crashTime) > 172800000) {
                    isOldCrash = true;
                }
                lastCrash = crashTime;
                if (!(this.crashCount >= this.mReportLimit || isFrequentCrash || this.mReportType == 0 || isOldCrash)) {
                    fin = null;
                    JSONObject jSONObject = null;
                    String str = "";
                    try {
                        FileInputStream fin2 = new FileInputStream(file);
                        try {
                            JSONObject jSONObject2 = new JSONObject(CommonUtils.inputStreamToString(fin2));
                            try {
                                str = jSONObject2.getString("Url");
                                jSONObject2.remove("Url");
                                if (fin2 != null) {
                                    try {
                                        fin2.close();
                                        jSONObject = jSONObject2;
                                        fin = fin2;
                                    } catch (IOException e2) {
                                        e2.printStackTrace();
                                        jSONObject = jSONObject2;
                                        fin = fin2;
                                    }
                                } else {
                                    jSONObject = jSONObject2;
                                    fin = fin2;
                                }
                            } catch (IOException e3) {
                                e2 = e3;
                                jSONObject = jSONObject2;
                                fin = fin2;
                                e2.printStackTrace();
                                if (fin == null) {
                                    try {
                                        fin.close();
                                    } catch (IOException e22) {
                                        e22.printStackTrace();
                                    }
                                }
                            } catch (JSONException e4) {
                                jSONObject = jSONObject2;
                                fin = fin2;
                                DebugLog.log(TAG, "try parse native crash log");
                                try {
                                    jSONObject = LogParser.toJsonObj(file);
                                    fileName = new File(file.getName().trim()).getName();
                                    process = "";
                                    if (fileName.split("-").length > 2) {
                                        process = fileName.split("-")[1];
                                    }
                                    appJson = this.mParams.getCallback().getAppData(process, false, 1);
                                    CommonUtils.fillDeviceinfo(this.mContext, jSONObject);
                                    jSONObject.put("BuildTime", this.mParams.getBuildVersion());
                                    jSONObject.put("AppData", appJson);
                                    str = constructUrlForIncompleteLog(file.getAbsolutePath());
                                    needBackup = true;
                                } catch (Exception e1) {
                                    e1.printStackTrace();
                                } catch (Throwable th2) {
                                    th = th2;
                                }
                                if (fin != null) {
                                    try {
                                        fin.close();
                                    } catch (JSONException e5) {
                                        e5.printStackTrace();
                                    }
                                }
                                if (jSONObject != null) {
                                    try {
                                        jSONObject.remove("Url");
                                        DebugLog.log(TAG, "url is: ", str);
                                        postCrashReport(jSONObject, str);
                                    } catch (Exception e6) {
                                        e6.printStackTrace();
                                    }
                                }
                                if (!file.exists()) {
                                    if (needBackup) {
                                        file.renameTo(new File(getLastCrashFileName()));
                                    } else {
                                        file.delete();
                                    }
                                }
                            } catch (Throwable th3) {
                                th = th3;
                                jSONObject = jSONObject2;
                                fin = fin2;
                            }
                        } catch (IOException e7) {
                            e22 = e7;
                            fin = fin2;
                            e22.printStackTrace();
                            if (fin == null) {
                                fin.close();
                            }
                        } catch (JSONException e8) {
                            fin = fin2;
                            DebugLog.log(TAG, "try parse native crash log");
                            jSONObject = LogParser.toJsonObj(file);
                            fileName = new File(file.getName().trim()).getName();
                            process = "";
                            if (fileName.split("-").length > 2) {
                                process = fileName.split("-")[1];
                            }
                            appJson = this.mParams.getCallback().getAppData(process, false, 1);
                            CommonUtils.fillDeviceinfo(this.mContext, jSONObject);
                            jSONObject.put("BuildTime", this.mParams.getBuildVersion());
                            jSONObject.put("AppData", appJson);
                            str = constructUrlForIncompleteLog(file.getAbsolutePath());
                            needBackup = true;
                            if (fin != null) {
                                fin.close();
                            }
                            if (jSONObject != null) {
                                jSONObject.remove("Url");
                                DebugLog.log(TAG, "url is: ", str);
                                postCrashReport(jSONObject, str);
                            }
                            if (!file.exists()) {
                                if (needBackup) {
                                    file.delete();
                                } else {
                                    file.renameTo(new File(getLastCrashFileName()));
                                }
                            }
                        } catch (Throwable th4) {
                            th = th4;
                            fin = fin2;
                        }
                    } catch (IOException e9) {
                        e22 = e9;
                        e22.printStackTrace();
                        if (fin == null) {
                            fin.close();
                        }
                    } catch (JSONException e10) {
                        DebugLog.log(TAG, "try parse native crash log");
                        jSONObject = LogParser.toJsonObj(file);
                        fileName = new File(file.getName().trim()).getName();
                        process = "";
                        if (fileName.split("-").length > 2) {
                            process = fileName.split("-")[1];
                        }
                        appJson = this.mParams.getCallback().getAppData(process, false, 1);
                        CommonUtils.fillDeviceinfo(this.mContext, jSONObject);
                        jSONObject.put("BuildTime", this.mParams.getBuildVersion());
                        jSONObject.put("AppData", appJson);
                        str = constructUrlForIncompleteLog(file.getAbsolutePath());
                        needBackup = true;
                        if (fin != null) {
                            fin.close();
                        }
                        if (jSONObject != null) {
                            jSONObject.remove("Url");
                            DebugLog.log(TAG, "url is: ", str);
                            postCrashReport(jSONObject, str);
                        }
                        if (!file.exists()) {
                            if (needBackup) {
                                file.renameTo(new File(getLastCrashFileName()));
                            } else {
                                file.delete();
                            }
                        }
                    }
                    if (jSONObject != null) {
                        jSONObject.remove("Url");
                        DebugLog.log(TAG, "url is: ", str);
                        postCrashReport(jSONObject, str);
                    }
                }
                if (!file.exists()) {
                    if (needBackup) {
                        file.renameTo(new File(getLastCrashFileName()));
                    } else {
                        file.delete();
                    }
                }
            }
            return;
        }
        return;
        if (fin != null) {
            try {
                fin.close();
            } catch (IOException e222) {
                e222.printStackTrace();
            }
        }
        throw th;
        throw th;
    }

    private boolean postCrashReport(JSONObject jsonLog, String urlstr) {
        DebugLog.log(TAG, "post native crash report");
        if (this.mContext == null) {
            DebugLog.m1741e(TAG, "NativeCrashHandler not initialized");
            return false;
        } else if (!NetworkUtil.isWifiOrEthernetOn(this.mContext)) {
            DebugLog.log(TAG, "Send Native CrashReport: not in wifi or ethernet status");
            return false;
        } else if (TextUtils.isEmpty(urlstr)) {
            DebugLog.m1741e(TAG, "url is empty");
            return false;
        } else {
            OutputStream os = null;
            try {
                HttpURLConnection conn = (HttpURLConnection) new URL(urlstr).openConnection();
                conn.setRequestMethod(HTTP.POST);
                conn.setDoOutput(true);
                conn.setRequestProperty(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded\n");
                os = conn.getOutputStream();
                os.write("msg=".getBytes());
                os.write(DeliverUtils.encoding(jsonLog.toString()).getBytes());
                os.flush();
                if (conn.getResponseCode() == 200) {
                    Log.i(TAG, "send crash report:success");
                    if (os != null) {
                        try {
                            os.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    return true;
                }
                Log.e(TAG, "send crash report:fail");
                if (os == null) {
                    return false;
                }
                try {
                    os.close();
                    return false;
                } catch (IOException e2) {
                    e2.printStackTrace();
                    return false;
                }
            } catch (Exception e3) {
                e3.printStackTrace();
                if (os == null) {
                    return false;
                }
                try {
                    os.close();
                    return false;
                } catch (IOException e22) {
                    e22.printStackTrace();
                    return false;
                }
            } catch (Throwable th) {
                if (os != null) {
                    try {
                        os.close();
                    } catch (IOException e222) {
                        e222.printStackTrace();
                    }
                }
            }
        }
    }

    private void getCrashInfo() {
        InputStreamReader isr;
        FileNotFoundException e;
        IOException e2;
        Throwable th;
        File file = new File(this.mCrashPath + File.separator + CRASH_TAG + this.formatter.format(new Date()));
        if (!file.exists()) {
            try {
                if (file.createNewFile()) {
                    DebugLog.log(TAG, "文件>>>", file.getAbsolutePath(), "创建成功");
                    this.crashCount = 0;
                    this.lastCrashTime = null;
                    return;
                }
            } catch (IOException e3) {
                this.crashCount = 0;
                this.lastCrashTime = null;
                return;
            }
        }
        BufferedReader br = null;
        FileInputStream fis = null;
        InputStreamReader isr2 = null;
        try {
            FileInputStream fis2 = new FileInputStream(file);
            try {
                isr = new InputStreamReader(fis2, "UTF-8");
            } catch (FileNotFoundException e4) {
                e = e4;
                fis = fis2;
                try {
                    e.printStackTrace();
                    if (br != null) {
                        try {
                            br.close();
                        } catch (IOException e22) {
                            e22.printStackTrace();
                        }
                    }
                    if (isr2 != null) {
                        try {
                            isr2.close();
                        } catch (IOException e222) {
                            e222.printStackTrace();
                        }
                    }
                    if (fis != null) {
                        try {
                            fis.close();
                        } catch (IOException e2222) {
                            e2222.printStackTrace();
                        }
                    }
                    try {
                        file.delete();
                    } catch (Exception e5) {
                        e5.printStackTrace();
                        return;
                    }
                } catch (Throwable th2) {
                    th = th2;
                    if (br != null) {
                        try {
                            br.close();
                        } catch (IOException e22222) {
                            e22222.printStackTrace();
                        }
                    }
                    if (isr2 != null) {
                        try {
                            isr2.close();
                        } catch (IOException e222222) {
                            e222222.printStackTrace();
                        }
                    }
                    if (fis != null) {
                        try {
                            fis.close();
                        } catch (IOException e2222222) {
                            e2222222.printStackTrace();
                        }
                    }
                    throw th;
                }
            } catch (IOException e6) {
                e2222222 = e6;
                fis = fis2;
                e2222222.printStackTrace();
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException e22222222) {
                        e22222222.printStackTrace();
                    }
                }
                if (isr2 != null) {
                    try {
                        isr2.close();
                    } catch (IOException e222222222) {
                        e222222222.printStackTrace();
                    }
                }
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e2222222222) {
                        e2222222222.printStackTrace();
                    }
                }
                file.delete();
            } catch (Throwable th3) {
                th = th3;
                fis = fis2;
                if (br != null) {
                    br.close();
                }
                if (isr2 != null) {
                    isr2.close();
                }
                if (fis != null) {
                    fis.close();
                }
                throw th;
            }
            try {
                BufferedReader br2 = new BufferedReader(isr);
                try {
                    String crashCnt = br2.readLine();
                    String time = br2.readLine();
                    if (TextUtils.isEmpty(crashCnt) || TextUtils.isEmpty(time)) {
                        this.crashCount = 0;
                        this.lastCrashTime = null;
                        if (br2 != null) {
                            try {
                                br2.close();
                            } catch (IOException e22222222222) {
                                e22222222222.printStackTrace();
                            }
                        }
                        if (isr != null) {
                            try {
                                isr.close();
                            } catch (IOException e222222222222) {
                                e222222222222.printStackTrace();
                            }
                        }
                        if (fis2 != null) {
                            try {
                                fis2.close();
                                return;
                            } catch (IOException e2222222222222) {
                                e2222222222222.printStackTrace();
                                return;
                            }
                        }
                        return;
                    }
                    try {
                        this.crashCount = Integer.parseInt(crashCnt);
                        this.lastCrashTime = this.timeFormatter.parse(time);
                        if (br2 != null) {
                            try {
                                br2.close();
                            } catch (IOException e22222222222222) {
                                e22222222222222.printStackTrace();
                            }
                        }
                        if (isr != null) {
                            try {
                                isr.close();
                            } catch (IOException e222222222222222) {
                                e222222222222222.printStackTrace();
                            }
                        }
                        if (fis2 != null) {
                            try {
                                fis2.close();
                            } catch (IOException e2222222222222222) {
                                e2222222222222222.printStackTrace();
                            }
                        }
                    } catch (Exception e7) {
                        this.crashCount = 0;
                        this.lastCrashTime = null;
                        if (br2 != null) {
                            try {
                                br2.close();
                            } catch (IOException e22222222222222222) {
                                e22222222222222222.printStackTrace();
                            }
                        }
                        if (isr != null) {
                            try {
                                isr.close();
                            } catch (IOException e222222222222222222) {
                                e222222222222222222.printStackTrace();
                            }
                        }
                        if (fis2 != null) {
                            try {
                                fis2.close();
                            } catch (IOException e2222222222222222222) {
                                e2222222222222222222.printStackTrace();
                            }
                        }
                    }
                } catch (FileNotFoundException e8) {
                    e = e8;
                    isr2 = isr;
                    fis = fis2;
                    br = br2;
                    e.printStackTrace();
                    if (br != null) {
                        br.close();
                    }
                    if (isr2 != null) {
                        isr2.close();
                    }
                    if (fis != null) {
                        fis.close();
                    }
                    file.delete();
                } catch (IOException e9) {
                    e2222222222222222222 = e9;
                    isr2 = isr;
                    fis = fis2;
                    br = br2;
                    e2222222222222222222.printStackTrace();
                    if (br != null) {
                        br.close();
                    }
                    if (isr2 != null) {
                        isr2.close();
                    }
                    if (fis != null) {
                        fis.close();
                    }
                    file.delete();
                } catch (Throwable th4) {
                    th = th4;
                    isr2 = isr;
                    fis = fis2;
                    br = br2;
                    if (br != null) {
                        br.close();
                    }
                    if (isr2 != null) {
                        isr2.close();
                    }
                    if (fis != null) {
                        fis.close();
                    }
                    throw th;
                }
            } catch (FileNotFoundException e10) {
                e = e10;
                isr2 = isr;
                fis = fis2;
                e.printStackTrace();
                if (br != null) {
                    br.close();
                }
                if (isr2 != null) {
                    isr2.close();
                }
                if (fis != null) {
                    fis.close();
                }
                file.delete();
            } catch (IOException e11) {
                e2222222222222222222 = e11;
                isr2 = isr;
                fis = fis2;
                e2222222222222222222.printStackTrace();
                if (br != null) {
                    br.close();
                }
                if (isr2 != null) {
                    isr2.close();
                }
                if (fis != null) {
                    fis.close();
                }
                file.delete();
            } catch (Throwable th5) {
                th = th5;
                isr2 = isr;
                fis = fis2;
                if (br != null) {
                    br.close();
                }
                if (isr2 != null) {
                    isr2.close();
                }
                if (fis != null) {
                    fis.close();
                }
                throw th;
            }
        } catch (FileNotFoundException e12) {
            e = e12;
            e.printStackTrace();
            if (br != null) {
                br.close();
            }
            if (isr2 != null) {
                isr2.close();
            }
            if (fis != null) {
                fis.close();
            }
            file.delete();
        } catch (IOException e13) {
            e2222222222222222222 = e13;
            e2222222222222222222.printStackTrace();
            if (br != null) {
                br.close();
            }
            if (isr2 != null) {
                isr2.close();
            }
            if (fis != null) {
                fis.close();
            }
            file.delete();
        }
    }

    private synchronized boolean saveCrashInfo(int crashCount, Date crashTime) {
        boolean isSaveSuccess;
        IOException e;
        FileNotFoundException e2;
        Throwable th;
        Exception e3;
        isSaveSuccess = false;
        File file = new File(this.mCrashPath + File.separator + CRASH_TAG + this.formatter.format(new Date()));
        if (!file.exists()) {
            try {
                if (file.createNewFile()) {
                    DebugLog.log(TAG, "文件>>>", file.getAbsolutePath(), "创建成功");
                }
            } catch (IOException e4) {
                e4.printStackTrace();
            }
        }
        FileOutputStream fos = null;
        try {
            FileOutputStream fos2 = new FileOutputStream(file);
            try {
                fos2.write((String.valueOf(crashCount) + "\n").getBytes("UTF-8"));
                fos2.write((this.timeFormatter.format(crashTime) + "\n").getBytes("UTF-8"));
                fos2.flush();
                isSaveSuccess = true;
                if (fos2 != null) {
                    try {
                        fos2.close();
                    } catch (IOException e42) {
                        e42.printStackTrace();
                        fos = fos2;
                    }
                }
                fos = fos2;
            } catch (FileNotFoundException e5) {
                e2 = e5;
                fos = fos2;
                try {
                    e2.printStackTrace();
                    if (fos != null) {
                        try {
                            fos.close();
                        } catch (IOException e422) {
                            e422.printStackTrace();
                        }
                    }
                    DebugLog.log(TAG, "isSaveSuccess = ", Boolean.valueOf(isSaveSuccess));
                    DebugLog.log(TAG, "Count", Integer.valueOf(crashCount), " time:", this.timeFormatter.format(crashTime));
                    return isSaveSuccess;
                } catch (Throwable th2) {
                    th = th2;
                    if (fos != null) {
                        try {
                            fos.close();
                        } catch (IOException e4222) {
                            e4222.printStackTrace();
                        }
                    }
                    throw th;
                }
            } catch (IOException e6) {
                e4222 = e6;
                fos = fos2;
                e4222.printStackTrace();
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e42222) {
                        e42222.printStackTrace();
                    }
                }
                DebugLog.log(TAG, "isSaveSuccess = ", Boolean.valueOf(isSaveSuccess));
                DebugLog.log(TAG, "Count", Integer.valueOf(crashCount), " time:", this.timeFormatter.format(crashTime));
                return isSaveSuccess;
            } catch (Exception e7) {
                e3 = e7;
                fos = fos2;
                e3.printStackTrace();
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e422222) {
                        e422222.printStackTrace();
                    }
                }
                DebugLog.log(TAG, "isSaveSuccess = ", Boolean.valueOf(isSaveSuccess));
                DebugLog.log(TAG, "Count", Integer.valueOf(crashCount), " time:", this.timeFormatter.format(crashTime));
                return isSaveSuccess;
            } catch (Throwable th3) {
                th = th3;
                fos = fos2;
                if (fos != null) {
                    fos.close();
                }
                throw th;
            }
        } catch (FileNotFoundException e8) {
            e2 = e8;
            e2.printStackTrace();
            if (fos != null) {
                fos.close();
            }
            DebugLog.log(TAG, "isSaveSuccess = ", Boolean.valueOf(isSaveSuccess));
            DebugLog.log(TAG, "Count", Integer.valueOf(crashCount), " time:", this.timeFormatter.format(crashTime));
            return isSaveSuccess;
        } catch (IOException e9) {
            e422222 = e9;
            e422222.printStackTrace();
            if (fos != null) {
                fos.close();
            }
            DebugLog.log(TAG, "isSaveSuccess = ", Boolean.valueOf(isSaveSuccess));
            DebugLog.log(TAG, "Count", Integer.valueOf(crashCount), " time:", this.timeFormatter.format(crashTime));
            return isSaveSuccess;
        } catch (Exception e10) {
            e3 = e10;
            e3.printStackTrace();
            if (fos != null) {
                fos.close();
            }
            DebugLog.log(TAG, "isSaveSuccess = ", Boolean.valueOf(isSaveSuccess));
            DebugLog.log(TAG, "Count", Integer.valueOf(crashCount), " time:", this.timeFormatter.format(crashTime));
            return isSaveSuccess;
        }
        DebugLog.log(TAG, "isSaveSuccess = ", Boolean.valueOf(isSaveSuccess));
        DebugLog.log(TAG, "Count", Integer.valueOf(crashCount), " time:", this.timeFormatter.format(crashTime));
        return isSaveSuccess;
    }

    private void delCrashInfoFile(String filepath) {
        String suffix = this.formatter.format(new Date());
        try {
            File[] files = new File(filepath).listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.getAbsolutePath().contains(CRASH_TAG) && !file.getAbsolutePath().endsWith(suffix)) {
                        file.delete();
                        DebugLog.log(TAG, "delete file = ", file.getAbsolutePath());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<File> getSortedDmpFiles() {
        List<File> list = getDmpFiles(this.mCrashPath, new ArrayList());
        if (list != null && list.size() > 0) {
            Collections.sort(list, new C20972());
        }
        return list;
    }

    private List<File> getDmpFiles(String path, List<File> files) {
        File pathFile = new File(path);
        if (pathFile != null && pathFile.isDirectory()) {
            File[] subfiles = pathFile.listFiles();
            if (subfiles != null) {
                for (File file : subfiles) {
                    if (file.isFile() && (file.getName().indexOf(".dmp") > -1 || file.getName().indexOf(".xca") > -1)) {
                        files.add(file);
                    }
                }
            }
        }
        return files;
    }

    private String getJavaBacktrace(int tid, String tname, boolean isAnr) {
        int i = 0;
        if (TextUtils.isEmpty(tname.trim())) {
            return "";
        }
        boolean isMain;
        if (tid == Process.myPid()) {
            isMain = true;
        } else {
            isMain = false;
        }
        Map<Thread, StackTraceElement[]> threads = Thread.getAllStackTraces();
        StringBuilder sb = new StringBuilder();
        boolean dumpStack = false;
        for (Entry<Thread, StackTraceElement[]> entry : threads.entrySet()) {
            Thread thread = (Thread) entry.getKey();
            if (isAnr) {
                if (thread.getName().equals("main") || thread.getName().equals(this.mProcessName)) {
                    dumpStack = true;
                    continue;
                }
            } else if (thread.getName().contains(tname.trim()) || (isMain && thread.getName().equals("main"))) {
                dumpStack = true;
                continue;
            }
            if (dumpStack) {
                sb.append("\"" + thread.getName() + "\":\n");
                StackTraceElement[] stackTraceElements = (StackTraceElement[]) entry.getValue();
                int length = stackTraceElements.length;
                while (i < length) {
                    StackTraceElement e = stackTraceElements[i];
                    sb.append(e.getClassName() + "." + e.getMethodName() + "(" + e.getFileName() + SOAP.DELIM + e.getLineNumber() + ")\n");
                    i++;
                }
                return sb.toString();
            }
        }
        return sb.toString();
    }

    private void addDebugInfo(File crashFile, JSONObject appJson) {
        if (crashFile != null && appJson != null) {
            try {
                long len = crashFile.length();
                String content = CommonUtils.inputStreamToString(new FileInputStream(crashFile));
                if (content.length() > 1024) {
                    content = content.substring(0, 1024);
                }
                appJson.put("Size", String.valueOf(len));
                appJson.put(Util.FUNCTION_TAG_CONTENT, DeliverUtils.encoding(content));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private JSONObject dealCrashLog(String filePath, boolean isAnr, int tid, String tname) {
        Exception e;
        Throwable th;
        Writer writer;
        DebugLog.m1740d(TAG, "fill additional log");
        Writer writer2 = null;
        FileOutputStream fos = null;
        OutputStreamWriter osw = null;
        JSONObject jsonLog = null;
        boolean bCallback = false;
        try {
            Writer outputStreamWriter;
            jsonLog = LogParser.toJsonObj(new File(filePath));
            CommonUtils.fillDeviceinfo(this.mContext, jsonLog);
            FileOutputStream fos2 = new FileOutputStream(filePath, true);
            try {
                outputStreamWriter = new OutputStreamWriter(fos2, "UTF-8");
            } catch (Exception e2) {
                e = e2;
                fos = fos2;
                try {
                    e.printStackTrace();
                    if (writer2 != null) {
                        try {
                            writer2.close();
                        } catch (IOException e3) {
                        }
                    }
                    if (osw != null) {
                        try {
                            osw.close();
                        } catch (IOException e4) {
                        }
                    }
                    if (fos != null) {
                        try {
                            fos.close();
                        } catch (IOException e5) {
                        }
                    }
                    if (!bCallback) {
                        this.mParams.getCallback().onCrash(jsonLog, isAnr ? 1 : 2, "");
                    }
                    return jsonLog;
                } catch (Throwable th2) {
                    th = th2;
                    if (writer2 != null) {
                        try {
                            writer2.close();
                        } catch (IOException e6) {
                        }
                    }
                    if (osw != null) {
                        try {
                            osw.close();
                        } catch (IOException e7) {
                        }
                    }
                    if (fos != null) {
                        try {
                            fos.close();
                        } catch (IOException e8) {
                        }
                    }
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                fos = fos2;
                if (writer2 != null) {
                    writer2.close();
                }
                if (osw != null) {
                    osw.close();
                }
                if (fos != null) {
                    fos.close();
                }
                throw th;
            }
            try {
                outputStreamWriter = new BufferedWriter(outputStreamWriter);
                try {
                    String backtrace = getJavaBacktrace(tid, tname, isAnr);
                    outputStreamWriter.write("\n>>> OtherInfo <<<\n");
                    outputStreamWriter.write("BuildTime: " + this.mParams.getBuildVersion() + "\n");
                    jsonLog.put("BuildTime", this.mParams.getBuildVersion());
                    JSONObject appJson = null;
                    if (this.mReportType != 1) {
                        if (!TextUtils.isEmpty(backtrace)) {
                            outputStreamWriter.write("\n>>> JavaBacktrace <<<\n");
                            outputStreamWriter.write(backtrace);
                        }
                        jsonLog.put("JavaBacktrace", backtrace);
                        String pid = String.valueOf(Process.myPid());
                        Process proc = new ProcessBuilder(new String[0]).command(new String[]{"ps", "-t", pid}).redirectErrorStream(true).start();
                        outputStreamWriter.write("\n>>> Threads <<<\n");
                        String threads = CommonUtils.inputStreamToString(proc.getInputStream());
                        outputStreamWriter.write(threads);
                        jsonLog.put("Threads", threads);
                        if (isAnr) {
                            StringBuilder sb = new StringBuilder();
                            outputStreamWriter.write("\n>>> Traces <<<\n");
                            File traceFile = CommonUtils.getTraceFile(this.mProcessName);
                            if (traceFile == null || !traceFile.exists()) {
                                outputStreamWriter.write("no traces.txt found\n");
                            } else {
                                writerTraceFile(traceFile, outputStreamWriter, sb);
                                jsonLog.put("Traces", sb.toString());
                                if (TextUtils.isEmpty(sb.toString())) {
                                    outputStreamWriter.write("no traces.txt found\n");
                                }
                            }
                        }
                        if (TextUtils.isEmpty(jsonLog.optString("Logcat"))) {
                            DebugLog.m1740d(TAG, "fill logcat");
                            proc = new ProcessBuilder(new String[0]).command(new String[]{"/system/bin/logcat", "-v", "threadtime", "-t", String.valueOf(this.mLogSize), "-d", "*:D"}).redirectErrorStream(true).start();
                            outputStreamWriter.write("\n>>> Logcat <<<\n");
                            String logcat = CommonUtils.inputStreamToString(proc.getInputStream());
                            jsonLog.put("Logcat", DeliverUtils.encoding(logcat));
                            outputStreamWriter.write(logcat);
                        }
                        DebugLog.m1740d(TAG, "fill events");
                        proc = new ProcessBuilder(new String[0]).command(new String[]{"/system/bin/logcat", "-v", "threadtime", "-b", JsonBundleConstants.A71_TRACKING_EVENTS, "-t", String.valueOf(this.mLogSize), "-d"}).redirectErrorStream(true).start();
                        outputStreamWriter.write("\n>>> Events <<<\n");
                        String events = CommonUtils.inputStreamToString(proc.getInputStream());
                        outputStreamWriter.write(events);
                        jsonLog.put("Events", DeliverUtils.encoding(events));
                        DebugLog.m1740d(TAG, "fill qiyi log");
                        DebugLog.enableLogBuffer(false);
                        jsonLog.put("QiyiLog", DeliverUtils.encoding(DebugLog.logBuffer.toString()));
                        ICrashCallback callback = this.mParams.getCallback();
                        int type = isAnr ? 2 : 1;
                        jsonLog.put("Path", filePath);
                        callback.onCrash(jsonLog, type, "");
                        appJson = callback.getAppData(this.mProcessName, true, type);
                        bCallback = true;
                        outputStreamWriter.write("\n>>> QiyiLog <<<\n");
                        outputStreamWriter.write(URLDecoder.decode(jsonLog.optString("QiyiLog")));
                    }
                    if (appJson != null) {
                        outputStreamWriter.write("\n>>> AppData <<<\n");
                        Iterator iterator = appJson.keys();
                        while (iterator.hasNext()) {
                            String key = (String) iterator.next();
                            String value = URLDecoder.decode(appJson.getString(key));
                            outputStreamWriter.write(key + ":\n");
                            outputStreamWriter.write(value + "\n");
                        }
                        jsonLog.put("AppData", appJson);
                    }
                    if (outputStreamWriter != null) {
                        try {
                            outputStreamWriter.close();
                        } catch (IOException e9) {
                        }
                    }
                    if (outputStreamWriter != null) {
                        try {
                            outputStreamWriter.close();
                        } catch (IOException e10) {
                        }
                    }
                    if (fos2 != null) {
                        try {
                            fos2.close();
                            writer = outputStreamWriter;
                            fos = fos2;
                            writer2 = outputStreamWriter;
                        } catch (IOException e11) {
                            writer = outputStreamWriter;
                            fos = fos2;
                            writer2 = outputStreamWriter;
                        }
                    } else {
                        writer = outputStreamWriter;
                        fos = fos2;
                        writer2 = outputStreamWriter;
                    }
                } catch (Exception e12) {
                    e = e12;
                    osw = outputStreamWriter;
                    fos = fos2;
                    writer2 = outputStreamWriter;
                    e.printStackTrace();
                    if (writer2 != null) {
                        writer2.close();
                    }
                    if (osw != null) {
                        osw.close();
                    }
                    if (fos != null) {
                        fos.close();
                    }
                    if (bCallback) {
                        if (isAnr) {
                        }
                        this.mParams.getCallback().onCrash(jsonLog, isAnr ? 1 : 2, "");
                    }
                    return jsonLog;
                } catch (Throwable th4) {
                    th = th4;
                    osw = outputStreamWriter;
                    fos = fos2;
                    writer2 = outputStreamWriter;
                    if (writer2 != null) {
                        writer2.close();
                    }
                    if (osw != null) {
                        osw.close();
                    }
                    if (fos != null) {
                        fos.close();
                    }
                    throw th;
                }
            } catch (Exception e13) {
                e = e13;
                writer = outputStreamWriter;
                fos = fos2;
                e.printStackTrace();
                if (writer2 != null) {
                    writer2.close();
                }
                if (osw != null) {
                    osw.close();
                }
                if (fos != null) {
                    fos.close();
                }
                if (bCallback) {
                    if (isAnr) {
                    }
                    this.mParams.getCallback().onCrash(jsonLog, isAnr ? 1 : 2, "");
                }
                return jsonLog;
            } catch (Throwable th5) {
                th = th5;
                writer = outputStreamWriter;
                fos = fos2;
                if (writer2 != null) {
                    writer2.close();
                }
                if (osw != null) {
                    osw.close();
                }
                if (fos != null) {
                    fos.close();
                }
                throw th;
            }
        } catch (Exception e14) {
            e = e14;
            e.printStackTrace();
            if (writer2 != null) {
                writer2.close();
            }
            if (osw != null) {
                osw.close();
            }
            if (fos != null) {
                fos.close();
            }
            if (bCallback) {
                if (isAnr) {
                }
                this.mParams.getCallback().onCrash(jsonLog, isAnr ? 1 : 2, "");
            }
            return jsonLog;
        }
        if (bCallback) {
            if (isAnr) {
            }
            this.mParams.getCallback().onCrash(jsonLog, isAnr ? 1 : 2, "");
        }
        return jsonLog;
    }

    private void writerTraceFile(File file, Writer writer, StringBuilder sb) {
        Exception e;
        Throwable th;
        DebugLog.m1740d(TAG, "process traces file ", file.getAbsolutePath());
        BufferedReader br = null;
        FileInputStream fin = null;
        InputStreamReader isr = null;
        try {
            FileInputStream fin2 = new FileInputStream(file);
            try {
                InputStreamReader isr2 = new InputStreamReader(fin2, "UTF-8");
                try {
                    BufferedReader br2 = new BufferedReader(isr2);
                    boolean start = false;
                    try {
                        String preLine = "";
                        Pattern pattern = Pattern.compile(String.format("^Cmd\\sline:\\s%s.*", new Object[]{this.mContext.getPackageName()}));
                        while (true) {
                            String line = br2.readLine();
                            if (line == null) {
                                break;
                            }
                            if (pattern.matcher(line).matches()) {
                                start = true;
                                writer.write(preLine);
                                sb.append(preLine);
                            }
                            if (start) {
                                writer.write(line + "\n");
                                sb.append(line + "\n");
                            }
                            preLine = line + "\n";
                            if (start && line.contains("----- end")) {
                                start = false;
                            }
                        }
                        if (br2 != null) {
                            try {
                                br2.close();
                            } catch (IOException e2) {
                                e2.printStackTrace();
                            }
                        }
                        if (isr2 != null) {
                            try {
                                isr2.close();
                            } catch (IOException e22) {
                                e22.printStackTrace();
                            }
                        }
                        if (fin2 != null) {
                            try {
                                fin2.close();
                                isr = isr2;
                                fin = fin2;
                                br = br2;
                                return;
                            } catch (IOException e222) {
                                e222.printStackTrace();
                                isr = isr2;
                                fin = fin2;
                                br = br2;
                                return;
                            }
                        }
                        fin = fin2;
                        br = br2;
                    } catch (Exception e3) {
                        e = e3;
                        isr = isr2;
                        fin = fin2;
                        br = br2;
                        try {
                            e.printStackTrace();
                            if (br != null) {
                                try {
                                    br.close();
                                } catch (IOException e2222) {
                                    e2222.printStackTrace();
                                }
                            }
                            if (isr != null) {
                                try {
                                    isr.close();
                                } catch (IOException e22222) {
                                    e22222.printStackTrace();
                                }
                            }
                            if (fin == null) {
                                try {
                                    fin.close();
                                } catch (IOException e222222) {
                                    e222222.printStackTrace();
                                }
                            }
                        } catch (Throwable th2) {
                            th = th2;
                            if (br != null) {
                                try {
                                    br.close();
                                } catch (IOException e2222222) {
                                    e2222222.printStackTrace();
                                }
                            }
                            if (isr != null) {
                                try {
                                    isr.close();
                                } catch (IOException e22222222) {
                                    e22222222.printStackTrace();
                                }
                            }
                            if (fin != null) {
                                try {
                                    fin.close();
                                } catch (IOException e222222222) {
                                    e222222222.printStackTrace();
                                }
                            }
                            throw th;
                        }
                    } catch (Throwable th3) {
                        th = th3;
                        isr = isr2;
                        fin = fin2;
                        br = br2;
                        if (br != null) {
                            br.close();
                        }
                        if (isr != null) {
                            isr.close();
                        }
                        if (fin != null) {
                            fin.close();
                        }
                        throw th;
                    }
                } catch (Exception e4) {
                    e = e4;
                    isr = isr2;
                    fin = fin2;
                    e.printStackTrace();
                    if (br != null) {
                        br.close();
                    }
                    if (isr != null) {
                        isr.close();
                    }
                    if (fin == null) {
                        fin.close();
                    }
                } catch (Throwable th4) {
                    th = th4;
                    isr = isr2;
                    fin = fin2;
                    if (br != null) {
                        br.close();
                    }
                    if (isr != null) {
                        isr.close();
                    }
                    if (fin != null) {
                        fin.close();
                    }
                    throw th;
                }
            } catch (Exception e5) {
                e = e5;
                fin = fin2;
                e.printStackTrace();
                if (br != null) {
                    br.close();
                }
                if (isr != null) {
                    isr.close();
                }
                if (fin == null) {
                    fin.close();
                }
            } catch (Throwable th5) {
                th = th5;
                fin = fin2;
                if (br != null) {
                    br.close();
                }
                if (isr != null) {
                    isr.close();
                }
                if (fin != null) {
                    fin.close();
                }
                throw th;
            }
        } catch (Exception e6) {
            e = e6;
            e.printStackTrace();
            if (br != null) {
                br.close();
            }
            if (isr != null) {
                isr.close();
            }
            if (fin == null) {
                fin.close();
            }
        }
    }

    private void backupCrashLog(String path) {
        boolean isCreateDirSucces;
        Exception e;
        Throwable th;
        File crashFile = new File(path);
        String backupFileName = new File(crashFile.getName().trim()).getName();
        File crashDirFile = new File(this.mContext.getExternalFilesDir(null).getAbsolutePath() + File.separator + "app" + File.separator + "crash");
        if (crashDirFile.exists() && crashDirFile.isDirectory()) {
            isCreateDirSucces = true;
        } else {
            Log.d(TAG, crashDirFile + " dir not exist");
            this.mContext.getExternalFilesDir(null);
            isCreateDirSucces = crashDirFile.mkdirs();
        }
        if (isCreateDirSucces) {
            File[] files = crashDirFile.listFiles();
            if (files != null && files.length > this.mReportLimit) {
                for (File file : files) {
                    if (file.getName().contains("xca") || file.getName().contains("dmp")) {
                        file.delete();
                        DebugLog.log(TAG, "delete file = ", file.getAbsolutePath());
                    }
                }
            }
            FileInputStream fin = null;
            FileOutputStream fos = null;
            try {
                FileInputStream fin2 = new FileInputStream(crashFile);
                try {
                    FileOutputStream fos2 = new FileOutputStream(crashDirFile + File.separator + backupFileName);
                    try {
                        DebugLog.log(TAG, "crashFilePath:", crashDirFile + File.separator + backupFileName);
                        byte[] buf = new byte[1024];
                        while (true) {
                            int len = fin2.read(buf);
                            if (len <= 0) {
                                break;
                            }
                            fos2.write(buf, 0, len);
                        }
                        if (fin2 != null) {
                            try {
                                fin2.close();
                            } catch (IOException e2) {
                                e2.printStackTrace();
                            }
                        }
                        if (fos2 != null) {
                            try {
                                fos2.close();
                            } catch (IOException e22) {
                                e22.printStackTrace();
                            }
                        }
                    } catch (Exception e3) {
                        e = e3;
                        fos = fos2;
                        fin = fin2;
                        try {
                            e.printStackTrace();
                            if (fin != null) {
                                try {
                                    fin.close();
                                } catch (IOException e222) {
                                    e222.printStackTrace();
                                }
                            }
                            if (fos == null) {
                                try {
                                    fos.close();
                                } catch (IOException e2222) {
                                    e2222.printStackTrace();
                                }
                            }
                        } catch (Throwable th2) {
                            th = th2;
                            if (fin != null) {
                                try {
                                    fin.close();
                                } catch (IOException e22222) {
                                    e22222.printStackTrace();
                                }
                            }
                            if (fos != null) {
                                try {
                                    fos.close();
                                } catch (IOException e222222) {
                                    e222222.printStackTrace();
                                }
                            }
                            throw th;
                        }
                    } catch (Throwable th3) {
                        th = th3;
                        fos = fos2;
                        fin = fin2;
                        if (fin != null) {
                            fin.close();
                        }
                        if (fos != null) {
                            fos.close();
                        }
                        throw th;
                    }
                } catch (Exception e4) {
                    e = e4;
                    fin = fin2;
                    e.printStackTrace();
                    if (fin != null) {
                        fin.close();
                    }
                    if (fos == null) {
                        fos.close();
                    }
                } catch (Throwable th4) {
                    th = th4;
                    fin = fin2;
                    if (fin != null) {
                        fin.close();
                    }
                    if (fos != null) {
                        fos.close();
                    }
                    throw th;
                }
            } catch (Exception e5) {
                e = e5;
                e.printStackTrace();
                if (fin != null) {
                    fin.close();
                }
                if (fos == null) {
                    fos.close();
                }
            }
        }
    }

    private String constructUrl(String path) {
        File file = new File(path);
        String crshid = "";
        if (file.getName().endsWith(".dmp")) {
            crshid = "2";
        } else if (file.getName().endsWith(".xca")) {
            crshid = "5";
        }
        String po = "0";
        String crpo = this.mParams.getCrpo();
        String crashtp = "1";
        String crplg = this.mParams.getCrplg();
        String othdt = "";
        String crplgv = this.mParams.getCrplgv();
        boolean isRn = this.mParams.isRn();
        boolean isWebView = this.mParams.isWebview();
        String pchv = CrashReporter.getInstance().getPatchVersion();
        if (isRn) {
            return DeliverUtils.constructUrl(this.mContext, new RnCrashStatistics(crshid, po, crpo, crashtp, crplg, othdt, crplgv, pchv));
        } else if (isWebView) {
            return DeliverUtils.constructUrl(this.mContext, new RnCrashStatistics(crshid, po, crpo, crashtp, crplg, othdt, crplgv, pchv));
        } else {
            return DeliverUtils.constructUrl(this.mContext, new NativeCrashStatistics(crshid, po, crpo, crashtp, crplg, othdt, crplgv, pchv));
        }
    }

    private String constructUrlForIncompleteLog(String path) {
        String url;
        File file = new File(path);
        String fileName = new File(file.getName().trim()).getName();
        String version = "";
        if (fileName.split("-").length > 2) {
            version = fileName.split("-")[0];
        }
        String crshid = "";
        if (file.getName().endsWith(".dmp")) {
            crshid = "2";
        } else if (file.getName().endsWith(".xca")) {
            crshid = "5";
        }
        String po = "2";
        String crpo = this.mParams.getCrpo();
        String crashtp = "1";
        String crplg = this.mParams.getCrplg();
        String othdt = "";
        String crplgv = this.mParams.getCrplgv();
        boolean isRn = this.mParams.isRn();
        boolean isWebView = this.mParams.isWebview();
        String pchv = CrashReporter.getInstance().getPatchVersion();
        if (isRn) {
            url = DeliverUtils.constructUrl(this.mContext, new RnCrashStatistics(crshid, po, crpo, crashtp, crplg, othdt, crplgv, pchv));
        } else if (isWebView) {
            url = DeliverUtils.constructUrl(this.mContext, new RnCrashStatistics(crshid, po, crpo, crashtp, crplg, othdt, crplgv, pchv));
        } else {
            url = DeliverUtils.constructUrl(this.mContext, new NativeCrashStatistics(crshid, po, crpo, crashtp, crplg, othdt, crplgv, pchv));
        }
        return url.replaceAll("&v=[^&]*", "&v=" + version);
    }

    public static void nativeCallback(String path, boolean isAnr, int tid, String tname) {
        Throwable th;
        FileOutputStream fos = null;
        try {
            String name = tname.replaceAll("\n|\r", "");
            Log.i(TAG, "call back from native");
            DebugLog.m1740d(TAG, "crash file:", path, " isAnr:", Boolean.valueOf(isAnr), " tid: ", Integer.valueOf(tid), " tname: ", name);
            File file = new File(path);
            getInstance().getCrashInfo();
            int crashCount = getInstance().crashCount + 1;
            Date lastCrash = getInstance().lastCrashTime;
            Date crashTime = new Date();
            Date startTime = getInstance().mStartTime;
            getInstance().saveCrashInfo(crashCount, crashTime);
            boolean isLaunchCrash = false;
            File lastCrashFile = new File(getInstance().getLastCrashFileName());
            if (Math.abs(crashTime.getTime() - startTime.getTime()) < 10000) {
                getInstance().addLaunchCrashCount();
                isLaunchCrash = true;
            }
            if ((lastCrash == null || Math.abs(crashTime.getTime() - lastCrash.getTime()) >= 15000) && crashCount <= getInstance().mReportLimit) {
                JSONObject jsonLog = getInstance().dealCrashLog(path, isAnr, tid, name);
                String url = getInstance().constructUrl(path);
                getInstance().backupCrashLog(path);
                ICrashCallback callback = getInstance().mParams.getCallback();
                if (!callback.disableUploadCrash()) {
                    boolean success = false;
                    if (isLaunchCrash) {
                        if (jsonLog == null || callback == null || callback.disableUploadCrash()) {
                            DebugLog.m1741e(TAG, "crash log has error！");
                        } else {
                            success = getInstance().postCrashReport(jsonLog, url);
                        }
                    }
                    if (!file.renameTo(lastCrashFile)) {
                        file.delete();
                    }
                    if (!success) {
                        jsonLog.put("Url", url);
                        FileOutputStream fos2 = new FileOutputStream(file);
                        try {
                            fos2.write(jsonLog.toString().getBytes("UTF-8"));
                            fos2.flush();
                            fos = fos2;
                        } catch (Exception e) {
                            fos = fos2;
                            if (fos != null) {
                                try {
                                    fos.close();
                                } catch (IOException e2) {
                                    e2.printStackTrace();
                                    return;
                                }
                            }
                        } catch (Throwable th2) {
                            th = th2;
                            fos = fos2;
                            if (fos != null) {
                                try {
                                    fos.close();
                                } catch (IOException e22) {
                                    e22.printStackTrace();
                                }
                            }
                            throw th;
                        }
                    }
                }
                if (fos != null) {
                    try {
                        fos.close();
                        return;
                    } catch (IOException e222) {
                        e222.printStackTrace();
                        return;
                    }
                }
                return;
            }
            DebugLog.m1740d(TAG, "frequent crash,ignore");
            if (!file.renameTo(lastCrashFile)) {
                file.delete();
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e2222) {
                    e2222.printStackTrace();
                }
            }
        } catch (Exception e3) {
            if (fos != null) {
                fos.close();
            }
        } catch (Throwable th3) {
            th = th3;
            if (fos != null) {
                fos.close();
            }
            throw th;
        }
    }
}
