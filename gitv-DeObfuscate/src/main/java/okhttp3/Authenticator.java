package okhttp3;

import java.io.IOException;

public interface Authenticator {
    public static final Authenticator NONE = new C21451();

    static class C21451 implements Authenticator {
        C21451() {
        }

        public Request authenticate(Route route, Response response) {
            return null;
        }
    }

    Request authenticate(Route route, Response response) throws IOException;
}
