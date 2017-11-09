package com.gala.video.app.epg.ui.setting.update;

import com.gala.video.app.epg.ui.setting.model.SettingModel;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.setting.ISetting;
import com.gala.video.lib.share.project.Project;
import java.util.List;

public class SettingDreamTimeUpdate extends BaseSettingUpdate {
    private static final String LOG_TAG = "EPG/setting/SettingDreamTimeUpdate";
    private ISetting mISetting;

    public SettingModel updateSettingModel(SettingModel model) {
        if (Project.getInstance().getBuild().isHomeVersion() && Project.getInstance().getConfig().getSystemSetting() != null) {
            List dreamTimes = Project.getInstance().getConfig().getSystemSetting().getAllDreamTime();
            String selectedDreamTime = Project.getInstance().getConfig().getSystemSetting().getCurrDreamTime();
            if (!ListUtils.isEmpty(dreamTimes)) {
                model.setItems(createItems(dreamTimes, selectedDreamTime));
            }
        }
        return model;
    }

    public void saveNewCache(String selectedState) {
        this.mISetting = Project.getInstance().getConfig().getSystemSetting();
        if (this.mISetting != null) {
            LogUtils.i(LOG_TAG, "mISetting.setDreamTime() ---- ", selectedState);
            this.mISetting.setDreamTime(selectedState);
        }
    }
}
