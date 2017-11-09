package org.cybergarage.upnp.ssdp;

import java.io.InputStream;
import org.cybergarage.http.HTTP;
import org.cybergarage.http.HTTPResponse;

public class SSDPResponse extends HTTPResponse {
    public SSDPResponse() {
        setVersion("1.1");
    }

    public SSDPResponse(InputStream in) {
        super(in);
    }

    public void setST(String value) {
        setHeader(HTTP.ST, value);
    }

    public String getST() {
        return getHeaderValue(HTTP.ST);
    }

    public void setLocation(String value) {
        setHeader(HTTP.LOCATION, value);
    }

    public String getLocation() {
        return getHeaderValue(HTTP.LOCATION);
    }

    public void setUSN(String value) {
        setHeader(HTTP.USN, value);
    }

    public String getUSN() {
        return getHeaderValue(HTTP.USN);
    }

    public void setMYNAME(String value) {
        setHeader(HTTP.MYNAME, value);
    }

    public String getMYNAME() {
        return getHeaderValue(HTTP.MYNAME);
    }

    public void setFileMd5(String value) {
        setHeader(HTTP.FILEMD5, value);
    }

    public String getFileMd5() {
        return getHeaderValue(HTTP.FILEMD5);
    }

    public void setConnect(boolean keepConnect) {
        if (keepConnect) {
            setHeader(HTTP.IGALACONNECTION, HTTP.KEEP_ALIVE);
        } else {
            setHeader(HTTP.IGALACONNECTION, HTTP.CLOSE);
        }
    }

    public void setIGALAVERSION(int version) {
        setHeader(HTTP.IGALAVERSION, version);
    }

    public void setIGALADEVICE(int deviceName) {
        setHeader(HTTP.IGALADEVICE, deviceName);
    }

    public void setDEVICEVERSION(int version) {
        setHeader(HTTP.DEVICEVERSION, version);
    }

    public void setIGALAPORT(int port) {
        setHeader(HTTP.IGALAPORT, port);
    }

    public void setIGALAUDPPORT(int port) {
        setHeader(HTTP.IGALAUDPPORT, port);
    }

    public void setLeaseTime(int len) {
        setHeader(HTTP.CACHE_CONTROL, "max-age=" + Integer.toString(len));
    }

    public int getLeaseTime() {
        return SSDP.getLeaseTime(getHeaderValue(HTTP.CACHE_CONTROL));
    }

    public String getHeader() {
        StringBuffer str = new StringBuffer();
        str.append(getStatusLineString());
        str.append(getHeaderString());
        str.append(HTTP.CRLF);
        return str.toString();
    }
}
