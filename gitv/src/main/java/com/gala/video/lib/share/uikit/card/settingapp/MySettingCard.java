package com.gala.video.lib.share.uikit.card.settingapp;

import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.uikit.data.CardInfoModel;
import com.gala.video.lib.share.uikit.item.Item;
import com.gala.video.lib.share.uikit.item.SettingItem;
import java.util.List;

public class MySettingCard extends SettingCard {
    private static final String LOG_TAG = "MySettingCard";
    private boolean mIsFirstSetModel = true;
    private SettingItem mLogoutItem;
    private SettingItem mSecurityItem;

    public MySettingCard(SettingAppCard settingAppCard) {
        super(settingAppCard);
    }

    public void setModel(CardInfoModel model) {
        super.setModel(model);
        if (this.mIsFirstSetModel) {
            this.mSecurityItem = findItemBySettingType(9);
            this.mLogoutItem = findItemBySettingType(10);
            this.mIsFirstSetModel = false;
        }
        if (this.mSecurityItem == null || this.mLogoutItem == null) {
            LogUtils.e(LOG_TAG, "setModel mSecurityItem = ", this.mSecurityItem, " mLogoutItem = ", this.mLogoutItem);
            return;
        }
        boolean hasSecurityItem;
        boolean hasLogoutItem;
        boolean isLogin = GetInterfaceTools.getIGalaAccountManager().isLogin(AppRuntimeEnv.get().getApplicationContext());
        if (findItemBySettingType(9) != null) {
            hasSecurityItem = true;
        } else {
            hasSecurityItem = false;
        }
        if (findItemBySettingType(10) != null) {
            hasLogoutItem = true;
        } else {
            hasLogoutItem = false;
        }
        List<Item> currentItems = this.mSettingAppCard.getItems();
        if (isLogin) {
            if (!hasLogoutItem) {
                currentItems.add(this.mLogoutItem);
            }
            if (!hasSecurityItem) {
                int itemCount = ListUtils.getCount(this.mSettingAppCard.getItems());
                if (itemCount >= 2) {
                    currentItems.add(itemCount - 2, this.mSecurityItem);
                    return;
                }
                return;
            }
            return;
        }
        currentItems.remove(this.mSecurityItem);
        currentItems.remove(this.mLogoutItem);
    }

    public void onDestory() {
        super.onDestory();
        this.mSecurityItem = null;
        this.mLogoutItem = null;
    }
}
