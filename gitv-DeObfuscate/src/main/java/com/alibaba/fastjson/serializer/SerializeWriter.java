package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.util.IOUtils;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.List;
import org.xbill.DNS.WKSRecord.Service;

public final class SerializeWriter extends Writer {
    private static final ThreadLocal<char[]> bufLocal = new ThreadLocal();
    private static final ThreadLocal<byte[]> bytesBufLocal = new ThreadLocal();
    static final int nonDirectFeautres = ((((((((((SerializerFeature.UseSingleQuotes.mask | 0) | SerializerFeature.BrowserSecure.mask) | SerializerFeature.BrowserCompatible.mask) | SerializerFeature.PrettyFormat.mask) | SerializerFeature.WriteEnumUsingToString.mask) | SerializerFeature.WriteNonStringValueAsString.mask) | SerializerFeature.WriteSlashAsSpecial.mask) | SerializerFeature.IgnoreErrorGetter.mask) | SerializerFeature.WriteClassName.mask) | SerializerFeature.NotWriteDefaultValue.mask);
    protected boolean beanToArray;
    protected char[] buf;
    protected int count;
    protected boolean disableCircularReferenceDetect;
    protected int features;
    protected char keySeperator;
    protected boolean notWriteDefaultValue;
    protected boolean quoteFieldNames;
    protected boolean sortField;
    protected boolean useSingleQuotes;
    protected boolean writeDirect;
    protected boolean writeEnumUsingName;
    protected boolean writeEnumUsingToString;
    protected boolean writeNonStringValueAsString;
    private final Writer writer;

    public SerializeWriter() {
        this((Writer) null);
    }

    public SerializeWriter(Writer writer) {
        this(writer, JSON.DEFAULT_GENERATE_FEATURE, SerializerFeature.EMPTY);
    }

    public SerializeWriter(SerializerFeature... features) {
        this(null, features);
    }

    public SerializeWriter(Writer writer, SerializerFeature... features) {
        this(writer, 0, features);
    }

    public SerializeWriter(Writer writer, int defaultFeatures, SerializerFeature... features) {
        this.writer = writer;
        this.buf = (char[]) bufLocal.get();
        if (this.buf != null) {
            bufLocal.set(null);
        } else {
            this.buf = new char[2048];
        }
        int featuresValue = defaultFeatures;
        for (SerializerFeature feature : features) {
            featuresValue |= feature.getMask();
        }
        this.features = featuresValue;
        computeFeatures();
    }

    public int getBufferLength() {
        return this.buf.length;
    }

    public SerializeWriter(int initialSize) {
        this(null, initialSize);
    }

    public SerializeWriter(Writer writer, int initialSize) {
        this.writer = writer;
        if (initialSize <= 0) {
            throw new IllegalArgumentException("Negative initial size: " + initialSize);
        }
        this.buf = new char[initialSize];
    }

    public void config(SerializerFeature feature, boolean state) {
        if (state) {
            this.features |= feature.getMask();
            if (feature == SerializerFeature.WriteEnumUsingToString) {
                this.features &= SerializerFeature.WriteEnumUsingName.getMask() ^ -1;
            } else if (feature == SerializerFeature.WriteEnumUsingName) {
                this.features &= SerializerFeature.WriteEnumUsingToString.getMask() ^ -1;
            }
        } else {
            this.features &= feature.getMask() ^ -1;
        }
        computeFeatures();
    }

    protected void computeFeatures() {
        boolean z;
        boolean z2 = true;
        this.quoteFieldNames = (this.features & SerializerFeature.QuoteFieldNames.mask) != 0;
        if ((this.features & SerializerFeature.UseSingleQuotes.mask) != 0) {
            z = true;
        } else {
            z = false;
        }
        this.useSingleQuotes = z;
        if ((this.features & SerializerFeature.SortField.mask) != 0) {
            z = true;
        } else {
            z = false;
        }
        this.sortField = z;
        if ((this.features & SerializerFeature.DisableCircularReferenceDetect.mask) != 0) {
            z = true;
        } else {
            z = false;
        }
        this.disableCircularReferenceDetect = z;
        if ((this.features & SerializerFeature.BeanToArray.mask) != 0) {
            z = true;
        } else {
            z = false;
        }
        this.beanToArray = z;
        if ((this.features & SerializerFeature.WriteNonStringValueAsString.mask) != 0) {
            z = true;
        } else {
            z = false;
        }
        this.writeNonStringValueAsString = z;
        if ((this.features & SerializerFeature.NotWriteDefaultValue.mask) != 0) {
            z = true;
        } else {
            z = false;
        }
        this.notWriteDefaultValue = z;
        if ((this.features & SerializerFeature.WriteEnumUsingName.mask) != 0) {
            z = true;
        } else {
            z = false;
        }
        this.writeEnumUsingName = z;
        if ((this.features & SerializerFeature.WriteEnumUsingToString.mask) != 0) {
            z = true;
        } else {
            z = false;
        }
        this.writeEnumUsingToString = z;
        if (!(this.quoteFieldNames && (this.features & nonDirectFeautres) == 0 && (this.beanToArray || this.writeEnumUsingName))) {
            z2 = false;
        }
        this.writeDirect = z2;
        this.keySeperator = this.useSingleQuotes ? '\'' : '\"';
    }

    public boolean isSortField() {
        return this.sortField;
    }

    public boolean isNotWriteDefaultValue() {
        return this.notWriteDefaultValue;
    }

    public boolean isEnabled(SerializerFeature feature) {
        return (this.features & feature.mask) != 0;
    }

    public boolean isEnabled(int feature) {
        return (this.features & feature) != 0;
    }

    public void write(int c) {
        int newcount = this.count + 1;
        if (newcount > this.buf.length) {
            if (this.writer == null) {
                expandCapacity(newcount);
            } else {
                flush();
                newcount = 1;
            }
        }
        this.buf[this.count] = (char) c;
        this.count = newcount;
    }

    public void write(char[] c, int off, int len) {
        if (off < 0 || off > c.length || len < 0 || off + len > c.length || off + len < 0) {
            throw new IndexOutOfBoundsException();
        } else if (len != 0) {
            int newcount = this.count + len;
            if (newcount > this.buf.length) {
                if (this.writer == null) {
                    expandCapacity(newcount);
                } else {
                    do {
                        int rest = this.buf.length - this.count;
                        System.arraycopy(c, off, this.buf, this.count, rest);
                        this.count = this.buf.length;
                        flush();
                        len -= rest;
                        off += rest;
                    } while (len > this.buf.length);
                    newcount = len;
                }
            }
            System.arraycopy(c, off, this.buf, this.count, len);
            this.count = newcount;
        }
    }

    public void expandCapacity(int minimumCapacity) {
        int newCapacity = ((this.buf.length * 3) / 2) + 1;
        if (newCapacity < minimumCapacity) {
            newCapacity = minimumCapacity;
        }
        char[] newValue = new char[newCapacity];
        System.arraycopy(this.buf, 0, newValue, 0, this.count);
        this.buf = newValue;
    }

    public SerializeWriter append(CharSequence csq) {
        String s = csq == null ? "null" : csq.toString();
        write(s, 0, s.length());
        return this;
    }

    public SerializeWriter append(CharSequence csq, int start, int end) {
        if (csq == null) {
            csq = "null";
        }
        String s = csq.subSequence(start, end).toString();
        write(s, 0, s.length());
        return this;
    }

    public SerializeWriter append(char c) {
        write((int) c);
        return this;
    }

    public void write(String str, int off, int len) {
        int newcount = this.count + len;
        if (newcount > this.buf.length) {
            if (this.writer == null) {
                expandCapacity(newcount);
            } else {
                do {
                    int rest = this.buf.length - this.count;
                    str.getChars(off, off + rest, this.buf, this.count);
                    this.count = this.buf.length;
                    flush();
                    len -= rest;
                    off += rest;
                } while (len > this.buf.length);
                newcount = len;
            }
        }
        str.getChars(off, off + len, this.buf, this.count);
        this.count = newcount;
    }

    public void writeTo(Writer out) throws IOException {
        if (this.writer != null) {
            throw new UnsupportedOperationException("writer not null");
        }
        out.write(this.buf, 0, this.count);
    }

    public void writeTo(OutputStream out, String charsetName) throws IOException {
        writeTo(out, Charset.forName(charsetName));
    }

    public void writeTo(OutputStream out, Charset charset) throws IOException {
        writeToEx(out, charset);
    }

    public int writeToEx(OutputStream out, Charset charset) throws IOException {
        if (this.writer != null) {
            throw new UnsupportedOperationException("writer not null");
        } else if (charset == IOUtils.UTF8) {
            return encodeToUTF8(out);
        } else {
            byte[] bytes = new String(this.buf, 0, this.count).getBytes(charset);
            out.write(bytes);
            return bytes.length;
        }
    }

    public char[] toCharArray() {
        if (this.writer != null) {
            throw new UnsupportedOperationException("writer not null");
        }
        char[] newValue = new char[this.count];
        System.arraycopy(this.buf, 0, newValue, 0, this.count);
        return newValue;
    }

    public byte[] toBytes(String charsetName) {
        Charset charset;
        if (charsetName == null || "UTF-8".equals(charsetName)) {
            charset = IOUtils.UTF8;
        } else {
            charset = Charset.forName(charsetName);
        }
        return toBytes(charset);
    }

    public byte[] toBytes(Charset charset) {
        if (this.writer != null) {
            throw new UnsupportedOperationException("writer not null");
        } else if (charset == IOUtils.UTF8) {
            return encodeToUTF8Bytes();
        } else {
            return new String(this.buf, 0, this.count).getBytes(charset);
        }
    }

    private int encodeToUTF8(OutputStream out) throws IOException {
        int bytesLength = (int) (((double) this.count) * 3.0d);
        byte[] bytes = (byte[]) bytesBufLocal.get();
        if (bytes == null) {
            bytes = new byte[8192];
            bytesBufLocal.set(bytes);
        }
        if (bytes.length < bytesLength) {
            bytes = new byte[bytesLength];
        }
        int position = IOUtils.encodeUTF8(this.buf, 0, this.count, bytes);
        out.write(bytes, 0, position);
        return position;
    }

    private byte[] encodeToUTF8Bytes() {
        int bytesLength = (int) (((double) this.count) * 3.0d);
        byte[] bytes = (byte[]) bytesBufLocal.get();
        if (bytes == null) {
            bytes = new byte[8192];
            bytesBufLocal.set(bytes);
        }
        if (bytes.length < bytesLength) {
            bytes = new byte[bytesLength];
        }
        int position = IOUtils.encodeUTF8(this.buf, 0, this.count, bytes);
        byte[] copy = new byte[position];
        System.arraycopy(bytes, 0, copy, 0, position);
        return copy;
    }

    public int size() {
        return this.count;
    }

    public String toString() {
        return new String(this.buf, 0, this.count);
    }

    public void close() {
        if (this.writer != null && this.count > 0) {
            flush();
        }
        if (this.buf.length <= 8192) {
            bufLocal.set(this.buf);
        }
        this.buf = null;
    }

    public void write(String text) {
        if (text == null) {
            writeNull();
        } else {
            write(text, 0, text.length());
        }
    }

    public void writeInt(int i) {
        if (i == Integer.MIN_VALUE) {
            write("-2147483648");
            return;
        }
        int size = i < 0 ? IOUtils.stringSize(-i) + 1 : IOUtils.stringSize(i);
        int newcount = this.count + size;
        if (newcount > this.buf.length) {
            if (this.writer == null) {
                expandCapacity(newcount);
            } else {
                char[] chars = new char[size];
                IOUtils.getChars(i, size, chars);
                write(chars, 0, chars.length);
                return;
            }
        }
        IOUtils.getChars(i, newcount, this.buf);
        this.count = newcount;
    }

    public void writeByteArray(byte[] bytes) {
        int bytesLen = bytes.length;
        int quote = this.useSingleQuotes ? '\'' : '\"';
        if (bytesLen == 0) {
            write(this.useSingleQuotes ? "''" : "\"\"");
            return;
        }
        int s;
        int i;
        int left;
        char[] CA = IOUtils.CA;
        int eLen = (bytesLen / 3) * 3;
        int charsLen = (((bytesLen - 1) / 3) + 1) << 2;
        int offset = this.count;
        int newcount = (this.count + charsLen) + 2;
        if (newcount > this.buf.length) {
            if (this.writer != null) {
                write(quote);
                s = 0;
                while (s < eLen) {
                    int s2 = s + 1;
                    s = s2 + 1;
                    s2 = s + 1;
                    i = (((bytes[s] & 255) << 16) | ((bytes[s2] & 255) << 8)) | (bytes[s] & 255);
                    write((int) CA[(i >>> 18) & 63]);
                    write((int) CA[(i >>> 12) & 63]);
                    write((int) CA[(i >>> 6) & 63]);
                    write((int) CA[i & 63]);
                    s = s2;
                }
                left = bytesLen - eLen;
                if (left > 0) {
                    i = ((bytes[eLen] & 255) << 10) | (left == 2 ? (bytes[bytesLen - 1] & 255) << 2 : 0);
                    write((int) CA[i >> 12]);
                    write((int) CA[(i >>> 6) & 63]);
                    write((int) left == 2 ? CA[i & 63] : '=');
                    write(61);
                }
                write(quote);
                return;
            }
            expandCapacity(newcount);
        }
        this.count = newcount;
        int offset2 = offset + 1;
        this.buf[offset] = quote;
        int d = offset2;
        s = 0;
        while (s < eLen) {
            s2 = s + 1;
            s = s2 + 1;
            s2 = s + 1;
            i = (((bytes[s] & 255) << 16) | ((bytes[s2] & 255) << 8)) | (bytes[s] & 255);
            int i2 = d + 1;
            this.buf[d] = CA[(i >>> 18) & 63];
            d = i2 + 1;
            this.buf[i2] = CA[(i >>> 12) & 63];
            i2 = d + 1;
            this.buf[d] = CA[(i >>> 6) & 63];
            d = i2 + 1;
            this.buf[i2] = CA[i & 63];
            s = s2;
        }
        left = bytesLen - eLen;
        if (left > 0) {
            i = ((bytes[eLen] & 255) << 10) | (left == 2 ? (bytes[bytesLen - 1] & 255) << 2 : 0);
            this.buf[newcount - 5] = CA[i >> 12];
            this.buf[newcount - 4] = CA[(i >>> 6) & 63];
            this.buf[newcount - 3] = left == 2 ? CA[i & 63] : '=';
            this.buf[newcount - 2] = '=';
        }
        this.buf[newcount - 1] = quote;
    }

    public void writeFloat(float value, boolean checkWriteClassName) {
        if (Float.isNaN(value) || Float.isInfinite(value)) {
            writeNull();
            return;
        }
        String floatText = Float.toString(value);
        if (floatText.endsWith(".0")) {
            floatText = floatText.substring(0, floatText.length() - 2);
        }
        write(floatText);
        if (checkWriteClassName && isEnabled(SerializerFeature.WriteClassName)) {
            write(70);
        }
    }

    public void writeDouble(double doubleValue, boolean checkWriteClassName) {
        if (Double.isNaN(doubleValue) || Double.isInfinite(doubleValue)) {
            writeNull();
            return;
        }
        String doubleText = Double.toString(doubleValue);
        if (doubleText.endsWith(".0")) {
            doubleText = doubleText.substring(0, doubleText.length() - 2);
        }
        write(doubleText);
        if (checkWriteClassName && isEnabled(SerializerFeature.WriteClassName)) {
            write(68);
        }
    }

    public void writeEnum(Enum<?> value) {
        if (value == null) {
            writeNull();
            return;
        }
        String strVal = null;
        if (this.writeEnumUsingName && !this.writeEnumUsingToString) {
            strVal = value.name();
        } else if (this.writeEnumUsingToString) {
            strVal = value.toString();
        }
        if (strVal != null) {
            int quote = isEnabled(SerializerFeature.UseSingleQuotes) ? '\'' : '\"';
            write(quote);
            write(strVal);
            write(quote);
            return;
        }
        writeInt(value.ordinal());
    }

    public void writeLong(long i) {
        boolean needQuotationMark;
        if (!isEnabled(SerializerFeature.BrowserCompatible) || isEnabled(SerializerFeature.WriteClassName) || (i <= 9007199254740991L && i >= -9007199254740991L)) {
            needQuotationMark = false;
        } else {
            needQuotationMark = true;
        }
        if (i != Long.MIN_VALUE) {
            int size = i < 0 ? IOUtils.stringSize(-i) + 1 : IOUtils.stringSize(i);
            int newcount = this.count + size;
            if (needQuotationMark) {
                newcount += 2;
            }
            if (newcount > this.buf.length) {
                if (this.writer == null) {
                    expandCapacity(newcount);
                } else {
                    char[] chars = new char[size];
                    IOUtils.getChars(i, size, chars);
                    if (needQuotationMark) {
                        write(34);
                        write(chars, 0, chars.length);
                        write(34);
                        return;
                    }
                    write(chars, 0, chars.length);
                    return;
                }
            }
            if (needQuotationMark) {
                this.buf[this.count] = '\"';
                IOUtils.getChars(i, newcount - 1, this.buf);
                this.buf[newcount - 1] = '\"';
            } else {
                IOUtils.getChars(i, newcount, this.buf);
            }
            this.count = newcount;
        } else if (needQuotationMark) {
            write("\"-9223372036854775808\"");
        } else {
            write("-9223372036854775808");
        }
    }

    public void writeNull() {
        write("null");
    }

    public void writeNull(SerializerFeature feature) {
        writeNull(0, feature.mask);
    }

    public void writeNull(int beanFeatures, int feature) {
        if ((beanFeatures & feature) == 0 && (this.features & feature) == 0) {
            writeNull();
        } else if (feature == SerializerFeature.WriteNullListAsEmpty.mask) {
            write("[]");
        } else if (feature == SerializerFeature.WriteNullStringAsEmpty.mask) {
            writeString("");
        } else if (feature == SerializerFeature.WriteNullBooleanAsFalse.mask) {
            write("false");
        } else if (feature == SerializerFeature.WriteNullNumberAsZero.mask) {
            write(48);
        } else {
            writeNull();
        }
    }

    public void writeStringWithDoubleQuote(String text, char seperator) {
        if (text == null) {
            writeNull();
            if (seperator != '\u0000') {
                write((int) seperator);
                return;
            }
            return;
        }
        int i;
        int len = text.length();
        int newcount = (this.count + len) + 2;
        if (seperator != '\u0000') {
            newcount++;
        }
        if (newcount > this.buf.length) {
            if (this.writer != null) {
                write(34);
                for (i = 0; i < text.length(); i++) {
                    int ch = text.charAt(i);
                    if (isEnabled(SerializerFeature.BrowserSecure)) {
                        if ((ch < '0' || ch > '9') && ((ch < 'a' || ch > 'z') && !((ch >= 'A' && ch <= 'Z') || ch == ',' || ch == '.' || ch == '_'))) {
                            write(92);
                            write(Service.UUCP_PATH);
                            write((int) IOUtils.DIGITS[(ch >>> 12) & 15]);
                            write((int) IOUtils.DIGITS[(ch >>> 8) & 15]);
                            write((int) IOUtils.DIGITS[(ch >>> 4) & 15]);
                            write((int) IOUtils.DIGITS[ch & 15]);
                        }
                        write(ch);
                    } else if (!isEnabled(SerializerFeature.BrowserCompatible)) {
                        if ((ch < IOUtils.specicalFlags_doubleQuotes.length && IOUtils.specicalFlags_doubleQuotes[ch] != (byte) 0) || (ch == '/' && isEnabled(SerializerFeature.WriteSlashAsSpecial))) {
                            write(92);
                            if (IOUtils.specicalFlags_doubleQuotes[ch] == (byte) 4) {
                                write(Service.UUCP_PATH);
                                write((int) IOUtils.DIGITS[(ch >>> 12) & 15]);
                                write((int) IOUtils.DIGITS[(ch >>> 8) & 15]);
                                write((int) IOUtils.DIGITS[(ch >>> 4) & 15]);
                                write((int) IOUtils.DIGITS[ch & 15]);
                            } else {
                                write((int) IOUtils.replaceChars[ch]);
                            }
                        }
                        write(ch);
                    } else if (ch == '\b' || ch == '\f' || ch == '\n' || ch == '\r' || ch == '\t' || ch == '\"' || ch == '/' || ch == '\\') {
                        write(92);
                        write((int) IOUtils.replaceChars[ch]);
                    } else if (ch < ' ') {
                        write(92);
                        write(Service.UUCP_PATH);
                        write(48);
                        write(48);
                        write((int) IOUtils.ASCII_CHARS[ch * 2]);
                        write((int) IOUtils.ASCII_CHARS[(ch * 2) + 1]);
                    } else {
                        if (ch >= '') {
                            write(92);
                            write(Service.UUCP_PATH);
                            write((int) IOUtils.DIGITS[(ch >>> 12) & 15]);
                            write((int) IOUtils.DIGITS[(ch >>> 8) & 15]);
                            write((int) IOUtils.DIGITS[(ch >>> 4) & 15]);
                            write((int) IOUtils.DIGITS[ch & 15]);
                        }
                        write(ch);
                    }
                }
                write(34);
                if (seperator != '\u0000') {
                    write((int) seperator);
                    return;
                }
                return;
            }
            expandCapacity(newcount);
        }
        int start = this.count + 1;
        int end = start + len;
        this.buf[this.count] = '\"';
        text.getChars(0, len, this.buf, start);
        this.count = newcount;
        int lastSpecialIndex;
        char ch2;
        if (isEnabled(SerializerFeature.BrowserSecure)) {
            lastSpecialIndex = -1;
            for (i = start; i < end; i++) {
                ch2 = this.buf[i];
                if ((ch2 < '0' || ch2 > '9') && ((ch2 < 'a' || ch2 > 'z') && !((ch2 >= 'A' && ch2 <= 'Z') || ch2 == ',' || ch2 == '.' || ch2 == '_'))) {
                    lastSpecialIndex = i;
                    newcount += 5;
                }
            }
            if (newcount > this.buf.length) {
                expandCapacity(newcount);
            }
            this.count = newcount;
            for (i = lastSpecialIndex; i >= start; i--) {
                ch2 = this.buf[i];
                if ((ch2 < '0' || ch2 > '9') && ((ch2 < 'a' || ch2 > 'z') && !((ch2 >= 'A' && ch2 <= 'Z') || ch2 == ',' || ch2 == '.' || ch2 == '_'))) {
                    System.arraycopy(this.buf, i + 1, this.buf, i + 6, (end - i) - 1);
                    this.buf[i] = '\\';
                    this.buf[i + 1] = 'u';
                    this.buf[i + 2] = IOUtils.DIGITS[(ch2 >>> 12) & 15];
                    this.buf[i + 3] = IOUtils.DIGITS[(ch2 >>> 8) & 15];
                    this.buf[i + 4] = IOUtils.DIGITS[(ch2 >>> 4) & 15];
                    this.buf[i + 5] = IOUtils.DIGITS[ch2 & 15];
                    end += 5;
                }
            }
            if (seperator != '\u0000') {
                this.buf[this.count - 2] = '\"';
                this.buf[this.count - 1] = seperator;
                return;
            }
            this.buf[this.count - 1] = '\"';
        } else if (isEnabled(SerializerFeature.BrowserCompatible)) {
            lastSpecialIndex = -1;
            for (i = start; i < end; i++) {
                ch2 = this.buf[i];
                if (ch2 == '\"' || ch2 == '/' || ch2 == '\\') {
                    lastSpecialIndex = i;
                    newcount++;
                } else if (ch2 == '\b' || ch2 == '\f' || ch2 == '\n' || ch2 == '\r' || ch2 == '\t') {
                    lastSpecialIndex = i;
                    newcount++;
                } else if (ch2 < ' ') {
                    lastSpecialIndex = i;
                    newcount += 5;
                } else if (ch2 >= '') {
                    lastSpecialIndex = i;
                    newcount += 5;
                }
            }
            if (newcount > this.buf.length) {
                expandCapacity(newcount);
            }
            this.count = newcount;
            for (i = lastSpecialIndex; i >= start; i--) {
                ch2 = this.buf[i];
                if (ch2 == '\b' || ch2 == '\f' || ch2 == '\n' || ch2 == '\r' || ch2 == '\t') {
                    System.arraycopy(this.buf, i + 1, this.buf, i + 2, (end - i) - 1);
                    this.buf[i] = '\\';
                    this.buf[i + 1] = IOUtils.replaceChars[ch2];
                    end++;
                } else if (ch2 == '\"' || ch2 == '/' || ch2 == '\\') {
                    System.arraycopy(this.buf, i + 1, this.buf, i + 2, (end - i) - 1);
                    this.buf[i] = '\\';
                    this.buf[i + 1] = ch2;
                    end++;
                } else if (ch2 < ' ') {
                    System.arraycopy(this.buf, i + 1, this.buf, i + 6, (end - i) - 1);
                    this.buf[i] = '\\';
                    this.buf[i + 1] = 'u';
                    this.buf[i + 2] = '0';
                    this.buf[i + 3] = '0';
                    this.buf[i + 4] = IOUtils.ASCII_CHARS[ch2 * 2];
                    this.buf[i + 5] = IOUtils.ASCII_CHARS[(ch2 * 2) + 1];
                    end += 5;
                } else if (ch2 >= '') {
                    System.arraycopy(this.buf, i + 1, this.buf, i + 6, (end - i) - 1);
                    this.buf[i] = '\\';
                    this.buf[i + 1] = 'u';
                    this.buf[i + 2] = IOUtils.DIGITS[(ch2 >>> 12) & 15];
                    this.buf[i + 3] = IOUtils.DIGITS[(ch2 >>> 8) & 15];
                    this.buf[i + 4] = IOUtils.DIGITS[(ch2 >>> 4) & 15];
                    this.buf[i + 5] = IOUtils.DIGITS[ch2 & 15];
                    end += 5;
                }
            }
            if (seperator != '\u0000') {
                this.buf[this.count - 2] = '\"';
                this.buf[this.count - 1] = seperator;
                return;
            }
            this.buf[this.count - 1] = '\"';
        } else {
            int specialCount = 0;
            lastSpecialIndex = -1;
            int firstSpecialIndex = -1;
            char lastSpecial = '\u0000';
            for (i = start; i < end; i++) {
                ch2 = this.buf[i];
                if (ch2 == ' ') {
                    specialCount++;
                    lastSpecialIndex = i;
                    lastSpecial = ch2;
                    newcount += 4;
                    if (firstSpecialIndex == -1) {
                        firstSpecialIndex = i;
                    }
                } else if (ch2 >= ']') {
                    if (ch2 >= '' && ch2 <= ' ') {
                        if (firstSpecialIndex == -1) {
                            firstSpecialIndex = i;
                        }
                        specialCount++;
                        lastSpecialIndex = i;
                        lastSpecial = ch2;
                        newcount += 4;
                    }
                } else if (isSpecial(ch2, this.features)) {
                    specialCount++;
                    lastSpecialIndex = i;
                    lastSpecial = ch2;
                    if (ch2 < IOUtils.specicalFlags_doubleQuotes.length && IOUtils.specicalFlags_doubleQuotes[ch2] == (byte) 4) {
                        newcount += 4;
                    }
                    if (firstSpecialIndex == -1) {
                        firstSpecialIndex = i;
                    }
                }
            }
            if (specialCount > 0) {
                newcount += specialCount;
                if (newcount > this.buf.length) {
                    expandCapacity(newcount);
                }
                this.count = newcount;
                int i2;
                int i3;
                if (specialCount == 1) {
                    int srcPos;
                    int destPos;
                    int LengthOfCopy;
                    if (lastSpecial == ' ') {
                        srcPos = lastSpecialIndex + 1;
                        destPos = lastSpecialIndex + 6;
                        LengthOfCopy = (end - lastSpecialIndex) - 1;
                        System.arraycopy(this.buf, srcPos, this.buf, destPos, LengthOfCopy);
                        this.buf[lastSpecialIndex] = '\\';
                        lastSpecialIndex++;
                        this.buf[lastSpecialIndex] = 'u';
                        lastSpecialIndex++;
                        this.buf[lastSpecialIndex] = '2';
                        lastSpecialIndex++;
                        this.buf[lastSpecialIndex] = '0';
                        lastSpecialIndex++;
                        this.buf[lastSpecialIndex] = '2';
                        this.buf[lastSpecialIndex + 1] = '8';
                    } else {
                        ch2 = lastSpecial;
                        if (ch2 >= IOUtils.specicalFlags_doubleQuotes.length || IOUtils.specicalFlags_doubleQuotes[ch2] != (byte) 4) {
                            srcPos = lastSpecialIndex + 1;
                            destPos = lastSpecialIndex + 2;
                            LengthOfCopy = (end - lastSpecialIndex) - 1;
                            System.arraycopy(this.buf, srcPos, this.buf, destPos, LengthOfCopy);
                            this.buf[lastSpecialIndex] = '\\';
                            this.buf[lastSpecialIndex + 1] = IOUtils.replaceChars[ch2];
                        } else {
                            srcPos = lastSpecialIndex + 1;
                            destPos = lastSpecialIndex + 6;
                            LengthOfCopy = (end - lastSpecialIndex) - 1;
                            System.arraycopy(this.buf, srcPos, this.buf, destPos, LengthOfCopy);
                            i2 = lastSpecialIndex;
                            i3 = i2 + 1;
                            this.buf[i2] = '\\';
                            i2 = i3 + 1;
                            this.buf[i3] = 'u';
                            i3 = i2 + 1;
                            this.buf[i2] = IOUtils.DIGITS[(ch2 >>> 12) & 15];
                            i2 = i3 + 1;
                            this.buf[i3] = IOUtils.DIGITS[(ch2 >>> 8) & 15];
                            i3 = i2 + 1;
                            this.buf[i2] = IOUtils.DIGITS[(ch2 >>> 4) & 15];
                            i2 = i3 + 1;
                            this.buf[i3] = IOUtils.DIGITS[ch2 & 15];
                        }
                    }
                } else if (specialCount > 1) {
                    i2 = firstSpecialIndex;
                    for (i = firstSpecialIndex - start; i < text.length(); i++) {
                        ch2 = text.charAt(i);
                        if ((ch2 < IOUtils.specicalFlags_doubleQuotes.length && IOUtils.specicalFlags_doubleQuotes[ch2] != (byte) 0) || (ch2 == '/' && isEnabled(SerializerFeature.WriteSlashAsSpecial))) {
                            i3 = i2 + 1;
                            this.buf[i2] = '\\';
                            if (IOUtils.specicalFlags_doubleQuotes[ch2] == (byte) 4) {
                                i2 = i3 + 1;
                                this.buf[i3] = 'u';
                                i3 = i2 + 1;
                                this.buf[i2] = IOUtils.DIGITS[(ch2 >>> 12) & 15];
                                i2 = i3 + 1;
                                this.buf[i3] = IOUtils.DIGITS[(ch2 >>> 8) & 15];
                                i3 = i2 + 1;
                                this.buf[i2] = IOUtils.DIGITS[(ch2 >>> 4) & 15];
                                i2 = i3 + 1;
                                this.buf[i3] = IOUtils.DIGITS[ch2 & 15];
                                end += 5;
                            } else {
                                i2 = i3 + 1;
                                this.buf[i3] = IOUtils.replaceChars[ch2];
                                end++;
                            }
                        } else if (ch2 == ' ') {
                            i3 = i2 + 1;
                            this.buf[i2] = '\\';
                            i2 = i3 + 1;
                            this.buf[i3] = 'u';
                            i3 = i2 + 1;
                            this.buf[i2] = IOUtils.DIGITS[(ch2 >>> 12) & 15];
                            i2 = i3 + 1;
                            this.buf[i3] = IOUtils.DIGITS[(ch2 >>> 8) & 15];
                            i3 = i2 + 1;
                            this.buf[i2] = IOUtils.DIGITS[(ch2 >>> 4) & 15];
                            i2 = i3 + 1;
                            this.buf[i3] = IOUtils.DIGITS[ch2 & 15];
                            end += 5;
                        } else {
                            i3 = i2 + 1;
                            this.buf[i2] = ch2;
                            i2 = i3;
                        }
                    }
                }
            }
            if (seperator != '\u0000') {
                this.buf[this.count - 2] = '\"';
                this.buf[this.count - 1] = seperator;
                return;
            }
            this.buf[this.count - 1] = '\"';
        }
    }

    public void writeFieldNameDirect(String text) {
        int len = text.length();
        int newcount = (this.count + len) + 3;
        if (newcount > this.buf.length) {
            expandCapacity(newcount);
        }
        int start = this.count + 1;
        this.buf[this.count] = '\"';
        text.getChars(0, len, this.buf, start);
        this.count = newcount;
        this.buf[this.count - 2] = '\"';
        this.buf[this.count - 1] = ':';
    }

    public void write(List<String> list) {
        if (list.isEmpty()) {
            write("[]");
            return;
        }
        int offset = this.count;
        int list_size = list.size();
        int offset2 = offset;
        for (int i = 0; i < list_size; i++) {
            int j;
            String text = (String) list.get(i);
            boolean hasSpecial = false;
            if (text != null) {
                int len = text.length();
                for (j = 0; j < len; j++) {
                    char ch = text.charAt(j);
                    hasSpecial = ch < ' ' || ch > '~' || ch == '\"' || ch == '\\';
                    if (hasSpecial) {
                        break;
                    }
                }
            } else {
                hasSpecial = true;
            }
            if (hasSpecial) {
                write(91);
                for (j = 0; j < list.size(); j++) {
                    text = (String) list.get(j);
                    if (j != 0) {
                        write(44);
                    }
                    if (text == null) {
                        write("null");
                    } else {
                        writeStringWithDoubleQuote(text, '\u0000');
                    }
                }
                write(93);
                return;
            }
            int newcount = (text.length() + offset2) + 3;
            if (i == list.size() - 1) {
                newcount++;
            }
            if (newcount > this.buf.length) {
                this.count = offset2;
                expandCapacity(newcount);
            }
            if (i == 0) {
                offset = offset2 + 1;
                this.buf[offset2] = '[';
            } else {
                offset = offset2 + 1;
                this.buf[offset2] = ',';
            }
            offset2 = offset + 1;
            this.buf[offset] = '\"';
            text.getChars(0, text.length(), this.buf, offset2);
            offset = offset2 + text.length();
            offset2 = offset + 1;
            this.buf[offset] = '\"';
        }
        offset = offset2 + 1;
        this.buf[offset2] = ']';
        this.count = offset;
    }

    public void writeFieldValue(char seperator, String name, char value) {
        write((int) seperator);
        writeFieldName(name);
        if (value == '\u0000') {
            writeString("\u0000");
        } else {
            writeString(Character.toString(value));
        }
    }

    public void writeFieldValue(char seperator, String name, boolean value) {
        int intSize;
        if (value) {
            intSize = 4;
        } else {
            intSize = 5;
        }
        int nameLen = name.length();
        int newcount = ((this.count + nameLen) + 4) + intSize;
        if (newcount > this.buf.length) {
            if (this.writer != null) {
                write((int) seperator);
                writeString(name);
                write(58);
                write(value);
                return;
            }
            expandCapacity(newcount);
        }
        int start = this.count;
        this.count = newcount;
        this.buf[start] = seperator;
        int nameEnd = (start + nameLen) + 1;
        this.buf[start + 1] = this.keySeperator;
        name.getChars(0, nameLen, this.buf, start + 2);
        this.buf[nameEnd + 1] = this.keySeperator;
        if (value) {
            System.arraycopy(":true".toCharArray(), 0, this.buf, nameEnd + 2, 5);
        } else {
            System.arraycopy(":false".toCharArray(), 0, this.buf, nameEnd + 2, 6);
        }
    }

    public void write(boolean value) {
        if (value) {
            write("true");
        } else {
            write("false");
        }
    }

    public void writeFieldValue(char seperator, String name, int value) {
        if (value == Integer.MIN_VALUE || !this.quoteFieldNames) {
            write((int) seperator);
            writeFieldName(name);
            writeInt(value);
            return;
        }
        int intSize = value < 0 ? IOUtils.stringSize(-value) + 1 : IOUtils.stringSize(value);
        int nameLen = name.length();
        int newcount = ((this.count + nameLen) + 4) + intSize;
        if (newcount > this.buf.length) {
            if (this.writer != null) {
                write((int) seperator);
                writeFieldName(name);
                writeInt(value);
                return;
            }
            expandCapacity(newcount);
        }
        int start = this.count;
        this.count = newcount;
        this.buf[start] = seperator;
        int nameEnd = (start + nameLen) + 1;
        this.buf[start + 1] = this.keySeperator;
        name.getChars(0, nameLen, this.buf, start + 2);
        this.buf[nameEnd + 1] = this.keySeperator;
        this.buf[nameEnd + 2] = ':';
        IOUtils.getChars(value, this.count, this.buf);
    }

    public void writeFieldValue(char seperator, String name, long value) {
        if (value == Long.MIN_VALUE || !this.quoteFieldNames) {
            write((int) seperator);
            writeFieldName(name);
            writeLong(value);
            return;
        }
        int intSize = value < 0 ? IOUtils.stringSize(-value) + 1 : IOUtils.stringSize(value);
        int nameLen = name.length();
        int newcount = ((this.count + nameLen) + 4) + intSize;
        if (newcount > this.buf.length) {
            if (this.writer != null) {
                write((int) seperator);
                writeFieldName(name);
                writeLong(value);
                return;
            }
            expandCapacity(newcount);
        }
        int start = this.count;
        this.count = newcount;
        this.buf[start] = seperator;
        int nameEnd = (start + nameLen) + 1;
        this.buf[start + 1] = this.keySeperator;
        name.getChars(0, nameLen, this.buf, start + 2);
        this.buf[nameEnd + 1] = this.keySeperator;
        this.buf[nameEnd + 2] = ':';
        IOUtils.getChars(value, this.count, this.buf);
    }

    public void writeFieldValue(char seperator, String name, float value) {
        write((int) seperator);
        writeFieldName(name);
        writeFloat(value, false);
    }

    public void writeFieldValue(char seperator, String name, double value) {
        write((int) seperator);
        writeFieldName(name);
        writeDouble(value, false);
    }

    public void writeFieldValue(char seperator, String name, String value) {
        if (!this.quoteFieldNames) {
            write((int) seperator);
            writeFieldName(name);
            if (value == null) {
                writeNull();
            } else {
                writeString(value);
            }
        } else if (this.useSingleQuotes) {
            write((int) seperator);
            writeFieldName(name);
            if (value == null) {
                writeNull();
            } else {
                writeString(value);
            }
        } else if (isEnabled(SerializerFeature.BrowserSecure)) {
            write((int) seperator);
            writeStringWithDoubleQuote(name, ':');
            writeStringWithDoubleQuote(value, '\u0000');
        } else if (isEnabled(SerializerFeature.BrowserCompatible)) {
            write((int) seperator);
            writeStringWithDoubleQuote(name, ':');
            writeStringWithDoubleQuote(value, '\u0000');
        } else {
            writeFieldValueStringWithDoubleQuoteCheck(seperator, name, value);
        }
    }

    public void writeFieldValueStringWithDoubleQuoteCheck(char seperator, String name, String value) {
        int valueLen;
        int nameLen = name.length();
        int newcount = this.count;
        if (value == null) {
            valueLen = 4;
            newcount += nameLen + 8;
        } else {
            valueLen = value.length();
            newcount += (nameLen + valueLen) + 6;
        }
        if (newcount > this.buf.length) {
            if (this.writer != null) {
                write((int) seperator);
                writeStringWithDoubleQuote(name, ':');
                writeStringWithDoubleQuote(value, '\u0000');
                return;
            }
            expandCapacity(newcount);
        }
        this.buf[this.count] = seperator;
        int nameStart = this.count + 2;
        int nameEnd = nameStart + nameLen;
        this.buf[this.count + 1] = '\"';
        name.getChars(0, nameLen, this.buf, nameStart);
        this.count = newcount;
        this.buf[nameEnd] = '\"';
        int i = nameEnd + 1;
        int i2 = i + 1;
        this.buf[i] = ':';
        if (value == null) {
            i = i2 + 1;
            this.buf[i2] = 'n';
            i2 = i + 1;
            this.buf[i] = 'u';
            i = i2 + 1;
            this.buf[i2] = 'l';
            i2 = i + 1;
            this.buf[i] = 'l';
            return;
        }
        int i3;
        i = i2 + 1;
        this.buf[i2] = '\"';
        int valueStart = i;
        int valueEnd = valueStart + valueLen;
        value.getChars(0, valueLen, this.buf, valueStart);
        int specialCount = 0;
        int lastSpecialIndex = -1;
        int firstSpecialIndex = -1;
        char lastSpecial = '\u0000';
        for (i3 = valueStart; i3 < valueEnd; i3++) {
            char ch = this.buf[i3];
            if (ch >= ']') {
                if (ch >= '' && (ch == ' ' || ch <= ' ')) {
                    if (firstSpecialIndex == -1) {
                        firstSpecialIndex = i3;
                    }
                    specialCount++;
                    lastSpecialIndex = i3;
                    lastSpecial = ch;
                    newcount += 4;
                }
            } else if (isSpecial(ch, this.features)) {
                specialCount++;
                lastSpecialIndex = i3;
                lastSpecial = ch;
                if (ch < IOUtils.specicalFlags_doubleQuotes.length && IOUtils.specicalFlags_doubleQuotes[ch] == (byte) 4) {
                    newcount += 4;
                }
                if (firstSpecialIndex == -1) {
                    firstSpecialIndex = i3;
                }
            }
        }
        if (specialCount > 0) {
            newcount += specialCount;
            if (newcount > this.buf.length) {
                expandCapacity(newcount);
            }
            this.count = newcount;
            int i4;
            int i5;
            if (specialCount == 1) {
                int srcPos;
                int destPos;
                int LengthOfCopy;
                if (lastSpecial == ' ') {
                    srcPos = lastSpecialIndex + 1;
                    destPos = lastSpecialIndex + 6;
                    LengthOfCopy = (valueEnd - lastSpecialIndex) - 1;
                    System.arraycopy(this.buf, srcPos, this.buf, destPos, LengthOfCopy);
                    this.buf[lastSpecialIndex] = '\\';
                    lastSpecialIndex++;
                    this.buf[lastSpecialIndex] = 'u';
                    lastSpecialIndex++;
                    this.buf[lastSpecialIndex] = '2';
                    lastSpecialIndex++;
                    this.buf[lastSpecialIndex] = '0';
                    lastSpecialIndex++;
                    this.buf[lastSpecialIndex] = '2';
                    this.buf[lastSpecialIndex + 1] = '8';
                } else {
                    ch = lastSpecial;
                    if (ch >= IOUtils.specicalFlags_doubleQuotes.length || IOUtils.specicalFlags_doubleQuotes[ch] != (byte) 4) {
                        srcPos = lastSpecialIndex + 1;
                        destPos = lastSpecialIndex + 2;
                        LengthOfCopy = (valueEnd - lastSpecialIndex) - 1;
                        System.arraycopy(this.buf, srcPos, this.buf, destPos, LengthOfCopy);
                        this.buf[lastSpecialIndex] = '\\';
                        this.buf[lastSpecialIndex + 1] = IOUtils.replaceChars[ch];
                    } else {
                        srcPos = lastSpecialIndex + 1;
                        destPos = lastSpecialIndex + 6;
                        LengthOfCopy = (valueEnd - lastSpecialIndex) - 1;
                        System.arraycopy(this.buf, srcPos, this.buf, destPos, LengthOfCopy);
                        i4 = lastSpecialIndex;
                        i5 = i4 + 1;
                        this.buf[i4] = '\\';
                        i4 = i5 + 1;
                        this.buf[i5] = 'u';
                        i5 = i4 + 1;
                        this.buf[i4] = IOUtils.DIGITS[(ch >>> 12) & 15];
                        i4 = i5 + 1;
                        this.buf[i5] = IOUtils.DIGITS[(ch >>> 8) & 15];
                        i5 = i4 + 1;
                        this.buf[i4] = IOUtils.DIGITS[(ch >>> 4) & 15];
                        i4 = i5 + 1;
                        this.buf[i5] = IOUtils.DIGITS[ch & 15];
                    }
                }
            } else if (specialCount > 1) {
                i4 = firstSpecialIndex;
                for (i3 = firstSpecialIndex - valueStart; i3 < value.length(); i3++) {
                    ch = value.charAt(i3);
                    if ((ch < IOUtils.specicalFlags_doubleQuotes.length && IOUtils.specicalFlags_doubleQuotes[ch] != (byte) 0) || (ch == '/' && isEnabled(SerializerFeature.WriteSlashAsSpecial))) {
                        i5 = i4 + 1;
                        this.buf[i4] = '\\';
                        if (IOUtils.specicalFlags_doubleQuotes[ch] == (byte) 4) {
                            i4 = i5 + 1;
                            this.buf[i5] = 'u';
                            i5 = i4 + 1;
                            this.buf[i4] = IOUtils.DIGITS[(ch >>> 12) & 15];
                            i4 = i5 + 1;
                            this.buf[i5] = IOUtils.DIGITS[(ch >>> 8) & 15];
                            i5 = i4 + 1;
                            this.buf[i4] = IOUtils.DIGITS[(ch >>> 4) & 15];
                            i4 = i5 + 1;
                            this.buf[i5] = IOUtils.DIGITS[ch & 15];
                            valueEnd += 5;
                        } else {
                            i4 = i5 + 1;
                            this.buf[i5] = IOUtils.replaceChars[ch];
                            valueEnd++;
                        }
                    } else if (ch == ' ') {
                        i5 = i4 + 1;
                        this.buf[i4] = '\\';
                        i4 = i5 + 1;
                        this.buf[i5] = 'u';
                        i5 = i4 + 1;
                        this.buf[i4] = IOUtils.DIGITS[(ch >>> 12) & 15];
                        i4 = i5 + 1;
                        this.buf[i5] = IOUtils.DIGITS[(ch >>> 8) & 15];
                        i5 = i4 + 1;
                        this.buf[i4] = IOUtils.DIGITS[(ch >>> 4) & 15];
                        i4 = i5 + 1;
                        this.buf[i5] = IOUtils.DIGITS[ch & 15];
                        valueEnd += 5;
                    } else {
                        i5 = i4 + 1;
                        this.buf[i4] = ch;
                        i4 = i5;
                    }
                }
            }
        }
        this.buf[this.count - 1] = '\"';
    }

    public void writeFieldValueStringWithDoubleQuote(char seperator, String name, String value) {
        int nameLen = name.length();
        int newcount = this.count;
        int valueLen = value.length();
        newcount += (nameLen + valueLen) + 6;
        if (newcount > this.buf.length) {
            if (this.writer != null) {
                write((int) seperator);
                writeStringWithDoubleQuote(name, ':');
                writeStringWithDoubleQuote(value, '\u0000');
                return;
            }
            expandCapacity(newcount);
        }
        this.buf[this.count] = seperator;
        int nameStart = this.count + 2;
        int nameEnd = nameStart + nameLen;
        this.buf[this.count + 1] = '\"';
        name.getChars(0, nameLen, this.buf, nameStart);
        this.count = newcount;
        this.buf[nameEnd] = '\"';
        int i = nameEnd + 1;
        int i2 = i + 1;
        this.buf[i] = ':';
        i = i2 + 1;
        this.buf[i2] = '\"';
        value.getChars(0, valueLen, this.buf, i);
        this.buf[this.count - 1] = '\"';
    }

    static boolean isSpecial(char ch, int features) {
        boolean z = true;
        if (ch == ' ') {
            return false;
        }
        if (ch == '/') {
            if ((SerializerFeature.WriteSlashAsSpecial.mask & features) == 0) {
                z = false;
            }
            return z;
        } else if (ch > '#' && ch != '\\') {
            return false;
        } else {
            if (ch <= '\u001f' || ch == '\\' || ch == '\"') {
                return true;
            }
            return false;
        }
    }

    public void writeFieldValue(char seperator, String name, Enum<?> value) {
        if (value == null) {
            write((int) seperator);
            writeFieldName(name);
            writeNull();
        } else if (this.writeEnumUsingName && !this.writeEnumUsingToString) {
            writeEnumFieldValue(seperator, name, value.name());
        } else if (this.writeEnumUsingToString) {
            writeEnumFieldValue(seperator, name, value.toString());
        } else {
            writeFieldValue(seperator, name, value.ordinal());
        }
    }

    private void writeEnumFieldValue(char seperator, String name, String value) {
        if (this.useSingleQuotes) {
            writeFieldValue(seperator, name, value);
        } else {
            writeFieldValueStringWithDoubleQuote(seperator, name, value);
        }
    }

    public void writeFieldValue(char seperator, String name, BigDecimal value) {
        write((int) seperator);
        writeFieldName(name);
        if (value == null) {
            writeNull();
        } else {
            write(value.toString());
        }
    }

    public void writeString(String text, char seperator) {
        if (this.useSingleQuotes) {
            writeStringWithSingleQuote(text);
            write((int) seperator);
            return;
        }
        writeStringWithDoubleQuote(text, seperator);
    }

    public void writeString(String text) {
        if (this.useSingleQuotes) {
            writeStringWithSingleQuote(text);
        } else {
            writeStringWithDoubleQuote(text, '\u0000');
        }
    }

    protected void writeStringWithSingleQuote(String text) {
        int newcount;
        if (text == null) {
            newcount = this.count + 4;
            if (newcount > this.buf.length) {
                expandCapacity(newcount);
            }
            "null".getChars(0, 4, this.buf, this.count);
            this.count = newcount;
            return;
        }
        int i;
        int len = text.length();
        newcount = (this.count + len) + 2;
        if (newcount > this.buf.length) {
            if (this.writer != null) {
                write(39);
                for (i = 0; i < text.length(); i++) {
                    int ch = text.charAt(i);
                    if (ch <= '\r' || ch == '\\' || ch == '\'' || (ch == '/' && isEnabled(SerializerFeature.WriteSlashAsSpecial))) {
                        write(92);
                        write(IOUtils.replaceChars[ch]);
                    } else {
                        write(ch);
                    }
                }
                write(39);
                return;
            }
            expandCapacity(newcount);
        }
        int start = this.count + 1;
        int end = start + len;
        this.buf[this.count] = '\'';
        text.getChars(0, len, this.buf, start);
        this.count = newcount;
        int specialCount = 0;
        int lastSpecialIndex = -1;
        char lastSpecial = '\u0000';
        for (i = start; i < end; i++) {
            char ch2 = this.buf[i];
            if (ch2 <= '\r' || ch2 == '\\' || ch2 == '\'' || (ch2 == '/' && isEnabled(SerializerFeature.WriteSlashAsSpecial))) {
                specialCount++;
                lastSpecialIndex = i;
                lastSpecial = ch2;
            }
        }
        newcount += specialCount;
        if (newcount > this.buf.length) {
            expandCapacity(newcount);
        }
        this.count = newcount;
        if (specialCount == 1) {
            System.arraycopy(this.buf, lastSpecialIndex + 1, this.buf, lastSpecialIndex + 2, (end - lastSpecialIndex) - 1);
            this.buf[lastSpecialIndex] = '\\';
            this.buf[lastSpecialIndex + 1] = IOUtils.replaceChars[lastSpecial];
        } else if (specialCount > 1) {
            System.arraycopy(this.buf, lastSpecialIndex + 1, this.buf, lastSpecialIndex + 2, (end - lastSpecialIndex) - 1);
            this.buf[lastSpecialIndex] = '\\';
            lastSpecialIndex++;
            this.buf[lastSpecialIndex] = IOUtils.replaceChars[lastSpecial];
            end++;
            for (i = lastSpecialIndex - 2; i >= start; i--) {
                ch2 = this.buf[i];
                if (ch2 <= '\r' || ch2 == '\\' || ch2 == '\'' || (ch2 == '/' && isEnabled(SerializerFeature.WriteSlashAsSpecial))) {
                    System.arraycopy(this.buf, i + 1, this.buf, i + 2, (end - i) - 1);
                    this.buf[i] = '\\';
                    this.buf[i + 1] = IOUtils.replaceChars[ch2];
                    end++;
                }
            }
        }
        this.buf[this.count - 1] = '\'';
    }

    public void writeFieldName(String key) {
        writeFieldName(key, false);
    }

    public void writeFieldName(String key, boolean checkSpecial) {
        if (key == null) {
            write("null:");
        } else if (this.useSingleQuotes) {
            if (this.quoteFieldNames) {
                writeStringWithSingleQuote(key);
                write(58);
                return;
            }
            writeKeyWithSingleQuoteIfHasSpecial(key);
        } else if (this.quoteFieldNames) {
            writeStringWithDoubleQuote(key, ':');
        } else {
            boolean hashSpecial;
            if (key.length() == 0) {
                hashSpecial = true;
            } else {
                hashSpecial = false;
            }
            for (int i = 0; i < key.length(); i++) {
                if (isSpecial(key.charAt(i), 0)) {
                    hashSpecial = true;
                    break;
                }
            }
            if (hashSpecial) {
                writeStringWithDoubleQuote(key, ':');
                return;
            }
            write(key);
            write(58);
        }
    }

    private void writeKeyWithSingleQuoteIfHasSpecial(String text) {
        boolean hasSpecial;
        int i;
        char ch;
        byte[] specicalFlags_singleQuotes = IOUtils.specicalFlags_singleQuotes;
        int len = text.length();
        int newcount = (this.count + len) + 1;
        if (newcount > this.buf.length) {
            if (this.writer == null) {
                expandCapacity(newcount);
            } else if (len == 0) {
                write(39);
                write(39);
                write(58);
                return;
            } else {
                hasSpecial = false;
                for (i = 0; i < len; i++) {
                    ch = text.charAt(i);
                    if (ch < specicalFlags_singleQuotes.length && specicalFlags_singleQuotes[ch] != (byte) 0) {
                        hasSpecial = true;
                        break;
                    }
                }
                if (hasSpecial) {
                    write(39);
                }
                for (i = 0; i < len; i++) {
                    int ch2 = text.charAt(i);
                    if (ch2 >= specicalFlags_singleQuotes.length || specicalFlags_singleQuotes[ch2] == (byte) 0) {
                        write(ch2);
                    } else {
                        write(92);
                        write(IOUtils.replaceChars[ch2]);
                    }
                }
                if (hasSpecial) {
                    write(39);
                }
                write(58);
                return;
            }
        }
        if (len == 0) {
            if (this.count + 3 > this.buf.length) {
                expandCapacity(this.count + 3);
            }
            char[] cArr = this.buf;
            int i2 = this.count;
            this.count = i2 + 1;
            cArr[i2] = '\'';
            cArr = this.buf;
            i2 = this.count;
            this.count = i2 + 1;
            cArr[i2] = '\'';
            cArr = this.buf;
            i2 = this.count;
            this.count = i2 + 1;
            cArr[i2] = ':';
            return;
        }
        int start = this.count;
        int end = start + len;
        text.getChars(0, len, this.buf, start);
        this.count = newcount;
        hasSpecial = false;
        i = start;
        while (i < end) {
            ch = this.buf[i];
            if (ch < specicalFlags_singleQuotes.length && specicalFlags_singleQuotes[ch] != (byte) 0) {
                if (hasSpecial) {
                    newcount++;
                    if (newcount > this.buf.length) {
                        expandCapacity(newcount);
                    }
                    this.count = newcount;
                    System.arraycopy(this.buf, i + 1, this.buf, i + 2, end - i);
                    this.buf[i] = '\\';
                    i++;
                    this.buf[i] = IOUtils.replaceChars[ch];
                    end++;
                } else {
                    newcount += 3;
                    if (newcount > this.buf.length) {
                        expandCapacity(newcount);
                    }
                    this.count = newcount;
                    System.arraycopy(this.buf, i + 1, this.buf, i + 3, (end - i) - 1);
                    System.arraycopy(this.buf, 0, this.buf, 1, i);
                    this.buf[start] = '\'';
                    i++;
                    this.buf[i] = '\\';
                    i++;
                    this.buf[i] = IOUtils.replaceChars[ch];
                    end += 2;
                    this.buf[this.count - 2] = '\'';
                    hasSpecial = true;
                }
            }
            i++;
        }
        this.buf[newcount - 1] = ':';
    }

    public void flush() {
        if (this.writer != null) {
            try {
                this.writer.write(this.buf, 0, this.count);
                this.writer.flush();
                this.count = 0;
            } catch (IOException e) {
                throw new JSONException(e.getMessage(), e);
            }
        }
    }
}
