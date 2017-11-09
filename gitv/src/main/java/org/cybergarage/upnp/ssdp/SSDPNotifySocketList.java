package org.cybergarage.upnp.ssdp;

import java.net.InetAddress;
import java.util.Vector;
import org.cybergarage.net.HostInterface;
import org.cybergarage.upnp.ControlPoint;
import org.cybergarage.util.Debug;

public class SSDPNotifySocketList extends Vector {
    private InetAddress[] binds = null;

    public SSDPNotifySocketList(InetAddress[] binds) {
        this.binds = binds;
    }

    public SSDPNotifySocket getSSDPNotifySocket(int n) {
        return (SSDPNotifySocket) get(n);
    }

    public void setControlPoint(ControlPoint ctrlPoint) {
        int nSockets = size();
        for (int n = 0; n < nSockets; n++) {
            getSSDPNotifySocket(n).setControlPoint(ctrlPoint);
        }
    }

    public boolean open() {
        String[] bindAddresses;
        int i;
        InetAddress[] binds = this.binds;
        if (binds != null) {
            bindAddresses = new String[binds.length];
            for (i = 0; i < binds.length; i++) {
                bindAddresses[i] = binds[i].getHostAddress();
            }
        } else {
            int nHostAddrs = HostInterface.getNHostAddresses();
            bindAddresses = new String[nHostAddrs];
            for (int n = 0; n < nHostAddrs; n++) {
                bindAddresses[n] = HostInterface.getHostAddress(n);
            }
        }
        i = 0;
        while (i < bindAddresses.length) {
            if (!(HostInterface.isIPv6Address(bindAddresses[i]) || bindAddresses[i] == null)) {
                Debug.message("add ssdp notifysocket: " + bindAddresses[i]);
                SSDPNotifySocket ssdpNotifySocket = SSDPNotifySocket.getInstance(bindAddresses[i]);
                ssdpNotifySocket.openReceiveBroadcastSocket();
                add(ssdpNotifySocket);
            }
            i++;
        }
        return true;
    }

    public void close() {
        int nSockets = size();
        for (int n = 0; n < nSockets; n++) {
            SSDPNotifySocket sock = getSSDPNotifySocket(n);
            sock.closeReceiveMulticastSocket();
            sock.closeReceiveBroadcastSocket();
        }
        clear();
    }

    public void start() {
        int nSockets = size();
        for (int n = 0; n < nSockets; n++) {
            getSSDPNotifySocket(n).start();
        }
    }

    public void stop() {
        int nSockets = size();
        for (int n = 0; n < nSockets; n++) {
            getSSDPNotifySocket(n).stop();
        }
    }
}
