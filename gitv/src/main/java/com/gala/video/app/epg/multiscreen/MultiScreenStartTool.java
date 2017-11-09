package com.gala.video.app.epg.multiscreen;

import android.content.Context;
import com.gala.multiscreen.dmr.logic.MSIcon;
import com.gala.multiscreen.dmr.model.MSMessage.PlayKind;
import com.gala.multiscreen.dmr.model.MSMessage.PushKind;
import com.gala.multiscreen.dmr.model.MSMessage.SeekTimeKind;
import com.gala.multiscreen.dmr.model.msg.PushVideo;
import com.gala.video.lib.framework.coreservice.multiscreen.IMSGalaCustomListener.OnPushVideoEvent;
import com.gala.video.lib.framework.coreservice.multiscreen.IMSStandListener;
import com.gala.video.lib.framework.coreservice.multiscreen.impl.MultiScreen;
import com.gala.video.lib.share.pingback.PingBackCollectionFieldUtils;
import com.gala.video.lib.share.project.Project;
import java.util.List;

public class MultiScreenStartTool {
    private static IMSStandListener sMsStandListener = new IMSStandListener() {
        public void onNotify(PlayKind kind, String message) {
        }

        public void onSeek(SeekTimeKind kind, long time) {
        }

        public void setAVTransportURI(PushKind kind, String url, boolean isNext) {
            switch (kind) {
                case VIDEO:
                case MUSIC:
                    MsUtils.sendUrlToPlayer(url, isNext);
                    return;
                default:
                    return;
            }
        }
    };
    private static OnPushVideoEvent sOnPushVideoistener = new OnPushVideoEvent() {
        public void onEvent(PushVideo pushVideo) {
            PingBackCollectionFieldUtils.setIncomeSrc("phone");
            MsUtils.sendTvidToPlayer(pushVideo);
        }
    };

    public static void start(Context context) {
        MultiScreen.get().getGalaCustomOperator().registerOnPushVideoEvent(sOnPushVideoistener);
        MultiScreen.get().getStandardOperator().setMSStandardListener(sMsStandListener);
        String pkgName = Project.getInstance().getBuild().getPackageName();
        List<MSIcon> list = Project.getInstance().getConfig().getMultiScreenIconList();
        MultiScreen.get().start(context, Project.getInstance().getConfig().getMultiScreenName(), "", pkgName, list);
    }
}
