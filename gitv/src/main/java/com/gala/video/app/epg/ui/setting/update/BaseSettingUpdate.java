package com.gala.video.app.epg.ui.setting.update;

import android.content.Context;
import com.gala.video.app.epg.ui.setting.SettingConstants;
import com.gala.video.app.epg.ui.setting.data.CustomDataInfo;
import com.gala.video.app.epg.ui.setting.data.SettingDataProvider;
import com.gala.video.app.epg.ui.setting.model.SettingItem;
import com.gala.video.app.epg.ui.setting.model.SettingModel;
import com.gala.video.app.epg.ui.setting.utils.SettingUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import java.util.ArrayList;
import java.util.List;

public class BaseSettingUpdate implements ISettingUpdate {
    protected SettingDataProvider mDataProvider = SettingDataProvider.getInstance();

    public SettingModel updateSettingModel(SettingModel model) {
        return model;
    }

    public void reupdateSettingMode(SettingItem item) {
    }

    public void saveNewCache(String selectedState) {
    }

    public void saveNewCacheByPos(SettingItem item) {
        if (SettingUtils.isCustomId(item.getId())) {
            this.mDataProvider.updateCustomItem(item);
        }
    }

    public String getLastStateByPos(SettingItem item) {
        CharSequence ret = "";
        if (item == null) {
            return ret;
        }
        if (SettingUtils.isCustomId(item.getId())) {
            CustomDataInfo info = SettingDataProvider.getInstance().getCustomDataInfo(item);
            ret = info == null ? "" : info.getValue();
        }
        if (StringUtils.isEmpty(ret)) {
            return StringUtils.isEmpty(item.getItemLastState()) ? "" : item.getItemLastState() + " ";
        } else {
            return ret;
        }
    }

    protected List<SettingItem> createItems(List<String> list, String keyValue) {
        List<SettingItem> items = new ArrayList();
        int length = list.size();
        for (int i = 0; i < length; i++) {
            SettingItem item = new SettingItem();
            String curValue = (String) list.get(i);
            item.setItemName((String) list.get(i));
            item.setItemFocusable(true);
            if (curValue.equals(keyValue)) {
                item.setSelected(true);
            }
            if (i == 0) {
                item.setItemBackground(SettingConstants.SETTING_ITEM_BG_TOP);
            } else if (i == list.size() - 1) {
                item.setItemBackground(SettingConstants.SETTING_ITEM_BG_BOTTOM);
            } else {
                item.setItemBackground(SettingConstants.SETTING_ITEM_BG_NORMAL);
            }
            items.add(item);
        }
        return items;
    }

    public void startActivityByAction(Context context, String action, int id, String params) {
        SettingUtils.startActivityByAction(context, action, id, params);
    }

    protected void setItemOptionAndLastState(SettingItem item) {
        if (!SettingUtils.isCustomId(item.getId())) {
            return;
        }
        if (StringUtils.isEmpty(item.getItemKey())) {
            item.setItemOptions(item.getItemOptions());
            item.setItemLastState(item.getItemLastState());
            return;
        }
        CustomDataInfo info = SettingDataProvider.getInstance().getCustomDataInfo(item);
        if (info == null) {
            return;
        }
        if (!StringUtils.isEmpty(info.getItemOption())) {
            item.setItemOptions(SettingDataProvider.getInstance().getOptions(info));
            item.setItemLastState(info.getValue());
        } else if (!StringUtils.isEmpty(info.getValue())) {
            item.setItemLastState(info.getValue());
        }
    }
}
