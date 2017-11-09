package okhttp3.internal.http2;

import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.WidgetType;
import okhttp3.internal.Util;
import okio.ByteString;
import org.cybergarage.soap.SOAP;

public final class Header {
    public static final ByteString PSEUDO_PREFIX = ByteString.encodeUtf8(SOAP.DELIM);
    public static final ByteString RESPONSE_STATUS = ByteString.encodeUtf8(":status");
    public static final ByteString TARGET_AUTHORITY = ByteString.encodeUtf8(":authority");
    public static final ByteString TARGET_METHOD = ByteString.encodeUtf8(":method");
    public static final ByteString TARGET_PATH = ByteString.encodeUtf8(":path");
    public static final ByteString TARGET_SCHEME = ByteString.encodeUtf8(":scheme");
    final int hpackSize;
    public final ByteString name;
    public final ByteString value;

    public Header(String name, String value) {
        this(ByteString.encodeUtf8(name), ByteString.encodeUtf8(value));
    }

    public Header(ByteString name, String value) {
        this(name, ByteString.encodeUtf8(value));
    }

    public Header(ByteString name, ByteString value) {
        this.name = name;
        this.value = value;
        this.hpackSize = (name.size() + 32) + value.size();
    }

    public boolean equals(Object other) {
        if (!(other instanceof Header)) {
            return false;
        }
        Header that = (Header) other;
        if (this.name.equals(that.name) && this.value.equals(that.value)) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return ((this.name.hashCode() + WidgetType.ITEM_SETTING_NETWORK) * 31) + this.value.hashCode();
    }

    public String toString() {
        return Util.format("%s: %s", this.name.utf8(), this.value.utf8());
    }
}
