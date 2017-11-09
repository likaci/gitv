package com.gala.video.lib.share.uikit.contract;

import com.gala.video.lib.share.uikit.view.SettingItemView;

public class SettingItemContract {

    public interface Presenter extends com.gala.video.lib.share.uikit.contract.ItemContract.Presenter {
        String getLTDes();

        void setItemView(SettingItemView settingItemView);

        void setLTDes(String str);
    }
}
