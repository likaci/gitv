package com.gala.video.app.epg.ui.setting.update;

import android.content.Context;
import com.gala.video.app.epg.ui.setting.model.SettingItem;
import com.gala.video.app.epg.ui.setting.model.SettingModel;

public interface ISettingUpdate {
    String getLastStateByPos(SettingItem settingItem);

    void reupdateSettingMode(SettingItem settingItem);

    void saveNewCache(String str);

    void saveNewCacheByPos(SettingItem settingItem);

    void startActivityByAction(Context context, String str, int i, String str2);

    SettingModel updateSettingModel(SettingModel settingModel);
}
