package org.cybergarage.upnp.ssdp;

import com.gala.video.lib.share.ifimpl.netdiagnose.NetDiagnoseCheckTools;
import org.cybergarage.http.HTTP;

public class SSDPNotifyRequest extends SSDPRequest {
    private static SSDPNotifyRequest request = null;

    public SSDPNotifyRequest() {
        setMethod(HTTP.NOTIFY);
        setURI(NetDiagnoseCheckTools.NO_CHECK_FLAG);
    }

    public static SSDPNotifyRequest getInstance() {
        if (request == null) {
            request = new SSDPNotifyRequest();
        }
        return request;
    }
}
