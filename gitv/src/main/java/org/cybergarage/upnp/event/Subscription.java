package org.cybergarage.upnp.event;

import org.cybergarage.upnp.UPnP;

public class Subscription {
    public static final String INFINITE_STRING = "infinite";
    public static final int INFINITE_VALUE = -1;
    public static final String SUBSCRIBE_METHOD = "SUBSCRIBE";
    public static final String TIMEOUT_HEADER = "Second-";
    public static final String UNSUBSCRIBE_METHOD = "UNSUBSCRIBE";
    public static final String UUID = "uuid:";
    public static final String XMLNS = "urn:schemas-upnp-org:event-1-0";

    public static final String toTimeoutHeaderString(long time) {
        if (time == -1) {
            return INFINITE_STRING;
        }
        return new StringBuilder(TIMEOUT_HEADER).append(Long.toString(time)).toString();
    }

    public static final long getTimeout(String headerValue) {
        long timeout = -1;
        try {
            timeout = Long.parseLong(headerValue.substring(headerValue.indexOf(45) + 1, headerValue.length()));
        } catch (Exception e) {
        }
        return timeout;
    }

    public static final String createSID() {
        return UPnP.createUUID();
    }

    public static final String toSIDHeaderString(String id) {
        return new StringBuilder(UUID).append(id).toString();
    }

    public static final String getSID(String headerValue) {
        if (headerValue == null) {
            return "";
        }
        return headerValue.startsWith(UUID) ? headerValue.substring(UUID.length(), headerValue.length()) : headerValue;
    }
}
