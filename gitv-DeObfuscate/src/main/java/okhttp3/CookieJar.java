package okhttp3;

import java.util.Collections;
import java.util.List;

public interface CookieJar {
    public static final CookieJar NO_COOKIES = new C21511();

    static class C21511 implements CookieJar {
        C21511() {
        }

        public void saveFromResponse(HttpUrl url, List<Cookie> list) {
        }

        public List<Cookie> loadForRequest(HttpUrl url) {
            return Collections.emptyList();
        }
    }

    List<Cookie> loadForRequest(HttpUrl httpUrl);

    void saveFromResponse(HttpUrl httpUrl, List<Cookie> list);
}
