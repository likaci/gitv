package retrofit;

public interface ErrorHandler {
    public static final ErrorHandler DEFAULT = new C22171();

    static class C22171 implements ErrorHandler {
        C22171() {
        }

        public Throwable handleError(RetrofitError cause) {
            return cause;
        }
    }

    Throwable handleError(RetrofitError retrofitError);
}
