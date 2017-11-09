package org.xbill.DNS;

import java.net.SocketAddress;

public interface PacketLogger {
    void log(String str, SocketAddress socketAddress, SocketAddress socketAddress2, byte[] bArr);
}
