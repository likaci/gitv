package com.gala.video.app.epg.home.ads.controller;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.gala.download.DownloaderAPI;
import com.gala.download.base.FileRequest;
import com.gala.download.base.IDownloader;
import com.gala.download.base.IFileCallback;
import com.gala.video.app.epg.R;
import com.gala.video.app.epg.home.ads.model.ImageAdInfo;
import com.gala.video.app.epg.home.ads.model.StartAdModel;
import com.gala.video.app.epg.home.data.pingback.HomePingbackFactory;
import com.gala.video.app.epg.screensaver.constants.ScreenSaverConstants.ScreenSaverPingBack;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.common.model.multiscreen.MultiScreenParams;
import com.gala.video.lib.share.ifimpl.ads.AdsClientUtils;
import com.gala.video.lib.share.ifimpl.imsg.utils.IMsgUtils.DBColumns;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.ads.AdsConstants.AdClickType;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.pingback.HomePingbackType.ClickPingback;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.pingback.HomePingbackType.CommonPingback;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.pingback.HomePingbackType.ShowPingback;
import com.gala.video.lib.share.pingback.HomeAdPingbackModel;
import com.gala.video.lib.share.pingback.PingBackCollectionFieldUtils;
import com.gala.video.lib.share.pingback.PingBackParams.Keys;
import com.gala.video.lib.share.utils.ResourceUtil;
import com.mcto.ads.AdsClient;
import com.mcto.ads.CupidAd;
import com.mcto.ads.internal.net.PingbackConstants;
import org.cybergarage.soap.SOAP;

public class StartScreenImageAd implements IStartScreenAd {
    private static final int AD_ELAPSE = 1000;
    private static final int AD_PLAY_SECOND = 3;
    private static final String TAG = "home/ad/StartScreenImageAd";
    private static final long TIME_OUT = 2000;
    private View mAdContainer;
    private int mAdId;
    private String mAdImageUrl = "";
    private ImageAdInfo mAdInfo;
    private AdsClient mAdsClient;
    private Bitmap mBitmap;
    private AdStatusCallBack mCallBack;
    private boolean mCanSkip = false;
    private int mCountDownTime = 3;
    private IDownloader mDownloader = DownloaderAPI.getDownloader();
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private String mPingbackBlock = "NA";
    private Runnable mRunnable = new Runnable() {
        public void run() {
            if (StartScreenImageAd.this.mCountDownTime > 0) {
                LogUtils.d(StartScreenImageAd.TAG, StartScreenImageAd.this.mCountDownTime + SOAP.DELIM + System.currentTimeMillis());
                StartScreenImageAd.this.mTimerTextView.setText(StartScreenImageAd.this.mCountDownTime = StartScreenImageAd.this.mCountDownTime - 1 + "");
                StartScreenImageAd.this.mHandler.postDelayed(StartScreenImageAd.this.mRunnable, 1000);
                return;
            }
            LogUtils.d(StartScreenImageAd.TAG, "start image screen is finished");
            StartScreenImageAd.this.mTimerTextView = null;
            StartScreenImageAd.this.mBitmap = null;
            StartScreenImageAd.this.mAdContainer = null;
            StartScreenImageAd.this.mAdsClient.flushCupidPingback();
            if (StartScreenImageAd.this.mCallBack != null) {
                StartScreenImageAd.this.mCallBack.onFinished();
            }
        }
    };
    private StartAdModel mStartAdModel = null;
    private long mStartRequestTime;
    private long mStartTime = 0;
    private TextView mTimerTextView;

    public StartScreenImageAd(AdsClient client, ImageAdInfo adInfo, long startRequestTime) {
        this.mAdsClient = client;
        this.mStartRequestTime = startRequestTime;
        if (adInfo != null) {
            this.mAdInfo = adInfo;
            this.mAdImageUrl = adInfo.getImageUrl();
            this.mAdId = adInfo.getAdid();
            this.mStartAdModel = parseAdDataToStartAdModel(this.mAdInfo.getCupiAd());
            this.mCanSkip = adInfo.canSkip();
        }
    }

    private void sendPingback(long interval) {
        String st = "";
        if (StringUtils.isEmpty(DownloaderAPI.getDownloader().getLocalPath(new FileRequest(this.mAdImageUrl)))) {
            st = "pic_download";
        } else {
            st = "pic_local";
        }
        HomePingbackFactory.instance().createPingback(CommonPingback.AD_DATA_REQUEST_PINGBACK).addItem(Keys.RI, "ad_startapk").addItem("st", st).addItem("td", String.valueOf(interval)).addItem("r", DBColumns.PIC).addItem(Keys.T, "11").addItem("ct", "150619_request").setOthersNull().post();
    }

    public void loadData(long interval) {
        sendPingback(interval);
        loadImage();
    }

    public void showAd(ViewGroup container) {
        this.mAdContainer = container;
        this.mAdContainer.bringToFront();
        ImageView imgView = (ImageView) container.findViewById(R.id.epg_screen_ad);
        TextView textView = (TextView) container.findViewById(R.id.epg_tv_adtime);
        TextView tipsTxtView = (TextView) container.findViewById(R.id.epg_start_ad_jump_tip);
        String text = ResourceUtil.getStr(R.string.screen_saver_click_text);
        SpannableStringBuilder ssb = new SpannableStringBuilder(text);
        ForegroundColorSpan white = new ForegroundColorSpan(-921103);
        ForegroundColorSpan white1 = new ForegroundColorSpan(-921103);
        ForegroundColorSpan orange = new ForegroundColorSpan(-19456);
        ssb.setSpan(white, 0, 1, 33);
        ssb.setSpan(orange, 2, 4, 33);
        ssb.setSpan(white1, 5, text.length(), 33);
        tipsTxtView.setText(ssb);
        TextView adBadgeTxtView = (TextView) container.findViewById(R.id.epg_tv_ad_badge);
        if (this.mStartAdModel != null && this.mStartAdModel.isNeedAdBadge()) {
            adBadgeTxtView.setVisibility(0);
        }
        if (this.mStartAdModel != null && this.mStartAdModel.isEnableJumping()) {
            tipsTxtView.setVisibility(0);
        }
        showAd(imgView, textView);
    }

    public void stop() {
    }

    public boolean enableJump() {
        return this.mStartAdModel.isEnableJumping();
    }

    public boolean dispatchKeyEvent(KeyEvent keyEvent) {
        if ((keyEvent.getKeyCode() == 23 || keyEvent.getKeyCode() == 66) && keyEvent.getAction() == 0) {
            if (enableJump()) {
                onClick();
            }
            sendPingback(keyEvent);
            return true;
        }
        if (keyEvent.getKeyCode() == 20 && keyEvent.getAction() == 0) {
            sendPingback(keyEvent);
        }
        return false;
    }

    private void sendPingback(KeyEvent keyEvent) {
        String rseat = "";
        if (20 == keyEvent.getKeyCode()) {
            rseat = ScreenSaverPingBack.SEAT_KEY_DOWN;
        } else if (22 == keyEvent.getKeyCode()) {
            rseat = ScreenSaverPingBack.SEAT_KEY_RIGHT;
        }
        HomePingbackFactory.instance().createPingback(ClickPingback.START_AD_PAGE_CLICK_PINGBACK).addItem("rpage", "ad_start").addItem("block", getBlockForAd(this.mStartAdModel)).addItem("rt", "i").addItem(Keys.ISACT, "NA").addItem("rseat", rseat).setOthersNull().post();
    }

    public void setAdStatusCallBack(AdStatusCallBack callBack) {
        this.mCallBack = callBack;
    }

    private StartAdModel parseAdDataToStartAdModel(CupidAd cupidAd) {
        if (cupidAd == null) {
            LogUtils.w(TAG, "parseAdDataToStartAdModel, cupidAd data is null");
            return null;
        }
        StartAdModel startAdModel = new StartAdModel();
        startAdModel.setAdId(cupidAd.getAdId());
        Object isNeedAdBadge = cupidAd.getCreativeObject().get("needAdBadge");
        boolean z = isNeedAdBadge != null && "true".equals(isNeedAdBadge.toString());
        startAdModel.setNeedAdBadge(z);
        GetInterfaceTools.getIAdProcessingUtils().parseAdRawData(cupidAd, startAdModel);
        return startAdModel;
    }

    private void loadImage() {
        LogUtils.d(TAG, "loadImage");
        this.mStartTime = SystemClock.elapsedRealtime();
        this.mDownloader.loadFile(new FileRequest(this.mAdImageUrl), new IFileCallback() {
            public void onSuccess(FileRequest fileRequest, String s) {
                long elapse = SystemClock.elapsedRealtime() - StartScreenImageAd.this.mStartTime;
                LogUtils.d(StartScreenImageAd.TAG, "request advertisement data and image success ,time cost : " + elapse);
                Bitmap bitmap = BitmapFactory.decodeFile(s);
                if (elapse >= 2000 || bitmap == null) {
                    LogUtils.d(StartScreenImageAd.TAG, "it is timeout when downloading image.");
                    StartScreenImageAd.this.mAdsClient.onAdError(StartScreenImageAd.this.mAdId);
                    StartScreenImageAd.this.mAdsClient.sendAdPingBacks();
                } else {
                    StartScreenImageAd.this.mBitmap = bitmap;
                }
                if (StartScreenImageAd.this.mAdInfo.getAdid() != -1 && StartScreenImageAd.this.mBitmap != null) {
                    StartScreenImageAd.this.mCallBack.onAdPrepared();
                }
            }

            public void onFailure(FileRequest fileRequest, Exception e) {
                LogUtils.e(StartScreenImageAd.TAG, "request advertisement image failed", e);
                if (StartScreenImageAd.this.mAdId != -1) {
                    StartScreenImageAd.this.mAdsClient.onAdError(StartScreenImageAd.this.mAdId);
                    StartScreenImageAd.this.mAdsClient.sendAdPingBacks();
                }
                HomePingbackFactory.instance().createPingback(CommonPingback.AD_DATA_REQUEST_PINGBACK).addItem(Keys.RI, "ad_startapk").addItem("st", MultiScreenParams.DLNA_PHONE_CONTROLL_ERROR).addItem("td", String.valueOf(SystemClock.elapsedRealtime() - StartScreenImageAd.this.mStartTime)).addItem(Keys.T, "11").addItem("ct", "150619_request").setOthersNull().post();
            }
        });
    }

    private void showAd(ImageView imageView, TextView textView) {
        this.mTimerTextView = textView;
        if (this.mBitmap == null) {
            this.mAdContainer.setVisibility(8);
            imageView.setVisibility(8);
            this.mAdId = -1;
            if (this.mCallBack != null) {
                this.mCallBack.onFinished();
            }
            LogUtils.d(TAG, "ad image is null");
            return;
        }
        LogUtils.d(TAG, "showAd mAdId=" + this.mAdId);
        this.mAdContainer.setVisibility(0);
        imageView.setImageBitmap(this.mBitmap);
        imageView.setVisibility(0);
        this.mAdsClient.onAdStarted(this.mAdId);
        this.mTimerTextView.setTypeface(Typeface.createFromAsset(AppRuntimeEnv.get().getApplicationContext().getAssets(), "fonts/DS-DIGI.TTF"), 1);
        this.mHandler.postAtFrontOfQueue(this.mRunnable);
        HomePingbackFactory.instance().createPingback(ShowPingback.START_AD_PAGE_SHOW_PINGBACK).addItem("qtcurl", "ad_start").addItem("block", getBlockForAd(this.mStartAdModel)).addItem(Keys.ISACT, "NA").addItem("td", String.valueOf(SystemClock.elapsedRealtime() - this.mStartRequestTime)).setOthersNull().post();
    }

    public void onClick() {
        if (this.mStartAdModel == null) {
            LogUtils.w(TAG, "onClick, ad click info is null!");
            return;
        }
        AdsClientUtils.getInstance().onAdClicked(this.mStartAdModel.getAdId());
        if (this.mStartAdModel.isEnableJumping()) {
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
            PingBackCollectionFieldUtils.setIncomeSrc("others");
            GetInterfaceTools.getIAdProcessingUtils().onClickAd(this.mAdContainer.getContext(), this.mStartAdModel, pingbackModel);
            if (this.mCallBack != null) {
                this.mCallBack.onFinished();
            }
        }
    }

    private String getBlockForAd(StartAdModel startAdModel) {
        String block = PingbackConstants.AD_EVENTS;
        if (startAdModel == null) {
            return block;
        }
        switch (startAdModel.getAdClickType()) {
            case DEFAULT:
                block = "ad_jump_defalt";
                break;
            case VIDEO:
                block = "ad_start_pic_jump_play";
                break;
            case H5:
                block = "ad_start_pic_jump_h5";
                break;
            case CAROUSEL:
                block = "ad_start_pic_jump_carousel";
                break;
            case IMAGE:
                block = "ad_start_pic_jump_pic";
                break;
            case PLAY_LIST:
                block = "ad_start_pic_jump_plid";
                break;
            case CAROUSEL_ILLEGAL:
                LogUtils.d(TAG, "getBlockForAd, start image ad click type :" + AdClickType.CAROUSEL_ILLEGAL);
                break;
            case VIDEO_ILLEGAL:
                LogUtils.d(TAG, "getBlockForAd, start image ad click type :" + AdClickType.VIDEO_ILLEGAL);
                break;
            default:
                LogUtils.d(TAG, "getBlockForAd, start image ad data :" + startAdModel);
                break;
        }
        return block;
    }
}
