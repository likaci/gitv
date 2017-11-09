package com.gala.video.app.epg.ui.setting.update;

import android.content.Context;
import com.gala.video.app.epg.C0508R;
import com.gala.video.app.epg.ui.setting.SettingConstants;
import com.gala.video.app.epg.ui.setting.model.SettingItem;
import com.gala.video.app.epg.ui.setting.model.SettingModel;
import com.gala.video.app.epg.ui.setting.utils.SettingUtils;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.common.widget.QToast;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.setting.ISetting;
import com.gala.video.lib.share.project.Project;
import com.gala.video.lib.share.system.preference.AppPreference;
import com.gala.video.lib.share.system.preference.setting.SettingPlayPreference;
import com.gala.video.lib.share.system.preference.setting.SettingSharepreference;
import java.util.List;

public class SettingCommonUpdate extends BaseSettingUpdate {
    private static final String LOG_TAG = "EPG/setting/SettingCommonUpdate";
    private static final String[] MESS_DIALOG_OPTIONS = new String[]{"开启", "关闭"};
    public static final String SKYWORTH_MESSAGE_UNREAD_COUNT = "skyworth_message_unread_count";
    public static final String SKYWORTH_SETTING = "skyworth_setting";
    protected String deviceName;
    protected String imailMessageNum;
    private ISetting mISetting = Project.getInstance().getConfig().getSystemSetting();
    protected String weatherState;

    public SettingModel updateSettingModel(SettingModel model) {
        LogUtils.m1574i(LOG_TAG, "model factory --- CommonModel");
        Context context = AppRuntimeEnv.get().getApplicationContext();
        List<SettingItem> items = model.getItems();
        for (SettingItem item : items) {
            int id = item.getId();
            if (!SettingUtils.isCustomId(id)) {
                switch (id) {
                    case 513:
                        if (Project.getInstance().getBuild().isHomeVersion()) {
                            if (Project.getInstance().getConfig().isSkyworthVersion()) {
                                if (this.mISetting == null) {
                                    break;
                                }
                                this.deviceName = this.mISetting.getCurrDeviceName();
                                SettingSharepreference.saveDeviceNameResult(context, this.deviceName);
                                List nameOptions = this.mISetting.getAllDeviceName();
                                if (!(ListUtils.isEmpty(nameOptions) || StringUtils.isEmpty(this.deviceName))) {
                                    item.setItemOptions(nameOptions);
                                    item.setItemLastState(this.deviceName);
                                    break;
                                }
                            }
                            this.deviceName = SettingSharepreference.getDeviceName(context);
                            if (StringUtils.isEmpty(this.deviceName)) {
                                if (!ListUtils.isEmpty(item.getItemOptions())) {
                                    item.setItemLastState((String) item.getItemOptions().get(0));
                                    break;
                                }
                                break;
                            }
                            item.setItemLastState(this.deviceName);
                            break;
                        }
                        this.deviceName = SettingSharepreference.getDeviceName(context);
                        item.setItemLastState(this.deviceName);
                        break;
                    case 515:
                        if (Project.getInstance().getBuild().isHomeVersion()) {
                            if (this.mISetting == null) {
                                break;
                            }
                            item.setItemOptions(this.mISetting.getAllScreenSaveTime());
                            item.setItemLastState(this.mISetting.getCurrScreenSaveTime());
                            break;
                        }
                        item.setItemLastState(SettingSharepreference.getResultScreenSaver(AppRuntimeEnv.get().getApplicationContext()));
                        break;
                    case 516:
                        if (this.mISetting == null) {
                            break;
                        }
                        item.setItemOptions(this.mISetting.getAllDreamTime());
                        item.setItemLastState(this.mISetting.getCurrDreamTime());
                        break;
                    case 517:
                        item.setItemLastState(getMessageNum(context) + " ");
                        break;
                    case 519:
                        if (!Project.getInstance().getBuild().isOpenMessageCenter()) {
                            items.remove(item);
                            break;
                        }
                        String[] se = SettingConstants.MESS_DIALOG_OPTIONS;
                        item.setItemLastState(SettingPlayPreference.getMessDialogOpen(context) ? se[0] : se[1]);
                        break;
                    default:
                        break;
                }
            }
            setItemOptionAndLastState(item);
        }
        model.setItems(items);
        return model;
    }

    private String getMessageNum(Context context) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(LOG_TAG, ">>>>> home launcher ---- getMessageNum");
        }
        String messStr = "";
        AppPreference appPreference = AppPreference.get(context, SKYWORTH_SETTING);
        if (appPreference != null) {
            int messNum = appPreference.getInt(SKYWORTH_MESSAGE_UNREAD_COUNT, 0);
            if (messNum != 0) {
                messStr = messStr + messNum + "条未读";
            }
        } else if (LogUtils.mIsDebug) {
            LogUtils.m1571e(LOG_TAG, ">>>>> home launcher ---- getMessageNum --- AppPreference is null!");
        }
        if (LogUtils.mIsDebug) {
            LogUtils.m1570d(LOG_TAG, ">>>>> home launcher ---- getMessageNum --- result:", messStr);
        }
        return messStr;
    }

    public void reupdateSettingMode(SettingItem item) {
        if (!Project.getInstance().getBuild().isHomeVersion() || item.getId() != 513) {
            return;
        }
        if (this.mISetting == null || !Project.getInstance().getConfig().isSkyworthVersion()) {
            item.setItemOptions(item.getItemOptions());
        } else {
            item.setItemOptions(this.mISetting.getAllDeviceName());
        }
    }

    private void saveDeviceName(Context context, String deviceName) {
        SettingSharepreference.saveDeviceNameResult(context, deviceName);
        if (Project.getInstance().getBuild().isHomeVersion() && Project.getInstance().getConfig().isSkyworthVersion()) {
            Project.getInstance().getConfig().getSystemSetting().setDeviceName(deviceName);
        }
    }

    private void saveScreenSaverTime(Context context, String time) {
        if (Project.getInstance().getBuild().isHomeVersion() && Project.getInstance().getConfig().isSkyworthVersion()) {
            Project.getInstance().getConfig().getSystemSetting().setScreenSaverTime(time);
        } else {
            SettingSharepreference.setResultScreenSaver(context, time);
        }
    }

    private void saveMessDialogIsOpen(Context context, String isOpen) {
        if (MESS_DIALOG_OPTIONS[1].equals(isOpen)) {
            GetInterfaceTools.getMsgCenter().setDiaLogFlag(false);
            SettingPlayPreference.setMessDialogOpen(context, false);
            QToast.makeTextAndShow(AppRuntimeEnv.get().getApplicationContext(), C0508R.string.close_message, 5000);
            return;
        }
        GetInterfaceTools.getMsgCenter().setDiaLogFlag(true);
        SettingPlayPreference.setMessDialogOpen(context, true);
    }

    public String getLastStateByPos(SettingItem item) {
        String lastState = super.getLastStateByPos(item);
        int id = item == null ? -1 : item.getId();
        if (Project.getInstance().getBuild().isHomeVersion() && id == 513) {
            if (Project.getInstance().getConfig().isSkyworthVersion()) {
                return Project.getInstance().getConfig().getSystemSetting().getCurrDeviceName();
            }
            return SettingSharepreference.getDeviceName(AppRuntimeEnv.get().getApplicationContext());
        } else if (Project.getInstance().getBuild().isHomeVersion() && id == 517) {
            return getMessageNum(AppRuntimeEnv.get().getApplicationContext());
        } else {
            return lastState;
        }
    }

    public void saveNewCacheByPos(SettingItem item) {
        Context context = AppRuntimeEnv.get().getApplicationContext();
        if (Project.getInstance().getBuild().isHomeVersion()) {
            this.mISetting = Project.getInstance().getConfig().getSystemSetting();
        }
        int id = item.getId();
        String lastState = item.getItemLastState();
        switch (id) {
            case 513:
                LogUtils.m1576i(LOG_TAG, "saveNewCache() itemId = ", Integer.valueOf(id));
                saveDeviceName(context, lastState);
                return;
            case 515:
                LogUtils.m1576i(LOG_TAG, "saveNewCache() itemId = ", Integer.valueOf(id));
                saveScreenSaverTime(AppRuntimeEnv.get().getApplicationContext(), lastState);
                return;
            case 516:
                LogUtils.m1576i(LOG_TAG, "saveNewCache() itemId = ", Integer.valueOf(id));
                if (Project.getInstance().getBuild().isHomeVersion() && this.mISetting != null) {
                    this.mISetting.setDreamTime(lastState);
                    return;
                }
                return;
            case 519:
                LogUtils.m1576i(LOG_TAG, "saveNewCache() itemId = ", Integer.valueOf(id));
                saveMessDialogIsOpen(AppRuntimeEnv.get().getApplicationContext(), lastState);
                return;
            default:
                super.saveNewCacheByPos(item);
                return;
        }
    }
}
