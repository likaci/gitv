package com.gala.video.app.epg.ui.setting.update;

import android.content.Context;
import com.gala.video.app.epg.ui.setting.SettingConstants;
import com.gala.video.app.epg.ui.setting.model.SettingItem;
import com.gala.video.app.epg.ui.setting.model.SettingModel;
import com.gala.video.app.epg.ui.setting.utils.SettingUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.coreservice.multiscreen.impl.MultiScreen;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.project.Project;
import java.util.List;

public class SettingAboutUpdate extends BaseSettingUpdate {
    private static final String LOG_TAG = "EPG/setting/SettingAboutUpdate";

    public SettingModel updateSettingModel(SettingModel model) {
        List<SettingItem> items = model.getItems();
        for (SettingItem item : items) {
            if (SettingUtils.isCustomId(item.getId())) {
                setItemOptionAndLastState(item);
            } else if (Project.getInstance().getBuild().isHomeVersion() && Project.getInstance().getConfig().isSkyworthVersion() && item.getId() == SettingConstants.ID_ABOUT) {
                item.setItemActionType("activity");
                item.setItemAction(SettingUtils.ACTION_ABOUT_SETTING);
            }
        }
        if (Project.getInstance().getBuild().isHomeVersion()) {
            for (SettingItem item2 : items) {
                if (item2.getId() == SettingConstants.ID_ABOUT) {
                    SettingItem lastItem = (SettingItem) items.get(items.size() - 1);
                    items.remove(item2);
                    lastItem.setItemBackground(SettingConstants.SETTING_ITEM_BG_NORMAL);
                    item2.setItemBackground(SettingConstants.SETTING_ITEM_BG_BOTTOM);
                    items.add(item2);
                    break;
                }
            }
        }
        return model;
    }

    public void saveNewCache(String selectedState) {
    }

    public void startActivityByAction(Context context, String action, int id, String params) {
        if (MultiScreen.get().isSupportMS() && id == SettingConstants.ID_MULTISCREEN) {
            LogUtils.i(LOG_TAG, ">>>>> about - multiscreen -- startActivityByAction");
            GetInterfaceTools.getWebEntry().gotoMultiscreenActivity(context);
            return;
        }
        super.startActivityByAction(context, action, id, params);
    }
}
