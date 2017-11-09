package org.cybergarage.http;

import android.text.TextUtils;
import com.gala.android.dlna.sdk.stddmrcontroller.Util;
import com.gala.video.lib.share.common.configs.WebConstants;
import java.net.URL;
import org.cybergarage.soap.SOAP;

public class HTTP {
    public static final String CACHE_CONTROL = "Cache-Control";
    public static final String CALLBACK = "CALLBACK";
    public static final String CHARSET = "charset";
    public static final String CHUNKED = "Chunked";
    public static final String CLOSE = "close";
    public static final String CONNECTION = "Connection";
    public static final String CONTENT_LENGTH = "Content-Length";
    public static final String CONTENT_RANGE = "Content-Range";
    public static final String CONTENT_RANGE_BYTES = "bytes";
    public static final String CONTENT_TYPE = "Content-Type";
    public static final byte CR = (byte) 13;
    public static final String CRLF = "\r\n";
    public static final String DATE = "Date";
    public static final int DEFAULT_CHUNK_SIZE = 524288;
    public static final int DEFAULT_PORT = 80;
    public static final int DEFAULT_TIMEOUT = 30;
    public static final String DEVICEVERSION = "DEVICEVERSION";
    public static final String DIFFTIME = "DIFFTIME";
    public static final String DMCTIME = "DMCTIME";
    public static final String DMRTIME = "DMRTIME";
    public static final String EXT = "EXT";
    public static final String FILEMD5 = "FILEMD5";
    public static final String GET = "GET";
    public static final String GID = "GID";
    public static final String HEAD = "HEAD";
    public static final String HEADER_LINE_DELIM = " :";
    public static final String HEAD_SIZE = "HEAD-SIZE";
    public static final String HOST = "HOST";
    public static final String IGALACONNECTION = (Util.getTag(true) + CONNECTION);
    public static final String IGALADEVICE = (Util.getTag(true) + "DEVICE");
    public static final String IGALAPORT = (Util.getTag(true) + "PORT");
    public static final String IGALAUDPPORT = (Util.getTag(true) + "UDPPORT");
    public static final String IGALAVERSION = (Util.getTag(true) + "VERSION");
    public static final String KEEP_ALIVE = "Keep-Alive";
    public static final byte LF = (byte) 10;
    public static final String LOCATION = "Location";
    public static final String MAN = "MAN";
    public static final String MAXDELAYTIME = "MAXDELAYTIME";
    public static final String MAX_AGE = "max-age";
    public static final String MX = "MX";
    public static final String MYNAME = "MYNAME";
    public static final String M_SEARCH = "M-SEARCH";
    public static final String NOTIFY = "NOTIFY";
    public static final String NO_CACHE = "no-cache";
    public static final String NT = "NT";
    public static final String NTS = "NTS";
    public static final String POST = "POST";
    public static final String RANGE = "Range";
    public static final String REPLY = "REPLY";
    public static final String REQEST_LINE_DELIM = " ";
    public static final String SEQ = "SEQ";
    public static final String SERVER = "Server";
    public static final String SID = "SID";
    public static final String SOAP_ACTION = "SOAPACTION";
    public static final int SOCKET_REC_BUFFER_SIZE = 5120;
    public static final int SOCKET_SEND_BUFFER_SIZE = 5120;
    public static final String ST = "ST";
    public static final String STATUS_LINE_DELIM = " ";
    public static final byte[] ST_BYTES = "ST:".getBytes();
    public static final String SUBSCRIBE = "SUBSCRIBE";
    public static final String TAB = "\t";
    public static final String TIMEOUT = "TIMEOUT";
    public static final String TRANSFER_ENCODING = "Transfer-Encoding";
    public static final String UNSUBSCRIBE = "UNSUBSCRIBE";
    public static final String USN = "USN";
    public static final String VERSION = "1.1";
    public static final String VERSION_10 = "1.0";
    public static final String VERSION_11 = "1.1";
    private static int chunkSize = 524288;

    public static final boolean isAbsoluteURL(String urlStr) {
        try {
            URL url = new URL(urlStr);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static final String getHost(String urlStr) {
        if (TextUtils.isEmpty(urlStr)) {
            return "";
        }
        String[] strs = urlStr.split("/");
        if (strs.length < 3) {
            return "";
        }
        String[] host = strs[2].split(SOAP.DELIM);
        if (host.length < 2) {
            return "";
        }
        return host[0];
    }

    public static final int getPort(String urlStr) {
        if (TextUtils.isEmpty(urlStr)) {
            return 80;
        }
        String[] strs = urlStr.split("/");
        if (strs.length < 3) {
            return 80;
        }
        String[] host = strs[2].split(SOAP.DELIM);
        if (host.length < 2) {
            return 80;
        }
        return Integer.parseInt(host[1]);
    }

    public static final String getRequestHostURL(String host, int port) {
        return new StringBuilder(WebConstants.WEB_SITE_BASE_HTTP).append(host).append(SOAP.DELIM).append(port).toString();
    }

    public static final String toRelativeURL(String urlStr, boolean withParam) {
        String uri = urlStr;
        if (!isAbsoluteURL(urlStr)) {
            return (urlStr.length() <= 0 || urlStr.charAt(0) == '/') ? uri : "/" + urlStr;
        } else {
            try {
                URL url = new URL(urlStr);
                uri = url.getPath();
                if (withParam) {
                    String queryStr = url.getQuery();
                    if (!queryStr.equals("")) {
                        uri = new StringBuilder(String.valueOf(uri)).append("?").append(queryStr).toString();
                    }
                }
                if (uri.endsWith("/")) {
                    return uri.substring(0, uri.length() - 1);
                }
                return uri;
            } catch (Exception e) {
                return uri;
            }
        }
    }

    public static final String toRelativeURL(String urlStr) {
        return toRelativeURL(urlStr, true);
    }

    public static final String getAbsoluteURL(String baseURLStr, String relURlStr) {
        try {
            URL baseURL = new URL(baseURLStr);
            return baseURL.getProtocol() + "://" + baseURL.getHost() + SOAP.DELIM + baseURL.getPort() + toRelativeURL(relURlStr);
        } catch (Exception e) {
            return "";
        }
    }

    public static final void setChunkSize(int size) {
        chunkSize = size;
    }

    public static final int getChunkSize() {
        return chunkSize;
    }
}
