package com.gala.video.app.epg.home.ads.controller;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.gala.sdk.plugin.PluginType;
import com.gala.video.app.epg.home.ads.AdImageResTaskStrategy;
import com.gala.video.app.epg.home.ads.model.ExitAppAdModel;
import com.gala.video.app.epg.home.ads.presenter.ExitAppAdPresenter;
import com.gala.video.app.epg.home.ads.presenter.ExitAppAdPresenter.OnAdImageClickListener;
import com.gala.video.app.epg.home.ads.presenter.ExitOperateImagePresenter;
import com.gala.video.app.epg.home.ads.presenter.ExitOperateImagePresenter.OnOperateImageClickListener;
import com.gala.video.app.epg.home.ads.task.ExitAppDisplayAdInfoRequestTask;
import com.gala.video.app.epg.home.ads.task.ExitAppDisplayAdInfoRequestTask.OnExitAdRequestListener;
import com.gala.video.app.epg.home.data.pingback.HomePingbackFactory;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifimpl.ads.AdsClientUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.ads.AdsConstants.AdClickType;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.pingback.HomePingbackType.ClickPingback;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.pingback.HomePingbackType.ShowPingback;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.pingback.IHomePingback;
import com.mcto.ads.internal.net.PingbackConstants;
import java.util.Random;

public class ExitAppController {
    private static final int AD_TYPE = 1;
    private static final int LOADING_TYPE = 5;
    private static final int NONE_TYPE = 0;
    private static final int ON_CREATE_TYPE = 4;
    private static final int OPERATE_TYPE = 2;
    private static final int REC_TYPE = 3;
    private static final String TAG = "ExitAppController";
    private Context mContext;
    private OnExitAdRequestListener mExitAdRequestListener = new OnExitAdRequestListener() {
        public void onSuccess(ExitAppAdModel exitAppAdModel, Bitmap bitmap) {
            if (exitAppAdModel == null || bitmap == null) {
                ExitAppController.this.onNoAdData();
                return;
            }
            ExitAppController.this.mShowType = 1;
            ExitAppController.this.mExitAppAdModel = exitAppAdModel;
            LogUtils.d(ExitAppController.TAG, "exitAppAdModel = " + exitAppAdModel);
            boolean hasQr = exitAppAdModel.shouldShowQr();
            Bitmap qrBitmap = exitAppAdModel.getQrBitmap();
            boolean isSuccShowQr = false;
            if (hasQr && qrBitmap != null) {
                isSuccShowQr = true;
            }
            ExitAppController.this.mExitAppAdPresenter.show(exitAppAdModel, bitmap);
            AdsClientUtils.getInstance().onAdStarted(exitAppAdModel.getAdId());
            AdsClientUtils.getInstance().sendAdPingBacks();
            AdsClientUtils.getInstance().flushCupidPingback();
            ExitAppController.this.sendPageShowPingBack(1, "exit", hasQr, isSuccShowQr);
        }

        public void onFailed() {
            ExitAppController.this.onNoAdData();
        }

        public void onNoAdData() {
            ExitAppController.this.onNoAdData();
        }

        public void onTimeOut() {
            ExitAppController.this.onNoAdData();
        }
    };
    private ExitAppAdModel mExitAppAdModel = null;
    private ExitAppAdPresenter mExitAppAdPresenter;
    private ExitAppDisplayAdInfoRequestTask mExitAppDisplayAdInfoRequestTask;
    private ExitOperateImagePresenter mExitOperateImagePresenter;
    private int mShowType = 5;

    public ExitAppController(Context context) {
        this.mContext = context;
        this.mExitAppAdPresenter = new ExitAppAdPresenter(context);
        this.mExitOperateImagePresenter = new ExitOperateImagePresenter(context);
        this.mExitAppDisplayAdInfoRequestTask = new ExitAppDisplayAdInfoRequestTask();
        this.mExitAppDisplayAdInfoRequestTask.setOnExitAdRequestListener(this.mExitAdRequestListener);
    }

    public void setWidgets(View contentLayout, View adImageQrLayout, View qrLayout, ImageView adImv, ImageView qrImv, TextView qrTitleTxv, TextView qrDescTxv, TextView adLabelTxv, View leftBtn, View rightBtn) {
        this.mExitAppAdPresenter.setWidgets(contentLayout, qrLayout, adImv, qrImv, qrTitleTxv, qrDescTxv, adLabelTxv);
        this.mExitOperateImagePresenter.setWidgets(contentLayout, adImv, adImageQrLayout);
    }

    public void show() {
        downloadAd();
    }

    private void downloadAd() {
        if (!AdImageResTaskStrategy.getInstance().hasRequestExitAd() || AdImageResTaskStrategy.getInstance().hasExitAd()) {
            this.mExitAppDisplayAdInfoRequestTask.execute();
        } else {
            this.mExitAppDisplayAdInfoRequestTask.getExitAdRequestListener().onNoAdData();
        }
    }

    public void setNextFocusDownId(int id) {
        this.mExitAppAdPresenter.setNextFocusDownId(id);
        this.mExitOperateImagePresenter.setNextFocusDownId(id);
    }

    private void onNoAdData() {
        this.mShowType = getShowType();
        switch (this.mShowType) {
            case 2:
                this.mExitOperateImagePresenter.show();
                break;
            default:
                LogUtils.d(TAG, "both image and recommend do not exist!");
                break;
        }
        sendPageShowPingBack(this.mShowType, "exit", false, false);
    }

    private int random(int max) {
        return new Random(System.currentTimeMillis()).nextInt(max);
    }

    private int getShowType() {
        if (hasLocalOperateImage()) {
            return 2;
        }
        return 0;
    }

    private boolean hasLocalOperateImage() {
        return this.mExitOperateImagePresenter.hasLocalImage();
    }

    public void setOnOperateImageClickListener(OnOperateImageClickListener onOperateImageClickListener) {
        this.mExitOperateImagePresenter.setOnOperateImageClickListener(onOperateImageClickListener);
    }

    public void setOnAdImageClickListener(OnAdImageClickListener listener) {
        this.mExitAppAdPresenter.setOnOperateImageClickListener(listener);
    }

    public void sendPageShowPingbackOnCreate() {
        sendPageShowPingBack(4, "exit_show", false, false);
    }

    public void sendPageShowPingBack(int type, String qtcurl, boolean hasQR, boolean qrSucc) {
        IHomePingback pingback = HomePingbackFactory.instance().createPingback(ShowPingback.EXIT_APP_SHOW_PINGBACK).addItem("bstp", "1").addItem("qtcurl", qtcurl);
        String block = PluginType.DEFAULT_TYPE;
        switch (type) {
            case 1:
                block = getBlockForAd(this.mExitAppAdModel);
                break;
            case 2:
                block = this.mExitOperateImagePresenter.isEnableJump() ? "opration_jump" : "operation";
                break;
            case 3:
                block = "rec";
                break;
            case 4:
                block = "exit_show";
                break;
        }
        pingback.addItem("block", block).setOthersNull().post();
    }

    public void sendExitAppPageClickPingback(String rseat, String r, String c1) {
        String block = "";
        switch (this.mShowType) {
            case 0:
                block = PluginType.DEFAULT_TYPE;
                break;
            case 1:
                block = getBlockForAd(this.mExitAppAdModel);
                break;
            case 2:
                block = this.mExitOperateImagePresenter.isEnableJump() ? "opration_jump" : "operation";
                break;
            case 3:
                block = "rec";
                break;
            case 5:
                block = "exit_show";
                break;
        }
        HomePingbackFactory.instance().createPingback(ClickPingback.EXIT_APP_CLICK_PINGBACK).addItem("rpage", "exit").addItem("rseat", rseat).addItem("block", block).addItem("r", r).addItem("c1", c1).addItem("rt", "i").setOthersNull().post();
        this.mShowType = 5;
    }

    private String getBlockForAd(ExitAppAdModel exitAppAdModel) {
        String block = PingbackConstants.AD_EVENTS;
        if (exitAppAdModel == null) {
            return block;
        }
        switch (exitAppAdModel.getAdClickType()) {
            case DEFAULT:
                block = "ad_jump_defalt";
                break;
            case VIDEO:
                block = "ad_jump_play";
                break;
            case H5:
                block = "ad_jump_h5";
                break;
            case CAROUSEL:
                block = "ad_jump_carousel";
                break;
            case IMAGE:
                block = "ad_jump_pic";
                break;
            case PLAY_LIST:
                block = "ad_jump_plid";
                break;
            case CAROUSEL_ILLEGAL:
                LogUtils.d(TAG, "getBlockForAd, ad click type :" + AdClickType.CAROUSEL_ILLEGAL);
                break;
            case VIDEO_ILLEGAL:
                LogUtils.d(TAG, "getBlockForAd, ad click type :" + AdClickType.VIDEO_ILLEGAL);
                break;
            default:
                LogUtils.d(TAG, "getBlockForAd, ad data :" + exitAppAdModel);
                break;
        }
        return block;
    }
}
