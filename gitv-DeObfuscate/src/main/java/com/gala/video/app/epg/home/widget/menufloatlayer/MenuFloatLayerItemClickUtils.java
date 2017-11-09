package com.gala.video.app.epg.home.widget.menufloatlayer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import com.gala.tvapi.tv2.TVApi;
import com.gala.tvapi.tv2.model.ModuleUpdate;
import com.gala.tvapi.tv2.result.ApiResultModuleUpdate;
import com.gala.video.api.ApiException;
import com.gala.video.api.IApiCallback;
import com.gala.video.app.epg.C0508R;
import com.gala.video.app.epg.apkupgrade.UpdateManager;
import com.gala.video.app.epg.home.data.pingback.HomePingbackFactory;
import com.gala.video.app.epg.home.data.pingback.HomePingbackSender;
import com.gala.video.app.epg.home.widget.menufloatlayer.ui.MenuFloatLayerItemView;
import com.gala.video.app.epg.home.widget.menufloatlayer.ui.MenuFloatLayerSettingItemView;
import com.gala.video.app.epg.ui.albumlist.AlbumUtils;
import com.gala.video.app.epg.ui.background.SettingBGActivity;
import com.gala.video.app.epg.ui.search.SearchEnterUtils;
import com.gala.video.app.epg.ui.setting.ConcernWeChatActivity;
import com.gala.video.app.epg.ui.setting.CustomSettingProvider;
import com.gala.video.app.epg.ui.setting.CustomSettingProvider.SettingType;
import com.gala.video.app.epg.ui.setting.utils.SettingUtils;
import com.gala.video.lib.framework.core.network.check.NetWorkManager;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.common.activity.QBaseActivity;
import com.gala.video.lib.share.common.widget.QToast;
import com.gala.video.lib.share.ifmanager.CreateInterfaceTools;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.dynamic.IDynamicResult;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.ItemDataType;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.pingback.HomePingbackType.ClickPingback;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.upgrade.AppVersion;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.upgrade.IUpdateManager.UpdateOperation;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.web.model.WebIntentParams;
import com.gala.video.lib.share.network.NetworkStatePresenter;
import com.gala.video.lib.share.pingback.PingBackCollectionFieldUtils;
import com.gala.video.lib.share.project.Project;
import com.gala.video.lib.share.utils.PageIOUtils;

public class MenuFloatLayerItemClickUtils {
    private static final int DELAY_THREE_SECOND = 3000;
    private static final String MAIN_APK_UPGRADE_KEY = "pri";
    private static final String TAG = "MenuFloatLayerItemClickUtils";
    private static Handler mHandler = new Handler(Looper.getMainLooper());
    private static volatile boolean mIsChecking = false;

    public static void onClick(Context context, MenuFloatLayerItemView itemView) {
        if (itemView != null) {
            HomePingbackFactory.instance().createPingback(ClickPingback.MENU_FLOAT_LAYER_CLICK_PINGBACK).addItem("r", itemView.getText()).addItem("block", "menupanel").addItem("rseat", itemView.getText()).addItem("rpage", "tab_" + HomePingbackSender.getInstance().getTabName()).addItem("rt", "i").setOthersNull().post();
            ItemDataType itemDataType = itemView.getItemType();
            if (itemDataType == null) {
                LogUtils.m1571e(TAG, "onClick, menu float layer item type is null");
                return;
            }
            PingBackCollectionFieldUtils.setIncomeSrc("others");
            switch (itemDataType) {
                case SEARCH:
                    SearchEnterUtils.startSearchActivity(context);
                    return;
                case RECORD:
                    AlbumUtils.startFootPlayhistoryPage(context);
                    return;
                case SETTING:
                    PageIOUtils.activityIn(context, new Intent(context, MenuFloatLayerSettingActivity.class));
                    return;
                case FEEDBACK:
                    SettingUtils.starFeedbackSettingActivity(context, null);
                    return;
                case LOGIN:
                    GetInterfaceTools.getLoginProvider().startUcenterActivityFromSettingLayer(context);
                    return;
                case VIP_ATTRIBUTE:
                    CharSequence url = "";
                    IDynamicResult dynamicResult = GetInterfaceTools.getIDynamicQDataProvider().getDynamicQDataModel();
                    if (dynamicResult != null) {
                        url = dynamicResult.getHomeHeaderVipUrl();
                    }
                    if (StringUtils.isEmpty(url)) {
                        LogUtils.m1577w(TAG, "vip click url is empty");
                        WebIntentParams params2 = new WebIntentParams();
                        params2.pageType = 2;
                        params2.buyFrom = "";
                        GetInterfaceTools.getWebEntry().startPurchasePage((Activity) context, params2);
                        return;
                    }
                    LogUtils.m1568d(TAG, "vip click url = " + url);
                    WebIntentParams params = new WebIntentParams();
                    params.pageUrl = url;
                    params.buyFrom = "";
                    GetInterfaceTools.getWebEntry().gotoCommonWebActivity(context, params);
                    return;
                case BACKGROUND:
                    PageIOUtils.activityIn(context, new Intent(context, SettingBGActivity.class));
                    return;
                case PLAY_PROMPT:
                    SettingUtils.startPlaySettingActivity(context, null);
                    return;
                case NETWORK:
                    SettingUtils.startNetworkSettingActivity(context, null);
                    return;
                case TAB_MANAGE:
                    SettingUtils.startTabManageActivity(context);
                    return;
                default:
                    LogUtils.m1577w(TAG, "onClick, menu float layer item type is illegal");
                    return;
            }
        }
    }

    public static void onSettingItemClick(Context context, MenuFloatLayerSettingItemView itemView) {
        if (itemView != null) {
            HomePingbackFactory.instance().createPingback(ClickPingback.MENU_FLOAT_LAYER_SETTING_PAGE_CLICK_PINGBACK).addItem("r", itemView.getTitle()).addItem("block", "设置").addItem("rseat", itemView.getTitle()).addItem("rpage", "设置").setOthersNull().post();
            ItemDataType itemDataType = itemView.getItemType();
            if (itemDataType == null) {
                LogUtils.m1571e(TAG, "onClick, menu setting item type is null");
                return;
            }
            switch (itemDataType) {
                case FEEDBACK:
                    SettingUtils.starFeedbackSettingActivity(context, null);
                    return;
                case LOGIN:
                    GetInterfaceTools.getLoginProvider().startUcenterActivityFromSetting(context);
                    return;
                case PLAY_PROMPT:
                    SettingUtils.startPlaySettingActivity(context, null);
                    return;
                case NETWORK:
                    SettingUtils.startNetworkSettingActivity(context, null);
                    return;
                case TAB_MANAGE:
                    SettingUtils.startTabManageActivity(context);
                    return;
                case COMMON_SETTING:
                    SettingUtils.startCommonSettingActivity(context, null);
                    return;
                case HELP_CENTER:
                    GetInterfaceTools.getWebEntry().startFaqActivity(context);
                    return;
                case CONCERN_WEIXIN:
                    PageIOUtils.activityIn(context, new Intent(context, ConcernWeChatActivity.class));
                    return;
                case MULTI_SCREEN:
                    GetInterfaceTools.getWebEntry().gotoMultiscreenActivity(context);
                    return;
                case SYSTEM_UPGRADE:
                    onClick(context);
                    return;
                case ABOUT_DEVICE:
                    SettingUtils.startAboutSettingActivity(context);
                    break;
            }
            LogUtils.m1577w(TAG, "onClick, menu setting item type is illegal");
        }
    }

    public static void onClick(Context context) {
        if (Project.getInstance().getBuild().isHomeVersion() && !ListUtils.isEmpty(CustomSettingProvider.getInstance().getItems(SettingType.UPGRADE))) {
            SettingUtils.startUpgradeForLauncher((Activity) context);
        } else if (NetWorkManager.getInstance().getNetState() != 1 && NetWorkManager.getInstance().getNetState() != 2) {
            CreateInterfaceTools.createNetworkProvider().makeDialogAsNetworkError(context, context.getString(C0508R.string.no_network)).show();
        } else if (!mIsChecking) {
            checkApk(context);
        }
    }

    private static void checkApk(final Context context) {
        mIsChecking = true;
        TVApi.moduleUpdate.call(new IApiCallback<ApiResultModuleUpdate>() {

            class C07111 implements Runnable {
                C07111() {
                }

                public void run() {
                    MenuFloatLayerItemClickUtils.mIsChecking = false;
                    if (UpdateManager.getInstance().isShowingDialog()) {
                        if (LogUtils.mIsDebug) {
                            LogUtils.m1568d(MenuFloatLayerItemClickUtils.TAG, "check Apk upgrade dialog showing, do nothing");
                        }
                    } else if (UpdateManager.getInstance().hasUpdate()) {
                        MenuFloatLayerItemClickUtils.showUpdateDialog(context, false);
                    } else {
                        MenuFloatLayerItemClickUtils.showNoUpdateToast(context);
                    }
                }
            }

            public void onSuccess(ApiResultModuleUpdate result) {
                if (result == null || ListUtils.isEmpty(result.data)) {
                    LogUtils.m1568d(MenuFloatLayerItemClickUtils.TAG, "check Apk app upgrade, result is null");
                    MenuFloatLayerItemClickUtils.showNoneedUpdateToast(context);
                    return;
                }
                for (ModuleUpdate module : result.data) {
                    if (MenuFloatLayerItemClickUtils.MAIN_APK_UPGRADE_KEY.equals(module.key)) {
                        AppVersion version = new AppVersion();
                        version.setVersion(module.version);
                        version.setTip(module.tip);
                        version.setUrl(module.url);
                        version.setUpgradeType(module.upType);
                        version.setMd5(module.md5);
                        if (LogUtils.mIsDebug) {
                            LogUtils.m1568d(MenuFloatLayerItemClickUtils.TAG, "check upgrade success version : " + version.toString());
                        }
                        UpdateManager.getInstance().setAppVersion(version);
                        MenuFloatLayerItemClickUtils.mHandler.post(new C07111());
                        return;
                    }
                    LogUtils.m1568d(MenuFloatLayerItemClickUtils.TAG, "check Apk app upgrade, module key is not equal to MAIN_APK_UPGRADE_KEY");
                    MenuFloatLayerItemClickUtils.showNoneedUpdateToast(context);
                }
            }

            public void onException(ApiException exception) {
                if (LogUtils.mIsDebug) {
                    LogUtils.m1569d(MenuFloatLayerItemClickUtils.TAG, "check Apk app upgrade request failed, exception = ", exception);
                }
                MenuFloatLayerItemClickUtils.showNoneedUpdateToast(context);
            }
        }, "{}");
    }

    private static void showNoUpdateToast(Context context) {
        if (context != null) {
            QToast.makeTextAndShow(context, context.getResources().getString(C0508R.string.not_need_update), 3000);
        }
    }

    private static void showNoneedUpdateToast(final Context context) {
        mHandler.post(new Runnable() {
            public void run() {
                MenuFloatLayerItemClickUtils.mIsChecking = false;
                if (NetworkStatePresenter.getInstance().handleNetWork()) {
                    MenuFloatLayerItemClickUtils.showNoUpdateToast(context);
                }
            }
        });
    }

    public static void showUpdateDialog(final Context context, boolean isFetchData) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "show UpdateDialog, isFetchData = " + isFetchData);
        }
        UpdateManager.getInstance().showDialogAndStartDownload(context, true, new UpdateOperation() {
            public void exitApp() {
                if (context != null && (context instanceof QBaseActivity)) {
                    ((QBaseActivity) context).onExitApp();
                }
            }

            public void cancelUpdate() {
            }
        });
    }
}
