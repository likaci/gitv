package org.cybergarage.upnp.device;

import org.cybergarage.upnp.Device;
import org.cybergarage.util.Debug;
import org.cybergarage.util.ThreadCore;

public class Advertiser extends ThreadCore {
    private Device device;

    public Advertiser(Device dev) {
        setDevice(dev);
    }

    public void setDevice(Device dev) {
        this.device = dev;
    }

    public Device getDevice() {
        return this.device;
    }

    public void run() {
        Device dev = getDevice();
        long leaseTime = (long) dev.getLeaseTime();
        while (isRunnable()) {
            try {
                Thread.sleep(((leaseTime / 6) + ((long) (((double) ((float) leaseTime)) * (Math.random() * 0.25d)))) * 1000);
                if (!isRunnable()) {
                    break;
                }
                dev.announce();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Debug.message("Advertiser thread has exited");
    }
}
