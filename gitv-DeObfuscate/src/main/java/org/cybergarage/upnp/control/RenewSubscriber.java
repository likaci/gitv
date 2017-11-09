package org.cybergarage.upnp.control;

import org.cybergarage.upnp.ControlPoint;
import org.cybergarage.util.Debug;
import org.cybergarage.util.ThreadCore;

public class RenewSubscriber extends ThreadCore {
    public static final long INTERVAL = 30;
    private ControlPoint ctrlPoint;
    private long timeout = -1;

    public RenewSubscriber(ControlPoint ctrlp) {
        setControlPoint(ctrlp);
    }

    public void setControlPoint(ControlPoint ctrlp) {
        this.ctrlPoint = ctrlp;
    }

    public ControlPoint getControlPoint() {
        return this.ctrlPoint;
    }

    public void setSubscriberTimeout(long time) {
        this.timeout = time;
    }

    public void run() {
        ControlPoint ctrlp = getControlPoint();
        while (isRunnable()) {
            try {
                Thread.sleep(30000);
                if (!isRunnable()) {
                    break;
                }
                ctrlp.renewSubscriberService(this.timeout);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Debug.message("RenewSubscriber thread has exited");
    }
}
