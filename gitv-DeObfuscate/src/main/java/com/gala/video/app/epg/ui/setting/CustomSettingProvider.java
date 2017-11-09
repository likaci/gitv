package com.gala.video.app.epg.ui.setting;

import com.alibaba.fastjson.JSON;
import com.gala.video.app.epg.ui.setting.model.CustomSetting;
import com.gala.video.app.epg.ui.setting.model.CustomSettingItem;
import com.gala.video.app.epg.ui.setting.model.IntentAction;
import com.gala.video.app.epg.ui.setting.model.ItemValue;
import com.gala.video.app.epg.ui.setting.model.SettingItem;
import com.gala.video.app.epg.ui.setting.utils.SettingUtils;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.project.Project;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CustomSettingProvider {
    private static CustomSettingProvider mInstance = null;
    private final String SETTING_JSON_PATH = "/system/etc/gitvsettings/settings.json";
    private final String TAG = "setting/CustomSettingProvider";
    int customStartId = -1;
    private CustomSetting mCustomSetting;

    public enum SettingType {
        NETWORK,
        COMMON,
        UPGRADE,
        PLAY_DISPLAY,
        PLAY,
        DISPLAY,
        ABOUT,
        SIGNAL,
        ABOUTDEV,
        UNKNOWN,
        FEED_BACK
    }

    private CustomSettingProvider() {
        if (Project.getInstance().getBuild().isHomeVersion()) {
            parseSettingJson();
        }
    }

    public static CustomSettingProvider getInstance() {
        if (mInstance == null) {
            mInstance = new CustomSettingProvider();
        }
        return mInstance;
    }

    public List<SettingItem> getItems(SettingType type) {
        List<SettingItem> items = new ArrayList();
        if (this.mCustomSetting == null) {
            return items;
        }
        List<CustomSettingItem> customItems = new ArrayList();
        switch (type) {
            case NETWORK:
                customItems = this.mCustomSetting.network;
                this.customStartId = 2048;
                break;
            case COMMON:
                customItems = this.mCustomSetting.common;
                this.customStartId = SettingConstants.ID_CUSTOM_COMMON_START;
                break;
            case UPGRADE:
                customItems = this.mCustomSetting.upgrade;
                this.customStartId = SettingConstants.ID_CUSTOM_UPGRADE_START;
                break;
            case PLAY:
                customItems = this.mCustomSetting.play;
                this.customStartId = SettingConstants.ID_CUSTOM_PLAY_START;
                break;
            case DISPLAY:
                customItems = this.mCustomSetting.display;
                this.customStartId = SettingConstants.ID_CUSTOM_DISPLAY_START;
                break;
            case ABOUT:
                customItems = this.mCustomSetting.about;
                this.customStartId = 3072;
                break;
            case SIGNAL:
                customItems = this.mCustomSetting.signal;
                this.customStartId = SettingConstants.ID_CUSTOM_SIGNAL_START;
                break;
            case FEED_BACK:
                customItems = this.mCustomSetting.feedback;
                this.customStartId = 3840;
                break;
        }
        return toSettingItems(customItems, this.customStartId);
    }

    public String getDevNameSuffix() {
        if (this.mCustomSetting == null) {
            return "电视";
        }
        if (StringUtils.isEmpty(this.mCustomSetting.suffix)) {
            return "电视";
        }
        return this.mCustomSetting.suffix;
    }

    public List<String> getAboutDev() {
        if (this.mCustomSetting == null) {
            return new ArrayList();
        }
        return this.mCustomSetting.aboutdev;
    }

    public boolean hasSignalSource() {
        if (this.mCustomSetting == null || ListUtils.isEmpty(this.mCustomSetting.signal)) {
            return false;
        }
        return true;
    }

    public boolean hasUpgradeSetting() {
        if (this.mCustomSetting == null || ListUtils.isEmpty(this.mCustomSetting.upgrade)) {
            return false;
        }
        return true;
    }

    public String getNetworkAction() {
        CharSequence action = "";
        if (this.mCustomSetting != null) {
            List<CustomSettingItem> networks = this.mCustomSetting.network;
            if (!ListUtils.isEmpty((List) networks)) {
                for (CustomSettingItem item : networks) {
                    if (item != null && "network".equalsIgnoreCase(item.key)) {
                        if (!(item.value == null || item.value.action == null)) {
                            action = item.value.action.intent;
                        }
                    }
                }
            }
        }
        if (StringUtils.isEmpty(action)) {
            return SettingConstants.SYSTEM_SETTINGS_ACTION;
        }
        return action;
    }

    private void parseSettingJson() {
        Exception e;
        Throwable th;
        ByteArrayOutputStream bos = new ByteArrayOutputStream(1024);
        FileInputStream fileInputStream = null;
        byte[] buffer = new byte[1024];
        try {
            FileInputStream is = new FileInputStream("/system/etc/gitvsettings/settings.json");
            while (true) {
                try {
                    int i = is.read(buffer);
                    if (i <= 0) {
                        break;
                    }
                    bos.write(buffer, 0, i);
                } catch (Exception e2) {
                    e = e2;
                    fileInputStream = is;
                } catch (Throwable th2) {
                    th = th2;
                    fileInputStream = is;
                }
            }
            LogUtils.m1568d("setting/CustomSettingProvider", "bos.toString() =" + bos.toString());
            this.mCustomSetting = (CustomSetting) JSON.parseObject(bos.toString(), CustomSetting.class);
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e3) {
                    LogUtils.m1569d("setting/CustomSettingProvider", "getSettingJson IOException = ", e3);
                    fileInputStream = is;
                    return;
                }
            }
            bos.close();
            fileInputStream = is;
        } catch (Exception e4) {
            e = e4;
            try {
                LogUtils.m1569d("setting/CustomSettingProvider", "getSettingJson Exception =", e);
                if (fileInputStream != null) {
                    try {
                        fileInputStream.close();
                    } catch (IOException e32) {
                        LogUtils.m1569d("setting/CustomSettingProvider", "getSettingJson IOException = ", e32);
                        return;
                    }
                }
                bos.close();
            } catch (Throwable th3) {
                th = th3;
                if (fileInputStream != null) {
                    try {
                        fileInputStream.close();
                    } catch (IOException e322) {
                        LogUtils.m1569d("setting/CustomSettingProvider", "getSettingJson IOException = ", e322);
                        throw th;
                    }
                }
                bos.close();
                throw th;
            }
        }
    }

    private List<SettingItem> toSettingItems(List<CustomSettingItem> customItems, int id) {
        List<SettingItem> items = new ArrayList();
        int size = ListUtils.getCount((List) customItems);
        for (int i = 0; i < size; i++) {
            SettingItem item = new SettingItem();
            CustomSettingItem customItem = (CustomSettingItem) customItems.get(i);
            if (!(customItem == null || SettingUtils.isImplemented(customItem.key))) {
                if ("network".equals(customItem.key)) {
                    item.setId(3);
                } else if (!SettingConstants.DEVNAME.equals(customItem.key)) {
                    item.setId(id + i);
                }
                item.setItemKey(customItem.key);
                ItemValue value = customItem.value;
                if (value != null) {
                    IntentAction action = value.action;
                    if (action != null) {
                        if (!StringUtils.isEmpty(action.intent)) {
                            item.setItemAction(action.intent);
                            item.setItemActionType("action");
                        } else if (!StringUtils.isEmpty(action.pkg)) {
                            item.setItemAction(action.pkg);
                            item.setItemPackageName(action.pkg);
                            item.setItemActionType(SettingConstants.ACTION_TYPE_PACKAGE_NAME);
                        }
                        item.setItemParams(action.params);
                    }
                    item.setItemName(value.caption);
                    item.setItemDes(value.desc);
                }
                item.setItemFocusable(true);
                items.add(item);
            }
        }
        return items;
    }
}
