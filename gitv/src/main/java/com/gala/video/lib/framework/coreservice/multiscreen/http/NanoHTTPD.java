package com.gala.video.lib.framework.coreservice.multiscreen.http;

import com.gala.sdk.player.TipType;
import com.gala.video.app.stub.Thread8K;
import com.gala.video.webview.utils.WebSDKConstants;
import com.tvos.downloadmanager.data.DownloadRecordColumns;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.PushbackInputStream;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URLDecoder;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TimeZone;
import org.cybergarage.http.HTTP;
import org.cybergarage.http.HTTPStatus;
import org.cybergarage.upnp.UPnPStatus;
import org.cybergarage.upnp.std.av.server.object.SearchCriteria;

public abstract class NanoHTTPD {
    public static final String MIME_HTML = "text/html";
    public static final String MIME_PLAINTEXT = "text/plain";
    private static final String QUERY_STRING_PARAMETER = "NanoHttpd.QUERY_STRING";
    public static final int SOCKET_READ_TIMEOUT = 5000;
    private AsyncRunner asyncRunner;
    private final String hostname;
    private final int myPort;
    private ServerSocket myServerSocket;
    private Thread myThread;
    private Set<Socket> openConnections;
    private TempFileManagerFactory tempFileManagerFactory;

    public interface AsyncRunner {
        void exec(Runnable runnable);
    }

    public static class Cookie {
        private String e;
        private String n;
        private String v;

        public Cookie(String name, String value, String expires) {
            this.n = name;
            this.v = value;
            this.e = expires;
        }

        public Cookie(String name, String value) {
            this(name, value, 30);
        }

        public Cookie(String name, String value, int numDays) {
            this.n = name;
            this.v = value;
            this.e = getHTTPTime(numDays);
        }

        public String getHTTPHeader() {
            return String.format("%s=%s; expires=%s", new Object[]{this.n, this.v, this.e});
        }

        public static String getHTTPTime(int days) {
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
            dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
            calendar.add(5, days);
            return dateFormat.format(calendar.getTime());
        }
    }

    public class CookieHandler implements Iterable<String> {
        private HashMap<String, String> cookies = new HashMap();
        private ArrayList<Cookie> queue = new ArrayList();

        public CookieHandler(Map<String, String> httpHeaders) {
            String raw = (String) httpHeaders.get(WebSDKConstants.PARAM_KEY_COOKIE);
            if (raw != null) {
                for (String token : raw.split(";")) {
                    String[] data = token.trim().split(SearchCriteria.EQ);
                    if (data.length == 2) {
                        this.cookies.put(data[0], data[1]);
                    }
                }
            }
        }

        public Iterator<String> iterator() {
            return this.cookies.keySet().iterator();
        }

        public String read(String name) {
            return (String) this.cookies.get(name);
        }

        public void set(String name, String value, int expires) {
            this.queue.add(new Cookie(name, value, Cookie.getHTTPTime(expires)));
        }

        public void set(Cookie cookie) {
            this.queue.add(cookie);
        }

        public void delete(String name) {
            set(name, "-delete-", -30);
        }

        public void unloadQueue(Response response) {
            Iterator it = this.queue.iterator();
            while (it.hasNext()) {
                response.addHeader("Set-Cookie", ((Cookie) it.next()).getHTTPHeader());
            }
        }
    }

    public static class DefaultAsyncRunner implements AsyncRunner {
        private long requestCount;

        public void exec(Runnable code) {
            this.requestCount++;
            Thread t = new Thread8K(code, "DefaultAsyncRunner");
            t.setDaemon(true);
            t.setName("NanoHttpd Request Processor (#" + this.requestCount + ")");
            t.start();
        }
    }

    public interface TempFile {
        void delete() throws Exception;

        String getName();

        OutputStream open() throws Exception;
    }

    public static class DefaultTempFile implements TempFile {
        private File file;
        private OutputStream fstream = new FileOutputStream(this.file);

        public DefaultTempFile(String tempdir) throws IOException {
            this.file = File.createTempFile("NanoHTTPD-", "", new File(tempdir));
        }

        public OutputStream open() throws Exception {
            return this.fstream;
        }

        public void delete() throws Exception {
            NanoHTTPD.safeClose(this.fstream);
            this.file.delete();
        }

        public String getName() {
            return this.file.getAbsolutePath();
        }
    }

    public interface TempFileManager {
        void clear();

        TempFile createTempFile() throws Exception;
    }

    public static class DefaultTempFileManager implements TempFileManager {
        private final List<TempFile> tempFiles = new ArrayList();
        private final String tmpdir = System.getProperty("java.io.tmpdir");

        public TempFile createTempFile() throws Exception {
            DefaultTempFile tempFile = new DefaultTempFile(this.tmpdir);
            this.tempFiles.add(tempFile);
            return tempFile;
        }

        public void clear() {
            for (TempFile file : this.tempFiles) {
                try {
                    file.delete();
                } catch (Exception e) {
                }
            }
            this.tempFiles.clear();
        }
    }

    public interface TempFileManagerFactory {
        TempFileManager create();
    }

    private class DefaultTempFileManagerFactory implements TempFileManagerFactory {
        private DefaultTempFileManagerFactory() {
        }

        public TempFileManager create() {
            return new DefaultTempFileManager();
        }
    }

    public interface IHTTPSession {
        void execute() throws IOException;

        CookieHandler getCookies();

        Map<String, String> getHeaders();

        InputStream getInputStream();

        Method getMethod();

        Map<String, String> getParms();

        String getQueryParameterString();

        String getUri();

        void parseBody(Map<String, String> map) throws IOException, ResponseException;
    }

    protected class HTTPSession implements IHTTPSession {
        public static final int BUFSIZE = 8192;
        private CookieHandler cookies;
        private Map<String, String> headers;
        private PushbackInputStream inputStream;
        private Method method;
        private final OutputStream outputStream;
        private Map<String, String> parms;
        private String queryParameterString;
        private int rlen;
        private int splitbyte;
        private final TempFileManager tempFileManager;
        private String uri;

        public HTTPSession(TempFileManager tempFileManager, InputStream inputStream, OutputStream outputStream) {
            this.tempFileManager = tempFileManager;
            this.inputStream = new PushbackInputStream(inputStream, 8192);
            this.outputStream = outputStream;
        }

        public HTTPSession(TempFileManager tempFileManager, InputStream inputStream, OutputStream outputStream, InetAddress inetAddress) {
            this.tempFileManager = tempFileManager;
            this.inputStream = new PushbackInputStream(inputStream, 8192);
            this.outputStream = outputStream;
            String remoteIp = (inetAddress.isLoopbackAddress() || inetAddress.isAnyLocalAddress()) ? "127.0.0.1" : inetAddress.getHostAddress().toString();
            this.headers = new HashMap();
            this.headers.put("remote-addr", remoteIp);
            this.headers.put("http-client-ip", remoteIp);
        }

        public void execute() throws IOException {
            try {
                byte[] buf = new byte[8192];
                this.splitbyte = 0;
                this.rlen = 0;
                int read = this.inputStream.read(buf, 0, 8192);
                if (read == -1) {
                    NanoHTTPD.safeClose(this.inputStream);
                    NanoHTTPD.safeClose(this.outputStream);
                    throw new SocketException("NanoHttpd Shutdown");
                }
                while (read > 0) {
                    this.rlen += read;
                    this.splitbyte = findHeaderEnd(buf, this.rlen);
                    if (this.splitbyte > 0) {
                        break;
                    }
                    read = this.inputStream.read(buf, this.rlen, 8192 - this.rlen);
                }
                if (this.splitbyte < this.rlen) {
                    this.inputStream.unread(buf, this.splitbyte, this.rlen - this.splitbyte);
                }
                this.parms = new HashMap();
                if (this.headers == null) {
                    this.headers = new HashMap();
                }
                BufferedReader hin = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(buf, 0, this.rlen), Charset.forName("UTF-8")));
                Map<String, String> pre = new HashMap();
                decodeHeader(hin, pre, this.parms, this.headers);
                this.method = Method.lookup((String) pre.get("method"));
                if (this.method == null) {
                    throw new ResponseException(Status.BAD_REQUEST, "BAD REQUEST: Syntax error.");
                }
                this.uri = (String) pre.get(DownloadRecordColumns.COLUMN_URI);
                this.cookies = new CookieHandler(this.headers);
                Response r = NanoHTTPD.this.serve(this);
                if (r == null) {
                    throw new ResponseException(Status.INTERNAL_ERROR, "SERVER INTERNAL ERROR: Serve() returned a null response.");
                }
                this.cookies.unloadQueue(r);
                r.setRequestMethod(this.method);
                r.send(this.outputStream);
                this.tempFileManager.clear();
            } catch (Exception e) {
                NanoHTTPD.safeClose(this.inputStream);
                NanoHTTPD.safeClose(this.outputStream);
                throw new SocketException("NanoHttpd Shutdown");
            } catch (SocketException e2) {
                throw e2;
            } catch (SocketTimeoutException ste) {
                throw ste;
            } catch (IOException ioe) {
                new Response(Status.INTERNAL_ERROR, "text/plain", "SERVER INTERNAL ERROR: IOException: " + ioe.getMessage()).send(this.outputStream);
                NanoHTTPD.safeClose(this.outputStream);
                this.tempFileManager.clear();
            } catch (ResponseException re) {
                new Response(re.getStatus(), "text/plain", re.getMessage()).send(this.outputStream);
                NanoHTTPD.safeClose(this.outputStream);
                this.tempFileManager.clear();
            } catch (Throwable th) {
                this.tempFileManager.clear();
            }
        }

        public void parseBody(Map<String, String> files) throws IOException, ResponseException {
            BufferedReader in;
            Throwable th;
            RandomAccessFile randomAccessFile = null;
            try {
                long size;
                randomAccessFile = getTmpBucket();
                if (this.headers.containsKey("content-length")) {
                    size = (long) Integer.parseInt((String) this.headers.get("content-length"));
                } else if (this.splitbyte < this.rlen) {
                    size = (long) (this.rlen - this.splitbyte);
                } else {
                    size = 0;
                }
                byte[] buf = new byte[512];
                while (this.rlen >= 0 && size > 0) {
                    this.rlen = this.inputStream.read(buf, 0, (int) Math.min(size, 512));
                    size -= (long) this.rlen;
                    if (this.rlen > 0) {
                        randomAccessFile.write(buf, 0, this.rlen);
                    }
                }
                ByteBuffer fbuf = randomAccessFile.getChannel().map(MapMode.READ_ONLY, null, randomAccessFile.length());
                randomAccessFile.seek(0);
                in = new BufferedReader(new InputStreamReader(new FileInputStream(randomAccessFile.getFD()), Charset.forName("UTF-8")));
                try {
                    if (Method.POST.equals(this.method)) {
                        String contentType = "";
                        String contentTypeHeader = (String) this.headers.get("content-type");
                        StringTokenizer st = null;
                        if (contentTypeHeader != null) {
                            StringTokenizer stringTokenizer = new StringTokenizer(contentTypeHeader, ",; ");
                            if (stringTokenizer.hasMoreTokens()) {
                                contentType = stringTokenizer.nextToken();
                            }
                        }
                        if (!"multipart/form-data".equalsIgnoreCase(contentType)) {
                            String postLine = "";
                            StringBuilder postLineBuffer = new StringBuilder();
                            char[] pbuf = new char[512];
                            for (int read = in.read(pbuf); read >= 0; read = in.read(pbuf)) {
                                if (postLine.endsWith(HTTP.CRLF)) {
                                    break;
                                }
                                postLine = String.valueOf(pbuf, 0, read);
                                postLineBuffer.append(postLine);
                            }
                            postLine = postLineBuffer.toString().trim();
                            if ("application/x-www-form-urlencoded".equalsIgnoreCase(contentType)) {
                                decodeParms(postLine, this.parms);
                            } else if (postLine.length() != 0) {
                                files.put("postData", postLine);
                            }
                        } else if (st.hasMoreTokens()) {
                            String boundaryStartString = "boundary=";
                            String boundary = contentTypeHeader.substring(contentTypeHeader.indexOf(boundaryStartString) + boundaryStartString.length(), contentTypeHeader.length());
                            if (boundary.startsWith("\"") && boundary.endsWith("\"")) {
                                boundary = boundary.substring(1, boundary.length() - 1);
                            }
                            decodeMultipartData(boundary, fbuf, in, this.parms, files);
                        } else {
                            throw new ResponseException(Status.BAD_REQUEST, "BAD REQUEST: Content type is multipart/form-data but boundary missing. Usage: GET /example/file.html");
                        }
                    } else if (Method.PUT.equals(this.method)) {
                        Map<String, String> map = files;
                        map.put("content", saveTmpFile(fbuf, 0, fbuf.limit()));
                    }
                    NanoHTTPD.safeClose((Closeable) randomAccessFile);
                    NanoHTTPD.safeClose((Closeable) in);
                } catch (Throwable th2) {
                    th = th2;
                    NanoHTTPD.safeClose((Closeable) randomAccessFile);
                    NanoHTTPD.safeClose((Closeable) in);
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                in = null;
                NanoHTTPD.safeClose((Closeable) randomAccessFile);
                NanoHTTPD.safeClose((Closeable) in);
                throw th;
            }
        }

        private void decodeHeader(BufferedReader in, Map<String, String> pre, Map<String, String> parms, Map<String, String> headers) throws ResponseException {
            try {
                String inLine = in.readLine();
                if (inLine != null) {
                    StringTokenizer st = new StringTokenizer(inLine);
                    if (st.hasMoreTokens()) {
                        pre.put("method", st.nextToken());
                        if (st.hasMoreTokens()) {
                            String uri = st.nextToken();
                            int qmi = uri.indexOf(63);
                            if (qmi >= 0) {
                                decodeParms(uri.substring(qmi + 1), parms);
                                uri = NanoHTTPD.this.decodePercent(uri.substring(0, qmi));
                            } else {
                                uri = NanoHTTPD.this.decodePercent(uri);
                            }
                            if (st.hasMoreTokens()) {
                                String line = in.readLine();
                                while (line != null && line.trim().length() > 0) {
                                    int p = line.indexOf(58);
                                    if (p >= 0) {
                                        headers.put(line.substring(0, p).trim().toLowerCase(Locale.US), line.substring(p + 1).trim());
                                    }
                                    line = in.readLine();
                                }
                            }
                            pre.put(DownloadRecordColumns.COLUMN_URI, uri);
                            return;
                        }
                        throw new ResponseException(Status.BAD_REQUEST, "BAD REQUEST: Missing URI. Usage: GET /example/file.html");
                    }
                    throw new ResponseException(Status.BAD_REQUEST, "BAD REQUEST: Syntax error. Usage: GET /example/file.html");
                }
            } catch (IOException ioe) {
                throw new ResponseException(Status.INTERNAL_ERROR, "SERVER INTERNAL ERROR: IOException: " + ioe.getMessage(), ioe);
            }
        }

        private void decodeMultipartData(String boundary, ByteBuffer fbuf, BufferedReader in, Map<String, String> parms, Map<String, String> files) throws ResponseException {
            try {
                int[] bpositions = getBoundaryPositions(fbuf, boundary.getBytes(Charset.forName("UTF-8")));
                int boundarycount = 1;
                String mpline = in.readLine();
                while (mpline != null) {
                    if (mpline.contains(boundary)) {
                        int p;
                        boundarycount++;
                        Map<String, String> item = new HashMap();
                        mpline = in.readLine();
                        while (mpline != null && mpline.trim().length() > 0) {
                            p = mpline.indexOf(58);
                            if (p != -1) {
                                item.put(mpline.substring(0, p).trim().toLowerCase(Locale.US), mpline.substring(p + 1).trim());
                            }
                            mpline = in.readLine();
                        }
                        if (mpline != null) {
                            String contentDisposition = (String) item.get("content-disposition");
                            if (contentDisposition == null) {
                                throw new ResponseException(Status.BAD_REQUEST, "BAD REQUEST: Content type is multipart/form-data but no content-disposition info found. Usage: GET /example/file.html");
                            }
                            StringTokenizer st = new StringTokenizer(contentDisposition, ";");
                            Map<String, String> disposition = new HashMap();
                            while (st.hasMoreTokens()) {
                                String token = st.nextToken().trim();
                                p = token.indexOf(61);
                                if (p != -1) {
                                    disposition.put(token.substring(0, p).trim().toLowerCase(Locale.US), token.substring(p + 1).trim());
                                }
                            }
                            String pname = (String) disposition.get(WebSDKConstants.PARAM_KEY_PL_NAME);
                            pname = pname.substring(1, pname.length() - 1);
                            String value = "";
                            if (item.get("content-type") != null) {
                                if (boundarycount <= bpositions.length) {
                                    int offset = stripMultipartHeaders(fbuf, bpositions[boundarycount - 2]);
                                    files.put(pname, saveTmpFile(fbuf, offset, (bpositions[boundarycount - 1] - offset) - 4));
                                    value = (String) disposition.get("filename");
                                    value = value.substring(1, value.length() - 1);
                                    do {
                                        mpline = in.readLine();
                                        if (mpline == null) {
                                            break;
                                        }
                                    } while (!mpline.contains(boundary));
                                } else {
                                    throw new ResponseException(Status.INTERNAL_ERROR, "Error processing request");
                                }
                            }
                            while (mpline != null && !mpline.contains(boundary)) {
                                mpline = in.readLine();
                                if (mpline != null) {
                                    int d = mpline.indexOf(boundary);
                                    if (d == -1) {
                                        value = value + mpline;
                                    } else {
                                        value = value + mpline.substring(0, d - 2);
                                    }
                                }
                            }
                            parms.put(pname, value);
                        }
                    } else {
                        throw new ResponseException(Status.BAD_REQUEST, "BAD REQUEST: Content type is multipart/form-data but next chunk does not start with boundary. Usage: GET /example/file.html");
                    }
                }
            } catch (IOException ioe) {
                throw new ResponseException(Status.INTERNAL_ERROR, "SERVER INTERNAL ERROR: IOException: " + ioe.getMessage(), ioe);
            }
        }

        private int findHeaderEnd(byte[] buf, int rlen) {
            int splitbyte = 0;
            while (splitbyte + 3 < rlen) {
                if (buf[splitbyte] == (byte) 13 && buf[splitbyte + 1] == (byte) 10 && buf[splitbyte + 2] == (byte) 13 && buf[splitbyte + 3] == (byte) 10) {
                    return splitbyte + 4;
                }
                splitbyte++;
            }
            return 0;
        }

        private int[] getBoundaryPositions(ByteBuffer b, byte[] boundary) {
            int matchcount = 0;
            int matchbyte = -1;
            List<Integer> matchbytes = new ArrayList();
            int i = 0;
            while (i < b.limit()) {
                if (b.get(i) == boundary[matchcount]) {
                    if (matchcount == 0) {
                        matchbyte = i;
                    }
                    matchcount++;
                    if (matchcount == boundary.length) {
                        matchbytes.add(Integer.valueOf(matchbyte));
                        matchcount = 0;
                        matchbyte = -1;
                    }
                } else {
                    i -= matchcount;
                    matchcount = 0;
                    matchbyte = -1;
                }
                i++;
            }
            int[] ret = new int[matchbytes.size()];
            for (i = 0; i < ret.length; i++) {
                ret[i] = ((Integer) matchbytes.get(i)).intValue();
            }
            return ret;
        }

        private String saveTmpFile(ByteBuffer b, int offset, int len) {
            Exception e;
            Throwable th;
            String path = "";
            if (len > 0) {
                FileOutputStream fileOutputStream = null;
                try {
                    TempFile tempFile = this.tempFileManager.createTempFile();
                    ByteBuffer src = b.duplicate();
                    FileOutputStream fileOutputStream2 = new FileOutputStream(tempFile.getName());
                    try {
                        FileChannel dest = fileOutputStream2.getChannel();
                        src.position(offset).limit(offset + len);
                        dest.write(src.slice());
                        path = tempFile.getName();
                        NanoHTTPD.safeClose((Closeable) fileOutputStream2);
                    } catch (Exception e2) {
                        e = e2;
                        fileOutputStream = fileOutputStream2;
                        try {
                            throw new Error(e);
                        } catch (Throwable th2) {
                            th = th2;
                            NanoHTTPD.safeClose((Closeable) fileOutputStream);
                            throw th;
                        }
                    } catch (Throwable th3) {
                        th = th3;
                        fileOutputStream = fileOutputStream2;
                        NanoHTTPD.safeClose((Closeable) fileOutputStream);
                        throw th;
                    }
                } catch (Exception e3) {
                    e = e3;
                    throw new Error(e);
                }
            }
            return path;
        }

        private RandomAccessFile getTmpBucket() {
            try {
                return new RandomAccessFile(this.tempFileManager.createTempFile().getName(), "rw");
            } catch (Exception e) {
                throw new Error(e);
            }
        }

        private int stripMultipartHeaders(ByteBuffer b, int offset) {
            int i = offset;
            while (i < b.limit()) {
                if (b.get(i) == (byte) 13) {
                    i++;
                    if (b.get(i) == (byte) 10) {
                        i++;
                        if (b.get(i) == (byte) 13) {
                            i++;
                            if (b.get(i) == (byte) 10) {
                                break;
                            }
                        } else {
                            continue;
                        }
                    } else {
                        continue;
                    }
                }
                i++;
            }
            return i + 1;
        }

        private void decodeParms(String parms, Map<String, String> p) {
            if (parms == null) {
                this.queryParameterString = "";
                return;
            }
            this.queryParameterString = parms;
            StringTokenizer st = new StringTokenizer(parms, "&");
            while (st.hasMoreTokens()) {
                String e = st.nextToken();
                int sep = e.indexOf(61);
                if (sep >= 0) {
                    p.put(NanoHTTPD.this.decodePercent(e.substring(0, sep)).trim(), NanoHTTPD.this.decodePercent(e.substring(sep + 1)));
                } else {
                    p.put(NanoHTTPD.this.decodePercent(e).trim(), "");
                }
            }
        }

        public final Map<String, String> getParms() {
            return this.parms;
        }

        public String getQueryParameterString() {
            return this.queryParameterString;
        }

        public final Map<String, String> getHeaders() {
            return this.headers;
        }

        public final String getUri() {
            return this.uri;
        }

        public final Method getMethod() {
            return this.method;
        }

        public final InputStream getInputStream() {
            return this.inputStream;
        }

        public CookieHandler getCookies() {
            return this.cookies;
        }
    }

    public enum Method {
        GET,
        PUT,
        POST,
        DELETE,
        HEAD,
        OPTIONS;

        static Method lookup(String method) {
            for (Method m : values()) {
                if (m.toString().equalsIgnoreCase(method)) {
                    return m;
                }
            }
            return null;
        }
    }

    public static class Response {
        private boolean chunkedTransfer;
        private InputStream data;
        private Map<String, String> header;
        private String mimeType;
        private Method requestMethod;
        private IStatus status;

        public interface IStatus {
            String getDescription();

            int getRequestStatus();
        }

        public enum Status implements IStatus {
            SWITCH_PROTOCOL(101, "Switching Protocols"),
            OK(200, "OK"),
            CREATED(201, "Created"),
            ACCEPTED(202, "Accepted"),
            NO_CONTENT(204, "No Content"),
            PARTIAL_CONTENT(206, "Partial Content"),
            REDIRECT(301, "Moved Permanently"),
            NOT_MODIFIED(TipType.CONCRETE_TYPE_REPLAY_PLAYNEXT, "Not Modified"),
            BAD_REQUEST(400, "Bad Request"),
            UNAUTHORIZED(UPnPStatus.INVALID_ACTION, "Unauthorized"),
            FORBIDDEN(UPnPStatus.OUT_OF_SYNC, "Forbidden"),
            NOT_FOUND(404, "Not Found"),
            METHOD_NOT_ALLOWED(405, "Method Not Allowed"),
            RANGE_NOT_SATISFIABLE(HTTPStatus.INVALID_RANGE, "Requested Range Not Satisfiable"),
            INTERNAL_ERROR(500, "Internal Server Error");
            
            private final String description;
            private final int requestStatus;

            private Status(int requestStatus, String description) {
                this.requestStatus = requestStatus;
                this.description = description;
            }

            public int getRequestStatus() {
                return this.requestStatus;
            }

            public String getDescription() {
                return "" + this.requestStatus + " " + this.description;
            }
        }

        public Response(String msg) {
            this(Status.OK, "text/html", msg);
        }

        public Response(IStatus status, String mimeType, InputStream data) {
            this.header = new HashMap();
            this.status = status;
            this.mimeType = mimeType;
            this.data = data;
        }

        public Response(IStatus status, String mimeType, String txt) {
            InputStream byteArrayInputStream;
            this.header = new HashMap();
            this.status = status;
            this.mimeType = mimeType;
            if (txt != null) {
                try {
                    byteArrayInputStream = new ByteArrayInputStream(txt.getBytes("UTF-8"));
                } catch (UnsupportedEncodingException uee) {
                    uee.printStackTrace();
                    return;
                }
            }
            byteArrayInputStream = null;
            this.data = byteArrayInputStream;
        }

        public void addHeader(String name, String value) {
            this.header.put(name, value);
        }

        public String getHeader(String name) {
            return (String) this.header.get(name);
        }

        protected void send(OutputStream outputStream) {
            String mime = this.mimeType;
            SimpleDateFormat gmtFrmt = new SimpleDateFormat("E, d MMM yyyy HH:mm:ss 'GMT'", Locale.US);
            gmtFrmt.setTimeZone(TimeZone.getTimeZone("GMT"));
            try {
                if (this.status == null) {
                    throw new Error("sendResponse(): Status can't be null.");
                }
                PrintWriter pw = new PrintWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                pw.print("HTTP/1.1 " + this.status.getDescription() + " \r\n");
                if (mime != null) {
                    pw.print("Content-Type: " + mime + HTTP.CRLF);
                }
                if (this.header == null || this.header.get(HTTP.DATE) == null) {
                    pw.print("Date: " + gmtFrmt.format(new Date()) + HTTP.CRLF);
                }
                if (this.header != null) {
                    for (String key : this.header.keySet()) {
                        pw.print(key + ": " + ((String) this.header.get(key)) + HTTP.CRLF);
                    }
                }
                sendConnectionHeaderIfNotAlreadyPresent(pw, this.header);
                if (this.requestMethod == Method.HEAD || !this.chunkedTransfer) {
                    int pending = this.data != null ? this.data.available() : 0;
                    sendContentLengthHeaderIfNotAlreadyPresent(pw, this.header, pending);
                    pw.print(HTTP.CRLF);
                    pw.flush();
                    sendAsFixedLength(outputStream, pending);
                } else {
                    sendAsChunked(outputStream, pw);
                }
                outputStream.flush();
                NanoHTTPD.safeClose(this.data);
            } catch (IOException e) {
            }
        }

        protected void sendContentLengthHeaderIfNotAlreadyPresent(PrintWriter pw, Map<String, String> header, int size) {
            if (!headerAlreadySent(header, "content-length")) {
                pw.print("Content-Length: " + size + HTTP.CRLF);
            }
        }

        protected void sendConnectionHeaderIfNotAlreadyPresent(PrintWriter pw, Map<String, String> header) {
            if (!headerAlreadySent(header, "connection")) {
                pw.print("Connection: keep-alive\r\n");
            }
        }

        private boolean headerAlreadySent(Map<String, String> header, String name) {
            boolean alreadySent = false;
            for (String headerName : header.keySet()) {
                alreadySent |= headerName.equalsIgnoreCase(name);
            }
            return alreadySent;
        }

        private void sendAsChunked(OutputStream outputStream, PrintWriter pw) throws IOException {
            pw.print("Transfer-Encoding: chunked\r\n");
            pw.print(HTTP.CRLF);
            pw.flush();
            byte[] CRLF = HTTP.CRLF.getBytes();
            byte[] buff = new byte[16384];
            while (true) {
                int read = this.data.read(buff);
                if (read > 0) {
                    outputStream.write(String.format("%x\r\n", new Object[]{Integer.valueOf(read)}).getBytes(Charset.forName("UTF-8")));
                    outputStream.write(buff, 0, read);
                    outputStream.write(CRLF);
                } else {
                    outputStream.write(String.format("0\r\n\r\n", new Object[0]).getBytes(Charset.forName("UTF-8")));
                    return;
                }
            }
        }

        private void sendAsFixedLength(OutputStream outputStream, int pending) throws IOException {
            if (this.requestMethod != Method.HEAD && this.data != null) {
                byte[] buff = new byte[16384];
                while (pending > 0) {
                    int i;
                    InputStream inputStream = this.data;
                    if (pending > 16384) {
                        i = 16384;
                    } else {
                        i = pending;
                    }
                    int read = inputStream.read(buff, 0, i);
                    if (read > 0) {
                        outputStream.write(buff, 0, read);
                        pending -= read;
                    } else {
                        return;
                    }
                }
            }
        }

        public IStatus getStatus() {
            return this.status;
        }

        public void setStatus(Status status) {
            this.status = status;
        }

        public String getMimeType() {
            return this.mimeType;
        }

        public void setMimeType(String mimeType) {
            this.mimeType = mimeType;
        }

        public InputStream getData() {
            return this.data;
        }

        public void setData(InputStream data) {
            this.data = data;
        }

        public Method getRequestMethod() {
            return this.requestMethod;
        }

        public void setRequestMethod(Method requestMethod) {
            this.requestMethod = requestMethod;
        }

        public void setChunkedTransfer(boolean chunkedTransfer) {
            this.chunkedTransfer = chunkedTransfer;
        }
    }

    public static final class ResponseException extends Exception {
        private static final long serialVersionUID = 1;
        private final Status status;

        public ResponseException(Status status, String message) {
            super(message);
            this.status = status;
        }

        public ResponseException(Status status, String message, Exception e) {
            super(message, e);
            this.status = status;
        }

        public Status getStatus() {
            return this.status;
        }
    }

    public NanoHTTPD(int port) {
        this(null, port);
    }

    public NanoHTTPD(String hostname, int port) {
        this.openConnections = new HashSet();
        this.hostname = hostname;
        this.myPort = port;
        setTempFileManagerFactory(new DefaultTempFileManagerFactory());
        setAsyncRunner(new DefaultAsyncRunner());
    }

    private static final void safeClose(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
            }
        }
    }

    private static final void safeClose(Socket closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
            }
        }
    }

    private static final void safeClose(ServerSocket closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
            }
        }
    }

    public void start() throws IOException {
        this.myServerSocket = new ServerSocket();
        this.myServerSocket.bind(this.hostname != null ? new InetSocketAddress(this.hostname, this.myPort) : new InetSocketAddress(this.myPort));
        this.myThread = new Thread8K(new Runnable() {
            public void run() {
                do {
                    try {
                        final Socket finalAccept = NanoHTTPD.this.myServerSocket.accept();
                        NanoHTTPD.this.registerConnection(finalAccept);
                        finalAccept.setSoTimeout(5000);
                        final InputStream inputStream = finalAccept.getInputStream();
                        NanoHTTPD.this.asyncRunner.exec(new Runnable() {
                            public void run() {
                                try {
                                    OutputStream outputStream = finalAccept.getOutputStream();
                                    HTTPSession session = new HTTPSession(NanoHTTPD.this.tempFileManagerFactory.create(), inputStream, outputStream, finalAccept.getInetAddress());
                                    while (!finalAccept.isClosed()) {
                                        session.execute();
                                    }
                                    NanoHTTPD.safeClose((Closeable) outputStream);
                                    NanoHTTPD.safeClose(inputStream);
                                    NanoHTTPD.safeClose(finalAccept);
                                    NanoHTTPD.this.unRegisterConnection(finalAccept);
                                } catch (Exception e) {
                                    if (!((e instanceof SocketException) && "NanoHttpd Shutdown".equals(e.getMessage()))) {
                                        e.printStackTrace();
                                    }
                                    NanoHTTPD.safeClose(null);
                                    NanoHTTPD.safeClose(inputStream);
                                    NanoHTTPD.safeClose(finalAccept);
                                    NanoHTTPD.this.unRegisterConnection(finalAccept);
                                } catch (Throwable th) {
                                    NanoHTTPD.safeClose(null);
                                    NanoHTTPD.safeClose(inputStream);
                                    NanoHTTPD.safeClose(finalAccept);
                                    NanoHTTPD.this.unRegisterConnection(finalAccept);
                                }
                            }
                        });
                    } catch (IOException e) {
                    }
                } while (!NanoHTTPD.this.myServerSocket.isClosed());
            }
        }, "NanoHTTPD");
        this.myThread.setDaemon(true);
        this.myThread.setName("NanoHttpd Main Listener");
        this.myThread.start();
    }

    public void stop() {
        try {
            safeClose(this.myServerSocket);
            closeAllConnections();
            if (this.myThread != null) {
                this.myThread.join();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void registerConnection(Socket socket) {
        this.openConnections.add(socket);
    }

    public synchronized void unRegisterConnection(Socket socket) {
        this.openConnections.remove(socket);
    }

    public synchronized void closeAllConnections() {
        for (Socket socket : this.openConnections) {
            safeClose(socket);
        }
    }

    public final int getListeningPort() {
        return this.myServerSocket == null ? -1 : this.myServerSocket.getLocalPort();
    }

    public final boolean wasStarted() {
        return (this.myServerSocket == null || this.myThread == null) ? false : true;
    }

    public final boolean isAlive() {
        return wasStarted() && !this.myServerSocket.isClosed() && this.myThread.isAlive();
    }

    @Deprecated
    public Response serve(String uri, Method method, Map<String, String> map, Map<String, String> map2, Map<String, String> map3) {
        return new Response(Status.NOT_FOUND, "text/plain", "Not Found");
    }

    public Response serve(IHTTPSession session) {
        Map<String, String> files = new HashMap();
        Method method = session.getMethod();
        if (Method.PUT.equals(method) || Method.POST.equals(method)) {
            try {
                session.parseBody(files);
            } catch (IOException ioe) {
                return new Response(Status.INTERNAL_ERROR, "text/plain", "SERVER INTERNAL ERROR: IOException: " + ioe.getMessage());
            } catch (ResponseException re) {
                return new Response(re.getStatus(), "text/plain", re.getMessage());
            }
        }
        Map<String, String> parms = session.getParms();
        parms.put(QUERY_STRING_PARAMETER, session.getQueryParameterString());
        return serve(session.getUri(), method, session.getHeaders(), parms, files);
    }

    protected String decodePercent(String str) {
        String decoded = null;
        try {
            decoded = URLDecoder.decode(str, "UTF8");
        } catch (UnsupportedEncodingException e) {
        }
        return decoded;
    }

    protected Map<String, List<String>> decodeParameters(Map<String, String> parms) {
        return decodeParameters((String) parms.get(QUERY_STRING_PARAMETER));
    }

    protected Map<String, List<String>> decodeParameters(String queryString) {
        Map<String, List<String>> parms = new HashMap();
        if (queryString != null) {
            StringTokenizer st = new StringTokenizer(queryString, "&");
            while (st.hasMoreTokens()) {
                String e = st.nextToken();
                int sep = e.indexOf(61);
                String propertyName = sep >= 0 ? decodePercent(e.substring(0, sep)).trim() : decodePercent(e).trim();
                if (!parms.containsKey(propertyName)) {
                    parms.put(propertyName, new ArrayList());
                }
                String propertyValue = sep >= 0 ? decodePercent(e.substring(sep + 1)) : null;
                if (propertyValue != null) {
                    ((List) parms.get(propertyName)).add(propertyValue);
                }
            }
        }
        return parms;
    }

    public void setAsyncRunner(AsyncRunner asyncRunner) {
        this.asyncRunner = asyncRunner;
    }

    public void setTempFileManagerFactory(TempFileManagerFactory tempFileManagerFactory) {
        this.tempFileManagerFactory = tempFileManagerFactory;
    }
}
