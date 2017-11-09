package org.cybergarage.http;

import java.io.InputStream;

public class HTTPResponse extends HTTPPacket {
    private int statusCode;

    public HTTPResponse() {
        this.statusCode = 0;
        setVersion("1.1");
        setContentType(HTML.CONTENT_TYPE);
        setServer(HTTPServer.getName());
        setContent("");
    }

    public HTTPResponse(HTTPResponse httpRes) {
        this.statusCode = 0;
        set((HTTPPacket) httpRes);
    }

    public HTTPResponse(InputStream in) {
        super(in);
        this.statusCode = 0;
    }

    public HTTPResponse(HTTPSocket httpSock) {
        this(httpSock.getInputStream());
    }

    public void setStatusCode(int code) {
        this.statusCode = code;
    }

    public int getStatusCode() {
        if (this.statusCode != 0) {
            return this.statusCode;
        }
        return new HTTPStatus(getFirstLine()).getStatusCode();
    }

    public boolean isSuccessful() {
        return HTTPStatus.isSuccessful(getStatusCode());
    }

    public String getStatusLineString() {
        return "HTTP/" + getVersion() + " " + getStatusCode() + " " + HTTPStatus.code2String(this.statusCode) + HTTP.CRLF;
    }

    public String getHeader() {
        StringBuffer str = new StringBuffer();
        str.append(getStatusLineString());
        str.append(getHeaderString());
        return str.toString();
    }

    public String toString() {
        StringBuffer str = new StringBuffer();
        str.append(getStatusLineString());
        str.append(getHeaderString());
        str.append(HTTP.CRLF);
        str.append(getContentString());
        return str.toString();
    }

    public void print() {
        System.out.println("------------------------------DUMP HTTPResponse [Start]------------------------------");
        System.out.println(toString().replace(HTTP.CRLF, HTTP.TAB));
        System.out.println("-------------------------------DUMP HTTPResponse [End]-------------------------------");
    }
}
