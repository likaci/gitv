package com.gala.android.dlna.sdk.controlpoint.qimohttpserver;

import com.gala.android.dlna.sdk.controlpoint.qimohttpserver.NanoHTTPD.IHTTPSession;
import com.gala.android.dlna.sdk.controlpoint.qimohttpserver.NanoHTTPD.Response;
import com.gala.android.dlna.sdk.controlpoint.qimohttpserver.NanoHTTPD.Response.IStatus;
import com.gala.android.dlna.sdk.controlpoint.qimohttpserver.NanoHTTPD.Response.Status;
import com.gala.cloudui.constants.CuteConstants;
import com.gala.video.lib.share.ifimpl.netdiagnose.NetDiagnoseCheckTools;
import com.gala.video.lib.share.uikit.data.data.processor.UIKitConfig;
import com.tvos.appdetailpage.client.Constants;
import com.xcrash.crashreporter.utils.CrashConst;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.StringTokenizer;
import org.cybergarage.http.HTTP;
import org.cybergarage.soap.SOAP;

public class SimpleWebServer extends NanoHTTPD {
    public static final List<String> INDEX_FILE_NAMES = new ArrayList<String>() {
        {
            add("index.html");
            add("index.htm");
        }
    };
    private static final String LICENCE = "Copyright (c) 2012-2013 by Paul S. Hawke, 2001,2005-2013 by Jarno Elonen, 2010 by Konstantinos Togias\n\nRedistribution and use in source and binary forms, with or without\nmodification, are permitted provided that the following conditions\nare met:\n\nRedistributions of source code must retain the above copyright notice,\nthis list of conditions and the following disclaimer. Redistributions in\nbinary form must reproduce the above copyright notice, this list of\nconditions and the following disclaimer in the documentation and/or other\nmaterials provided with the distribution. The name of the author may not\nbe used to endorse or promote products derived from this software without\nspecific prior written permission. \n \nTHIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR\nIMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES\nOF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.\nIN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,\nINCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT\nNOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,\nDATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY\nTHEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT\n(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE\nOF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.";
    public static final String MIME_DEFAULT_BINARY = "application/octet-stream";
    private static final Map<String, String> MIME_TYPES = new HashMap<String, String>() {
        {
            put("css", "text/css");
            put("htm", "text/html");
            put("html", "text/html");
            put("xml", "text/xml");
            put("java", "text/x-java-source, text/java");
            put("md", "text/plain");
            put(CuteConstants.TYPE_TXT, "text/plain");
            put(Constants.USERGAME_ORDER_ASC, "text/plain");
            put(UIKitConfig.KEY_GIF, "image/gif");
            put("jpg", "image/jpeg");
            put("jpeg", "image/jpeg");
            put("png", "image/png");
            put("mp3", "audio/mpeg");
            put("m3u", "audio/mpeg-url");
            put("mp4", "video/mp4");
            put("ogv", "video/ogg");
            put("flv", "video/x-flv");
            put("mov", "video/quicktime");
            put("swf", "application/x-shockwave-flash");
            put("js", "application/javascript");
            put("pdf", "application/pdf");
            put("doc", "application/msword");
            put("ogg", "application/x-ogg");
            put("zip", "application/octet-stream");
            put("exe", "application/octet-stream");
            put("class", "application/octet-stream");
            put("apk", "application/vnd.android.package-archive");
        }
    };
    private static Map<String, WebServerPlugin> mimeTypeHandlers = new HashMap();
    private final boolean quiet;
    protected List<File> rootDirs;

    public static void main(String[] args) {
        int port = 8080;
        String host = null;
        List<File> rootDirs = new ArrayList();
        boolean quiet = false;
        Map<String, String> options = new HashMap();
        int i = 0;
        while (i < args.length) {
            if (args[i].equalsIgnoreCase("-h") || args[i].equalsIgnoreCase("--host")) {
                host = args[i + 1];
            } else if (args[i].equalsIgnoreCase("-p") || args[i].equalsIgnoreCase("--port")) {
                port = Integer.parseInt(args[i + 1]);
            } else if (args[i].equalsIgnoreCase("-q") || args[i].equalsIgnoreCase("--quiet")) {
                quiet = true;
            } else if (args[i].equalsIgnoreCase("-d") || args[i].equalsIgnoreCase("--dir")) {
                rootDirs.add(new File(args[i + 1]).getAbsoluteFile());
            } else if (args[i].equalsIgnoreCase("--licence")) {
                System.out.println("++++httpserver Copyright (c) 2012-2013 by Paul S. Hawke, 2001,2005-2013 by Jarno Elonen, 2010 by Konstantinos Togias\n\nRedistribution and use in source and binary forms, with or without\nmodification, are permitted provided that the following conditions\nare met:\n\nRedistributions of source code must retain the above copyright notice,\nthis list of conditions and the following disclaimer. Redistributions in\nbinary form must reproduce the above copyright notice, this list of\nconditions and the following disclaimer in the documentation and/or other\nmaterials provided with the distribution. The name of the author may not\nbe used to endorse or promote products derived from this software without\nspecific prior written permission. \n \nTHIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR\nIMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES\nOF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.\nIN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,\nINCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT\nNOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,\nDATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY\nTHEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT\n(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE\nOF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.\n");
            } else if (args[i].startsWith("-X:")) {
                int dot = args[i].indexOf(61);
                if (dot > 0) {
                    options.put(args[i].substring(0, dot), args[i].substring(dot + 1, args[i].length()));
                }
            }
            i++;
        }
        if (rootDirs.isEmpty()) {
            rootDirs.add(new File(".").getAbsoluteFile());
        }
        options.put(CrashConst.KEY_HOST, host);
        options.put("port", port);
        options.put("quiet", String.valueOf(quiet));
        StringBuilder sb = new StringBuilder();
        for (File dir : rootDirs) {
            if (sb.length() > 0) {
                sb.append(SOAP.DELIM);
            }
            try {
                sb.append(dir.getCanonicalPath());
            } catch (IOException e) {
            }
        }
        options.put("home", sb.toString());
        Iterator it = ServiceLoader.load(WebServerPluginInfo.class).iterator();
        while (it.hasNext()) {
            WebServerPluginInfo info = (WebServerPluginInfo) it.next();
            for (String mime : info.getMimeTypes()) {
                String[] indexFiles = info.getIndexFilesForMimeType(mime);
                if (!quiet) {
                    System.out.print("++++httpserver # Found plugin for Mime type: \"" + mime + "\"");
                    if (indexFiles != null) {
                        System.out.print(" (serving index files: ");
                        for (String indexFile : indexFiles) {
                            System.out.print(indexFile + " ");
                        }
                    }
                    System.out.println(").");
                }
                registerPluginForMimeType(indexFiles, mime, info.getWebServerPlugin(mime), options);
            }
        }
        ServerRunner.executeInstance(new SimpleWebServer(host, port, (List) rootDirs, quiet));
    }

    protected static void registerPluginForMimeType(String[] indexFiles, String mimeType, WebServerPlugin plugin, Map<String, String> commandLineOptions) {
        if (mimeType != null && plugin != null) {
            if (indexFiles != null) {
                for (String filename : indexFiles) {
                    int dot = filename.lastIndexOf(46);
                    if (dot >= 0) {
                        MIME_TYPES.put(filename.substring(dot + 1).toLowerCase(), mimeType);
                    }
                }
                INDEX_FILE_NAMES.addAll(Arrays.asList(indexFiles));
            }
            mimeTypeHandlers.put(mimeType, plugin);
            plugin.initialize(commandLineOptions);
        }
    }

    public SimpleWebServer(String host, int port, File wwwroot, boolean quiet) {
        super(host, port);
        this.quiet = quiet;
        this.rootDirs = new ArrayList();
        this.rootDirs.add(wwwroot);
        init();
    }

    public SimpleWebServer(String host, int port, List<File> wwwroots, boolean quiet) {
        super(host, port);
        this.quiet = quiet;
        this.rootDirs = new ArrayList(wwwroots);
        init();
    }

    private boolean canServeUri(String uri, File homeDir) {
        boolean canServeUri = new File(homeDir, uri).exists();
        if (canServeUri) {
            return canServeUri;
        }
        WebServerPlugin plugin = (WebServerPlugin) mimeTypeHandlers.get(getMimeTypeForFile(uri));
        if (plugin != null) {
            return plugin.canServeUri(uri, homeDir);
        }
        return canServeUri;
    }

    private String encodeUri(String uri) {
        String newUri = "";
        StringTokenizer st = new StringTokenizer(uri, "/ ", true);
        while (st.hasMoreTokens()) {
            String tok = st.nextToken();
            if (tok.equals("/")) {
                newUri = new StringBuilder(String.valueOf(newUri)).append("/").toString();
            } else if (tok.equals(" ")) {
                newUri = new StringBuilder(String.valueOf(newUri)).append("%20").toString();
            } else {
                try {
                    newUri = new StringBuilder(String.valueOf(newUri)).append(URLEncoder.encode(tok, "UTF-8")).toString();
                } catch (UnsupportedEncodingException e) {
                }
            }
        }
        return newUri;
    }

    private String findIndexFileInDirectory(File directory) {
        for (String fileName : INDEX_FILE_NAMES) {
            if (new File(directory, fileName).isFile()) {
                return fileName;
            }
        }
        return null;
    }

    protected Response getForbiddenResponse(String s) {
        return newFixedLengthResponse(Status.FORBIDDEN, "text/plain", "FORBIDDEN: " + s);
    }

    protected Response getInternalErrorResponse(String s) {
        return newFixedLengthResponse(Status.INTERNAL_ERROR, "text/plain", "INTERNAL ERROR: " + s);
    }

    private String getMimeTypeForFile(String uri) {
        int dot = uri.lastIndexOf(46);
        String mime = null;
        if (dot >= 0) {
            mime = (String) MIME_TYPES.get(uri.substring(dot + 1).toLowerCase());
        }
        return mime == null ? "application/octet-stream" : mime;
    }

    protected Response getNotFoundResponse() {
        return newFixedLengthResponse(Status.NOT_FOUND, "text/plain", "Error 404, file not found.");
    }

    public void init() {
    }

    protected String listDirectory(String uri, File f) {
        String heading = "Directory " + uri;
        StringBuilder msg = new StringBuilder("<html><head><title>" + heading + "</title><style><!--\n" + "span.dirname { font-weight: bold; }\n" + "span.filesize { font-size: 75%; }\n" + "// -->\n" + "</style>" + "</head><body><h1>" + heading + "</h1>");
        String up = null;
        if (uri.length() > 1) {
            String u = uri.substring(0, uri.length() - 1);
            int slash = u.lastIndexOf(47);
            if (slash >= 0 && slash < u.length()) {
                up = uri.substring(0, slash + 1);
            }
        }
        List<String> files = Arrays.asList(f.list(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return new File(dir, name).isFile();
            }
        }));
        Collections.sort(files);
        List<String> directories = Arrays.asList(f.list(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return new File(dir, name).isDirectory();
            }
        }));
        Collections.sort(directories);
        if (up != null || directories.size() + files.size() > 0) {
            msg.append("<ul>");
            if (up != null || directories.size() > 0) {
                msg.append("<section class=\"directories\">");
                if (up != null) {
                    msg.append("<li><a rel=\"directory\" href=\"").append(up).append("\"><span class=\"dirname\">..</span></a></b></li>");
                }
                for (String directory : directories) {
                    String dir = new StringBuilder(String.valueOf(directory)).append("/").toString();
                    msg.append("<li><a rel=\"directory\" href=\"").append(encodeUri(new StringBuilder(String.valueOf(uri)).append(dir).toString())).append("\"><span class=\"dirname\">").append(dir).append("</span></a></b></li>");
                }
                msg.append("</section>");
            }
            if (files.size() > 0) {
                msg.append("<section class=\"files\">");
                for (String file : files) {
                    msg.append("<li><a href=\"").append(encodeUri(new StringBuilder(String.valueOf(uri)).append(file).toString())).append("\"><span class=\"filename\">").append(file).append("</span></a>");
                    long len = new File(f, file).length();
                    msg.append("&nbsp;<span class=\"filesize\">(");
                    if (len < 1024) {
                        msg.append(len).append(" bytes");
                    } else if (len < 1048576) {
                        msg.append(len / 1024).append(".").append(((len % 1024) / 10) % 100).append(" KB");
                    } else {
                        msg.append(len / 1048576).append(".").append(((len % 1048576) / 10000) % 100).append(" MB");
                    }
                    msg.append(")</span></li>");
                }
                msg.append("</section>");
            }
            msg.append("</ul>");
        }
        msg.append("</body></html>");
        return msg.toString();
    }

    public Response newFixedLengthResponse(IStatus status, String mimeType, String message) {
        Response response = super.newFixedLengthResponse(status, mimeType, message);
        response.addHeader("Accept-Ranges", HTTP.CONTENT_RANGE_BYTES);
        return response;
    }

    private Response respond(Map<String, String> headers, IHTTPSession session, String uri) {
        uri = uri.trim().replace(File.separatorChar, '/');
        if (uri.indexOf(63) >= 0) {
            uri = uri.substring(0, uri.indexOf(63));
        }
        if (uri.contains("../")) {
            return getForbiddenResponse("Won't serve ../ for security reasons.");
        }
        boolean canServeUri = false;
        File homeDir = null;
        int i = 0;
        while (!canServeUri && i < this.rootDirs.size()) {
            homeDir = (File) this.rootDirs.get(i);
            canServeUri = canServeUri(uri, homeDir);
            i++;
        }
        if (!canServeUri) {
            return getNotFoundResponse();
        }
        File f = new File(homeDir, uri);
        if (f.isDirectory()) {
            if (!uri.endsWith("/")) {
                uri = new StringBuilder(String.valueOf(uri)).append("/").toString();
                Response res = newFixedLengthResponse(Status.REDIRECT, "text/html", "<html><body>Redirected: <a href=\"" + uri + "\">" + uri + "</a></body></html>");
                res.addHeader(HTTP.LOCATION, uri);
                return res;
            }
        }
        if (f.isDirectory()) {
            String indexFile = findIndexFileInDirectory(f);
            if (indexFile != null) {
                return respond(headers, session, new StringBuilder(String.valueOf(uri)).append(indexFile).toString());
            } else if (!f.canRead()) {
                return getForbiddenResponse("No directory listing.");
            } else {
                return newFixedLengthResponse(Status.OK, "text/html", listDirectory(uri, f));
            }
        }
        Response response;
        String mimeTypeForFile = getMimeTypeForFile(uri);
        WebServerPlugin plugin = (WebServerPlugin) mimeTypeHandlers.get(mimeTypeForFile);
        if (plugin == null || !plugin.canServeUri(uri, homeDir)) {
            response = serveFile(uri, headers, f, mimeTypeForFile);
        } else {
            response = plugin.serveFile(uri, headers, session, f, mimeTypeForFile);
            if (response != null && (response instanceof InternalRewrite)) {
                InternalRewrite rewrite = (InternalRewrite) response;
                return respond(rewrite.getHeaders(), session, rewrite.getUri());
            }
        }
        if (response == null) {
            response = getNotFoundResponse();
        }
        return response;
    }

    public Response serve(IHTTPSession session) {
        Map<String, String> header = session.getHeaders();
        Map<String, String> parms = session.getParms();
        String uri = session.getUri();
        if (!this.quiet) {
            System.out.println("++++httpserver " + session.getMethod() + " '" + uri + "' ");
            for (String value : header.keySet()) {
                System.out.println("++++httpserver   HDR: '" + value + "' = '" + ((String) header.get(value)) + "'");
            }
            for (String value2 : parms.keySet()) {
                System.out.println("++++httpserver   PRM: '" + value2 + "' = '" + ((String) parms.get(value2)) + "'");
            }
        }
        for (File homeDir : this.rootDirs) {
            if (!homeDir.isDirectory()) {
                return getInternalErrorResponse("given path is not a directory (" + homeDir + ").");
            }
        }
        return respond(Collections.unmodifiableMap(header), session, uri);
    }

    Response serveFile(String uri, Map<String, String> header, File file, String mime) {
        try {
            boolean headerIfNoneMatchPresentAndMatching;
            long fileLen;
            Response res;
            String etag = Integer.toHexString((file.getAbsolutePath() + file.lastModified() + file.length()).hashCode());
            long startFrom = 0;
            long endAt = -1;
            String range = (String) header.get("range");
            if (range != null) {
                if (range.startsWith("bytes=")) {
                    range = range.substring("bytes=".length());
                    int minus = range.indexOf(45);
                    if (minus > 0) {
                        try {
                            startFrom = Long.parseLong(range.substring(0, minus));
                            endAt = Long.parseLong(range.substring(minus + 1));
                        } catch (NumberFormatException e) {
                        }
                    }
                }
            }
            String ifRange = (String) header.get("if-range");
            boolean headerIfRangeMissingOrMatching = ifRange == null || etag.equals(ifRange);
            String ifNoneMatch = (String) header.get("if-none-match");
            if (ifNoneMatch != null) {
                if (ifNoneMatch.equals(NetDiagnoseCheckTools.NO_CHECK_FLAG) || ifNoneMatch.equals(etag)) {
                    headerIfNoneMatchPresentAndMatching = true;
                    fileLen = file.length();
                    if (headerIfRangeMissingOrMatching || range == null || startFrom < 0 || startFrom >= fileLen) {
                        if (!headerIfRangeMissingOrMatching && range != null && startFrom >= fileLen) {
                            res = newFixedLengthResponse(Status.RANGE_NOT_SATISFIABLE, "text/plain", "");
                            res.addHeader(HTTP.CONTENT_RANGE, "bytes */" + fileLen);
                            res.addHeader("ETag", etag);
                            return res;
                        } else if (range != null && headerIfNoneMatchPresentAndMatching) {
                            res = newFixedLengthResponse(Status.NOT_MODIFIED, mime, "");
                            res.addHeader("ETag", etag);
                            return res;
                        } else if (headerIfRangeMissingOrMatching && headerIfNoneMatchPresentAndMatching) {
                            res = newFixedLengthResponse(Status.NOT_MODIFIED, mime, "");
                            res.addHeader("ETag", etag);
                            return res;
                        } else {
                            res = newFixedFileResponse(file, mime);
                            res.addHeader(HTTP.CONTENT_LENGTH, fileLen);
                            res.addHeader("ETag", etag);
                            return res;
                        }
                    } else if (headerIfNoneMatchPresentAndMatching) {
                        res = newFixedLengthResponse(Status.NOT_MODIFIED, mime, "");
                        res.addHeader("ETag", etag);
                        return res;
                    } else {
                        if (endAt < 0) {
                            endAt = fileLen - 1;
                        }
                        long newLen = (endAt - startFrom) + 1;
                        if (newLen < 0) {
                            newLen = 0;
                        }
                        FileInputStream fis = new FileInputStream(file);
                        fis.skip(startFrom);
                        res = newFixedLengthResponse(Status.PARTIAL_CONTENT, mime, fis, newLen);
                        res.addHeader("Accept-Ranges", HTTP.CONTENT_RANGE_BYTES);
                        res.addHeader(HTTP.CONTENT_LENGTH, newLen);
                        res.addHeader(HTTP.CONTENT_RANGE, "bytes " + startFrom + "-" + endAt + "/" + fileLen);
                        res.addHeader("ETag", etag);
                        return res;
                    }
                }
            }
            headerIfNoneMatchPresentAndMatching = false;
            fileLen = file.length();
            if (headerIfRangeMissingOrMatching) {
            }
            if (!headerIfRangeMissingOrMatching) {
            }
            if (range != null) {
            }
            if (headerIfRangeMissingOrMatching) {
            }
            res = newFixedFileResponse(file, mime);
            res.addHeader(HTTP.CONTENT_LENGTH, fileLen);
            res.addHeader("ETag", etag);
            return res;
        } catch (IOException e2) {
            return getForbiddenResponse("Reading file failed.");
        }
    }

    private Response newFixedFileResponse(File file, String mime) throws FileNotFoundException {
        Response res = newFixedLengthResponse(Status.OK, mime, new FileInputStream(file), (long) ((int) file.length()));
        res.addHeader("Accept-Ranges", HTTP.CONTENT_RANGE_BYTES);
        return res;
    }
}
