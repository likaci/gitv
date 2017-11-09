package com.gala.video.app.epg.ui.background;

import android.os.Bundle;
import android.view.View;
import com.gala.video.app.epg.R;
import com.gala.video.app.epg.utils.ActivityUtils;
import com.gala.video.lib.framework.core.pingback.PingBack;
import com.gala.video.lib.share.common.activity.QMultiScreenActivity;
import com.gala.video.lib.share.pingback.PingBackParams;
import com.gala.video.lib.share.pingback.PingBackParams.Keys;

public class SettingBGActivity extends QMultiScreenActivity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.epg_activity_bg_setting);
        PingBackParams params = new PingBackParams();
        params.add(Keys.T, "21").add("bstp", "1").add("qtcurl", "wallpaper").add("block", "wallpaper");
        PingBack.getInstance().postPingBackToLongYuan(params.build());
        SettingBGFragment fragment = (SettingBGFragment) getFragmentManager().findFragmentById(R.id.epg_layout_setting_bg_main);
        if (fragment == null) {
            fragment = SettingBGFragment.newInstance();
            ActivityUtils.replaceFragment(getFragmentManager(), fragment, R.id.epg_layout_setting_bg_main);
        }
        SettingBGPresenter settingBGPresenter = new SettingBGPresenter(fragment);
    }

    public View getBackgroundContainer() {
        return findViewById(R.id.epg_layout_setting_bg_main);
    }
}
