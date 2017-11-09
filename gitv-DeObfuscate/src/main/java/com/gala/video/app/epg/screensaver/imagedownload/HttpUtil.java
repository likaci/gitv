package com.gala.video.app.epg.screensaver.imagedownload;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import org.cybergarage.http.HTTP;

class HttpUtil {
    HttpUtil() {
    }

    public HttpURLConnection getHttpURLConnection(String urlPath) throws Exception {
        HttpURLConnection conn = (HttpURLConnection) new URL(urlPath).openConnection();
        conn.setRequestMethod(HTTP.GET);
        conn.setConnectTimeout(5000);
        return conn;
    }

    public byte[] readStream(InputStream is) throws Exception {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        while (true) {
            int len = is.read(buffer);
            if (len != -1) {
                os.write(buffer, 0, len);
            } else {
                os.close();
                is.close();
                return os.toByteArray();
            }
        }
    }
}
