package retrofit.mime;

import com.gala.video.webview.utils.WebSDKConstants;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;

public final class FormUrlEncodedTypedOutput implements TypedOutput {
    final ByteArrayOutputStream content = new ByteArrayOutputStream();

    public void addField(String name, String value) {
        if (name == null) {
            throw new NullPointerException(WebSDKConstants.PARAM_KEY_PL_NAME);
        } else if (value == null) {
            throw new NullPointerException("value");
        } else {
            if (this.content.size() > 0) {
                this.content.write(38);
            }
            try {
                name = URLEncoder.encode(name, "UTF-8");
                value = URLEncoder.encode(value, "UTF-8");
                this.content.write(name.getBytes("UTF-8"));
                this.content.write(61);
                this.content.write(value.getBytes("UTF-8"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public String fileName() {
        return null;
    }

    public String mimeType() {
        return "application/x-www-form-urlencoded; charset=UTF-8";
    }

    public long length() {
        return (long) this.content.size();
    }

    public void writeTo(OutputStream out) throws IOException {
        out.write(this.content.toByteArray());
    }
}
