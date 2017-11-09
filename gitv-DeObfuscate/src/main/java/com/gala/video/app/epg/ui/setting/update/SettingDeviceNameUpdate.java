package com.gala.video.app.epg.ui.setting.update;

import com.gala.video.app.epg.ui.setting.CustomSettingProvider;
import com.gala.video.app.epg.ui.setting.model.SettingItem;
import com.gala.video.app.epg.ui.setting.model.SettingModel;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.setting.ISetting;
import com.gala.video.lib.share.project.Project;
import com.gala.video.lib.share.system.preference.setting.SettingSharepreference;
import java.util.ArrayList;
import java.util.List;

public class SettingDeviceNameUpdate extends BaseSettingUpdate {
    private static final String LOG_TAG = "EPG/setting/SettingDeviceNameUpdate";
    private ISetting mISetting;

    public SettingModel updateSettingModel(SettingModel model) {
        List deviceNames;
        String selectedDeviceName;
        List<SettingItem> items;
        if (!Project.getInstance().getBuild().isHomeVersion()) {
            items = model.getItems();
            deviceNames = new ArrayList();
            if (!ListUtils.isEmpty((List) items)) {
                for (SettingItem settingItem : items) {
                    deviceNames.add(settingItem.getItemName());
                }
            }
            selectedDeviceName = SettingSharepreference.getDeviceName(AppRuntimeEnv.get().getApplicationContext());
        } else if (Project.getInstance().getConfig().isSkyworthVersion()) {
            deviceNames = Project.getInstance().getConfig().getSystemSetting().getAllDeviceName();
            selectedDeviceName = Project.getInstance().getConfig().getSystemSetting().getCurrDeviceName();
        } else {
            items = model.getItems();
            deviceNames = new ArrayList();
            if (!ListUtils.isEmpty((List) items)) {
                for (SettingItem settingItem2 : items) {
                    deviceNames.add(settingItem2.getItemName() + CustomSettingProvider.getInstance().getDevNameSuffix());
                }
            }
            selectedDeviceName = SettingSharepreference.getDeviceName(AppRuntimeEnv.get().getApplicationContext());
        }
        if (!ListUtils.isEmpty(deviceNames)) {
            model.setItems(createItems(deviceNames, selectedDeviceName));
        }
        return model;
    }

    public void saveNewCache(String selectedState) {
        if (Project.getInstance().getBuild().isHomeVersion() && Project.getInstance().getConfig().isSkyworthVersion()) {
            this.mISetting = Project.getInstance().getConfig().getSystemSetting();
            if (this.mISetting != null) {
                LogUtils.m1576i(LOG_TAG, "mISetting.setDeviceName() ---- ", selectedState);
                this.mISetting.setDeviceName(selectedState);
                return;
            }
            return;
        }
        LogUtils.m1576i(LOG_TAG, "SettingSharepreference.saveDeviceNameResult() ---- ", selectedState);
        SettingSharepreference.saveDeviceNameResult(AppRuntimeEnv.get().getApplicationContext(), selectedState);
    }
}
