package org.cybergarage.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import org.apache.http.conn.util.InetAddressUtils;

public class InternetInfo {
    private static final String DEFAULT_NAME = "unknow";

    public static boolean isEnable(Context ctx) {
        NetworkInfo info = ((ConnectivityManager) ctx.getSystemService("connectivity")).getActiveNetworkInfo();
        if (info == null || !info.isConnected()) {
            return false;
        }
        if (info.getTypeName().toString().equalsIgnoreCase("ethernet") || info.getTypeName().toString().equalsIgnoreCase("wifi")) {
            return true;
        }
        return false;
    }

    public static String getIpIndentity() {
        try {
            String ipAddress = getIp();
            if (ipAddress.contains(".")) {
                ipAddress = ipAddress.substring(ipAddress.lastIndexOf(".") + 1);
            }
            return String.valueOf(Integer.parseInt(ipAddress));
        } catch (Exception e) {
            return DEFAULT_NAME;
        }
    }

    public static String getIp() {
        Context context = null;
        if (context == null) {
            return DEFAULT_NAME;
        }
        NetworkInfo info = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        if (info.getTypeName().toString().equalsIgnoreCase("ethernet")) {
            return getWiredAddress(true);
        }
        if (info.getTypeName().toString().equalsIgnoreCase("wifi")) {
            return getWirelessIpAddress(context);
        }
        return null;
    }

    public static String getWirelessIpAddress(Context context) {
        int ipAddr = ((WifiManager) context.getSystemService("wifi")).getConnectionInfo().getIpAddress();
        StringBuffer ipBuf = new StringBuffer();
        ipAddr >>>= 8;
        ipAddr >>>= 8;
        ipBuf.append(ipAddr & 255).append('.').append(ipAddr & 255).append('.').append(ipAddr & 255).append('.').append((ipAddr >>> 8) & 255);
        return ipBuf.toString();
    }

    public static String getWiredAddress(boolean useIPv4) {
        try {
            for (NetworkInterface intf : Collections.list(NetworkInterface.getNetworkInterfaces())) {
                for (InetAddress addr : Collections.list(intf.getInetAddresses())) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress().toUpperCase();
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
