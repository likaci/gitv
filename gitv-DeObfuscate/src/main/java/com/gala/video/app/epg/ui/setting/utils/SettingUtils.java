package com.gala.video.app.epg.ui.setting.utils;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import com.gala.video.app.epg.C0508R;
import com.gala.video.app.epg.home.utils.HomeItemUtils;
import com.gala.video.app.epg.ui.setting.AboutActivity;
import com.gala.video.app.epg.ui.setting.CustomSettingProvider;
import com.gala.video.app.epg.ui.setting.ISettingConstant;
import com.gala.video.app.epg.ui.setting.SettingConstants;
import com.gala.video.app.epg.ui.setting.SettingMainActivity;
import com.gala.video.app.epg.ui.setting.model.SettingItem;
import com.gala.video.app.epg.ui.setting.ui.SettingAboutForLauncherActivity;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.utils.DeviceUtils;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.framework.coreservice.multiscreen.impl.MultiScreen;
import com.gala.video.lib.share.common.widget.GlobalDialog;
import com.gala.video.lib.share.common.widget.QToast;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.ItemDataType;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.WidgetType;
import com.gala.video.lib.share.project.Project;
import com.gala.video.lib.share.utils.PageIOUtils;
import com.gala.video.lib.share.utils.ResourceUtil;
import com.push.mqttv3.internal.ClientDefaults;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import org.apache.http.conn.util.InetAddressUtils;

public class SettingUtils {
    public static final String ABOUT = ResourceUtil.getStr(C0508R.string.setting_about);
    public static final String ACCOUNT_MANAGE = ResourceUtil.getStr(C0508R.string.str_account_manage);
    public static final String ACTION_ABOUT_SETTING = "mipt.gala.settings.action.ABOUT";
    public static final String COMMON = ResourceUtil.getStr(C0508R.string.setting_common);
    private static final String CUSTOM_PARAMS = "custom_params";
    public static final String FEEDBACK = ResourceUtil.getStr(C0508R.string.setting_feedback);
    public static final String HELP = ResourceUtil.getStr(C0508R.string.setting_help);
    private static final String[] IMPLEMENT_KEY = new String[]{SettingConstants.NETSPEED, SettingConstants.NETDIAGNOSE, SettingConstants.WEATHER, SettingConstants.DEFINITION, SettingConstants.SKIPHEADTAIL, SettingConstants.ASPECTRATIO, SettingConstants.BACKGROUND, SettingConstants.HELP, SettingConstants.MULTISCREEN, SettingConstants.ABOUTDEV, SettingConstants.DEVNAME};
    public static final String LOGIN = ResourceUtil.getStr(C0508R.string.setting_my);
    public static final String LOGOUT = ResourceUtil.getStr(C0508R.string.login_mycenter_logout);
    public static final String MULTISCREEN = ResourceUtil.getStr(C0508R.string.setting_multiscreen);
    public static final String NETWORK = ResourceUtil.getStr(C0508R.string.setting_network);
    public static final String PLAY_SHOW = ResourceUtil.getStr(C0508R.string.setting_play_show);
    private static final String PUBLIC_IP = "public_ip";
    private static final int[] SETTING_FOCUS_RESIDS = new int[]{C0508R.drawable.epg_tab_setting_icon_account_focused, C0508R.drawable.epg_tab_setting_icon_net_focused, C0508R.drawable.epg_tab_setting_icon_play_focused, C0508R.drawable.epg_tab_setting_icon_common_focused, C0508R.drawable.epg_tab_setting_icon_tab_manage_focused, C0508R.drawable.epg_tab_setting_icon_update_focused, C0508R.drawable.epg_tab_setting_icon_help_focused, C0508R.drawable.epg_tab_setting_icon_feedback_focused, C0508R.drawable.tab_setting_icon_weixin_focused, C0508R.drawable.epg_tab_setting_icon_multiscreen_focused, C0508R.drawable.epg_tab_setting_icon_about_focused};
    private static final ItemDataType[] SETTING_ITEM_TYPES = new ItemDataType[]{ItemDataType.LOGIN, ItemDataType.NETWORK, ItemDataType.PLAY_PROMPT, ItemDataType.COMMON_SETTING, ItemDataType.TAB_MANAGE, ItemDataType.SYSTEM_UPGRADE, ItemDataType.HELP_CENTER, ItemDataType.FEEDBACK, ItemDataType.CONCERN_WEIXIN, ItemDataType.MULTI_SCREEN, ItemDataType.ABOUT_DEVICE};
    private static final String[] SETTING_NAMES = new String[]{LOGIN, NETWORK, PLAY_SHOW, COMMON, TAB_MANAGE, UPGRADE, HELP, FEEDBACK, WECHAT, MULTISCREEN, ABOUT};
    private static final int[] SETTING_RESIDS = new int[]{C0508R.drawable.share_tab_setting_icon_account, C0508R.drawable.share_tab_setting_icon_net, C0508R.drawable.share_tab_setting_icon_play, C0508R.drawable.share_tab_setting_icon_common, C0508R.drawable.share_tab_setting_icon_tab_manage, C0508R.drawable.share_tab_setting_icon_update, C0508R.drawable.share_tab_setting_icon_help, C0508R.drawable.share_tab_setting_icon_feedback, C0508R.drawable.share_tab_setting_icon_weixin, C0508R.drawable.share_tab_setting_icon_multiscreen, C0508R.drawable.share_tab_setting_icon_about};
    private static final int[] SETTING_TYPES = new int[]{WidgetType.ITEM_SETTING_ACCOUNT, WidgetType.ITEM_SETTING_NETWORK, WidgetType.ITEM_SETTING_DISPLAY, WidgetType.ITEM_SETTING_COMMON, WidgetType.ITEM_SETTING_TAB_MANAGE, WidgetType.ITEM_SETTING_UPGRADE, WidgetType.ITEM_SETTING_HELP, WidgetType.ITEM_SETTING_FEEDBACK, WidgetType.ITEM_SETTING_WEIXIN, WidgetType.ITEM_SETTING_MULTISCREEN, WidgetType.ITEM_SETTING_ABOUT};
    public static final String TAB_MANAGE = ResourceUtil.getStr(C0508R.string.setting_tab_manage);
    public static final String UPGRADE = ResourceUtil.getStr(C0508R.string.setting_upgrade);
    public static final String WECHAT = ResourceUtil.getStr(C0508R.string.setting_wechat);

    public enum SettingType {
        RESID,
        CARDTYPE,
        FOCUSRESID
    }

    public static void startPlaySettingActivity(Context context, Bundle bundle) {
        Intent intent = new Intent(context, SettingMainActivity.class);
        if (bundle == null) {
            bundle = new Bundle();
        }
        bundle.putInt(ISettingConstant.SETTING_FLAG_KEY, 0);
        intent.putExtras(bundle);
        PageIOUtils.activityIn(context, intent);
    }

    public static void startPlaySettingActivityOpenApi(Context context, int flags) {
        Intent intent = new Intent(context, SettingMainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(ISettingConstant.SETTING_FLAG_KEY, 0);
        intent.putExtras(bundle);
        intent.setFlags(flags);
        PageIOUtils.activityIn(context, intent);
    }

    public static void startNetworkSettingActivity(Context context, Bundle bundle) {
        Intent intent = new Intent(context, SettingMainActivity.class);
        if (bundle == null) {
            bundle = new Bundle();
        }
        bundle.putInt(ISettingConstant.SETTING_FLAG_KEY, 1);
        intent.putExtras(bundle);
        PageIOUtils.activityIn(context, intent);
    }

    public static void starFeedbackSettingActivity(Context context, Bundle bundle) {
        Intent intent = new Intent(context, SettingMainActivity.class);
        if (bundle == null) {
            bundle = new Bundle();
        }
        bundle.putInt(ISettingConstant.SETTING_FLAG_KEY, 4);
        intent.putExtras(bundle);
        PageIOUtils.activityIn(context, intent);
    }

    public static void startAboutSettingActivity(Context context) {
        if (!Project.getInstance().getBuild().isHomeVersion()) {
            PageIOUtils.activityIn(context, new Intent(context, AboutActivity.class));
        } else if (Project.getInstance().getConfig().isSkyworthVersion()) {
            Intent intent = new Intent();
            intent.setAction(ACTION_ABOUT_SETTING);
            intent.putExtra("public_ip", AppRuntimeEnv.get().getDeviceIp());
            PageIOUtils.activityIn(context, intent);
        } else {
            PageIOUtils.activityIn(context, new Intent(context, SettingAboutForLauncherActivity.class));
        }
    }

    public static void startCommonSettingActivity(Context context, Bundle bundle) {
        Intent intent = new Intent(context, SettingMainActivity.class);
        if (bundle == null) {
            bundle = new Bundle();
        }
        bundle.putInt(ISettingConstant.SETTING_FLAG_KEY, 2);
        intent.putExtras(bundle);
        PageIOUtils.activityIn(context, intent);
    }

    public static void startTabManageActivity(Context context) {
        HomeItemUtils.onTabSettingClick(context, "");
    }

    public static void startActivityByAction(Context context, String action, int id, String params) {
        if (!StringUtils.isEmpty((CharSequence) action)) {
            try {
                Intent intent = new Intent(action);
                intent.setFlags(ClientDefaults.MAX_MSG_SIZE);
                intent.putExtra(CUSTOM_PARAMS, params);
                if (ACTION_ABOUT_SETTING.equals(action)) {
                    intent.putExtra("public_ip", AppRuntimeEnv.get().getDeviceIp());
                }
                PageIOUtils.activityIn(context, intent);
            } catch (ActivityNotFoundException e) {
                if (isNetWorkSetting(id)) {
                    startSystemSettings(context);
                } else {
                    showNotInstallTip(context);
                }
            }
        } else if (isNetWorkSetting(id)) {
            startSystemSettings(context);
        } else {
            showNotInstallTip(context);
        }
    }

    public static void startAppByPkgClassName(Context context, String packageName, String className, int id, String params) {
        try {
            if (!TextUtils.isEmpty(packageName)) {
                Intent intent;
                if (TextUtils.isEmpty(className)) {
                    intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
                    if (intent != null) {
                        intent.putExtra(CUSTOM_PARAMS, params);
                    } else if (isNetWorkSetting(id)) {
                        startSystemSettings(context);
                        return;
                    } else {
                        showNotInstallTip(context);
                        return;
                    }
                }
                ComponentName componentName = new ComponentName(packageName, className);
                intent = new Intent();
                intent.setFlags(268468224);
                intent.setComponent(componentName);
                intent.setAction("android.intent.action.VIEW");
                intent.putExtra(CUSTOM_PARAMS, params);
                PageIOUtils.activityIn(context, intent);
            } else if (isNetWorkSetting(id)) {
                startSystemSettings(context);
            } else {
                showNotInstallTip(context);
            }
        } catch (ActivityNotFoundException e) {
            if (TextUtils.isEmpty(packageName) || TextUtils.isEmpty(className)) {
                showNotSupportedDialog(context);
            } else {
                showNotInstallTip(context);
            }
        }
    }

    public static void handleExceptionItem(SettingItem item) {
        if (item != null) {
            showNotInstallTip(AppRuntimeEnv.get().getApplicationContext());
        }
    }

    private static void showNotInstallTip(Context context) {
        QToast.makeTextAndShow(context, C0508R.string.setting_not_install, 3000);
    }

    private static void startSystemSettings(Context context) {
        try {
            Intent intent = new Intent(SettingConstants.SYSTEM_SETTINGS_ACTION);
            intent.setFlags(ClientDefaults.MAX_MSG_SIZE);
            PageIOUtils.activityIn(context, intent);
        } catch (ActivityNotFoundException e) {
            showNotInstallTip(context);
        }
    }

    public static boolean isNetWorkSetting(int id) {
        return id == 3;
    }

    protected static void showNotSupportedDialog(Context contxt) {
        final GlobalDialog dialog = Project.getInstance().getControl().getGlobalDialog(contxt);
        dialog.setParams(contxt.getString(C0508R.string.tip_excp_4)).show();
        new Handler().postDelayed(new Runnable() {
            public void run() {
                dialog.dismiss();
            }
        }, 3000);
    }

    public static String getIPV4Addr(Context context) {
        String ip = DeviceUtils.getWirelessIpAddress(context);
        try {
            Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
            while (en.hasMoreElements()) {
                Enumeration<InetAddress> enumIpAddr = ((NetworkInterface) en.nextElement()).getInetAddresses();
                while (enumIpAddr.hasMoreElements()) {
                    InetAddress inetAddress = (InetAddress) enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && InetAddressUtils.isIPv4Address(inetAddress.getHostAddress())) {
                        ip = inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return ip;
    }

    public static void assignIdForCustomItems(List<SettingItem> items, int startId) {
        if (!ListUtils.isEmpty((List) items) && startId >= 0) {
            int size = items.size();
            for (int i = 0; i < size; i++) {
                SettingItem item = (SettingItem) items.get(i);
                int id = item.getId();
                if (!isNetWorkSetting(id) && id != 513) {
                    item.setId(startId + i);
                } else if (isNetWorkSetting(id) && StringUtils.isEmpty(item.getItemAction())) {
                    item.setItemActionType("action");
                    item.setItemAction(SettingConstants.SYSTEM_SETTINGS_ACTION);
                }
            }
        }
    }

    public static boolean isCustomId(int id) {
        return id >= 2048;
    }

    public static void assertCustomItemCount(List<SettingItem> customItems) {
        if (ListUtils.getCount((List) customItems) > 20) {
            int size = 20;
            if (((SettingItem) customItems.get(19)).isGroup()) {
                size = 20 + 1;
            }
            List<SettingItem> tmp = new ArrayList();
            tmp.addAll(customItems);
            customItems.clear();
            for (int i = 0; i < size; i++) {
                customItems.add(tmp.get(i));
            }
        }
    }

    public static void insertCustomItems(List<SettingItem> localItems, List<SettingItem> customItems, boolean insertHead) {
        if (!ListUtils.isEmpty((List) customItems) && localItems != null) {
            if (localItems.size() == 0) {
                localItems.addAll(customItems);
                return;
            }
            int customItemSize = customItems.size();
            boolean firstCustomItemIsGroup = ((SettingItem) customItems.get(0)).isGroup();
            if (customItemSize != 1 || !firstCustomItemIsGroup) {
                if (!insertHead) {
                    List<SettingItem> tmp = new ArrayList();
                    tmp.addAll(localItems);
                    localItems.clear();
                    localItems.addAll(customItems);
                    customItems.clear();
                    customItems.addAll(tmp);
                    tmp.clear();
                }
                firstCustomItemIsGroup = ((SettingItem) customItems.get(0)).isGroup();
                int localItemSize = localItems.size();
                customItemSize = customItems.size();
                if (((SettingItem) localItems.get(0)).isGroup()) {
                    localItems.addAll(0, customItems);
                } else if (firstCustomItemIsGroup) {
                    if (localItemSize == 1) {
                        ((SettingItem) localItems.get(0)).setItemBackground(SettingConstants.SETTING_ITEM_BG_BOTTOM);
                        ((SettingItem) customItems.get(customItemSize - 1)).setItemBackground(SettingConstants.SETTING_ITEM_BG_NORMAL);
                        localItems.addAll(0, customItems);
                        return;
                    }
                    ((SettingItem) localItems.get(0)).setItemBackground(SettingConstants.SETTING_ITEM_BG_NORMAL);
                    ((SettingItem) customItems.get(customItemSize - 1)).setItemBackground(SettingConstants.SETTING_ITEM_BG_NORMAL);
                    localItems.addAll(0, customItems);
                } else if (localItemSize == 1) {
                    ((SettingItem) localItems.get(0)).setItemBackground(SettingConstants.SETTING_ITEM_BG_BOTTOM);
                    if (customItemSize == 1) {
                        ((SettingItem) customItems.get(0)).setItemBackground(SettingConstants.SETTING_ITEM_BG_TOP);
                    } else {
                        ((SettingItem) customItems.get(customItemSize - 1)).setItemBackground(SettingConstants.SETTING_ITEM_BG_NORMAL);
                    }
                    localItems.addAll(0, customItems);
                } else {
                    ((SettingItem) localItems.get(0)).setItemBackground(SettingConstants.SETTING_ITEM_BG_NORMAL);
                    if (customItemSize == 1) {
                        ((SettingItem) customItems.get(0)).setItemBackground(SettingConstants.SETTING_ITEM_BG_TOP);
                    } else {
                        ((SettingItem) customItems.get(customItemSize - 1)).setItemBackground(SettingConstants.SETTING_ITEM_BG_NORMAL);
                    }
                    localItems.addAll(0, customItems);
                }
            }
        }
    }

    public static void insertToGroup(List<SettingItem> localItems, List<SettingItem> customItems, int groupIndex) {
        if (!ListUtils.isEmpty((List) localItems) && !ListUtils.isEmpty((List) customItems)) {
            int groupCount = getGroupCount(localItems);
            if (groupCount != 0 && groupIndex <= groupCount) {
                localItems.addAll((getNextGroupIndex(localItems, groupIndex) + 1) - 1, customItems);
                int c = localItems.size();
                boolean groupStart = false;
                int i = 0;
                while (i < c) {
                    SettingItem item = (SettingItem) localItems.get(i);
                    if (!item.isItemFocusable()) {
                        groupStart = true;
                    } else if (groupStart) {
                        groupStart = false;
                        if (i >= c - 1 || !((SettingItem) localItems.get(i + 1)).isItemFocusable()) {
                            item.setItemBackground(SettingConstants.SETTING_ITEM_BG_CIRCLE);
                        } else {
                            item.setItemBackground(SettingConstants.SETTING_ITEM_BG_TOP);
                        }
                    } else if (i < c - 1 && ((SettingItem) localItems.get(i + 1)).isItemFocusable()) {
                        item.setItemBackground(SettingConstants.SETTING_ITEM_BG_NORMAL);
                    } else if (i == 0) {
                        item.setItemBackground(SettingConstants.SETTING_ITEM_BG_TOP);
                    } else {
                        item.setItemBackground(SettingConstants.SETTING_ITEM_BG_BOTTOM);
                    }
                    i++;
                }
            }
        }
    }

    private static int getGroupCount(List<SettingItem> items) {
        int size = 0;
        if (!ListUtils.isEmpty((List) items)) {
            for (SettingItem item : items) {
                if (!item.isItemFocusable()) {
                    size++;
                }
            }
        }
        return size;
    }

    private static int getNextGroupIndex(List<SettingItem> items, int curGroupIndex) {
        int index = 0;
        int size = ListUtils.getCount((List) items);
        int i = 0;
        while (i < size) {
            if (!((SettingItem) items.get(i)).isItemFocusable()) {
                index++;
                if (index > curGroupIndex) {
                    index = i;
                    break;
                }
            }
            i++;
        }
        if (i == size) {
            return i;
        }
        return index;
    }

    private static int getGroupSize(List<SettingItem> items, int curGroupIndex) {
        int count = ListUtils.getCount((List) items);
        int groupIndex = 0;
        int size = 0;
        for (int i = 0; i < count; i++) {
            if (!((SettingItem) items.get(i)).isItemFocusable()) {
                groupIndex++;
            } else if (groupIndex == curGroupIndex) {
                size++;
            } else if (groupIndex > curGroupIndex) {
                break;
            }
        }
        return size;
    }

    private static int getGroupLastIndex(List<SettingItem> items, int curGroupIndex) {
        int index = -1;
        int size = ListUtils.getCount((List) items);
        int groupIndex = 0;
        for (int i = 0; i < size; i++) {
            if (!((SettingItem) items.get(i)).isItemFocusable()) {
                groupIndex++;
            } else if (groupIndex > curGroupIndex) {
                return i - 1;
            }
            if (i == size - 1) {
                index = i;
            }
        }
        return index;
    }

    public static boolean isImplemented(String key) {
        boolean isImplement = false;
        if (StringUtils.isEmpty((CharSequence) key)) {
            return 0;
        }
        for (Object equals : IMPLEMENT_KEY) {
            if (key.equals(equals)) {
                isImplement = true;
                break;
            }
        }
        return isImplement;
    }

    public static void startUpgradeForLauncher(Context activity) {
        List upgrades = CustomSettingProvider.getInstance().getItems(com.gala.video.app.epg.ui.setting.CustomSettingProvider.SettingType.UPGRADE);
        if (!ListUtils.isEmpty(upgrades) && upgrades.get(0) != null) {
            SettingItem upgrade = (SettingItem) upgrades.get(0);
            CharSequence pkgName = upgrade.getItemPackageName();
            CharSequence action = upgrade.getItemAction();
            String params = upgrade.getItemParams();
            if (!StringUtils.isEmpty(action)) {
                startActivityByAction(activity, action, upgrade.getId(), params);
            } else if (StringUtils.isEmpty(pkgName)) {
                showNotInstallTip(activity);
            } else {
                startAppByPkgClassName(activity, pkgName, upgrade.getItemClassName(), upgrade.getId(), params);
            }
        }
    }

    public static String[] getSettingNames() {
        int size = SETTING_NAMES.length;
        if (Project.getInstance().getBuild().isHomeVersion()) {
            size -= 3;
        }
        if (!MultiScreen.get().isSupportMS()) {
            size--;
        }
        if (size == SETTING_NAMES.length) {
            return SETTING_NAMES;
        }
        String[] names = new String[size];
        int length = SETTING_NAMES.length;
        int index = 0;
        for (int i = 0; i < length; i++) {
            if (!shouldHidden(i) && index < size) {
                names[index] = SETTING_NAMES[i];
                index++;
            }
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
        if (Project.getInstance().getBuild().isHomeVersion()) {
            size -= 3;
        }
        if (!MultiScreen.get().isSupportMS()) {
            size--;
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
            if (shouldHidden(i)) {
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

    public static ItemDataType[] getItemTypes() {
        int size = SETTING_ITEM_TYPES.length;
        if (Project.getInstance().getBuild().isHomeVersion()) {
            size -= 3;
        }
        if (!MultiScreen.get().isSupportMS()) {
            size--;
        }
        if (size == SETTING_NAMES.length) {
            return SETTING_ITEM_TYPES;
        }
        ItemDataType[] names = new ItemDataType[size];
        int length = SETTING_ITEM_TYPES.length;
        int index = 0;
        for (int i = 0; i < length; i++) {
            if (!shouldHidden(i) && index < size) {
                names[index] = SETTING_ITEM_TYPES[i];
                index++;
            }
        }
        return names;
    }

    private static boolean shouldHidden(int index) {
        if (index >= SETTING_NAMES.length) {
            return true;
        }
        if (Project.getInstance().getBuild().isHomeVersion() && (SETTING_NAMES[index].equals(HELP) || SETTING_NAMES[index].equals(FEEDBACK) || SETTING_NAMES[index].equals(WECHAT))) {
            return true;
        }
        if (!SETTING_NAMES[index].equals(MULTISCREEN) || MultiScreen.get().isSupportMS()) {
            return false;
        }
        return true;
    }
}
