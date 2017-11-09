package com.gala.video.app.epg.ui.setting.update;

import com.gala.video.app.epg.ui.setting.model.SettingItem;
import com.gala.video.app.epg.ui.setting.model.SettingModel;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.share.system.preference.setting.SettingPlayPreference;
import java.util.ArrayList;
import java.util.List;

public class SettingBeginEndUpdate extends BaseSettingUpdate {
    public SettingModel updateSettingModel(SettingModel model) {
        String[] JUMP_START_END_OPTIONS = new String[]{"开启", "关闭"};
        List values = new ArrayList();
        String selectedValue = SettingPlayPreference.getJumpStartEnd(AppRuntimeEnv.get().getApplicationContext()) ? JUMP_START_END_OPTIONS[0] : JUMP_START_END_OPTIONS[1];
        for (SettingItem item : model.getItems()) {
            values.add(item.getItemName());
        }
        if (!ListUtils.isEmpty(values)) {
            model.setItems(createItems(values, selectedValue));
        }
        return model;
    }

    public void saveNewCache(String selectedState) {
        if (new String[]{"开启", "关闭"}[1].equals(selectedState)) {
            SettingPlayPreference.setJumpStartEnd(AppRuntimeEnv.get().getApplicationContext(), false);
        } else {
            SettingPlayPreference.setJumpStartEnd(AppRuntimeEnv.get().getApplicationContext(), true);
        }
    }
}
