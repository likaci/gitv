package org.cybergarage.upnp.device;

public class USN {
    public static final String ROOTDEVICE = "upnp:rootdevice";

    public static final boolean isRootDevice(String usnValue) {
        if (usnValue == null) {
            return false;
        }
        return usnValue.endsWith("upnp:rootdevice");
    }

    public static final String getUDN(String usnValue) {
        if (usnValue == null) {
            return "";
        }
        int idx = usnValue.indexOf("::");
        if (idx < 0) {
            return usnValue.trim();
        }
        return new String(usnValue.getBytes(), 0, idx).trim();
    }
}
