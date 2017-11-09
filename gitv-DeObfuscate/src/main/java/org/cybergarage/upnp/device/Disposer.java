package org.cybergarage.upnp.device;

import org.cybergarage.upnp.ControlPoint;
import org.cybergarage.util.Debug;
import org.cybergarage.util.ThreadCore;

public class Disposer extends ThreadCore {
    private ControlPoint ctrlPoint;

    public Disposer(ControlPoint ctrlp) {
        setControlPoint(ctrlp);
    }

    public void setControlPoint(ControlPoint ctrlp) {
        this.ctrlPoint = ctrlp;
    }

    public ControlPoint getControlPoint() {
        return this.ctrlPoint;
    }

    public void run() {
        ControlPoint ctrlp = getControlPoint();
        long monitorInterval = ctrlp.getExpiredDeviceMonitoringInterval() * 1000;
        while (isRunnable()) {
            try {
                Thread.sleep(monitorInterval);
                if (!isRunnable()) {
                    break;
                }
                ctrlp.removeExpiredDevices();
                ctrlp.search();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Debug.message("Disposer thread has exited");
    }
}
