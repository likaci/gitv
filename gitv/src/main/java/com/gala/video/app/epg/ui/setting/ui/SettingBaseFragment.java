package com.gala.video.app.epg.ui.setting.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import com.gala.video.app.epg.SupportFragment;
import com.gala.video.app.epg.ui.setting.ISettingEvent;
import com.gala.video.app.epg.ui.setting.model.SettingItem;
import com.gala.video.lib.framework.core.utils.LogUtils;

public class SettingBaseFragment extends SupportFragment {
    protected Bundle mBundle;
    protected Context mContext;
    protected ISettingEvent mSettingEvent;

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mContext = activity;
        try {
            this.mSettingEvent = (ISettingEvent) activity;
        } catch (Exception e) {
            throw new IllegalStateException("your activity must implements ISettingEvent  !");
        }
    }

    public void onDetach() {
        super.onDetach();
        LogUtils.e("SettingBaseFragment", "SettingBaseFragment --- onDetach()");
        this.mContext = null;
        this.mSettingEvent = null;
    }

    protected int getDimen(int id) {
        if (this.mContext != null) {
            return (int) this.mContext.getResources().getDimension(id);
        }
        return -1;
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        return false;
    }

    public void updateItem(SettingItem settingItem) {
    }
}
