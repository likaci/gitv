package com.gala.video.app.epg.multiscreen;

import android.content.Intent;
import android.os.Bundle;
import com.gala.multiscreen.dmr.model.msg.PushVideo;
import com.gala.multiscreen.dmr.util.ContextProfile;
import com.gala.video.lib.share.common.model.multiscreen.MultiScreenParams;
import com.gala.video.lib.share.common.model.player.PushPlayParamBuilder;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.project.Project;

public class MsUtils {
    private static final String BUY_SOURCE = "phone";
    private static final String TAG = "MsUtils";

    public static void sendTvidToPlayer(PushVideo pushVideo) {
        try {
            if (ContextProfile.getContext() != null) {
                MultiScreenParams cmd = new MultiScreenParams();
                cmd.history = pushVideo.history.equals("") ? "-1" : pushVideo.history;
                cmd.tvid = pushVideo.tvid;
                cmd.aid = pushVideo.aid;
                cmd.streamType = pushVideo.res;
                cmd.auth = pushVideo.auth;
                cmd.key = pushVideo.key;
                cmd.from = "";
                cmd.platform = pushVideo.platform;
                cmd.openForOversea = pushVideo.open_for_oversea.equals("1");
                cmd.ctype = pushVideo.ctype;
                cmd.title = pushVideo.title;
                cmd.f2019v = pushVideo.f608v;
                PushPlayParamBuilder builder = new PushPlayParamBuilder();
                builder.setMultiScreenParams(cmd).setBuySource("phone").setFrom("phone").setTabSource("其他");
                GetInterfaceTools.getPlayerPageProvider().startPushPlayerPage(ContextProfile.getContext(), builder);
            }
        } catch (Exception ex) {
            ex.fillInStackTrace();
        }
    }

    public static void sendUrlToPlayer(String url, boolean isNext) {
        if (ContextProfile.getContext() != null) {
            Intent intent = new Intent();
            intent.setAction(Project.getInstance().getBuild().getPackageName() + ".action.ACTION_PLAYVIDEO");
            Bundle bundle = new Bundle();
            bundle.putString("playInfo", "standarddlna");
            bundle.putBoolean("isNext", isNext);
            bundle.putString("url", url);
            intent.putExtras(bundle);
            ContextProfile.getContext().sendBroadcast(intent);
        }
    }
}
