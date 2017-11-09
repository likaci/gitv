package com.gala.video.app.epg.ui.background;

import android.os.Bundle;
import android.view.View;
import com.gala.video.app.epg.C0508R;
import com.gala.video.app.epg.utils.ActivityUtils;
import com.gala.video.lib.framework.core.pingback.PingBack;
import com.gala.video.lib.share.common.activity.QMultiScreenActivity;
import com.gala.video.lib.share.pingback.PingBackParams;
import com.gala.video.lib.share.pingback.PingBackParams.Keys;

public class SettingBGActivity extends QMultiScreenActivity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0508R.layout.epg_activity_bg_setting);
        PingBackParams params = new PingBackParams();
        params.add(Keys.f2035T, "21").add("bstp", "1").add("qtcurl", "wallpaper").add("block", "wallpaper");
        PingBack.getInstance().postPingBackToLongYuan(params.build());
        SettingBGFragment fragment = (SettingBGFragment) getFragmentManager().findFragmentById(C0508R.id.epg_layout_setting_bg_main);
        if (fragment == null) {
            fragment = SettingBGFragment.newInstance();
            ActivityUtils.replaceFragment(getFragmentManager(), fragment, C0508R.id.epg_layout_setting_bg_main);
        }
        SettingBGPresenter settingBGPresenter = new SettingBGPresenter(fragment);
    }

    public View getBackgroundContainer() {
        return findViewById(C0508R.id.epg_layout_setting_bg_main);
    }
}
