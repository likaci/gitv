package com.gala.video.app.epg.ui.setting.update;

import com.gala.video.app.epg.ui.setting.model.SettingItem;
import com.gala.video.app.epg.ui.setting.model.SettingModel;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.setting.ISetting;
import com.gala.video.lib.share.project.Project;
import com.gala.video.lib.share.system.preference.setting.SettingSharepreference;
import java.util.ArrayList;
import java.util.List;

public class SettingSaverTimeUpdate extends BaseSettingUpdate {
    private static final String LOG_TAG = "EPG/setting/SettingSaverTimeUpdate";
    private ISetting mISetting;

    public SettingModel updateSettingModel(SettingModel model) {
        List saverTimes;
        String selectedSaverTime;
        if (!Project.getInstance().getBuild().isHomeVersion() || Project.getInstance().getConfig().getSystemSetting() == null) {
            List<SettingItem> items = model.getItems();
            saverTimes = new ArrayList();
            if (!ListUtils.isEmpty((List) items)) {
                for (SettingItem settingItem : items) {
                    LogUtils.m1568d(LOG_TAG, "settingItem.getItemName() = " + settingItem.getItemName());
                    saverTimes.add(settingItem.getItemName());
                }
            }
            selectedSaverTime = SettingSharepreference.getResultScreenSaver(AppRuntimeEnv.get().getApplicationContext());
            LogUtils.m1568d(LOG_TAG, "selectedSaverTime = " + selectedSaverTime);
        } else {
            saverTimes = Project.getInstance().getConfig().getSystemSetting().getAllScreenSaveTime();
            selectedSaverTime = Project.getInstance().getConfig().getSystemSetting().getCurrScreenSaveTime();
        }
        if (!ListUtils.isEmpty(saverTimes)) {
            model.setItems(createItems(saverTimes, selectedSaverTime));
        }
        return model;
    }

    public void saveNewCache(String selectedState) {
        if (!Project.getInstance().getBuild().isHomeVersion() || Project.getInstance().getConfig().getSystemSetting() == null) {
            LogUtils.m1576i(LOG_TAG, "SettingSharepreference.setResultScreenSaver() =  ", selectedState);
            SettingSharepreference.setResultScreenSaver(AppRuntimeEnv.get().getApplicationContext(), selectedState);
            GetInterfaceTools.getIScreenSaver().reStart();
            return;
        }
        this.mISetting = Project.getInstance().getConfig().getSystemSetting();
        if (this.mISetting != null) {
            LogUtils.m1576i(LOG_TAG, "mISetting.setScreenSaverTime() = ", selectedState);
            this.mISetting.setScreenSaverTime(selectedState);
        }
    }
}
