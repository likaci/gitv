package com.gala.video.app.epg.ui.setting.systeminfo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Build.VERSION;
import com.gala.video.lib.framework.core.utils.DeviceUtils;
import com.gala.video.lib.framework.core.utils.DeviceUtils.MacTypeEnum;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.setting.SystemInfo;
import com.gala.video.lib.share.project.Project;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import org.apache.http.conn.util.InetAddressUtils;

public class SystemInfoHelper {
    private SystemInfoHelper() {
    }

    public static final SystemInfo getSystemInfo(Context context) {
        String devName = getDeviceName();
        String model = Build.MODEL;
        String sysVer = VERSION.RELEASE;
        String softVer = getPublicVersionString(Project.getInstance().getBuild().getVersionString(), Project.getInstance().getBuild().getCustomerName(), Project.getInstance().getBuild().getProductName(), Project.getInstance().getConfig().getMultiScreenName());
        String ip = DeviceUtils.getWirelessIpAddress(context);
        LogUtils.d("SystemInfoHelper", "ip1 = " + ip);
        NetworkInfo networkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getNetworkInfo(1);
        boolean wifiConnected = false;
        if (networkInfo != null) {
            wifiConnected = networkInfo.isConnected();
        }
        String lookupInterfaceName = wifiConnected ? "wlan0" : "eth0";
        try {
            Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
            while (en.hasMoreElements()) {
                NetworkInterface intf = (NetworkInterface) en.nextElement();
                String interfaceName = intf.getDisplayName();
                LogUtils.i("SystemInfoHelper", "interfaceName=" + interfaceName);
                if (lookupInterfaceName.equals(interfaceName)) {
                    Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses();
                    while (enumIpAddr.hasMoreElements()) {
                        InetAddress inetAddress = (InetAddress) enumIpAddr.nextElement();
                        if (!inetAddress.isLoopbackAddress() && InetAddressUtils.isIPv4Address(inetAddress.getHostAddress())) {
                            ip = inetAddress.getHostAddress().toString();
                            LogUtils.d("SystemInfoHelper", "ip2 = " + ip);
                            break;
                        }
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return new SystemInfo(devName, model, sysVer, softVer, ip, DeviceUtils.getMacAddr(MacTypeEnum.MAC_ETH), DeviceUtils.getMacAddr(MacTypeEnum.MAC_WIFI));
    }

    private static String getPublicVersionString(String version, String custom, String product, String multiScreen) {
        StringBuilder sb = new StringBuilder();
        sb.append(version);
        sb.append("-").append(custom);
        sb.append("-").append(product);
        sb.append("-").append(multiScreen);
        return sb.toString();
    }

    private static String getDeviceName() {
        return "超清盒子";
    }

    @SuppressLint({"DefaultLocale"})
    public static String[] getDeviceNames() {
        String model = Build.MODEL;
        String[] deviceNames = new String[]{""};
        if (!model.toUpperCase().startsWith("MIBOX") && !model.toUpperCase().startsWith("MITV")) {
            return deviceNames;
        }
        return new String[]{"小米盒子"};
    }
}
