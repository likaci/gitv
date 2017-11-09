package com.gala.video.app.epg.ui.setting.update;

import com.gala.video.app.epg.ui.setting.model.SettingModel;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.setting.ISetting;
import com.gala.video.lib.share.project.Project;
import java.util.List;

public class SettingDRCModeUpdate extends BaseSettingUpdate {
    private static final String LOG_TAG = "EPG/setting/SettingDRCModeUpdate";
    private ISetting mISetting;

    public SettingModel updateSettingModel(SettingModel model) {
        if (Project.getInstance().getBuild().isHomeVersion() && Project.getInstance().getConfig().getSystemSetting() != null) {
            List drcModes = Project.getInstance().getConfig().getSystemSetting().getDRCEntries();
            String selecedDRC = Project.getInstance().getConfig().getSystemSetting().getCurrDRCMode();
            if (!ListUtils.isEmpty(drcModes)) {
                model.setItems(createItems(drcModes, selecedDRC));
            }
        }
        return model;
    }

    public void saveNewCache(String selectedState) {
        this.mISetting = Project.getInstance().getConfig().getSystemSetting();
        if (this.mISetting != null) {
            LogUtils.i(LOG_TAG, "mISetting.setDRCMode() ---- ", selectedState);
            this.mISetting.setDRCMode(selectedState);
        }
    }
}
