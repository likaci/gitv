package org.cybergarage.upnp.ssdp;

import android.text.TextUtils;
import com.gala.android.dlna.sdk.stddmrcontroller.Util;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import org.cybergarage.http.HTTP;
import org.cybergarage.http.HTTPHeader;
import org.cybergarage.soap.SOAP;
import org.cybergarage.upnp.device.MAN;
import org.cybergarage.upnp.device.NT;
import org.cybergarage.upnp.device.NTS;
import org.cybergarage.upnp.device.ST;
import org.cybergarage.upnp.device.USN;
import org.cybergarage.util.Debug;

public class SSDPPacket {
    private DatagramPacket dgmPacket = null;
    private String localAddr = "";
    private Map<String, String> mHeaderMap = new HashMap();
    public byte[] packetBytes = null;
    private long timeStamp;

    public SSDPPacket(byte[] buf, int length) {
        this.packetBytes = new byte[length];
        for (int i = 0; i < length; i++) {
            this.packetBytes[i] = buf[i];
        }
        this.dgmPacket = new DatagramPacket(this.packetBytes, length);
    }

    public DatagramPacket getDatagramPacket() {
        return this.dgmPacket;
    }

    public void setLocalAddress(String addr) {
        this.localAddr = addr;
    }

    public String getLocalAddress() {
        return this.localAddr;
    }

    public void setTimeStamp(long value) {
        this.timeStamp = value;
    }

    public long getTimeStamp() {
        return this.timeStamp;
    }

    public InetAddress getRemoteInetAddress() {
        return getDatagramPacket().getAddress();
    }

    public String getRemoteAddress() {
        return getDatagramPacket().getAddress().getHostAddress();
    }

    public int getRemotePort() {
        return getDatagramPacket().getPort();
    }

    public byte[] getData() {
        if (this.packetBytes != null) {
            return this.packetBytes;
        }
        this.mHeaderMap.clear();
        this.packetBytes = getDatagramPacket().getData();
        return this.packetBytes;
    }

    public void setData(byte[] data) {
        if (this.dgmPacket != null) {
            this.dgmPacket.setData(data);
            this.packetBytes = data;
        }
    }

    private String getValue(byte[] data, String name) {
        String value = (String) this.mHeaderMap.get(name);
        if (!TextUtils.isEmpty(value)) {
            return value;
        }
        value = HTTPHeader.getValue(data, name);
        this.mHeaderMap.put(name, value);
        return value;
    }

    public String getHost() {
        return getValue(getData(), HTTP.HOST);
    }

    public String getCacheControl() {
        return getValue(getData(), HTTP.CACHE_CONTROL);
    }

    public String getLocation() {
        return getValue(getData(), HTTP.LOCATION);
    }

    public String getMAN() {
        return getValue(getData(), HTTP.MAN);
    }

    public String getST() {
        return getValue(getData(), HTTP.ST);
    }

    public String getNT() {
        return getValue(getData(), HTTP.NT);
    }

    public String getNTS() {
        return getValue(getData(), HTTP.NTS);
    }

    public String getMyName() {
        return getValue(getData(), HTTP.MYNAME);
    }

    public String getFileMd5() {
        return getValue(getData(), HTTP.FILEMD5);
    }

    public String getFriendlyName() {
        return getValue(getData(), HTTP.MYNAME);
    }

    public int getGalaHttpPort() {
        String port = getValue(getData(), HTTP.IGALAPORT);
        if (port == null || port.length() == 0) {
            return 39521;
        }
        return Integer.parseInt(port);
    }

    public int getGalaUDPHttpPort() {
        String port = getValue(getData(), HTTP.IGALAUDPPORT);
        if (port == null || port.length() == 0) {
            return 39522;
        }
        int i = 39522;
        try {
            return Integer.parseInt(port);
        } catch (NumberFormatException e) {
            Debug.message("Invalid port = " + port);
            return i;
        }
    }

    public int getGalaVersion() {
        String version = getValue(getData(), HTTP.IGALAVERSION);
        if (version == null || version.length() == 0) {
            return -1;
        }
        return Integer.parseInt(version);
    }

    public int getGalaDeviceType() {
        String device = getValue(getData(), HTTP.IGALADEVICE);
        if (device == null || device.length() == 0) {
            return 0;
        }
        return Integer.parseInt(device);
    }

    public int getGalaDeviceVersion() {
        String device = getValue(getData(), HTTP.DEVICEVERSION);
        if (device == null || device.length() == 0) {
            return 0;
        }
        return Integer.parseInt(device);
    }

    public String getServer() {
        return getValue(getData(), HTTP.SERVER);
    }

    public String getGALAConnect() {
        return HTTPHeader.getValue(getData(), HTTP.IGALACONNECTION);
    }

    public boolean isGalaServer() {
        if (getServer() != null) {
            return getServer().toLowerCase().contains(Util.getTag(false));
        }
        return false;
    }

    public boolean isRounterServer() {
        return getNT().contains("InternetGatewayDevice") || getUSN().contains("InternetGatewayDevice");
    }

    public boolean isMicrosoftServer() {
        if (getServer() == null || !getServer().contains("Microsoft-Windows")) {
            return false;
        }
        return true;
    }

    public boolean isSupperConnectKeepAlive() {
        String galaConnect = getGALAConnect();
        if (galaConnect == null || galaConnect.length() <= 0) {
            return false;
        }
        if (galaConnect.contains(HTTP.KEEP_ALIVE)) {
            return true;
        }
        return galaConnect.contains(HTTP.CLOSE) ? false : false;
    }

    public String getUSN() {
        return HTTPHeader.getValue(getData(), HTTP.USN);
    }

    public int getMX() {
        return HTTPHeader.getIntegerValue(getData(), HTTP.MX);
    }

    public InetAddress getHostInetAddress() {
        String addrStr = "127.0.0.1";
        String host = getHost();
        int canmaIdx = host.lastIndexOf(SOAP.DELIM);
        if (canmaIdx >= 0) {
            addrStr = host.substring(0, canmaIdx);
            if (addrStr.charAt(0) == '[') {
                addrStr = addrStr.substring(1, addrStr.length());
            }
            if (addrStr.charAt(addrStr.length() - 1) == ']') {
                addrStr = addrStr.substring(0, addrStr.length() - 1);
            }
        }
        return new InetSocketAddress(addrStr, 0).getAddress();
    }

    public boolean isRootDevice() {
        if (NT.isRootDevice(getNT()) || ST.isRootDevice(getST())) {
            return true;
        }
        return USN.isRootDevice(getUSN());
    }

    public boolean isDiscover() {
        return MAN.isDiscover(getMAN());
    }

    public boolean isAlive() {
        return NTS.isAlive(getNTS());
    }

    public boolean isByeBye() {
        return NTS.isByeBye(getNTS());
    }

    public int getLeaseTime() {
        return SSDP.getLeaseTime(getCacheControl());
    }

    public String toString() {
        return new String(getData());
    }
}
