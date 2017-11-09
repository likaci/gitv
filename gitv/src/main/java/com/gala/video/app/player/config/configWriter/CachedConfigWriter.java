package com.gala.video.app.player.config.configWriter;

import android.content.Context;
import com.gala.video.app.player.config.configWriter.IConfigWriter.OnConfigWriteListener;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.framework.core.utils.io.FileUtil;
import com.gala.video.lib.share.system.preference.SystemConfigPreference;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CachedConfigWriter implements IConfigWriter {
    private static final String CACHED_CONFIG_JSON_NAME = "playerConfig.json";
    private static String sDefaultCachedConfigPath;
    private final String TAG;
    private Context mContext;
    private ExecutorService mSingleThreadPool;

    private static class CachedConfigWriterInstanceHolder {
        public static CachedConfigWriter sCachedConfigWriter = new CachedConfigWriter();

        private CachedConfigWriterInstanceHolder() {
        }
    }

    private CachedConfigWriter() {
        this.TAG = "ConfigWriter/CachedConfigWriter@" + Integer.toHexString(hashCode());
        this.mContext = AppRuntimeEnv.get().getApplicationContext();
        this.mSingleThreadPool = Executors.newSingleThreadExecutor();
        sDefaultCachedConfigPath = this.mContext.getFilesDir() + "/" + CACHED_CONFIG_JSON_NAME;
    }

    public static CachedConfigWriter instance() {
        return CachedConfigWriterInstanceHolder.sCachedConfigWriter;
    }

    public void writeConfigAsync(final String content, final OnConfigWriteListener listener) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "writeConfigAsync()");
        }
        this.mSingleThreadPool.execute(new Runnable() {
            public void run() {
                boolean success = CachedConfigWriter.this.writeConfigSync(content);
                if (listener == null) {
                    return;
                }
                if (success) {
                    listener.onSuccess();
                } else {
                    listener.onFailed(new Exception("write cached config json failed!"));
                }
            }
        });
    }

    public boolean writeConfigSync(String content) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "writeConfigSync()");
        }
        boolean success = false;
        if (content != null) {
            String cachedJsonResultPath = SystemConfigPreference.getPlayerConfigCachedJsonResultPath(this.mContext);
            if (StringUtils.isEmpty((CharSequence) cachedJsonResultPath)) {
                cachedJsonResultPath = sDefaultCachedConfigPath;
            }
            if (LogUtils.mIsDebug) {
                LogUtils.d(this.TAG, "writeConfigSync(), cachedJsonResultPath:" + cachedJsonResultPath);
            }
            if (FileUtil.exites(cachedJsonResultPath)) {
                FileUtil.remove(cachedJsonResultPath);
            }
            FileUtil.checkDir(cachedJsonResultPath);
            success = FileUtil.writeFile(cachedJsonResultPath, content.getBytes());
            if (success) {
                SystemConfigPreference.setPlayerConfigCachedJsonResultPath(this.mContext, cachedJsonResultPath);
            }
        }
        return success;
    }
}
