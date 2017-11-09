package com.gala.video.lib.share.uikit.data.data.Model;

import com.gala.video.lib.share.C1632R;
import com.gala.video.lib.share.utils.ResourceUtil;

public class SettingConfig {
    private static final String[] IMGS_HOME = new String[]{"share_tab_setting_icon_account", "share_tab_setting_icon_net", "share_tab_setting_icon_play", "share_tab_setting_icon_common", "share_tab_setting_icon_tab_manage", "share_tab_setting_icon_update", "share_tab_setting_icon_help", "share_tab_setting_icon_feedback", "share_tab_setting_icon_weixin", "share_tab_setting_icon_multiscreen", "share_tab_setting_icon_about"};
    private static final String[] IMGS_MINE = new String[]{"share_tab_setting_icon_net", "share_tab_setting_icon_play", "share_tab_setting_icon_common", "share_tab_setting_icon_tab_manage", "share_tab_setting_icon_update", "share_tab_setting_icon_help", "share_tab_setting_icon_feedback", "share_tab_setting_icon_weixin", "share_tab_setting_icon_multiscreen", "share_tab_icon_security_center", "share_tab_setting_icon_about", "share_tab_icon_logout"};
    private static final String[] NAMES_HOME = new String[]{ResourceUtil.getStr(C1632R.string.setting_my), ResourceUtil.getStr(C1632R.string.setting_network), ResourceUtil.getStr(C1632R.string.setting_play_show), ResourceUtil.getStr(C1632R.string.setting_common), ResourceUtil.getStr(C1632R.string.setting_tab_manage), ResourceUtil.getStr(C1632R.string.setting_upgrade), ResourceUtil.getStr(C1632R.string.setting_help), ResourceUtil.getStr(C1632R.string.setting_feedback), ResourceUtil.getStr(C1632R.string.setting_wechat), ResourceUtil.getStr(C1632R.string.setting_multiscreen), ResourceUtil.getStr(C1632R.string.setting_about)};
    private static final String[] NAMES_MINE = new String[]{ResourceUtil.getStr(C1632R.string.setting_network), ResourceUtil.getStr(C1632R.string.setting_play_show), ResourceUtil.getStr(C1632R.string.setting_common), ResourceUtil.getStr(C1632R.string.setting_tab_manage), ResourceUtil.getStr(C1632R.string.setting_upgrade), ResourceUtil.getStr(C1632R.string.setting_help), ResourceUtil.getStr(C1632R.string.setting_feedback), ResourceUtil.getStr(C1632R.string.setting_wechat), ResourceUtil.getStr(C1632R.string.setting_multiscreen), ResourceUtil.getStr(C1632R.string.setting_account_manager), ResourceUtil.getStr(C1632R.string.setting_about), ResourceUtil.getStr(C1632R.string.setting_logout)};
    private static final Integer[] TYPES_HOME = new Integer[]{Integer.valueOf(3), Integer.valueOf(12), Integer.valueOf(14), Integer.valueOf(4), Integer.valueOf(13), Integer.valueOf(1), Integer.valueOf(5), Integer.valueOf(6), Integer.valueOf(8), Integer.valueOf(7), Integer.valueOf(2)};
    private static final Integer[] TYPES_MINE = new Integer[]{Integer.valueOf(12), Integer.valueOf(14), Integer.valueOf(4), Integer.valueOf(13), Integer.valueOf(1), Integer.valueOf(5), Integer.valueOf(6), Integer.valueOf(8), Integer.valueOf(7), Integer.valueOf(9), Integer.valueOf(2), Integer.valueOf(10)};

    public static SettingModel[] getHomeSettingModels() {
        SettingModel[] models = new SettingModel[NAMES_HOME.length];
        for (int i = 0; i < NAMES_HOME.length; i++) {
            try {
                models[i] = getHomeSettingModle(i);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return models;
    }

    public static SettingModel[] getMineSettingModels() {
        SettingModel[] models = new SettingModel[NAMES_MINE.length];
        for (int i = 0; i < NAMES_MINE.length; i++) {
            try {
                models[i] = getMineSettingModle(i);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return models;
    }

    public static SettingModel[] getDefaultSettingModels() {
        return new SettingModel[]{getHomeSettingModle(1), getHomeSettingModle(2), getHomeSettingModle(3), getHomeSettingModle(5), getHomeSettingModle(7), getHomeSettingModle(10)};
    }

    private static SettingModel getHomeSettingModle(int i) {
        SettingModel m = new SettingModel();
        m.name = NAMES_HOME[i];
        m.img = IMGS_HOME[i];
        m.type = TYPES_HOME[i].intValue();
        return m;
    }

    private static SettingModel getMineSettingModle(int i) {
        SettingModel m = new SettingModel();
        m.name = NAMES_MINE[i];
        m.img = IMGS_MINE[i];
        m.type = TYPES_MINE[i].intValue();
        return m;
    }
}
