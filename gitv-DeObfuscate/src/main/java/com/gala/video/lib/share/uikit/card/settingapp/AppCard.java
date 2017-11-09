package com.gala.video.lib.share.uikit.card.settingapp;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import com.gala.appmanager.GalaAppManager;
import com.gala.appmanager.appinfo.AppInfo;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.utils.BitmapUtils;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.uikit.action.data.AppActionData;
import com.gala.video.lib.share.uikit.action.model.ApplicationActionModel;
import com.gala.video.lib.share.uikit.data.CardInfoModel;
import com.gala.video.lib.share.uikit.data.data.processor.CardInfoBuildTool;
import com.gala.video.lib.share.uikit.item.AppItem;
import com.gala.video.lib.share.uikit.item.Item;
import com.gala.video.lib.share.uikit.loader.data.AppRequest;
import com.gala.video.lib.share.uikit.loader.data.AppStore;
import com.gala.video.lib.share.utils.ResourceUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class AppCard implements ISettingApp, Observer {
    private static final String LOG_TAG = "AppCard";
    private List<AppInfo> mAppInfoList;
    private GalaAppManager mAppManager;
    private int mAppType;
    private boolean mIsContainsLocalApp = false;
    private SettingAppCard mSettingAppCard;

    public AppCard(SettingAppCard settingAppCard) {
        this.mSettingAppCard = settingAppCard;
        this.mAppType = AppRequest.checkApp();
        this.mIsContainsLocalApp = isContainsLocalApp();
        unRegisterListenerForLocalApp();
        registerListenerForLocalApp();
    }

    private void registerListenerForLocalApp() {
        if (this.mIsContainsLocalApp) {
            if (this.mAppManager == null) {
                this.mAppManager = GalaAppManager.getInstance(AppRuntimeEnv.get().getApplicationContext());
            }
            this.mAppManager.addObserver(this);
            this.mAppManager.registerReceiver();
        }
    }

    private void unRegisterListenerForLocalApp() {
        if (this.mIsContainsLocalApp && this.mAppManager != null) {
            this.mAppManager.deleteObserver(this);
            this.mAppManager.unregisterReceiver();
        }
    }

    public void onDestory() {
        unRegisterListenerForLocalApp();
    }

    public void setModel(CardInfoModel model) {
        if (this.mIsContainsLocalApp) {
            this.mAppInfoList = this.mAppManager.getAllApps();
            setItemsIcon();
        }
    }

    private void setItemsIcon() {
        List items = this.mSettingAppCard.getItems();
        int appInfoCount = ListUtils.getCount(this.mAppInfoList);
        int itemsCount = ListUtils.getCount(items);
        LogUtils.m1568d(LOG_TAG, "local app size = " + appInfoCount);
        int appInfoIndex = 0;
        for (int itemIndex = 0; itemIndex < itemsCount && appInfoIndex < appInfoCount; itemIndex++) {
            Item item = (Item) items.get(itemIndex);
            if (isLocalAppItem(item)) {
                Drawable drawable = clipSquareDrawable(((AppInfo) this.mAppInfoList.get(appInfoIndex)).getAppIcon());
                ((AppInfo) this.mAppInfoList.get(appInfoIndex)).setAppIcon(drawable);
                ((AppItem) item).setIconDrawable(drawable);
                appInfoIndex++;
            }
        }
    }

    private Drawable clipSquareDrawable(Drawable drawable) {
        if (drawable == null) {
            LogUtils.m1571e(LOG_TAG, "clipSquareDrawable drawable = null");
            return null;
        }
        if (Math.abs(drawable.getIntrinsicHeight() - drawable.getIntrinsicWidth()) >= 5 && (drawable instanceof BitmapDrawable)) {
            Bitmap newBitmap = BitmapUtils.clipSquareImage(drawable);
            if (newBitmap != null) {
                drawable = new BitmapDrawable(ResourceUtil.getResource(), newBitmap);
            }
        }
        return drawable;
    }

    private boolean isContainsLocalApp() {
        return this.mAppType == 1 || this.mAppType == 3;
    }

    private boolean isLocalAppItem(Item item) {
        return getAppActionData(item).getApplicationType() == 1;
    }

    public void update(Observable o, Object arg) {
        LogUtils.m1576i(LOG_TAG, "local app update mIsContainsLocalApp = ", Boolean.valueOf(this.mIsContainsLocalApp));
        if (this.mIsContainsLocalApp) {
            int i;
            AppStore appStore;
            int itemsCount = ListUtils.getCount(this.mSettingAppCard.getItems());
            List<AppStore> applist = new ArrayList(12);
            for (i = 0; i < itemsCount; i++) {
                Item item = (Item) this.mSettingAppCard.getItems().get(i);
                if (isLocalAppItem(item)) {
                    break;
                }
                appStore = new AppStore();
                AppActionData appActionData = getAppActionData(item);
                appStore.app_image_url = item.getModel().getCuteViewData("ID_IMAGE", "value");
                appStore.app_name = item.getModel().getCuteViewData("ID_TITLE", "text");
                appStore.app_type = appActionData.getApplicationType();
                appStore.app_download_url = appActionData.getAppDownloadUrl();
                appStore.app_package_name = appActionData.getAppPackageName();
                applist.add(appStore);
            }
            this.mAppInfoList = this.mAppManager.getAllApps();
            int localAppCount = ListUtils.getCount(this.mAppInfoList);
            for (i = 0; i < localAppCount; i++) {
                AppInfo appInfo = (AppInfo) this.mAppInfoList.get(i);
                appStore = new AppStore();
                appStore.app_type = 1;
                appStore.app_name = appInfo.getAppName();
                appStore.app_download_url = appInfo.getApkAbsolutePath();
                appStore.app_package_name = appInfo.getAppPackageName();
                applist.add(appStore);
            }
            CardInfoBuildTool.buildAppCard(this.mSettingAppCard.getModel(), applist);
            this.mSettingAppCard.notifyDataSetChanged();
        }
    }

    private AppActionData getAppActionData(Item item) {
        return ((ApplicationActionModel) item.getModel().getActionModel()).getData();
    }
}
