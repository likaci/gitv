package retrofit;

public interface RequestInterceptor {
    public static final RequestInterceptor NONE = new C22281();

    public interface RequestFacade {
        void addEncodedPathParam(String str, String str2);

        void addEncodedQueryParam(String str, String str2);

        void addHeader(String str, String str2);

        void addPathParam(String str, String str2);

        void addQueryParam(String str, String str2);
    }

    static class C22281 implements RequestInterceptor {
        C22281() {
        }

        public void intercept(RequestFacade request) {
        }
    }

    void intercept(RequestFacade requestFacade);
}
