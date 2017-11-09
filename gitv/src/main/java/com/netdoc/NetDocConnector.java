package com.netdoc;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Proxy;
import android.net.ProxyInfo;
import android.os.Build.VERSION;
import android.util.Log;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import org.cybergarage.soap.SOAP;

@SuppressLint({"NewApi"})
public class NetDocConnector {
    private static final String LOG_TAG = "JniNetDoctor";
    private static final Object SYNCLOCK = new Object();
    private Context ctx = null;
    private boolean isLoadSuccess = false;

    public native void JniCheckDefPlay();

    public native void JniCheckLivePlay(int i, LiveTaskInfo liveTaskInfo, String str);

    public native void JniCheckPlay(int i, TaskInfo taskInfo, String str);

    public native int JniCheckSmurfs(int i, TaskInfo taskInfo, String str, SmurfsListenerInterface smurfsListenerInterface);

    public native void JniInitNetDoctor(String str, int i, String str2);

    public native int JniIsHijacked(String str);

    public native void JniRecvPushMsg(String str);

    public native int JniReqHTTPDNS(String str, HTTPDNSListenerInterface hTTPDNSListenerInterface, boolean z);

    public native void JniSendLogInfo(String str);

    public native void JniSetListener(NetDocListenerInterface netDocListenerInterface);

    public native void JniStopCheckSmurfs(int i);

    public native void JniStopDefPlay();

    public native void JniStopLivePlay(String str);

    public native void JniStopPlay(String str);

    public native void JniStopReqHTTPDNS(int i);

    public native void JniStopTestNetwork(int i);

    public native int JniTestNetwork(OtherTestListenerInterface otherTestListenerInterface);

    public native void JniUninitNetDoctor();

    public NetDocConnector() {
        try {
            System.loadLibrary("netdoc");
            this.isLoadSuccess = true;
        } catch (Throwable th) {
            Log.e(LOG_TAG, "System.loadLibrary: load netdoc failed!");
            th.printStackTrace();
            this.isLoadSuccess = false;
        }
    }

    public NetDocConnector(String str) {
        try {
            System.load(str);
            this.isLoadSuccess = true;
        } catch (Throwable th) {
            Log.e(LOG_TAG, "System.load: load netdoc failed!");
            th.printStackTrace();
            this.isLoadSuccess = false;
        }
    }

    public void initNetDoctor(String str, int i, String str2) {
        if (this.isLoadSuccess) {
            if (str2 == null) {
                str2 = "";
            }
            if (str == null) {
                str = "";
            }
            try {
                JniInitNetDoctor(str, i, str2);
            } catch (UnsatisfiedLinkError e) {
                Log.e(LOG_TAG, "initNetDoctor call failed!");
            }
        }
    }

    public void uninitNetDoctor() {
        if (this.isLoadSuccess) {
            try {
                JniUninitNetDoctor();
            } catch (UnsatisfiedLinkError e) {
                Log.e(LOG_TAG, "uninitNetDoctor call failed!");
            }
            this.ctx = null;
        }
    }

    public void checkPlay(int i, TaskInfo taskInfo, String str) {
        if (this.isLoadSuccess) {
            if (str == null) {
                str = "";
            }
            if (taskInfo.vid == null) {
                taskInfo.vid = "";
            }
            if (taskInfo.tvid == null) {
                taskInfo.tvid = "";
            }
            if (taskInfo.bid == null) {
                taskInfo.bid = "";
            }
            if (taskInfo.aid == null) {
                taskInfo.aid = "";
            }
            if (taskInfo.cid == null) {
                taskInfo.cid = "";
            }
            if (taskInfo.cookie == null) {
                taskInfo.cookie = "";
            }
            if (taskInfo.uid == null) {
                taskInfo.uid = "";
            }
            if (taskInfo.platformid == null) {
                taskInfo.platformid = "";
            }
            if (taskInfo.deviceid == null) {
                taskInfo.deviceid = "";
            }
            if (taskInfo.k_ver == null) {
                taskInfo.k_ver = "";
            }
            if (taskInfo.k_from == null) {
                taskInfo.k_from = "";
            }
            if (taskInfo.k_ver_puma == null) {
                taskInfo.k_ver_puma = "";
            }
            if (taskInfo.qyid == null) {
                taskInfo.qyid = "";
            }
            if (taskInfo.sgti == null) {
                taskInfo.sgti = "";
            }
            try {
                JniCheckPlay(i, taskInfo, str);
            } catch (UnsatisfiedLinkError e) {
                Log.e(LOG_TAG, "checkPlay call failed!");
            }
        }
    }

    public void checkDefPlay() {
        if (this.isLoadSuccess) {
            try {
                JniCheckDefPlay();
            } catch (UnsatisfiedLinkError e) {
                Log.e(LOG_TAG, "checkDefPlay call failed!");
            }
        }
    }

    public void checkLivePlay(int i, LiveTaskInfo liveTaskInfo, String str) {
        if (this.isLoadSuccess) {
            if (str == null) {
                str = "";
            }
            if (liveTaskInfo.channelid == null) {
                liveTaskInfo.channelid = "";
            }
            if (liveTaskInfo.programid == null) {
                liveTaskInfo.programid = "";
            }
            if (liveTaskInfo.bid == null) {
                liveTaskInfo.bid = "";
            }
            if (liveTaskInfo.uid == null) {
                liveTaskInfo.uid = "";
            }
            if (liveTaskInfo.platformid == null) {
                liveTaskInfo.platformid = "";
            }
            if (liveTaskInfo.uuid == null) {
                liveTaskInfo.uuid = "";
            }
            if (liveTaskInfo.key == null) {
                liveTaskInfo.key = "";
            }
            if (liveTaskInfo.cookie == null) {
                liveTaskInfo.cookie = "";
            }
            if (liveTaskInfo.k_ver == null) {
                liveTaskInfo.k_ver = "";
            }
            if (liveTaskInfo.k_from == null) {
                liveTaskInfo.k_from = "";
            }
            try {
                JniCheckLivePlay(i, liveTaskInfo, str);
            } catch (UnsatisfiedLinkError e) {
                Log.e(LOG_TAG, "checkLivePlay call failed!");
            }
        }
    }

    public void stopPlay(String str) {
        if (this.isLoadSuccess) {
            if (str == null) {
                str = "";
            }
            try {
                JniStopPlay(str);
            } catch (UnsatisfiedLinkError e) {
                Log.e(LOG_TAG, "stopPlay call failed!");
            }
        }
    }

    public void stopDefPlay() {
        if (this.isLoadSuccess) {
            try {
                JniStopDefPlay();
            } catch (UnsatisfiedLinkError e) {
                Log.e(LOG_TAG, "stopDefPlay call failed!");
            }
        }
    }

    public void stopLivePlay(String str) {
        if (this.isLoadSuccess) {
            if (str == null) {
                str = "";
            }
            try {
                JniStopLivePlay(str);
            } catch (UnsatisfiedLinkError e) {
                Log.e(LOG_TAG, "stopLivePlay call failed!");
            }
        }
    }

    public void sendLogInfo(String str) {
        if (this.isLoadSuccess) {
            if (str == null) {
                str = "";
            }
            try {
                JniSendLogInfo(str);
            } catch (UnsatisfiedLinkError e) {
                Log.e(LOG_TAG, "sendLogInfo call failed!");
            }
        }
    }

    public void setListener(NetDocListenerInterface netDocListenerInterface) {
        if (this.isLoadSuccess) {
            try {
                JniSetListener(netDocListenerInterface);
            } catch (UnsatisfiedLinkError e) {
                Log.e(LOG_TAG, "setListener call failed!");
            }
        }
    }

    public int reqHTTPDNS(String str, HTTPDNSListenerInterface hTTPDNSListenerInterface, boolean z) {
        int i = 0;
        if (this.isLoadSuccess) {
            if (str == null) {
                str = "";
            }
            try {
                i = JniReqHTTPDNS(str, hTTPDNSListenerInterface, z);
            } catch (UnsatisfiedLinkError e) {
                Log.e(LOG_TAG, "reqHTTPDNS call failed!");
            }
        }
        return i;
    }

    public void stopReqHTTPDNS(int i) {
        if (this.isLoadSuccess) {
            try {
                JniStopReqHTTPDNS(i);
            } catch (UnsatisfiedLinkError e) {
                Log.e(LOG_TAG, "stopReqHTTPDNS call failed!");
            }
        }
    }

    public int testNetwork(OtherTestListenerInterface otherTestListenerInterface) {
        int i = 0;
        if (this.isLoadSuccess) {
            try {
                i = JniTestNetwork(otherTestListenerInterface);
            } catch (UnsatisfiedLinkError e) {
                Log.e(LOG_TAG, "testNetwork call failed!");
            }
        }
        return i;
    }

    public void stopTestNetwork(int i) {
        if (this.isLoadSuccess) {
            try {
                JniStopTestNetwork(i);
            } catch (UnsatisfiedLinkError e) {
                Log.e(LOG_TAG, "stopTestNetwork call failed!");
            }
        }
    }

    public void recvPushMsg(String str) {
        if (!this.isLoadSuccess) {
            return;
        }
        if (str == null || str.isEmpty()) {
            Log.e(LOG_TAG, "recv push msg null or empty!");
            return;
        }
        try {
            JniRecvPushMsg(str);
        } catch (UnsatisfiedLinkError e) {
            Log.e(LOG_TAG, "recvPushMsg call failed!");
            e.printStackTrace();
        }
    }

    public int checkSmurfs(int i, TaskInfo taskInfo, String str, SmurfsListenerInterface smurfsListenerInterface) {
        int i2 = 0;
        if (this.isLoadSuccess) {
            if (str == null) {
                str = "";
            }
            if (taskInfo.vid == null) {
                taskInfo.vid = "";
            }
            if (taskInfo.tvid == null) {
                taskInfo.tvid = "";
            }
            if (taskInfo.bid == null) {
                taskInfo.bid = "";
            }
            if (taskInfo.aid == null) {
                taskInfo.aid = "";
            }
            if (taskInfo.cid == null) {
                taskInfo.cid = "";
            }
            if (taskInfo.cookie == null) {
                taskInfo.cookie = "";
            }
            if (taskInfo.uid == null) {
                taskInfo.uid = "";
            }
            if (taskInfo.platformid == null) {
                taskInfo.platformid = "";
            }
            if (taskInfo.deviceid == null) {
                taskInfo.deviceid = "";
            }
            if (taskInfo.k_ver == null) {
                taskInfo.k_ver = "";
            }
            if (taskInfo.k_from == null) {
                taskInfo.k_from = "";
            }
            if (taskInfo.k_ver_puma == null) {
                taskInfo.k_ver_puma = "";
            }
            if (taskInfo.qyid == null) {
                taskInfo.qyid = "";
            }
            if (taskInfo.sgti == null) {
                taskInfo.sgti = "";
            }
            try {
                i2 = JniCheckSmurfs(i, taskInfo, str, smurfsListenerInterface);
            } catch (UnsatisfiedLinkError e) {
                Log.e(LOG_TAG, "checkSmurfs call failed!");
            }
        }
        return i2;
    }

    public void stopCheckSmurfs(int i) {
        if (this.isLoadSuccess) {
            try {
                JniStopCheckSmurfs(i);
            } catch (UnsatisfiedLinkError e) {
                Log.e(LOG_TAG, "stopCheckSmurfs call failed!");
            }
        }
    }

    public int isHijacked(String str) {
        int i = -1;
        if (this.isLoadSuccess) {
            if (str == null || str.isEmpty()) {
                Log.e(LOG_TAG, "domain_name null or empty!");
            } else {
                try {
                    i = JniIsHijacked(str);
                } catch (UnsatisfiedLinkError e) {
                    Log.e(LOG_TAG, "IsHijacked called failed!");
                    e.printStackTrace();
                }
            }
        }
        return i;
    }

    public void setContext(Context context) {
        if (this.isLoadSuccess && this.ctx == null) {
            synchronized (SYNCLOCK) {
                if (this.ctx == null) {
                    this.ctx = context;
                }
            }
        }
    }

    public String getNetworkType() {
        int i;
        if (this.ctx != null) {
            NetworkInfo activeNetworkInfo = ((ConnectivityManager) this.ctx.getSystemService("connectivity")).getActiveNetworkInfo();
            if (activeNetworkInfo != null && activeNetworkInfo.isAvailable()) {
                switch (activeNetworkInfo.getType()) {
                    case 0:
                        switch (activeNetworkInfo.getSubtype()) {
                            case 1:
                            case 2:
                            case 4:
                            case 7:
                                i = 2;
                                break;
                            case 3:
                            case 6:
                            case 8:
                            case 9:
                            case 10:
                            case 12:
                            case 15:
                                i = 3;
                                break;
                            case 13:
                                i = 4;
                                break;
                            default:
                                i = 5;
                                break;
                        }
                    case 1:
                        i = 1;
                        break;
                    default:
                        i = 5;
                        break;
                }
                return Integer.toString(i);
            }
        }
        i = 0;
        return Integer.toString(i);
    }

    public String isVpnConnected() {
        String str = "0";
        try {
            Enumeration networkInterfaces = NetworkInterface.getNetworkInterfaces();
            if (networkInterfaces != null) {
                Iterator it = Collections.list(networkInterfaces).iterator();
                while (it.hasNext()) {
                    NetworkInterface networkInterface = (NetworkInterface) it.next();
                    if (networkInterface.isUp() && networkInterface.getInterfaceAddresses().size() != 0) {
                        String str2;
                        if (networkInterface.getName().contains("tun") || networkInterface.getName().contains("ppp")) {
                            str2 = "1";
                        } else {
                            str2 = str;
                        }
                        str = str2;
                    }
                }
            }
        } catch (SocketException e) {
            Log.e(LOG_TAG, e.getMessage());
        }
        return str;
    }

    @SuppressLint({"NewApi"})
    public String getProxyInfo() {
        String str = "";
        if (this.ctx != null) {
            try {
                String str2;
                int i;
                String str3;
                String property;
                ConnectivityManager connectivityManager = (ConnectivityManager) this.ctx.getSystemService("connectivity");
                NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                if (activeNetworkInfo == null || !activeNetworkInfo.isConnected()) {
                    str2 = null;
                    i = -1;
                    str3 = null;
                } else {
                    int parseInt;
                    if (VERSION.SDK_INT >= 11) {
                        property = System.getProperty("http.proxyHost");
                        str3 = System.getProperty("http.proxyPort");
                        if (str3 == null) {
                            str3 = "-1";
                        }
                        parseInt = Integer.parseInt(str3);
                    } else {
                        Log.i(LOG_TAG, "API 11 earlier");
                        property = Proxy.getHost(this.ctx);
                        parseInt = Proxy.getPort(this.ctx);
                    }
                    if (VERSION.SDK_INT >= 23) {
                        Log.i(LOG_TAG, "API 23 later");
                        ProxyInfo defaultProxy = connectivityManager.getDefaultProxy();
                        if (defaultProxy != null) {
                            str2 = defaultProxy.getPacFileUrl().toString();
                            i = parseInt;
                            str3 = property;
                        }
                    }
                    str2 = null;
                    i = parseInt;
                    str3 = property;
                }
                property = "";
                String str4 = "";
                if (str2 != null && str2.length() != 0) {
                    property = "5";
                } else if (i > 0) {
                    property = "3";
                }
                if (i > 0) {
                    str4 = (str3 != null ? str3 : "") + SOAP.DELIM + i;
                }
                if (str2 == null) {
                    str2 = "";
                }
                return "{\"ptype\":\"" + property + "\",\"psvr\":\"" + str4 + "\",\"purl\":\"" + str2 + "\"}";
            } catch (Exception e) {
                Log.e(LOG_TAG, "getProxyInfo failed!");
            }
        }
        return str;
    }
}
