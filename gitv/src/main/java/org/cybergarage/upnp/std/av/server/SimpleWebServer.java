package org.cybergarage.upnp.std.av.server;

import com.gala.cloudui.constants.CuteConstants;
import com.gala.video.lib.share.uikit.data.data.processor.UIKitConfig;
import com.tvos.appdetailpage.client.Constants;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import org.cybergarage.http.HTTP;
import org.cybergarage.upnp.std.av.server.NanoHTTPD.Method;
import org.cybergarage.upnp.std.av.server.NanoHTTPD.Response;
import org.cybergarage.upnp.std.av.server.NanoHTTPD.Response.Status;
import org.cybergarage.upnp.std.av.server.object.item.file.FileItemNode;

public class SimpleWebServer extends NanoHTTPD {
    private static final String LICENCE = "Copyright (c) 2012-2013 by Paul S. Hawke, 2001,2005-2013 by Jarno Elonen, 2010 by Konstantinos Togias\n\nRedistribution and use in source and binary forms, with or without\nmodification, are permitted provided that the following conditions\nare met:\n\nRedistributions of source code must retain the above copyright notice,\nthis list of conditions and the following disclaimer. Redistributions in\nbinary form must reproduce the above copyright notice, this list of\nconditions and the following disclaimer in the documentation and/or other\nmaterials provided with the distribution. The name of the author may not\nbe used to endorse or promote products derived from this software without\nspecific prior written permission. \n \nTHIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR\nIMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES\nOF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.\nIN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,\nINCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT\nNOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,\nDATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY\nTHEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT\n(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE\nOF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.";
    private static final Map<String, String> MIME_TYPES = new HashMap<String, String>() {
        {
            put("css", "text/css");
            put("htm", "text/html");
            put("html", "text/html");
            put("xml", "text/xml");
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
        }
    };
    private static final String TAG = "SimpleWebServer";
    private File rootDir;

    public SimpleWebServer(String host, int port, File wwwroot) {
        super(host, port);
        this.rootDir = wwwroot;
    }

    public File getRootDir() {
        return this.rootDir;
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

    public Response serveFile(String uri, Map<String, String> header, File homeDir) {
        Response res;
        String mime;
        int dot;
        String etag;
        long startFrom;
        long endAt;
        String range;
        int minus;
        long fileLen;
        Response response = null;
        if (!homeDir.isDirectory()) {
            Response response2 = new Response(Status.INTERNAL_ERROR, "text/plain", "INTERNAL ERRROR: serveFile(): given homeDir is not a directory.");
        }
        if (response == null) {
            uri = uri.trim().replace(File.separatorChar, '/');
            if (uri.indexOf(63) >= 0) {
                uri = uri.substring(0, uri.indexOf(63));
            }
            if (uri.startsWith("src/main") || uri.endsWith("src/main") || uri.contains("../")) {
                response2 = new Response(Status.FORBIDDEN, "text/plain", "FORBIDDEN: Won't serve ../ for security reasons.");
            }
        }
        File f = new File(homeDir, uri);
        if (response == null && !f.exists()) {
            response2 = new Response(Status.NOT_FOUND, "text/plain", "Error 404, file not found.");
        }
        if (response == null && f.isDirectory()) {
            if (!uri.endsWith("/")) {
                uri = new StringBuilder(String.valueOf(uri)).append("/").toString();
                response2 = new Response(Status.REDIRECT, "text/html", "<html><body>Redirected: <a href=\"" + uri + "\">" + uri + "</a></body></html>");
                response2.addHeader(HTTP.LOCATION, uri);
            }
            if (response == null) {
                if (new File(f, "index.html").exists()) {
                    f = new File(homeDir, new StringBuilder(String.valueOf(uri)).append("/index.html").toString());
                    res = response;
                } else if (new File(f, "index.htm").exists()) {
                    f = new File(homeDir, new StringBuilder(String.valueOf(uri)).append("/index.htm").toString());
                    res = response;
                } else if (f.canRead()) {
                    String[] files = f.list();
                    String msg = "<html><body><h1>Directory " + uri + "</h1><br/>";
                    if (uri.length() > 1) {
                        String u = uri.substring(0, uri.length() - 1);
                        int slash = u.lastIndexOf(47);
                        if (slash >= 0 && slash < u.length()) {
                            msg = new StringBuilder(String.valueOf(msg)).append("<b><a href=\"").append(uri.substring(0, slash + 1)).append("\">..</a></b><br/>").toString();
                        }
                    }
                    if (files != null) {
                        for (int i = 0; i < files.length; i++) {
                            File curFile = new File(f, files[i]);
                            boolean dir = curFile.isDirectory();
                            if (dir) {
                                msg = new StringBuilder(String.valueOf(msg)).append("<b>").toString();
                                files[i] = files[i] + "/";
                            }
                            msg = new StringBuilder(String.valueOf(msg)).append("<a href=\"").append(encodeUri(new StringBuilder(String.valueOf(uri)).append(files[i]).toString())).append("\">").append(files[i]).append("</a>").toString();
                            if (curFile.isFile()) {
                                long len = curFile.length();
                                msg = new StringBuilder(String.valueOf(msg)).append(" &nbsp;<font size=2>(").toString();
                                if (len < 1024) {
                                    msg = new StringBuilder(String.valueOf(msg)).append(len).append(" bytes").toString();
                                } else if (len < 1048576) {
                                    msg = new StringBuilder(String.valueOf(msg)).append(len / 1024).append(".").append(((len % 1024) / 10) % 100).append(" KB").toString();
                                } else {
                                    msg = new StringBuilder(String.valueOf(msg)).append(len / 1048576).append(".").append(((len % 1048576) / 10) % 100).append(" MB").toString();
                                }
                                msg = new StringBuilder(String.valueOf(msg)).append(")</font>").toString();
                            }
                            msg = new StringBuilder(String.valueOf(msg)).append("<br/>").toString();
                            if (dir) {
                                msg = new StringBuilder(String.valueOf(msg)).append("</b>").toString();
                            }
                        }
                    }
                    res = new Response(new StringBuilder(String.valueOf(msg)).append("</body></html>").toString());
                } else {
                    res = new Response(Status.FORBIDDEN, "text/plain", "FORBIDDEN: No directory listing.");
                }
                if (res != null) {
                    mime = null;
                    dot = f.getCanonicalPath().lastIndexOf(46);
                    if (dot >= 0) {
                        mime = (String) MIME_TYPES.get(f.getCanonicalPath().substring(dot + 1).toLowerCase());
                    }
                    if (mime == null) {
                        mime = "application/octet-stream";
                    }
                    etag = Integer.toHexString((f.getAbsolutePath() + f.lastModified() + f.length()).hashCode());
                    startFrom = 0;
                    endAt = -1;
                    range = (String) header.get("range");
                    if (range != null && range.startsWith("bytes=")) {
                        range = range.substring("bytes=".length());
                        minus = range.indexOf(45);
                        if (minus > 0) {
                            try {
                                startFrom = Long.parseLong(range.substring(0, minus));
                                endAt = Long.parseLong(range.substring(minus + 1));
                            } catch (NumberFormatException e) {
                            }
                        }
                    }
                    try {
                        fileLen = f.length();
                        if (range != null || startFrom < 0) {
                            if (etag.equals(header.get("if-none-match"))) {
                                response2 = new Response(Status.OK, mime, new FileInputStream(f));
                                response2.addHeader(HTTP.CONTENT_LENGTH, fileLen);
                                response2.addHeader("ETag", etag);
                            } else {
                                response2 = new Response(Status.NOT_MODIFIED, mime, "");
                            }
                        } else if (startFrom >= fileLen) {
                            response2 = new Response(Status.RANGE_NOT_SATISFIABLE, "text/plain", "");
                            try {
                                response2.addHeader(HTTP.CONTENT_RANGE, "bytes 0-0/" + fileLen);
                                response2.addHeader("ETag", etag);
                            } catch (IOException e2) {
                                response2 = new Response(Status.FORBIDDEN, "text/plain", "FORBIDDEN: Reading file failed.");
                                response.addHeader("Accept-Ranges", HTTP.CONTENT_RANGE_BYTES);
                                return response;
                            }
                        } else {
                            if (endAt < 0) {
                                endAt = fileLen - 1;
                            }
                            long newLen = (endAt - startFrom) + 1;
                            if (newLen < 0) {
                                newLen = 0;
                            }
                            final long dataLen = newLen;
                            InputStream anonymousClass2 = new FileInputStream(f) {
                                public int available() throws IOException {
                                    return (int) dataLen;
                                }
                            };
                            anonymousClass2.skip(startFrom);
                            response2 = new Response(Status.PARTIAL_CONTENT, mime, anonymousClass2);
                            response2.addHeader(HTTP.CONTENT_LENGTH, dataLen);
                            response2.addHeader(HTTP.CONTENT_RANGE, "bytes " + startFrom + "-" + endAt + "/" + fileLen);
                            response2.addHeader("ETag", etag);
                        }
                    } catch (IOException e3) {
                        response = res;
                        response2 = new Response(Status.FORBIDDEN, "text/plain", "FORBIDDEN: Reading file failed.");
                        response.addHeader("Accept-Ranges", HTTP.CONTENT_RANGE_BYTES);
                        return response;
                    }
                }
                response = res;
                response.addHeader("Accept-Ranges", HTTP.CONTENT_RANGE_BYTES);
                return response;
            }
        }
        res = response;
        if (res != null) {
            response = res;
        } else {
            mime = null;
            dot = f.getCanonicalPath().lastIndexOf(46);
            if (dot >= 0) {
                mime = (String) MIME_TYPES.get(f.getCanonicalPath().substring(dot + 1).toLowerCase());
            }
            if (mime == null) {
                mime = "application/octet-stream";
            }
            etag = Integer.toHexString((f.getAbsolutePath() + f.lastModified() + f.length()).hashCode());
            startFrom = 0;
            endAt = -1;
            range = (String) header.get("range");
            range = range.substring("bytes=".length());
            minus = range.indexOf(45);
            if (minus > 0) {
                startFrom = Long.parseLong(range.substring(0, minus));
                endAt = Long.parseLong(range.substring(minus + 1));
            }
            fileLen = f.length();
            if (range != null) {
            }
            if (etag.equals(header.get("if-none-match"))) {
                response2 = new Response(Status.OK, mime, new FileInputStream(f));
                response2.addHeader(HTTP.CONTENT_LENGTH, fileLen);
                response2.addHeader("ETag", etag);
            } else {
                response2 = new Response(Status.NOT_MODIFIED, mime, "");
            }
        }
        response.addHeader("Accept-Ranges", HTTP.CONTENT_RANGE_BYTES);
        return response;
    }

    public Response serve(String uri, Method method, Map<String, String> header, Map<String, String> parms, Map<String, String> files) {
        System.out.println(method + " '" + uri + "' ");
        for (String value : header.keySet()) {
            System.out.println("  HDR: '" + value + "' = '" + ((String) header.get(value)) + "'");
        }
        for (String value2 : parms.keySet()) {
            System.out.println("  PRM: '" + value2 + "' = '" + ((String) parms.get(value2)) + "'");
        }
        for (String value22 : files.keySet()) {
            System.out.println("  UPLOADED: '" + value22 + "' = '" + ((String) files.get(value22)) + "'");
        }
        String fname = ((FileItemNode) this.mCDS.findContentNodeByID((String) parms.get("id"))).getFile().getName();
        return serveFile(uri, header, getRootDir());
    }
}
