package org.cybergarage.upnp;

import java.util.Vector;

public class DeviceList extends Vector<Device> {
    public static final String ELEM_NAME = "deviceList";

    public Device getDevice(int n) {
        if (size() != 0 && n <= size() - 1) {
            return (Device) get(n);
        }
        return null;
    }
}
