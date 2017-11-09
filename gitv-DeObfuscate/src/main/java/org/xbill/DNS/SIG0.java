package org.xbill.DNS;

import java.security.PrivateKey;
import java.util.Date;
import org.xbill.DNS.DNSSEC.DNSSECException;

public class SIG0 {
    private static final short VALIDITY = (short) 300;

    private SIG0() {
    }

    public static void signMessage(Message message, KEYRecord key, PrivateKey privkey, SIGRecord previous) throws DNSSECException {
        int validity = Options.intValue("sig0validity");
        if (validity < 0) {
            validity = 300;
        }
        long now = System.currentTimeMillis();
        message.addRecord(DNSSEC.signMessage(message, previous, key, privkey, new Date(now), new Date(((long) (validity * 1000)) + now)), 3);
    }

    public static void verifyMessage(Message message, byte[] b, KEYRecord key, SIGRecord previous) throws DNSSECException {
        SIGRecord sig = null;
        Record[] additional = message.getSectionArray(3);
        int i = 0;
        while (i < additional.length) {
            if (additional[i].getType() == 24 && ((SIGRecord) additional[i]).getTypeCovered() == 0) {
                sig = additional[i];
                break;
            }
            i++;
        }
        DNSSEC.verifyMessage(message, b, sig, previous, key);
    }
}
