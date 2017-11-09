package org.xbill.DNS;

import java.io.IOException;

public class GPOSRecord extends Record {
    private static final long serialVersionUID = -6349714958085750705L;
    private byte[] altitude;
    private byte[] latitude;
    private byte[] longitude;

    GPOSRecord() {
    }

    Record getObject() {
        return new GPOSRecord();
    }

    private void validate(double longitude, double latitude) throws IllegalArgumentException {
        if (longitude < -90.0d || longitude > 90.0d) {
            throw new IllegalArgumentException(new StringBuffer().append("illegal longitude ").append(longitude).toString());
        } else if (latitude < -180.0d || latitude > 180.0d) {
            throw new IllegalArgumentException(new StringBuffer().append("illegal latitude ").append(latitude).toString());
        }
    }

    public GPOSRecord(Name name, int dclass, long ttl, double longitude, double latitude, double altitude) {
        super(name, 27, dclass, ttl);
        validate(longitude, latitude);
        this.longitude = Double.toString(longitude).getBytes();
        this.latitude = Double.toString(latitude).getBytes();
        this.altitude = Double.toString(altitude).getBytes();
    }

    public GPOSRecord(Name name, int dclass, long ttl, String longitude, String latitude, String altitude) {
        super(name, 27, dclass, ttl);
        try {
            this.longitude = byteArrayFromString(longitude);
            this.latitude = byteArrayFromString(latitude);
            validate(getLongitude(), getLatitude());
            this.altitude = byteArrayFromString(altitude);
        } catch (TextParseException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    void rrFromWire(DNSInput in) throws IOException {
        this.longitude = in.readCountedString();
        this.latitude = in.readCountedString();
        this.altitude = in.readCountedString();
        try {
            validate(getLongitude(), getLatitude());
        } catch (IllegalArgumentException e) {
            throw new WireParseException(e.getMessage());
        }
    }

    void rdataFromString(Tokenizer st, Name origin) throws IOException {
        try {
            this.longitude = byteArrayFromString(st.getString());
            this.latitude = byteArrayFromString(st.getString());
            this.altitude = byteArrayFromString(st.getString());
            try {
                validate(getLongitude(), getLatitude());
            } catch (IllegalArgumentException e) {
                throw new WireParseException(e.getMessage());
            }
        } catch (TextParseException e2) {
            throw st.exception(e2.getMessage());
        }
    }

    String rrToString() {
        StringBuffer sb = new StringBuffer();
        sb.append(byteArrayToString(this.longitude, true));
        sb.append(" ");
        sb.append(byteArrayToString(this.latitude, true));
        sb.append(" ");
        sb.append(byteArrayToString(this.altitude, true));
        return sb.toString();
    }

    public String getLongitudeString() {
        return byteArrayToString(this.longitude, false);
    }

    public double getLongitude() {
        return Double.parseDouble(getLongitudeString());
    }

    public String getLatitudeString() {
        return byteArrayToString(this.latitude, false);
    }

    public double getLatitude() {
        return Double.parseDouble(getLatitudeString());
    }

    public String getAltitudeString() {
        return byteArrayToString(this.altitude, false);
    }

    public double getAltitude() {
        return Double.parseDouble(getAltitudeString());
    }

    void rrToWire(DNSOutput out, Compression c, boolean canonical) {
        out.writeCountedString(this.longitude);
        out.writeCountedString(this.latitude);
        out.writeCountedString(this.altitude);
    }
}
