package com.gala.video.app.epg.screensaver;

import android.content.Context;
import android.view.KeyEvent;
import android.widget.TextView;
import com.gala.tvapi.vrs.model.ChannelLabel;
import com.gala.video.app.epg.home.data.ResourceOperatePingbackModel;
import com.gala.video.app.epg.home.data.pingback.HomePingbackFactory;
import com.gala.video.app.epg.home.utils.ResourceOperateImageUtils;
import com.gala.video.app.epg.screensaver.constants.ScreenSaverConstants.ScreenSaverPingBack;
import com.gala.video.lib.framework.core.pingback.PingBackUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifimpl.ads.AdsClientUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.dynamic.IDynamicResult.OperationImageType;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.pingback.HomePingbackType.ClickPingback;
import com.gala.video.lib.share.ifmanager.bussnessIF.screensaver.IScreenSaverOperate;
import com.gala.video.lib.share.ifmanager.bussnessIF.screensaver.IScreenSaverOperate.IScreenSaverAdClick;
import com.gala.video.lib.share.ifmanager.bussnessIF.screensaver.IScreenSaverOperate.IScreenSaverBeforeFadeIn;
import com.gala.video.lib.share.ifmanager.bussnessIF.screensaver.IScreenSaverOperate.IScreenSaverClick;
import com.gala.video.lib.share.ifmanager.bussnessIF.screensaver.model.ScreenSaverAdModel;
import com.gala.video.lib.share.ifmanager.bussnessIF.screensaver.model.ScreenSaverModel;
import com.gala.video.lib.share.pingback.HomeAdPingbackModel;
import com.gala.video.lib.share.pingback.PingBackCollectionFieldUtils;

public class ScreenSaverStartTool {
    private static final String TAG = "ScreenSaverStartTool";
    private IScreenSaverBeforeFadeIn mIScreenSaverBeforeFadeInCallBack = new IScreenSaverBeforeFadeIn() {
        public boolean onBeforeFadeIn(ScreenSaverModel screenSaverModel, TextView mTvClickTxtView) {
            LogUtils.d(ScreenSaverStartTool.TAG, "IScreenSaverOperate.IScreenSaverBeforeFadeIn, onBeforeFadeIn, screensavermodel " + screenSaverModel);
            if (screenSaverModel != null) {
                ChannelLabel label = screenSaverModel.getChannelLabel();
                if (ResourceOperateImageUtils.isSupportJump(label) && ResourceOperateImageUtils.isSupportResType(label)) {
                    mTvClickTxtView.setVisibility(0);
                    return true;
                }
                mTvClickTxtView.setVisibility(4);
                return false;
            }
            mTvClickTxtView.setVisibility(4);
            return false;
        }
    };
    private IScreenSaverAdClick mScreenSaverAdClickListener = new IScreenSaverAdClick() {
        public boolean onKeyEvent(KeyEvent event, ScreenSaverAdModel adModel, Context context) {
            if (event.getKeyCode() != 23 && event.getKeyCode() != 66) {
                return false;
            }
            PingBackCollectionFieldUtils.setIncomeSrc("others");
            ScreenSaverStartTool.this.onScreenSaverAdClick(adModel, context);
            return true;
        }
    };
    private IScreenSaverClick mScreenSaverClickListener = new IScreenSaverClick() {
        public boolean onKeyEvent(KeyEvent event, ScreenSaverModel model, Context context) {
            if (model == null) {
                LogUtils.w(ScreenSaverStartTool.TAG, "on click screen saver image, model is null");
                return true;
            }
            boolean isEnableJump;
            if (ResourceOperateImageUtils.isSupportJump(model.getChannelLabel()) && ResourceOperateImageUtils.isSupportResType(model.getChannelLabel())) {
                isEnableJump = true;
            } else {
                isEnableJump = false;
            }
            HomePingbackFactory.instance().createPingback(ClickPingback.SCREEN_SAVER_PAGE_CLICK_PINGBACK).addItem("rpage", "screensaver").addItem("block", isEnableJump ? "screensaver_jump" : "screensaver").addItem("rseat", ScreenSaverStartTool.this.getRSeatByKeyEvent(event)).addItem("r", ResourceOperateImageUtils.getRValue(model.getChannelLabel())).setOthersNull().post();
            if (23 != event.getKeyCode() && 66 != event.getKeyCode()) {
                return false;
            }
            if (isEnableJump) {
                PingBackCollectionFieldUtils.setIncomeSrc("others");
                ResourceOperatePingbackModel pingbackModel = ResourceOperateImageUtils.getPingbackModel(model.getChannelLabel(), OperationImageType.SCREENSAVER);
                pingbackModel.setS2("screensaver");
                PingBackUtils.setTabSrc("其他");
                pingbackModel.setEnterType(13);
                pingbackModel.setIncomesrc("others");
                ResourceOperateImageUtils.onClick(context, model.getChannelLabel(), pingbackModel);
                return true;
            }
            LogUtils.d(ScreenSaverStartTool.TAG, "on click screen saver image, not support jump, or not support Resource type");
            return true;
        }
    };

    public void StartSS() {
        IScreenSaverOperate iOperate = GetInterfaceTools.getIScreenSaver();
        iOperate.setScreenSaverAdClickListener(this.mScreenSaverAdClickListener);
        iOperate.setScreenSaverClickListener(this.mScreenSaverClickListener);
        iOperate.setScreenSaverBeforeFadeInCallBack(this.mIScreenSaverBeforeFadeInCallBack);
        iOperate.start();
    }

    private void onScreenSaverAdClick(ScreenSaverAdModel screenSaverAdModel, Context context) {
        if (screenSaverAdModel == null) {
            LogUtils.d(TAG, "onScreenSaverAdClick, screen saver ad data is empty");
            return;
        }
        HomeAdPingbackModel pingbackModel = new HomeAdPingbackModel();
        pingbackModel.setH5EnterType(16);
        pingbackModel.setH5From("ad_jump");
        pingbackModel.setH5TabSrc("其他");
        pingbackModel.setPlFrom("ad_jump");
        pingbackModel.setPlTabSrc("其他");
        pingbackModel.setVideoFrom("ad_jump");
        pingbackModel.setVideoTabSource("其他");
        pingbackModel.setVideoBuySource("");
        pingbackModel.setCarouselFrom("ad_jump");
        pingbackModel.setCarouselTabSource("其他");
        AdsClientUtils.getInstance().onAdClicked(screenSaverAdModel.getAdId());
        if (screenSaverAdModel.isEnableJumping()) {
            GetInterfaceTools.getIAdProcessingUtils().onClickAd(context, screenSaverAdModel, pingbackModel);
        } else {
            LogUtils.d(TAG, "onScreenSaverAdClick, screen saver ad can not jump");
        }
    }

    private String getRSeatByKeyEvent(KeyEvent event) {
        String rseat = "";
        switch (event.getKeyCode()) {
            case 3:
                return "home";
            case 4:
                return "back";
            case 19:
                return ScreenSaverPingBack.SEAT_KEY_UP;
            case 20:
                return ScreenSaverPingBack.SEAT_KEY_DOWN;
            case 21:
                return ScreenSaverPingBack.SEAT_KEY_LEFT;
            case 22:
                return ScreenSaverPingBack.SEAT_KEY_RIGHT;
            case 23:
                return ScreenSaverPingBack.SEAT_KEY_OK;
            case 24:
                return ScreenSaverPingBack.SEAT_KEY_VOLUP;
            case 25:
                return ScreenSaverPingBack.SEAT_KEY_VOLDOWN;
            case 82:
                return "menu";
            default:
                return "unknown";
        }
    }
}
