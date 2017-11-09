package org.xbill.DNS;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Generator {
    private long current;
    public final int dclass;
    public long end;
    public final String namePattern;
    public final Name origin;
    public final String rdataPattern;
    public long start;
    public long step;
    public final long ttl;
    public final int type;

    public static boolean supportedType(int type) {
        Type.check(type);
        if (type == 12 || type == 5 || type == 39 || type == 1 || type == 28 || type == 2) {
            return true;
        }
        return false;
    }

    public Generator(long start, long end, long step, String namePattern, int type, int dclass, long ttl, String rdataPattern, Name origin) {
        if (start < 0 || end < 0 || start > end || step <= 0) {
            throw new IllegalArgumentException("invalid range specification");
        } else if (supportedType(type)) {
            DClass.check(dclass);
            this.start = start;
            this.end = end;
            this.step = step;
            this.namePattern = namePattern;
            this.type = type;
            this.dclass = dclass;
            this.ttl = ttl;
            this.rdataPattern = rdataPattern;
            this.origin = origin;
            this.current = start;
        } else {
            throw new IllegalArgumentException("unsupported type");
        }
    }

    private String substitute(String spec, long n) throws IOException {
        boolean escaped = false;
        byte[] str = spec.getBytes();
        StringBuffer sb = new StringBuffer();
        int i = 0;
        while (i < str.length) {
            char c = (char) (str[i] & 255);
            if (escaped) {
                sb.append(c);
                escaped = false;
            } else if (c == '\\') {
                if (i + 1 == str.length) {
                    throw new TextParseException("invalid escape character");
                }
                escaped = true;
            } else if (c == '$') {
                boolean negative = false;
                long offset = 0;
                long width = 0;
                long base = 10;
                boolean wantUpperCase = false;
                if (i + 1 >= str.length || str[i + 1] != (byte) 36) {
                    if (i + 1 < str.length && str[i + 1] == (byte) 123) {
                        i++;
                        if (i + 1 < str.length && str[i + 1] == (byte) 45) {
                            negative = true;
                            i++;
                        }
                        while (i + 1 < str.length) {
                            i++;
                            c = (char) (str[i] & 255);
                            if (c == ',' || c == '}') {
                                break;
                            } else if (c < '0' || c > '9') {
                                throw new TextParseException("invalid offset");
                            } else {
                                c = (char) (c - 48);
                                offset = (offset * 10) + ((long) c);
                            }
                        }
                        if (negative) {
                            offset = -offset;
                        }
                        if (c == ',') {
                            while (i + 1 < str.length) {
                                i++;
                                c = (char) (str[i] & 255);
                                if (c == ',' || c == '}') {
                                    break;
                                } else if (c < '0' || c > '9') {
                                    throw new TextParseException("invalid width");
                                } else {
                                    c = (char) (c - 48);
                                    width = (width * 10) + ((long) c);
                                }
                            }
                        }
                        if (c == ',') {
                            if (i + 1 == str.length) {
                                throw new TextParseException("invalid base");
                            }
                            i++;
                            c = (char) (str[i] & 255);
                            if (c == 'o') {
                                base = 8;
                            } else if (c == 'x') {
                                base = 16;
                            } else if (c == 'X') {
                                base = 16;
                                wantUpperCase = true;
                            } else if (c != 'd') {
                                throw new TextParseException("invalid base");
                            }
                        }
                        if (i + 1 == str.length || str[i + 1] != (byte) 125) {
                            throw new TextParseException("invalid modifiers");
                        }
                        i++;
                    }
                    long v = n + offset;
                    if (v < 0) {
                        throw new TextParseException("invalid offset expansion");
                    }
                    String number;
                    if (base == 8) {
                        number = Long.toOctalString(v);
                    } else if (base == 16) {
                        number = Long.toHexString(v);
                    } else {
                        number = Long.toString(v);
                    }
                    if (wantUpperCase) {
                        number = number.toUpperCase();
                    }
                    if (width != 0 && width > ((long) number.length())) {
                        int zeros = ((int) width) - number.length();
                        while (true) {
                            int zeros2 = zeros - 1;
                            if (zeros <= 0) {
                                break;
                            }
                            sb.append('0');
                            zeros = zeros2;
                        }
                    }
                    sb.append(number);
                } else {
                    i++;
                    sb.append((char) (str[i] & 255));
                }
            } else {
                sb.append(c);
            }
            i++;
        }
        return sb.toString();
    }

    public Record nextRecord() throws IOException {
        if (this.current > this.end) {
            return null;
        }
        Name name = Name.fromString(substitute(this.namePattern, this.current), this.origin);
        String rdata = substitute(this.rdataPattern, this.current);
        this.current += this.step;
        return Record.fromString(name, this.type, this.dclass, this.ttl, rdata, this.origin);
    }

    public Record[] expand() throws IOException {
        List list = new ArrayList();
        long i = this.start;
        while (i < this.end) {
            list.add(Record.fromString(Name.fromString(substitute(this.namePattern, this.current), this.origin), this.type, this.dclass, this.ttl, substitute(this.rdataPattern, this.current), this.origin));
            i += this.step;
        }
        return (Record[]) list.toArray(new Record[list.size()]);
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("$GENERATE ");
        sb.append(new StringBuffer().append(this.start).append("-").append(this.end).toString());
        if (this.step > 1) {
            sb.append(new StringBuffer().append("/").append(this.step).toString());
        }
        sb.append(" ");
        sb.append(new StringBuffer().append(this.namePattern).append(" ").toString());
        sb.append(new StringBuffer().append(this.ttl).append(" ").toString());
        if (!(this.dclass == 1 && Options.check("noPrintIN"))) {
            sb.append(new StringBuffer().append(DClass.string(this.dclass)).append(" ").toString());
        }
        sb.append(new StringBuffer().append(Type.string(this.type)).append(" ").toString());
        sb.append(new StringBuffer().append(this.rdataPattern).append(" ").toString());
        return sb.toString();
    }
}
