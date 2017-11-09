package com.gala.video.app.epg.home.ads.task;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import com.gala.imageprovider.ImageProviderApi;
import com.gala.imageprovider.base.IImageCallback;
import com.gala.imageprovider.base.IImageProvider;
import com.gala.imageprovider.base.ImageRequest;
import com.gala.video.app.epg.home.ads.model.ExitAppAdModel;
import com.gala.video.app.epg.home.data.pingback.HomePingbackFactory;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.framework.core.utils.ThreadUtils;
import com.gala.video.lib.share.common.model.multiscreen.MultiScreenParams;
import com.gala.video.lib.share.ifimpl.ads.AdsClientUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.ads.IAdApi;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.pingback.HomePingbackType.CommonPingback;
import com.gala.video.lib.share.pingback.PingBackParams.Keys;
import com.mcto.ads.AdsClient;
import com.mcto.ads.CupidAd;
import com.mcto.ads.CupidAdSlot;
import com.mcto.ads.internal.net.PingbackConstants;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONException;

public class ExitAppDisplayAdInfoRequestTask {
    private static final long EXIT_AD_IMAGE_LIMIT_TIME_OUT = 800;
    private static final int EXIT_AD_IMAGE_MSG_FAILED = 111;
    private static final int EXIT_AD_IMAGE_MSG_NO_DATA = 112;
    private static final int EXIT_AD_IMAGE_MSG_SUCCESS = 110;
    private static final int EXIT_AD_IMAGE_MSG_TIME_OUT = 113;
    private static final String TAG = "ads/ExitAppDisplayAdInfoRequestTask";
    private volatile boolean isRequestSuccess;
    private AdsClient mAdsClient;
    private volatile Bitmap mCurrentAdBitmap;
    private volatile ExitAppAdModel mCurrentExitAppAdModle;
    private long mDownloadLimitTime;
    private IImageProvider mImageProvider;
    private OnExitAdRequestListener mOnExitAdRequestListener;
    private TimeHandler mTimeHandler;
    private long startTime;

    public interface OnExitAdRequestListener {
        void onFailed();

        void onNoAdData();

        void onSuccess(ExitAppAdModel exitAppAdModel, Bitmap bitmap);

        void onTimeOut();
    }

    @SuppressLint({"HandlerLeak"})
    private class TimeHandler extends Handler {
        private TimeHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 110:
                    LogUtils.d(ExitAppDisplayAdInfoRequestTask.TAG, "TimeHandler, fetch ad data success");
                    if (ExitAppDisplayAdInfoRequestTask.this.mOnExitAdRequestListener != null) {
                        ExitAppDisplayAdInfoRequestTask.this.mOnExitAdRequestListener.onSuccess(ExitAppDisplayAdInfoRequestTask.this.mCurrentExitAppAdModle, ExitAppDisplayAdInfoRequestTask.this.mCurrentAdBitmap);
                    }
                    HomePingbackFactory.instance().createPingback(CommonPingback.AD_DATA_REQUEST_PINGBACK).addItem(Keys.RI, "ad_exitapk").addItem("st", "1").addItem("td", String.valueOf(SystemClock.elapsedRealtime() - ExitAppDisplayAdInfoRequestTask.this.startTime)).setOthersNull().post();
                    return;
                case 111:
                    LogUtils.d(ExitAppDisplayAdInfoRequestTask.TAG, "TimeHandler, fetch ad data failed");
                    if (ExitAppDisplayAdInfoRequestTask.this.mOnExitAdRequestListener != null) {
                        ExitAppDisplayAdInfoRequestTask.this.mOnExitAdRequestListener.onFailed();
                    }
                    HomePingbackFactory.instance().createPingback(CommonPingback.AD_DATA_REQUEST_PINGBACK).addItem(Keys.RI, "ad_exitapk").addItem("st", MultiScreenParams.DLNA_PHONE_CONTROLL_ERROR).addItem("td", String.valueOf(SystemClock.elapsedRealtime() - ExitAppDisplayAdInfoRequestTask.this.startTime)).addItem(Keys.T, "11").addItem("ct", "150619_request").setOthersNull().post();
                    return;
                case 112:
                    LogUtils.d(ExitAppDisplayAdInfoRequestTask.TAG, "TimeHandler, no ad data");
                    if (ExitAppDisplayAdInfoRequestTask.this.mOnExitAdRequestListener != null) {
                        ExitAppDisplayAdInfoRequestTask.this.mOnExitAdRequestListener.onNoAdData();
                    }
                    HomePingbackFactory.instance().createPingback(CommonPingback.AD_DATA_REQUEST_PINGBACK).addItem(Keys.RI, "ad_exitapk").addItem("st", "0").addItem("td", String.valueOf(SystemClock.elapsedRealtime() - ExitAppDisplayAdInfoRequestTask.this.startTime)).addItem(Keys.T, "11").addItem("ct", "150619_request").setOthersNull().post();
                    return;
                case 113:
                    if (!ExitAppDisplayAdInfoRequestTask.this.isRequestSuccess) {
                        LogUtils.d(ExitAppDisplayAdInfoRequestTask.TAG, "TimeHandler, fetch exit ad time out");
                        if (ExitAppDisplayAdInfoRequestTask.this.mOnExitAdRequestListener != null) {
                            ExitAppDisplayAdInfoRequestTask.this.mOnExitAdRequestListener.onTimeOut();
                        }
                        HomePingbackFactory.instance().createPingback(CommonPingback.AD_DATA_REQUEST_PINGBACK).addItem(Keys.RI, "ad_exitapk").addItem("st", MultiScreenParams.DLNA_PHONE_CONTROLL_ERROR).addItem("td", String.valueOf(SystemClock.elapsedRealtime() - ExitAppDisplayAdInfoRequestTask.this.startTime)).addItem(Keys.T, "11").addItem("ct", "150619_request").setOthersNull().post();
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    }

    public ExitAppDisplayAdInfoRequestTask() {
        this.mImageProvider = ImageProviderApi.getImageProvider();
        this.isRequestSuccess = false;
        this.mTimeHandler = new TimeHandler(Looper.getMainLooper());
        this.mAdsClient = AdsClientUtils.getInstance();
        this.mDownloadLimitTime = EXIT_AD_IMAGE_LIMIT_TIME_OUT;
    }

    public ExitAppDisplayAdInfoRequestTask(long limitTime) {
        this.mImageProvider = ImageProviderApi.getImageProvider();
        this.isRequestSuccess = false;
        this.mTimeHandler = new TimeHandler(Looper.getMainLooper());
        this.mAdsClient = AdsClientUtils.getInstance();
        this.mDownloadLimitTime = limitTime;
    }

    public void execute() {
        LogUtils.d(TAG, "execute exit app ad download task");
        clear();
        this.mTimeHandler.sendEmptyMessageDelayed(113, this.mDownloadLimitTime);
        this.startTime = SystemClock.elapsedRealtime();
        ThreadUtils.execute(new Runnable() {
            public void run() {
                if (ExitAppDisplayAdInfoRequestTask.this.requestAndParseAdInfo()) {
                    ExitAppDisplayAdInfoRequestTask.this.downloadAdImage();
                } else if (ExitAppDisplayAdInfoRequestTask.this.mTimeHandler.hasMessages(113)) {
                    ExitAppDisplayAdInfoRequestTask.this.mTimeHandler.removeCallbacksAndMessages(null);
                    ExitAppDisplayAdInfoRequestTask.this.mTimeHandler.sendEmptyMessage(112);
                }
            }
        });
    }

    private void clear() {
        this.mCurrentAdBitmap = null;
        this.mCurrentExitAppAdModle = null;
    }

    public OnExitAdRequestListener getExitAdRequestListener() {
        return this.mOnExitAdRequestListener;
    }

    public void setOnExitAdRequestListener(OnExitAdRequestListener onExitAdRequestListener) {
        this.mOnExitAdRequestListener = onExitAdRequestListener;
    }

    private boolean requestAndParseAdInfo() {
        if (this.mAdsClient == null) {
            return false;
        }
        IAdApi iAdApi = GetInterfaceTools.getIAdApi();
        AdsClient adsClient = this.mAdsClient;
        CharSequence adJson = iAdApi.getExitAppDialogAds(AdsClient.getSDKVersion());
        if (!StringUtils.isEmpty(adJson)) {
            return parseAdJson(adJson, this.mAdsClient);
        }
        LogUtils.d(TAG, "requestAndParseAdInfo, adJson is empty");
        if (this.mTimeHandler.hasMessages(113)) {
            this.mTimeHandler.removeCallbacksAndMessages(null);
            this.mTimeHandler.sendEmptyMessage(112);
        }
        this.mAdsClient.onRequestMobileServerFailed();
        this.mAdsClient.sendAdPingBacks();
        return false;
    }

    private boolean parseAdJson(String adJson, AdsClient adsClient) {
        LogUtils.d(TAG, "parseAdJson...");
        if (adJson == null || adsClient == null) {
            return false;
        }
        try {
            Map<String, Object> passportMap = new HashMap();
            passportMap.put(PingbackConstants.PASSPORT_ID, GetInterfaceTools.getIGalaAccountManager().getUID());
            adsClient.setSdkStatus(passportMap);
            adsClient.onRequestMobileServerSucceededWithAdData(adJson, "", "qc_100001_100145");
            adsClient.flushCupidPingback();
            List<CupidAdSlot> slotList = adsClient.getSlotsByType(0);
            if (ListUtils.isEmpty((List) slotList)) {
                LogUtils.d(TAG, "parseAdJson, no slot with the type of SLOT_TYPE_PAGE");
                return false;
            }
            for (CupidAdSlot slot : slotList) {
                int mSlotId = slot.getSlotId();
                LogUtils.d(TAG, "parseAdJson, mSlotId = " + mSlotId);
                List<CupidAd> cupidAdList = adsClient.getAdSchedules(mSlotId);
                if (ListUtils.isEmpty((List) cupidAdList)) {
                    LogUtils.d(TAG, "parseAdJson, the CupidAd with " + mSlotId + " is empty.");
                } else {
                    for (CupidAd cupidAd : cupidAdList) {
                        if (cupidAd == null) {
                            LogUtils.d(TAG, "parseAdJson, CupidAd object is null.");
                        } else if ("exit".equals(cupidAd.getCreativeType())) {
                            Map exitAppAds = cupidAd.getCreativeObject();
                            if (ListUtils.isEmpty(exitAppAds)) {
                                return false;
                            }
                            Object imageUrlObj = exitAppAds.get("imgUrl");
                            Object needQrObj = exitAppAds.get("needQR");
                            Object qrTitleObj = exitAppAds.get("qrTitle");
                            Object qrDescObj = exitAppAds.get("qrDescription");
                            ExitAppAdModel model = new ExitAppAdModel();
                            model.setClickThroughType(cupidAd.getClickThroughType());
                            model.setmQrUrl(cupidAd.getClickThroughUrl());
                            model.setAdId(cupidAd.getAdId());
                            model.setAdImageUrl(imageUrlObj != null ? imageUrlObj.toString() : "");
                            model.setmNeedQr(needQrObj != null ? needQrObj.toString() : "");
                            model.setmQrTitle(qrTitleObj != null ? qrTitleObj.toString() : "");
                            model.setmQrDesc(qrDescObj != null ? qrDescObj.toString() : "");
                            GetInterfaceTools.getIAdProcessingUtils().parseAdRawData(cupidAd, model);
                            this.mCurrentExitAppAdModle = model;
                            LogUtils.d(TAG, "parseAdJson, exit app ad info = " + model);
                            return true;
                        }
                    }
                    continue;
                }
            }
            return false;
        } catch (JSONException e) {
            LogUtils.e(TAG, "parseAdJson, JSONException", e);
        } catch (Exception e2) {
            LogUtils.e(TAG, "parseAdJson, exception", e2);
        }
    }

    private void downloadAdImage() {
        LogUtils.d(TAG, "downloadAdImage");
        if (this.mCurrentExitAppAdModle != null) {
            this.mImageProvider.loadImage(new ImageRequest(this.mCurrentExitAppAdModle.getAdImageUrl()), new IImageCallback() {
                public void onSuccess(ImageRequest imageRequest, Bitmap bitmap) {
                    if (ExitAppDisplayAdInfoRequestTask.this.mTimeHandler.hasMessages(113)) {
                        ExitAppDisplayAdInfoRequestTask.this.mTimeHandler.removeCallbacksAndMessages(null);
                        LogUtils.d(ExitAppDisplayAdInfoRequestTask.TAG, "downloadAdImage success");
                        ExitAppDisplayAdInfoRequestTask.this.isRequestSuccess = true;
                        ExitAppDisplayAdInfoRequestTask.this.mCurrentAdBitmap = bitmap;
                        ExitAppDisplayAdInfoRequestTask.this.mTimeHandler.sendEmptyMessage(110);
                        return;
                    }
                    LogUtils.d(ExitAppDisplayAdInfoRequestTask.TAG, "downloadAdImage success time out ");
                }

                public void onFailure(ImageRequest imageRequest, Exception e) {
                    if (ExitAppDisplayAdInfoRequestTask.this.mTimeHandler.hasMessages(113)) {
                        ExitAppDisplayAdInfoRequestTask.this.mTimeHandler.removeCallbacksAndMessages(null);
                        LogUtils.d(ExitAppDisplayAdInfoRequestTask.TAG, "downloadAdImage failed");
                        ExitAppDisplayAdInfoRequestTask.this.isRequestSuccess = true;
                        ExitAppDisplayAdInfoRequestTask.this.mTimeHandler.sendEmptyMessage(111);
                        return;
                    }
                    LogUtils.d(ExitAppDisplayAdInfoRequestTask.TAG, "downloadAdImage failed time out ");
                }
            });
        }
    }
}
