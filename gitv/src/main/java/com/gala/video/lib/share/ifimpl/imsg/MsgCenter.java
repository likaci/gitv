package com.gala.video.lib.share.ifimpl.imsg;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import com.gala.tvapi.tv2.TVApiBase;
import com.gala.tvapi.type.PlatformType;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.share.ifimpl.imsg.utils.IMsgUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.imsg.IMsgCenter.Wrapper;
import com.gala.video.lib.share.ifmanager.bussnessIF.imsg.IMsgContent;
import com.gala.video.lib.share.ifmanager.bussnessIF.imsg.IMsgDialogCacheManager;
import com.gala.video.lib.share.project.Project;
import com.push.pushservice.api.PushManager;
import com.push.pushservice.constants.DataConst.LocalArea;
import com.push.pushservice.data.BasicPushInitParam;
import java.util.List;

class MsgCenter extends Wrapper {
    private static final String TAG = "imsg/IMsgCenter";
    private IMsgDialogCacheManager mCacheManager = new MsgDialogCacheManager();
    private final IMsgDataHelper mDBHelper = new IMsgDataHelper();

    public IMsgDialogCacheManager getMsgDialogCache() {
        return this.mCacheManager;
    }

    public void init() {
        LocalArea local = TVApiBase.getTVApiProperty().getPlatform() == PlatformType.TAIWAN ? LocalArea.LA_TAIWAN : LocalArea.LA_MAINLAND;
        Context context = AppRuntimeEnv.get().getApplicationContext();
        String p2 = Project.getInstance().getBuild().getPingbackP2();
        PushManager.init(new BasicPushInitParam(context, IMsgUtils.getAppId(p2), context.getPackageName(), IMsgUtils.getAppVersion(TVApiBase.getTVApiProperty().getVersion()), TVApiBase.getTVApiProperty().getPassportDeviceId(), GetInterfaceTools.getIGalaAccountManager().getUID(), p2, TVApiBase.getTVApiProperty().getUUID(), local, IMsgUtils.getAppId(p2), 24));
        PushManager.startWork(context);
        for (int i = 0; i < 10; i++) {
            if (!IMsgUtils.isServiceLive(context)) {
                Log.d("imsg/IMsgCenter", "wait for pushserivice start");
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        new IMsgBroadcast().isAppStart();
        deleteOldIMsg();
    }

    public int getUnreadIMsgListCount() {
        return this.mDBHelper.getUnreadMsgCount();
    }

    public List<IMsgContent> getIMsgList() {
        return this.mDBHelper.query();
    }

    public List<IMsgContent> getIMsgListForType(int type) {
        return this.mDBHelper.query(type);
    }

    public List<IMsgContent> getNeedShowList() {
        return this.mDBHelper.getDialogList();
    }

    public void insertIMsg(IMsgContent content) {
        content.localTime = TVApiBase.getTVApiProperty().getCurrentTime();
        if (content.valid_till == 0) {
            content.valid_till = content.localTime + IMsgUtils.DEFAULT_INVALID_TIME;
        }
        Log.d("imsg/IMsgCenter", "localTime = " + content.localTime + ", valid_till = " + content.valid_till);
        this.mDBHelper.insert(content);
    }

    public boolean isMsgExist(IMsgContent msg) {
        return this.mDBHelper.isMsgExist(msg);
    }

    public void updateIsReadFlag(IMsgContent content) {
        String[] whereArgs = new String[]{String.valueOf(content.localTime)};
        this.mDBHelper.setIsRead("localTime = ?", whereArgs);
    }

    public void updateIsShowFlag(int isNeedShow, IMsgContent... contents) {
        if (contents != null && contents.length > 0) {
            int length = contents.length;
            for (int i = 0; i < length; i++) {
                String[] whereArgs = new String[]{String.valueOf(contents[i].localTime)};
                this.mDBHelper.setIsNeedShow("localTime = ?", whereArgs, isNeedShow);
            }
        }
    }

    public void updateIsReadFlag(int msg_type) {
        Log.d("imsg/IMsgCenter", "updateIsReadFlag(int msg_type) = " + msg_type);
        if (msg_type == 0) {
            this.mDBHelper.setIsRead(null, null);
            return;
        }
        String[] whereArgs = new String[]{String.valueOf(msg_type)};
        this.mDBHelper.setIsRead("type= ?", whereArgs);
    }

    @SuppressLint({"SimpleDateFormat"})
    public void deleteOldIMsg() {
        Log.d("imsg/IMsgCenter", "old time = " + TVApiBase.getTVApiProperty().getCurrentTime());
        String[] whereArgs = new String[]{String.valueOf(time)};
        this.mDBHelper.delete("valid_till<?", whereArgs);
    }

    public void setDiaLogFlag(boolean isShowDialog) {
        new IMsgBroadcast().setDiaLogFlag(isShowDialog);
    }

    public void pushMsgToApp(Context context, IMsgContent msg) {
        new MsgDataProcessor().pushMsgToApp(context, msg);
    }
}
