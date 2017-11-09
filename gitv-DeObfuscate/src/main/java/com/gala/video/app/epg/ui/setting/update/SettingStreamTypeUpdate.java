package com.gala.video.app.epg.ui.setting.update;

import android.content.Context;
import com.gala.video.app.epg.ui.setting.model.SettingItem;
import com.gala.video.app.epg.ui.setting.model.SettingModel;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.pingback.PingBackParams.Values;
import com.gala.video.lib.share.project.Project;
import com.gala.video.lib.share.system.preference.setting.SettingPlayPreference;
import java.util.ArrayList;
import java.util.List;

public class SettingStreamTypeUpdate extends BaseSettingUpdate {
    private static final String LOG_TAG = "EPG/setting/SettingStreamTypeUpdate";
    private static final String SETTING_ITEM_BOTTOM = "setting_item_bottom";
    private static final String[] STREAMTYPE_OPTIONS = new String[]{"高清", "720P", "杜比 720P", "1080P", "杜比 1080P", "4K", "杜比 4K", "流畅"};

    public SettingModel updateSettingModel(SettingModel model) {
        List<SettingItem> items = model.getItems();
        List<SettingItem> filterItems = new ArrayList();
        List<String> streamTypes = new ArrayList();
        String selectedType = getStreamType(AppRuntimeEnv.get().getApplicationContext());
        for (SettingItem curItem : items) {
            if (Project.getInstance().getConfig().isEnableDolby() || !curItem.getItemName().contains("杜比")) {
                filterItems.add(curItem);
            }
        }
        if (!ListUtils.isEmpty((List) filterItems)) {
            ((SettingItem) filterItems.get(filterItems.size() - 1)).setItemBackground("setting_item_bottom");
        }
        for (SettingItem item : filterItems) {
            streamTypes.add(item.getItemName());
        }
        model.setItems(createItems(streamTypes, selectedType));
        return model;
    }

    public void saveNewCache(String selectedState) {
        types = new String[8][];
        types[0] = new String[]{"高清", "2"};
        types[1] = new String[]{"720P", "4"};
        types[2] = new String[]{"杜比 720P", "14"};
        types[3] = new String[]{"1080P", "5"};
        types[4] = new String[]{"杜比 1080P", "15"};
        types[5] = new String[]{"4K", "10"};
        types[6] = new String[]{"杜比 4K", Values.value16};
        types[7] = new String[]{"流畅", "1"};
        int definition = 2;
        int audiotype = 0;
        if (selectedState.equals(types[0][0])) {
            definition = 2;
        } else if (selectedState.equals(types[1][0])) {
            definition = 4;
        } else if (selectedState.equals(types[2][0])) {
            definition = 4;
            audiotype = 1;
        } else if (selectedState.equals(types[3][0])) {
            definition = 5;
        } else if (selectedState.equals(types[4][0])) {
            definition = 5;
            audiotype = 1;
        } else if (selectedState.equals(types[5][0])) {
            definition = 10;
        } else if (selectedState.equals(types[6][0])) {
            definition = 10;
            audiotype = 1;
        } else if (selectedState.equals(types[7][0])) {
            definition = 1;
        }
        LogUtils.m1576i(LOG_TAG, ">>>>> save Stream Type ", Integer.valueOf(definition));
        LogUtils.m1576i(LOG_TAG, ">>>>> save Audio Type ", Integer.valueOf(audiotype));
        SettingPlayPreference.setStreamType(AppRuntimeEnv.get().getApplicationContext(), definition);
        SettingPlayPreference.setAudioType(AppRuntimeEnv.get().getApplicationContext(), audiotype);
    }

    private String getStreamType(Context context) {
        LogUtils.m1574i(LOG_TAG, ">>>>>getStreamType() --- begin");
        String result = "";
        int definition = SettingPlayPreference.getStreamType(context);
        int audioType = SettingPlayPreference.getAudioType(context);
        if (definition == 2) {
            result = STREAMTYPE_OPTIONS[0];
        } else if (definition == 4 && audioType == 0) {
            result = STREAMTYPE_OPTIONS[1];
        } else if (definition == 4 && audioType == 1) {
            result = STREAMTYPE_OPTIONS[2];
        } else if (definition == 5 && audioType == 0) {
            result = STREAMTYPE_OPTIONS[3];
        } else if (definition == 5 && audioType == 1) {
            result = STREAMTYPE_OPTIONS[4];
        } else if (definition == 10 && audioType == 0) {
            result = STREAMTYPE_OPTIONS[5];
        } else if (definition == 10 && audioType == 0) {
            result = STREAMTYPE_OPTIONS[6];
        } else if (definition == 1) {
            result = STREAMTYPE_OPTIONS[7];
        }
        LogUtils.m1576i(LOG_TAG, ">>>>>getStreamType() ---end--- definition = ", Integer.valueOf(definition), " --- result = ", result);
        return result;
    }
}
