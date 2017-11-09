package com.gala.video.lib.framework.coreservice.multiscreen;

import com.gala.multiscreen.dmr.model.MSMessage.KeyKind;
import com.gala.multiscreen.dmr.model.MSMessage.RequestKind;
import com.gala.multiscreen.dmr.model.msg.Notify;
import com.gala.multiscreen.dmr.model.msg.PushVideo;
import com.gala.multiscreen.dmr.model.msg.Video;
import com.gala.multiscreen.dmr.model.type.Action;
import java.util.List;

public interface IMSGalaCustomListener {

    public interface OnPushVideoEvent {
        void onEvent(PushVideo pushVideo);
    }

    public interface OnFlingEvent {
        void onEvent(KeyKind keyKind);
    }

    public interface OnKeyChanged {
        void onEvent(int i);
    }

    public interface OnNotifyEvent {
        void onEvent(RequestKind requestKind, String str);
    }

    long getPlayPosition();

    boolean onActionChanged(Action action);

    void onFlingEvent(KeyKind keyKind);

    void onInput(String str, boolean z);

    boolean onKeyChanged(int i);

    void onNotifyEvent(RequestKind requestKind, String str);

    Notify onPhoneSync();

    boolean onPushPlayList(List<Video> list);

    void onPushVideoEvent(PushVideo pushVideo);

    boolean onResolutionChanged(String str);

    boolean onSeekChanged(long j);

    void onSeekEvent(KeyKind keyKind);
}
