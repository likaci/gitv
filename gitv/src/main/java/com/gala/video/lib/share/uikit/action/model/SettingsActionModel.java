package com.gala.video.lib.share.uikit.action.model;

import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.ItemDataType;
import com.gala.video.lib.share.uikit.action.data.SettingsActionData;
import com.gala.video.lib.share.uikit.data.data.Model.SettingModel;

public class SettingsActionModel extends BaseActionModel<SettingModel> {
    private SettingsActionData mSettingsData;

    public SettingsActionModel(ItemDataType itemDataType) {
        super(itemDataType);
    }

    public BaseActionModel buildActionModel(SettingModel settingType) {
        if (this.mSettingsData == null) {
            this.mSettingsData = new SettingsActionData();
        }
        this.mSettingsData.setSettinsType(settingType.type);
        this.mSettingsData.setName(settingType.name);
        return this;
    }

    public SettingsActionData getSettingsData() {
        return this.mSettingsData;
    }
}
