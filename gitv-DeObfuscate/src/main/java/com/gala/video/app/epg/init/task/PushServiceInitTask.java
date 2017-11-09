package com.gala.video.app.epg.init.task;

import android.content.Context;
import com.gala.video.app.epg.ui.imsg.dialog.MsgDialogHelper;
import com.gala.video.lib.share.ifimpl.imsg.utils.IMsgUtils;
import com.gala.video.lib.share.project.Project;
import com.gala.video.lib.share.system.preference.setting.SettingPlayPreference;

public class PushServiceInitTask implements Runnable {
    private Context mContext;

    public PushServiceInitTask(Context context) {
        this.mContext = context;
    }

    public void run() {
        IMsgUtils.getAppId(Project.getInstance().getBuild().getPingbackP2());
        IMsgUtils.sPkgName = this.mContext.getPackageName();
        IMsgUtils.setShowDialog(SettingPlayPreference.getMessDialogOpen(this.mContext));
        MsgDialogHelper.get().setReceiverListener();
        IMsgUtils.init();
        IMsgUtils.getSystem();
    }
}
