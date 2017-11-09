package com.gala.multiscreen.dmr.logic.listener;

import com.gala.android.dlna.sdk.mediarenderer.GalaDLNAListener;
import com.gala.android.dlna.sdk.mediarenderer.MediaRenderer;

public class CustomListener {
    private GalaDLNAListener mGalaDLNAListener = new GalaDLNAListener() {
        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onReceiveSendMessage(int r21, java.lang.String r22, java.lang.StringBuffer r23) {
            /*
            r20 = this;
            r14 = "fail";
            r17 = com.gala.multiscreen.dmr.util.MSLog.LogType.MS_FROM_PHONE;
            r0 = r22;
            r1 = r17;
            com.gala.multiscreen.dmr.util.MSLog.log(r0, r1);
            r17 = com.gala.multiscreen.dmr.model.msg.MultiScreenMessage.class;
            r0 = r22;
            r1 = r17;
            r8 = com.alibaba.fastjson.JSON.parseObject(r0, r1);	 Catch:{ Exception -> 0x00d3 }
            r8 = (com.gala.multiscreen.dmr.model.msg.MultiScreenMessage) r8;	 Catch:{ Exception -> 0x00d3 }
            r3 = com.gala.multiscreen.dmr.logic.listener.MSCallbacks.getGalaMS();	 Catch:{ Exception -> 0x00d3 }
            r0 = r8.type;	 Catch:{ Exception -> 0x00d3 }
            r17 = r0;
            r16 = com.gala.multiscreen.dmr.model.type.MSType.findType(r17);	 Catch:{ Exception -> 0x00d3 }
            r0 = r8.control;	 Catch:{ Exception -> 0x00d3 }
            r17 = r0;
            r5 = com.gala.multiscreen.dmr.model.type.MSControl.findControl(r17);	 Catch:{ Exception -> 0x00d3 }
            r17 = com.gala.multiscreen.dmr.logic.listener.CustomListener.AnonymousClass2.$SwitchMap$com$gala$multiscreen$dmr$model$type$MSType;	 Catch:{ Exception -> 0x00d3 }
            r18 = r16.ordinal();	 Catch:{ Exception -> 0x00d3 }
            r17 = r17[r18];	 Catch:{ Exception -> 0x00d3 }
            switch(r17) {
                case 1: goto L_0x008f;
                case 2: goto L_0x00ac;
                case 3: goto L_0x00bf;
                case 4: goto L_0x00f3;
                default: goto L_0x0037;
            };	 Catch:{ Exception -> 0x00d3 }
        L_0x0037:
            r17 = com.gala.multiscreen.dmr.logic.listener.CustomListener.AnonymousClass2.$SwitchMap$com$gala$multiscreen$dmr$model$type$MSControl;	 Catch:{ Exception -> 0x00d3 }
            r18 = r5.ordinal();	 Catch:{ Exception -> 0x00d3 }
            r17 = r17[r18];	 Catch:{ Exception -> 0x00d3 }
            switch(r17) {
                case 1: goto L_0x0122;
                case 2: goto L_0x0151;
                case 3: goto L_0x015b;
                default: goto L_0x0042;
            };	 Catch:{ Exception -> 0x00d3 }
        L_0x0042:
            r17 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x00d3 }
            r17.<init>();	 Catch:{ Exception -> 0x00d3 }
            r18 = "MSERROR: UNKNOW MESSAGE-----type = ";
            r17 = r17.append(r18);	 Catch:{ Exception -> 0x00d3 }
            r18 = r16.toString();	 Catch:{ Exception -> 0x00d3 }
            r17 = r17.append(r18);	 Catch:{ Exception -> 0x00d3 }
            r18 = ", control = ";
            r17 = r17.append(r18);	 Catch:{ Exception -> 0x00d3 }
            r18 = r5.toString();	 Catch:{ Exception -> 0x00d3 }
            r17 = r17.append(r18);	 Catch:{ Exception -> 0x00d3 }
            r17 = r17.toString();	 Catch:{ Exception -> 0x00d3 }
            r18 = com.gala.multiscreen.dmr.util.MSLog.LogType.ERROR;	 Catch:{ Exception -> 0x00d3 }
            com.gala.multiscreen.dmr.util.MSLog.log(r17, r18);	 Catch:{ Exception -> 0x00d3 }
        L_0x006e:
            r17 = new java.lang.StringBuilder;
            r17.<init>();
            r18 = "TV reply : ";
            r17 = r17.append(r18);
            r0 = r17;
            r17 = r0.append(r14);
            r17 = r17.toString();
            r18 = com.gala.multiscreen.dmr.util.MSLog.LogType.MS_TO_PHONE;
            com.gala.multiscreen.dmr.util.MSLog.log(r17, r18);
            r0 = r23;
            r0.append(r14);
            return;
        L_0x008f:
            r17 = com.gala.multiscreen.dmr.model.msg.ChangeRes.class;
            r0 = r17;
            r4 = r8.pressValue(r0);	 Catch:{ Exception -> 0x00d3 }
            r4 = (com.gala.multiscreen.dmr.model.msg.ChangeRes) r4;	 Catch:{ Exception -> 0x00d3 }
            r0 = r4.res;	 Catch:{ Exception -> 0x00d3 }
            r17 = r0;
            r0 = r17;
            r17 = r3.onResolutionChanged(r0);	 Catch:{ Exception -> 0x00d3 }
            r7 = java.lang.Boolean.valueOf(r17);	 Catch:{ Exception -> 0x00d3 }
            r14 = com.gala.multiscreen.dmr.util.MsgBuilder.buildResultMsg(r7);	 Catch:{ Exception -> 0x00d3 }
            goto L_0x006e;
        L_0x00ac:
            r17 = com.gala.multiscreen.dmr.SeekCounter.getSeekCounter();	 Catch:{ Exception -> 0x00d3 }
            r17 = r17.isSeeking();	 Catch:{ Exception -> 0x00d3 }
            if (r17 != 0) goto L_0x006e;
        L_0x00b6:
            r12 = r3.getPlayPosition();	 Catch:{ Exception -> 0x00d3 }
            r14 = com.gala.multiscreen.dmr.util.MsgBuilder.buildResultMsg(r12);	 Catch:{ Exception -> 0x00d3 }
            goto L_0x006e;
        L_0x00bf:
            r17 = com.gala.multiscreen.dmr.model.msg.PlayList.class;
            r0 = r17;
            r10 = r8.pressValue(r0);	 Catch:{ Exception -> 0x00d3 }
            r10 = (com.gala.multiscreen.dmr.model.msg.PlayList) r10;	 Catch:{ Exception -> 0x00d3 }
            r0 = r10.playlist;	 Catch:{ Exception -> 0x00d3 }
            r17 = r0;
            r0 = r17;
            r3.onPushPlayList(r0);	 Catch:{ Exception -> 0x00d3 }
            goto L_0x006e;
        L_0x00d3:
            r6 = move-exception;
            r17 = new java.lang.StringBuilder;
            r17.<init>();
            r18 = "onReceiveSendMessage() exception : ";
            r17 = r17.append(r18);
            r18 = r6.getMessage();
            r17 = r17.append(r18);
            r17 = r17.toString();
            r18 = com.gala.multiscreen.dmr.util.MSLog.LogType.ERROR;
            com.gala.multiscreen.dmr.util.MSLog.log(r17, r18);
            goto L_0x006e;
        L_0x00f3:
            r17 = com.gala.multiscreen.dmr.model.msg.Seek.class;
            r0 = r17;
            r15 = r8.pressValue(r0);	 Catch:{ Exception -> 0x00d3 }
            r15 = (com.gala.multiscreen.dmr.model.msg.Seek) r15;	 Catch:{ Exception -> 0x00d3 }
            r0 = r15.time_stamp;	 Catch:{ Exception -> 0x00d3 }
            r18 = r0;
            r0 = r18;
            r17 = r3.onSeekChanged(r0);	 Catch:{ Exception -> 0x00d3 }
            r7 = java.lang.Boolean.valueOf(r17);	 Catch:{ Exception -> 0x00d3 }
            r17 = r7.booleanValue();	 Catch:{ Exception -> 0x00d3 }
            if (r17 == 0) goto L_0x0118;
        L_0x0111:
            r17 = com.gala.multiscreen.dmr.SeekCounter.getSeekCounter();	 Catch:{ Exception -> 0x00d3 }
            r17.doSeek();	 Catch:{ Exception -> 0x00d3 }
        L_0x0118:
            r0 = r15.time_stamp;	 Catch:{ Exception -> 0x00d3 }
            r18 = r0;
            r14 = com.gala.multiscreen.dmr.util.MsgBuilder.buildResultMsg(r18);	 Catch:{ Exception -> 0x00d3 }
            goto L_0x006e;
        L_0x0122:
            r17 = com.gala.multiscreen.dmr.model.msg.MSAction.class;
            r0 = r17;
            r2 = r8.pressValue(r0);	 Catch:{ Exception -> 0x00d3 }
            r2 = (com.gala.multiscreen.dmr.model.msg.MSAction) r2;	 Catch:{ Exception -> 0x00d3 }
            r17 = r2.getAction();	 Catch:{ Exception -> 0x00d3 }
            r18 = com.gala.multiscreen.dmr.model.type.Action.BACK;	 Catch:{ Exception -> 0x00d3 }
            r0 = r17;
            r1 = r18;
            if (r0 != r1) goto L_0x013d;
        L_0x0138:
            r17 = "";
            com.gala.multiscreen.dmr.model.MSMessage.VALUE_KEY = r17;	 Catch:{ Exception -> 0x00d3 }
        L_0x013d:
            r17 = r2.getAction();	 Catch:{ Exception -> 0x00d3 }
            r0 = r17;
            r17 = r3.onActionChanged(r0);	 Catch:{ Exception -> 0x00d3 }
            r7 = java.lang.Boolean.valueOf(r17);	 Catch:{ Exception -> 0x00d3 }
            r14 = com.gala.multiscreen.dmr.util.MsgBuilder.buildResultMsg(r7);	 Catch:{ Exception -> 0x00d3 }
            goto L_0x006e;
        L_0x0151:
            r9 = r3.onPhoneSync();	 Catch:{ Exception -> 0x00d3 }
            r14 = com.gala.multiscreen.dmr.util.MsgBuilder.buildResultMsg(r9);	 Catch:{ Exception -> 0x00d3 }
            goto L_0x006e;
        L_0x015b:
            r17 = com.gala.multiscreen.dmr.model.msg.PushVideo.class;
            r0 = r17;
            r11 = r8.pressValue(r0);	 Catch:{ Exception -> 0x00d3 }
            r11 = (com.gala.multiscreen.dmr.model.msg.PushVideo) r11;	 Catch:{ Exception -> 0x00d3 }
            r0 = r11.session;	 Catch:{ Exception -> 0x00d3 }
            r17 = r0;
            com.gala.multiscreen.dmr.model.MSMessage.VALUE_SESSION = r17;	 Catch:{ Exception -> 0x00d3 }
            r0 = r11.key;	 Catch:{ Exception -> 0x00d3 }
            r17 = r0;
            com.gala.multiscreen.dmr.model.MSMessage.VALUE_KEY = r17;	 Catch:{ Exception -> 0x00d3 }
            r0 = r11.aid;	 Catch:{ Exception -> 0x00d3 }
            r17 = r0;
            r18 = "";
            r17 = r17.equals(r18);	 Catch:{ Exception -> 0x00d3 }
            if (r17 == 0) goto L_0x0199;
        L_0x017e:
            r0 = r11.tvid;	 Catch:{ Exception -> 0x00d3 }
            r17 = r0;
            r18 = "";
            r17 = r17.equals(r18);	 Catch:{ Exception -> 0x00d3 }
            if (r17 == 0) goto L_0x0199;
        L_0x018b:
            r17 = com.gala.multiscreen.dmr.model.MSMessage.VALUE_SESSION;	 Catch:{ Exception -> 0x00d3 }
            r18 = "";
            r17 = r17.equals(r18);	 Catch:{ Exception -> 0x00d3 }
            if (r17 == 0) goto L_0x01b3;
        L_0x0196:
            r14 = "fail";
        L_0x0199:
            r3.onPushVideoEvent(r11);	 Catch:{ Exception -> 0x00d3 }
            r17 = com.gala.multiscreen.dmr.SeekCounter.getSeekCounter();	 Catch:{ Exception -> 0x00d3 }
            r17.finishSeek();	 Catch:{ Exception -> 0x00d3 }
            r17 = com.gala.multiscreen.dmr.model.MSMessage.VALUE_SESSION;	 Catch:{ Exception -> 0x00d3 }
            r18 = "";
            r17 = r17.equals(r18);	 Catch:{ Exception -> 0x00d3 }
            if (r17 == 0) goto L_0x01be;
        L_0x01ae:
            r14 = "success";
        L_0x01b1:
            goto L_0x006e;
        L_0x01b3:
            r17 = 0;
            r17 = java.lang.Boolean.valueOf(r17);	 Catch:{ Exception -> 0x00d3 }
            r14 = com.gala.multiscreen.dmr.util.MsgBuilder.buildResultMsg(r17);	 Catch:{ Exception -> 0x00d3 }
            goto L_0x0199;
        L_0x01be:
            r17 = 1;
            r17 = java.lang.Boolean.valueOf(r17);	 Catch:{ Exception -> 0x00d3 }
            r14 = com.gala.multiscreen.dmr.util.MsgBuilder.buildResultMsg(r17);	 Catch:{ Exception -> 0x00d3 }
            goto L_0x01b1;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.gala.multiscreen.dmr.logic.listener.CustomListener.1.onReceiveSendMessage(int, java.lang.String, java.lang.StringBuffer):void");
        }
    };

    public void init(MediaRenderer mMediaRenderer) {
        mMediaRenderer.setGalaDLNAListener(this.mGalaDLNAListener);
    }
}
