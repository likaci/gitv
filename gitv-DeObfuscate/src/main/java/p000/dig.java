package p000;

import com.gala.video.app.epg.screensaver.constants.ScreenSaverConstants.ScreenSaverPingBack;
import com.gala.video.lib.share.ifimpl.ucenter.history.impl.HistoryInfoHelper;
import java.io.IOException;
import java.net.InetAddress;
import org.xbill.DNS.DClass;
import org.xbill.DNS.Message;
import org.xbill.DNS.Name;
import org.xbill.DNS.Record;
import org.xbill.DNS.ReverseMap;
import org.xbill.DNS.SimpleResolver;
import org.xbill.DNS.TSIG;
import org.xbill.DNS.Type;
import org.xbill.DNS.WKSRecord.Service;

public class dig {
    static int dclass = 1;
    static Name name = null;
    static int type = 1;

    static void usage() {
        System.out.println("Usage: dig [@server] name [<type>] [<class>] [options]");
        System.exit(0);
    }

    static void doQuery(Message response, long ms) throws IOException {
        System.out.println("; java dig 0.0");
        System.out.println(response);
        System.out.println(new StringBuffer().append(";; Query time: ").append(ms).append(" ms").toString());
    }

    static void doAXFR(Message response) throws IOException {
        System.out.println(new StringBuffer().append("; java dig 0.0 <> ").append(name).append(" axfr").toString());
        if (response.isSigned()) {
            System.out.print(";; TSIG ");
            if (response.isVerified()) {
                System.out.println(ScreenSaverPingBack.SEAT_KEY_OK);
            } else {
                System.out.println("failed");
            }
        }
        if (response.getRcode() != 0) {
            System.out.println(response);
            return;
        }
        Record[] records = response.getSectionArray(1);
        for (Object println : records) {
            System.out.println(println);
        }
        System.out.print(";; done (");
        System.out.print(response.getHeader().getCount(1));
        System.out.print(" records, ");
        System.out.print(response.getHeader().getCount(3));
        System.out.println(" additional)");
    }

    public static void main(String[] argv) throws IOException {
        int arg;
        int i;
        Message query;
        long startTime;
        Message response;
        long endTime;
        String server = null;
        SimpleResolver simpleResolver = null;
        boolean printQuery = false;
        if (argv.length < 1) {
            dig.usage();
        }
        if (argv[0].startsWith("@")) {
            arg = 0 + 1;
            try {
                server = argv[0].substring(1);
            } catch (ArrayIndexOutOfBoundsException e) {
                i = arg;
                if (name == null) {
                    dig.usage();
                }
                if (simpleResolver == null) {
                    simpleResolver = new SimpleResolver();
                }
                query = Message.newQuery(Record.newRecord(name, type, dclass));
                if (printQuery) {
                    System.out.println(query);
                }
                startTime = System.currentTimeMillis();
                response = simpleResolver.send(query);
                endTime = System.currentTimeMillis();
                if (type != 252) {
                    dig.doQuery(response, endTime - startTime);
                } else {
                    dig.doAXFR(response);
                }
            }
        }
        arg = 0;
        if (server != null) {
            simpleResolver = new SimpleResolver(server);
        } else {
            simpleResolver = new SimpleResolver();
        }
        i = arg + 1;
        String nameString = argv[arg];
        if (nameString.equals("-x")) {
            arg = i + 1;
            name = ReverseMap.fromAddress(argv[i]);
            type = 12;
            dclass = 1;
            i = arg;
        } else {
            name = Name.fromString(nameString, Name.root);
            type = Type.value(argv[i]);
            if (type < 0) {
                type = 1;
            } else {
                i++;
            }
            dclass = DClass.value(argv[i]);
            if (dclass < 0) {
                dclass = 1;
            } else {
                i++;
            }
        }
        while (argv[i].startsWith("-") && argv[i].length() > 1) {
            switch (argv[i].charAt(1)) {
                case Service.TACNEWS /*98*/:
                    String addrStr;
                    if (argv[i].length() > 2) {
                        addrStr = argv[i].substring(2);
                    } else {
                        i++;
                        addrStr = argv[i];
                    }
                    try {
                        simpleResolver.setLocalAddress(InetAddress.getByName(addrStr));
                        break;
                    } catch (Exception e2) {
                        System.out.println("Invalid address");
                        return;
                    }
                case 'd':
                    simpleResolver.setEDNS(0, 0, 32768, null);
                    break;
                case 'e':
                    String ednsStr;
                    if (argv[i].length() > 2) {
                        ednsStr = argv[i].substring(2);
                    } else {
                        i++;
                        ednsStr = argv[i];
                    }
                    int edns = Integer.parseInt(ednsStr);
                    if (edns >= 0 && edns <= 1) {
                        simpleResolver.setEDNS(edns);
                        break;
                    } else {
                        System.out.println(new StringBuffer().append("Unsupported EDNS level: ").append(edns).toString());
                        return;
                    }
                case 'i':
                    simpleResolver.setIgnoreTruncation(true);
                    break;
                case 'k':
                    String key;
                    if (argv[i].length() > 2) {
                        key = argv[i].substring(2);
                    } else {
                        i++;
                        key = argv[i];
                    }
                    simpleResolver.setTSIGKey(TSIG.fromString(key));
                    break;
                case HistoryInfoHelper.MSG_MERGE /*112*/:
                    try {
                        String portStr;
                        if (argv[i].length() > 2) {
                            portStr = argv[i].substring(2);
                        } else {
                            i++;
                            portStr = argv[i];
                        }
                        int port = Integer.parseInt(portStr);
                        if (port >= 0 && port <= 65536) {
                            simpleResolver.setPort(port);
                            break;
                        } else {
                            System.out.println("Invalid port");
                            return;
                        }
                    } catch (ArrayIndexOutOfBoundsException e3) {
                        break;
                    }
                case 'q':
                    printQuery = true;
                    break;
                case 't':
                    simpleResolver.setTCP(true);
                    break;
                default:
                    System.out.print("Invalid option: ");
                    System.out.println(argv[i]);
                    break;
            }
            i++;
        }
        if (simpleResolver == null) {
            simpleResolver = new SimpleResolver();
        }
        query = Message.newQuery(Record.newRecord(name, type, dclass));
        if (printQuery) {
            System.out.println(query);
        }
        startTime = System.currentTimeMillis();
        response = simpleResolver.send(query);
        endTime = System.currentTimeMillis();
        if (type != 252) {
            dig.doAXFR(response);
        } else {
            dig.doQuery(response, endTime - startTime);
        }
    }
}
