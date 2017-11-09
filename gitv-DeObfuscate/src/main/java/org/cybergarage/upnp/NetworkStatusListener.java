package org.cybergarage.upnp;

public interface NetworkStatusListener {
    void OnNetworkStatusChanged(NETWORK_STATUS network_status);

    void OnResponseTimeGot(long j);
}
