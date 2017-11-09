package retrofit.mime;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import org.cybergarage.http.HTTP;

public class TypedByteArray implements TypedInput, TypedOutput {
    private final byte[] bytes;
    private final String mimeType;

    public TypedByteArray(String mimeType, byte[] bytes) {
        if (mimeType == null) {
            mimeType = "application/unknown";
        }
        if (bytes == null) {
            throw new NullPointerException(HTTP.CONTENT_RANGE_BYTES);
        }
        this.mimeType = mimeType;
        this.bytes = bytes;
    }

    public byte[] getBytes() {
        return this.bytes;
    }

    public String fileName() {
        return null;
    }

    public String mimeType() {
        return this.mimeType;
    }

    public long length() {
        return (long) this.bytes.length;
    }

    public void writeTo(OutputStream out) throws IOException {
        out.write(this.bytes);
    }

    public InputStream in() throws IOException {
        return new ByteArrayInputStream(this.bytes);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TypedByteArray that = (TypedByteArray) o;
        if (!Arrays.equals(this.bytes, that.bytes)) {
            return false;
        }
        if (this.mimeType.equals(that.mimeType)) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return (this.mimeType.hashCode() * 31) + Arrays.hashCode(this.bytes);
    }
}
