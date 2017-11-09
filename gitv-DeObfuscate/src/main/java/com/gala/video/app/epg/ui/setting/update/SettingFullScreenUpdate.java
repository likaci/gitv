package com.gala.video.app.epg.ui.setting.update;

import com.gala.video.app.epg.ui.setting.model.SettingItem;
import com.gala.video.app.epg.ui.setting.model.SettingModel;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.share.system.preference.setting.SettingPlayPreference;
import java.util.ArrayList;
import java.util.List;

public class SettingFullScreenUpdate extends BaseSettingUpdate {
    public SettingModel updateSettingModel(SettingModel model) {
        String[] SCALE_OPTIONS = new String[]{"原始比例", "强制全屏"};
        List values = new ArrayList();
        String selectedValue = SettingPlayPreference.getStretchPlaybackToFullScreen(AppRuntimeEnv.get().getApplicationContext()) ? SCALE_OPTIONS[1] : SCALE_OPTIONS[0];
        for (SettingItem item : model.getItems()) {
            values.add(item.getItemName());
        }
        if (!ListUtils.isEmpty(values)) {
            model.setItems(createItems(values, selectedValue));
        }
        return model;
    }

    public void saveNewCache(String selectedState) {
        if (new String[]{"原始比例", "强制全屏"}[0].equals(selectedState)) {
            SettingPlayPreference.setStretchPlaybackToFullScreen(AppRuntimeEnv.get().getApplicationContext(), false);
        } else {
            SettingPlayPreference.setStretchPlaybackToFullScreen(AppRuntimeEnv.get().getApplicationContext(), true);
        }
    }
}
