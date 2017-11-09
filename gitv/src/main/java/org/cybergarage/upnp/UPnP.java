package org.cybergarage.upnp;

import com.gala.android.dlna.sdk.stddmrcontroller.Util;
import org.cybergarage.net.HostInterface;
import org.cybergarage.soap.SOAP;
import org.cybergarage.upnp.ssdp.SSDP;
import org.cybergarage.util.Debug;
import org.cybergarage.xml.Parser;
import org.xbill.DNS.Message;

public class UPnP {
    public static final int DEFAULT_EXPIRED_DEVICE_EXTRA_TIME = 30;
    public static final int DEFAULT_TTL = 4;
    public static final String INMPR03 = "INMPR03";
    public static final int INMPR03_DISCOVERY_OVER_WIRELESS_COUNT = 5;
    public static final String INMPR03_VERSION = "1.0";
    public static final String NAME = (Util.getTag(true) + Util.FUNCTION_TAG_DLNA);
    public static final int SERVER_RETRY_COUNT = 100;
    public static final int USE_IPV6_ADMINISTRATIVE_SCOPE = 5;
    public static final int USE_IPV6_GLOBAL_SCOPE = 7;
    public static final int USE_IPV6_LINK_LOCAL_SCOPE = 3;
    public static final int USE_IPV6_SITE_LOCAL_SCOPE = 6;
    public static final int USE_IPV6_SUBNET_SCOPE = 4;
    public static final int USE_LOOPBACK_ADDR = 2;
    public static final int USE_ONLY_IPV4_ADDR = 9;
    public static final int USE_ONLY_IPV6_ADDR = 1;
    public static final int USE_SSDP_SEARCHRESPONSE_MULTIPLE_INTERFACES = 8;
    public static final String VERSION = "1.0";
    public static final String XML_CLASS_PROPERTTY = "cyberlink.upnp.xml.parser";
    public static final String XML_DECLARATION = "<?xml version=\"1.0\" encoding=\"utf-8\"?>";
    private static int timeToLive = 4;
    private static Parser xmlParser;

    static {
        setTimeToLive(4);
    }

    public static final String getServerName() {
        String osName = System.getProperty("os.name");
        return new StringBuilder(String.valueOf(osName)).append("/").append(System.getProperty("os.version")).append(" UPnP/1.0 ").append(NAME).append("/").append("1.0").toString();
    }

    public static final void setEnable(int value) {
        switch (value) {
            case 1:
                HostInterface.USE_ONLY_IPV6_ADDR = true;
                return;
            case 2:
                HostInterface.USE_LOOPBACK_ADDR = true;
                return;
            case 3:
                SSDP.setIPv6Address(SSDP.IPV6_LINK_LOCAL_ADDRESS);
                return;
            case 4:
                SSDP.setIPv6Address(SSDP.IPV6_SUBNET_ADDRESS);
                return;
            case 5:
                SSDP.setIPv6Address(SSDP.IPV6_ADMINISTRATIVE_ADDRESS);
                return;
            case 6:
                SSDP.setIPv6Address(SSDP.IPV6_SITE_LOCAL_ADDRESS);
                return;
            case 7:
                SSDP.setIPv6Address(SSDP.IPV6_GLOBAL_ADDRESS);
                return;
            case 9:
                HostInterface.USE_ONLY_IPV4_ADDR = true;
                return;
            default:
                return;
        }
    }

    public static final void setDisable(int value) {
        switch (value) {
            case 1:
                HostInterface.USE_ONLY_IPV6_ADDR = false;
                return;
            case 2:
                HostInterface.USE_LOOPBACK_ADDR = false;
                return;
            case 9:
                HostInterface.USE_ONLY_IPV4_ADDR = false;
                return;
            default:
                return;
        }
    }

    public static final boolean isEnabled(int value) {
        switch (value) {
            case 1:
                return HostInterface.USE_ONLY_IPV6_ADDR;
            case 2:
                return HostInterface.USE_LOOPBACK_ADDR;
            case 9:
                return HostInterface.USE_ONLY_IPV4_ADDR;
            default:
                return false;
        }
    }

    private static final String toUUID(int seed) {
        String id = Integer.toString(Message.MAXLENGTH & seed, 16);
        int idLen = id.length();
        String uuid = "";
        for (int n = 0; n < 4 - idLen; n++) {
            uuid = new StringBuilder(String.valueOf(uuid)).append("0").toString();
        }
        return new StringBuilder(String.valueOf(uuid)).append(id).toString();
    }

    public static final String createUUID() {
        long time1 = System.currentTimeMillis();
        long time2 = (long) (((double) System.currentTimeMillis()) * Math.random());
        return new StringBuilder(String.valueOf(toUUID((int) (time1 & 65535)))).append("-").append(toUUID(((int) ((time1 >> 32) | 40960)) & Message.MAXLENGTH)).append("-").append(toUUID((int) (time2 & 65535))).append("-").append(toUUID(((int) ((time2 >> 32) | 57344)) & Message.MAXLENGTH)).toString();
    }

    public static final void setXMLParser(Parser parser) {
        xmlParser = parser;
        SOAP.setXMLParser(parser);
    }

    public static final Parser getXMLParser() {
        if (xmlParser == null) {
            xmlParser = loadDefaultXMLParser();
            if (xmlParser == null) {
                throw new RuntimeException("No XML parser defined. And unable to laod any. \nTry to invoke UPnP.setXMLParser before UPnP.getXMLParser");
            }
            SOAP.setXMLParser(xmlParser);
        }
        return xmlParser;
    }

    private static Parser loadDefaultXMLParser() {
        String[] parserClass = new String[]{System.getProperty(XML_CLASS_PROPERTTY), "org.cybergarage.xml.parser.XmlPullParser", "org.cybergarage.xml.parser.JaxpParser", "org.cybergarage.xml.parser.kXML2Parser", "org.cybergarage.xml.parser.XercesParser"};
        for (int i = 0; i < parserClass.length; i++) {
            if (parserClass[i] != null) {
                try {
                    return (Parser) Class.forName(parserClass[i]).newInstance();
                } catch (Throwable e) {
                    Debug.warning("Unable to load " + parserClass[i] + " as XMLParser due to " + e);
                }
            }
        }
        return null;
    }

    public static final void setTimeToLive(int value) {
        timeToLive = value;
    }

    public static final int getTimeToLive() {
        return timeToLive;
    }

    public static final void initialize() {
    }
}
