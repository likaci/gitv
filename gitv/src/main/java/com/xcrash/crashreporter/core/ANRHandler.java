package com.xcrash.crashreporter.core;

import android.app.ActivityManager;
import android.app.ActivityManager.ProcessErrorStateInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Handler;
import android.os.Looper;
import android.os.Process;
import android.text.TextUtils;
import android.util.Log;
import com.mcto.ads.internal.common.JsonBundleConstants;
import com.tvos.apps.utils.DateUtil;
import com.xcrash.crashreporter.CrashReporter;
import com.xcrash.crashreporter.bean.AnrStatistics;
import com.xcrash.crashreporter.generic.CrashReportParams;
import com.xcrash.crashreporter.utils.CommonUtils;
import com.xcrash.crashreporter.utils.CrashConst;
import com.xcrash.crashreporter.utils.DebugLog;
import com.xcrash.crashreporter.utils.DeliverUtils;
import com.xcrash.crashreporter.utils.NetworkUtil;
import com.xcrash.crashreporter.utils.Utility;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;
import java.util.regex.Pattern;
import org.cybergarage.soap.SOAP;
import org.json.JSONObject;

public class ANRHandler extends BroadcastReceiver {
    private static final String ANR_INTENT = "android.intent.action.ANR";
    public static final String CHAR_SET = "UTF-8";
    public static final String LAST_ANR_NAME = "anr_last";
    private static final int MAX_ANR_COUNT = 5;
    private static final int MAX_TRACE_SIZE = 512000;
    private static final String TAG = "xcrash.ANRHandler";
    private static ANRHandler instance;
    private String anrPath;
    private DateFormat formatter = new SimpleDateFormat(DateUtil.PATTERN_STANDARD10H);
    public int mAnrCount;
    private Context mContext;
    private String mErrorProcess;
    private Handler mHandler;
    public Date mLastCrashTime;
    private int mLogSize = 200;
    private CrashReportParams mParams;
    private String mProcessName;
    private int mReportLimit = 50;
    public int mReportType;
    private Date mStartTime;
    private DateFormat timeFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

    private ANRHandler() {
    }

    public static ANRHandler getInstance() {
        if (instance == null) {
            instance = new ANRHandler();
        }
        return instance;
    }

    public void init(Context ctx, String processName, int policy, int maxCount, int logSize, CrashReportParams params) {
        this.mReportType = policy;
        if (maxCount > 5) {
            maxCount = 5;
        }
        this.mReportLimit = maxCount;
        this.mProcessName = processName;
        this.mLogSize = logSize;
        this.mParams = params;
        init(ctx);
    }

    public void init(Context context) {
        if (this.mReportType == 0) {
            Log.i(TAG, "ANR reporter disabled");
        } else if (this.mContext != null) {
            DebugLog.e(TAG, "anr handler already initialized");
        } else {
            Log.i(TAG, "start anr monitor");
            this.mContext = context;
            this.anrPath = CommonUtils.getCrashDirectory(this.mContext);
            register(this.mContext);
            this.mStartTime = new Date();
        }
    }

    public void stop() {
        if (this.mContext == null) {
            Log.w(TAG, "anr monitor not initialized");
            return;
        }
        Log.i(TAG, "stop monitor anr");
        this.mContext = null;
        instance.unregister();
    }

    private void register(final Context context) {
        Thread monitorThread = new Thread(new Runnable() {
            public void run() {
                Looper.prepare();
                synchronized (ANRHandler.this) {
                    ANRHandler.this.mHandler = new Handler();
                    ANRHandler.this.notifyAll();
                }
                Log.i(ANRHandler.TAG, "start anr monitor thread");
                try {
                    context.registerReceiver(ANRHandler.this, new IntentFilter(ANRHandler.ANR_INTENT), null, ANRHandler.this.mHandler);
                } catch (Exception e) {
                    e.printStackTrace();
                    DebugLog.e(ANRHandler.TAG, "register anr receiver fail");
                }
                Looper.loop();
                Log.i(ANRHandler.TAG, "stop anr monitor thread");
                context.unregisterReceiver(ANRHandler.this);
                ANRHandler.this.mHandler = null;
            }
        }, TAG);
        monitorThread.setDaemon(true);
        monitorThread.start();
    }

    private void unregister() {
        Looper looper = this.mHandler.getLooper();
        looper.quit();
        try {
            looper.getThread().join();
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }

    private ProcessErrorStateInfo getProcessErrorInfo() {
        List<ProcessErrorStateInfo> processErrorList = ((ActivityManager) this.mContext.getSystemService("activity")).getProcessesInErrorState();
        if (processErrorList != null) {
            for (ProcessErrorStateInfo errorStateInfo : processErrorList) {
                if (errorStateInfo.condition == 2) {
                    DebugLog.d(TAG, errorStateInfo.processName, " Process error info :", errorStateInfo.shortMsg);
                    if (errorStateInfo.longMsg == null) {
                        return errorStateInfo;
                    }
                    Log.i(TAG, errorStateInfo.longMsg);
                    return errorStateInfo;
                }
            }
        }
        return null;
    }

    public void sendAnrTracesBackground() {
        if (this.mContext == null) {
            DebugLog.e(TAG, "ANRHandler not initialized");
            return;
        }
        new Thread(new Runnable() {
            public void run() {
                FileInputStream fin;
                Throwable e;
                Throwable th;
                List<File> list = ANRHandler.this.getAnrFiles(ANRHandler.this.anrPath, new ArrayList());
                Date current = new Date();
                if (list != null && list.size() > 0) {
                    ANRHandler.this.getAnrInfo();
                    long lastCrash = 0;
                    for (File file : list) {
                        boolean isFrequentAnr = false;
                        boolean isOldAnr = false;
                        long crashTime = file.lastModified();
                        if (lastCrash != 0 && Math.abs(crashTime - lastCrash) < 15000) {
                            isFrequentAnr = true;
                        }
                        if (Math.abs(current.getTime() - crashTime) > 172800000) {
                            isOldAnr = true;
                        }
                        lastCrash = crashTime;
                        if (ANRHandler.this.mAnrCount >= ANRHandler.this.mReportLimit || isFrequentAnr || isOldAnr) {
                            file.delete();
                        } else {
                            fin = null;
                            try {
                                FileInputStream fin2 = new FileInputStream(file);
                                try {
                                    JSONObject jsonLog = new JSONObject(CommonUtils.inputStreamToString(fin2));
                                    JSONObject jSONObject;
                                    try {
                                        String url = jsonLog.getString("Url");
                                        jsonLog.remove("Url");
                                        if (ANRHandler.this.postCrashReport(jsonLog, url)) {
                                            file.delete();
                                        }
                                        if (fin2 != null) {
                                            try {
                                                fin2.close();
                                                jSONObject = jsonLog;
                                                fin = fin2;
                                            } catch (IOException e2) {
                                                e2.printStackTrace();
                                                jSONObject = jsonLog;
                                                fin = fin2;
                                            }
                                        } else {
                                            fin = fin2;
                                        }
                                    } catch (Throwable th2) {
                                        th = th2;
                                        jSONObject = jsonLog;
                                        fin = fin2;
                                    }
                                } catch (Throwable th3) {
                                    th = th3;
                                    fin = fin2;
                                }
                            } catch (Throwable th4) {
                                e = th4;
                                DebugLog.e(ANRHandler.TAG, "Error occurs! Give up this post.");
                                e.printStackTrace();
                                if (fin == null) {
                                    fin.close();
                                }
                            }
                        }
                    }
                    return;
                }
                return;
                if (fin != null) {
                    try {
                        fin.close();
                    } catch (IOException e22) {
                        e22.printStackTrace();
                    }
                }
                throw th;
                throw th;
            }
        }, "AnrReporter").start();
    }

    private List<File> getAnrFiles(String path, List<File> files) {
        File pathFile = new File(path);
        if (pathFile != null && pathFile.isDirectory()) {
            File[] subfiles = pathFile.listFiles();
            if (subfiles != null) {
                for (File file : subfiles) {
                    if (file.isFile() && file.getName().indexOf(".anr") > -1) {
                        files.add(file);
                    }
                }
            }
        }
        return files;
    }

    private JSONObject getAllThreadTracesLog(String processName) {
        StringBuilder sb = new StringBuilder();
        Thread main = Looper.getMainLooper().getThread();
        sb.append("Cmd line: " + processName + "\n");
        if (main != null) {
            sb.append("\"main\" prio=" + main.getPriority() + " tid=" + main.getId() + " " + main.getState() + "\n");
            for (StackTraceElement e : main.getStackTrace()) {
                sb.append("at " + e.getClassName() + "." + e.getMethodName() + "(" + e.getFileName() + SOAP.DELIM + e.getLineNumber() + ")\n");
            }
            sb.append("\n");
        }
        for (Entry<Thread, StackTraceElement[]> entry : Thread.getAllStackTraces().entrySet()) {
            Thread thread = (Thread) entry.getKey();
            if (thread != main) {
                sb.append("\"" + thread.getName() + "\" prio=" + thread.getPriority() + " tid=" + thread.getId() + " " + thread.getState() + "\n");
                for (StackTraceElement e2 : (StackTraceElement[]) entry.getValue()) {
                    sb.append("at " + e2.getClassName() + "." + e2.getMethodName() + "(" + e2.getFileName() + SOAP.DELIM + e2.getLineNumber() + ")\n");
                }
                sb.append("\n");
            }
        }
        return constructAnrLog(sb.toString(), processName);
    }

    private JSONObject processTraceFile(File file, String processName) {
        Throwable e;
        Throwable th;
        DebugLog.d(TAG, "process traces file ", file.getAbsolutePath());
        boolean hasTraces = false;
        FileInputStream fin = null;
        BufferedReader br = null;
        try {
            StringBuilder sb = new StringBuilder();
            FileInputStream fin2 = new FileInputStream(file);
            try {
                BufferedReader br2 = new BufferedReader(new InputStreamReader(fin2));
                boolean start = false;
                try {
                    JSONObject constructAnrLog;
                    String preLine = "";
                    Pattern pattern = Pattern.compile(String.format("^Cmd\\sline:\\s%s.*", new Object[]{this.mContext.getPackageName()}));
                    int length = 0;
                    while (true) {
                        String line = br2.readLine();
                        if (line != null && length < MAX_TRACE_SIZE) {
                            if (pattern.matcher(line).matches()) {
                                start = true;
                                hasTraces = true;
                                sb.append(preLine);
                            }
                            if (start) {
                                sb.append(line + "\n");
                                length += line.length();
                            }
                            preLine = line + "\n";
                            if (start && line.contains("----- end")) {
                                start = false;
                            }
                        } else if (hasTraces) {
                            if (br2 != null) {
                                try {
                                    br2.close();
                                } catch (IOException e2) {
                                    e2.printStackTrace();
                                }
                            }
                            if (fin2 == null) {
                                try {
                                    fin2.close();
                                    br = br2;
                                    fin = fin2;
                                } catch (IOException e22) {
                                    e22.printStackTrace();
                                    br = br2;
                                    fin = fin2;
                                }
                            } else {
                                fin = fin2;
                            }
                            return null;
                        } else {
                            constructAnrLog = constructAnrLog(sb.toString(), processName);
                            if (br2 != null) {
                                try {
                                    br2.close();
                                } catch (IOException e222) {
                                    e222.printStackTrace();
                                }
                            }
                            if (fin2 != null) {
                                try {
                                    fin2.close();
                                } catch (IOException e2222) {
                                    e2222.printStackTrace();
                                }
                            }
                            br = br2;
                            fin = fin2;
                            return constructAnrLog;
                        }
                    }
                    if (hasTraces) {
                        if (br2 != null) {
                            br2.close();
                        }
                        if (fin2 == null) {
                            fin = fin2;
                        } else {
                            fin2.close();
                            br = br2;
                            fin = fin2;
                        }
                        return null;
                    }
                    constructAnrLog = constructAnrLog(sb.toString(), processName);
                    if (br2 != null) {
                        br2.close();
                    }
                    if (fin2 != null) {
                        fin2.close();
                    }
                    br = br2;
                    fin = fin2;
                    return constructAnrLog;
                } catch (Throwable th2) {
                    th = th2;
                    br = br2;
                    fin = fin2;
                    if (br != null) {
                        br.close();
                    }
                    if (fin != null) {
                        fin.close();
                    }
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                fin = fin2;
                if (br != null) {
                    br.close();
                }
                if (fin != null) {
                    fin.close();
                }
                throw th;
            }
        } catch (Throwable th4) {
            e = th4;
            e.printStackTrace();
            if (br != null) {
                br.close();
            }
            if (fin != null) {
                fin.close();
            }
            return null;
        }
    }

    private JSONObject constructAnrLog(String traces, String processName) {
        Exception e;
        Throwable th;
        FileOutputStream fos = null;
        JSONObject anrJson = new JSONObject();
        File lastANR = new File(getLastANRFileName());
        if (lastANR.exists()) {
            lastANR.delete();
        }
        try {
            FileOutputStream fos2 = new FileOutputStream(getLastANRFileName());
            try {
                anrJson.put("bv", this.mParams.getBuildVersion());
                anrJson.put("traces", traces);
                fos2.write(">>> Traces <<<\n".getBytes("UTF-8"));
                fos2.write(traces.getBytes("UTF-8"));
                DebugLog.d(TAG, "fill logcat");
                String logcat = CommonUtils.inputStreamToString(new ProcessBuilder(new String[0]).command(new String[]{"/system/bin/logcat", "-v", "threadtime", "-t", String.valueOf(this.mLogSize), "-d", "*:D"}).redirectErrorStream(true).start().getInputStream());
                anrJson.put("log", URLEncoder.encode(logcat, "UTF-8"));
                fos2.write(">>> Logcat <<<\n".getBytes("UTF-8"));
                fos2.write(logcat.getBytes("UTF-8"));
                String events = CommonUtils.inputStreamToString(new ProcessBuilder(new String[0]).command(new String[]{"/system/bin/logcat", "-v", "threadtime", "-b", JsonBundleConstants.A71_TRACKING_EVENTS, "-t", String.valueOf(this.mLogSize), "-d"}).redirectErrorStream(true).start().getInputStream());
                anrJson.put(JsonBundleConstants.A71_TRACKING_EVENTS, URLEncoder.encode(events, "UTF-8"));
                fos2.write(">>> Events <<<\n".getBytes("UTF-8"));
                fos2.write(events.getBytes("UTF-8"));
                fos2.flush();
                String pid = String.valueOf(Process.myPid());
                anrJson.put("threads", CommonUtils.inputStreamToString(new ProcessBuilder(new String[0]).command(new String[]{"ps", "-t", pid}).redirectErrorStream(true).start().getInputStream()));
                anrJson.put("sttm", this.timeFormatter.format(this.mStartTime));
                anrJson.put("crtm", this.timeFormatter.format(new Date()));
                CommonUtils.fillDeviceinfoForANR(this.mContext, anrJson);
                this.mParams.getCallback().getAppData(processName, false, 2);
                if (fos2 != null) {
                    try {
                        fos2.close();
                        fos = fos2;
                    } catch (IOException e2) {
                        e2.printStackTrace();
                        fos = fos2;
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
                    return anrJson;
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
            return anrJson;
        }
        return anrJson;
    }

    private boolean postCrashReport(JSONObject json, String urlStr) {
        Log.i(TAG, "post anr report");
        if (this.mContext == null) {
            DebugLog.e(TAG, "AnrCrashHandler not initialized");
            return false;
        } else if (NetworkUtil.isWifiOrEthernetOn(this.mContext)) {
            return DeliverUtils.postWithGzip(json, urlStr);
        } else {
            DebugLog.log(TAG, "Send ANR CrashReport: not in wifi or ethernet status");
            return false;
        }
    }

    private void saveCrashLog(JSONObject json, String url) {
        String version;
        Exception e;
        Throwable th;
        DebugLog.log(TAG, "save crash log to file");
        if (TextUtils.isEmpty(this.mParams.getV())) {
            version = Utility.getVersionName(this.mContext);
        } else {
            version = this.mParams.getV();
        }
        FileOutputStream fos = null;
        try {
            FileOutputStream fos2 = new FileOutputStream(this.anrPath + File.separator + (version + "-" + this.mErrorProcess + "-" + new SimpleDateFormat("yyyyMMdd-hhmmss").format(new Date()) + ".anr"));
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

    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "receiving " + String.valueOf(intent));
        ProcessErrorStateInfo errorStateInfo = getProcessErrorInfo();
        if (errorStateInfo != null) {
            DebugLog.d(TAG, "anr process name ", errorStateInfo.processName);
            if (!errorStateInfo.processName.contains(this.mContext.getPackageName())) {
                Log.i(TAG, "anr not happened in qiyi；" + this.mContext.getPackageName());
            } else if (ANR_INTENT.equals(intent.getAction())) {
                try {
                    getAnrInfo();
                    Date lastAnrTime = this.mLastCrashTime;
                    Date anrTime = new Date();
                    this.mAnrCount++;
                    this.mLastCrashTime = anrTime;
                    setAnrInfo();
                    if ((lastAnrTime == null || anrTime.getTime() - lastAnrTime.getTime() >= 30000) && this.mAnrCount <= this.mReportLimit) {
                        AnrStatistics statistics;
                        String deliverUrl;
                        this.mErrorProcess = errorStateInfo.processName;
                        File traceFile = CommonUtils.getTraceFile(this.mErrorProcess);
                        JSONObject anrJson = null;
                        if (traceFile != null) {
                            try {
                                if (traceFile.exists()) {
                                    anrJson = processTraceFile(traceFile, errorStateInfo.processName);
                                    if (anrJson != null) {
                                        statistics = new AnrStatistics(this.mParams.getCrpo(), this.mParams.getCrplg(), "", CrashReporter.getInstance().getPatchVersion());
                                        deliverUrl = DeliverUtils.constructUrl(this.mContext, statistics);
                                        DeliverUtils.addMirrorPublicParamsToBody(this.mContext, anrJson, statistics);
                                        if (!postCrashReport(anrJson, deliverUrl)) {
                                            saveCrashLog(anrJson, deliverUrl);
                                            return;
                                        }
                                        return;
                                    }
                                    return;
                                }
                            } catch (Throwable th) {
                            }
                        }
                        Log.i(TAG, "trace not exist, try to get it from runtime");
                        if (errorStateInfo.processName.equals(this.mProcessName)) {
                            anrJson = getAllThreadTracesLog(errorStateInfo.processName);
                        }
                        if (anrJson != null) {
                            statistics = new AnrStatistics(this.mParams.getCrpo(), this.mParams.getCrplg(), "", CrashReporter.getInstance().getPatchVersion());
                            deliverUrl = DeliverUtils.constructUrl(this.mContext, statistics);
                            DeliverUtils.addMirrorPublicParamsToBody(this.mContext, anrJson, statistics);
                            if (!postCrashReport(anrJson, deliverUrl)) {
                                saveCrashLog(anrJson, deliverUrl);
                                return;
                            }
                            return;
                        }
                        return;
                    }
                    DebugLog.i(TAG, "too frequent anr,ignore");
                } catch (Throwable th2) {
                    DebugLog.e(TAG, "give up processing anr");
                }
            }
        }
    }

    public void setAnrInfo() {
        if (this.mLastCrashTime != null) {
            DebugLog.d(TAG, "setCrashInfo lastCrashTime ", this.mLastCrashTime.toString(), " count", Integer.valueOf(this.mAnrCount));
        }
        if (this.mContext != null) {
            String today = this.formatter.format(new Date());
            SharedPreferences sp = this.mContext.getSharedPreferences(CrashConst.SP_CRASH_REPORTER, 4);
            String storeDate = sp.getString(CrashConst.KEY_ANR_DATE, null);
            String timeStr = null;
            if (this.mLastCrashTime != null) {
                timeStr = this.timeFormatter.format(this.mLastCrashTime).toString();
            }
            Editor editor = sp.edit();
            editor.putString(CrashConst.KEY_ANR_LAST, timeStr);
            editor.putString(CrashConst.KEY_ANR_DATE, today);
            if (storeDate == null || storeDate.equals(today)) {
                editor.putInt(CrashConst.KEY_ANR_COUNT, this.mAnrCount);
            } else {
                editor.putInt(CrashConst.KEY_ANR_COUNT, 1);
            }
            editor.apply();
        }
    }

    public void getAnrInfo() {
        if (this.mContext != null) {
            String today = this.formatter.format(new Date());
            SharedPreferences sp = this.mContext.getSharedPreferences(CrashConst.SP_CRASH_REPORTER, 4);
            String storeDate = sp.getString(CrashConst.KEY_ANR_DATE, null);
            if (storeDate == null || !storeDate.equals(today)) {
                this.mAnrCount = 0;
                this.mLastCrashTime = null;
            } else {
                String time = sp.getString(CrashConst.KEY_ANR_LAST, null);
                this.mAnrCount = sp.getInt(CrashConst.KEY_ANR_COUNT, 0);
                if (time != null) {
                    try {
                        this.mLastCrashTime = this.timeFormatter.parse(time);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
            if (this.mLastCrashTime != null) {
                DebugLog.d(TAG, "getCrashInfo:count ", Integer.valueOf(this.mAnrCount), " lastCrashTime ", this.mLastCrashTime.toString());
            }
        }
    }

    public String getLastANRFileName() {
        return this.anrPath + File.separator + LAST_ANR_NAME;
    }
}
