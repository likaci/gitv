package org.xbill.DNS;

import com.gala.video.webview.utils.WebSDKConstants;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.xbill.DNS.Tokenizer.Token;

public class WKSRecord extends Record {
    private static final long serialVersionUID = -9104259763909119805L;
    private byte[] address;
    private int protocol;
    private int[] services;

    public static class Protocol {
        public static final int ARGUS = 13;
        public static final int BBN_RCC_MON = 10;
        public static final int BR_SAT_MON = 76;
        public static final int CFTP = 62;
        public static final int CHAOS = 16;
        public static final int DCN_MEAS = 19;
        public static final int EGP = 8;
        public static final int EMCON = 14;
        public static final int GGP = 3;
        public static final int HMP = 20;
        public static final int ICMP = 1;
        public static final int IGMP = 2;
        public static final int IGP = 9;
        public static final int IPCV = 71;
        public static final int IPPC = 67;
        public static final int IRTP = 28;
        public static final int ISO_TP4 = 29;
        public static final int LEAF_1 = 25;
        public static final int LEAF_2 = 26;
        public static final int MERIT_INP = 32;
        public static final int MFE_NSP = 31;
        public static final int MIT_SUBNET = 65;
        public static final int MUX = 18;
        public static final int NETBLT = 30;
        public static final int NVP_II = 11;
        public static final int PRM = 21;
        public static final int PUP = 12;
        public static final int RDP = 27;
        public static final int RVD = 66;
        public static final int SAT_EXPAK = 64;
        public static final int SAT_MON = 69;
        public static final int SEP = 33;
        public static final int ST = 5;
        public static final int TCP = 6;
        public static final int TRUNK_1 = 23;
        public static final int TRUNK_2 = 24;
        public static final int UCL = 7;
        public static final int UDP = 17;
        public static final int WB_EXPAK = 79;
        public static final int WB_MON = 78;
        public static final int XNET = 15;
        public static final int XNS_IDP = 22;
        private static Mnemonic protocols = new Mnemonic("IP protocol", 3);

        private Protocol() {
        }

        static {
            protocols.setMaximum(255);
            protocols.setNumericAllowed(true);
            protocols.add(1, "icmp");
            protocols.add(2, "igmp");
            protocols.add(3, "ggp");
            protocols.add(5, "st");
            protocols.add(6, "tcp");
            protocols.add(7, "ucl");
            protocols.add(8, "egp");
            protocols.add(9, "igp");
            protocols.add(10, "bbn-rcc-mon");
            protocols.add(11, "nvp-ii");
            protocols.add(12, "pup");
            protocols.add(13, "argus");
            protocols.add(14, "emcon");
            protocols.add(15, "xnet");
            protocols.add(16, "chaos");
            protocols.add(17, "udp");
            protocols.add(18, "mux");
            protocols.add(19, "dcn-meas");
            protocols.add(20, "hmp");
            protocols.add(21, "prm");
            protocols.add(22, "xns-idp");
            protocols.add(23, "trunk-1");
            protocols.add(24, "trunk-2");
            protocols.add(25, "leaf-1");
            protocols.add(26, "leaf-2");
            protocols.add(27, "rdp");
            protocols.add(28, "irtp");
            protocols.add(29, "iso-tp4");
            protocols.add(30, "netblt");
            protocols.add(31, "mfe-nsp");
            protocols.add(32, "merit-inp");
            protocols.add(33, "sep");
            protocols.add(62, "cftp");
            protocols.add(64, "sat-expak");
            protocols.add(65, "mit-subnet");
            protocols.add(66, "rvd");
            protocols.add(67, "ippc");
            protocols.add(69, "sat-mon");
            protocols.add(71, "ipcv");
            protocols.add(76, "br-sat-mon");
            protocols.add(78, "wb-mon");
            protocols.add(79, "wb-expak");
        }

        public static String string(int type) {
            return protocols.getText(type);
        }

        public static int value(String s) {
            return protocols.getValue(s);
        }
    }

    public static class Service {
        public static final int AUTH = 113;
        public static final int BL_IDM = 142;
        public static final int BOOTPC = 68;
        public static final int BOOTPS = 67;
        public static final int CHARGEN = 19;
        public static final int CISCO_FNA = 130;
        public static final int CISCO_SYS = 132;
        public static final int CISCO_TNA = 131;
        public static final int CSNET_NS = 105;
        public static final int DAYTIME = 13;
        public static final int DCP = 93;
        public static final int DISCARD = 9;
        public static final int DOMAIN = 53;
        public static final int DSP = 33;
        public static final int ECHO = 7;
        public static final int EMFIS_CNTL = 141;
        public static final int EMFIS_DATA = 140;
        public static final int ERPC = 121;
        public static final int FINGER = 79;
        public static final int FTP = 21;
        public static final int FTP_DATA = 20;
        public static final int GRAPHICS = 41;
        public static final int HOSTNAME = 101;
        public static final int HOSTS2_NS = 81;
        public static final int INGRES_NET = 134;
        public static final int ISI_GL = 55;
        public static final int ISO_TSAP = 102;
        public static final int LA_MAINT = 51;
        public static final int LINK = 245;
        public static final int LOCUS_CON = 127;
        public static final int LOCUS_MAP = 125;
        public static final int LOC_SRV = 135;
        public static final int LOGIN = 49;
        public static final int METAGRAM = 99;
        public static final int MIT_DOV = 91;
        public static final int MPM = 45;
        public static final int MPM_FLAGS = 44;
        public static final int MPM_SND = 46;
        public static final int MSG_AUTH = 31;
        public static final int MSG_ICP = 29;
        public static final int NAMESERVER = 42;
        public static final int NETBIOS_DGM = 138;
        public static final int NETBIOS_NS = 137;
        public static final int NETBIOS_SSN = 139;
        public static final int NETRJS_1 = 71;
        public static final int NETRJS_2 = 72;
        public static final int NETRJS_3 = 73;
        public static final int NETRJS_4 = 74;
        public static final int NICNAME = 43;
        public static final int NI_FTP = 47;
        public static final int NI_MAIL = 61;
        public static final int NNTP = 119;
        public static final int NSW_FE = 27;
        public static final int NTP = 123;
        public static final int POP_2 = 109;
        public static final int PROFILE = 136;
        public static final int PWDGEN = 129;
        public static final int QUOTE = 17;
        public static final int RJE = 5;
        public static final int RLP = 39;
        public static final int RTELNET = 107;
        public static final int SFTP = 115;
        public static final int SMTP = 25;
        public static final int STATSRV = 133;
        public static final int SUNRPC = 111;
        public static final int SUPDUP = 95;
        public static final int SUR_MEAS = 243;
        public static final int SU_MIT_TG = 89;
        public static final int SWIFT_RVF = 97;
        public static final int TACACS_DS = 65;
        public static final int TACNEWS = 98;
        public static final int TELNET = 23;
        public static final int TFTP = 69;
        public static final int TIME = 37;
        public static final int USERS = 11;
        public static final int UUCP_PATH = 117;
        public static final int VIA_FTP = 63;
        public static final int X400 = 103;
        public static final int X400_SND = 104;
        private static Mnemonic services = new Mnemonic("TCP/UDP service", 3);

        private Service() {
        }

        static {
            services.setMaximum(Message.MAXLENGTH);
            services.setNumericAllowed(true);
            services.add(5, "rje");
            services.add(7, "echo");
            services.add(9, "discard");
            services.add(11, "users");
            services.add(13, "daytime");
            services.add(17, "quote");
            services.add(19, "chargen");
            services.add(20, "ftp-data");
            services.add(21, "ftp");
            services.add(23, "telnet");
            services.add(25, "smtp");
            services.add(27, "nsw-fe");
            services.add(29, "msg-icp");
            services.add(31, "msg-auth");
            services.add(33, "dsp");
            services.add(37, "time");
            services.add(39, "rlp");
            services.add(41, "graphics");
            services.add(42, "nameserver");
            services.add(43, "nicname");
            services.add(44, "mpm-flags");
            services.add(45, "mpm");
            services.add(46, "mpm-snd");
            services.add(47, "ni-ftp");
            services.add(49, "login");
            services.add(51, "la-maint");
            services.add(53, WebSDKConstants.PARAM_KEY_DOMAIN);
            services.add(55, "isi-gl");
            services.add(61, "ni-mail");
            services.add(63, "via-ftp");
            services.add(65, "tacacs-ds");
            services.add(67, "bootps");
            services.add(68, "bootpc");
            services.add(69, "tftp");
            services.add(71, "netrjs-1");
            services.add(72, "netrjs-2");
            services.add(73, "netrjs-3");
            services.add(74, "netrjs-4");
            services.add(79, "finger");
            services.add(81, "hosts2-ns");
            services.add(89, "su-mit-tg");
            services.add(91, "mit-dov");
            services.add(93, "dcp");
            services.add(95, "supdup");
            services.add(97, "swift-rvf");
            services.add(98, "tacnews");
            services.add(99, "metagram");
            services.add(101, "hostname");
            services.add(102, "iso-tsap");
            services.add(103, "x400");
            services.add(104, "x400-snd");
            services.add(105, "csnet-ns");
            services.add(107, "rtelnet");
            services.add(109, "pop-2");
            services.add(111, "sunrpc");
            services.add(113, "auth");
            services.add(115, "sftp");
            services.add(UUCP_PATH, "uucp-path");
            services.add(NNTP, "nntp");
            services.add(ERPC, "erpc");
            services.add(NTP, "ntp");
            services.add(LOCUS_MAP, "locus-map");
            services.add(LOCUS_CON, "locus-con");
            services.add(PWDGEN, "pwdgen");
            services.add(CISCO_FNA, "cisco-fna");
            services.add(CISCO_TNA, "cisco-tna");
            services.add(CISCO_SYS, "cisco-sys");
            services.add(STATSRV, "statsrv");
            services.add(INGRES_NET, "ingres-net");
            services.add(LOC_SRV, "loc-srv");
            services.add(PROFILE, "profile");
            services.add(NETBIOS_NS, "netbios-ns");
            services.add(NETBIOS_DGM, "netbios-dgm");
            services.add(NETBIOS_SSN, "netbios-ssn");
            services.add(EMFIS_DATA, "emfis-data");
            services.add(EMFIS_CNTL, "emfis-cntl");
            services.add(BL_IDM, "bl-idm");
            services.add(243, "sur-meas");
            services.add(LINK, "link");
        }

        public static String string(int type) {
            return services.getText(type);
        }

        public static int value(String s) {
            return services.getValue(s);
        }
    }

    WKSRecord() {
    }

    Record getObject() {
        return new WKSRecord();
    }

    public WKSRecord(Name name, int dclass, long ttl, InetAddress address, int protocol, int[] services) {
        super(name, 11, dclass, ttl);
        if (Address.familyOf(address) != 1) {
            throw new IllegalArgumentException("invalid IPv4 address");
        }
        this.address = address.getAddress();
        this.protocol = Record.checkU8("protocol", protocol);
        for (int checkU16 : services) {
            Record.checkU16(org.cybergarage.upnp.Service.ELEM_NAME, checkU16);
        }
        this.services = new int[services.length];
        System.arraycopy(services, 0, this.services, 0, services.length);
        Arrays.sort(this.services);
    }

    void rrFromWire(DNSInput in) throws IOException {
        int i;
        this.address = in.readByteArray(4);
        this.protocol = in.readU8();
        byte[] array = in.readByteArray();
        List list = new ArrayList();
        for (i = 0; i < array.length; i++) {
            for (int j = 0; j < 8; j++) {
                if (((1 << (7 - j)) & (array[i] & 255)) != 0) {
                    list.add(new Integer((i * 8) + j));
                }
            }
        }
        this.services = new int[list.size()];
        for (i = 0; i < list.size(); i++) {
            this.services[i] = ((Integer) list.get(i)).intValue();
        }
    }

    void rdataFromString(Tokenizer st, Name origin) throws IOException {
        this.address = Address.toByteArray(st.getString(), 1);
        if (this.address == null) {
            throw st.exception("invalid address");
        }
        String s = st.getString();
        this.protocol = Protocol.value(s);
        if (this.protocol < 0) {
            throw st.exception(new StringBuffer().append("Invalid IP protocol: ").append(s).toString());
        }
        List list = new ArrayList();
        while (true) {
            Token t = st.get();
            if (!t.isString()) {
                break;
            }
            int service = Service.value(t.value);
            if (service < 0) {
                throw st.exception(new StringBuffer().append("Invalid TCP/UDP service: ").append(t.value).toString());
            }
            list.add(new Integer(service));
        }
        st.unget();
        this.services = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            this.services[i] = ((Integer) list.get(i)).intValue();
        }
    }

    String rrToString() {
        StringBuffer sb = new StringBuffer();
        sb.append(Address.toDottedQuad(this.address));
        sb.append(" ");
        sb.append(this.protocol);
        for (int append : this.services) {
            sb.append(new StringBuffer().append(" ").append(append).toString());
        }
        return sb.toString();
    }

    public InetAddress getAddress() {
        try {
            return InetAddress.getByAddress(this.address);
        } catch (UnknownHostException e) {
            return null;
        }
    }

    public int getProtocol() {
        return this.protocol;
    }

    public int[] getServices() {
        return this.services;
    }

    void rrToWire(DNSOutput out, Compression c, boolean canonical) {
        out.writeByteArray(this.address);
        out.writeU8(this.protocol);
        byte[] array = new byte[((this.services[this.services.length - 1] / 8) + 1)];
        for (int port : this.services) {
            int i = port / 8;
            array[i] = (byte) (array[i] | (1 << (7 - (port % 8))));
        }
        out.writeByteArray(array);
    }
}
