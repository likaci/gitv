package org.xbill.DNS;

import java.io.IOException;
import java.util.List;

public interface Resolver {
    Message send(Message message) throws IOException;

    Object sendAsync(Message message, ResolverListener resolverListener);

    void setEDNS(int i);

    void setEDNS(int i, int i2, int i3, List list);

    void setIgnoreTruncation(boolean z);

    void setPort(int i);

    void setTCP(boolean z);

    void setTSIGKey(TSIG tsig);

    void setTimeout(int i);

    void setTimeout(int i, int i2);
}
