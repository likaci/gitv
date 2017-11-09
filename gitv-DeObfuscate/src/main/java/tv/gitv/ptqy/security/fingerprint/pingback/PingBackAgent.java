package tv.gitv.ptqy.security.fingerprint.pingback;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;
import tv.gitv.ptqy.security.fingerprint.LogMgr;
import tv.gitv.ptqy.security.fingerprint.Utils.HttpUtils;
import tv.gitv.ptqy.security.fingerprint.Utils.PackageUtils;
import tv.gitv.ptqy.security.fingerprint.action.LocalFingerPrintCacheHelper;
import tv.gitv.ptqy.security.fingerprint.callback.OnRequestBackListener;
import tv.gitv.ptqy.security.fingerprint.constants.Consts;
import tv.gitv.ptqy.security.fingerprint.sharedpreference.PrefUtils;

public class PingBackAgent {
    private static final String PINGBACK_FILE_NAME = ".dfppb";

    class C22431 implements OnRequestBackListener {
        private final /* synthetic */ Context val$context;

        C22431(Context context) {
            this.val$context = context;
        }

        public void onRequestSuccess() {
            PrefUtils.setUploadPingbackTime(this.val$context, System.currentTimeMillis());
            PingBackAgent.cleanErrorCache(this.val$context);
            LogMgr.m1900i(Consts.TAG, "[DFP] send pingback success");
        }

        public void onRequestFailure() {
            Log.e(Consts.TAG, "[DFP] send pingback failure");
        }
    }

    class C22442 extends Thread {
        private final /* synthetic */ Context val$context;

        C22442(Context context) {
            this.val$context = context;
        }

        public void run() {
            try {
                long lastPingbackTime = PrefUtils.getUploadPingbackTime(this.val$context);
                long currentTime = System.currentTimeMillis();
                if (currentTime < lastPingbackTime || currentTime - lastPingbackTime > 86400000) {
                    PingBackAgent.sendErrorStatistics(this.val$context);
                }
            } catch (Throwable e) {
                LogMgr.m1899i("sendErrorPingbackAsync :" + e.toString());
            }
        }
    }

    public static void saveCrash(Context context, String message) {
        saveErrorCache(context, 0, "crash", message);
    }

    public static void saveFetchFingerprintError(Context context, String message) {
        saveErrorCache(context, 0, Consts.ERROR_TYPE_SIGN_FAIL, message);
    }

    public static void saveMacAddressError(Context context, String message) {
        saveErrorCache(context, 1, Consts.ERROR_TYPE_MAC_FAIL, message);
    }

    public static void saveBluetoothAddressError(Context context, String message) {
        saveErrorCache(context, 1, Consts.ERROR_TYPE_BT_MAC_FAIL, message);
    }

    public static void saveEnvInfoError(Context context, String message) {
        saveErrorCache(context, 0, Consts.ERROR_TYPE_ENVINFO_FAIL, message);
    }

    public static void saveSaveDfp2StorageError(Context context, String message) {
        saveErrorCache(context, 1, Consts.ERROR_TYPE_SAVE_FAIL, message);
    }

    private static void saveErrorCache(Context context, int errorLevel, String errorType, String errorMsg) {
        JSONObject jobj = new JSONObject();
        try {
            jobj.put("sys", Consts.PLATFORM);
            jobj.put("sdk", Consts.SEG_DFP);
            jobj.put("s_v", "2.0");
            jobj.put("s_aid", PackageUtils.getPackageName(context));
            jobj.put("s_av", PackageUtils.getAppVersion(context));
            String dfp = "";
            try {
                dfp = new LocalFingerPrintCacheHelper(context).readFingerPrintLocalCache();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (!TextUtils.isEmpty(dfp)) {
                jobj.put("s_d", dfp);
            }
            jobj.put("s_e", errorLevel);
            jobj.put("s_et", errorType);
            jobj.put("s_ed", errorMsg);
            jobj.put("ua_model", Build.MODEL);
            jobj.put("sys_v", VERSION.SDK_INT);
            jobj.put("s_dns", getDns(context));
        } catch (Exception e2) {
            LogMgr.m1899i("saveErrorCache exception: " + e2.toString());
        }
        JSONArray jarr = new JSONArray();
        String stockLog = readErrorCache(context);
        try {
            if (TextUtils.isEmpty(stockLog)) {
                jarr = new JSONArray();
            } else {
                jarr = new JSONArray(stockLog);
            }
        } catch (Exception e22) {
            LogMgr.m1899i("saveErrorCache exception: " + e22.toString());
        }
        jarr.put(jobj);
        PrefUtils.setErrorCache(context, jarr.toString());
        if ("mounted".equals(Environment.getExternalStorageState())) {
            if (VERSION.SDK_INT >= 19) {
                for (File dir : context.getExternalFilesDirs(null)) {
                    if (dir != null) {
                        write2External(dir.getAbsolutePath(), jarr.toString());
                    }
                }
            }
            write2External(Environment.getExternalStorageDirectory().getAbsolutePath(), jarr.toString());
        }
    }

    private static String getDns(Context ctx) {
        Process localProcess;
        BufferedReader br;
        String dns;
        try {
            NetworkInfo activeNetInfo = ((ConnectivityManager) ctx.getApplicationContext().getSystemService("connectivity")).getActiveNetworkInfo();
            if (activeNetInfo == null || activeNetInfo.getType() != 1) {
                try {
                    localProcess = Runtime.getRuntime().exec("getprop net.dns1");
                    br = new BufferedReader(new InputStreamReader(localProcess.getInputStream()));
                    dns = br.readLine();
                    br.close();
                    localProcess.destroy();
                    return dns;
                } catch (Throwable th) {
                    return "";
                }
            }
            DhcpInfo dhcpInfo = ((WifiManager) ctx.getApplicationContext().getSystemService("wifi")).getDhcpInfo();
            String dns1 = dnsIntToIp(dhcpInfo.dns1);
            return new StringBuilder(String.valueOf(dns1)).append(File.separator).append(dnsIntToIp(dhcpInfo.dns2)).toString();
        } catch (Throwable th2) {
            return "";
        }
    }

    private static String dnsIntToIp(int dnsInt) {
        return (dnsInt & 255) + "." + ((dnsInt >> 8) & 255) + "." + ((dnsInt >> 16) & 255) + "." + ((dnsInt >> 24) & 255);
    }

    private static String readErrorCache(Context context) {
        String cache = PrefUtils.getErrorCache(context);
        if (!TextUtils.isEmpty(cache)) {
            return cache;
        }
        if ("mounted".equals(Environment.getExternalStorageState())) {
            String cacheExternal;
            if (VERSION.SDK_INT >= 19) {
                for (File dir : context.getExternalFilesDirs(null)) {
                    if (dir != null) {
                        cacheExternal = readFromExternal(dir.getAbsolutePath());
                        if (cacheExternal != null) {
                            return cacheExternal;
                        }
                    }
                }
            }
            cacheExternal = readFromExternal(Environment.getExternalStorageDirectory().getAbsolutePath());
            if (cacheExternal != null) {
                return cacheExternal;
            }
        }
        return null;
    }

    private static void write2External(String path, String errorLog) {
        new File(new StringBuilder(String.valueOf(path)).append(File.separator).append(PINGBACK_FILE_NAME).toString()).delete();
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(new File(new StringBuilder(String.valueOf(path)).append(File.separator).append(PINGBACK_FILE_NAME).toString())));
            writer.write(Base64.encodeToString(errorLog.getBytes(), 2));
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String readFromExternal(String path) {
        try {
            FileReader reader = new FileReader(new StringBuilder(String.valueOf(path)).append(File.separator).append(PINGBACK_FILE_NAME).toString());
            BufferedReader br = new BufferedReader(reader);
            StringBuilder sb = new StringBuilder("");
            while (true) {
                String str = br.readLine();
                if (str == null) {
                    String line = new String(Base64.decode(sb.toString(), 2));
                    br.close();
                    reader.close();
                    return line;
                }
                sb.append(str);
            }
        } catch (Exception e) {
            LogMgr.m1899i("ping back readFromExternal: " + e.toString());
            return null;
        }
    }

    private static void sendErrorStatistics(Context context) {
        String cache = readErrorCache(context);
        if (!TextUtils.isEmpty(cache)) {
            if (cache.length() > 500000) {
                cleanErrorCache(context);
                return;
            }
            JSONArray jarr = null;
            try {
                jarr = new JSONArray(cache);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (jarr != null && jarr.length() != 0) {
                Map<String, String> params = new HashMap();
                params.put("msg", jarr.toString());
                HttpUtils.doPostRequest(Consts.PINGBACK_ADDRESS, HttpUtils.params2payload(params), new C22431(context));
            }
        }
    }

    private static void cleanErrorCache(Context context) {
        PrefUtils.setErrorCache(context, "");
        if ("mounted".equals(Environment.getExternalStorageState())) {
            if (VERSION.SDK_INT >= 19) {
                for (File dir : context.getExternalFilesDirs(null)) {
                    if (dir != null) {
                        write2External(dir.getAbsolutePath(), "");
                    }
                }
            }
            write2External(Environment.getExternalStorageDirectory().getAbsolutePath(), "");
        }
    }

    public static void sendErrorPingbackAsync(Context context) {
        new C22442(context).start();
    }
}
