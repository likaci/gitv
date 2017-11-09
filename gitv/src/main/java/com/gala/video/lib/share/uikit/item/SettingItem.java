package com.gala.video.lib.share.uikit.item;

import com.gala.video.lib.share.uikit.action.model.SettingsActionModel;
import com.gala.video.lib.share.uikit.contract.SettingItemContract.Presenter;
import com.gala.video.lib.share.uikit.view.SettingItemView;

public class SettingItem extends Item implements Presenter {
    public static final int ABOUT = 2;
    public static final int ACCOUNT = 3;
    public static final int COMMON = 4;
    public static final int DISPLAY = 14;
    public static final int FEEDBACK = 6;
    public static final int HELP = 5;
    public static final int LOGOUT = 10;
    public static final int MULTISCREEN = 7;
    public static final int NETWORK = 12;
    public static final int PAGE = 11;
    public static final int SECURITY = 9;
    public static final int TAB_MANAGE = 13;
    public static final int UPGRADE = 1;
    public static final int WEIXIN = 8;
    private String mLTDes;
    private SettingItemView mView;

    public int getSettingItemType() {
        return ((SettingsActionModel) this.mItemInfoModel.getActionModel()).getSettingsData().getSettinsType();
    }

    public void setItemView(SettingItemView itemView) {
        this.mView = itemView;
    }

    public String getLTDes() {
        return this.mLTDes;
    }

    public void setLTDes(String mLTDes) {
        this.mLTDes = mLTDes;
        if (this.mView != null) {
            this.mView.setLTDes(mLTDes);
        }
    }
}
