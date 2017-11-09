package com.gala.video.lib.framework.coreservice.netdiagnose.utils;

import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.cybergarage.http.HTTP;
import org.json.JSONObject;

public class NetDiagnoseDNSUtils {
    private static String DNSINFO_COST_TIME = "DNS info cost time = ";
    private static String DNSINFO_PARSE_ERROR = "DNS INFO parse error";
    private static String DNS_INFO_GET_ERROR = "get DNS info fail";
    private static final String DNS_URL = "http://api.systest.ptqy.gitv.tv/app/test?version=2.0&method=dns%7cscache%7cresult&devtype=gitv&checksum=7c9434c36bc19b45fb8519ebe7ea4318";
    private static final int RESPONSE_OK = 200;
    private static final String TAG = "NetDiagnoseDNSUtils";
    private static String URL_IS_NULL = "url is null :";
    private static String sDNSResult;
    private static String sDnsUrl;
    private static String sResultUrl;
    private static String sScacheUrl;

    @Deprecated
    public static String executeDNS() {
        long time = System.currentTimeMillis();
        try {
            JSONObject jsonObject = new JSONObject(requestByGet(DNS_URL));
            sDnsUrl = (String) jsonObject.get("dns-url");
            LogUtils.d(TAG, "sDnsUrl = " + sDnsUrl);
            sScacheUrl = (String) jsonObject.get("scache-url");
            LogUtils.d(TAG, "sScacheUrl =  " + sScacheUrl);
            sResultUrl = (String) jsonObject.get("result-url");
            LogUtils.d(TAG, "sResultUrl =  " + sResultUrl);
            requestByGet(sDnsUrl);
            requestByGet(sScacheUrl);
            sDNSResult = requestByGet(sResultUrl);
        } catch (Exception e) {
            sDNSResult = DNSINFO_PARSE_ERROR;
            e.printStackTrace();
        }
        StringBuffer result = new StringBuffer();
        result.append(DNSINFO_COST_TIME).append(System.currentTimeMillis() - time).append("ms").append(sDNSResult);
        return result.toString();
    }

    public static String executeDNS2() {
        long time = System.currentTimeMillis();
        try {
            JSONObject jsonObject = new JSONObject(requestHttpByGet(DNS_URL));
            sDnsUrl = (String) jsonObject.get("dns-url");
            LogUtils.d(TAG, "sDnsUrl = " + sDnsUrl);
            sScacheUrl = (String) jsonObject.get("scache-url");
            LogUtils.d(TAG, "sScacheUrl =  " + sScacheUrl);
            sResultUrl = (String) jsonObject.get("result-url");
            LogUtils.d(TAG, "sResultUrl =  " + sResultUrl);
            requestHttpByGet(sDnsUrl);
            requestHttpByGet(sScacheUrl);
            sDNSResult = requestHttpByGet(sResultUrl);
        } catch (Exception e) {
            sDNSResult = DNSINFO_PARSE_ERROR;
            e.printStackTrace();
        }
        StringBuffer result = new StringBuffer();
        result.append(DNSINFO_COST_TIME).append(System.currentTimeMillis() - time).append("ms").append(sDNSResult);
        return result.toString();
    }

    @Deprecated
    private static String requestByGet(String url) {
        if (StringUtils.isEmpty((CharSequence) url)) {
            return URL_IS_NULL;
        }
        String result;
        try {
            HttpResponse response = getHttpClient().execute(new HttpGet(url));
            if (response.getStatusLine().getStatusCode() == 200) {
                return EntityUtils.toString(response.getEntity(), "UTF-8");
            }
            LogUtils.d(TAG, "response.getStatusLine().getStatusCode() = " + response.getStatusLine().getStatusCode());
            result = DNS_INFO_GET_ERROR;
            return result;
        } catch (ClientProtocolException e) {
            result = DNS_INFO_GET_ERROR;
            e.printStackTrace();
        } catch (Exception e2) {
            result = DNS_INFO_GET_ERROR;
            e2.printStackTrace();
        }
    }

    private static String requestHttpByGet(String url) {
        Throwable th;
        if (StringUtils.isEmpty((CharSequence) url)) {
            return URL_IS_NULL;
        }
        String result;
        BufferedReader bufferedReader = null;
        try {
            HttpURLConnection conn = getHttpURLConnection(url);
            if (200 == conn.getResponseCode()) {
                BufferedReader bufferedReader2 = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                try {
                    StringBuffer stringBuffer = new StringBuffer();
                    while (true) {
                        String line = bufferedReader2.readLine();
                        if (line == null) {
                            break;
                        }
                        stringBuffer.append(line);
                    }
                    result = stringBuffer.toString();
                    bufferedReader = bufferedReader2;
                } catch (Exception e) {
                    bufferedReader = bufferedReader2;
                    try {
                        result = DNS_INFO_GET_ERROR;
                        if (bufferedReader != null) {
                            try {
                                bufferedReader.close();
                            } catch (IOException e2) {
                                e2.printStackTrace();
                            }
                        }
                        return result;
                    } catch (Throwable th2) {
                        th = th2;
                        if (bufferedReader != null) {
                            try {
                                bufferedReader.close();
                            } catch (IOException e22) {
                                e22.printStackTrace();
                            }
                        }
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    bufferedReader = bufferedReader2;
                    if (bufferedReader != null) {
                        bufferedReader.close();
                    }
                    throw th;
                }
            }
            result = DNS_INFO_GET_ERROR;
            conn.disconnect();
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e222) {
                    e222.printStackTrace();
                }
            }
        } catch (Exception e3) {
            result = DNS_INFO_GET_ERROR;
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            return result;
        }
        return result;
    }

    @Deprecated
    private static DefaultHttpClient getHttpClient() {
        HttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, 3000);
        HttpConnectionParams.setSoTimeout(httpParams, 8000);
        HttpConnectionParams.setSocketBufferSize(httpParams, 8192);
        HttpClientParams.setRedirecting(httpParams, true);
        return new DefaultHttpClient(httpParams);
    }

    private static HttpURLConnection getHttpURLConnection(String urlPath) throws Exception {
        HttpURLConnection conn = (HttpURLConnection) new URL(urlPath).openConnection();
        conn.setRequestMethod(HTTP.GET);
        conn.setConnectTimeout(3000);
        conn.setReadTimeout(8000);
        conn.setRequestProperty("Charset", "UTF-8");
        return conn;
    }
}
