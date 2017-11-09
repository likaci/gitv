package com.gala.video.lib.share.ifmanager.bussnessIF.epg.project.control;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.animation.Animation;
import com.gala.video.lib.share.common.widget.GlobalDialog;
import com.gala.video.lib.share.ifmanager.IInterfaceWrapper;

public interface IControlInterface extends IInterfaceWrapper {

    public static abstract class Wrapper implements IControlInterface {
        public Object getInterface() {
            return this;
        }

        public static IControlInterface asInterface(Object wrapper) {
            if (wrapper == null || !(wrapper instanceof IControlInterface)) {
                return null;
            }
            return (IControlInterface) wrapper;
        }
    }

    boolean debugMode();

    boolean disableGifAnimForDetailPage();

    Drawable getBackgroundDrawable();

    GlobalDialog getGlobalDialog(Context context);

    Animation getLoadingViewAnimation();

    String getNetWorkSettingAction();

    boolean isOpenAnimation();

    boolean isOpenCarousel();

    boolean isOpenCrossWalk();

    boolean isUsingGalaSettings();

    boolean isUsingGalaSettingsOutSide();

    boolean releasePlayerOnStop();
}
