package org.cybergarage.upnp.ssdp;

import java.io.InputStream;
import org.cybergarage.http.HTTP;
import org.cybergarage.http.HTTPRequest;

public class SSDPRequest extends HTTPRequest {
    public SSDPRequest() {
        setVersion("1.1");
    }

    public SSDPRequest(InputStream in) {
        super(in);
    }

    public void setNT(String value) {
        setHeader(HTTP.NT, value);
    }

    public String getNT() {
        return getHeaderValue(HTTP.NT);
    }

    public void setNTS(String value) {
        setHeader(HTTP.NTS, value);
    }

    public String getNTS() {
        return getHeaderValue(HTTP.NTS);
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

    public void setConnect(boolean keepConnect) {
        if (keepConnect) {
            setHeader(HTTP.IGALACONNECTION, HTTP.KEEP_ALIVE);
        } else {
            setHeader(HTTP.IGALACONNECTION, HTTP.CLOSE);
        }
    }

    public void setIGALAPORT(int port) {
        setHeader(HTTP.IGALAPORT, port);
    }

    public void setIGALAVERSION(int version) {
        setHeader(HTTP.IGALAVERSION, version);
    }

    public void setIGALAUDPPORT(int port) {
        setHeader(HTTP.IGALAUDPPORT, port);
    }

    public void setIGALADEVICE(int deviceName) {
        setHeader(HTTP.IGALADEVICE, deviceName);
    }

    public void setDEVICEVERSION(int version) {
        setHeader(HTTP.DEVICEVERSION, version);
    }

    public String getFileMd5() {
        return getHeaderValue(HTTP.FILEMD5);
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

    public void setLeaseTime(int len) {
        setHeader(HTTP.CACHE_CONTROL, "max-age=" + Integer.toString(len));
    }

    public int getLeaseTime() {
        return SSDP.getLeaseTime(getHeaderValue(HTTP.CACHE_CONTROL));
    }
}
