package com.gala.video.lib.share.ifimpl.web.config;

import android.os.Build;
import android.os.Build.VERSION;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gala.video.api.ApiFactory;
import com.gala.video.api.ICommonApiCallback;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.utils.DeviceUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.framework.core.utils.io.FileUtil;
import com.gala.video.lib.share.common.configs.AppClientUtils;
import com.gala.video.lib.share.project.Project;
import com.gala.video.lib.share.utils.JsExcutor;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import org.apache.http.util.EncodingUtils;

class JSConfigDataLoad {
    private static final String CONFIG_JS_FILE = "getAppConfig.js";
    private static final String DOMAINNAME_END = "/js/tv/app/getAppConfig.js";
    private static final String DOMAINNAME_TITLE = "http://static.";
    private static final String FUNCTION_NAME = "getEPGConfig";
    private static final String IGALA_DOMAIN_NAME = "ptqy.gitv.tv";
    private static final String KEY_ANDROIDVERISON = "androidVerison";
    private static final String KEY_APKVERSION = "apkVersion";
    private static final String KEY_HARDWARE = "hardware";
    private static final String KEY_MEMORY = "memory";
    private static final String KEY_MODEL = "model";
    private static final String KEY_PRODUCT = "product";
    private static final String KEY_UUID = "uuid";
    private static final String TAG = "EPG/web/JSConfigDataLoad";
    private JSConfigDataLoadCallback mJSConfigDataLoadCallback;
    private String mLocalConfigJSPath;
    private JSONObject sConfigJson;
    private String sUrl;

    class C17651 implements ICommonApiCallback {
        C17651() {
        }

        public void onSuccess(String jsFileContent) {
            if (StringUtils.isEmpty((CharSequence) jsFileContent)) {
                LogUtils.m1571e(JSConfigDataLoad.TAG, "checkCongfigJson() -> jsFileContent is null!");
                return;
            }
            LogUtils.m1568d(JSConfigDataLoad.TAG, "checkResult() -> onSuccess()");
            JSConfigDataLoad.this.checkCongfigJson(JSConfigDataLoad.this.getJsResultStr(JSConfigDataLoad.FUNCTION_NAME, jsFileContent));
        }

        public void onException(Exception e, String s) {
            new WebPingback().error(e, "ApiFactory.getCommonApi()");
            LogUtils.m1571e(JSConfigDataLoad.TAG, "checkCongfigJson() -> callSync exception = " + e);
        }
    }

    interface JSConfigDataLoadCallback {
        void onFail(String str);

        void onSuccess(JSONObject jSONObject);
    }

    JSConfigDataLoad() {
    }

    public void loadSynData(JSConfigDataLoadCallback callback) {
        this.mJSConfigDataLoadCallback = callback;
        init();
    }

    private void init() {
        initPath();
        checkAndSaveResult(this.sUrl);
        checkLocalConfig();
    }

    private void initPath() {
        String domainName = Project.getInstance().getBuild().getDomainName();
        if (StringUtils.isEmpty((CharSequence) domainName)) {
            domainName = "ptqy.gitv.tv";
        }
        this.sUrl = DOMAINNAME_TITLE + domainName + DOMAINNAME_END;
        this.mLocalConfigJSPath = AppRuntimeEnv.get().getApplicationContext().getFilesDir() + "/" + CONFIG_JS_FILE;
    }

    private void checkCongfigJson(String configJson) {
        if (StringUtils.isEmpty((CharSequence) configJson)) {
            LogUtils.m1571e(TAG, "checkCongfigJson() -> configJosn  is null ,no enableCrosswalk !!");
            return;
        }
        JSONObject jsonObject = getCongfigJsonObject(configJson);
        saveJsonToLocal(configJson);
        if (jsonObject == null) {
            LogUtils.m1571e(TAG, "checkCongfigJson() -> json jsonObject is null!");
        } else {
            this.sConfigJson = jsonObject;
        }
    }

    private void checkLocalConfig() {
        if (this.sConfigJson == null) {
            String json = getLocalConfigJson();
            if (json != null) {
                this.sConfigJson = getCongfigJsonObject(json);
            }
        }
        if (this.sConfigJson != null) {
            if (this.mJSConfigDataLoadCallback != null) {
                this.mJSConfigDataLoadCallback.onSuccess(this.sConfigJson);
            }
        } else if (this.mJSConfigDataLoadCallback != null) {
            this.mJSConfigDataLoadCallback.onFail("ConfigJson is null");
        }
    }

    private void saveJsonToLocal(String json) {
        String path = this.mLocalConfigJSPath;
        try {
            if (StringUtils.isEmpty((CharSequence) path)) {
                LogUtils.m1571e(TAG, "saveJsonToLocal() -> path :" + path);
                return;
            }
            File file = new File(path);
            if (!file.exists()) {
                file.createNewFile();
            }
            LogUtils.m1571e(TAG, "saveJsonToLocal() -> flag :" + FileUtil.writeFile(path, json));
        } catch (Exception e) {
            LogUtils.m1571e(TAG, "saveJsonToLocal()  Exception -> e :" + e);
        }
    }

    private String getLocalConfigJson() {
        String configJson = null;
        CharSequence path = this.mLocalConfigJSPath;
        if (!StringUtils.isEmpty(path) && new File(path).exists()) {
            try {
                configJson = readFile(path);
            } catch (Exception e) {
                LogUtils.m1571e(TAG, "readFile LocalConfigJson e:" + e);
            }
        }
        LogUtils.m1568d(TAG, "getLocalConfigJson() -> configJson :" + configJson);
        return configJson;
    }

    private static String readFile(String filePath) throws IOException {
        Exception e;
        Throwable th;
        String res = "";
        FileInputStream fin = null;
        try {
            FileInputStream fin2 = new FileInputStream(filePath);
            try {
                int length = fin2.available();
                if (LogUtils.mIsDebug) {
                    LogUtils.m1568d(TAG, "read file:" + filePath + ", length = " + length);
                }
                byte[] buffer = new byte[length];
                fin2.read(buffer);
                res = EncodingUtils.getString(buffer, "UTF-8");
                fin2.close();
                if (fin2 != null) {
                    try {
                        fin2.close();
                    } catch (Exception e2) {
                        LogUtils.m1571e(TAG, "readFile e2 :" + e2);
                        fin = fin2;
                    }
                }
                fin = fin2;
            } catch (Exception e3) {
                e = e3;
                fin = fin2;
                try {
                    LogUtils.m1572e(TAG, "readFile e:", e);
                    if (fin != null) {
                        try {
                            fin.close();
                        } catch (Exception e22) {
                            LogUtils.m1571e(TAG, "readFile e2 :" + e22);
                        }
                    }
                    return res;
                } catch (Throwable th2) {
                    th = th2;
                    if (fin != null) {
                        try {
                            fin.close();
                        } catch (Exception e222) {
                            LogUtils.m1571e(TAG, "readFile e2 :" + e222);
                        }
                    }
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                fin = fin2;
                if (fin != null) {
                    fin.close();
                }
                throw th;
            }
        } catch (Exception e4) {
            e = e4;
            LogUtils.m1572e(TAG, "readFile e:", e);
            if (fin != null) {
                fin.close();
            }
            return res;
        }
        return res;
    }

    private static JSONObject getCongfigJsonObject(String configJson) {
        try {
            return JSON.parseObject(configJson);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void checkAndSaveResult(String url) {
        if (!StringUtils.isEmpty((CharSequence) url)) {
            LogUtils.m1568d(TAG, "checkResult() -> url :" + url);
            ApiFactory.getCommonApi().callSync(url, new C17651(), false, "");
        }
    }

    private String getJsResultStr(String fountionName, String json) {
        return JsExcutor.runScript(json, fountionName, new String[]{getDeviceJsonObject().toJSONString()}, AppRuntimeEnv.get().getApplicationContext());
    }

    private JSONObject getDeviceJsonObject() {
        JSONObject jo = new JSONObject();
        jo.put(KEY_HARDWARE, DeviceUtils.getCpuInfo());
        jo.put(KEY_MODEL, Build.MODEL.replace(" ", "-"));
        jo.put(KEY_PRODUCT, Build.PRODUCT);
        jo.put("memory", Integer.valueOf(AppRuntimeEnv.get().getTotalMemory()));
        jo.put("uuid", Project.getInstance().getBuild().getVrsUUID());
        jo.put(KEY_ANDROIDVERISON, VERSION.SDK);
        jo.put(KEY_APKVERSION, AppClientUtils.getClientVersion());
        LogUtils.m1568d(TAG, "getDeviceJsonObject jo :" + jo.toJSONString());
        return jo;
    }
}
