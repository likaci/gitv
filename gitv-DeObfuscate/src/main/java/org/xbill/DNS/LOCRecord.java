package org.xbill.DNS;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import org.xbill.DNS.Tokenizer.Token;

public class LOCRecord extends Record {
    private static final long serialVersionUID = 9058224788126750409L;
    private static NumberFormat w2 = new DecimalFormat();
    private static NumberFormat w3 = new DecimalFormat();
    private long altitude;
    private long hPrecision;
    private long latitude;
    private long longitude;
    private long size;
    private long vPrecision;

    static {
        w2.setMinimumIntegerDigits(2);
        w3.setMinimumIntegerDigits(3);
    }

    LOCRecord() {
    }

    Record getObject() {
        return new LOCRecord();
    }

    public LOCRecord(Name name, int dclass, long ttl, double latitude, double longitude, double altitude, double size, double hPrecision, double vPrecision) {
        super(name, 29, dclass, ttl);
        this.latitude = (long) (((3600.0d * latitude) * 1000.0d) + 2.147483648E9d);
        this.longitude = (long) (((3600.0d * longitude) * 1000.0d) + 2.147483648E9d);
        this.altitude = (long) ((100000.0d + altitude) * 100.0d);
        this.size = (long) (100.0d * size);
        this.hPrecision = (long) (100.0d * hPrecision);
        this.vPrecision = (long) (100.0d * vPrecision);
    }

    void rrFromWire(DNSInput in) throws IOException {
        if (in.readU8() != 0) {
            throw new WireParseException("Invalid LOC version");
        }
        this.size = parseLOCformat(in.readU8());
        this.hPrecision = parseLOCformat(in.readU8());
        this.vPrecision = parseLOCformat(in.readU8());
        this.latitude = in.readU32();
        this.longitude = in.readU32();
        this.altitude = in.readU32();
    }

    private double parseFixedPoint(String s) {
        if (s.matches("^-?\\d+$")) {
            return (double) Integer.parseInt(s);
        }
        if (s.matches("^-?\\d+\\.\\d*$")) {
            String[] parts = s.split("\\.");
            double value = (double) Integer.parseInt(parts[0]);
            double fraction = (double) Integer.parseInt(parts[1]);
            if (value < 0.0d) {
                fraction *= -1.0d;
            }
            return (fraction / Math.pow(10.0d, (double) parts[1].length())) + value;
        }
        throw new NumberFormatException();
    }

    private long parsePosition(Tokenizer st, String type) throws IOException {
        boolean isLatitude = type.equals("latitude");
        int min = 0;
        double sec = 0.0d;
        int deg = st.getUInt16();
        if (deg > 180 || (deg > 90 && isLatitude)) {
            throw st.exception(new StringBuffer().append("Invalid LOC ").append(type).append(" degrees").toString());
        }
        String s = st.getString();
        try {
            min = Integer.parseInt(s);
            if (min < 0 || min > 59) {
                throw st.exception(new StringBuffer().append("Invalid LOC ").append(type).append(" minutes").toString());
            }
            sec = parseFixedPoint(st.getString());
            if (sec < 0.0d || sec >= 60.0d) {
                throw st.exception(new StringBuffer().append("Invalid LOC ").append(type).append(" seconds").toString());
            }
            s = st.getString();
            if (s.length() != 1) {
                throw st.exception(new StringBuffer().append("Invalid LOC ").append(type).toString());
            }
            long value = (long) (1000.0d * (((double) (60 * (((long) min) + (60 * ((long) deg))))) + sec));
            char c = Character.toUpperCase(s.charAt(0));
            if ((isLatitude && c == 'S') || (!isLatitude && c == 'W')) {
                value = -value;
            } else if ((isLatitude && c != 'N') || !(isLatitude || c == 'E')) {
                throw st.exception(new StringBuffer().append("Invalid LOC ").append(type).toString());
            }
            return value + 2147483648L;
        } catch (NumberFormatException e) {
        }
    }

    private long parseDouble(Tokenizer st, String type, boolean required, long min, long max, long defaultValue) throws IOException {
        Token token = st.get();
        if (!token.isEOL()) {
            String s = token.value;
            if (s.length() > 1 && s.charAt(s.length() - 1) == 'm') {
                s = s.substring(0, s.length() - 1);
            }
            try {
                long value = (long) (100.0d * parseFixedPoint(s));
                if (value >= min && value <= max) {
                    return value;
                }
                throw st.exception(new StringBuffer().append("Invalid LOC ").append(type).toString());
            } catch (NumberFormatException e) {
                throw st.exception(new StringBuffer().append("Invalid LOC ").append(type).toString());
            }
        } else if (required) {
            throw st.exception(new StringBuffer().append("Invalid LOC ").append(type).toString());
        } else {
            st.unget();
            return defaultValue;
        }
    }

    void rdataFromString(Tokenizer st, Name origin) throws IOException {
        this.latitude = parsePosition(st, "latitude");
        this.longitude = parsePosition(st, "longitude");
        this.altitude = parseDouble(st, "altitude", true, -10000000, 4284967295L, 0) + 10000000;
        this.size = parseDouble(st, "size", false, 0, 9000000000L, 100);
        this.hPrecision = parseDouble(st, "horizontal precision", false, 0, 9000000000L, 1000000);
        this.vPrecision = parseDouble(st, "vertical precision", false, 0, 9000000000L, 1000);
    }

    private void renderFixedPoint(StringBuffer sb, NumberFormat formatter, long value, long divisor) {
        sb.append(value / divisor);
        value %= divisor;
        if (value != 0) {
            sb.append(".");
            sb.append(formatter.format(value));
        }
    }

    private String positionToString(long value, char pos, char neg) {
        char direction;
        StringBuffer sb = new StringBuffer();
        long temp = value - 2147483648L;
        if (temp < 0) {
            temp = -temp;
            direction = neg;
        } else {
            direction = pos;
        }
        sb.append(temp / 3600000);
        temp %= 3600000;
        sb.append(" ");
        sb.append(temp / 60000);
        temp %= 60000;
        sb.append(" ");
        renderFixedPoint(sb, w3, temp, 1000);
        sb.append(" ");
        sb.append(direction);
        return sb.toString();
    }

    String rrToString() {
        StringBuffer sb = new StringBuffer();
        sb.append(positionToString(this.latitude, 'N', 'S'));
        sb.append(" ");
        sb.append(positionToString(this.longitude, 'E', 'W'));
        sb.append(" ");
        renderFixedPoint(sb, w2, this.altitude - 10000000, 100);
        sb.append("m ");
        renderFixedPoint(sb, w2, this.size, 100);
        sb.append("m ");
        renderFixedPoint(sb, w2, this.hPrecision, 100);
        sb.append("m ");
        renderFixedPoint(sb, w2, this.vPrecision, 100);
        sb.append("m");
        return sb.toString();
    }

    public double getLatitude() {
        return ((double) (this.latitude - 2147483648L)) / 3600000.0d;
    }

    public double getLongitude() {
        return ((double) (this.longitude - 2147483648L)) / 3600000.0d;
    }

    public double getAltitude() {
        return ((double) (this.altitude - 10000000)) / 100.0d;
    }

    public double getSize() {
        return ((double) this.size) / 100.0d;
    }

    public double getHPrecision() {
        return ((double) this.hPrecision) / 100.0d;
    }

    public double getVPrecision() {
        return ((double) this.vPrecision) / 100.0d;
    }

    void rrToWire(DNSOutput out, Compression c, boolean canonical) {
        out.writeU8(0);
        out.writeU8(toLOCformat(this.size));
        out.writeU8(toLOCformat(this.hPrecision));
        out.writeU8(toLOCformat(this.vPrecision));
        out.writeU32(this.latitude);
        out.writeU32(this.longitude);
        out.writeU32(this.altitude);
    }

    private static long parseLOCformat(int b) throws WireParseException {
        long out = (long) (b >> 4);
        int exp = b & 15;
        if (out > 9 || exp > 9) {
            throw new WireParseException("Invalid LOC Encoding");
        }
        int exp2 = exp;
        while (true) {
            exp = exp2 - 1;
            if (exp2 <= 0) {
                return out;
            }
            out *= 10;
            exp2 = exp;
        }
    }

    private int toLOCformat(long l) {
        byte exp = (byte) 0;
        while (l > 9) {
            exp = (byte) (exp + 1);
            l /= 10;
        }
        return (int) ((l << 4) + ((long) exp));
    }
}
