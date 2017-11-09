package com.gala.video.lib.share.ifmanager.bussnessIF.player;

import android.content.Context;
import com.gala.multiscreen.dmr.model.MSMessage.KeyKind;
import com.gala.multiscreen.dmr.model.msg.Notify;
import com.gala.sdk.player.IGalaVideoPlayer;
import com.gala.sdk.player.ISceneActionProvider;
import com.gala.tv.voice.service.AbsVoiceAction;
import com.gala.video.lib.share.ifmanager.IInterfaceWrapper;
import com.gala.video.lib.share.ifmanager.bussnessIF.player.multiscreen.DlnaKeyEvent;
import com.gala.video.lib.share.ifmanager.bussnessIF.player.multiscreen.ISuperEventInput;
import com.gala.video.lib.share.ifmanager.bussnessIF.player.multiscreen.ISuperPlayerOverlay;
import com.gala.video.lib.share.ifmanager.bussnessIF.player.multiscreen.KeyValue;
import java.util.List;

public interface IMultiEventHelper extends IInterfaceWrapper {

    public static abstract class Wrapper implements IMultiEventHelper {
        public Object getInterface() {
            return this;
        }

        public static IMultiEventHelper asInterface(Object wrapper) {
            if (wrapper == null || !(wrapper instanceof IMultiEventHelper)) {
                return null;
            }
            return (IMultiEventHelper) wrapper;
        }
    }

    void addSceneActionProvider(ISceneActionProvider iSceneActionProvider);

    long getPlayPosition();

    List<AbsVoiceAction> getSupportedVoices(List<AbsVoiceAction> list);

    List<AbsVoiceAction> getSupportedVoicesWithoutPreAndNext(List<AbsVoiceAction> list);

    boolean isPushVideoByTvPlatform();

    boolean onDlnaKeyEvent(DlnaKeyEvent dlnaKeyEvent, KeyKind keyKind);

    void onGetSceneAction(KeyValue keyValue);

    boolean onKeyChanged(int i);

    Notify onPhoneSync();

    boolean onResolutionChanged(String str);

    boolean onSeekChanged(long j);

    void registerPlayStateListener(IGalaVideoPlayer iGalaVideoPlayer);

    void registerPlayer(IGalaVideoPlayer iGalaVideoPlayer);

    void setContext(Context context);

    void setEventInput(ISuperEventInput iSuperEventInput);

    void setOverlay(ISuperPlayerOverlay iSuperPlayerOverlay);
}
