package com.gala.video.lib.share.ifimpl.netdiagnose.job;

import com.gala.video.app.epg.screensaver.constants.ScreenSaverConstants.ScreenSaverPingBack;
import com.gala.video.lib.share.ifimpl.ucenter.history.impl.HistoryInfoHelper;
import java.io.IOException;
import java.net.InetAddress;
import org.cybergarage.http.HTTP;
import org.xbill.DNS.DClass;
import org.xbill.DNS.Message;
import org.xbill.DNS.Name;
import org.xbill.DNS.Record;
import org.xbill.DNS.ReverseMap;
import org.xbill.DNS.SimpleResolver;
import org.xbill.DNS.TSIG;
import org.xbill.DNS.Type;
import org.xbill.DNS.WKSRecord.Service;

public class DigUtils {
    static int dclass = 1;
    static Name name = null;
    static int type = 1;

    static void doQuery(StringBuilder sb, Message response, long ms) throws IOException {
        sb.append("###nslookup start");
        sb.append(HTTP.CRLF);
        sb.append(response);
        sb.append(HTTP.CRLF);
        sb.append("###Query time: " + ms + " ms");
    }

    static void doAXFR(StringBuilder sb, Message response) throws IOException {
        sb.append("###java dig 0.0 <> " + name + " axfr");
        sb.append(HTTP.CRLF);
        if (response.isSigned()) {
            sb.append("###TSIG ");
            if (response.isVerified()) {
                sb.append(ScreenSaverPingBack.SEAT_KEY_OK);
                sb.append(HTTP.CRLF);
            } else {
                sb.append("failed");
                sb.append(HTTP.CRLF);
            }
        }
        if (response.getRcode() != 0) {
            sb.append(response);
            return;
        }
        Record[] records = response.getSectionArray(1);
        for (Object append : records) {
            sb.append(append);
            sb.append(HTTP.CRLF);
        }
        sb.append("###done (");
        sb.append(response.getHeader().getCount(1));
        sb.append(" records, ");
        sb.append(response.getHeader().getCount(3));
        sb.append(" additional)");
        sb.append(HTTP.CRLF);
    }

    static String digHost(String[] argv) {
        int arg;
        int i;
        SimpleResolver res;
        Exception e;
        Message query;
        Message response;
        StringBuilder sb = new StringBuilder();
        String server = null;
        SimpleResolver simpleResolver = null;
        boolean printQuery = false;
        if (argv[0].startsWith("@")) {
            arg = 0 + 1;
            try {
                server = argv[0].substring(1);
            } catch (ArrayIndexOutOfBoundsException e2) {
                i = arg;
                res = simpleResolver;
                if (res != null) {
                    simpleResolver = res;
                } else {
                    try {
                        System.out.print("res is null");
                        simpleResolver = new SimpleResolver();
                    } catch (Exception e3) {
                        e = e3;
                        simpleResolver = res;
                        sb.append("### nslookup error : " + e);
                        sb.append(HTTP.CRLF);
                        e.printStackTrace();
                        return sb.toString();
                    }
                }
                query = Message.newQuery(Record.newRecord(name, type, dclass));
                if (printQuery) {
                    sb.append(query);
                    sb.append(HTTP.CRLF);
                }
                startTime = System.currentTimeMillis();
                response = simpleResolver.send(query);
                endTime = System.currentTimeMillis();
                if (type != 252) {
                    long startTime;
                    long endTime;
                    doQuery(sb, response, endTime - startTime);
                } else {
                    doAXFR(sb, response);
                }
                return sb.toString();
            } catch (Exception e4) {
                e = e4;
                i = arg;
                sb.append("### nslookup error : " + e);
                sb.append(HTTP.CRLF);
                e.printStackTrace();
                return sb.toString();
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
                    } catch (Exception e5) {
                        sb.append("Invalid address");
                        sb.append(HTTP.CRLF);
                        return sb.toString();
                    } catch (ArrayIndexOutOfBoundsException e6) {
                        break;
                    }
                    try {
                        simpleResolver.setLocalAddress(InetAddress.getByName(addrStr));
                        break;
                    } catch (ArrayIndexOutOfBoundsException e62) {
                        break;
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
                    }
                    sb.append("Unsupported EDNS level: " + edns);
                    sb.append(HTTP.CRLF);
                    return sb.toString();
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
                    }
                    sb.append("Invalid port");
                    sb.append(HTTP.CRLF);
                    return sb.toString();
                    break;
                case 'q':
                    printQuery = true;
                    break;
                case 't':
                    simpleResolver.setTCP(true);
                    break;
                default:
                    sb.append("Invalid option: ");
                    sb.append(argv[i]);
                    sb.append(HTTP.CRLF);
                    break;
            }
            i++;
        }
        res = simpleResolver;
        if (res != null) {
            System.out.print("res is null");
            simpleResolver = new SimpleResolver();
        } else {
            simpleResolver = res;
        }
        try {
            query = Message.newQuery(Record.newRecord(name, type, dclass));
            if (printQuery) {
                sb.append(query);
                sb.append(HTTP.CRLF);
            }
            startTime = System.currentTimeMillis();
            response = simpleResolver.send(query);
            endTime = System.currentTimeMillis();
            if (type != 252) {
                doAXFR(sb, response);
            } else {
                doQuery(sb, response, endTime - startTime);
            }
        } catch (Exception e7) {
            e = e7;
            sb.append("### nslookup error : " + e);
            sb.append(HTTP.CRLF);
            e.printStackTrace();
            return sb.toString();
        }
        return sb.toString();
    }

    public static String digHost(String host) {
        return digHost(new String[]{host});
    }
}
