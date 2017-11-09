package com.gala.video.lib.share.uikit.card.settingapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifmanager.CreateInterfaceTools;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.databus.IDataBus;
import com.gala.video.lib.share.ifmanager.bussnessIF.databus.IDataBus.MyObserver;
import com.gala.video.lib.share.project.Project;
import com.gala.video.lib.share.system.preference.AppPreference;
import com.gala.video.lib.share.uikit.data.CardInfoModel;
import com.gala.video.lib.share.uikit.item.SettingItem;

public class SettingCard implements ISettingApp {
    private static final String ACTION_SYSTEM_MESSAGE_UNREAD = "com.skyworth.notice.UN_READ";
    private static final String LOG_TAG = "SettingCard";
    private static final int MESSAGE_NUM_0 = 0;
    private static final int MESSAGE_NUM_MAX = 99;
    private static final String SKYWORTH_MESSAGE_UNREAD_COUNT = "skyworth_message_unread_count";
    private static final String SKYWORTH_SETTING = "skyworth_setting";
    private AppPreference mAppPreference;
    private Context mApplicationContext;
    private MyObserver mCheckUpgradeObserver = new MyObserver() {
        public void update(String event) {
            if (LogUtils.mIsDebug) {
                LogUtils.d(SettingCard.LOG_TAG, "receive upgrade event");
            }
            SettingCard.this.findItemBySettingType(1).setLTDes("新");
        }
    };
    private BroadcastReceiver mCommonMessageReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (LogUtils.mIsDebug) {
                LogUtils.d(SettingCard.LOG_TAG, "SystemMessageReceiver");
            }
            if (SettingCard.ACTION_SYSTEM_MESSAGE_UNREAD.equals(intent.getAction())) {
                int mMsgNum = intent.getIntExtra("unRead", 0);
                SettingCard.this.findItemBySettingType(4).setLTDes(SettingCard.this.createMsgNumString(mMsgNum));
                SettingCard.this.mAppPreference.save("skyworth_message_unread_count", mMsgNum);
                if (LogUtils.mIsDebug) {
                    LogUtils.d(SettingCard.LOG_TAG, "SystemMessage unRead Message Num=" + mMsgNum);
                }
            }
        }
    };
    private IDataBus mDataBus;
    private boolean mIsHomeVersion = false;
    protected SettingAppCard mSettingAppCard;

    public SettingCard(SettingAppCard settingAppCard) {
        this.mSettingAppCard = settingAppCard;
        this.mIsHomeVersion = Project.getInstance().getBuild().isHomeVersion();
        this.mDataBus = GetInterfaceTools.getDataBus();
        if (this.mIsHomeVersion) {
            if (LogUtils.mIsDebug) {
                LogUtils.d(LOG_TAG, "Register MessageReceiver");
            }
            IntentFilter filter = new IntentFilter();
            filter.addAction(ACTION_SYSTEM_MESSAGE_UNREAD);
            this.mApplicationContext = AppRuntimeEnv.get().getApplicationContext();
            this.mApplicationContext.registerReceiver(this.mCommonMessageReceiver, filter);
            this.mAppPreference = new AppPreference((Context) this.mSettingAppCard.getServiceManager().getService(Context.class), "skyworth_setting");
        }
        this.mDataBus.registerStickySubscriber(IDataBus.CHECK_UPGRADE_EVENT, this.mCheckUpgradeObserver);
    }

    public void setModel(CardInfoModel model) {
        if (CreateInterfaceTools.createUpdateManager().isNeedShowNewIcon()) {
            findItemBySettingType(1).setLTDes("新");
        }
        if (this.mIsHomeVersion) {
            int msgNum = this.mAppPreference.getInt("skyworth_message_unread_count", 0);
            if (msgNum > 0) {
                findItemBySettingType(4).setLTDes(createMsgNumString(msgNum));
            }
        }
    }

    public void onDestory() {
        if (this.mIsHomeVersion) {
            this.mApplicationContext.unregisterReceiver(this.mCommonMessageReceiver);
        }
        this.mDataBus.unRegisterSubscriber(IDataBus.CHECK_UPGRADE_EVENT, this.mCheckUpgradeObserver);
        this.mDataBus = null;
        this.mAppPreference = null;
        this.mIsHomeVersion = false;
        this.mApplicationContext = null;
    }

    protected SettingItem findItemBySettingType(int settingType) {
        int count = ListUtils.getCount(this.mSettingAppCard.getItems());
        for (int i = 0; i < count; i++) {
            SettingItem item = (SettingItem) this.mSettingAppCard.getItems().get(i);
            if (item.getSettingItemType() == settingType) {
                return item;
            }
        }
        return null;
    }

    private String createMsgNumString(int num) {
        if (num > 99) {
            return "99+";
        }
        if (num > 0) {
            return num + "";
        }
        return "";
    }
}
