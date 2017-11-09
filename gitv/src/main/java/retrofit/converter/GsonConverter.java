package retrofit.converter;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import retrofit.mime.MimeUtil;
import retrofit.mime.TypedInput;
import retrofit.mime.TypedOutput;

public class GsonConverter implements Converter {
    private String encoding;
    private final Gson gson;

    private static class JsonTypedOutput implements TypedOutput {
        private final byte[] jsonBytes;
        private final String mimeType;

        JsonTypedOutput(byte[] jsonBytes, String encode) {
            this.jsonBytes = jsonBytes;
            this.mimeType = "application/json; charset=" + encode;
        }

        public String fileName() {
            return null;
        }

        public String mimeType() {
            return this.mimeType;
        }

        public long length() {
            return (long) this.jsonBytes.length;
        }

        public void writeTo(OutputStream out) throws IOException {
            out.write(this.jsonBytes);
        }
    }

    public GsonConverter(Gson gson) {
        this(gson, "UTF-8");
    }

    public GsonConverter(Gson gson, String encoding) {
        this.gson = gson;
        this.encoding = encoding;
    }

    public Object fromBody(TypedInput body, Type type) throws ConversionException {
        Throwable e;
        Throwable th;
        String charset = "UTF-8";
        if (body.mimeType() != null) {
            charset = MimeUtil.parseCharset(body.mimeType());
        }
        InputStreamReader inputStreamReader = null;
        try {
            InputStreamReader isr = new InputStreamReader(body.in(), charset);
            try {
                Object fromJson = this.gson.fromJson(isr, type);
                if (isr != null) {
                    try {
                        isr.close();
                    } catch (IOException e2) {
                    }
                }
                return fromJson;
            } catch (IOException e3) {
                e = e3;
                inputStreamReader = isr;
                try {
                    throw new ConversionException(e);
                } catch (Throwable th2) {
                    th = th2;
                    if (inputStreamReader != null) {
                        try {
                            inputStreamReader.close();
                        } catch (IOException e4) {
                        }
                    }
                    throw th;
                }
            } catch (JsonParseException e5) {
                e = e5;
                inputStreamReader = isr;
                throw new ConversionException(e);
            } catch (Throwable th3) {
                th = th3;
                inputStreamReader = isr;
                if (inputStreamReader != null) {
                    inputStreamReader.close();
                }
                throw th;
            }
        } catch (IOException e6) {
            e = e6;
            throw new ConversionException(e);
        } catch (JsonParseException e7) {
            e = e7;
            throw new ConversionException(e);
        }
    }

    public TypedOutput toBody(Object object) {
        try {
            return new JsonTypedOutput(this.gson.toJson(object).getBytes(this.encoding), this.encoding);
        } catch (UnsupportedEncodingException e) {
            throw new AssertionError(e);
        }
    }
}
