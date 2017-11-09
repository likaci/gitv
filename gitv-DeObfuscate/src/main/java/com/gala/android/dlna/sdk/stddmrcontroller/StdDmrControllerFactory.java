package com.gala.android.dlna.sdk.stddmrcontroller;

import org.cybergarage.upnp.Device;

public class StdDmrControllerFactory {
    public static StdDmrController getStdDmrControllerByDevice(Device targetDevice) {
        if (Util.isMiDmrDevice(targetDevice)) {
            return new MiDmrController(targetDevice);
        }
        if (Util.isStdDmrDevice(targetDevice)) {
            return new StdDmrController(targetDevice);
        }
        return null;
    }
}
