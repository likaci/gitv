package org.cybergarage.upnp.control;

import java.net.MalformedURLException;
import java.net.URL;
import org.cybergarage.http.HTTP;
import org.cybergarage.http.HTTPRequest;
import org.cybergarage.soap.SOAPRequest;
import org.cybergarage.upnp.Service;

public class ControlRequest extends SOAPRequest {
    public ControlRequest(HTTPRequest httpReq) {
        set(httpReq);
    }

    public boolean isQueryControl() {
        return isSOAPAction(Control.QUERY_SOAPACTION);
    }

    public boolean isActionControl() {
        return !isQueryControl();
    }

    protected void setRequestHost(Service service) {
        String ctrlURL = service.getControlURL();
        String urlBase = service.getRootDevice().getURLBase();
        if (urlBase != null && urlBase.length() > 0) {
            try {
                String basePath = new URL(urlBase).getPath();
                int baseLen = basePath.length();
                if (baseLen > 0 && (1 < baseLen || basePath.charAt(0) != '/')) {
                    ctrlURL = new StringBuilder(String.valueOf(basePath)).append(ctrlURL).toString();
                }
            } catch (MalformedURLException e) {
            }
        }
        setURI(ctrlURL, true);
        String postURL = "";
        if (HTTP.isAbsoluteURL(ctrlURL)) {
            postURL = ctrlURL;
        }
        if (postURL == null || postURL.length() <= 0) {
            postURL = service.getRootDevice().getURLBase();
        }
        if (postURL == null || postURL.length() <= 0) {
            postURL = service.getRootDevice().getLocation();
        }
        String reqHost = HTTP.getHost(postURL);
        int reqPort = HTTP.getPort(postURL);
        setHost(reqHost, reqPort);
        setRequestHost(reqHost);
        setRequestPort(reqPort);
    }
}
