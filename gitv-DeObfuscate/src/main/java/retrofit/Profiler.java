package retrofit;

public interface Profiler<T> {

    public static final class RequestInformation {
        private final String baseUrl;
        private final long contentLength;
        private final String contentType;
        private final String method;
        private final String relativePath;

        public RequestInformation(String method, String baseUrl, String relativePath, long contentLength, String contentType) {
            this.method = method;
            this.baseUrl = baseUrl;
            this.relativePath = relativePath;
            this.contentLength = contentLength;
            this.contentType = contentType;
        }

        public String getMethod() {
            return this.method;
        }

        public String getBaseUrl() {
            return this.baseUrl;
        }

        public String getRelativePath() {
            return this.relativePath;
        }

        public long getContentLength() {
            return this.contentLength;
        }

        public String getContentType() {
            return this.contentType;
        }
    }

    void afterCall(RequestInformation requestInformation, long j, int i, T t);

    T beforeCall();
}
