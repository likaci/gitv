package com.xcrash.crashreporter.utils;

import android.text.TextUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONException;
import org.json.JSONObject;

public class LogParser {
    protected static String parseStr(String log, String patStr) throws UnsupportedEncodingException {
        Matcher matcher = Pattern.compile(patStr).matcher(log);
        if (!matcher.find()) {
            return "";
        }
        String matchStr = matcher.group(1);
        if (matchStr != null) {
            return matchStr.trim();
        }
        return "";
    }

    protected static int parseInt(String log, String patStr) {
        int i = -1;
        Matcher matcher = Pattern.compile(patStr).matcher(log);
        if (matcher.find()) {
            try {
                i = Integer.parseInt(matcher.group(1));
            } catch (NumberFormatException e) {
            }
        }
        return i;
    }

    protected static String getSectionPat(String name) {
        return ">>>\\s" + name.replaceAll(" ", "\\\\s") + "\\s<<<\\n((.|\\n)*?)(?:\\n\\n|$)";
    }

    protected static String getItemPat(String name) {
        return "\\n" + name.replaceAll(" ", "\\\\s") + ":\\s?(.*?)(?:\\n|$)";
    }

    protected static String getSectionStr(String log, String section) throws IOException {
        String sectionStr = parseStr(log, getSectionPat(section));
        if (TextUtils.isEmpty(sectionStr)) {
            return "";
        }
        return sectionStr.trim() + "\n";
    }

    public static JSONObject toJsonObj(File file) throws JSONException, IOException {
        FileInputStream fis = new FileInputStream(file);
        String log = CommonUtils.inputStreamToString(fis);
        fis.close();
        JSONObject jobj = new JSONObject();
        jobj.put("XcrashVer", parseStr(log, getItemPat("libxcrash")));
        jobj.put("Kernel", parseStr(log, getItemPat("Kernel")));
        jobj.put("ApiLevel", parseStr(log, getItemPat("Android API level")));
        jobj.put("StartTime", parseStr(log, getItemPat("Start time")));
        jobj.put("CrashTime", parseStr(log, getItemPat("Crash time")));
        jobj.put("Pid", parseInt(log, getItemPat("PID")));
        jobj.put("Pname", parseStr(log, getItemPat("Pname")));
        jobj.put("Tid", parseInt(log, getItemPat("TID")));
        jobj.put("Tname", parseStr(log, getItemPat("Tname")));
        jobj.put("Signal", parseStr(log, getItemPat("Signal")));
        jobj.put("SignalCode", parseStr(log, getItemPat("Code")));
        jobj.put("FaultAddr", parseStr(log, getItemPat("Fault addr")));
        jobj.put("CpuOnline", parseStr(log, getItemPat("CPU online")));
        jobj.put("CpuOffline", parseStr(log, getItemPat("CPU offline")));
        jobj.put("CpuLoadavg", parseStr(log, getItemPat("CPU loadavg")));
        jobj.put("TotalMemory", parseStr(log, getItemPat("Memory total")));
        jobj.put("UsedMemory", parseStr(log, getItemPat("Memory used")));
        jobj.put("Buddyinfo", getSectionStr(log, "Buddyinfo"));
        jobj.put("Registers", getSectionStr(log, "Registers"));
        jobj.put("BacktraceDebug", getSectionStr(log, "Backtrace debug"));
        jobj.put("Backtrace", getSectionStr(log, "Backtrace"));
        jobj.put("Stack", getSectionStr(log, "Stack"));
        jobj.put("MemoryAndCode", getSectionStr(log, "Memory and Code"));
        jobj.put("JavaBacktrace", getSectionStr(log, "JavaBacktrace"));
        jobj.put("Threads", getSectionStr(log, "Threads"));
        jobj.put("Traces", getSectionStr(log, "Traces"));
        jobj.put("Logcat", URLEncoder.encode(getSectionStr(log, "Logcat")));
        jobj.put("Events", URLEncoder.encode(getSectionStr(log, "Events")));
        jobj.put("QiyiLog", URLEncoder.encode(getSectionStr(log, "QiyiLog")));
        String otherInfo = getSectionStr(log, "OtherInfo");
        if (!TextUtils.isEmpty(otherInfo)) {
            JSONObject appData = new JSONObject();
            appData.put("Cube", URLEncoder.encode(parseStr(otherInfo, getItemPat("Cube"))));
            appData.put("Player", URLEncoder.encode(parseStr(otherInfo, getItemPat("Player"))));
            appData.put("Hcdn", URLEncoder.encode(parseStr(otherInfo, getItemPat("Hcdn"))));
            appData.put("VivoVersion", URLEncoder.encode(parseStr(otherInfo, getItemPat("VivoVersion"))));
            appData.put("LaunchMode", URLEncoder.encode(parseStr(otherInfo, getItemPat("LaunchMode"))));
            appData.put("HardwareInfo", getSectionStr(log, "HardwareInfo"));
            appData.put("PlayerLog", URLEncoder.encode(getSectionStr(log, "PlayerLog")));
            jobj.put("AppData", appData);
        }
        return jobj;
    }
}
