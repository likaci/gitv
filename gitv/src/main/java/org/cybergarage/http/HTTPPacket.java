package org.cybergarage.http;

import com.gala.android.dlna.sdk.mediarenderer.service.infor.AVTransportConstStr;
import com.gala.video.app.epg.ui.albumlist.utils.AlbumEnterFactory;
import com.gala.video.lib.share.ifimpl.netdiagnose.NetDiagnoseCheckTools;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.StringTokenizer;
import java.util.Vector;
import org.cybergarage.net.HostInterface;
import org.cybergarage.soap.SOAP;
import org.cybergarage.upnp.std.av.server.object.SearchCriteria;
import org.cybergarage.util.Debug;
import org.cybergarage.util.StringUtil;

public class HTTPPacket {
    private byte[] content;
    private InputStream contentInput;
    private String firstLine;
    private Vector httpHeaderList;
    private boolean isKeepAlive;
    private boolean isSingleSend;
    private String tempContent;
    private String version;

    public HTTPPacket() {
        this.isSingleSend = false;
        this.isKeepAlive = false;
        this.firstLine = "";
        this.httpHeaderList = new Vector();
        this.content = new byte[0];
        this.tempContent = "";
        this.contentInput = null;
        setVersion("1.1");
        setContentInputStream(null);
    }

    public HTTPPacket(HTTPPacket httpPacket) {
        this.isSingleSend = false;
        this.isKeepAlive = false;
        this.firstLine = "";
        this.httpHeaderList = new Vector();
        this.content = new byte[0];
        this.tempContent = "";
        this.contentInput = null;
        setVersion("1.1");
        set(httpPacket);
        setContentInputStream(null);
    }

    public HTTPPacket(InputStream in) {
        this.isSingleSend = false;
        this.isKeepAlive = false;
        this.firstLine = "";
        this.httpHeaderList = new Vector();
        this.content = new byte[0];
        this.tempContent = "";
        this.contentInput = null;
        setVersion("1.1");
        set(in);
        setContentInputStream(null);
    }

    public void init() {
        setFirstLine("");
        clearHeaders();
        setContent(new byte[0], false);
        setContentInputStream(null);
    }

    public void quicklyInit() {
        setContent(new byte[0], false);
    }

    public void setVersion(String ver) {
        this.version = ver;
    }

    public String getVersion() {
        return this.version;
    }

    private String readLine(BufferedInputStream in) throws IOException {
        ByteArrayOutputStream lineBuf = new ByteArrayOutputStream();
        byte[] readBuf = new byte[1];
        int readLen = in.read(readBuf);
        if (readLen == -1) {
            return null;
        }
        while (readLen > 0) {
            if (readBuf[0] == (byte) 10) {
                break;
            }
            if (readBuf[0] != (byte) 13) {
                lineBuf.write(readBuf[0]);
            }
            readLen = in.read(readBuf);
        }
        return lineBuf.toString();
    }

    protected boolean set(InputStream in, boolean onlyHeaders) {
        try {
            BufferedInputStream bufferedInputStream = new BufferedInputStream(in);
            String firstLine = readLine(bufferedInputStream);
            if (firstLine == null || firstLine.length() <= 0) {
                return false;
            }
            String headerLine;
            HTTPHeader header;
            setFirstLine(firstLine);
            if (new HTTPStatus(firstLine).getStatusCode() == 100) {
                headerLine = readLine(bufferedInputStream);
                while (headerLine != null && headerLine.length() > 0) {
                    header = new HTTPHeader(headerLine);
                    if (header.hasName()) {
                        setHeader(header);
                    }
                    headerLine = readLine(bufferedInputStream);
                }
                String actualFirstLine = readLine(bufferedInputStream);
                if (actualFirstLine == null || actualFirstLine.length() <= 0) {
                    return true;
                }
                setFirstLine(actualFirstLine);
            }
            headerLine = readLine(bufferedInputStream);
            while (headerLine != null && headerLine.length() > 0) {
                header = new HTTPHeader(headerLine);
                if (header.hasName()) {
                    setHeader(header);
                }
                headerLine = readLine(bufferedInputStream);
            }
            if (onlyHeaders) {
                setContent("", false);
                return true;
            }
            String chunkSizeLine;
            long contentLen;
            boolean isChunkedRequest = isChunked();
            if (isChunkedRequest) {
                chunkSizeLine = readLine(bufferedInputStream);
                contentLen = chunkSizeLine != null ? Long.parseLong(chunkSizeLine.trim(), 16) : 0;
            } else {
                contentLen = getContentLength();
            }
            ByteArrayOutputStream contentBuf = new ByteArrayOutputStream();
            while (0 < contentLen) {
                long j;
                int chunkSize = HTTP.getChunkSize();
                if (contentLen > ((long) chunkSize)) {
                    j = (long) chunkSize;
                } else {
                    j = contentLen;
                }
                byte[] readBuf = new byte[((int) j)];
                long readCnt = 0;
                while (readCnt < contentLen) {
                    long bufReadLen = contentLen - readCnt;
                    if (((long) chunkSize) < bufReadLen) {
                        bufReadLen = (long) chunkSize;
                    }
                    int readLen = bufferedInputStream.read(readBuf, 0, (int) bufReadLen);
                    if (readLen < 0) {
                        break;
                    }
                    contentBuf.write(readBuf, 0, readLen);
                    readCnt += (long) readLen;
                    if (readCnt > 2000 && contentLen > readCnt && contentBuf.toString().contains("pl.youku.com") && contentBuf.toString().contains(AVTransportConstStr.SETAVTRANSPORTURI)) {
                        Debug.message("Workaround Youku again!");
                        Debug.message("contentLen = " + contentLen);
                        Debug.message("readCnt = " + readCnt);
                        break;
                    }
                }
                if (isChunkedRequest) {
                    long skipLen = 0;
                    do {
                        long skipCnt = bufferedInputStream.skip(((long) HTTP.CRLF.length()) - skipLen);
                        if (skipCnt < 0) {
                            break;
                        }
                        skipLen += skipCnt;
                    } while (skipLen < ((long) HTTP.CRLF.length()));
                    chunkSizeLine = readLine(bufferedInputStream);
                    contentLen = Long.parseLong(new String(chunkSizeLine.getBytes(), 0, chunkSizeLine.length() - 2), 16);
                } else {
                    contentLen = 0;
                }
            }
            setContent(contentBuf.toByteArray(), false);
            return true;
        } catch (Exception e) {
            Debug.warning(e);
            e.printStackTrace();
            return false;
        }
    }

    protected boolean set(InputStream in) {
        return set(in, false);
    }

    protected boolean set(HTTPSocket httpSock) {
        return set(httpSock.getInputStream());
    }

    protected void set(HTTPPacket httpPacket) {
        setFirstLine(httpPacket.getFirstLine());
        clearHeaders();
        int nHeaders = httpPacket.getNHeaders();
        for (int n = 0; n < nHeaders; n++) {
            addHeader(httpPacket.getHeader(n));
        }
        setContent(httpPacket.getContent());
    }

    public boolean read(HTTPSocket httpSock) {
        init();
        return set(httpSock);
    }

    private String[] read(InputStream in) {
        ArrayList<String> list = new ArrayList();
        byte[] buffer = new byte[1];
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int len;
        do {
            len = in.read(buffer);
            if (buffer[0] == (byte) 35) {
                list.add(out.toString());
                out.reset();
            } else {
                try {
                    if (buffer[0] == (byte) 10) {
                        list.add(out.toString());
                        break;
                    }
                    out.write(buffer);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } while (len > -1);
        out.close();
        return (String[]) list.toArray();
    }

    public boolean readQuickly(BufferedReader in) {
        quicklyInit();
        if (in == null) {
            return false;
        }
        try {
            String line = in.readLine();
            Debug.message("Qimo quick message received [TCP]: " + line);
            if (line == null || line.length() <= 0) {
                return false;
            }
            setIsKeepAlive(false);
            byte[] contentByte = new byte[1];
            String dataStr = line;
            String[] dataSplit = dataStr.split("#");
            if (dataSplit.length == 1) {
                Debug.message("Qimo single byte message received [TCP]: " + line);
                setIsSingleSend(true);
                contentByte[0] = line.getBytes()[0];
                setContent(contentByte);
                return true;
            } else if (dataSplit.length != 3) {
                Debug.message("Failed to parse Qimo quick message [TCP]: " + line);
                return false;
            } else {
                setIsSingleSend(false);
                setTempContent(dataStr);
                contentByte[0] = dataSplit[2].getBytes()[0];
                if (contentByte[0] == (short) 17) {
                    Debug.message("Qimo keep alive message received [TCP]: " + line);
                    setIsKeepAlive(true);
                    return true;
                }
                setContent(contentByte);
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean getIsSingleSend() {
        return this.isSingleSend;
    }

    public void setIsSingleSend(boolean isSingleSend) {
        this.isSingleSend = isSingleSend;
    }

    public boolean getIsKeepAlive() {
        return this.isKeepAlive;
    }

    public void setIsKeepAlive(boolean isKeepAlive) {
        this.isKeepAlive = isKeepAlive;
    }

    public boolean readQuickly(DatagramSocket serverSock) {
        quicklyInit();
        if (serverSock == null) {
            return false;
        }
        byte[] buffer = new byte[100];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        try {
            serverSock.receive(packet);
            String line = new String(packet.getData());
            Debug.message("Qimo quick message received [UDP]: " + line);
            if (line == null || line.length() <= 0) {
                return false;
            }
            byte[] contentByte = new byte[1];
            String dataStr = line;
            String[] dataSplit = dataStr.split("#");
            if (dataSplit.length != 3) {
                Debug.message("Failed to parse Qimo quick message [UDP]: " + line);
                setIsSingleSend(true);
                return true;
            }
            setIsSingleSend(false);
            setTempContent(dataStr);
            contentByte[0] = dataSplit[2].getBytes()[0];
            setContent(contentByte);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void setFirstLine(String value) {
        this.firstLine = value;
    }

    protected String getFirstLine() {
        return this.firstLine;
    }

    protected String getFirstLineToken(int num) {
        StringTokenizer st = new StringTokenizer(this.firstLine, " ");
        String lastToken = "";
        for (int n = 0; n <= num; n++) {
            if (!st.hasMoreTokens()) {
                return "";
            }
            lastToken = st.nextToken();
        }
        return lastToken;
    }

    public boolean hasFirstLine() {
        return this.firstLine.length() > 0;
    }

    public int getNHeaders() {
        return this.httpHeaderList.size();
    }

    public void addHeader(HTTPHeader header) {
        this.httpHeaderList.add(header);
    }

    public void addHeader(String name, String value) {
        this.httpHeaderList.add(new HTTPHeader(name, value));
    }

    public HTTPHeader getHeader(int n) {
        return (HTTPHeader) this.httpHeaderList.get(n);
    }

    public HTTPHeader getHeader(String name) {
        int nHeaders = getNHeaders();
        for (int n = 0; n < nHeaders; n++) {
            HTTPHeader header = getHeader(n);
            if (header.getName().equalsIgnoreCase(name)) {
                return header;
            }
        }
        return null;
    }

    public void clearHeaders() {
        this.httpHeaderList.clear();
        this.httpHeaderList = new Vector();
    }

    public boolean hasHeader(String name) {
        return getHeader(name) != null;
    }

    public void setHeader(String name, String value) {
        HTTPHeader header = getHeader(name);
        if (header != null) {
            header.setValue(value);
        } else {
            addHeader(name, value);
        }
    }

    public void setHeader(String name, int value) {
        setHeader(name, Integer.toString(value));
    }

    public void setHeader(String name, long value) {
        setHeader(name, Long.toString(value));
    }

    public void setHeader(HTTPHeader header) {
        setHeader(header.getName(), header.getValue());
    }

    public String getHeaderValue(String name) {
        HTTPHeader header = getHeader(name);
        if (header == null) {
            return "";
        }
        return header.getValue();
    }

    public void setStringHeader(String name, String value, String startWidth, String endWidth) {
        String headerValue = value;
        if (!headerValue.startsWith(startWidth)) {
            headerValue = new StringBuilder(String.valueOf(startWidth)).append(headerValue).toString();
        }
        if (!headerValue.endsWith(endWidth)) {
            headerValue = new StringBuilder(String.valueOf(headerValue)).append(endWidth).toString();
        }
        setHeader(name, headerValue);
    }

    public void setStringHeader(String name, String value) {
        setStringHeader(name, value, "\"", "\"");
    }

    public String getStringHeaderValue(String name, String startWidth, String endWidth) {
        String headerValue = getHeaderValue(name);
        if (headerValue.startsWith(startWidth)) {
            headerValue = headerValue.substring(1, headerValue.length());
        }
        if (headerValue.endsWith(endWidth)) {
            return headerValue.substring(0, headerValue.length() - 1);
        }
        return headerValue;
    }

    public String getStringHeaderValue(String name) {
        return getStringHeaderValue(name, "\"", "\"");
    }

    public void setIntegerHeader(String name, int value) {
        setHeader(name, Integer.toString(value));
    }

    public void setLongHeader(String name, long value) {
        setHeader(name, Long.toString(value));
    }

    public int getIntegerHeaderValue(String name) {
        HTTPHeader header = getHeader(name);
        if (header == null) {
            return 0;
        }
        return StringUtil.toInteger(header.getValue());
    }

    public long getLongHeaderValue(String name) {
        HTTPHeader header = getHeader(name);
        if (header == null) {
            return 0;
        }
        return StringUtil.toLong(header.getValue());
    }

    public StringBuffer getHeaderString() {
        StringBuffer str = new StringBuffer();
        int nHeaders = getNHeaders();
        for (int n = 0; n < nHeaders; n++) {
            HTTPHeader header = getHeader(n);
            str.append(header.getName()).append(": ").append(header.getValue()).append(HTTP.CRLF);
        }
        return str;
    }

    public void setContent(byte[] data, boolean updateWithContentLength) {
        this.content = data;
        if (updateWithContentLength) {
            setContentLength((long) data.length);
        }
    }

    public void setContent(byte[] data) {
        setContent(data, true);
    }

    public void setContent(String data, boolean updateWithContentLength) {
        setContent(data.getBytes(), updateWithContentLength);
    }

    public void setContent(String data) {
        setContent(data, true);
    }

    public byte[] getContent() {
        return this.content;
    }

    public void setTempContent(String tempContent) {
        this.tempContent = tempContent;
    }

    public String getTempContent() {
        return this.tempContent;
    }

    public String getContentString() {
        String charSet = getCharSet();
        if (charSet == null || charSet.length() <= 0) {
            return new String(this.content);
        }
        try {
            return new String(this.content, charSet);
        } catch (Exception e) {
            Debug.warning(e);
            return new String(this.content);
        }
    }

    public boolean hasContent() {
        return this.content.length > 0;
    }

    public void setContentInputStream(InputStream in) {
        this.contentInput = in;
    }

    public InputStream getContentInputStream() {
        return this.contentInput;
    }

    public boolean hasContentInputStream() {
        return this.contentInput != null;
    }

    public void setContentType(String type) {
        setHeader(HTTP.CONTENT_TYPE, type);
    }

    public String getContentType() {
        return getHeaderValue(HTTP.CONTENT_TYPE);
    }

    public String getCharSet() {
        String contentType = getContentType();
        if (contentType == null) {
            return "";
        }
        contentType = contentType.toLowerCase();
        int charSetIdx = contentType.indexOf(HTTP.CHARSET);
        if (charSetIdx < 0) {
            return "";
        }
        int charSetEndIdx = (HTTP.CHARSET.length() + charSetIdx) + 1;
        String charSet = new String(contentType.getBytes(), charSetEndIdx, contentType.length() - charSetEndIdx);
        if (charSet.length() < 0) {
            return "";
        }
        if (charSet.charAt(0) == '\"') {
            charSet = charSet.substring(1, charSet.length() - 1);
        }
        if (charSet.length() < 0) {
            return "";
        }
        if (charSet.charAt(charSet.length() - 1) == '\"') {
            return charSet.substring(0, charSet.length() - 1);
        }
        return charSet;
    }

    public void setContentLength(long len) {
        setLongHeader(HTTP.CONTENT_LENGTH, len);
    }

    public long getContentLength() {
        return getLongHeaderValue(HTTP.CONTENT_LENGTH);
    }

    public boolean hasConnection() {
        return hasHeader(HTTP.CONNECTION);
    }

    public void setConnection(String value) {
        setHeader(HTTP.CONNECTION, value);
    }

    public String getConnection() {
        return getHeaderValue(HTTP.CONNECTION);
    }

    public boolean isCloseConnection() {
        if (!hasConnection()) {
            return false;
        }
        String connection = getConnection();
        if (connection != null) {
            return connection.equalsIgnoreCase(HTTP.CLOSE);
        }
        return false;
    }

    public boolean isKeepAliveConnection() {
        if (!hasConnection()) {
            return false;
        }
        String connection = getConnection();
        if (connection != null) {
            return connection.equalsIgnoreCase(HTTP.KEEP_ALIVE);
        }
        return false;
    }

    public boolean hasContentRange() {
        return hasHeader(HTTP.CONTENT_RANGE) || hasHeader(HTTP.RANGE);
    }

    public void setContentRange(long firstPos, long lastPos, long length) {
        setHeader(HTTP.CONTENT_RANGE, new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(new StringBuilder(String.valueOf("" + "bytes ")).append(Long.toString(firstPos)).append("-").toString())).append(Long.toString(lastPos)).append("/").toString())).append(0 < length ? Long.toString(length) : NetDiagnoseCheckTools.NO_CHECK_FLAG).toString());
    }

    public long[] getContentRange() {
        long[] range = new long[]{0, 0, 0};
        if (hasContentRange()) {
            String rangeLine = getHeaderValue(HTTP.CONTENT_RANGE);
            if (rangeLine.length() <= 0) {
                rangeLine = getHeaderValue(HTTP.RANGE);
            }
            if (rangeLine.length() > 0) {
                StringTokenizer strToken = new StringTokenizer(rangeLine, " =");
                if (strToken.hasMoreTokens()) {
                    String bytesStr = strToken.nextToken(" ");
                    if (strToken.hasMoreTokens()) {
                        try {
                            range[0] = Long.parseLong(strToken.nextToken(" -"));
                        } catch (NumberFormatException e) {
                        }
                        if (strToken.hasMoreTokens()) {
                            try {
                                range[1] = Long.parseLong(strToken.nextToken("-/"));
                            } catch (NumberFormatException e2) {
                            }
                            if (strToken.hasMoreTokens()) {
                                try {
                                    range[2] = Long.parseLong(strToken.nextToken("/"));
                                } catch (NumberFormatException e3) {
                                }
                            }
                        }
                    }
                }
            }
        }
        return range;
    }

    public long getContentRangeFirstPosition() {
        return getContentRange()[0];
    }

    public long getContentRangeLastPosition() {
        return getContentRange()[1];
    }

    public long getContentRangeInstanceLength() {
        return getContentRange()[2];
    }

    public void setCacheControl(String directive) {
        setHeader(HTTP.CACHE_CONTROL, directive);
    }

    public void setCacheControl(String directive, int value) {
        setHeader(HTTP.CACHE_CONTROL, new StringBuilder(String.valueOf(directive)).append(SearchCriteria.EQ).append(Integer.toString(value)).toString());
    }

    public void setCacheControl(int value) {
        setCacheControl(HTTP.MAX_AGE, value);
    }

    public String getCacheControl() {
        return getHeaderValue(HTTP.CACHE_CONTROL);
    }

    public void setServer(String name) {
        setHeader(HTTP.SERVER, name);
    }

    public String getServer() {
        return getHeaderValue(HTTP.SERVER);
    }

    public void setHost(String host, int port) {
        String hostAddr = host;
        if (HostInterface.isIPv6Address(host)) {
            hostAddr = "[" + host + AlbumEnterFactory.SIGN_STR;
        }
        setHeader(HTTP.HOST, new StringBuilder(String.valueOf(hostAddr)).append(SOAP.DELIM).append(Integer.toString(port)).toString());
    }

    public void setHost(String host) {
        String hostAddr = host;
        if (HostInterface.isIPv6Address(host)) {
            hostAddr = "[" + host + AlbumEnterFactory.SIGN_STR;
        }
        setHeader(HTTP.HOST, hostAddr);
    }

    public String getHost() {
        return getHeaderValue(HTTP.HOST);
    }

    public void setDate(Calendar cal) {
        setHeader(HTTP.DATE, new Date(cal).getDateString());
    }

    public String getDate() {
        return getHeaderValue(HTTP.DATE);
    }

    public boolean hasTransferEncoding() {
        return hasHeader(HTTP.TRANSFER_ENCODING);
    }

    public void setTransferEncoding(String value) {
        setHeader(HTTP.TRANSFER_ENCODING, value);
    }

    public String getTransferEncoding() {
        return getHeaderValue(HTTP.TRANSFER_ENCODING);
    }

    public boolean isChunked() {
        if (!hasTransferEncoding()) {
            return false;
        }
        String transEnc = getTransferEncoding();
        if (transEnc != null) {
            return transEnc.equalsIgnoreCase(HTTP.CHUNKED);
        }
        return false;
    }
}
