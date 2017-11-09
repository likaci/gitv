package com.gala.video.app.epg.ui.ucenter.account.utils;

import com.gala.video.app.epg.R;
import com.gala.video.app.epg.ui.setting.utils.SettingUtils;
import com.gala.video.app.epg.ui.setting.utils.SettingUtils.SettingType;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.ItemDataType;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.WidgetType;

public class CommonFuncUtils {
    private static final int LOGOUT_INDEX = 11;
    private static final int SECURITY_INDEX = 9;
    private static final int[] SETTING_FOCUS_RESIDS = new int[]{R.drawable.epg_tab_setting_icon_net_focused, R.drawable.epg_tab_setting_icon_play_focused, R.drawable.epg_tab_setting_icon_common_focused, R.drawable.epg_tab_setting_icon_tab_manage_focused, R.drawable.epg_tab_setting_icon_update_focused, R.drawable.epg_tab_setting_icon_help_focused, R.drawable.epg_tab_setting_icon_feedback_focused, R.drawable.share_tab_setting_icon_weixin, R.drawable.epg_tab_setting_icon_multiscreen_focused, R.drawable.epg_tab_icon_security_center_focused, R.drawable.epg_tab_setting_icon_about_focused, R.drawable.epg_tab_icon_logout_focused};
    private static final ItemDataType[] SETTING_ITEM_TYPES = new ItemDataType[]{ItemDataType.LOGIN, ItemDataType.NETWORK, ItemDataType.PLAY_PROMPT, ItemDataType.COMMON_SETTING, ItemDataType.TAB_MANAGE, ItemDataType.SYSTEM_UPGRADE, ItemDataType.HELP_CENTER, ItemDataType.FEEDBACK, ItemDataType.CONCERN_WEIXIN, ItemDataType.MULTI_SCREEN, ItemDataType.ABOUT_DEVICE};
    private static final String[] SETTING_NAMES = new String[]{SettingUtils.NETWORK, SettingUtils.PLAY_SHOW, SettingUtils.COMMON, SettingUtils.TAB_MANAGE, SettingUtils.UPGRADE, SettingUtils.HELP, SettingUtils.FEEDBACK, SettingUtils.WECHAT, SettingUtils.MULTISCREEN, SettingUtils.ACCOUNT_MANAGE, SettingUtils.ABOUT, SettingUtils.LOGOUT};
    private static final int[] SETTING_RESIDS = new int[]{R.drawable.share_tab_setting_icon_net, R.drawable.share_tab_setting_icon_play, R.drawable.share_tab_setting_icon_common, R.drawable.share_tab_setting_icon_tab_manage, R.drawable.share_tab_setting_icon_update, R.drawable.share_tab_setting_icon_help, R.drawable.share_tab_setting_icon_feedback, R.drawable.share_tab_setting_icon_weixin, R.drawable.share_tab_setting_icon_multiscreen, R.drawable.share_tab_icon_security_center, R.drawable.share_tab_setting_icon_about, R.drawable.share_tab_icon_logout};
    private static final int[] SETTING_TYPES = new int[]{WidgetType.ITEM_SETTING_NETWORK, WidgetType.ITEM_SETTING_DISPLAY, WidgetType.ITEM_SETTING_COMMON, WidgetType.ITEM_SETTING_TAB_MANAGE, WidgetType.ITEM_SETTING_UPGRADE, WidgetType.ITEM_SETTING_HELP, WidgetType.ITEM_SETTING_FEEDBACK, WidgetType.ITEM_SETTING_WEIXIN, WidgetType.ITEM_SETTING_MULTISCREEN, WidgetType.ITEM_SETTING_SECURITY, WidgetType.ITEM_SETTING_ABOUT, WidgetType.ITEM_SETTING_LOGOUT};

    public static String[] getSettingNames() {
        int size = SETTING_NAMES.length;
        boolean isLogin = GetInterfaceTools.getIGalaAccountManager().isLogin(AppRuntimeEnv.get().getApplicationContext());
        if (!isLogin) {
            size -= 2;
        }
        if (size == SETTING_NAMES.length) {
            return SETTING_NAMES;
        }
        String[] names = new String[size];
        int length = SETTING_NAMES.length;
        int i = 0;
        int index = 0;
        while (i < length) {
            int index2;
            if (shouldHidden(isLogin, i)) {
                index2 = index;
            } else if (index < size) {
                index2 = index + 1;
                names[index] = SETTING_NAMES[i];
            } else {
                index2 = index;
            }
            i++;
            index = index2;
        }
        return names;
    }

    public static int[] getSettingInt(SettingType type) {
        int[] orignal = SETTING_RESIDS;
        switch (type) {
            case CARDTYPE:
                orignal = SETTING_TYPES;
                break;
            case FOCUSRESID:
                orignal = SETTING_FOCUS_RESIDS;
                break;
        }
        int size = orignal.length;
        boolean isLogin = GetInterfaceTools.getIGalaAccountManager().isLogin(AppRuntimeEnv.get().getApplicationContext());
        if (!isLogin) {
            size -= 2;
        }
        if (size == orignal.length) {
            return orignal;
        }
        int length = orignal.length;
        int[] settings = new int[size];
        int i = 0;
        int index = 0;
        while (i < length) {
            int index2;
            if (shouldHidden(isLogin, i)) {
                index2 = index;
            } else if (index < size) {
                index2 = index + 1;
                settings[index] = orignal[i];
            } else {
                index2 = index;
            }
            i++;
            index = index2;
        }
        return settings;
    }

    private static boolean shouldHidden(boolean isLogin, int i) {
        if (isLogin || (9 != i && 11 != i)) {
            return false;
        }
        return true;
    }
}
