package retrofit.client;

import java.io.IOException;

public interface Client {

    public interface Provider {
        Client get();
    }

    Response execute(Request request) throws IOException;
}
