package com.gala.video.app.epg.ui.setting.update;

import com.gala.video.app.epg.ui.setting.model.SettingModel;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.setting.ISetting;
import com.gala.video.lib.share.project.Project;
import java.util.List;

public class SettingResolutionUpdate extends BaseSettingUpdate {
    private static final String LOG_TAG = "EPG/setting/SettingResolutionUpdate";
    private ISetting mISetting;

    public SettingModel updateSettingModel(SettingModel model) {
        if (Project.getInstance().getBuild().isHomeVersion() && Project.getInstance().getConfig().getSystemSetting() != null) {
            List resolutions = Project.getInstance().getConfig().getSystemSetting().getOutputEntries();
            String selectedResolution = Project.getInstance().getConfig().getSystemSetting().getCurrOutput();
            if (!ListUtils.isEmpty(resolutions)) {
                model.setItems(createItems(resolutions, selectedResolution));
            }
        }
        return model;
    }

    public void saveNewCache(String selectedState) {
        this.mISetting = Project.getInstance().getConfig().getSystemSetting();
        if (this.mISetting != null) {
            LogUtils.m1576i(LOG_TAG, "mISetting.setOutputDisplay() ---- ", selectedState);
            this.mISetting.setOutputDisplay(selectedState);
        }
    }
}
