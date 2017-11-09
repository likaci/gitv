package com.gala.sdk.plugin.server.upgrade;

import android.content.Context;
import com.gala.sdk.plugin.HostPluginInfo;
import com.gala.sdk.plugin.Log;
import com.gala.sdk.plugin.server.utils.PluginDebugUtils;
import com.gala.sdk.plugin.server.utils.PluginDebugUtils.DEBUG_PROPERTY;
import com.gala.sdk.plugin.server.utils.Util;
import com.gala.tvapi.tv2.TVApi;
import com.gala.tvapi.tv2.model.ModuleUpdate;
import com.gala.tvapi.tv2.result.ApiResultModuleUpdate;
import com.gala.video.api.ApiException;
import com.gala.video.api.IApiCallback;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONObject;

public class UpgradeManager {
    private static final String TAG = "UpgradeManager";
    private Context mContext;

    private static class MyListener implements IApiCallback<ApiResultModuleUpdate> {
        private ApiException mException = null;
        private final Map<String, String> mRequestInfos = new HashMap();
        private final Map<String, UpgradeInfo> mUpgradeInfoMap = new HashMap();

        public MyListener(Map<String, String> requestInfos) {
            this.mRequestInfos.putAll(requestInfos);
        }

        public void onSuccess(ApiResultModuleUpdate resultModuleUpdate) {
            if (Log.DEBUG) {
                Log.d(UpgradeManager.TAG, "moduleUpdate() onSuccess!! upgateResult=" + resultModuleUpdate);
            }
            if (resultModuleUpdate != null && resultModuleUpdate.isSuccessfull()) {
                List<ModuleUpdate> modules = resultModuleUpdate.getData();
                if (modules != null && !modules.isEmpty()) {
                    for (ModuleUpdate moduleUpdate : modules) {
                        if (moduleUpdate != null) {
                            String pluginId = moduleUpdate.key;
                            if (!Util.isEmpty(pluginId) && this.mRequestInfos.containsKey(pluginId)) {
                                UpgradeInfo upgradeInfo = new UpgradeInfo();
                                upgradeInfo.setType(moduleUpdate.upType);
                                upgradeInfo.setUrl(moduleUpdate.url);
                                upgradeInfo.setVersion(moduleUpdate.version);
                                upgradeInfo.setTip(moduleUpdate.tip);
                                upgradeInfo.setMd5(moduleUpdate.md5);
                                putValue(pluginId, upgradeInfo);
                            }
                        }
                    }
                }
            }
        }

        public void onException(ApiException exception) {
            if (Log.DEBUG) {
                Log.d(UpgradeManager.TAG, "moduleUpdate() onException!! exception=" + exception);
            }
            this.mException = exception;
        }

        private void putValue(String pluginId, UpgradeInfo info) {
            synchronized (this.mUpgradeInfoMap) {
                this.mUpgradeInfoMap.put(pluginId, info);
            }
        }

        public Map<String, UpgradeInfo> getResult() {
            Map<String, UpgradeInfo> result = new HashMap();
            synchronized (this.mUpgradeInfoMap) {
                result.putAll(this.mUpgradeInfoMap);
            }
            return result;
        }

        public ApiException getException() {
            return this.mException;
        }
    }

    public UpgradeManager(Context context) {
        if (Log.VERBOSE) {
            Log.v(TAG, "UpgradeManager(context=" + context + ")");
        }
        this.mContext = context;
    }

    public UpgradeInfo checkUpgrade(String version, HostPluginInfo hPluginInfo) throws Exception {
        if (Log.VERBOSE) {
            Log.v(TAG, "checkUpgrade<<(hPluginInfo=" + hPluginInfo + ", version=" + version + ")");
        }
        UpgradeInfo info = null;
        if (!Util.isEmpty(hPluginInfo.getPluginId())) {
            Map<String, String> requestInfos = new HashMap();
            if (Util.isEmpty(version)) {
                requestInfos.put(hPluginInfo.getPluginId(), hPluginInfo.getPluginVersion());
            } else {
                requestInfos.put(hPluginInfo.getPluginId(), version);
            }
            info = (UpgradeInfo) checkUpgrades(requestInfos).get(hPluginInfo.getPluginId());
        }
        if (Log.VERBOSE) {
            Log.v(TAG, "checkUpgrade>>() return " + info);
        }
        return info;
    }

    public Map<String, UpgradeInfo> checkUpgrades(Map<String, String> requestInfos) throws ApiException {
        if (Log.VERBOSE) {
            Log.v(TAG, "checkUpgrades<<(requestInfos=" + requestInfos + ")");
        }
        Map<String, UpgradeInfo> result = new HashMap();
        if (requestInfos != null && requestInfos.size() > 0) {
            JSONObject jsonObject = new JSONObject();
            for (String pluginId : requestInfos.keySet()) {
                Util.putJson(jsonObject, pluginId, (String) requestInfos.get(pluginId));
            }
            MyListener listener = new MyListener(requestInfos);
            TVApi.moduleUpdate.callSync(listener, jsonObject.toString());
            if (listener.getException() != null) {
                throw listener.getException();
            }
            result.putAll(listener.getResult());
            if (PluginDebugUtils.needThrowable(DEBUG_PROPERTY.CHECK_UPGRADE_FAILED)) {
                throw new ApiException("checkUpgrade failed!!(for debug!)");
            }
        }
        if (Log.VERBOSE) {
            Log.v(TAG, "checkUpgrades>>() return " + result);
        }
        return result;
    }

    private Map<String, UpgradeInfo> testUpgrade(Map<String, String> requestInfos) {
        Map<String, UpgradeInfo> result = new HashMap();
        for (String pluginId : requestInfos.keySet()) {
            String currentVersion = (String) requestInfos.get(pluginId);
            UpgradeInfo info = new UpgradeInfo();
            info.setType(1);
            info.setVersion(currentVersion + "1");
            info.setTip("测试");
            info.setMd5("");
            result.put(pluginId, info);
        }
        return result;
    }
}
