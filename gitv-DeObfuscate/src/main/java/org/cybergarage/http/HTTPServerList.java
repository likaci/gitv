package org.cybergarage.http;

import java.net.InetAddress;
import java.util.Vector;
import org.cybergarage.net.HostInterface;
import org.cybergarage.upnp.Device;

public class HTTPServerList extends Vector {
    private InetAddress[] binds = null;
    private int port = Device.HTTP_DEFAULT_PORT;

    public HTTPServerList(InetAddress[] list, int port) {
        this.binds = list;
        this.port = port;
    }

    public void addRequestListener(HTTPRequestListener listener) {
        int nServers = size();
        for (int n = 0; n < nServers; n++) {
            getHTTPServer(n).addRequestListener(listener);
        }
    }

    public HTTPServer getHTTPServer(int n) {
        return (HTTPServer) get(n);
    }

    public void close() {
        int nServers = size();
        for (int n = 0; n < nServers; n++) {
            getHTTPServer(n).close();
        }
    }

    public int open() {
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
        int j = 0;
        i = 0;
        while (i < bindAddresses.length) {
            if (!HostInterface.isIPv6Address(bindAddresses[i])) {
                HTTPServer httpServer = new HTTPServer();
                if (bindAddresses[i] == null || !httpServer.open(bindAddresses[i], this.port)) {
                    close();
                    clear();
                } else {
                    add(httpServer);
                    j++;
                }
            }
            i++;
        }
        return j;
    }

    public boolean open(int port) {
        this.port = port;
        return open() != 0;
    }

    public void start() {
        int nServers = size();
        for (int n = 0; n < nServers; n++) {
            getHTTPServer(n).start();
        }
    }

    public void stop() {
        int nServers = size();
        for (int n = 0; n < nServers; n++) {
            getHTTPServer(n).stop();
        }
    }
}
