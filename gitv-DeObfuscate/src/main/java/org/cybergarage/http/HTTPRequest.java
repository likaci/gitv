package org.cybergarage.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.StringTokenizer;
import org.cybergarage.util.Debug;

public class HTTPRequest extends HTTPPacket {
    private static int mUDPUnknownHostTimes = 0;
    private HTTPSocket httpSocket;
    private HostUnknownTimeListener mHostUnknownTimeListener;
    private String method;
    private Socket postSocket;
    private String requestHost;
    private int requestPort;
    private Socket tcpSocketQuicklyA;
    private String tempContent;
    private DatagramSocket udpDsA;
    private DatagramSocket udpDsB;
    private String uri;

    public interface HostUnknownTimeListener {
        void hostUnknownTimes(int i);
    }

    public HTTPRequest() {
        this.method = null;
        this.tempContent = "";
        this.uri = null;
        this.requestHost = "";
        this.requestPort = -1;
        this.httpSocket = null;
        this.postSocket = null;
        this.tcpSocketQuicklyA = null;
        this.udpDsA = null;
        this.udpDsB = null;
        this.mHostUnknownTimeListener = null;
        setVersion("1.1");
    }

    public HTTPRequest(InputStream in) {
        super(in);
        this.method = null;
        this.tempContent = "";
        this.uri = null;
        this.requestHost = "";
        this.requestPort = -1;
        this.httpSocket = null;
        this.postSocket = null;
        this.tcpSocketQuicklyA = null;
        this.udpDsA = null;
        this.udpDsB = null;
        this.mHostUnknownTimeListener = null;
    }

    public HTTPRequest(HTTPSocket httpSock) {
        this(httpSock.getInputStream());
        setSocket(httpSock);
    }

    public void setMethod(String value) {
        this.method = value;
    }

    public String getMethod() {
        if (this.method != null) {
            return this.method;
        }
        return getFirstLineToken(0);
    }

    public boolean isMethod(String method) {
        String headerMethod = getMethod();
        if (headerMethod == null) {
            return false;
        }
        return headerMethod.equalsIgnoreCase(method);
    }

    public boolean isGetRequest() {
        return isMethod(HTTP.GET);
    }

    public boolean isPostRequest() {
        return isMethod(HTTP.POST);
    }

    public boolean isHeadRequest() {
        return isMethod(HTTP.HEAD);
    }

    public boolean isSubscribeRequest() {
        return isMethod("SUBSCRIBE");
    }

    public boolean isUnsubscribeRequest() {
        return isMethod("UNSUBSCRIBE");
    }

    public boolean isNotifyRequest() {
        return isMethod(HTTP.NOTIFY);
    }

    public boolean isQuicklyRequest() {
        if (getContent().length == 1) {
            return true;
        }
        return false;
    }

    public void setTempContent(String tempContent) {
        this.tempContent = tempContent;
    }

    public String getTempContent() {
        return this.tempContent;
    }

    public void setURI(String value, boolean isCheckRelativeURL) {
        this.uri = value;
        if (isCheckRelativeURL) {
            this.uri = HTTP.toRelativeURL(this.uri);
        }
    }

    public void setURI(String value) {
        setURI(value, false);
    }

    public String getURI() {
        if (this.uri != null) {
            return this.uri;
        }
        return getFirstLineToken(1);
    }

    public ParameterList getParameterList() {
        ParameterList paramList = new ParameterList();
        String uri = getURI();
        if (uri != null) {
            int paramIdx = uri.indexOf(63);
            if (paramIdx >= 0) {
                while (paramIdx > 0) {
                    int eqIdx = uri.indexOf(61, paramIdx + 1);
                    String name = uri.substring(paramIdx + 1, eqIdx);
                    int nextParamIdx = uri.indexOf(38, eqIdx + 1);
                    paramList.add(new Parameter(name, uri.substring(eqIdx + 1, nextParamIdx > 0 ? nextParamIdx : uri.length())));
                    paramIdx = nextParamIdx;
                }
            }
        }
        return paramList;
    }

    public String getParameterValue(String name) {
        return getParameterList().getValue(name);
    }

    public boolean isSOAPAction() {
        return hasHeader(HTTP.SOAP_ACTION);
    }

    public void setRequestHost(String host) {
        this.requestHost = host;
    }

    public String getRequestHost() {
        return this.requestHost;
    }

    public void setRequestPort(int host) {
        this.requestPort = host;
    }

    public int getRequestPort() {
        return this.requestPort;
    }

    public void setSocket(HTTPSocket value) {
        this.httpSocket = value;
    }

    public HTTPSocket getSocket() {
        return this.httpSocket;
    }

    public String getLocalAddress() {
        return getSocket().getLocalAddress();
    }

    public int getLocalPort() {
        return getSocket().getLocalPort();
    }

    public boolean parseRequestLine(String lineStr) {
        StringTokenizer st = new StringTokenizer(lineStr, " ");
        if (!st.hasMoreTokens()) {
            return false;
        }
        setMethod(st.nextToken());
        if (!st.hasMoreTokens()) {
            return false;
        }
        setURI(st.nextToken());
        if (!st.hasMoreTokens()) {
            return false;
        }
        setVersion(st.nextToken());
        return true;
    }

    public String getHTTPVersion() {
        if (hasFirstLine()) {
            return getFirstLineToken(2);
        }
        return "HTTP/" + super.getVersion();
    }

    public String getFirstLineString() {
        return getMethod() + " " + getURI() + " " + getHTTPVersion() + HTTP.CRLF;
    }

    public StringBuffer getHeader() {
        StringBuffer str = new StringBuffer();
        str.append(getFirstLineString());
        str.append(getHeaderString());
        return str;
    }

    public boolean isKeepAlive() {
        if (isCloseConnection()) {
            return false;
        }
        if (isKeepAliveConnection()) {
            return true;
        }
        boolean isHTTP10;
        if (getHTTPVersion().indexOf("1.0") > 0) {
            isHTTP10 = true;
        } else {
            isHTTP10 = false;
        }
        if (isHTTP10) {
            return false;
        }
        return true;
    }

    public boolean read() {
        return super.read(getSocket());
    }

    public boolean post(HTTPResponse httpRes) {
        HTTPSocket httpSock = getSocket();
        long offset = 0;
        long length = httpRes.getContentLength();
        if (hasContentRange()) {
            long firstPos = getContentRangeFirstPosition();
            long lastPos = getContentRangeLastPosition();
            if (lastPos <= 0) {
                lastPos = length - 1;
            }
            if (firstPos > length || lastPos > length) {
                return returnResponse(HTTPStatus.INVALID_RANGE);
            }
            httpRes.setContentRange(firstPos, lastPos, length);
            httpRes.setStatusCode(206);
            offset = firstPos;
            length = (lastPos - firstPos) + 1;
        }
        return httpSock.post(httpRes, offset, length, isHeadRequest());
    }

    public void connectHost(String host, int port, boolean isKeepAlive) throws IOException {
        if (!isKeepAlive) {
            this.postSocket = new Socket();
            if (this.postSocket != null) {
                this.postSocket.setTcpNoDelay(true);
                this.postSocket.setTrafficClass(16);
                this.postSocket.setPerformancePreferences(2, 3, 1);
                this.postSocket.setSoTimeout(10000);
                this.postSocket.connect(new InetSocketAddress(host, port), 5000);
            }
        } else if (this.postSocket == null) {
            this.postSocket = new Socket();
            if (this.postSocket != null) {
                this.postSocket.setTcpNoDelay(true);
                this.postSocket.setKeepAlive(true);
                this.postSocket.setOOBInline(true);
                this.postSocket.setTrafficClass(16);
                this.postSocket.setPerformancePreferences(2, 3, 1);
                this.postSocket.setSoTimeout(10000);
                this.postSocket.connect(new InetSocketAddress(host, port), 5000);
            }
        }
    }

    public void closeHostSocket() {
        try {
            if (this.postSocket != null) {
                Debug.message("clearHostSocket");
                this.postSocket.close();
                this.postSocket = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void connectHostQuickly(String host, int port) throws IOException {
        Debug.message("online ConnectHostQuickly()");
        this.tcpSocketQuicklyA = connectHostTcpQuickly(this.tcpSocketQuicklyA, host, port);
    }

    public void reconnectHostQuickly(String host, int port, String data) throws IOException {
        if (this.tcpSocketQuicklyA == null || !this.tcpSocketQuicklyA.isConnected()) {
            this.tcpSocketQuicklyA = connectHostTcpQuickly(this.tcpSocketQuicklyA, host, port);
            return;
        }
        Debug.message("online reconnectHostQuickly() sendUrgentData");
        if (data != null) {
            quicklyTCPPost(host, port, data);
        }
    }

    public void closeHostQuickly() {
        Debug.message("online closeHostQuickly() ");
        try {
            if (this.tcpSocketQuicklyA != null) {
                this.tcpSocketQuicklyA.close();
                this.tcpSocketQuicklyA = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (this.udpDsA != null) {
            this.udpDsA.close();
            this.udpDsA = null;
        }
        if (this.udpDsB != null) {
            this.udpDsB.close();
            this.udpDsB = null;
        }
    }

    private Socket connectHostTcpQuickly(Socket socketOne, String host, int port) throws IOException {
        if (socketOne == null) {
            socketOne = new Socket();
            if (socketOne != null) {
                socketOne.setTcpNoDelay(true);
                socketOne.setKeepAlive(true);
                socketOne.setOOBInline(true);
                socketOne.setTrafficClass(16);
                socketOne.setPerformancePreferences(2, 3, 1);
                socketOne.connect(new InetSocketAddress(host, port), 5000);
            }
        }
        return socketOne;
    }

    public synchronized boolean quicklyTCPPost(String host, int port, String data) {
        boolean z;
        this.tcpSocketQuicklyA = postQuickly(this.tcpSocketQuicklyA, host, port, data);
        if (this.tcpSocketQuicklyA == null) {
            Debug.message("quicklyTCPPost failed");
            z = false;
        } else {
            z = true;
        }
        return z;
    }

    public synchronized boolean quicklyPost(String host, int port, byte data) {
        boolean z = true;
        synchronized (this) {
            try {
                this.tcpSocketQuicklyA = connectHostTcpQuickly(this.tcpSocketQuicklyA, host, port);
                try {
                    if (this.tcpSocketQuicklyA != null) {
                        this.tcpSocketQuicklyA.sendUrgentData(data);
                    }
                } catch (Exception e) {
                    if (this.tcpSocketQuicklyA != null) {
                        try {
                            this.tcpSocketQuicklyA.close();
                        } catch (Exception e2) {
                        }
                        this.tcpSocketQuicklyA = null;
                    }
                    this.tcpSocketQuicklyA = connectHostTcpQuickly(this.tcpSocketQuicklyA, host, port);
                }
            } catch (IOException e3) {
                if (this.tcpSocketQuicklyA != null) {
                    try {
                        this.tcpSocketQuicklyA.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    this.tcpSocketQuicklyA = null;
                }
                z = false;
            }
        }
        return z;
    }

    private DatagramSocket quicklyUDPHost(DatagramSocket ds) {
        if (ds != null) {
            return ds;
        }
        try {
            DatagramSocket ds2 = new DatagramSocket();
            try {
                ds2.setTrafficClass(20);
                return ds2;
            } catch (SocketException e) {
                ds = ds2;
                ds.close();
                return null;
            }
        } catch (SocketException e2) {
            ds.close();
            return null;
        }
    }

    public synchronized boolean quicklyUDPPost(String host, int port, String data) {
        boolean retA;
        retA = quicklyUDPPost(this.udpDsA, host, port, data);
        if (!retA) {
            boolean retB = quicklyUDPPost(this.udpDsB, host, port, data);
            Debug.message("quicklyUDPPost UDP failed, resend ret: " + retB);
            retA = retB;
        }
        return retA;
    }

    public void setHostUnknownTimeListener(HostUnknownTimeListener listener) {
        this.mHostUnknownTimeListener = listener;
    }

    public synchronized boolean quicklyUDPPost(DatagramSocket ds, String host, int port, String data) {
        boolean z = false;
        synchronized (this) {
            ds = quicklyUDPHost(ds);
            if (ds != null) {
                try {
                    ds.send(new DatagramPacket(data.getBytes(), data.length(), InetAddress.getByName(host), port));
                    z = true;
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                    if (ds != null) {
                        ds.close();
                    }
                } catch (IOException e2) {
                    e2.printStackTrace();
                    if (ds != null) {
                        ds.close();
                    }
                }
            }
        }
        return z;
    }

    public Socket postQuickly(Socket socket, String host, int port, String data) {
        try {
            socket = connectHostTcpQuickly(socket, host, port);
            if (socket == null) {
                return socket;
            }
            try {
                PrintWriter pout = new PrintWriter(socket.getOutputStream());
                pout.print(data);
                pout.flush();
                return socket;
            } catch (Exception ex) {
                if (socket != null) {
                    try {
                        socket.close();
                    } catch (Exception e) {
                    }
                    socket = null;
                }
                Debug.message("Exception To reconnect and send data : " + ex);
                return connectHostTcpQuickly(socket, host, port);
            }
        } catch (IOException e2) {
            if (socket == null) {
                return socket;
            }
            try {
                socket.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return null;
        }
    }

    public synchronized boolean justPost(String host, int port, boolean isKeepAlive) {
        boolean z = false;
        synchronized (this) {
            setHost(host);
            setConnection(isKeepAlive ? HTTP.KEEP_ALIVE : HTTP.CLOSE);
            OutputStream out = null;
            try {
                connectHost(host, port, isKeepAlive);
                out = this.postSocket.getOutputStream();
                PrintWriter pout = new PrintWriter(out);
                pout.print(getHeader());
                pout.print(HTTP.CRLF);
                boolean isChunkedRequest = isChunked();
                String content = getContentString();
                int contentLength = 0;
                if (content != null) {
                    contentLength = content.length();
                }
                if (contentLength > 0) {
                    if (isChunkedRequest) {
                        pout.print(Long.toHexString((long) contentLength));
                        pout.print(HTTP.CRLF);
                    }
                    pout.print(content);
                    if (isChunkedRequest) {
                        pout.print(HTTP.CRLF);
                    }
                }
                if (isChunkedRequest) {
                    pout.print("0");
                    pout.print(HTTP.CRLF);
                }
                pout.flush();
                if (!isKeepAlive) {
                    if (out != null) {
                        try {
                            out.close();
                        } catch (Exception e) {
                        }
                    }
                    if (this.postSocket != null) {
                        try {
                            this.postSocket.close();
                        } catch (Exception e2) {
                        }
                        this.postSocket = null;
                    }
                }
                z = true;
            } catch (Exception e3) {
                if (isKeepAlive) {
                    if (this.postSocket != null) {
                        try {
                            this.postSocket.close();
                        } catch (Exception e4) {
                        }
                        this.postSocket = null;
                    }
                    Debug.warning(e3);
                }
                if (!isKeepAlive) {
                    if (out != null) {
                        try {
                            out.close();
                        } catch (Exception e5) {
                        }
                    }
                    if (this.postSocket != null) {
                        try {
                            this.postSocket.close();
                        } catch (Exception e6) {
                        }
                        this.postSocket = null;
                    }
                }
            } catch (Exception e32) {
                if (isKeepAlive) {
                    if (this.postSocket != null) {
                        try {
                            this.postSocket.close();
                        } catch (Exception e7) {
                        }
                        this.postSocket = null;
                    }
                    Debug.warning(e32);
                }
                if (!isKeepAlive) {
                    if (out != null) {
                        try {
                            out.close();
                        } catch (Exception e8) {
                        }
                    }
                    if (this.postSocket != null) {
                        try {
                            this.postSocket.close();
                        } catch (Exception e9) {
                        }
                        this.postSocket = null;
                    }
                }
            } catch (Throwable th) {
                if (!isKeepAlive) {
                    if (out != null) {
                        try {
                            out.close();
                        } catch (Exception e10) {
                        }
                    }
                    if (this.postSocket != null) {
                        try {
                            this.postSocket.close();
                        } catch (Exception e11) {
                        }
                        this.postSocket = null;
                    }
                }
            }
        }
        return z;
    }

    public HTTPResponse post(String host, int port, boolean isKeepAlive) {
        HTTPResponse httpRes = new HTTPResponse();
        setHost(host);
        setConnection(isKeepAlive ? HTTP.KEEP_ALIVE : HTTP.CLOSE);
        setHeader(HTTP.CACHE_CONTROL, HTTP.NO_CACHE);
        boolean isHeaderRequest = isHeadRequest();
        OutputStream out = null;
        InputStream in = null;
        try {
            connectHost(host, port, isKeepAlive);
            out = this.postSocket.getOutputStream();
            PrintWriter pout = new PrintWriter(out);
            pout.print(getHeader());
            pout.print(HTTP.CRLF);
            boolean isChunkedRequest = isChunked();
            String content = getContentString();
            int contentLength = 0;
            if (content != null) {
                contentLength = content.length();
            }
            if (contentLength > 0) {
                if (isChunkedRequest) {
                    pout.print(Long.toHexString((long) contentLength));
                    pout.print(HTTP.CRLF);
                }
                pout.print(content);
                if (isChunkedRequest) {
                    pout.print(HTTP.CRLF);
                }
            }
            if (isChunkedRequest) {
                pout.print("0");
                pout.print(HTTP.CRLF);
            }
            pout.flush();
            in = this.postSocket.getInputStream();
            httpRes.set(in, isHeaderRequest);
            int statuscode = httpRes.getStatusCode();
            if (statuscode == 0 || HTTP.CLOSE.equals(httpRes.getConnection())) {
                Debug.message("DMR server connection has been closed, status code =" + statuscode);
                if (isKeepAlive) {
                    closeSocket();
                }
            }
            if (!isKeepAlive) {
                if (in != null) {
                    try {
                        in.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (out != null) {
                    try {
                        out.close();
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }
                closeSocket();
            }
        } catch (SocketException e3) {
            httpRes.setStatusCode(404);
            e3.printStackTrace();
            if (!isKeepAlive) {
                if (in != null) {
                    try {
                        in.close();
                    } catch (Exception e22) {
                        e22.printStackTrace();
                    }
                }
                if (out != null) {
                    try {
                        out.close();
                    } catch (Exception e222) {
                        e222.printStackTrace();
                    }
                }
                closeSocket();
            }
        } catch (IOException e4) {
            httpRes.setStatusCode(500);
            e4.printStackTrace();
            if (!isKeepAlive) {
                if (in != null) {
                    try {
                        in.close();
                    } catch (Exception e2222) {
                        e2222.printStackTrace();
                    }
                }
                if (out != null) {
                    try {
                        out.close();
                    } catch (Exception e22222) {
                        e22222.printStackTrace();
                    }
                }
                closeSocket();
            }
        } catch (Throwable th) {
            if (!isKeepAlive) {
                if (in != null) {
                    try {
                        in.close();
                    } catch (Exception e222222) {
                        e222222.printStackTrace();
                    }
                }
                if (out != null) {
                    try {
                        out.close();
                    } catch (Exception e2222222) {
                        e2222222.printStackTrace();
                    }
                }
                closeSocket();
            }
        }
        return httpRes;
    }

    private void closeSocket() {
        if (this.postSocket != null && !this.postSocket.isClosed()) {
            try {
                this.postSocket.close();
            } catch (Exception e) {
            }
            this.postSocket = null;
        }
    }

    public HTTPResponse post(String host, int port) {
        return post(host, port, false);
    }

    public void set(HTTPRequest httpReq) {
        set((HTTPPacket) httpReq);
        setSocket(httpReq.getSocket());
    }

    public boolean returnResponse(int statusCode) {
        HTTPResponse httpRes = new HTTPResponse();
        httpRes.setStatusCode(statusCode);
        httpRes.setContentLength(0);
        return post(httpRes);
    }

    public boolean returnOK() {
        return returnResponse(200);
    }

    public boolean returnBadRequest() {
        return returnResponse(400);
    }

    public String toString() {
        StringBuffer str = new StringBuffer();
        str.append(getHeader()).append(HTTP.CRLF).append(getContentString());
        return str.toString();
    }

    public void print() {
        System.out.println("------------------------------DUMP HTTPRequest [Start]------------------------------");
        System.out.println(toString().replace(HTTP.CRLF, HTTP.TAB));
        System.out.println("-------------------------------DUMP HTTPRequest [End]-------------------------------");
    }
}
