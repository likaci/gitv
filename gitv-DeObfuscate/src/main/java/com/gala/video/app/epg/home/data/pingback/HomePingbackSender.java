package com.gala.video.app.epg.home.data.pingback;

import android.app.Activity;
import com.gala.video.app.epg.HomeActivity;
import com.gala.video.app.epg.home.data.TabData;
import com.gala.video.app.epg.screensaver.constants.ScreenSaverConstants.ScreenSaverPingBack;
import com.gala.video.app.epg.ui.search.ISearchConstant;
import com.gala.video.lib.framework.core.pingback.PingBackUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.DataSource;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.pingback.HomePingbackType.CommonPingback;
import com.gala.video.lib.share.ifmanager.bussnessIF.screensaver.IScreenSaverAnimation.IAd;
import com.gala.video.lib.share.ifmanager.bussnessIF.screensaver.IScreenSaverAnimation.IImage;
import com.gala.video.lib.share.ifmanager.bussnessIF.screensaver.IScreenSaverStatusDispatcher.IStatusListener;
import com.gala.video.lib.share.ifmanager.bussnessIF.screensaver.model.ScreenSaverAdModel;
import com.gala.video.lib.share.pingback.PingBackParams.Keys;

public class HomePingbackSender {
    private static final boolean HOME_PINGBACK_DEBUG = false;
    private static final String TAG = "HomePingbackSender";
    public static final int TYPE_APP_ITEM = 2;
    public static final int TYPE_BANNER_AD_ITEM = 4;
    public static final int TYPE_CAROUSEL_WINDOW = 0;
    public static final int TYPE_FOCUS_AD_ITEM = 3;
    public static final int TYPE_NULL = -1;
    public static final int TYPE_SETTING_ITEM = 1;
    private static final HomePingbackSender mInstance = new HomePingbackSender();
    private String coverFLowStatus = "manual";
    private TabData mCurTabData;
    private String mCurTabE;
    private String mCurTabIndex = "";
    private TabData mPreTabData;
    private String mPreTabE;
    private String mPreTabIndex = "";
    private String screenSaverE;
    private String tabManagerE;

    class C06581 implements IStatusListener {
        C06581() {
        }

        public void onStart() {
            HomePingbackSender.this.setScreenSaverE(PingBackUtils.createEventId());
        }

        public void onStop() {
            HomePingbackSender.this.setScreenSaverE("");
        }
    }

    class C06592 implements IAd {
        C06592() {
        }

        public void beforeFadeIn(ScreenSaverAdModel model) {
            Activity curActivity = GetInterfaceTools.getIScreenSaver().getCurActivity();
            String rfr = ISearchConstant.TVSRCHSOURCE_OTHER;
            if (curActivity != null && (curActivity instanceof HomeActivity)) {
                rfr = "home";
            }
            String ct = "161212_outside";
            switch (model.getAdClickType()) {
                case H5:
                    HomePingbackFactory.instance().createPingback(CommonPingback.SCREEN_SAVER_PAGE_SHOW_PINGBACK).addItem("ct", "161212_outside").addItem("qtcurl", "screensaver").addItem("block", ScreenSaverPingBack.BLOCK_SCREENSAVER_AD_H5).addItem("rfr", rfr).addItem("a", "21").addItem(Keys.f2035T, "11").addItem("e", HomePingbackSender.getInstance().getScreenSaverE()).setOthersNull().post();
                    return;
                case DEFAULT:
                    HomePingbackFactory.instance().createPingback(CommonPingback.SCREEN_SAVER_PAGE_SHOW_PINGBACK).addItem("ct", "161212_outside").addItem("qtcurl", "screensaver").addItem("block", ScreenSaverPingBack.BLOCK_SCREENSAVER_AD_DEFAULT).addItem("rfr", rfr).addItem("a", "21").addItem(Keys.f2035T, "11").addItem("e", HomePingbackSender.getInstance().getScreenSaverE()).setOthersNull().post();
                    return;
                case IMAGE:
                    HomePingbackFactory.instance().createPingback(CommonPingback.SCREEN_SAVER_PAGE_SHOW_PINGBACK).addItem("ct", "161212_outside").addItem("qtcurl", "screensaver").addItem("block", ScreenSaverPingBack.BLOCK_SCREENSAVER_AD_IMAGE).addItem("rfr", rfr).addItem("a", "21").addItem(Keys.f2035T, "11").addItem("e", HomePingbackSender.getInstance().getScreenSaverE()).setOthersNull().post();
                    return;
                case PLAY_LIST:
                    HomePingbackFactory.instance().createPingback(CommonPingback.SCREEN_SAVER_PAGE_SHOW_PINGBACK).addItem("ct", "161212_outside").addItem("qtcurl", "screensaver").addItem("block", ScreenSaverPingBack.BLOCK_SCREENSAVER_AD_PLID).addItem("rfr", rfr).addItem("a", "21").addItem(Keys.f2035T, "11").addItem("e", HomePingbackSender.getInstance().getScreenSaverE()).setOthersNull().post();
                    return;
                case VIDEO:
                    HomePingbackFactory.instance().createPingback(CommonPingback.SCREEN_SAVER_PAGE_SHOW_PINGBACK).addItem("ct", "161212_outside").addItem("qtcurl", "screensaver").addItem("block", ScreenSaverPingBack.BLOCK_SCREENSAVER_AD_VIDEO).addItem("rfr", rfr).addItem("a", "21").addItem(Keys.f2035T, "11").addItem("e", HomePingbackSender.getInstance().getScreenSaverE()).setOthersNull().post();
                    return;
                case CAROUSEL:
                    HomePingbackFactory.instance().createPingback(CommonPingback.SCREEN_SAVER_PAGE_SHOW_PINGBACK).addItem("ct", "161212_outside").addItem("qtcurl", "screensaver").addItem("block", ScreenSaverPingBack.BLOCK_SCREENSAVER_AD_CAROUSEL).addItem("rfr", rfr).addItem("a", "21").addItem(Keys.f2035T, "11").addItem("e", HomePingbackSender.getInstance().getScreenSaverE()).setOthersNull().post();
                    return;
                case NONE:
                    HomePingbackFactory.instance().createPingback(CommonPingback.SCREEN_SAVER_PAGE_SHOW_PINGBACK).addItem("ct", "161212_outside").addItem("a", "21").addItem("qtcurl", "screensaver").addItem("block", ScreenSaverPingBack.BLOCK_SCREENSAVER_AD_NONE).addItem("rfr", rfr).addItem(Keys.f2035T, "11").addItem("e", HomePingbackSender.getInstance().getScreenSaverE()).setOthersNull().post();
                    return;
                default:
                    return;
            }
        }
    }

    class C06603 implements IImage {
        C06603() {
        }

        public void beforeFadeIn(boolean isEnableJump) {
            Activity curActivity = GetInterfaceTools.getIScreenSaver().getCurActivity();
            String rfr = ISearchConstant.TVSRCHSOURCE_OTHER;
            if (curActivity != null && (curActivity instanceof HomeActivity)) {
                rfr = "home";
            }
            HomePingbackFactory.instance().createPingback(CommonPingback.SCREEN_SAVER_PAGE_SHOW_PINGBACK).addItem("ct", "161212_outside").addItem("qtcurl", "screensaver").addItem("block", isEnableJump ? "screensaver_jump" : "screensaver").addItem("rfr", rfr).addItem("a", "21").addItem(Keys.f2035T, "11").addItem("e", HomePingbackSender.getInstance().getScreenSaverE()).setOthersNull().post();
        }
    }

    private HomePingbackSender() {
    }

    public static HomePingbackSender getInstance() {
        return mInstance;
    }

    public void setCurTabData(TabData tabData) {
        this.mCurTabData = tabData;
    }

    public void setCurTabE() {
        this.mPreTabE = this.mCurTabE;
        this.mCurTabE = PingBackUtils.createEventId();
    }

    public String getScreenSaverE() {
        return this.screenSaverE;
    }

    public void setScreenSaverE(String screenSaverE) {
        this.screenSaverE = screenSaverE;
    }

    public String getTabIndex() {
        return this.mCurTabIndex;
    }

    public void setTabIndex(String tabIndex) {
        this.mCurTabIndex = tabIndex;
    }

    public void setPreTabIndex(String preTabIndex) {
        this.mPreTabIndex = preTabIndex;
    }

    public String getPreTabIndex() {
        return !StringUtils.isEmpty(this.mPreTabIndex) ? this.mPreTabIndex : this.mCurTabIndex;
    }

    public String getTabManagerE() {
        return this.tabManagerE;
    }

    public void setTabManagerE(String tabManagerE) {
        this.tabManagerE = tabManagerE;
    }

    public String getCurTabE() {
        return this.mCurTabE;
    }

    public String getPreTabE() {
        return !StringUtils.isEmpty(this.mPreTabE) ? this.mPreTabE : this.mCurTabE;
    }

    public String getCoverFLowStatus() {
        return this.coverFLowStatus;
    }

    public void setCoverFLowStatus(String coverFLowStatus) {
        this.coverFLowStatus = coverFLowStatus;
    }

    public DataSource getCurTabData() {
        return this.mCurTabData;
    }

    public String getTabName() {
        if (this.mCurTabData == null) {
            return "";
        }
        return this.mCurTabData.getTitle();
    }

    public String getPreTabName() {
        if (this.mPreTabData == null) {
            return "";
        }
        return this.mPreTabData.getTitle();
    }

    public void setPreTabData(TabData tabData) {
        this.mPreTabData = tabData;
    }

    public DataSource getPreTabData() {
        return this.mPreTabData;
    }

    public void registerScreenSaverListener() {
        registerScreenSaverStatusChangeListener();
        registerScreenSaverAdAnimListener();
        registerScreenSaverImageAnimListener();
    }

    private void registerScreenSaverStatusChangeListener() {
        GetInterfaceTools.getIScreenSaver().getStatusDispatcher().register(new C06581());
    }

    private void registerScreenSaverAdAnimListener() {
        GetInterfaceTools.getIScreenSaver().getAdRegister().register(new C06592());
    }

    private void registerScreenSaverImageAnimListener() {
        GetInterfaceTools.getIScreenSaver().getImgRegister().register(new C06603());
    }
}
