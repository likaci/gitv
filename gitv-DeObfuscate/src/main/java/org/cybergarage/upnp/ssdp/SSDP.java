package org.cybergarage.upnp.ssdp;

import org.cybergarage.http.HTTP;
import org.cybergarage.upnp.std.av.server.object.SearchCriteria;
import org.cybergarage.util.Debug;

public class SSDP {
    public static final String ADDRESS = "239.255.255.250";
    public static final String BROADCAST_ADDRESS = "255.255.255.255";
    public static final int DEFAULT_MSEARCH_MX = 10;
    public static final int IGALA_BROADCAST_PORT = 39390;
    private static String IPV6_ADDRESS = null;
    public static final String IPV6_ADMINISTRATIVE_ADDRESS = "FF04::C";
    public static final String IPV6_GLOBAL_ADDRESS = "FF0E::C";
    public static final String IPV6_LINK_LOCAL_ADDRESS = "FF02::C";
    public static final String IPV6_SITE_LOCAL_ADDRESS = "FF05::C";
    public static final String IPV6_SUBNET_ADDRESS = "FF03::C";
    public static final int PORT = 1900;
    public static final int RECV_MESSAGE_BUFSIZE = 1024;

    public static final void setIPv6Address(String addr) {
        IPV6_ADDRESS = addr;
    }

    public static final String getIPv6Address() {
        return IPV6_ADDRESS;
    }

    static {
        setIPv6Address(IPV6_LINK_LOCAL_ADDRESS);
    }

    public static final int getLeaseTime(String cacheCont) {
        int mx = 0;
        int maxAgeIdx = cacheCont.indexOf(HTTP.MAX_AGE);
        if (maxAgeIdx >= 0) {
            int endIdx = cacheCont.indexOf(44, maxAgeIdx);
            if (endIdx < 0) {
                endIdx = cacheCont.length();
            }
            try {
                mx = Integer.parseInt(cacheCont.substring(cacheCont.indexOf(SearchCriteria.EQ, maxAgeIdx) + 1, endIdx).trim());
            } catch (Exception e) {
                Debug.warning(e);
            }
        }
        return mx;
    }
}
