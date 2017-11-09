package com.gala.video.lib.share.ifmanager.bussnessIF.player;

import android.content.Context;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import com.gala.sdk.event.OnAdSpecialEventListener;
import com.gala.sdk.player.IGalaVideoPlayer;
import com.gala.sdk.player.OnPlayerStateChangedListener;
import com.gala.sdk.player.ScreenMode;
import com.gala.sdk.player.WindowZoomRatio;
import com.gala.video.lib.share.ifmanager.IInterfaceWrapper;

public interface IGalaVideoPlayerGenerator extends IInterfaceWrapper {

    public static abstract class Wrapper implements IGalaVideoPlayerGenerator {
        public Object getInterface() {
            return this;
        }

        public static IGalaVideoPlayerGenerator asInterface(Object wrapper) {
            if (wrapper == null || !(wrapper instanceof IGalaVideoPlayerGenerator)) {
                return null;
            }
            return (IGalaVideoPlayerGenerator) wrapper;
        }
    }

    IGalaVideoPlayer createVideoPlayer(Context context, ViewGroup viewGroup, Bundle bundle, OnPlayerStateChangedListener onPlayerStateChangedListener, ScreenMode screenMode, LayoutParams layoutParams, WindowZoomRatio windowZoomRatio, IMultiEventHelper iMultiEventHelper, OnAdSpecialEventListener onAdSpecialEventListener);

    SurfaceView getSurfaceViewPlayerUsed(IGalaVideoPlayer iGalaVideoPlayer);
}
