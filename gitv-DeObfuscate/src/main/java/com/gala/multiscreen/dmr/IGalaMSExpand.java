package com.gala.multiscreen.dmr;

import com.gala.multiscreen.dmr.model.msg.Notify;
import com.gala.multiscreen.dmr.model.msg.PushVideo;
import com.gala.multiscreen.dmr.model.msg.Video;
import com.gala.multiscreen.dmr.model.type.Action;
import java.util.List;

public interface IGalaMSExpand extends IGalaMSCallback {
    long getPlayPosition();

    boolean onActionChanged(Action action);

    boolean onKeyChanged(int i);

    Notify onPhoneSync();

    void onPushPlayList(List<Video> list);

    void onPushVideoEvent(PushVideo pushVideo);

    boolean onResolutionChanged(String str);

    boolean onSeekChanged(long j);
}
