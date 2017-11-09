package org.cybergarage.upnp.event;

import org.cybergarage.http.HTTP;
import org.cybergarage.http.HTTPResponse;
import org.cybergarage.upnp.UPnP;

public class SubscriptionResponse extends HTTPResponse {
    public SubscriptionResponse() {
        setServer(UPnP.getServerName());
    }

    public SubscriptionResponse(HTTPResponse httpRes) {
        super(httpRes);
    }

    public void setResponse(int code) {
        setStatusCode(code);
        setContentLength(0);
    }

    public void setErrorResponse(int code) {
        setStatusCode(code);
        setContentLength(0);
    }

    public void setSID(String id) {
        setHeader(HTTP.SID, Subscription.toSIDHeaderString(id));
    }

    public String getSID() {
        return Subscription.getSID(getHeaderValue(HTTP.SID));
    }

    public void setTimeout(long value) {
        setHeader(HTTP.TIMEOUT, Subscription.toTimeoutHeaderString(value));
    }

    public long getTimeout() {
        return Subscription.getTimeout(getHeaderValue(HTTP.TIMEOUT));
    }
}
