package com.gala.video.lib.share.ifmanager.bussnessIF.screensaver;

import android.app.Activity;
import android.content.Context;
import android.view.KeyEvent;
import android.widget.TextView;
import com.gala.video.lib.share.ifmanager.IInterfaceWrapper;
import com.gala.video.lib.share.ifmanager.bussnessIF.screensaver.IScreenSaverAnimation.IRegisterAd;
import com.gala.video.lib.share.ifmanager.bussnessIF.screensaver.IScreenSaverAnimation.IRegisterImage;
import com.gala.video.lib.share.ifmanager.bussnessIF.screensaver.model.ScreenSaverAdModel;
import com.gala.video.lib.share.ifmanager.bussnessIF.screensaver.model.ScreenSaverModel;

public interface IScreenSaverOperate extends IInterfaceWrapper {

    public static abstract class Wrapper implements IScreenSaverOperate {
        public Object getInterface() {
            return this;
        }

        public static IScreenSaverOperate asInterface(Object wrapper) {
            if (wrapper == null || !(wrapper instanceof IScreenSaverOperate)) {
                return null;
            }
            return (IScreenSaverOperate) wrapper;
        }
    }

    public interface IScreenSaverAdClick {
        boolean onKeyEvent(KeyEvent keyEvent, ScreenSaverAdModel screenSaverAdModel, Context context);
    }

    public interface IScreenSaverBeforeFadeIn {
        boolean onBeforeFadeIn(ScreenSaverModel screenSaverModel, TextView textView);
    }

    public interface IScreenSaverClick {
        boolean onKeyEvent(KeyEvent keyEvent, ScreenSaverModel screenSaverModel, Context context);
    }

    void exitHomeVersionScreenSaver(Context context);

    IRegisterAd getAdRegister();

    Activity getCurActivity();

    IRegisterImage getImgRegister();

    IScreenSaverStatusDispatcher getStatusDispatcher();

    void hideScreenSaver();

    boolean isApplicationBroughtToBackground();

    boolean isShowScreenSaver();

    void reStart();

    void setCurrentActivity(Activity activity);

    void setScreenSaverAdClickListener(IScreenSaverAdClick iScreenSaverAdClick);

    void setScreenSaverBeforeFadeInCallBack(IScreenSaverBeforeFadeIn iScreenSaverBeforeFadeIn);

    void setScreenSaverClickListener(IScreenSaverClick iScreenSaverClick);

    void setScreenSaverEnable(boolean z);

    void start();

    void stop();
}
