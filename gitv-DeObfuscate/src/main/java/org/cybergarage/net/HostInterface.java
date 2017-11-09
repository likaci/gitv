package org.cybergarage.net;

import android.text.TextUtils;
import com.gala.video.app.epg.ui.albumlist.utils.AlbumEnterFactory;
import com.gala.video.lib.share.common.configs.WebConstants;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Vector;
import org.cybergarage.soap.SOAP;
import org.cybergarage.util.Debug;

public class HostInterface {
    public static final int IPV4_BITMASK = 1;
    public static final int IPV6_BITMASK = 16;
    public static final int LOCAL_BITMASK = 256;
    public static boolean USE_LOOPBACK_ADDR = false;
    public static boolean USE_ONLY_IPV4_ADDR = false;
    public static boolean USE_ONLY_IPV6_ADDR = false;
    private static String ifAddress = "";
    public static String[] sBindAddress;
    private static String serverIp = "";

    public static final void setInterface(String ifaddr) {
        ifAddress = ifaddr;
    }

    public static final String getInterface() {
        return ifAddress;
    }

    private static final boolean hasAssignedInterface() {
        return ifAddress.length() > 0;
    }

    private static final boolean isUsableAddress(InetAddress addr) {
        if (!USE_LOOPBACK_ADDR && addr.isLoopbackAddress()) {
            return false;
        }
        if (USE_ONLY_IPV4_ADDR && (addr instanceof Inet6Address)) {
            return false;
        }
        if (USE_ONLY_IPV6_ADDR && (addr instanceof Inet4Address)) {
            return false;
        }
        return true;
    }

    public static final int getNHostAddresses() {
        if (getServerIp() != null && getServerIp().length() > 0) {
            return 1;
        }
        int nHostAddrs = 0;
        try {
            Enumeration nis = NetworkInterface.getNetworkInterfaces();
            while (nis.hasMoreElements()) {
                Enumeration addrs = ((NetworkInterface) nis.nextElement()).getInetAddresses();
                while (addrs.hasMoreElements()) {
                    if (isUsableAddress((InetAddress) addrs.nextElement())) {
                        nHostAddrs++;
                    }
                }
            }
            return nHostAddrs;
        } catch (Exception e) {
            Debug.warning(e);
            return 0;
        }
    }

    public static final InetAddress[] getInetAddress(int ipfilter, String[] interfaces) {
        Enumeration nis;
        if (getServerIp() != null && getServerIp().length() > 0) {
            InetAddress[] interAddress = new InetAddress[1];
            try {
                interAddress[0] = InetAddress.getByName(getServerIp());
                return interAddress;
            } catch (Exception e) {
            }
        }
        if (interfaces != null) {
            Vector iflist = new Vector();
            for (String byName : interfaces) {
                try {
                    NetworkInterface ni = NetworkInterface.getByName(byName);
                    if (ni != null) {
                        iflist.add(ni);
                    }
                } catch (SocketException e2) {
                }
            }
            nis = iflist.elements();
        } else {
            try {
                nis = NetworkInterface.getNetworkInterfaces();
            } catch (SocketException e3) {
                return null;
            }
        }
        ArrayList addresses = new ArrayList();
        while (nis.hasMoreElements()) {
            Enumeration addrs = ((NetworkInterface) nis.nextElement()).getInetAddresses();
            while (addrs.hasMoreElements()) {
                InetAddress addr = (InetAddress) addrs.nextElement();
                if ((ipfilter & 256) != 0 || !addr.isLoopbackAddress()) {
                    if ((ipfilter & 1) != 0 && (addr instanceof Inet4Address)) {
                        addresses.add(addr);
                    } else if ((ipfilter & 16) != 0 && (addr instanceof InetAddress)) {
                        addresses.add(addr);
                    }
                }
            }
        }
        return (InetAddress[]) addresses.toArray(new InetAddress[0]);
    }

    public static String getServerIp() {
        return serverIp;
    }

    public static void setServerIp(String mserverIp) {
        if (!TextUtils.isEmpty(mserverIp)) {
            if (isIPv4Address(mserverIp) || isIPv6Address(mserverIp)) {
                serverIp = mserverIp;
            }
        }
    }

    public static String[] getHostIpAddress(boolean isRefresh) {
        if (!TextUtils.isEmpty(serverIp) && isIPv4Address(serverIp)) {
            return serverIp.split(" ");
        }
        if (isRefresh) {
            Enumeration<NetworkInterface> nis = null;
            try {
                nis = NetworkInterface.getNetworkInterfaces();
            } catch (SocketException e) {
                Debug.message("Can not get NetworkInterface");
                e.printStackTrace();
            }
            if (nis != null) {
                ArrayList<String> ipList = new ArrayList();
                while (nis.hasMoreElements()) {
                    Enumeration<InetAddress> addrs = ((NetworkInterface) nis.nextElement()).getInetAddresses();
                    while (addrs.hasMoreElements()) {
                        InetAddress addr = (InetAddress) addrs.nextElement();
                        if (isUsableAddress(addr) && (addr instanceof Inet4Address)) {
                            ipList.add(addr.getHostAddress());
                        }
                    }
                }
                if (ipList.size() == 0) {
                    return null;
                }
                sBindAddress = new String[ipList.size()];
                ipList.toArray(sBindAddress);
            }
        }
        return sBindAddress;
    }

    public static final String getHostAddress(int n) {
        if (TextUtils.isEmpty(serverIp) || !isIPv4Address(serverIp)) {
            int hostAddrCnt = 0;
            try {
                Enumeration nis = NetworkInterface.getNetworkInterfaces();
                while (nis.hasMoreElements()) {
                    Enumeration addrs = ((NetworkInterface) nis.nextElement()).getInetAddresses();
                    while (addrs.hasMoreElements()) {
                        InetAddress addr = (InetAddress) addrs.nextElement();
                        if (isUsableAddress(addr)) {
                            if (hostAddrCnt >= n) {
                                return addr.getHostAddress().toString();
                            }
                            hostAddrCnt++;
                        }
                    }
                }
            } catch (Exception e) {
            }
            return "";
        }
        Debug.message(serverIp);
        return serverIp;
    }

    public static final boolean isIPv6Address(String host) {
        try {
            if (InetAddress.getByName(host) instanceof Inet6Address) {
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public static final boolean isIPv4Address(String host) {
        try {
            if (InetAddress.getByName(host) instanceof Inet4Address) {
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public static final boolean hasIPv4Addresses() {
        int addrCnt = getNHostAddresses();
        for (int n = 0; n < addrCnt; n++) {
            if (isIPv4Address(getHostAddress(n))) {
                return true;
            }
        }
        return false;
    }

    public static final boolean hasIPv6Addresses() {
        int addrCnt = getNHostAddresses();
        for (int n = 0; n < addrCnt; n++) {
            if (isIPv6Address(getHostAddress(n))) {
                return true;
            }
        }
        return false;
    }

    public static final String getIPv4Address() {
        int addrCnt = getNHostAddresses();
        for (int n = 0; n < addrCnt; n++) {
            String addr = getHostAddress(n);
            if (isIPv4Address(addr)) {
                return addr;
            }
        }
        return "";
    }

    public static final String getIPv6Address() {
        int addrCnt = getNHostAddresses();
        for (int n = 0; n < addrCnt; n++) {
            String addr = getHostAddress(n);
            if (isIPv6Address(addr)) {
                return addr;
            }
        }
        return "";
    }

    public static final String getHostURL(String host, int port, String uri) {
        String hostAddr = host;
        if (isIPv6Address(host)) {
            hostAddr = "[" + host + AlbumEnterFactory.SIGN_STR;
        }
        return new StringBuilder(WebConstants.WEB_SITE_BASE_HTTP).append(hostAddr).append(SOAP.DELIM).append(Integer.toString(port)).append(uri).toString();
    }
}
