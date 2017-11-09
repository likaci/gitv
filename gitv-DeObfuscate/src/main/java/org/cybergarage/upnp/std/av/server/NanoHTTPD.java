package org.cybergarage.upnp.std.av.server;

import com.gala.sdk.player.TipType;
import com.gala.video.lib.share.ifmanager.bussnessIF.openplay.IOpenApiCommandHolder;
import com.gala.video.webview.utils.WebSDKConstants;
import com.tvos.downloadmanager.data.DownloadRecordColumns;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TimeZone;
import org.cybergarage.http.HTTP;
import org.cybergarage.http.HTTPStatus;
import org.cybergarage.upnp.UPnPStatus;

public abstract class NanoHTTPD {
    public static final String MIME_DEFAULT_BINARY = "application/octet-stream";
    public static final String MIME_HTML = "text/html";
    public static final String MIME_PLAINTEXT = "text/plain";
    private static final String QUERY_STRING_PARAMETER = "NanoHttpd.QUERY_STRING";
    private static final String TAG = "NanoHTTPD";
    private AsyncRunner asyncRunner;
    private final String hostname;
    protected ContentDirectory mCDS;
    protected MediaServer mMediaServer;
    private final int myPort;
    private ServerSocket myServerSocket;
    private Thread myThread;
    private TempFileManagerFactory tempFileManagerFactory;

    class C21971 implements Runnable {
        C21971() {
        }

        public void run() {
            do {
                try {
                    final Socket finalAccept = NanoHTTPD.this.myServerSocket.accept();
                    InputStream inputStream = finalAccept.getInputStream();
                    OutputStream outputStream = finalAccept.getOutputStream();
                    final HTTPSession session = new HTTPSession(NanoHTTPD.this.tempFileManagerFactory.create(), inputStream, outputStream);
                    NanoHTTPD.this.asyncRunner.exec(new Runnable() {
                        public void run() {
                            session.run();
                            try {
                                finalAccept.close();
                            } catch (IOException e) {
                            }
                        }
                    });
                } catch (IOException e) {
                }
            } while (!NanoHTTPD.this.myServerSocket.isClosed());
        }
    }

    public interface AsyncRunner {
        void exec(Runnable runnable);
    }

    public static class DefaultAsyncRunner implements AsyncRunner {
        private long requestCount;

        public void exec(Runnable code) {
            this.requestCount++;
            Thread t = new Thread(code);
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

    protected class HTTPSession implements Runnable {
        public static final int BUFSIZE = 8192;
        private final InputStream inputStream;
        private final OutputStream outputStream;
        private final TempFileManager tempFileManager;

        public HTTPSession(TempFileManager tempFileManager, InputStream inputStream, OutputStream outputStream) {
            this.tempFileManager = tempFileManager;
            this.inputStream = inputStream;
            this.outputStream = outputStream;
        }

        public void run() {
            try {
                if (this.inputStream == null) {
                    this.tempFileManager.clear();
                    return;
                }
                byte[] buf = new byte[8192];
                int splitbyte = 0;
                int rlen = 0;
                int read = this.inputStream.read(buf, 0, 8192);
                while (read > 0) {
                    rlen += read;
                    splitbyte = findHeaderEnd(buf, rlen);
                    if (splitbyte > 0) {
                        break;
                    }
                    read = this.inputStream.read(buf, rlen, 8192 - rlen);
                }
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(buf, 0, rlen)));
                Map<String, String> pre = new HashMap();
                Map<String, String> parms = new HashMap();
                Map<String, String> header = new HashMap();
                Map<String, String> files = new HashMap();
                decodeHeader(bufferedReader, pre, parms, header);
                Method method = Method.lookup((String) pre.get("method"));
                if (method == null) {
                    Response.error(this.outputStream, Status.BAD_REQUEST, "BAD REQUEST: Syntax error.");
                    throw new InterruptedException();
                }
                String uri = (String) pre.get(DownloadRecordColumns.COLUMN_URI);
                long size = extractContentLength(header);
                RandomAccessFile f = getTmpBucket();
                if (splitbyte < rlen) {
                    f.write(buf, splitbyte, rlen - splitbyte);
                }
                if (splitbyte < rlen) {
                    size -= (long) ((rlen - splitbyte) + 1);
                } else if (splitbyte == 0 || size == IOpenApiCommandHolder.OAA_NO_LIMIT) {
                    size = 0;
                }
                buf = new byte[512];
                while (rlen >= 0 && size > 0) {
                    rlen = this.inputStream.read(buf, 0, 512);
                    size -= (long) rlen;
                    if (rlen > 0) {
                        f.write(buf, 0, rlen);
                    }
                }
                ByteBuffer fbuf = f.getChannel().map(MapMode.READ_ONLY, null, f.length());
                f.seek(0);
                BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(f.getFD())));
                if (Method.POST.equals(method)) {
                    String contentType = "";
                    String contentTypeHeader = (String) header.get("content-type");
                    StringTokenizer st = null;
                    if (contentTypeHeader != null) {
                        StringTokenizer stringTokenizer = new StringTokenizer(contentTypeHeader, ",; ");
                        if (stringTokenizer.hasMoreTokens()) {
                            contentType = stringTokenizer.nextToken();
                        }
                    }
                    if (!"multipart/form-data".equalsIgnoreCase(contentType)) {
                        String postLine = "";
                        char[] pbuf = new char[512];
                        for (read = in.read(pbuf); read >= 0; read = in.read(pbuf)) {
                            if (postLine.endsWith(HTTP.CRLF)) {
                                break;
                            }
                            postLine = new StringBuilder(String.valueOf(postLine)).append(String.valueOf(pbuf, 0, read)).toString();
                        }
                        decodeParms(postLine.trim(), parms);
                    } else if (st.hasMoreTokens()) {
                        String boundaryStartString = "boundary=";
                        String boundary = contentTypeHeader.substring(contentTypeHeader.indexOf(boundaryStartString) + boundaryStartString.length(), contentTypeHeader.length());
                        if (boundary.startsWith("\"") && boundary.startsWith("\"")) {
                            boundary = boundary.substring(1, boundary.length() - 1);
                        }
                        decodeMultipartData(boundary, fbuf, in, parms, files);
                    } else {
                        Response.error(this.outputStream, Status.BAD_REQUEST, "BAD REQUEST: Content type is multipart/form-data but boundary missing. Usage: GET /example/file.html");
                        throw new InterruptedException();
                    }
                } else if (Method.PUT.equals(method)) {
                    files.put("content", saveTmpFile(fbuf, 0, fbuf.limit()));
                }
                Response r = NanoHTTPD.this.serve(uri, method, header, parms, files);
                if (r == null) {
                    Response.error(this.outputStream, Status.INTERNAL_ERROR, "SERVER INTERNAL ERROR: Serve() returned a null response.");
                    throw new InterruptedException();
                }
                int conId = -1;
                if (NanoHTTPD.this.mMediaServer != null) {
                    conId = NanoHTTPD.this.mMediaServer.getContentDirectory().contentServiceRequestReceived((String) parms.get("id"));
                }
                r.setRequestMethod(method);
                r.send(this.outputStream);
                if (!(NanoHTTPD.this.mMediaServer == null || conId == -1)) {
                    NanoHTTPD.this.mMediaServer.getContentDirectory().contentServiceRequestFinished(conId);
                }
                in.close();
                this.inputStream.close();
                this.tempFileManager.clear();
            } catch (IOException ioe) {
                Response.error(this.outputStream, Status.INTERNAL_ERROR, "SERVER INTERNAL ERROR: IOException: " + ioe.getMessage());
                throw new InterruptedException();
            } catch (InterruptedException e) {
                this.tempFileManager.clear();
            } catch (Throwable th) {
                this.tempFileManager.clear();
            }
        }

        private long extractContentLength(Map<String, String> header) {
            String contentLength = (String) header.get("content-length");
            if (contentLength == null) {
                return IOpenApiCommandHolder.OAA_NO_LIMIT;
            }
            try {
                return (long) Integer.parseInt(contentLength);
            } catch (NumberFormatException ex) {
                ex.printStackTrace();
                return IOpenApiCommandHolder.OAA_NO_LIMIT;
            }
        }

        private void decodeHeader(BufferedReader in, Map<String, String> pre, Map<String, String> parms, Map<String, String> header) throws InterruptedException {
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
                                        header.put(line.substring(0, p).trim().toLowerCase(), line.substring(p + 1).trim());
                                    }
                                    line = in.readLine();
                                }
                            }
                            pre.put(DownloadRecordColumns.COLUMN_URI, uri);
                            return;
                        }
                        Response.error(this.outputStream, Status.BAD_REQUEST, "BAD REQUEST: Missing URI. Usage: GET /example/file.html");
                        throw new InterruptedException();
                    }
                    Response.error(this.outputStream, Status.BAD_REQUEST, "BAD REQUEST: Syntax error. Usage: GET /example/file.html");
                    throw new InterruptedException();
                }
            } catch (IOException ioe) {
                Response.error(this.outputStream, Status.INTERNAL_ERROR, "SERVER INTERNAL ERROR: IOException: " + ioe.getMessage());
                throw new InterruptedException();
            }
        }

        private void decodeMultipartData(String boundary, ByteBuffer fbuf, BufferedReader in, Map<String, String> parms, Map<String, String> files) throws InterruptedException {
            try {
                int[] bpositions = getBoundaryPositions(fbuf, boundary.getBytes());
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
                                item.put(mpline.substring(0, p).trim().toLowerCase(), mpline.substring(p + 1).trim());
                            }
                            mpline = in.readLine();
                        }
                        if (mpline != null) {
                            String contentDisposition = (String) item.get("content-disposition");
                            if (contentDisposition == null) {
                                Response.error(this.outputStream, Status.BAD_REQUEST, "BAD REQUEST: Content type is multipart/form-data but no content-disposition info found. Usage: GET /example/file.html");
                                throw new InterruptedException();
                            }
                            StringTokenizer st = new StringTokenizer(contentDisposition, "; ");
                            Map<String, String> disposition = new HashMap();
                            while (st.hasMoreTokens()) {
                                String token = st.nextToken();
                                p = token.indexOf(61);
                                if (p != -1) {
                                    disposition.put(token.substring(0, p).trim().toLowerCase(), token.substring(p + 1).trim());
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
                                    Response.error(this.outputStream, Status.INTERNAL_ERROR, "Error processing request");
                                    throw new InterruptedException();
                                }
                            }
                            while (mpline != null && !mpline.contains(boundary)) {
                                mpline = in.readLine();
                                if (mpline != null) {
                                    int d = mpline.indexOf(boundary);
                                    if (d == -1) {
                                        value = new StringBuilder(String.valueOf(value)).append(mpline).toString();
                                    } else {
                                        value = new StringBuilder(String.valueOf(value)).append(mpline.substring(0, d - 2)).toString();
                                    }
                                }
                            }
                            parms.put(pname, value);
                        }
                    } else {
                        Response.error(this.outputStream, Status.BAD_REQUEST, "BAD REQUEST: Content type is multipart/form-data but next chunk does not start with boundary. Usage: GET /example/file.html");
                        throw new InterruptedException();
                    }
                }
            } catch (IOException ioe) {
                Response.error(this.outputStream, Status.INTERNAL_ERROR, "SERVER INTERNAL ERROR: IOException: " + ioe.getMessage());
                throw new InterruptedException();
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

        public int[] getBoundaryPositions(ByteBuffer b, byte[] boundary) {
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
            String path = "";
            if (len > 0) {
                try {
                    TempFile tempFile = this.tempFileManager.createTempFile();
                    ByteBuffer src = b.duplicate();
                    FileChannel dest = new FileOutputStream(tempFile.getName()).getChannel();
                    src.position(offset).limit(offset + len);
                    dest.write(src.slice());
                    path = tempFile.getName();
                } catch (Exception e) {
                    System.err.println("Error: " + e.getMessage());
                }
            }
            return path;
        }

        private RandomAccessFile getTmpBucket() {
            try {
                return new RandomAccessFile(this.tempFileManager.createTempFile().getName(), "rw");
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
                return null;
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
                p.put(NanoHTTPD.QUERY_STRING_PARAMETER, "");
                return;
            }
            p.put(NanoHTTPD.QUERY_STRING_PARAMETER, parms);
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
    }

    public enum Method {
        GET,
        PUT,
        POST,
        DELETE,
        HEAD;

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
        private InputStream data;
        private Map<String, String> header;
        private String mimeType;
        private Method requestMethod;
        private Status status;

        public enum Status {
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
                return this.requestStatus + " " + this.description;
            }
        }

        public Response(String msg) {
            this(Status.OK, "text/html", msg);
        }

        public Response(Status status, String mimeType, InputStream data) {
            this.header = new HashMap();
            this.status = status;
            this.mimeType = mimeType;
            this.data = data;
        }

        public Response(Status status, String mimeType, String txt) {
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

        public static void error(OutputStream outputStream, Status error, String message) {
            new Response(error, "text/plain", message).send(outputStream);
        }

        public void addHeader(String name, String value) {
            this.header.put(name, value);
        }

        public String toString() {
            return new StringBuilder(String.valueOf(this.header.toString())).append("\n").append(this.status.toString()).toString();
        }

        private void send(OutputStream outputStream) {
            String mime = this.mimeType;
            SimpleDateFormat gmtFrmt = new SimpleDateFormat("E, d MMM yyyy HH:mm:ss 'GMT'", Locale.US);
            gmtFrmt.setTimeZone(TimeZone.getTimeZone("GMT"));
            try {
                if (this.status == null) {
                    throw new Error("sendResponse(): Status can't be null.");
                }
                PrintWriter pw = new PrintWriter(outputStream);
                pw.print("HTTP/1.0 " + this.status.getDescription() + " \r\n");
                if (mime != null) {
                    pw.print("Content-Type: " + mime + HTTP.CRLF);
                }
                if (this.header == null || this.header.get(HTTP.DATE) == null) {
                    pw.print("Date: " + gmtFrmt.format(new Date()) + HTTP.CRLF);
                }
                if (this.header != null) {
                    for (String key : this.header.keySet()) {
                        pw.print(new StringBuilder(String.valueOf(key)).append(": ").append((String) this.header.get(key)).append(HTTP.CRLF).toString());
                    }
                }
                pw.print(HTTP.CRLF);
                pw.flush();
                if (this.requestMethod != Method.HEAD && this.data != null) {
                    int pending = this.data.available();
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
                        if (read <= 0) {
                            break;
                        }
                        outputStream.write(buff, 0, read);
                        pending -= read;
                    }
                }
                outputStream.flush();
                outputStream.close();
                if (this.data != null) {
                    this.data.close();
                }
            } catch (IOException e) {
            }
        }

        public Status getStatus() {
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
    }

    public interface SessionStateListener {
        public static final int SESSION_END = 2;
        public static final int SESSION_START = 1;

        void sesstionState(int i);
    }

    public abstract Response serve(String str, Method method, Map<String, String> map, Map<String, String> map2, Map<String, String> map3);

    public void setMediaServer(MediaServer server) {
        this.mMediaServer = server;
    }

    public NanoHTTPD(int port) {
        this(null, port);
    }

    public NanoHTTPD(String hostname, int port) {
        this.mCDS = null;
        this.mMediaServer = null;
        this.hostname = hostname;
        this.myPort = port;
        setTempFileManagerFactory(new DefaultTempFileManagerFactory());
        setAsyncRunner(new DefaultAsyncRunner());
    }

    public void start() throws IOException {
        this.myServerSocket = new ServerSocket();
        this.myServerSocket.bind(this.hostname != null ? new InetSocketAddress(this.hostname, this.myPort) : new InetSocketAddress(this.myPort));
        this.myThread = new Thread(new C21971());
        this.myThread.setDaemon(true);
        this.myThread.setName("NanoHttpd Main Listener");
        this.myThread.start();
    }

    public void stop() {
        try {
            this.myServerSocket.close();
            this.myThread.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    public void setCDS(ContentDirectory cds) {
        this.mCDS = cds;
    }

    public void setAsyncRunner(AsyncRunner asyncRunner) {
        this.asyncRunner = asyncRunner;
    }

    public void setTempFileManagerFactory(TempFileManagerFactory tempFileManagerFactory) {
        this.tempFileManagerFactory = tempFileManagerFactory;
    }
}
