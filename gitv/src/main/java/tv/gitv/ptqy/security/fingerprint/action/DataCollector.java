package tv.gitv.ptqy.security.fingerprint.action;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Environment;
import android.os.StatFs;
import android.os.SystemClock;
import android.provider.Settings.Secure;
import android.util.DisplayMetrics;
import com.gala.video.app.epg.ui.albumlist.utils.AlbumEnterFactory;
import com.push.pushservice.constants.DataConst;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TimeZone;
import java.util.UUID;
import org.cybergarage.soap.SOAP;
import org.json.JSONObject;
import tv.gitv.ptqy.security.fingerprint.LogMgr;
import tv.gitv.ptqy.security.fingerprint.Utils.PermissionUtil;
import tv.gitv.ptqy.security.fingerprint.Utils.Utils;
import tv.gitv.ptqy.security.fingerprint.Utils.WifiScanResultComparator;
import tv.gitv.ptqy.security.fingerprint.constants.Consts;
import tv.gitv.ptqy.security.fingerprint.pingback.PingBackAgent;

public class DataCollector {
    private Context context;
    private Map<String, String> dataMap = new HashMap();

    private boolean checkSuProperty(java.lang.String r10) {
        /* JADX: method processing error */
/*
Error: java.util.NoSuchElementException
	at java.util.HashMap$HashIterator.nextNode(HashMap.java:1431)
	at java.util.HashMap$KeyIterator.next(HashMap.java:1453)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.applyRemove(BlockFinallyExtract.java:535)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.extractFinally(BlockFinallyExtract.java:175)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.processExceptionHandler(BlockFinallyExtract.java:79)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.visit(BlockFinallyExtract.java:51)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
*/
        /*
        r9 = this;
        r5 = 0;
        r2 = 0;
        r6 = java.lang.Runtime.getRuntime();	 Catch:{ Exception -> 0x0049, all -> 0x0057 }
        r7 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0049, all -> 0x0057 }
        r8 = "ls -l ";	 Catch:{ Exception -> 0x0049, all -> 0x0057 }
        r7.<init>(r8);	 Catch:{ Exception -> 0x0049, all -> 0x0057 }
        r7 = r7.append(r10);	 Catch:{ Exception -> 0x0049, all -> 0x0057 }
        r7 = r7.toString();	 Catch:{ Exception -> 0x0049, all -> 0x0057 }
        r2 = r6.exec(r7);	 Catch:{ Exception -> 0x0049, all -> 0x0057 }
        r3 = new java.io.BufferedReader;	 Catch:{ Exception -> 0x0049, all -> 0x0057 }
        r6 = new java.io.InputStreamReader;	 Catch:{ Exception -> 0x0049, all -> 0x0057 }
        r7 = r2.getInputStream();	 Catch:{ Exception -> 0x0049, all -> 0x0057 }
        r6.<init>(r7);	 Catch:{ Exception -> 0x0049, all -> 0x0057 }
        r3.<init>(r6);	 Catch:{ Exception -> 0x0049, all -> 0x0057 }
        r4 = r3.readLine();	 Catch:{ Exception -> 0x0049, all -> 0x0057 }
        if (r4 == 0) goto L_0x005e;	 Catch:{ Exception -> 0x0049, all -> 0x0057 }
    L_0x002e:
        r6 = r4.length();	 Catch:{ Exception -> 0x0049, all -> 0x0057 }
        r7 = 4;	 Catch:{ Exception -> 0x0049, all -> 0x0057 }
        if (r6 < r7) goto L_0x005e;	 Catch:{ Exception -> 0x0049, all -> 0x0057 }
    L_0x0035:
        r6 = 3;	 Catch:{ Exception -> 0x0049, all -> 0x0057 }
        r0 = r4.charAt(r6);	 Catch:{ Exception -> 0x0049, all -> 0x0057 }
        r6 = 120; // 0x78 float:1.68E-43 double:5.93E-322;
        if (r0 == r6) goto L_0x0042;
    L_0x003e:
        r6 = 115; // 0x73 float:1.61E-43 double:5.7E-322;
        if (r0 != r6) goto L_0x005e;
    L_0x0042:
        if (r2 == 0) goto L_0x0047;
    L_0x0044:
        r2.destroy();
    L_0x0047:
        r5 = 1;
    L_0x0048:
        return r5;
    L_0x0049:
        r1 = move-exception;
        r6 = r1.toString();	 Catch:{ Exception -> 0x0049, all -> 0x0057 }
        tv.gitv.ptqy.security.fingerprint.LogMgr.i(r6);	 Catch:{ Exception -> 0x0049, all -> 0x0057 }
        if (r2 == 0) goto L_0x0048;
    L_0x0053:
        r2.destroy();
        goto L_0x0048;
    L_0x0057:
        r5 = move-exception;
        if (r2 == 0) goto L_0x005d;
    L_0x005a:
        r2.destroy();
    L_0x005d:
        throw r5;
    L_0x005e:
        if (r2 == 0) goto L_0x0048;
    L_0x0060:
        r2.destroy();
        goto L_0x0048;
        */
        throw new UnsupportedOperationException("Method not decompiled: tv.gitv.ptqy.security.fingerprint.action.DataCollector.checkSuProperty(java.lang.String):boolean");
    }

    public DataCollector(Context context) {
        this.context = context;
    }

    public Map<String, String> collect(boolean internal) {
        this.dataMap.put("ul", getBoard());
        this.dataMap.put("ds", getProduct());
        this.dataMap.put("tz", getHardware());
        this.dataMap.put("fv", getBrand());
        this.dataMap.put("py", getCpuAbi());
        this.dataMap.put("ju", getDisplayRom());
        this.dataMap.put("wh", getManufacturer());
        this.dataMap.put("mg", getCpuInfoDigest());
        String wlanMacAddress = getWlanMacAddress();
        if (wlanMacAddress == null || wlanMacAddress.isEmpty()) {
            this.dataMap.put("fd", "");
            this.dataMap.put("mo", "");
        } else {
            this.dataMap.put("fd", Utils.md5(wlanMacAddress));
            try {
                this.dataMap.put("mo", Utils.xorEncryptDefault(wlanMacAddress.replace(SOAP.DELIM, "").substring(0, 6)));
            } catch (Exception e) {
                LogMgr.i(e.toString());
                this.dataMap.put("mo", "");
            }
        }
        String ethernetMacAddress = getEthernetMacAddress();
        if (ethernetMacAddress == null || ethernetMacAddress.isEmpty()) {
            this.dataMap.put("em", "");
            this.dataMap.put("te", "");
        } else {
            this.dataMap.put("em", Utils.md5(ethernetMacAddress));
            try {
                this.dataMap.put("te", Utils.xorEncryptDefault(ethernetMacAddress.replace(SOAP.DELIM, "").substring(0, 6)));
            } catch (Exception e2) {
                LogMgr.i(e2.toString());
                this.dataMap.put("te", "");
            }
        }
        this.dataMap.put("kl", getMessageID());
        this.dataMap.put("kd", getAndroidID());
        this.dataMap.put("wa", new StringBuilder(String.valueOf(getTimeZone())).toString());
        this.dataMap.put("qf", getSDKVersion());
        this.dataMap.put("ae", getPlatform());
        if (internal) {
            this.dataMap.put("hr", getWifiList());
            this.dataMap.put("wl", getModel());
            this.dataMap.put("wd", getDevice());
            this.dataMap.put("yt", getResolution());
            this.dataMap.put("mw", getVersion());
            this.dataMap.put("fu", getPackageName());
            this.dataMap.put("go", getApplicationVersion());
            this.dataMap.put("ot", new StringBuilder(String.valueOf(getTotalMemory())).toString());
            this.dataMap.put("ps", new StringBuilder(String.valueOf(getTotalSystem())).toString());
            this.dataMap.put("wj", new StringBuilder(String.valueOf(getTotalSDCard())).toString());
            this.dataMap.put("ks", new StringBuilder(String.valueOf(getFreeMemory())).toString());
            this.dataMap.put("se", new StringBuilder(String.valueOf(getFreeSystem())).toString());
            this.dataMap.put("sp", new StringBuilder(String.valueOf(getFreeSDCard())).toString());
            this.dataMap.put("yy", new StringBuilder(String.valueOf(getStartTime())).toString());
            this.dataMap.put("kp", new StringBuilder(String.valueOf(getActiveTime())).toString());
            this.dataMap.put("eh", new StringBuilder(String.valueOf(getFreeSystem())).toString());
            Map map = this.dataMap;
            String str = "ce";
            Object obj = (hasSu() || isSuperuserExist()) ? "true" : "false";
            map.put(str, obj);
            this.dataMap.put("mr", new StringBuilder(String.valueOf(getFreeSystem())).toString());
            this.dataMap.put("qd", getWifi());
            this.dataMap.put("kv", getNetworkType());
            this.dataMap.put("lw", getCellular());
            this.dataMap.put("dn", getDns());
            this.dataMap.put("gp", getGPS());
            this.dataMap.put("ed", String.valueOf(getEmuInfo()));
        }
        return this.dataMap;
    }

    private String intToIp(int paramInt) {
        return (paramInt & 255) + "." + ((paramInt >> 8) & 255) + "." + ((paramInt >> 16) & 255) + "." + ((paramInt >> 24) & 255);
    }

    public String getDns() {
        try {
            DhcpInfo dhcpInfo = ((WifiManager) this.context.getSystemService("wifi")).getDhcpInfo();
            return Arrays.toString(new String[]{intToIp(dhcpInfo.dns1), intToIp(dhcpInfo.dns2)});
        } catch (Exception e) {
            return "";
        }
    }

    public String getCpuInfoDigest() {
        StringBuilder cpu_info = new StringBuilder();
        try {
            LineNumberReader lineNumberReader = new LineNumberReader(new InputStreamReader(Runtime.getRuntime().exec("cat /proc/cpuinfo").getInputStream()));
            while (true) {
                String line = lineNumberReader.readLine();
                if (line == null) {
                    return Utils.md5(cpu_info.toString().trim());
                }
                cpu_info.append(line.trim());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return cpu_info.toString();
        }
    }

    public String getCpuAbi() {
        if (VERSION.SDK_INT >= 21) {
            return Arrays.toString(Build.SUPPORTED_ABIS);
        }
        return Arrays.toString(new String[]{Build.CPU_ABI, Build.CPU_ABI2});
    }

    public String getWlanMacAddress() {
        if (VERSION.SDK_INT >= 22) {
            try {
                String line = new LineNumberReader(new InputStreamReader(Runtime.getRuntime().exec("cat /sys/class/net/wlan0/address").getInputStream())).readLine();
                if (line != null && line.contains(SOAP.DELIM) && line.length() == 17) {
                    return line;
                }
            } catch (Exception e) {
                e.printStackTrace();
                LogMgr.e(Consts.TAG, e.getMessage());
                PingBackAgent.saveMacAddressError(this.context, e.getMessage());
            }
        } else {
            try {
                return ((WifiManager) this.context.getSystemService("wifi")).getConnectionInfo().getMacAddress();
            } catch (Exception e2) {
                e2.printStackTrace();
                LogMgr.e(Consts.TAG, e2.getMessage());
                PingBackAgent.saveMacAddressError(this.context, e2.getMessage());
            }
        }
        return "";
    }

    public String getEthernetMacAddress() {
        try {
            String line = new LineNumberReader(new InputStreamReader(Runtime.getRuntime().exec("cat /sys/class/net/eth0/address").getInputStream())).readLine();
            if (line != null && line.contains(SOAP.DELIM) && line.length() == 17) {
                return line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public String getWifiList() {
        if (this.context == null) {
            return "";
        }
        try {
            WifiManager wifiManager = (WifiManager) this.context.getSystemService("wifi");
            wifiManager.startScan();
            List<ScanResult> wifi_list = wifiManager.getScanResults();
            Collections.sort(wifi_list, new WifiScanResultComparator());
            StringBuilder stringBuilder = new StringBuilder();
            int i = 0;
            while (i < 2 && i < wifi_list.size()) {
                stringBuilder.append(new StringBuilder(String.valueOf(((ScanResult) wifi_list.get(i)).SSID)).append(",").append(((ScanResult) wifi_list.get(i)).BSSID).append("#").toString());
                i++;
            }
            return stringBuilder.substring(0, stringBuilder.length() - 1).toString();
        } catch (Throwable e) {
            LogMgr.i("getWifiList failed: " + e.toString());
            return "";
        }
    }

    public String getBoard() {
        return Build.BOARD;
    }

    public String getBrand() {
        return Build.BRAND;
    }

    public String getHardware() {
        return Build.HARDWARE;
    }

    public String getManufacturer() {
        return Build.MANUFACTURER;
    }

    public String getDisplayRom() {
        return Build.DISPLAY;
    }

    public String getProduct() {
        return Build.PRODUCT;
    }

    public String getModel() {
        return Build.MODEL;
    }

    public String getDevice() {
        return Build.DEVICE;
    }

    public String getResolution() {
        if (this.context == null) {
            return "";
        }
        DisplayMetrics metrics = this.context.getResources().getDisplayMetrics();
        return "[" + metrics.density + "," + Math.max(metrics.widthPixels, metrics.heightPixels) + "," + Math.min(metrics.heightPixels, metrics.widthPixels) + "," + metrics.xdpi + "," + metrics.ydpi + AlbumEnterFactory.SIGN_STR;
    }

    public String getVersion() {
        return VERSION.RELEASE;
    }

    public String getPackageName() {
        if (this.context == null) {
            return "";
        }
        return this.context.getPackageName();
    }

    public String getApplicationVersion() {
        if (this.context == null) {
            return "";
        }
        try {
            PackageInfo info = this.context.getPackageManager().getPackageInfo(getPackageName(), 128);
            if (info.versionName != null) {
                return info.versionName;
            }
        } catch (Exception e) {
            LogMgr.i(e.toString());
        }
        return "";
    }

    public long getTotalMemory() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader("/proc/meminfo"), 8192);
            String[] key_value = bufferedReader.readLine().split("\\s+");
            bufferedReader.close();
            return (long) Long.valueOf(key_value[1]).intValue();
        } catch (Exception e) {
            LogMgr.i(e.toString());
            return -1;
        }
    }

    public long getTotalSystem() {
        try {
            StatFs statFs = new StatFs(Environment.getRootDirectory().getPath());
            return ((long) (statFs.getBlockSize() * statFs.getBlockCount())) / 1024;
        } catch (Exception e) {
            LogMgr.i(e.toString());
            return -1;
        }
    }

    public long getTotalSDCard() {
        try {
            if ("mounted".equals(Environment.getExternalStorageState()) && PermissionUtil.hasSdcardWritePerm(this.context)) {
                File external = Environment.getExternalStorageDirectory();
                if (external != null) {
                    StatFs statFs = new StatFs(external.getPath());
                    return (long) (statFs.getBlockSize() * statFs.getBlockCount());
                }
            }
        } catch (Exception e) {
            LogMgr.i(e.toString());
        }
        return -1;
    }

    public long getFreeMemory() {
        long j = -1;
        if (this.context != null) {
            try {
                ActivityManager manager = (ActivityManager) this.context.getSystemService("activity");
                MemoryInfo memoryInfo = new MemoryInfo();
                manager.getMemoryInfo(memoryInfo);
                j = memoryInfo.availMem;
            } catch (Exception e) {
                LogMgr.i(e.toString());
            }
        }
        return j;
    }

    public long getFreeSystem() {
        try {
            StatFs statFs = new StatFs(Environment.getRootDirectory().getPath());
            return ((long) (statFs.getBlockSize() * statFs.getAvailableBlocks())) / 1024;
        } catch (Exception e) {
            LogMgr.i(e.toString());
            return -1;
        }
    }

    public long getFreeSDCard() {
        try {
            if ("mounted".equals(Environment.getExternalStorageState()) && PermissionUtil.hasSdcardWritePerm(this.context)) {
                File external = Environment.getExternalStorageDirectory();
                if (external != null) {
                    StatFs statFs = new StatFs(external.getPath());
                    return (long) (statFs.getBlockSize() * statFs.getAvailableBlocks());
                }
            }
        } catch (Exception e) {
            LogMgr.i(e.toString());
        }
        return -1;
    }

    public String getTimeZone() {
        try {
            TimeZone timeZone = TimeZone.getDefault();
            return "[" + timeZone.getDisplayName(false, 0) + "," + timeZone.getID() + AlbumEnterFactory.SIGN_STR;
        } catch (Exception e) {
            LogMgr.i(e.toString());
            return "";
        }
    }

    public long getStartTime() {
        return System.currentTimeMillis() - SystemClock.elapsedRealtime();
    }

    public long getActiveTime() {
        return SystemClock.uptimeMillis();
    }

    public String getSDKVersion() {
        return "2.0";
    }

    public String getPlatform() {
        return Consts.PLATFORM;
    }

    public String getWifi() {
        if (this.context == null) {
            return "";
        }
        try {
            WifiManager wifiManager = (WifiManager) this.context.getSystemService("wifi");
            if (wifiManager == null) {
                return "";
            }
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            return "[" + wifiInfo.getBSSID() + "," + wifiInfo.getSSID() + AlbumEnterFactory.SIGN_STR;
        } catch (Exception e) {
            LogMgr.i(e.toString());
            return "";
        }
    }

    public String getNetworkType() {
        if (this.context == null) {
            return "";
        }
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) this.context.getSystemService("connectivity");
            if (connectivityManager == null) {
                return "";
            }
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                if (networkInfo.getType() == 1) {
                    return "WiFi";
                }
                return "Mobile Network";
            }
            return "";
        } catch (Exception e) {
            LogMgr.i(e.toString());
        }
    }

    public String getCellular() {
        try {
            Enumeration interface_enumeration = NetworkInterface.getNetworkInterfaces();
            while (interface_enumeration.hasMoreElements()) {
                Enumeration address_enumeration = ((NetworkInterface) interface_enumeration.nextElement()).getInetAddresses();
                while (address_enumeration.hasMoreElements()) {
                    InetAddress inetAddress = (InetAddress) address_enumeration.nextElement();
                    if (!inetAddress.isLoopbackAddress() && (inetAddress instanceof Inet4Address)) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (Exception e) {
            LogMgr.i(e.toString());
        }
        return "";
    }

    public String getJsonString() {
        JSONObject jsonObject = new JSONObject();
        for (Entry<String, String> entry : this.dataMap.entrySet()) {
            try {
                jsonObject.put((String) entry.getKey(), entry.getValue());
            } catch (Exception e) {
                LogMgr.i(e.toString());
            }
        }
        LogMgr.i("envInfo: " + jsonObject.toString());
        return jsonObject.toString();
    }

    private boolean hasSu() {
        String bin_su = "/system/bin/su";
        String xbin_su = "/system/xbin/su";
        if ((new File(bin_su).exists() && checkSuProperty(bin_su)) || (new File(xbin_su).exists() && checkSuProperty(xbin_su))) {
            return true;
        }
        return false;
    }

    private boolean isSuperuserExist() {
        return new File("/system/app/Superuser.apk").exists();
    }

    public String getMessageID() {
        try {
            LocalFingerPrintCacheHelper helper = new LocalFingerPrintCacheHelper(this.context);
            String msg_id = helper.readSharedPreference("msg", DataConst.EXTRA_PUSH_MESSAGE_ID);
            if (msg_id != null) {
                helper.writeExternalFile(".msg_id", msg_id);
                return msg_id;
            }
            msg_id = helper.readExternalFile(".msg_id");
            if (msg_id != null) {
                helper.writeSharedPreference("msg", DataConst.EXTRA_PUSH_MESSAGE_ID, msg_id);
                return msg_id;
            }
            msg_id = UUID.randomUUID().toString();
            helper.writeSharedPreference("msg", DataConst.EXTRA_PUSH_MESSAGE_ID, msg_id);
            helper.writeExternalFile(".msg_id", msg_id);
            return msg_id;
        } catch (Exception e) {
            LogMgr.i(e.toString());
            return "";
        }
    }

    public String getAndroidID() {
        try {
            return Secure.getString(this.context.getContentResolver(), "android_id");
        } catch (Exception e) {
            LogMgr.i(e.toString());
            return "";
        }
    }

    public String getGPS() {
        try {
            LocationManager lm = (LocationManager) this.context.getSystemService("location");
            Location bestLocation = null;
            for (String provider : lm.getProviders(true)) {
                Location l = lm.getLastKnownLocation(provider);
                if (l != null && (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy())) {
                    bestLocation = l;
                }
            }
            if (bestLocation != null) {
                return bestLocation.getLatitude() + "," + bestLocation.getLongitude();
            }
        } catch (Exception e) {
            LogMgr.i("getGPS failed: " + e.toString());
        }
        return "";
    }

    public int getEmuInfo() {
        try {
            int intValue = Utils.getEmuInfo(this.context);
            LogMgr.i("vm intValue: " + intValue);
            return intValue;
        } catch (Exception e) {
            LogMgr.i(e.toString());
            return -1;
        }
    }
}
