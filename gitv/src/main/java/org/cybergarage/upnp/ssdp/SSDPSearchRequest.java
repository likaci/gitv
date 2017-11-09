package org.cybergarage.upnp.ssdp;

import com.gala.video.lib.share.ifimpl.netdiagnose.NetDiagnoseCheckTools;
import org.cybergarage.http.HTTP;
import org.cybergarage.net.HostInterface;
import org.cybergarage.util.Debug;

public class SSDPSearchRequest extends SSDPRequest {
    public SSDPSearchRequest(String serachTarget, int mx) {
        setMethod(HTTP.M_SEARCH);
        setURI(NetDiagnoseCheckTools.NO_CHECK_FLAG);
        setHeader(HTTP.ST, serachTarget);
        setHeader(HTTP.MX, Integer.toString(mx));
        setHeader(HTTP.MAN, "\"ssdp:discover\"");
        setHeader(HTTP.CONTENT_LENGTH, "0");
    }

    public SSDPSearchRequest(String serachTarget) {
        this(serachTarget, 10);
    }

    public SSDPSearchRequest() {
        this("upnp:rootdevice");
    }

    public void setLocalAddress(String bindAddr) {
        String ssdpAddr = SSDP.ADDRESS;
        Debug.message("setLocalAddress: bindAddr = " + bindAddr);
        if (HostInterface.isIPv6Address(bindAddr)) {
            ssdpAddr = SSDP.getIPv6Address();
        }
        setHost(ssdpAddr, SSDP.PORT);
    }
}
