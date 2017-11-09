package retrofit;

public final class Endpoints {
    private static final String DEFAULT_NAME = "default";

    private static class FixedEndpoint implements Endpoint {
        private final String apiUrl;
        private final String name;

        FixedEndpoint(String apiUrl, String name) {
            this.apiUrl = apiUrl;
            this.name = name;
        }

        public String getUrl() {
            return this.apiUrl;
        }

        public String getName() {
            return this.name;
        }
    }

    private Endpoints() {
    }

    public static Endpoint newFixedEndpoint(String url) {
        return new FixedEndpoint(url, "default");
    }

    public static Endpoint newFixedEndpoint(String url, String name) {
        return new FixedEndpoint(url, name);
    }
}
