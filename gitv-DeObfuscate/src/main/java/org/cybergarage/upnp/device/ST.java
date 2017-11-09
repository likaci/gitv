package org.cybergarage.upnp.device;

public class ST {
    public static final String ALL_DEVICE = "ssdp:all";
    public static final byte[] ALL_DEVICE_BYTE = ALL_DEVICE.getBytes();
    public static final String ROOT_DEVICE = "upnp:rootdevice";
    public static final byte[] ROOT_DEVICE_BYTE = "upnp:rootdevice".getBytes();
    public static final String URN_DEVICE = "urn:schemas-upnp-org:device:";
    public static final byte[] URN_DEVICE_BYTE = URN_DEVICE.getBytes();
    public static final String URN_SERVICE = "urn:schemas-upnp-org:service:";
    public static final byte[] URN_SERVICE_BYTE = URN_SERVICE.getBytes();
    public static final String UUID_DEVICE = "uuid";
    public static final byte[] UUID_DEVICE_BYTE = "uuid".getBytes();

    public static final boolean isAllDevice(String value) {
        if (value == null) {
            return false;
        }
        if (value.equals(ALL_DEVICE)) {
            return true;
        }
        return value.equals("\"ssdp:all\"");
    }

    public static final boolean isRootDevice(String value) {
        if (value == null) {
            return false;
        }
        if (value.equals("upnp:rootdevice")) {
            return true;
        }
        return value.equals("\"upnp:rootdevice\"");
    }

    public static final boolean isUUIDDevice(String value) {
        if (value == null) {
            return false;
        }
        if (value.startsWith("uuid")) {
            return true;
        }
        return value.startsWith("\"uuid");
    }

    public static final boolean isURNDevice(String value) {
        if (value == null) {
            return false;
        }
        if (value.startsWith(URN_DEVICE)) {
            return true;
        }
        return value.startsWith("\"urn:schemas-upnp-org:device:");
    }

    public static final boolean isURNService(String value) {
        if (value == null) {
            return false;
        }
        if (value.startsWith(URN_SERVICE)) {
            return true;
        }
        return value.startsWith("\"urn:schemas-upnp-org:service:");
    }
}
