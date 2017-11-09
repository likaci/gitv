package okhttp3;

import com.push.pushservice.constants.PushConstants;
import java.nio.charset.Charset;
import okio.ByteString;
import org.cybergarage.soap.SOAP;

public final class Credentials {
    private Credentials() {
    }

    public static String basic(String userName, String password) {
        return basic(userName, password, Charset.forName(PushConstants.CHARACTER_CODE));
    }

    public static String basic(String userName, String password, Charset charset) {
        return "Basic " + ByteString.of((userName + SOAP.DELIM + password).getBytes(charset)).base64();
    }
}
