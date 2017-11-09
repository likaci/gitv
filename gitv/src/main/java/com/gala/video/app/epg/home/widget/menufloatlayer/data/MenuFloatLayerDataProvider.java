package com.gala.video.app.epg.home.widget.menufloatlayer.data;

import com.gala.tvapi.type.UserType;
import com.gala.video.app.epg.R;
import com.gala.video.app.epg.home.data.actionbar.ActionBarDataFactory;
import com.gala.video.app.epg.home.data.provider.HomeMenuProvider;
import com.gala.video.app.epg.ui.setting.utils.SettingUtils;
import com.gala.video.app.epg.ui.setting.utils.SettingUtils.SettingType;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.ItemDataType;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.ItemModel;
import com.gala.video.lib.share.project.Project;
import java.util.ArrayList;
import java.util.List;

public class MenuFloatLayerDataProvider {
    private static final String TAG = "home/widget/MenuFloatLayerDataProvider";

    public static String getTitle() {
        String title = HomeMenuProvider.getInstance().getTitle();
        LogUtils.d(TAG, "getTitle, menu float layer title :" + title);
        return title;
    }

    public static List<MenuFloatLayerItemModel> getOnLineData() {
        List<MenuFloatLayerItemModel> resultModelList = new ArrayList();
        List<ItemModel> onLineModels = HomeMenuProvider.getInstance().getDataList();
        LogUtils.d(TAG, "getOnLineData, menu float layer on-line data: " + onLineModels);
        if (!ListUtils.isEmpty((List) onLineModels)) {
            for (ItemModel model : onLineModels) {
                MenuFloatLayerItemModel menuFloatLayerItemModel = new MenuFloatLayerItemModel();
                int iconResId = getIconResIdByItemType(model.getItemType());
                if (iconResId >= 0) {
                    menuFloatLayerItemModel.setIconResId(iconResId);
                    menuFloatLayerItemModel.setItemType(model.getItemType());
                    menuFloatLayerItemModel.setTitle(model.getTitle());
                    if (model.getItemType() == ItemDataType.LOGIN) {
                        String str;
                        if (GetInterfaceTools.getIGalaAccountManager().isLogin(AppRuntimeEnv.get().getApplicationContext())) {
                            str = ActionBarDataFactory.TOP_BAR_TIME_NAME_MY;
                        } else {
                            str = ActionBarDataFactory.TOP_BAR_TIME_NAME_MY;
                        }
                        menuFloatLayerItemModel.setTitle(str);
                    } else if (model.getItemType() == ItemDataType.VIP_ATTRIBUTE) {
                        UserType userType = GetInterfaceTools.getIGalaAccountManager().getUserType();
                        boolean isAccountExpire = userType != null && userType.isExpire();
                        if (GetInterfaceTools.getIGalaAccountManager().isVip() || isAccountExpire) {
                            menuFloatLayerItemModel.setTitle(ActionBarDataFactory.TOP_BAR_TIME_NAME_RENEW_VIP);
                        } else {
                            menuFloatLayerItemModel.setTitle(ActionBarDataFactory.TOP_BAR_TIME_NAME_OPEN_VIP);
                        }
                    }
                    if (model.getItemType() != ItemDataType.FEEDBACK || !Project.getInstance().getBuild().isHomeVersion()) {
                        resultModelList.add(menuFloatLayerItemModel);
                    }
                }
            }
        }
        return resultModelList;
    }

    public static List<MenuFloatLayerItemModel> getLocalDefaultData() {
        List<MenuFloatLayerItemModel> resultModelList = new ArrayList();
        resultModelList.add(new MenuFloatLayerItemModel(ActionBarDataFactory.TOP_BAR_TIME_NAME_SEARCH, ItemDataType.SEARCH, R.drawable.epg_menu_float_layer_search, false));
        resultModelList.add(new MenuFloatLayerItemModel("记录", ItemDataType.RECORD, R.drawable.epg_menu_float_layer_history, false));
        resultModelList.add(new MenuFloatLayerItemModel("设置", ItemDataType.SETTING, R.drawable.epg_menu_float_layer_setting_icon, false));
        resultModelList.add(new MenuFloatLayerItemModel(GetInterfaceTools.getIGalaAccountManager().isLogin(AppRuntimeEnv.get().getApplicationContext()) ? ActionBarDataFactory.TOP_BAR_TIME_NAME_MY : ActionBarDataFactory.TOP_BAR_TIME_NAME_MY, ItemDataType.LOGIN, R.drawable.epg_tab_setting_icon_account_focused, false));
        if (!Project.getInstance().getBuild().isHomeVersion()) {
            resultModelList.add(new MenuFloatLayerItemModel("客服反馈", ItemDataType.FEEDBACK, R.drawable.epg_tab_setting_icon_feedback_focused, false));
        }
        return resultModelList;
    }

    public static List<MenuFloatLayerItemModel> getSettingListData() {
        List<MenuFloatLayerItemModel> resultModelList = new ArrayList();
        String[] names = SettingUtils.getSettingNames();
        int[] resIds = SettingUtils.getSettingInt(SettingType.RESID);
        int[] focusResIds = SettingUtils.getSettingInt(SettingType.FOCUSRESID);
        ItemDataType[] itemDataTypes = SettingUtils.getItemTypes();
        int length = names.length;
        int resLength = resIds.length;
        int focusResLength = focusResIds.length;
        int itemLength = itemDataTypes.length;
        if (length == resLength && length == focusResLength && length == itemLength) {
            if (length > 0) {
                names[0] = GetInterfaceTools.getIGalaAccountManager().isLogin(AppRuntimeEnv.get().getApplicationContext()) ? ActionBarDataFactory.TOP_BAR_TIME_NAME_MY : ActionBarDataFactory.TOP_BAR_TIME_NAME_MY;
            }
            for (int i = 0; i < length; i++) {
                resultModelList.add(new MenuFloatLayerItemModel(names[i], itemDataTypes[i], resIds[i], focusResIds[i], false));
            }
        } else {
            LogUtils.e(TAG, "data do not match! name length=" + length + ",focus res id length=" + focusResLength + ",itemLength=" + itemLength);
        }
        return resultModelList;
    }

    private static int getIconResIdByItemType(ItemDataType itemDataType) {
        if (itemDataType == null) {
            return -1;
        }
        switch (itemDataType) {
            case SEARCH:
                return R.drawable.epg_menu_float_layer_search;
            case RECORD:
                return R.drawable.epg_menu_float_layer_history;
            case SETTING:
                return R.drawable.epg_menu_float_layer_setting_icon;
            case FEEDBACK:
                return R.drawable.epg_tab_setting_icon_feedback_focused;
            case LOGIN:
                return R.drawable.epg_tab_setting_icon_account_focused;
            case VIP_ATTRIBUTE:
                return R.drawable.epg_menu_float_layer_vip;
            case BACKGROUND:
                return R.drawable.epg_tab_setting_icon_play_focused;
            case PLAY_PROMPT:
                return R.drawable.epg_tab_setting_icon_play_focused;
            case NETWORK:
                return R.drawable.epg_tab_setting_icon_net_focused;
            case TAB_MANAGE:
                return R.drawable.epg_tab_setting_icon_tab_manage_focused;
            default:
                LogUtils.d(TAG, "getIconResIdByItemType, menu float layer item type is illegal");
                return -1;
        }
    }
}
