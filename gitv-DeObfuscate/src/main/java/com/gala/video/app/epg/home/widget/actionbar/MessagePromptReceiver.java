package com.gala.video.app.epg.home.widget.actionbar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.alibaba.fastjson.JSON;
import com.gala.video.app.epg.ui.imsg.dialog.MsgDialogHelper;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.imsg.IMsgContent;

public class MessagePromptReceiver extends BroadcastReceiver {
    private static final String TAG = "MessagePromptReceiver";

    public void onReceive(Context context, Intent intent) {
        LogUtils.m1568d(TAG, "onReceive, action = " + intent.getAction());
        int msgType = intent.getIntExtra("type", -100);
        String msgContent = intent.getStringExtra("content");
        LogUtils.m1568d(TAG, "message type = " + msgType + " message content = " + msgContent);
        if (msgType == 39) {
            IMsgContent content = null;
            try {
                content = (IMsgContent) JSON.parseObject(msgContent, IMsgContent.class);
            } catch (Exception e) {
                LogUtils.m1578w(TAG, "parse message content occur exception :", e);
            }
            if (content != null) {
                MsgDialogHelper.get().onMessage(content);
                int msgLevel = content.msg_level;
                int msgTemplateId = content.msg_template_id;
                LogUtils.m1568d(TAG, "message level = " + msgLevel + " msg template id = " + msgTemplateId + " page jumping = " + content.page_jumping);
                MessagePromptDispatcher.get().onMessageReceive(content);
            }
        }
    }
}
