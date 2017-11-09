package org.cybergarage.upnp.device;

import java.net.SocketAddress;
import org.cybergarage.upnp.ssdp.SSDPPacket;

public interface SearchListener {
    @Deprecated
    void deviceSearchReceived(SSDPPacket sSDPPacket);

    void ssdpSearchReceived(byte[] bArr, String str, SocketAddress socketAddress);
}
