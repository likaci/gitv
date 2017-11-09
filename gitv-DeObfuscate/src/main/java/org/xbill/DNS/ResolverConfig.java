package org.xbill.DNS;

import com.gala.video.webview.utils.WebSDKConstants;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import org.cybergarage.soap.SOAP;

public class ResolverConfig {
    static Class class$java$lang$String;
    static Class class$org$xbill$DNS$ResolverConfig;
    private static ResolverConfig currentConfig;
    private int ndots = -1;
    private Name[] searchlist = null;
    private String[] servers = null;

    static {
        refresh();
    }

    public ResolverConfig() {
        if (!findProperty() && !findSunJVM()) {
            if (this.servers == null || this.searchlist == null) {
                String OS = System.getProperty("os.name");
                String vendor = System.getProperty("java.vendor");
                if (OS.indexOf("Windows") != -1) {
                    if (OS.indexOf("95") == -1 && OS.indexOf("98") == -1 && OS.indexOf("ME") == -1) {
                        findNT();
                    } else {
                        find95();
                    }
                } else if (OS.indexOf("NetWare") != -1) {
                    findNetware();
                } else if (vendor.indexOf("Android") != -1) {
                    findAndroid();
                } else {
                    findUnix();
                }
            }
        }
    }

    private void addServer(String server, List list) {
        if (!list.contains(server)) {
            if (Options.check("verbose")) {
                System.out.println(new StringBuffer().append("adding server ").append(server).toString());
            }
            list.add(server);
        }
    }

    private void addSearch(String search, List list) {
        if (Options.check("verbose")) {
            System.out.println(new StringBuffer().append("adding search ").append(search).toString());
        }
        try {
            Name name = Name.fromString(search, Name.root);
            if (!list.contains(name)) {
                list.add(name);
            }
        } catch (TextParseException e) {
        }
    }

    private int parseNdots(String token) {
        token = token.substring(6);
        try {
            int ndots = Integer.parseInt(token);
            if (ndots >= 0) {
                if (!Options.check("verbose")) {
                    return ndots;
                }
                System.out.println(new StringBuffer().append("setting ndots ").append(token).toString());
                return ndots;
            }
        } catch (NumberFormatException e) {
        }
        return -1;
    }

    private void configureFromLists(List lserver, List lsearch) {
        if (this.servers == null && lserver.size() > 0) {
            this.servers = (String[]) lserver.toArray(new String[0]);
        }
        if (this.searchlist == null && lsearch.size() > 0) {
            this.searchlist = (Name[]) lsearch.toArray(new Name[0]);
        }
    }

    private void configureNdots(int lndots) {
        if (this.ndots < 0 && lndots > 0) {
            this.ndots = lndots;
        }
    }

    private boolean findProperty() {
        StringTokenizer st;
        List lserver = new ArrayList(0);
        List lsearch = new ArrayList(0);
        String prop = System.getProperty("dns.server");
        if (prop != null) {
            st = new StringTokenizer(prop, ",");
            while (st.hasMoreTokens()) {
                addServer(st.nextToken(), lserver);
            }
        }
        prop = System.getProperty("dns.search");
        if (prop != null) {
            st = new StringTokenizer(prop, ",");
            while (st.hasMoreTokens()) {
                addSearch(st.nextToken(), lsearch);
            }
        }
        configureFromLists(lserver, lsearch);
        if (this.servers == null || this.searchlist == null) {
            return false;
        }
        return true;
    }

    private boolean findSunJVM() {
        List lserver = new ArrayList(0);
        List lsearch = new ArrayList(0);
        try {
            Class[] noClasses = new Class[0];
            Object[] noObjects = new Object[0];
            Class resConfClass = Class.forName("sun.net.dns.ResolverConfiguration");
            Object resConf = resConfClass.getDeclaredMethod("open", noClasses).invoke(null, noObjects);
            List<String> lserver_tmp = (List) resConfClass.getMethod("nameservers", noClasses).invoke(resConf, noObjects);
            List<String> lsearch_tmp = (List) resConfClass.getMethod("searchlist", noClasses).invoke(resConf, noObjects);
            if (lserver_tmp.size() == 0) {
                return false;
            }
            if (lserver_tmp.size() > 0) {
                for (String addServer : lserver_tmp) {
                    addServer(addServer, lserver);
                }
            }
            if (lsearch_tmp.size() > 0) {
                for (String addServer2 : lsearch_tmp) {
                    addSearch(addServer2, lsearch);
                }
            }
            configureFromLists(lserver, lsearch);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void findResolvConf(String file) {
        try {
            InputStream in = new FileInputStream(file);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            List lserver = new ArrayList(0);
            List lsearch = new ArrayList(0);
            int lndots = -1;
            while (true) {
                String line = br.readLine();
                if (line == null) {
                    break;
                } else if (line.startsWith("nameserver")) {
                    st = new StringTokenizer(line);
                    st.nextToken();
                    addServer(st.nextToken(), lserver);
                } else {
                    try {
                        if (line.startsWith(WebSDKConstants.PARAM_KEY_DOMAIN)) {
                            st = new StringTokenizer(line);
                            st.nextToken();
                            if (st.hasMoreTokens() && lsearch.isEmpty()) {
                                addSearch(st.nextToken(), lsearch);
                            }
                        } else if (line.startsWith("search")) {
                            if (!lsearch.isEmpty()) {
                                lsearch.clear();
                            }
                            st = new StringTokenizer(line);
                            st.nextToken();
                            while (st.hasMoreTokens()) {
                                addSearch(st.nextToken(), lsearch);
                            }
                        } else if (line.startsWith("options")) {
                            st = new StringTokenizer(line);
                            st.nextToken();
                            while (st.hasMoreTokens()) {
                                String token = st.nextToken();
                                if (token.startsWith("ndots:")) {
                                    lndots = parseNdots(token);
                                }
                            }
                        }
                    } catch (IOException e) {
                    }
                }
            }
            br.close();
            configureFromLists(lserver, lsearch);
            configureNdots(lndots);
            InputStream inputStream = in;
        } catch (FileNotFoundException e2) {
        }
    }

    private void findUnix() {
        findResolvConf("/etc/resolv.conf");
    }

    private void findNetware() {
        findResolvConf("sys:/etc/resolv.cfg");
    }

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError().initCause(x1);
        }
    }

    private void findWin(InputStream in, Locale locale) {
        Class class$;
        ResourceBundle res;
        if (class$org$xbill$DNS$ResolverConfig == null) {
            class$ = class$("org.xbill.DNS.ResolverConfig");
            class$org$xbill$DNS$ResolverConfig = class$;
        } else {
            class$ = class$org$xbill$DNS$ResolverConfig;
        }
        String resPackageName = new StringBuffer().append(class$.getPackage().getName()).append(".windows.DNSServer").toString();
        if (locale != null) {
            res = ResourceBundle.getBundle(resPackageName, locale);
        } else {
            res = ResourceBundle.getBundle(resPackageName);
        }
        String host_name = res.getString("host_name");
        String primary_dns_suffix = res.getString("primary_dns_suffix");
        String dns_suffix = res.getString("dns_suffix");
        String dns_servers = res.getString("dns_servers");
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        List lserver = new ArrayList();
        List lsearch = new ArrayList();
        boolean readingServers = false;
        boolean readingSearches = false;
        while (true) {
            String line = br.readLine();
            if (line != null) {
                StringTokenizer stringTokenizer = new StringTokenizer(line);
                if (stringTokenizer.hasMoreTokens()) {
                    try {
                        String s = stringTokenizer.nextToken();
                        if (line.indexOf(SOAP.DELIM) != -1) {
                            readingServers = false;
                            readingSearches = false;
                        }
                        if (line.indexOf(host_name) != -1) {
                            while (stringTokenizer.hasMoreTokens()) {
                                s = stringTokenizer.nextToken();
                            }
                            try {
                                if (Name.fromString(s, null).labels() != 1) {
                                    addSearch(s, lsearch);
                                }
                            } catch (TextParseException e) {
                            }
                        } else if (line.indexOf(primary_dns_suffix) != -1) {
                            while (stringTokenizer.hasMoreTokens()) {
                                s = stringTokenizer.nextToken();
                            }
                            if (!s.equals(SOAP.DELIM)) {
                                addSearch(s, lsearch);
                                readingSearches = true;
                            }
                        } else if (readingSearches || line.indexOf(dns_suffix) != -1) {
                            while (stringTokenizer.hasMoreTokens()) {
                                s = stringTokenizer.nextToken();
                            }
                            if (!s.equals(SOAP.DELIM)) {
                                addSearch(s, lsearch);
                                readingSearches = true;
                            }
                        } else if (readingServers || line.indexOf(dns_servers) != -1) {
                            while (stringTokenizer.hasMoreTokens()) {
                                s = stringTokenizer.nextToken();
                            }
                            if (!s.equals(SOAP.DELIM)) {
                                addServer(s, lserver);
                                readingServers = true;
                            }
                        }
                    } catch (IOException e2) {
                        return;
                    }
                }
                readingServers = false;
                readingSearches = false;
            } else {
                configureFromLists(lserver, lsearch);
                return;
            }
        }
    }

    private void findWin(InputStream in) {
        int bufSize = Integer.getInteger("org.xbill.DNS.windows.parse.buffer", 8192).intValue();
        BufferedInputStream b = new BufferedInputStream(in, bufSize);
        b.mark(bufSize);
        findWin(b, null);
        if (this.servers == null) {
            try {
                b.reset();
                findWin(b, new Locale("", ""));
            } catch (IOException e) {
            }
        }
    }

    private void find95() {
        String s = "winipcfg.out";
        try {
            Runtime.getRuntime().exec(new StringBuffer().append("winipcfg /all /batch ").append(s).toString()).waitFor();
            findWin(new FileInputStream(new File(s)));
            new File(s).delete();
        } catch (Exception e) {
        }
    }

    private void findNT() {
        try {
            Process p = Runtime.getRuntime().exec("ipconfig /all");
            findWin(p.getInputStream());
            p.destroy();
        } catch (Exception e) {
        }
    }

    private void findAndroid() {
        String re1 = "^\\d+(\\.\\d+){3}$";
        String re2 = "^[0-9a-f]+(:[0-9a-f]*)+:[0-9a-f]+$";
        ArrayList lserver = new ArrayList();
        ArrayList lsearch = new ArrayList();
        try {
            Class class$;
            Class SystemProperties = Class.forName("android.os.SystemProperties");
            String str = "get";
            Class[] clsArr = new Class[1];
            if (class$java$lang$String == null) {
                class$ = class$("java.lang.String");
                class$java$lang$String = class$;
            } else {
                class$ = class$java$lang$String;
            }
            clsArr[0] = class$;
            Method method = SystemProperties.getMethod(str, clsArr);
            String[] netdns = new String[]{"net.dns1", "net.dns2", "net.dns3", "net.dns4"};
            for (int i = 0; i < netdns.length; i++) {
                String v = (String) method.invoke(null, new Object[]{netdns[i]});
                if (v != null && ((v.matches("^\\d+(\\.\\d+){3}$") || v.matches("^[0-9a-f]+(:[0-9a-f]*)+:[0-9a-f]+$")) && !lserver.contains(v))) {
                    lserver.add(v);
                }
            }
        } catch (Exception e) {
        }
        configureFromLists(lserver, lsearch);
    }

    public String[] servers() {
        return this.servers;
    }

    public String server() {
        if (this.servers == null) {
            return null;
        }
        return this.servers[0];
    }

    public Name[] searchPath() {
        return this.searchlist;
    }

    public int ndots() {
        if (this.ndots < 0) {
            return 1;
        }
        return this.ndots;
    }

    public static synchronized ResolverConfig getCurrentConfig() {
        ResolverConfig resolverConfig;
        synchronized (ResolverConfig.class) {
            resolverConfig = currentConfig;
        }
        return resolverConfig;
    }

    public static void refresh() {
        ResolverConfig newConfig = new ResolverConfig();
        Class class$;
        if (class$org$xbill$DNS$ResolverConfig == null) {
            class$ = class$("org.xbill.DNS.ResolverConfig");
            class$org$xbill$DNS$ResolverConfig = class$;
        } else {
            class$ = class$org$xbill$DNS$ResolverConfig;
        }
        synchronized (r1) {
            currentConfig = newConfig;
        }
    }
}
