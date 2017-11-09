package com.gala.video.app.epg.network;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import com.gala.video.app.epg.R;
import com.gala.video.app.epg.ui.setting.CustomSettingProvider;
import com.gala.video.app.epg.voice.utils.EntryUtils;
import com.gala.video.lib.share.common.widget.GlobalDialog;
import com.gala.video.lib.share.common.widget.QToast;
import com.gala.video.lib.share.ifimpl.interaction.ActionSet;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.setting.INetworkProvider.Wrapper;
import com.gala.video.lib.share.project.Project;
import com.gala.video.lib.share.utils.IntentUtils;
import com.gala.video.lib.share.utils.PageIOUtils;
import com.push.mqttv3.internal.ClientDefaults;

public class NetworkProvider extends Wrapper {
    public String getNetworkAction() {
        return CustomSettingProvider.getInstance().getNetworkAction();
    }

    public GlobalDialog makeDialogAsNetworkError(Context context, String message) {
        String action;
        String module;
        final GlobalDialog dialog = Project.getInstance().getControl().getGlobalDialog(context);
        dialog.getWindow().setType(2003);
        String entryName = null;
        if (message.equals(context.getString(R.string.no_network))) {
            if (Project.getInstance().getBuild().isHomeVersion()) {
                entryName = "网络设置";
            }
            if (Project.getInstance().getControl().isUsingGalaSettingsOutSide()) {
                action = EntryUtils.ACTION_SETTING_NETWORK;
            } else {
                action = Project.getInstance().getControl().getNetWorkSettingAction();
            }
            module = "网络设置";
        } else {
            entryName = "网络诊断";
            action = IntentUtils.getActionName(ActionSet.ACT_NET_DIAGNOSE);
            module = "网络诊断";
        }
        final Context context2 = context;
        dialog.setParams(message, entryName, new OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
                NetworkProvider.this.startSettingModule(context2, action, module);
            }
        });
        dialog.getContentTextView().setTag(Boolean.TRUE);
        return dialog;
    }

    private void startSettingModule(Context context, String action, String module) {
        try {
            Intent intent = new Intent(action);
            intent.setFlags(ClientDefaults.MAX_MSG_SIZE);
            PageIOUtils.activityIn(context, intent);
        } catch (ActivityNotFoundException e) {
            QToast.makeText(context, String.format("无法启动%s功能，可能系统沒有正确安装此功能！", new Object[]{module}), 2000).show();
        }
    }
}
