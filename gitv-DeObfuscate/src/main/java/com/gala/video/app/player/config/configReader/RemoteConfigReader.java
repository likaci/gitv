package com.gala.video.app.player.config.configReader;

import android.os.SystemClock;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gala.sdk.utils.MyLogUtils;
import com.gala.video.app.player.config.DeviceInfoParams;
import com.gala.video.app.player.config.configReader.IConfigReader.OnConfigReadListener;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.dynamic.IDynamicResult;
import com.gala.video.lib.share.utils.JsExcutor;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class RemoteConfigReader implements IConfigReader {
    private static final int CONN_TIMEOUT = 4000;
    private static final int READ_TIMEOUT = 8000;
    private final String TAG;
    private ExecutorService mCachedThreadPool;

    private static class RemoteConfigReaderInstanceHolder {
        public static RemoteConfigReader sRemoteConfigReader = new RemoteConfigReader();

        private RemoteConfigReaderInstanceHolder() {
        }
    }

    private RemoteConfigReader() {
        this.TAG = "ConfigReader/RemoteConfigReader@" + Integer.toHexString(hashCode());
        this.mCachedThreadPool = new ThreadPoolExecutor(0, 4, 0, TimeUnit.SECONDS, new SynchronousQueue());
    }

    public static RemoteConfigReader instance() {
        return RemoteConfigReaderInstanceHolder.sRemoteConfigReader;
    }

    public void readConfigAsync(OnConfigReadListener listener) {
        CharSequence path;
        String subChannelPath = null;
        IDynamicResult model = GetInterfaceTools.getIDynamicQDataProvider().getDynamicQDataModel();
        if (model != null) {
            path = model.getPlayerConfig();
        } else {
            Object path2 = null;
        }
        if (model != null) {
            subChannelPath = model.getSubChannelPlayerConfig();
        }
        MyLogUtils.m462d(this.TAG, "readConfigAsync(path:" + path + ",subChannelPath：" + subChannelPath + " OnConfigReadListener:" + listener + ")");
        if (!StringUtils.isEmpty(path)) {
            parseRemoteFileAsync(path, subChannelPath, listener);
        } else if (listener != null) {
            listener.onFailed(new Exception("path is empty"));
        }
    }

    public String readConfigSync() {
        CharSequence path;
        String subChannelPath = null;
        IDynamicResult model = GetInterfaceTools.getIDynamicQDataProvider().getDynamicQDataModel();
        if (model != null) {
            path = model.getPlayerConfig();
        } else {
            Object path2 = null;
        }
        if (model != null) {
            subChannelPath = model.getSubChannelPlayerConfig();
        }
        MyLogUtils.m462d(this.TAG, "readConfigAsync(path:" + path + " ,subChannelPath:" + subChannelPath + ",OnConfigReadListener:");
        if (StringUtils.isEmpty(path)) {
            return null;
        }
        try {
            return mergeJsonResult(parseRemoteFileSync(path), parseRemoteFileSync(subChannelPath));
        } catch (Throwable th) {
            return null;
        }
    }

    private String mergeJsonResult(String genernalConfig, String subChannelConfig) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "mergeJsonResult(genernalConfig: " + genernalConfig + ", subChannelConfig: " + subChannelConfig + ")");
        }
        if (StringUtils.isEmpty((CharSequence) subChannelConfig) || StringUtils.isEmpty((CharSequence) genernalConfig)) {
            return genernalConfig;
        }
        Map<String, String> generalConfig = (Map) JSON.parseObject(genernalConfig, Map.class);
        if (generalConfig != null) {
            for (Entry<String, String> entry : ((Map) JSON.parseObject(subChannelConfig, Map.class)).entrySet()) {
                generalConfig.put(entry.getKey(), entry.getValue());
            }
        }
        JSONObject json = new JSONObject();
        json.putAll(generalConfig);
        return json.toJSONString();
    }

    private void parseRemoteFileAsync(final String url, final String subChannelUrl, final OnConfigReadListener listener) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "parseRemoteFileAsync(" + url + "," + subChannelUrl + ")");
        }
        this.mCachedThreadPool.execute(new Runnable() {
            public void run() {
                try {
                    CharSequence jsonResult = RemoteConfigReader.this.parseRemoteFileSync(url);
                    String subChannelJson = RemoteConfigReader.this.parseRemoteFileSync(subChannelUrl);
                    if (listener == null) {
                        return;
                    }
                    if (StringUtils.isEmpty(jsonResult)) {
                        listener.onFailed(new Exception("wrong json result fetched"));
                        return;
                    }
                    listener.onSuccess(RemoteConfigReader.this.mergeJsonResult(jsonResult, subChannelJson));
                } catch (Throwable e) {
                    if (listener != null) {
                        listener.onFailed(e);
                    }
                }
            }
        });
    }

    private String parseRemoteFileSync(String url) throws Throwable {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "parseRemoteFileSync(" + url + ")");
        }
        byte[] bytes = readRemoteFile(url);
        if (bytes == null || bytes.length == 0) {
            throw new Exception("invalid javascript content!");
        }
        CharSequence jsContent = new String(bytes);
        String fileName = new File(url).getName();
        String functionName = fileName.substring(0, fileName.lastIndexOf("."));
        if (StringUtils.isEmpty(jsContent) || !jsContent.contains(functionName)) {
            return null;
        }
        return JsExcutor.runScript(jsContent, functionName, DeviceInfoParams.instance().getParams(), AppRuntimeEnv.get().getApplicationContext());
    }

    private byte[] readRemoteFile(String url) throws ClientProtocolException, IOException, AssertionError {
        long start = SystemClock.uptimeMillis();
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, ">> httpRequest(): conn TO=4000, read TO=8000");
        }
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "httpRequest: url=" + url);
        }
        HttpGet httpget = new HttpGet(url);
        HttpClient httpClient = new DefaultHttpClient();
        httpClient.getParams().setParameter("http.connection.timeout", Integer.valueOf(4000));
        httpClient.getParams().setParameter("http.socket.timeout", Integer.valueOf(READ_TIMEOUT));
        HttpResponse response = httpClient.execute(httpget);
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "httpRequest: response code=" + response.getStatusLine().getStatusCode() + ", request time consumed=" + (SystemClock.uptimeMillis() - start));
        }
        byte[] bytes = null;
        if (response.getStatusLine().getStatusCode() == 200) {
            bytes = readStream(response);
        }
        httpClient.getConnectionManager().shutdown();
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "<< httpRequest(), total time consumed=" + (SystemClock.uptimeMillis() - start));
        }
        return bytes;
    }

    private byte[] readStream(HttpResponse response) throws IOException {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, ">> readStream()");
        }
        long readStart = SystemClock.uptimeMillis();
        HttpEntity entity = response.getEntity();
        BufferedInputStream fileInStream = new BufferedInputStream(entity.getContent());
        long contentLength = entity.getContentLength();
        byte[] bytesBuffer = new byte[((int) contentLength)];
        int readFinishByteSize = 0;
        int bytesTotalSize = bytesBuffer.length;
        while (true) {
            int readBytesSize = fileInStream.read(bytesBuffer, readFinishByteSize, bytesTotalSize - readFinishByteSize);
            if (readBytesSize <= 0) {
                break;
            }
            readFinishByteSize += readBytesSize;
        }
        fileInStream.close();
        if (((long) readFinishByteSize) != contentLength) {
            throw new IOException();
        }
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "<< readStream(), total time consumed=" + (SystemClock.uptimeMillis() - readStart));
        }
        return bytesBuffer;
    }
}
