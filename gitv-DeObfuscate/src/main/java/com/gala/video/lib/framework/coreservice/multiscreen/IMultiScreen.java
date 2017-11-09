package com.gala.video.lib.framework.coreservice.multiscreen;

import android.content.Context;
import com.gala.multiscreen.dmr.logic.MSIcon;
import com.gala.multiscreen.dmr.model.MSMessage.KeyKind;
import com.gala.multiscreen.dmr.model.msg.DlnaMessage;
import java.util.List;

public interface IMultiScreen {
    IMSGalaCustomOperate getGalaCustomOperator();

    IMSStandardOperate getStandardOperator();

    boolean isPhoneConnected();

    boolean isPhoneKey();

    boolean isSupportMS();

    void onSeekFinish();

    void sendMessage(DlnaMessage dlnaMessage);

    void sendSysKey(Context context, KeyKind keyKind);

    void setDeviceName(String str);

    void setDlnaLogEnabled(boolean z);

    void setIsPhoneKey(boolean z);

    void setTvVersion(String str);

    void start(Context context, String str, String str2, String str3, List<MSIcon> list);

    void stop();
}
