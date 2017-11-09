package com.xcrash.crashreporter.core;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Debug;
import android.os.Process;
import android.text.TextUtils;
import android.util.Log;
import com.mcto.ads.internal.common.JsonBundleConstants;
import com.tvos.apps.utils.DateUtil;
import com.xcrash.crashreporter.CrashReporter;
import com.xcrash.crashreporter.bean.BizErrorStatistics;
import com.xcrash.crashreporter.bean.JavaCrashStatistics;
import com.xcrash.crashreporter.bean.JsErrorStatistics;
import com.xcrash.crashreporter.bean.RnCrashStatistics;
import com.xcrash.crashreporter.bean.RnJsErrorStatistics;
import com.xcrash.crashreporter.generic.CrashReportParams;
import com.xcrash.crashreporter.generic.ICrashCallback;
import com.xcrash.crashreporter.utils.CommonUtils;
import com.xcrash.crashreporter.utils.CrashConst;
import com.xcrash.crashreporter.utils.DebugLog;
import com.xcrash.crashreporter.utils.DeliverUtils;
import com.xcrash.crashreporter.utils.NetworkStatus;
import com.xcrash.crashreporter.utils.NetworkUtil;
import com.xcrash.crashreporter.utils.Utility;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
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
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.cybergarage.http.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

public final class CrashHandler implements UncaughtExceptionHandler {
    private static final String CHAR_SET = "UTF-8";
    private static final String CRASH_DIR = "crash";
    private static final String CRASH_ID = "5";
    private static final int CRASH_LENGTH = 4096;
    private static final String CRASH_TAG = "crash_times_";
    private static final String LAST_CRASH_NAME = "java_crash_last";
    private static final int MAX_CRASH_FILE_NUM = 50;
    private static final int MAX_CRASH_TIMES = 3;
    private static final String MKEY = "iqiyi&ppsqos";
    private static final String TAG = "xcrash.CrashHandler";
    private static CrashHandler instance;
    private Context mContext;
    private int mCrashCount = -1;
    private String mCrashFile;
    private int mCrashLimit = 50;
    private UncaughtExceptionHandler mDefaultHandler;
    private boolean mFinishLaunch = false;
    private DateFormat mFormatter = new SimpleDateFormat(DateUtil.PATTERN_STANDARD10H);
    private boolean mFrequentLaunchCrash = false;
    private int mLogSize = 200;
    private CrashReportParams mParams;
    private String mProcessName;
    private Date mStartTime;
    private String suffix;
    private DateFormat timeFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

    class C20931 implements Runnable {
        C20931() {
        }

        public void run() {
            CrashHandler.this.sendCrashReport();
        }
    }

    class C20942 implements Comparator<File> {
        C20942() {
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

    private CrashHandler() {
    }

    public static synchronized CrashHandler getInstance() {
        CrashHandler crashHandler;
        synchronized (CrashHandler.class) {
            if (instance == null) {
                instance = new CrashHandler();
            }
            crashHandler = instance;
        }
        return crashHandler;
    }

    public void init(Context context, String processName) {
        this.mContext = context;
        this.mProcessName = processName;
        DebugLog.log(TAG, "init>>>processName = ", processName);
        this.mCrashFile = CommonUtils.getCrashDirectory(this.mContext);
        DebugLog.log(TAG, "init>>>crashFile = ", this.mCrashFile);
        this.mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
        this.mStartTime = new Date();
    }

    public void init(Context ctx, String processName, int maxCount, int logSize, CrashReportParams params) {
        this.mCrashLimit = maxCount;
        this.mLogSize = logSize;
        this.mParams = params;
        init(ctx, processName);
    }

    public void init(Context context) {
        init(context, null);
    }

    public void setFinishLaunchFlag() {
        this.mFinishLaunch = true;
    }

    public void clearLaunchCrashCount() {
        Editor editor = this.mContext.getSharedPreferences(CrashConst.SP_CRASH_REPORTER, 4).edit();
        editor.putInt(CrashConst.KEY_JAVA_COUNTER, 0);
        editor.apply();
    }

    public int getLaunchCrashCount() {
        return this.mContext.getSharedPreferences(CrashConst.SP_CRASH_REPORTER, 4).getInt(CrashConst.KEY_JAVA_COUNTER, 0);
    }

    private void addLaunchCrashCount() {
        if (this.mProcessName.equals(this.mContext.getPackageName())) {
            SharedPreferences sharedPreferences = this.mContext.getSharedPreferences(CrashConst.SP_CRASH_REPORTER, 4);
            int count = sharedPreferences.getInt(CrashConst.KEY_JAVA_COUNTER, 0) + 1;
            if (count == 3) {
                this.mFrequentLaunchCrash = true;
            }
            Editor editor = sharedPreferences.edit();
            editor.putInt(CrashConst.KEY_JAVA_COUNTER, count);
            editor.apply();
        }
    }

    public String getLastCrashFileName() {
        return this.mCrashFile + File.separator + LAST_CRASH_NAME;
    }

    public boolean getFinishLaunchFlag() {
        return this.mFinishLaunch;
    }

    public void reportJsWarning(String msg, String stack, String addr, String plgt, String plgid) {
        JSONObject json = new JSONObject();
        try {
            json.put("CrashMsg", msg);
            json.put("CrashStack", stack);
            json.put("CrashAddr", addr);
            String str = "";
            postCrashReport(json, constructJsWarningUrl(plgt, plgid));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void reportJsException(String msg, String stack, String addr) {
        JSONObject json = new JSONObject();
        try {
            json.put("CrashMsg", msg);
            json.put("CrashStack", stack);
            json.put("CrashAddr", addr);
            postCrashReport(json, constructJsErrorUrl());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void reportBizError(Throwable throwable, String bizInfo, Thread thread) {
        JSONObject bizJson;
        JSONObject json = constructLog(throwable, false);
        try {
            bizJson = new JSONObject(bizInfo);
        } catch (Exception e) {
            bizJson = new JSONObject();
            try {
                bizJson.put("Info", bizInfo);
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }
        try {
            json.put("BizInfo", bizJson);
            json.put("Tname", thread.getName());
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        this.mParams.getCallback().onCrash(json, 4, "");
        DeliverUtils.postWithGzip(json, constructBizErrorUrl(json.optString("CrashMsg")));
    }

    public void uncaughtException(Thread thread, Throwable ex) {
        if (Math.abs(new Date().getTime() - this.mStartTime.getTime()) < 10000) {
            addLaunchCrashCount();
        }
        if (this.mContext != null) {
            Log.e(TAG, Process.myPid() + "******投递进程 = " + Utility.getCurrentProcessName(this.mContext));
        }
        try {
            if (!(handleException(ex) || this.mDefaultHandler == null)) {
                this.mDefaultHandler.uncaughtException(thread, ex);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e2) {
            e2.printStackTrace();
            Thread.currentThread().interrupt();
        }
        Log.e(TAG, "***********杀掉崩溃进程**********");
        String processName = Utility.getCurrentProcessName(this.mContext);
        if (processName == null || !processName.equals(this.mContext.getPackageName())) {
            if (DebugLog.isDebug()) {
                DebugLog.log(TAG, "交给系统处理");
                if (this.mDefaultHandler != null) {
                    this.mDefaultHandler.uncaughtException(thread, ex);
                    return;
                }
                return;
            }
            DebugLog.log(TAG, "杀进程处理");
            Process.killProcess(Process.myPid());
        } else if (this.mDefaultHandler != null) {
            this.mDefaultHandler.uncaughtException(thread, ex);
        }
    }

    public synchronized void sendCrashReportBackground() {
        DebugLog.log(TAG, "scan java crash log");
        if (this.mContext == null) {
            DebugLog.m1741e(TAG, "CrashHandler not initialized");
        } else if (NetworkUtil.isWifiOrEthernetOn(this.mContext)) {
            new Thread(new C20931(), "JCrashReporter Thread").start();
        } else {
            DebugLog.log(TAG, "sendCrashReport: not in wifi or ethernet status");
        }
    }

    public void sendCrashReport() {
        FileInputStream fin;
        Exception e;
        Throwable th;
        List<File> list = getSortedCrashFiles();
        if (list != null && list.size() > 0) {
            long lastCrashTime = 0;
            try {
                this.mCrashCount = Integer.parseInt(getErrorTimes());
            } catch (Exception e2) {
                this.mCrashCount = 0;
            }
            DebugLog.log(TAG, "Current crash times: ", Integer.valueOf(this.mCrashCount));
            for (File file : list) {
                boolean isFrequentCrash = false;
                long crashTime = file.lastModified();
                if (Math.abs(crashTime - lastCrashTime) < 15000) {
                    isFrequentCrash = true;
                }
                lastCrashTime = crashTime;
                if (this.mCrashCount <= -1 || this.mCrashCount > this.mCrashLimit || isFrequentCrash) {
                    file.delete();
                } else {
                    fin = null;
                    String urlstr = "";
                    JSONObject jsonLog = null;
                    try {
                        FileInputStream fin2 = new FileInputStream(file);
                        try {
                            JSONObject jsonLog2 = new JSONObject(CommonUtils.inputStreamToString(fin2));
                            if (fin2 != null) {
                                try {
                                    fin2.close();
                                } catch (IOException e3) {
                                    e3.printStackTrace();
                                }
                                file.delete();
                                jsonLog = jsonLog2;
                                fin = fin2;
                            } else {
                                jsonLog = jsonLog2;
                                fin = fin2;
                            }
                        } catch (Exception e4) {
                            e = e4;
                            fin = fin2;
                            try {
                                DebugLog.m1741e(TAG, "Can not parse crash log!");
                                e.printStackTrace();
                                if (fin != null) {
                                    try {
                                        fin.close();
                                    } catch (IOException e32) {
                                        e32.printStackTrace();
                                    }
                                    file.delete();
                                }
                                if (jsonLog == null) {
                                    try {
                                        urlstr = jsonLog.getString("Url");
                                        jsonLog.remove("Url");
                                        postCrashReport(jsonLog, urlstr);
                                    } catch (JSONException e5) {
                                        DebugLog.m1741e(TAG, "can not get url");
                                        e5.printStackTrace();
                                    }
                                }
                            } catch (Throwable th2) {
                                th = th2;
                            }
                        } catch (Throwable th3) {
                            th = th3;
                            fin = fin2;
                        }
                    } catch (Exception e6) {
                        e = e6;
                        DebugLog.m1741e(TAG, "Can not parse crash log!");
                        e.printStackTrace();
                        if (fin != null) {
                            fin.close();
                            file.delete();
                        }
                        if (jsonLog == null) {
                            urlstr = jsonLog.getString("Url");
                            jsonLog.remove("Url");
                            postCrashReport(jsonLog, urlstr);
                        }
                    }
                    if (jsonLog == null) {
                        urlstr = jsonLog.getString("Url");
                        jsonLog.remove("Url");
                        postCrashReport(jsonLog, urlstr);
                    }
                }
            }
            return;
        }
        return;
        file.delete();
        throw th;
        if (fin != null) {
            try {
                fin.close();
            } catch (IOException e322) {
                e322.printStackTrace();
            }
            file.delete();
        }
        throw th;
    }

    private List<File> getSortedCrashFiles() {
        List<File> list = getCrashFiles(this.mCrashFile, new ArrayList());
        if (list != null && list.size() > 0) {
            Collections.sort(list, new C20942());
        }
        return list;
    }

    private List<File> getCrashFiles(String path, List<File> files) {
        File pathFile = new File(path);
        if (pathFile.isDirectory()) {
            File[] subfiles = pathFile.listFiles();
            if (subfiles != null) {
                for (File file : subfiles) {
                    if (file != null && file.isFile() && file.getName().indexOf(".jca") > -1) {
                        files.add(file);
                    }
                }
            }
        }
        return files;
    }

    public boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }
        try {
            this.mCrashCount = Integer.parseInt(getErrorTimes());
        } catch (Exception e) {
            this.mCrashCount = 0;
        }
        DebugLog.log(TAG, "Current crash times: ", Integer.valueOf(this.mCrashCount));
        if (this.mCrashCount <= -1 || this.mCrashCount > this.mCrashLimit) {
            DebugLog.log(TAG, "超过当前最大投递次数 > ", Integer.valueOf(this.mCrashLimit));
        } else {
            this.mCrashCount++;
            if (saveErrorTimes(String.valueOf(this.mCrashCount))) {
                final JSONObject crashJson = constructLog(ex, true);
                String crashMsg = crashJson.optString("CrashMsg");
                ICrashCallback callback = this.mParams.getCallback();
                if (callback != null) {
                    if (callback.disableUploadCrash()) {
                        File destFile = new File(this.mCrashFile + File.separator + ((this.mParams.getV() == null ? Utility.getVersionName(this.mContext) : this.mParams.getV()) + "-" + this.mProcessName + "-" + new SimpleDateFormat("yyyyMMdd-hhmmss").format(new Date()) + ".jca"));
                        Utility.copyToFile(new File(getLastCrashFileName()), destFile);
                        try {
                            crashJson.put("Path", destFile.getAbsolutePath());
                        } catch (JSONException e2) {
                            e2.printStackTrace();
                        }
                    }
                    callback.onCrash(crashJson, 3, crashMsg);
                }
                JSONObject appData = this.mParams.getCallback().getAppData(this.mProcessName, true, 3);
                if (appData != null) {
                    try {
                        crashJson.put("AppData", appData);
                    } catch (JSONException e22) {
                        e22.printStackTrace();
                    }
                }
                if (callback == null || !callback.disableUploadCrash()) {
                    final String url = constructUrl();
                    new Thread(new Runnable() {
                        public void run() {
                            CrashHandler.this.postCrashReport(crashJson, url);
                        }
                    }, "JCrashReporter Thread").start();
                }
            } else {
                DebugLog.log(TAG, "can not write file,do not deliver crash log");
            }
            DebugLog.log(TAG, "Save error times!");
        }
        SaveCrashLog2File(ex);
        delCrashNumFile(this.mCrashFile);
        delCrashFiles();
        return true;
    }

    private void delCrashFiles() {
        try {
            File crashLogDirPath = new File(this.mContext.getExternalFilesDir(null).getAbsolutePath() + File.separator + "crash");
            if (crashLogDirPath.exists()) {
                File[] files = crashLogDirPath.listFiles();
                if (files == null) {
                    return;
                }
                if (files.length <= 50) {
                    DebugLog.log(TAG, "未达到崩溃文件50个数限制，不启动删除逻辑");
                    return;
                }
                for (File file : files) {
                    if (file.getName().startsWith("crash-") && file.getName().endsWith(".log")) {
                        file.delete();
                        DebugLog.log(TAG, "delete file = ", file.getAbsolutePath());
                    }
                }
                return;
            }
            DebugLog.m1740d(TAG, "crash dir does not exist!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void delCrashNumFile(String filepath) {
        this.suffix = this.mFormatter.format(new Date());
        try {
            File[] files = new File(filepath).listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.getAbsolutePath().contains(CRASH_TAG) && !file.getAbsolutePath().endsWith(this.suffix)) {
                        file.delete();
                        DebugLog.log(TAG, "delete file = ", file.getAbsolutePath());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private JSONObject constructLog(Throwable ex, boolean isCrash) {
        DebugLog.m1740d(TAG, "Construct java crash log");
        StringWriter localStringWriter = new StringWriter();
        PrintWriter localPrintWriter = new PrintWriter(localStringWriter);
        ex.printStackTrace(localPrintWriter);
        localPrintWriter.close();
        StringBuilder buffer = new StringBuilder();
        buffer.append(getCatchHeader());
        buffer.append(localStringWriter.toString());
        String crashMsg = buffer.toString();
        if (!TextUtils.isEmpty(crashMsg) && crashMsg.length() > 4096) {
            crashMsg = crashMsg.substring(0, 4095);
        }
        JSONObject json = new JSONObject();
        try {
            json.put("CrashMsg", crashMsg);
            json.put("StartTime", this.timeFormatter.format(this.mStartTime));
            json.put("CrashTime", this.timeFormatter.format(new Date()));
            json.put("BuildTime", DeliverUtils.encoding(this.mParams.getBuildVersion()));
            json.put("Pid", String.valueOf(Process.myPid()));
            json.put("Pname", this.mProcessName);
            json.put("Tname", Thread.currentThread().getName());
            json.put("Tid", String.valueOf(Process.myTid()));
            json.put("Signature", String.valueOf(this.mContext.getPackageManager().getPackageInfo(this.mContext.getPackageName(), 64).signatures[0].hashCode()));
            if (needAdditionalLog(crashMsg)) {
                DebugLog.enableLogBuffer(false);
                String pid = String.valueOf(Process.myPid());
                json.put("Threads", CommonUtils.inputStreamToString(new ProcessBuilder(new String[0]).command(new String[]{"ps", "-t", pid}).redirectErrorStream(true).start().getInputStream()));
                if (!this.mParams.isLogcatDisabled()) {
                    json.put("Logcat", DeliverUtils.encoding(CommonUtils.inputStreamToString(new ProcessBuilder(new String[0]).command(new String[]{"/system/bin/logcat", "-v", "threadtime", "-t", String.valueOf(this.mLogSize), "-d", "*:D"}).redirectErrorStream(true).start().getInputStream())));
                }
                json.put("Events", DeliverUtils.encoding(CommonUtils.inputStreamToString(new ProcessBuilder(new String[0]).command(new String[]{"/system/bin/logcat", "-v", "threadtime", "-b", JsonBundleConstants.A71_TRACKING_EVENTS, "-t", String.valueOf(this.mLogSize), "-d"}).redirectErrorStream(true).start().getInputStream())));
                DebugLog.log(TAG, "fill qiyi log");
                json.put("QiyiLog", DeliverUtils.encoding(DebugLog.logBuffer.toString()));
            }
            CommonUtils.fillDeviceinfo(this.mContext, json);
            CommonUtils.fillDeviceRuntimeInfo(this.mContext, json);
            if (isCrash) {
                saveLastCrash(json);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }

    private void saveLastCrash(JSONObject json) {
        Exception e;
        Throwable th;
        FileOutputStream fileOutputStream = null;
        try {
            FileOutputStream fos = new FileOutputStream(getLastCrashFileName());
            try {
                Iterator iterator = json.keys();
                while (iterator.hasNext()) {
                    String value = URLDecoder.decode(json.getString((String) iterator.next()), "UTF-8");
                    fos.write(String.format("\n>>> %s <<<\n", new Object[]{key}).getBytes("UTF-8"));
                    fos.write(value.getBytes("UTF-8"));
                }
                fos.flush();
                if (fos != null) {
                    try {
                        fos.close();
                        fileOutputStream = fos;
                        return;
                    } catch (IOException e2) {
                        e2.printStackTrace();
                        fileOutputStream = fos;
                        return;
                    }
                }
            } catch (Exception e3) {
                e = e3;
                fileOutputStream = fos;
                try {
                    e.printStackTrace();
                    if (fileOutputStream != null) {
                        try {
                            fileOutputStream.close();
                        } catch (IOException e22) {
                            e22.printStackTrace();
                        }
                    }
                } catch (Throwable th2) {
                    th = th2;
                    if (fileOutputStream != null) {
                        try {
                            fileOutputStream.close();
                        } catch (IOException e222) {
                            e222.printStackTrace();
                        }
                    }
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                fileOutputStream = fos;
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
                throw th;
            }
        } catch (Exception e4) {
            e = e4;
            e.printStackTrace();
            if (fileOutputStream != null) {
                fileOutputStream.close();
            }
        }
    }

    private String constructJsErrorUrl() {
        String crpo = "0";
        String crplg = "";
        String crplgv = "";
        String po = "0";
        if (NetworkUtil.isNetworkOff(this.mContext)) {
            po = "1";
        }
        return DeliverUtils.constructUrl(this.mContext, new JsErrorStatistics(null, po, crpo, crplg, null, crplgv, CrashReporter.getInstance().getPatchVersion()));
    }

    private String constructJsWarningUrl(String plgt, String plgid) {
        String crpo = plgt;
        String crplg = plgid;
        String crplgv = "";
        String po = "0";
        if (NetworkUtil.isNetworkOff(this.mContext)) {
            po = "1";
        }
        return DeliverUtils.constructUrl(this.mContext, new RnJsErrorStatistics(null, po, crpo, crplg, null, crplgv, CrashReporter.getInstance().getPatchVersion()));
    }

    private String constructBizErrorUrl(String crashMsg) {
        return DeliverUtils.constructUrl(this.mContext, new BizErrorStatistics(this.mParams.getCrpo(), this.mParams.getCrplg(), this.mParams.getCrplgv(), CrashReporter.getInstance().getPatchVersion()));
    }

    private String constructUrl() {
        String crpo = this.mParams.getCrpo();
        String crplg = this.mParams.getCrplg();
        String crplgv = this.mParams.getCrplgv();
        String po = "0";
        String crashtp = "0";
        boolean isRn = isRn();
        boolean isWebView = isWebView();
        if (NetworkUtil.isNetworkOff(this.mContext)) {
            po = "1";
        }
        String pchv = CrashReporter.getInstance().getPatchVersion();
        if (isRn) {
            return DeliverUtils.constructUrl(this.mContext, new RnCrashStatistics("5", po, crpo, crashtp, crplg, null, crplgv, pchv));
        } else if (isWebView) {
            return DeliverUtils.constructUrl(this.mContext, new RnCrashStatistics("5", po, crpo, crashtp, crplg, null, crplgv, pchv));
        } else {
            return DeliverUtils.constructUrl(this.mContext, new JavaCrashStatistics("5", po, crpo, crashtp, crplg, null, crplgv, pchv));
        }
    }

    private void saveCrashLog(JSONObject json, String url) {
        Exception e;
        Throwable th;
        DebugLog.log(TAG, "save crash log to file");
        String version = this.mParams.getV();
        FileOutputStream fos = null;
        try {
            FileOutputStream fos2 = new FileOutputStream(this.mCrashFile + File.separator + (version + "-" + this.mProcessName + "-" + new SimpleDateFormat("yyyyMMdd-hhmmss").format(new Date()) + ".jca"));
            try {
                json.put("Url", url);
                fos2.write(json.toString().getBytes("UTF-8"));
                fos2.flush();
                if (fos2 != null) {
                    try {
                        fos2.close();
                        fos = fos2;
                        return;
                    } catch (IOException e2) {
                        e2.printStackTrace();
                        fos = fos2;
                        return;
                    }
                }
            } catch (Exception e3) {
                e = e3;
                fos = fos2;
                try {
                    e.printStackTrace();
                    if (fos != null) {
                        try {
                            fos.close();
                        } catch (IOException e22) {
                            e22.printStackTrace();
                        }
                    }
                } catch (Throwable th2) {
                    th = th2;
                    if (fos != null) {
                        try {
                            fos.close();
                        } catch (IOException e222) {
                            e222.printStackTrace();
                        }
                    }
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                fos = fos2;
                if (fos != null) {
                    fos.close();
                }
                throw th;
            }
        } catch (Exception e4) {
            e = e4;
            e.printStackTrace();
            if (fos != null) {
                fos.close();
            }
        }
    }

    private void postCrashReport(JSONObject json, String urlStr) {
        DebugLog.log(TAG, "post crash report");
        if (NetworkUtil.isNetworkOff(this.mContext)) {
            DebugLog.log(TAG, "network off");
            saveCrashLog(json, urlStr);
            return;
        }
        OutputStream os = null;
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(urlStr).openConnection();
            conn.setRequestMethod(HTTP.POST);
            conn.setDoOutput(true);
            conn.setRequestProperty(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded");
            os = conn.getOutputStream();
            os.write("msg=".getBytes());
            os.write(DeliverUtils.encoding(json.toString()).getBytes());
            os.flush();
            if (conn.getResponseCode() == 200) {
                Log.i(TAG, "send crash report:success");
            } else {
                Log.e(TAG, "send crash report:fail");
                saveCrashLog(json, urlStr);
            }
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e2) {
            e2.printStackTrace();
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e3) {
                    e3.printStackTrace();
                }
            }
        } catch (Throwable th) {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e32) {
                    e32.printStackTrace();
                }
            }
        }
    }

    private boolean needAdditionalLog(String crashMsg) {
        if (this.mParams.isFullLogEnabled()) {
            return true;
        }
        if (!NetworkUtil.isWifiOrEthernetOn(this.mContext)) {
            return false;
        }
        if (crashMsg.contains("IllegalStateException") || crashMsg.contains("OutOfMemoryError") || crashMsg.contains("TimeoutException") || crashMsg.contains("RuntimeException") || crashMsg.contains("BadTokenException") || crashMsg.contains("StackOverflowError") || crashMsg.contains("RemoteServiceException") || crashMsg.contains("IndexOutOfBoundsException") || crashMsg.contains("InflateException") || crashMsg.contains("VerifyError") || crashMsg.contains("UnsatisfiedLinkError") || crashMsg.contains("java.lang.Exception") || crashMsg.contains("java.lang.NoSuchMethodError") || crashMsg.contains("java.lang.IllegalArgumentException") || crashMsg.contains("SQLiteDiskIOException") || crashMsg.contains("offsetRectBetweenParentAndChild") || crashMsg.contains("SQLiteCantOpenDatabaseException") || crashMsg.contains("drawAccessibilityFocusedDrawableIfNeeded")) {
            return true;
        }
        return false;
    }

    private String getCatchHeader() {
        long timestamp = System.currentTimeMillis();
        String key = TextUtils.isEmpty(this.mParams.getMkey()) ? "" : this.mParams.getMkey();
        return "Catch" + timestamp + Utility.md5(key + this.mParams.getU() + Utility.getMobileModel() + MKEY) + ">>>@-->>>";
    }

    private boolean saveErrorTimes(String errorTimes) {
        IOException e;
        FileNotFoundException e2;
        Throwable th;
        Exception e3;
        boolean isSaveSuccess = false;
        this.suffix = this.mFormatter.format(new Date());
        File file = new File(this.mCrashFile + File.separator + CRASH_TAG + this.suffix);
        if (!file.exists()) {
            try {
                if (file.createNewFile()) {
                    Log.i(TAG, "文件>>>" + file.getAbsolutePath() + "创建成功");
                }
            } catch (IOException e4) {
                e4.printStackTrace();
            }
        }
        FileOutputStream fos = null;
        try {
            FileOutputStream fos2 = new FileOutputStream(file);
            try {
                fos2.write(errorTimes.getBytes("UTF-8"));
                fos2.flush();
                isSaveSuccess = true;
                Log.i(TAG, "save>>>errorTimes = " + errorTimes);
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
            return isSaveSuccess;
        } catch (IOException e9) {
            e422222 = e9;
            e422222.printStackTrace();
            if (fos != null) {
                fos.close();
            }
            DebugLog.log(TAG, "isSaveSuccess = ", Boolean.valueOf(isSaveSuccess));
            return isSaveSuccess;
        } catch (Exception e10) {
            e3 = e10;
            e3.printStackTrace();
            if (fos != null) {
                fos.close();
            }
            DebugLog.log(TAG, "isSaveSuccess = ", Boolean.valueOf(isSaveSuccess));
            return isSaveSuccess;
        }
        DebugLog.log(TAG, "isSaveSuccess = ", Boolean.valueOf(isSaveSuccess));
        return isSaveSuccess;
    }

    private String getErrorTimes() {
        InputStreamReader isr;
        FileNotFoundException e;
        IOException e2;
        Throwable th;
        this.suffix = this.mFormatter.format(new Date());
        File file = new File(this.mCrashFile + File.separator + CRASH_TAG + this.suffix);
        if (!file.exists()) {
            try {
                if (file.createNewFile()) {
                    Log.i(TAG, "文件>>>" + file.getAbsolutePath() + "创建成功");
                    return "1";
                }
            } catch (IOException e3) {
                return "0";
            }
        }
        BufferedReader br = null;
        FileInputStream fis = null;
        InputStreamReader isr2 = null;
        try {
            BufferedReader br2;
            FileInputStream fis2 = new FileInputStream(file);
            try {
                isr = new InputStreamReader(fis2, "UTF-8");
                try {
                    br2 = new BufferedReader(isr);
                } catch (FileNotFoundException e4) {
                    e = e4;
                    isr2 = isr;
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
                        }
                        return "0";
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
                    isr2 = isr;
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
                    return "0";
                } catch (Throwable th3) {
                    th = th3;
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
            } catch (FileNotFoundException e7) {
                e = e7;
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
                return "0";
            } catch (IOException e8) {
                e2222222222 = e8;
                fis = fis2;
                e2222222222.printStackTrace();
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
                return "0";
            } catch (Throwable th4) {
                th = th4;
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
                String line = br2.readLine();
                if (TextUtils.isEmpty(line)) {
                    line = "1";
                }
                Log.i(TAG, "getline>>>errorTimes = " + line);
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
                if (fis2 == null) {
                    return line;
                }
                try {
                    fis2.close();
                    return line;
                } catch (IOException e2222222222222) {
                    e2222222222222.printStackTrace();
                    return line;
                }
            } catch (FileNotFoundException e9) {
                e = e9;
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
                return "0";
            } catch (IOException e10) {
                e2222222222222 = e10;
                isr2 = isr;
                fis = fis2;
                br = br2;
                e2222222222222.printStackTrace();
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
                return "0";
            } catch (Throwable th5) {
                th = th5;
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
        } catch (FileNotFoundException e11) {
            e = e11;
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
            return "0";
        } catch (IOException e12) {
            e2222222222222 = e12;
            e2222222222222.printStackTrace();
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
            return "0";
        }
    }

    private void SaveCrashLog2File(Throwable ex) {
        Exception e;
        Throwable th;
        StringBuffer sb = new StringBuffer();
        try {
            DateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd:hh:mm:ss");
            String basicInfo = getBasicInfo(this.mContext);
            String currentTime = simpleDateFormat.format(new Date());
            sb.append("**********basicInfo*************\n" + basicInfo);
            sb.append("crash time = " + currentTime + "\n");
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        for (Throwable cause = ex.getCause(); cause != null; cause = cause.getCause()) {
            cause.printStackTrace(printWriter);
        }
        printWriter.close();
        String result = writer.toString();
        sb.append(result);
        Log.e(TAG, "崩溃信息 = " + sb.toString());
        FileOutputStream fos = null;
        try {
            boolean isCreateDirSucces;
            String crashFileName = "crash-" + new SimpleDateFormat("yyyyMMdd-hhmmss").format(new Date()) + "-" + System.currentTimeMillis() + ".jca";
            File crashDirFile = new File(this.mContext.getExternalFilesDir(null).getAbsolutePath() + File.separator + "app" + File.separator + "crash");
            if (crashDirFile.exists() && crashDirFile.isDirectory()) {
                isCreateDirSucces = true;
            } else {
                Log.d(TAG, crashDirFile + " dir not exist");
                this.mContext.getExternalFilesDir(null);
                isCreateDirSucces = crashDirFile.mkdirs();
            }
            if (isCreateDirSucces) {
                FileOutputStream fileOutputStream = new FileOutputStream(crashDirFile + File.separator + crashFileName);
                try {
                    fileOutputStream.write(sb.toString().getBytes());
                    if (DebugLog.isDebug() && result.contains("OutOfMemoryError")) {
                        long time = System.currentTimeMillis();
                        String dumpPath = crashDirFile + File.separator + "heapdump" + time + ".hprof";
                        String zipPath = crashDirFile + File.separator + "heapdump" + time + ".zip";
                        Debug.dumpHprofData(dumpPath);
                        zipDumpFile(dumpPath, zipPath);
                        File dumpFile = new File(dumpPath);
                        if (dumpFile.isFile() && dumpFile.exists()) {
                            dumpFile.delete();
                        }
                        Log.e(TAG, "发生OOM，heap dump文件：" + zipPath);
                    }
                    fos = fileOutputStream;
                } catch (Exception e3) {
                    e2 = e3;
                    fos = fileOutputStream;
                    try {
                        e2.printStackTrace();
                        if (fos != null) {
                            try {
                                fos.close();
                            } catch (IOException e4) {
                                e4.printStackTrace();
                                return;
                            }
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        if (fos != null) {
                            try {
                                fos.close();
                            } catch (IOException e42) {
                                e42.printStackTrace();
                            }
                        }
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    fos = fileOutputStream;
                    if (fos != null) {
                        fos.close();
                    }
                    throw th;
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e422) {
                    e422.printStackTrace();
                }
            }
        } catch (Exception e5) {
            e2 = e5;
            e2.printStackTrace();
            if (fos != null) {
                fos.close();
            }
        }
    }

    public boolean zipDumpFile(String originFilePath, String zipFilePath) {
        boolean z;
        IOException e;
        Throwable th;
        File zipFile = new File(zipFilePath);
        File zipFileDir = zipFile.getParentFile();
        if (!(zipFileDir == null || zipFileDir.exists())) {
            zipFileDir.mkdirs();
        }
        if (zipFile.exists()) {
            zipFile.delete();
        }
        FileOutputStream out = null;
        ZipOutputStream zipOut = null;
        FileInputStream in = null;
        try {
            FileOutputStream out2 = new FileOutputStream(zipFilePath);
            try {
                ZipOutputStream zipOut2 = new ZipOutputStream(out2);
                try {
                    if (TextUtils.isEmpty(originFilePath)) {
                        z = false;
                        if (zipOut2 != null) {
                            try {
                                zipOut2.close();
                            } catch (IOException e2) {
                                e2.printStackTrace();
                            }
                        }
                        if (out2 != null) {
                            try {
                                out2.close();
                            } catch (IOException e22) {
                                e22.printStackTrace();
                            }
                        }
                        if (in != null) {
                            try {
                                in.close();
                            } catch (IOException e222) {
                                e222.printStackTrace();
                            }
                        }
                        zipOut = zipOut2;
                        out = out2;
                    } else {
                        File originFile = new File(originFilePath);
                        if (originFile.exists()) {
                            FileInputStream in2 = new FileInputStream(originFile);
                            try {
                                zipOut2.putNextEntry(new ZipEntry(originFile.getName()));
                                byte[] buffer = new byte[1024];
                                while (true) {
                                    int nNumber = in2.read(buffer);
                                    if (nNumber == -1) {
                                        break;
                                    }
                                    zipOut2.write(buffer, 0, nNumber);
                                }
                                zipOut2.closeEntry();
                                z = true;
                                if (zipOut2 != null) {
                                    try {
                                        zipOut2.close();
                                    } catch (IOException e2222) {
                                        e2222.printStackTrace();
                                    }
                                }
                                if (out2 != null) {
                                    try {
                                        out2.close();
                                    } catch (IOException e22222) {
                                        e22222.printStackTrace();
                                    }
                                }
                                if (in2 != null) {
                                    try {
                                        in2.close();
                                    } catch (IOException e222222) {
                                        e222222.printStackTrace();
                                    }
                                }
                                in = in2;
                                zipOut = zipOut2;
                                out = out2;
                            } catch (IOException e3) {
                                e222222 = e3;
                                in = in2;
                                zipOut = zipOut2;
                                out = out2;
                                try {
                                    e222222.printStackTrace();
                                    z = false;
                                    if (zipOut != null) {
                                        try {
                                            zipOut.close();
                                        } catch (IOException e2222222) {
                                            e2222222.printStackTrace();
                                        }
                                    }
                                    if (out != null) {
                                        try {
                                            out.close();
                                        } catch (IOException e22222222) {
                                            e22222222.printStackTrace();
                                        }
                                    }
                                    if (in != null) {
                                        try {
                                            in.close();
                                        } catch (IOException e222222222) {
                                            e222222222.printStackTrace();
                                        }
                                    }
                                    return z;
                                } catch (Throwable th2) {
                                    th = th2;
                                    if (zipOut != null) {
                                        try {
                                            zipOut.close();
                                        } catch (IOException e2222222222) {
                                            e2222222222.printStackTrace();
                                        }
                                    }
                                    if (out != null) {
                                        try {
                                            out.close();
                                        } catch (IOException e22222222222) {
                                            e22222222222.printStackTrace();
                                        }
                                    }
                                    if (in != null) {
                                        try {
                                            in.close();
                                        } catch (IOException e222222222222) {
                                            e222222222222.printStackTrace();
                                        }
                                    }
                                    throw th;
                                }
                            } catch (Throwable th3) {
                                th = th3;
                                in = in2;
                                zipOut = zipOut2;
                                out = out2;
                                if (zipOut != null) {
                                    zipOut.close();
                                }
                                if (out != null) {
                                    out.close();
                                }
                                if (in != null) {
                                    in.close();
                                }
                                throw th;
                            }
                        }
                        z = false;
                        if (zipOut2 != null) {
                            try {
                                zipOut2.close();
                            } catch (IOException e2222222222222) {
                                e2222222222222.printStackTrace();
                            }
                        }
                        if (out2 != null) {
                            try {
                                out2.close();
                            } catch (IOException e22222222222222) {
                                e22222222222222.printStackTrace();
                            }
                        }
                        if (in != null) {
                            try {
                                in.close();
                            } catch (IOException e222222222222222) {
                                e222222222222222.printStackTrace();
                            }
                        }
                        zipOut = zipOut2;
                        out = out2;
                    }
                } catch (IOException e4) {
                    e222222222222222 = e4;
                    zipOut = zipOut2;
                    out = out2;
                    e222222222222222.printStackTrace();
                    z = false;
                    if (zipOut != null) {
                        zipOut.close();
                    }
                    if (out != null) {
                        out.close();
                    }
                    if (in != null) {
                        in.close();
                    }
                    return z;
                } catch (Throwable th4) {
                    th = th4;
                    zipOut = zipOut2;
                    out = out2;
                    if (zipOut != null) {
                        zipOut.close();
                    }
                    if (out != null) {
                        out.close();
                    }
                    if (in != null) {
                        in.close();
                    }
                    throw th;
                }
            } catch (IOException e5) {
                e222222222222222 = e5;
                out = out2;
                e222222222222222.printStackTrace();
                z = false;
                if (zipOut != null) {
                    zipOut.close();
                }
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
                return z;
            } catch (Throwable th5) {
                th = th5;
                out = out2;
                if (zipOut != null) {
                    zipOut.close();
                }
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
                throw th;
            }
        } catch (IOException e6) {
            e222222222222222 = e6;
            e222222222222222.printStackTrace();
            z = false;
            if (zipOut != null) {
                zipOut.close();
            }
            if (out != null) {
                out.close();
            }
            if (in != null) {
                in.close();
            }
            return z;
        }
        return z;
    }

    private String getBasicInfo(Context context) {
        StringBuilder debug_info = new StringBuilder();
        try {
            debug_info.append("imei = " + Utility.getIMEI(context) + "\n");
            debug_info.append("model = " + Utility.getDeviceName() + "\n");
            debug_info.append("qiyi key = " + this.mParams.getMkey() + "\n");
            debug_info.append("app version = " + this.mParams.getV() + "\n");
            debug_info.append("os version = " + Utility.getOSVersionInfo() + "\n");
            debug_info.append("ua = " + DeliverUtils.encoding(Utility.getMobileModel()) + "\n");
            if (!Utility.isTvGuo()) {
                debug_info.append("network type = " + NetworkUtil.getNetWorkType(context) + "\n");
                NetworkStatus status = NetworkUtil.getNetworkStatusFor4G(context);
                String netInfo = "";
                if (status == NetworkStatus.OFF) {
                    netInfo = "无网络";
                } else if (status == NetworkStatus.MOBILE_2G) {
                    netInfo = "2G网络";
                } else if (status == NetworkStatus.MOBILE_3G) {
                    netInfo = "3G网络";
                } else if (status == NetworkStatus.MOBILE_4G) {
                    netInfo = "4G网络";
                } else if (status == NetworkStatus.WIFI) {
                    netInfo = "wifi网络";
                } else if (status == NetworkStatus.OTHER) {
                    netInfo = "other网络";
                }
                debug_info.append("network status= " + netInfo + "\n");
            }
        } catch (Exception e) {
            debug_info.append("获取客户端信息异常");
            e.printStackTrace();
        }
        return debug_info.toString();
    }

    private boolean isRn() {
        return this.mParams.isRn();
    }

    private boolean isWebView() {
        return this.mParams.isWebview();
    }
}
