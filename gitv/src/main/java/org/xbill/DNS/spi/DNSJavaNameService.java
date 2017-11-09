package org.xbill.DNS.spi;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.StringTokenizer;
import org.xbill.DNS.ARecord;
import org.xbill.DNS.ExtendedResolver;
import org.xbill.DNS.Lookup;
import org.xbill.DNS.Name;
import org.xbill.DNS.PTRRecord;
import org.xbill.DNS.Record;
import org.xbill.DNS.ReverseMap;
import org.xbill.DNS.TextParseException;

public class DNSJavaNameService implements InvocationHandler {
    static Class array$$B = null;
    static Class array$Ljava$net$InetAddress = null;
    private static final String domainProperty = "sun.net.spi.nameservice.domain";
    private static final String nsProperty = "sun.net.spi.nameservice.nameservers";
    private static final String v6Property = "java.net.preferIPv6Addresses";
    private boolean preferV6 = false;

    protected DNSJavaNameService() {
        String nameServers = System.getProperty(nsProperty);
        String domain = System.getProperty(domainProperty);
        String v6 = System.getProperty(v6Property);
        if (nameServers != null) {
            StringTokenizer st = new StringTokenizer(nameServers, ",");
            String[] servers = new String[st.countTokens()];
            int n = 0;
            while (st.hasMoreTokens()) {
                int n2 = n + 1;
                servers[n] = st.nextToken();
                n = n2;
            }
            try {
                Lookup.setDefaultResolver(new ExtendedResolver(servers));
            } catch (UnknownHostException e) {
                System.err.println("DNSJavaNameService: invalid sun.net.spi.nameservice.nameservers");
            }
        }
        if (domain != null) {
            try {
                Lookup.setDefaultSearchPath(new String[]{domain});
            } catch (TextParseException e2) {
                System.err.println("DNSJavaNameService: invalid sun.net.spi.nameservice.domain");
            }
        }
        if (v6 != null && v6.equalsIgnoreCase("true")) {
            this.preferV6 = true;
        }
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        try {
            if (method.getName().equals("getHostByAddr")) {
                return getHostByAddr((byte[]) args[0]);
            }
            if (method.getName().equals("lookupAllHostAddr")) {
                Object class$;
                Object addresses = lookupAllHostAddr((String) args[0]);
                Class returnType = method.getReturnType();
                if (array$Ljava$net$InetAddress == null) {
                    class$ = class$("[Ljava.net.InetAddress;");
                    array$Ljava$net$InetAddress = class$;
                } else {
                    class$ = array$Ljava$net$InetAddress;
                }
                if (returnType.equals(class$)) {
                    return addresses;
                }
                if (array$$B == null) {
                    class$ = class$("[[B");
                    array$$B = class$;
                } else {
                    class$ = array$$B;
                }
                if (returnType.equals(class$)) {
                    int naddrs = addresses.length;
                    Object byteAddresses = new byte[naddrs][];
                    for (int i = 0; i < naddrs; i++) {
                        byteAddresses[i] = addresses[i].getAddress();
                    }
                    return byteAddresses;
                }
            }
            throw new IllegalArgumentException("Unknown function name or arguments.");
        } catch (Throwable e) {
            System.err.println("DNSJavaNameService: Unexpected error.");
            e.printStackTrace();
        }
    }

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError().initCause(x1);
        }
    }

    public InetAddress[] lookupAllHostAddr(String host) throws UnknownHostException {
        try {
            Name name = new Name(host);
            Record[] records = null;
            if (this.preferV6) {
                records = new Lookup(name, 28).run();
            }
            if (records == null) {
                records = new Lookup(name, 1).run();
            }
            if (records == null && !this.preferV6) {
                records = new Lookup(name, 28).run();
            }
            if (records == null) {
                throw new UnknownHostException(host);
            }
            InetAddress[] array = new InetAddress[records.length];
            for (int i = 0; i < records.length; i++) {
                Record record = records[i];
                if (records[i] instanceof ARecord) {
                    array[i] = records[i].getAddress();
                } else {
                    array[i] = records[i].getAddress();
                }
            }
            return array;
        } catch (TextParseException e) {
            throw new UnknownHostException(host);
        }
    }

    public String getHostByAddr(byte[] addr) throws UnknownHostException {
        Record[] records = new Lookup(ReverseMap.fromAddress(InetAddress.getByAddress(addr)), 12).run();
        if (records != null) {
            return ((PTRRecord) records[0]).getTarget().toString();
        }
        throw new UnknownHostException();
    }
}
