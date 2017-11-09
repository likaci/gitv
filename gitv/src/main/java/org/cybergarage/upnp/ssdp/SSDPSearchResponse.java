package org.cybergarage.upnp.ssdp;

import org.cybergarage.http.HTTP;
import org.cybergarage.upnp.UPnP;

public class SSDPSearchResponse extends SSDPResponse {
    private static SSDPSearchResponse response = null;

    public SSDPSearchResponse() {
        setStatusCode(200);
        setCacheControl(30);
        setHeader(HTTP.SERVER, UPnP.getServerName());
        setHeader(HTTP.EXT, "");
    }

    public static SSDPSearchResponse getInstance() {
        if (response == null) {
            response = new SSDPSearchResponse();
        }
        return response;
    }
}
