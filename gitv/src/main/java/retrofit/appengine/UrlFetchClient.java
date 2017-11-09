package retrofit.appengine;

import com.google.appengine.api.urlfetch.HTTPHeader;
import com.google.appengine.api.urlfetch.HTTPMethod;
import com.google.appengine.api.urlfetch.HTTPRequest;
import com.google.appengine.api.urlfetch.HTTPResponse;
import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.cybergarage.http.HTTP;
import retrofit.client.Client;
import retrofit.client.Header;
import retrofit.client.Request;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;
import retrofit.mime.TypedOutput;

public class UrlFetchClient implements Client {
    private final URLFetchService urlFetchService;

    private static HTTPMethod getHttpMethod(String method) {
        if (HTTP.GET.equals(method)) {
            return HTTPMethod.GET;
        }
        if (HTTP.POST.equals(method)) {
            return HTTPMethod.POST;
        }
        if ("PATCH".equals(method)) {
            return HTTPMethod.PATCH;
        }
        if ("PUT".equals(method)) {
            return HTTPMethod.PUT;
        }
        if ("DELETE".equals(method)) {
            return HTTPMethod.DELETE;
        }
        if (HTTP.HEAD.equals(method)) {
            return HTTPMethod.HEAD;
        }
        throw new IllegalStateException("Illegal HTTP method: " + method);
    }

    public UrlFetchClient() {
        this(URLFetchServiceFactory.getURLFetchService());
    }

    public UrlFetchClient(URLFetchService urlFetchService) {
        this.urlFetchService = urlFetchService;
    }

    public Response execute(Request request) throws IOException {
        return parseResponse(execute(this.urlFetchService, createRequest(request)));
    }

    protected HTTPResponse execute(URLFetchService urlFetchService, HTTPRequest request) throws IOException {
        return urlFetchService.fetch(request);
    }

    static HTTPRequest createRequest(Request request) throws IOException {
        HTTPRequest fetchRequest = new HTTPRequest(new URL(request.getUrl()), getHttpMethod(request.getMethod()));
        for (Header header : request.getHeaders()) {
            fetchRequest.addHeader(new HTTPHeader(header.getName(), header.getValue()));
        }
        TypedOutput body = request.getBody();
        if (body != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            body.writeTo(baos);
            fetchRequest.setPayload(baos.toByteArray());
        }
        return fetchRequest;
    }

    static Response parseResponse(HTTPResponse response) {
        String url = response.getFinalUrl().toString();
        int status = response.getResponseCode();
        List<HTTPHeader> fetchHeaders = response.getHeaders();
        List<Header> headers = new ArrayList(fetchHeaders.size());
        String contentType = "application/octet-stream";
        for (HTTPHeader fetchHeader : fetchHeaders) {
            String name = fetchHeader.getName();
            String value = fetchHeader.getValue();
            if (HTTP.CONTENT_TYPE.equalsIgnoreCase(name)) {
                contentType = value;
            }
            headers.add(new Header(name, value));
        }
        TypedByteArray body = null;
        byte[] fetchBody = response.getContent();
        if (fetchBody != null) {
            body = new TypedByteArray(contentType, fetchBody);
        }
        return new Response(url, status, "", headers, body);
    }
}
