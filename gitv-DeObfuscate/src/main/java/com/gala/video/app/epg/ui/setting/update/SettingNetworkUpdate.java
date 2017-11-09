package com.gala.video.app.epg.ui.setting.update;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import com.gala.video.app.epg.C0508R;
import com.gala.video.app.epg.ui.setting.model.SettingItem;
import com.gala.video.app.epg.ui.setting.model.SettingModel;
import com.gala.video.app.epg.ui.setting.utils.SettingUtils;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.network.check.NetWorkManager;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.system.preference.setting.SettingSharepreference;
import java.util.List;

public class SettingNetworkUpdate extends BaseSettingUpdate {
    private static final String LOG_TAG = "EPG/setting/SettingNetworkUpdate";

    public SettingModel updateSettingModel(SettingModel model) {
        Context context = AppRuntimeEnv.get().getApplicationContext();
        List<SettingItem> items = model.getItems();
        for (SettingItem item : items) {
            int id = item.getId();
            if (!SettingUtils.isCustomId(id)) {
                switch (id) {
                    case 1:
                        item.setItemLastState(getNetSpeed(context));
                        break;
                    case 2:
                        break;
                    case 3:
                        item.setItemLastState(getNetState(context));
                        break;
                    default:
                        break;
                }
            }
            setItemOptionAndLastState(item);
        }
        model.setItems(items);
        return model;
    }

    private String getNetSpeed(Context context) {
        String result = "";
        if (StringUtils.isEmpty(SettingSharepreference.getNetworkSpeedResult(context))) {
            return result;
        }
        return context.getResources().getString(C0508R.string.setting_netspeed, new Object[]{netSpeed}) + " ";
    }

    private String getNetState(Context context) {
        String result = "";
        int netState = NetWorkManager.getInstance().getNetState();
        if (LogUtils.mIsDebug) {
            LogUtils.m1570d(LOG_TAG, "EPG/setting/SettingNetworkUpdategetNetState=", Integer.valueOf(netState));
        }
        switch (netState) {
            case 0:
                return context.getResources().getString(C0508R.string.setting_netstate_none) + " ";
            case 1:
            case 3:
                WifiInfo wifiInfo = ((WifiManager) context.getSystemService("wifi")).getConnectionInfo();
                return context.getResources().getString(C0508R.string.setting_netstate_wifi, new Object[]{wifiInfo.getSSID()}) + " ";
            case 2:
            case 4:
                return context.getResources().getString(C0508R.string.setting_netstate) + " ";
            default:
                return result;
        }
    }

    public String getLastStateByPos(SettingItem item) {
        String lastState = super.getLastStateByPos(item);
        switch (item == null ? -1 : item.getId()) {
            case 1:
                lastState = getNetSpeed(AppRuntimeEnv.get().getApplicationContext());
                break;
            case 3:
                lastState = getNetState(AppRuntimeEnv.get().getApplicationContext());
                break;
        }
        LogUtils.m1570d(LOG_TAG, ">>>>>getLastStateByPos: ", lastState);
        return lastState;
    }
}
