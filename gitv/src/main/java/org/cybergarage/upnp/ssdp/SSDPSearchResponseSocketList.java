package org.cybergarage.upnp.ssdp;

import java.net.InetAddress;
import java.util.Vector;
import org.cybergarage.net.HostInterface;
import org.cybergarage.upnp.ControlPoint;
import org.cybergarage.util.Debug;

public class SSDPSearchResponseSocketList extends Vector {
    private InetAddress[] binds = null;

    public SSDPSearchResponseSocketList(InetAddress[] binds) {
        this.binds = binds;
    }

    public void setControlPoint(ControlPoint ctrlPoint) {
        int nSockets = size();
        for (int n = 0; n < nSockets; n++) {
            getSSDPSearchResponseSocket(n).setControlPoint(ctrlPoint);
        }
    }

    public SSDPSearchResponseSocket getSSDPSearchResponseSocket(int n) {
        return (SSDPSearchResponseSocket) get(n);
    }

    public boolean open(int port) {
        String[] bindAddresses;
        InetAddress[] binds = this.binds;
        if (binds != null) {
            bindAddresses = new String[binds.length];
            for (int i = 0; i < binds.length; i++) {
                bindAddresses[i] = binds[i].getHostAddress();
                Debug.message("getNHostAddresses=" + bindAddresses[i] + "; n=" + i);
            }
        } else {
            int nHostAddrs = HostInterface.getNHostAddresses();
            bindAddresses = new String[nHostAddrs];
            for (int n = 0; n < nHostAddrs; n++) {
                bindAddresses[n] = HostInterface.getHostAddress(n);
                Debug.message("getNHostAddresses=" + bindAddresses[n] + "; n=" + n);
            }
        }
        for (int j = 0; j < bindAddresses.length; j++) {
            SSDPSearchResponseSocket socket = new SSDPSearchResponseSocket();
            if (!HostInterface.isIPv6Address(bindAddresses[j])) {
                if (socket.open(bindAddresses[j], port)) {
                    try {
                        Debug.message("getNHostAddresses open success" + bindAddresses[j] + "; j=" + j + "; port=" + port);
                        add(socket);
                    } catch (Exception e) {
                        stop();
                        close();
                        clear();
                        return false;
                    }
                }
                Debug.message("getNHostAddresses=" + bindAddresses[j] + "; j=" + j + "; port=" + port);
                stop();
                close();
                clear();
                return false;
            }
        }
        return true;
    }

    public boolean open() {
        return open(SSDP.PORT);
    }

    public void close() {
        int nSockets = size();
        for (int n = 0; n < nSockets; n++) {
            getSSDPSearchResponseSocket(n).close();
        }
        clear();
    }

    public void start() {
        int nSockets = size();
        for (int n = 0; n < nSockets; n++) {
            getSSDPSearchResponseSocket(n).start();
        }
    }

    public void stop() {
        int nSockets = size();
        for (int n = 0; n < nSockets; n++) {
            getSSDPSearchResponseSocket(n).stop();
        }
    }

    public boolean post(SSDPSearchRequest req) {
        boolean ret = true;
        int nSockets = size();
        int n = 0;
        while (n < nSockets) {
            try {
                SSDPSearchResponseSocket sock = getSSDPSearchResponseSocket(n);
                String bindAddr = sock.getLocalAddress();
                req.setLocalAddress(bindAddr);
                String ssdpAddr = SSDP.ADDRESS;
                if (HostInterface.isIPv6Address(bindAddr)) {
                    ssdpAddr = SSDP.getIPv6Address();
                } else if (!sock.post(ssdpAddr, (int) SSDP.PORT, req)) {
                    ret = false;
                }
                n++;
            } catch (Exception e) {
                Debug.message("++++Do search when stop, exception happened!!!");
                e.printStackTrace();
                return false;
            }
        }
        return ret;
    }
}
