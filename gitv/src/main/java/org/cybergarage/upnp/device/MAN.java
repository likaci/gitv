package org.cybergarage.upnp.device;

public class MAN {
    public static final String DISCOVER = "ssdp:discover";
    public static final byte[] DISCOVER_BYTE = DISCOVER.getBytes();

    public static final boolean isDiscover(String value) {
        if (value == null) {
            return false;
        }
        if (value.equals(DISCOVER)) {
            return true;
        }
        return value.equals("\"ssdp:discover\"");
    }
}
