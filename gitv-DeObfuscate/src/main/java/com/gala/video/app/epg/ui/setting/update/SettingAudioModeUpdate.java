package com.gala.video.app.epg.ui.setting.update;

import com.gala.video.app.epg.ui.setting.model.SettingModel;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.setting.ISetting;
import com.gala.video.lib.share.project.Project;
import java.util.List;

public class SettingAudioModeUpdate extends BaseSettingUpdate {
    private static final String LOG_TAG = "EPG/setting/SettingAudioModeUpdate";
    private ISetting mISetting;

    public SettingModel updateSettingModel(SettingModel model) {
        if (Project.getInstance().getBuild().isHomeVersion() && Project.getInstance().getConfig().getSystemSetting() != null) {
            List audioModes = Project.getInstance().getConfig().getSystemSetting().getAudioOutputEntries();
            String selectedValue = Project.getInstance().getConfig().getSystemSetting().getCurrAudioOutputMode();
            if (!ListUtils.isEmpty(audioModes)) {
                model.setItems(createItems(audioModes, selectedValue));
            }
        }
        return model;
    }

    public void saveNewCache(String selectedState) {
        this.mISetting = Project.getInstance().getConfig().getSystemSetting();
        if (this.mISetting != null) {
            LogUtils.m1576i(LOG_TAG, "mISetting.setAudioOutputMode() ---- ", selectedState);
            this.mISetting.setAudioOutputMode(selectedState);
        }
    }
}
