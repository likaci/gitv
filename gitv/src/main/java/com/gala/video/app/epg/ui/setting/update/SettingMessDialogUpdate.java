package com.gala.video.app.epg.ui.setting.update;

import com.gala.video.app.epg.R;
import com.gala.video.app.epg.ui.setting.model.SettingItem;
import com.gala.video.app.epg.ui.setting.model.SettingModel;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.share.common.widget.QToast;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.system.preference.setting.SettingPlayPreference;
import java.util.ArrayList;
import java.util.List;

public class SettingMessDialogUpdate extends BaseSettingUpdate {
    public SettingModel updateSettingModel(SettingModel model) {
        String[] MESS_DIALOG_OPTIONS = new String[]{"开启", "关闭"};
        List values = new ArrayList();
        String selectedValue = SettingPlayPreference.getMessDialogOpen(AppRuntimeEnv.get().getApplicationContext()) ? MESS_DIALOG_OPTIONS[0] : MESS_DIALOG_OPTIONS[1];
        for (SettingItem item : model.getItems()) {
            values.add(item.getItemName());
        }
        if (!ListUtils.isEmpty(values)) {
            model.setItems(createItems(values, selectedValue));
        }
        return model;
    }

    public void saveNewCache(String selectedState) {
        if (new String[]{"开启", "关闭"}[1].equals(selectedState)) {
            GetInterfaceTools.getMsgCenter().setDiaLogFlag(false);
            SettingPlayPreference.setMessDialogOpen(AppRuntimeEnv.get().getApplicationContext(), false);
            QToast.makeTextAndShow(AppRuntimeEnv.get().getApplicationContext(), R.string.close_message, 5000);
            return;
        }
        GetInterfaceTools.getMsgCenter().setDiaLogFlag(true);
        SettingPlayPreference.setMessDialogOpen(AppRuntimeEnv.get().getApplicationContext(), true);
    }
}
