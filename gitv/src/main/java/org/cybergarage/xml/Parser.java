package org.cybergarage.xml;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import org.cybergarage.http.HTTP;
import org.cybergarage.http.HTTPRequest;
import org.cybergarage.http.HTTPResponse;

public abstract class Parser {
    public abstract Node parse(InputStream inputStream) throws ParserException;

    public Node parse(URL locationURL) throws ParserException {
        String host = locationURL.getHost();
        int port = locationURL.getPort();
        if (port == -1) {
            port = 80;
        }
        String uri = locationURL.getPath();
        try {
            HttpURLConnection urlCon = (HttpURLConnection) locationURL.openConnection();
            urlCon.setConnectTimeout(5000);
            urlCon.setReadTimeout(10000);
            urlCon.setRequestMethod(HTTP.GET);
            urlCon.setRequestProperty(HTTP.CONTENT_LENGTH, "0");
            if (host != null) {
                urlCon.setRequestProperty(HTTP.HOST, host);
            }
            InputStream urlIn = urlCon.getInputStream();
            Node rootElem = parse(urlIn);
            urlIn.close();
            urlCon.disconnect();
            return rootElem;
        } catch (Exception e) {
            e.printStackTrace();
            HTTPRequest httpReq = new HTTPRequest();
            httpReq.setMethod(HTTP.GET);
            httpReq.setURI(locationURL.toString());
            HTTPResponse httpRes = httpReq.post(host, port);
            if (httpRes.isSuccessful()) {
                return parse(new ByteArrayInputStream(new String(httpRes.getContent()).getBytes()));
            }
            throw new ParserException("HTTP comunication failed: no answer from peer.Unable to retrive resoure -> " + locationURL.toString());
        }
    }

    public Node parse(File descriptionFile) throws ParserException {
        try {
            InputStream fileIn = new FileInputStream(descriptionFile);
            Node root = parse(fileIn);
            fileIn.close();
            return root;
        } catch (Exception e) {
            throw new ParserException(e);
        }
    }

    public Node parse(String descr) throws ParserException {
        try {
            return parse(new ByteArrayInputStream(descr.getBytes()));
        } catch (Exception e) {
            throw new ParserException(e);
        }
    }
}
