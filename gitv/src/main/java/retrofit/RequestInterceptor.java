package retrofit;

public interface RequestInterceptor {
    public static final RequestInterceptor NONE = new RequestInterceptor() {
        public void intercept(RequestFacade request) {
        }
    };

    public interface RequestFacade {
        void addEncodedPathParam(String str, String str2);

        void addEncodedQueryParam(String str, String str2);

        void addHeader(String str, String str2);

        void addPathParam(String str, String str2);

        void addQueryParam(String str, String str2);
    }

    void intercept(RequestFacade requestFacade);
}
