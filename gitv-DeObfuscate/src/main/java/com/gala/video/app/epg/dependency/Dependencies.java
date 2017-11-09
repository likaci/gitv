package com.gala.video.app.epg.dependency;

import com.gala.video.lib.share.project.Project;

public class Dependencies {
    public static String MSG_CENTER_ACTION = ".app.epg.ui.imsg.MsgCenterActivity";

    public static String getPackageName() {
        return Project.getInstance().getBuild().getPackageName();
    }

    public static boolean checkSupportPushService() {
        return true;
    }
}
