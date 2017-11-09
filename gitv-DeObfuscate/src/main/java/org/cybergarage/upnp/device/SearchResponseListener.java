package org.cybergarage.upnp.device;

import org.cybergarage.upnp.ssdp.SSDPPacket;

public interface SearchResponseListener {
    void deviceSearchResponseReceived(SSDPPacket sSDPPacket);
}
