package org.cybergarage.upnp.event;

import org.cybergarage.http.HTTP;
import org.cybergarage.http.HTTPRequest;
import org.cybergarage.upnp.Device;
import org.cybergarage.upnp.Service;
import org.cybergarage.upnp.device.NT;

public class SubscriptionRequest extends HTTPRequest {
    private static final String CALLBACK_END_WITH = ">";
    private static final String CALLBACK_START_WITH = "<";

    public SubscriptionRequest() {
        setContentLength(0);
    }

    public SubscriptionRequest(HTTPRequest httpReq) {
        this();
        set(httpReq);
    }

    private void setService(Service service) {
        Device rootDev;
        String eventSubURL = service.getEventSubURL();
        setURI(eventSubURL, true);
        String urlBaseStr = "";
        Device dev = service.getDevice();
        if (dev != null) {
            urlBaseStr = dev.getURLBase();
        }
        if (urlBaseStr == null || urlBaseStr.length() <= 0) {
            rootDev = service.getRootDevice();
            if (rootDev != null) {
                urlBaseStr = rootDev.getURLBase();
            }
        }
        if (urlBaseStr == null || urlBaseStr.length() <= 0) {
            rootDev = service.getRootDevice();
            if (rootDev != null) {
                urlBaseStr = rootDev.getLocation();
            }
        }
        if ((urlBaseStr == null || urlBaseStr.length() <= 0) && HTTP.isAbsoluteURL(eventSubURL)) {
            urlBaseStr = eventSubURL;
        }
        String reqHost = HTTP.getHost(urlBaseStr);
        int reqPort = HTTP.getPort(urlBaseStr);
        setHost(reqHost, reqPort);
        setRequestHost(reqHost);
        setRequestPort(reqPort);
    }

    public void setSubscribeRequest(Service service, String callback, long timeout) {
        setMethod("SUBSCRIBE");
        setService(service);
        setCallback(callback);
        setNT(NT.EVENT);
        setTimeout(timeout);
    }

    public void setRenewRequest(Service service, String uuid, long timeout) {
        setMethod("SUBSCRIBE");
        setService(service);
        setSID(uuid);
        setTimeout(timeout);
    }

    public void setUnsubscribeRequest(Service service) {
        setMethod("UNSUBSCRIBE");
        setService(service);
        setSID(service.getSID());
    }

    public void setNT(String value) {
        setHeader(HTTP.NT, value);
    }

    public String getNT() {
        return getHeaderValue(HTTP.NT);
    }

    public boolean hasNT() {
        String nt = getNT();
        return nt != null && nt.length() > 0;
    }

    public void setCallback(String value) {
        setStringHeader(HTTP.CALLBACK, value, "<", ">");
    }

    public String getCallback() {
        return getStringHeaderValue(HTTP.CALLBACK, "<", ">");
    }

    public boolean hasCallback() {
        String callback = getCallback();
        return callback != null && callback.length() > 0;
    }

    public void setGID(String value) {
        setHeader(HTTP.GID, value);
    }

    public String getGID() {
        return getHeaderValue(HTTP.GID);
    }

    public void setSID(String id) {
        setHeader(HTTP.SID, Subscription.toSIDHeaderString(id));
    }

    public String getSID() {
        String sid = Subscription.getSID(getHeaderValue(HTTP.SID));
        if (sid == null) {
            return "";
        }
        return sid;
    }

    public boolean hasSID() {
        String sid = getSID();
        return sid != null && sid.length() > 0;
    }

    public final void setTimeout(long value) {
        setHeader(HTTP.TIMEOUT, Subscription.toTimeoutHeaderString(value));
    }

    public long getTimeout() {
        return Subscription.getTimeout(getHeaderValue(HTTP.TIMEOUT));
    }

    public void post(SubscriptionResponse subRes) {
        super.post(subRes);
    }

    public SubscriptionResponse post() {
        return new SubscriptionResponse(post(getRequestHost(), getRequestPort()));
    }
}
