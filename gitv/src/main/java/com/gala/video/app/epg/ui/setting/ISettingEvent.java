package com.gala.video.app.epg.ui.setting;

import android.os.Bundle;
import com.gala.video.app.epg.ui.setting.model.SettingItem;
import com.gala.video.app.epg.ui.setting.ui.SettingBaseFragment;

public interface ISettingEvent {
    void onAttachActivity(SettingBaseFragment settingBaseFragment);

    void onDetachActivity(SettingBaseFragment settingBaseFragment);

    void onPopStackFragment(SettingItem settingItem);

    void onSwitchFragment(SettingBaseFragment settingBaseFragment, Bundle bundle);
}
