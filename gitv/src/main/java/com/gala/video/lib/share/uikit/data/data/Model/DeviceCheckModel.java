package com.gala.video.lib.share.uikit.data.data.Model;

import android.os.SystemClock;
import com.gala.tvapi.tv2.model.DeviceCheck;
import com.gala.tvapi.tv2.model.ResId;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.SerializableUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.common.configs.DeviceManager;
import com.gala.video.lib.share.common.configs.HomeDataConfig;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.utils.Precondition;
import java.io.IOException;
import java.util.List;

public class DeviceCheckModel extends ApiExceptionModel {
    private static long EXPIRED = 72000000;
    private static final String TAG = "EPG/home/DeviceCheckResult";
    private static final DeviceCheckModel mDeviceCheckResult = new DeviceCheckModel();
    private String mApiKey;
    private DeviceCheck mDevCheck;
    private List<ResId> mHomeResId;
    private String[] mIpLoc = null;
    private long mLastApiKeyTime = 0;

    private DeviceCheckModel() {
    }

    public static DeviceCheckModel getInstance() {
        return mDeviceCheckResult;
    }

    public boolean isDevCheckPass() {
        LogUtils.e(TAG, "isDevCheckPass() ApiKey is " + this.mApiKey);
        return !StringUtils.isEmpty(this.mApiKey);
    }

    public boolean isApiKeyValid() {
        long interval = SystemClock.elapsedRealtime() - this.mLastApiKeyTime;
        LogUtils.e(TAG, "isApiKeyExpired ApiKey is " + this.mApiKey + ",interval = " + interval);
        return !StringUtils.isEmpty(this.mApiKey) && interval < EXPIRED;
    }

    public List<ResId> getHomeResId() {
        return this.mHomeResId;
    }

    public void setHomeResId(List<ResId> homeResId) {
        this.mHomeResId = homeResId;
    }

    public String getApiKey() {
        return this.mApiKey;
    }

    public void clear() {
        super.clear();
        setApiKey(null);
        this.mDevCheck = null;
        this.mHomeResId = null;
        this.mIpLoc = null;
    }

    public void setApiKey(String apiKey) {
        this.mApiKey = apiKey;
        DeviceManager.instance().setState(isDevCheckPass() ? 1 : 2);
    }

    public DeviceCheck getDevCheck() {
        return this.mDevCheck;
    }

    public void setDevCheck(DeviceCheck devCheck) {
        if (!(devCheck == null || StringUtils.isEmpty(devCheck.apiKey))) {
            this.mLastApiKeyTime = SystemClock.elapsedRealtime();
            LogUtils.d(TAG, "set device check result apikey = " + devCheck.apiKey);
            if (!StringUtils.isEmpty(this.mApiKey)) {
                GetInterfaceTools.getPlayerFeatureProxy().updateDeviceCheckInfo(devCheck.apiKey, devCheck.authId);
            }
        }
        this.mDevCheck = devCheck;
    }

    public String[] getIpLoc() {
        if (Precondition.isEmpty(this.mIpLoc)) {
            try {
                return (String[]) SerializableUtils.read(HomeDataConfig.HOME_IP_LOCAL_DIR);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return this.mIpLoc;
    }

    public void setIpLoc(String[] mIpLoc) {
        this.mIpLoc = mIpLoc;
        try {
            SerializableUtils.write(getIpLoc(), HomeDataConfig.HOME_IP_LOCAL_DIR);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
