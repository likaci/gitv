package org.cybergarage.upnp.ssdp;

import java.net.InetAddress;
import java.util.Vector;
import org.cybergarage.net.HostInterface;
import org.cybergarage.upnp.device.SearchListener;

public class SSDPSearchSocketList extends Vector<SSDPSearchSocket> {
    private static final long serialVersionUID = 1;
    private InetAddress[] binds = null;
    private String multicastIPv4 = SSDP.ADDRESS;
    private String multicastIPv6 = SSDP.getIPv6Address();
    private int port = SSDP.PORT;

    public SSDPSearchSocketList(InetAddress[] binds) {
        this.binds = binds;
    }

    public SSDPSearchSocketList(InetAddress[] binds, int port, String multicastIPv4, String multicastIPv6) {
        this.binds = binds;
        this.port = port;
        this.multicastIPv4 = multicastIPv4;
        this.multicastIPv6 = multicastIPv6;
    }

    public SSDPSearchSocket getSSDPSearchSocket(int n) {
        try {
            return (SSDPSearchSocket) get(n);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public void addSearchListener(SearchListener listener) {
        int nServers = size();
        for (int n = 0; n < nServers; n++) {
            getSSDPSearchSocket(n).addSearchListener(listener);
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
                SSDPSearchSocket ssdpSearchSocket;
                if (HostInterface.isIPv6Address(bindAddresses[i])) {
                    ssdpSearchSocket = new SSDPSearchSocket(bindAddresses[i], this.port, this.multicastIPv6);
                } else {
                    ssdpSearchSocket = new SSDPSearchSocket(bindAddresses[i], this.port, this.multicastIPv4);
                }
                add(ssdpSearchSocket);
            }
            i++;
        }
        return true;
    }

    public void close() {
        int nSockets = size();
        for (int n = 0; n < nSockets; n++) {
            getSSDPSearchSocket(n).closeReceiveMulticastSocket();
        }
        clear();
    }

    public void start() {
        int nSockets = size();
        for (int n = 0; n < nSockets; n++) {
            getSSDPSearchSocket(n).start();
        }
    }

    public void stop() {
        int nSockets = size();
        for (int n = 0; n < nSockets; n++) {
            getSSDPSearchSocket(n).stop();
        }
    }
}
