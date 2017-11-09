package retrofit.mime;

import java.io.IOException;
import java.io.InputStream;

public interface TypedInput {
    InputStream in() throws IOException;

    long length();

    String mimeType();
}
