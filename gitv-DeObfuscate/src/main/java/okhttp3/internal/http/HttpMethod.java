package okhttp3.internal.http;

import org.cybergarage.http.HTTP;

public final class HttpMethod {
    public static boolean invalidatesCache(String method) {
        return method.equals(HTTP.POST) || method.equals("PATCH") || method.equals("PUT") || method.equals("DELETE") || method.equals("MOVE");
    }

    public static boolean requiresRequestBody(String method) {
        return method.equals(HTTP.POST) || method.equals("PUT") || method.equals("PATCH") || method.equals("PROPPATCH") || method.equals("REPORT");
    }

    public static boolean permitsRequestBody(String method) {
        if (requiresRequestBody(method) || method.equals("OPTIONS") || method.equals("DELETE") || method.equals("PROPFIND") || method.equals("MKCOL") || method.equals("LOCK")) {
            return true;
        }
        return false;
    }

    public static boolean redirectsWithBody(String method) {
        return method.equals("PROPFIND");
    }

    public static boolean redirectsToGet(String method) {
        return !method.equals("PROPFIND");
    }

    private HttpMethod() {
    }
}
