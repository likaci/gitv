package org.cybergarage.upnp.device;

import org.cybergarage.upnp.ssdp.SSDPPacket;

public interface NotifyListener {
    void deviceNotifyReceived(SSDPPacket sSDPPacket);
}
