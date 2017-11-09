package com.gala.multiscreen.dmr.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.Enumeration;
import org.apache.http.conn.util.InetAddressUtils;

public class NetProfile {
    private static final String DEFAULT_NAME = "Guest";

    public static boolean isAvaliable(Context ctx) {
        NetworkInfo info = ((ConnectivityManager) ctx.getSystemService("connectivity")).getActiveNetworkInfo();
        if (info == null || !info.isConnected()) {
            MSLog.log("isAvaliable() false, extra=" + (info == null ? null : info.getExtraInfo()));
            return false;
        }
        MSLog.log("getType :" + info.getType());
        boolean isAvaliable = false;
        if (info.getType() == 9 || info.getType() == 1) {
            isAvaliable = true;
        }
        MSLog.log("isAvaliable() " + isAvaliable + ", extra=" + info.getExtraInfo());
        return isAvaliable;
    }

    public static String getIpIndentity(String ipAddress) {
        try {
            if (ipAddress.contains(".")) {
                ipAddress = ipAddress.substring(ipAddress.lastIndexOf(".") + 1);
            }
            return String.valueOf(Integer.parseInt(ipAddress));
        } catch (Exception e) {
            return DEFAULT_NAME;
        }
    }

    public static String getIp() {
        Context context = ContextProfile.getContext();
        if (context == null) {
            return DEFAULT_NAME;
        }
        String ip = getWirelessIpAddress(context);
        try {
            Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
            while (en.hasMoreElements()) {
                Enumeration<InetAddress> enumIpAddr = ((NetworkInterface) en.nextElement()).getInetAddresses();
                while (enumIpAddr.hasMoreElements()) {
                    InetAddress inetAddress = (InetAddress) enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && InetAddressUtils.isIPv4Address(inetAddress.getHostAddress())) {
                        ip = inetAddress.getHostAddress().toString();
                    }
                }
            }
            return ip;
        } catch (Exception e) {
            e.printStackTrace();
            return ip;
        }
    }

    public static String getWirelessIpAddress(Context context) {
        int ipAddr = ((WifiManager) context.getSystemService("wifi")).getConnectionInfo().getIpAddress();
        StringBuffer ipBuf = new StringBuffer();
        ipAddr >>>= 8;
        ipAddr >>>= 8;
        ipBuf.append(ipAddr & 255).append('.').append(ipAddr & 255).append('.').append(ipAddr & 255).append('.').append((ipAddr >>> 8) & 255);
        return ipBuf.toString();
    }

    @SuppressLint({"DefaultLocale"})
    public static String getWiredAddress(boolean useIPv4) {
        try {
            for (NetworkInterface intf : Collections.list(NetworkInterface.getNetworkInterfaces())) {
                for (InetAddress addr : Collections.list(intf.getInetAddresses())) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress();
                        boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                        if (useIPv4) {
                            if (isIPv4) {
                                return sAddr;
                            }
                        } else if (!isIPv4) {
                            int delim = sAddr.indexOf(37);
                            if (delim >= 0) {
                                return sAddr.substring(0, delim);
                            }
                            return sAddr;
                        }
                    }
                }
            }
        } catch (Exception e) {
        }
        return "";
    }
}
