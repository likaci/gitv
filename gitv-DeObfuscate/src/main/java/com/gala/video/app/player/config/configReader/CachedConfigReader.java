package com.gala.video.app.player.config.configReader;

import android.content.Context;
import com.gala.sdk.utils.MyLogUtils;
import com.gala.video.app.player.config.configReader.IConfigReader.OnConfigReadListener;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.system.preference.SystemConfigPreference;
import java.io.FileInputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.http.util.EncodingUtils;

public class CachedConfigReader implements IConfigReader {
    private final String TAG;
    private Context mContext;
    private ExecutorService mSingleThreadPool;

    private static class CachedConfigReaderInstanceHolder {
        public static CachedConfigReader sCachedConfigReader = new CachedConfigReader();

        private CachedConfigReaderInstanceHolder() {
        }
    }

    private CachedConfigReader() {
        this.TAG = "ConfigReader/CachedConfigReader@" + Integer.toHexString(hashCode());
        this.mContext = AppRuntimeEnv.get().getApplicationContext();
        this.mSingleThreadPool = Executors.newSingleThreadExecutor();
    }

    public static CachedConfigReader instance() {
        return CachedConfigReaderInstanceHolder.sCachedConfigReader;
    }

    public void readConfigAsync(OnConfigReadListener listener) {
        MyLogUtils.m462d(this.TAG, "readConfigAsync(OnConfigReadListener:" + listener + ")");
        CharSequence cachedConfigPath = getCachedConfigPath();
        if (!StringUtils.isEmpty(cachedConfigPath)) {
            parseCachedFileAsync(cachedConfigPath, listener);
        } else if (listener != null) {
            listener.onFailed(new Exception("Cached Config Unavailable!"));
        }
    }

    private void parseCachedFileAsync(final String filePath, final OnConfigReadListener listener) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "parseCachedFileAsync()");
        }
        this.mSingleThreadPool.execute(new Runnable() {
            public void run() {
                try {
                    CharSequence jsonResult = CachedConfigReader.this.parseCachedFileSync(filePath);
                    if (listener == null) {
                        return;
                    }
                    if (StringUtils.isEmpty(jsonResult)) {
                        listener.onFailed(new Exception("wrong json result fetched"));
                    } else {
                        listener.onSuccess(jsonResult);
                    }
                } catch (Throwable e) {
                    if (listener != null) {
                        listener.onFailed(e);
                    }
                }
            }
        });
    }

    private String parseCachedFileSync(String filePath) throws Throwable {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "parseCachedFileSync(" + filePath + ")");
        }
        String content = "";
        try {
            FileInputStream fin = new FileInputStream(filePath);
            int length = fin.available();
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(this.TAG, "read file:" + filePath + ", length = " + length);
            }
            byte[] buffer = new byte[length];
            fin.read(buffer);
            content = EncodingUtils.getString(buffer, "UTF-8");
            fin.close();
            return content;
        } catch (Exception e) {
            if (!LogUtils.mIsDebug) {
                return content;
            }
            LogUtils.m1569d(this.TAG, "read file:" + filePath + ", exception occurs!", e);
            return content;
        }
    }

    private String getCachedConfigPath() {
        return SystemConfigPreference.getPlayerConfigCachedJsonResultPath(this.mContext);
    }

    public String readConfigSync() {
        MyLogUtils.m462d(this.TAG, "readConfigSync()");
        try {
            CharSequence cachedConfigPath = getCachedConfigPath();
            if (StringUtils.isEmpty(cachedConfigPath)) {
                return null;
            }
            return parseCachedFileSync(cachedConfigPath);
        } catch (Throwable th) {
            return null;
        }
    }

    private boolean cachedConfigAvailable(String path, String subChannelJsPath) {
        MyLogUtils.m462d(this.TAG, "cachedConfigAvailable(path: " + path + "), return " + equalsWithRemotePath(path, subChannelJsPath));
        return false;
    }

    private boolean equalsWithRemotePath(String remotePath, String subChannelJsPath) {
        CharSequence cachedJsPath = SystemConfigPreference.getPlayerConfigRemoteJsPath(this.mContext);
        boolean equals = !StringUtils.isEmpty(cachedJsPath) && cachedJsPath.equalsIgnoreCase(remotePath + subChannelJsPath);
        MyLogUtils.m462d(this.TAG, "equalsWithRemotePath(remotePath: " + remotePath + " ,subChannelJsPath:" + subChannelJsPath + "), cachedJsPath: " + cachedJsPath + ", return " + equals);
        return equals;
    }
}
