package com.gala.video.app.epg.home.ads.task;

import android.os.SystemClock;
import com.alibaba.fastjson.JSONException;
import com.gala.download.DownloaderAPI;
import com.gala.download.base.FileRequest;
import com.gala.download.base.IDownloader;
import com.gala.download.base.IFileCallback;
import com.gala.video.app.epg.home.ads.controller.HomeFocusImageAdProvider;
import com.gala.video.app.epg.home.data.pingback.HomePingbackFactory;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.framework.core.utils.ThreadUtils;
import com.gala.video.lib.share.common.model.multiscreen.MultiScreenParams;
import com.gala.video.lib.share.ifimpl.ads.AdsClientUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.ads.AdsConstants;
import com.gala.video.lib.share.ifmanager.bussnessIF.ads.AdsConstants.AdClickType;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.HomeFocusImageAdModel;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.ItemDataType;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.WidgetChangeStatus;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.WidgetType;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.homefocusimagead.IHomeFocusImageAdTask.Wrapper;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.pingback.HomePingbackType.CommonPingback;
import com.gala.video.lib.share.pingback.PingBackParams.Keys;
import com.gala.video.lib.share.project.Project;
import com.mcto.ads.AdsClient;
import com.mcto.ads.CupidAd;
import com.mcto.ads.CupidAdSlot;
import com.mcto.ads.constants.ClickThroughType;
import com.mcto.ads.internal.net.PingbackConstants;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HomeFocusImageAdRequestTask extends Wrapper {
    private static final int MSG_REQUEST_AD = 100;
    private static final String TAG = "ads/HomeFocusImageAdRequestTask";
    private int mAdCount;
    private volatile int mAdImageLoadedCount;
    private List<HomeFocusImageAdModel> mAdInfoList;
    private List<HomeFocusImageAdModel> mAdResultList;
    private AdsClient mAdsClient;
    private IDownloader mDownloader;
    private long mStartTime;

    class C05831 implements Runnable {
        C05831() {
        }

        public void run() {
            HomeFocusImageAdRequestTask.this.reset();
            HomeFocusImageAdRequestTask.this.requestAndParseAdInfo();
            HomeFocusImageAdRequestTask.this.downloadAdFocusItemImage(HomeFocusImageAdRequestTask.this.mAdInfoList);
        }
    }

    private static class SingletonHelper {
        private static HomeFocusImageAdRequestTask instance = new HomeFocusImageAdRequestTask();

        private SingletonHelper() {
        }
    }

    static /* synthetic */ int access$706(HomeFocusImageAdRequestTask x0) {
        int i = x0.mAdImageLoadedCount - 1;
        x0.mAdImageLoadedCount = i;
        return i;
    }

    private HomeFocusImageAdRequestTask() {
        this.mAdInfoList = new ArrayList();
        this.mAdResultList = new ArrayList();
        this.mAdCount = 0;
        this.mAdImageLoadedCount = 0;
        this.mDownloader = DownloaderAPI.getDownloader();
        this.mAdsClient = AdsClientUtils.getInstance();
    }

    public static HomeFocusImageAdRequestTask getInstance() {
        return SingletonHelper.instance;
    }

    public void execute() {
        ThreadUtils.execute(new C05831());
    }

    private synchronized void requestAndParseAdInfo() {
        if (this.mAdsClient != null) {
            long startTime = SystemClock.elapsedRealtime();
            String adJson = "";
            try {
                parseAdJson(GetInterfaceTools.getIAdApi().getHomeFocusImageAds(AdsClient.getSDKVersion()), this.mAdsClient);
                HomePingbackFactory.instance().createPingback(CommonPingback.FOCUS_AD_DATA_REQUEST_PINGBACK).addItem(Keys.RI, "ad_focus").addItem("td", String.valueOf(SystemClock.elapsedRealtime() - startTime)).addItem("st", this.mAdCount > 0 ? "1" : "0").addItem(Keys.f2035T, "11").addItem("ct", "150619_request").setOthersNull().post();
            } catch (Exception e) {
                HomePingbackFactory.instance().createPingback(CommonPingback.FOCUS_AD_DATA_REQUEST_PINGBACK).addItem(Keys.RI, "ad_focus").addItem("td", String.valueOf(SystemClock.elapsedRealtime() - startTime)).addItem("st", MultiScreenParams.DLNA_PHONE_CONTROLL_ERROR).addItem(Keys.f2035T, "11").addItem("ct", "150619_request").setOthersNull().post();
                this.mAdsClient.onRequestMobileServerFailed();
                this.mAdsClient.sendAdPingBacks();
            }
        }
    }

    private synchronized void reset() {
        this.mAdInfoList.clear();
        this.mAdResultList.clear();
        this.mAdImageLoadedCount = 0;
        this.mAdCount = 0;
    }

    private void parseAdJson(String adJson, AdsClient adsClient) {
        LogUtils.m1568d(TAG, "parseAdJson...");
        try {
            if (!StringUtils.isEmpty((CharSequence) adJson) && adsClient != null) {
                Map<String, Object> passportMap = new HashMap();
                passportMap.put(PingbackConstants.PASSPORT_ID, GetInterfaceTools.getIGalaAccountManager().getUID());
                adsClient.setSdkStatus(passportMap);
                adsClient.onRequestMobileServerSucceededWithAdData(adJson, "", "qc_100001_100145");
                adsClient.flushCupidPingback();
                List<CupidAdSlot> slotList = adsClient.getSlotsByType(0);
                if (ListUtils.isEmpty((List) slotList)) {
                    LogUtils.m1577w(TAG, "parseAdJson, no slot with the type of SLOT_TYPE_PAGE");
                    return;
                }
                for (CupidAdSlot slot : slotList) {
                    int mSlotId = slot.getSlotId();
                    LogUtils.m1568d(TAG, "parseAdJson, mSlotId = " + mSlotId);
                    List<CupidAd> cupidAdList = adsClient.getAdSchedules(mSlotId);
                    if (ListUtils.isEmpty((List) cupidAdList)) {
                        LogUtils.m1577w(TAG, "parseAdJson, the list of CupidAd gotten by SloId : " + mSlotId + " is empty.");
                    } else {
                        for (CupidAd cupidAd : cupidAdList) {
                            if (cupidAd == null) {
                                LogUtils.m1577w(TAG, "parseAdJson, CupidAd object is null.");
                            } else if (!"image".equals(cupidAd.getCreativeType())) {
                                continue;
                            } else if (this.mAdCount < 10) {
                                Map ads = cupidAd.getCreativeObject();
                                if (!ListUtils.isEmpty(ads)) {
                                    LogUtils.m1568d(TAG, "click info = " + cupidAd.getClickThroughUrl() + " click type = " + cupidAd.getClickThroughType());
                                    Object titleObj = ads.get("title");
                                    Object imageUrlObj = ads.get("imgUrl");
                                    Object isNeedBadgeObj = ads.get("needAdBadge");
                                    Object heightObj = ads.get("height");
                                    Object widthObj = ads.get("width");
                                    HomeFocusImageAdModel model = new HomeFocusImageAdModel();
                                    model.setAdId(cupidAd.getAdId());
                                    model.setClickThroughInfo(cupidAd.getClickThroughUrl());
                                    model.setClickThroughType(cupidAd.getClickThroughType());
                                    model.setTitle(titleObj != null ? titleObj.toString() : "");
                                    model.setImageUrl(imageUrlObj != null ? imageUrlObj.toString() : "");
                                    model.setNeedAdBadge(isNeedBadgeObj != null ? isNeedBadgeObj.toString() : "");
                                    model.setHeight(heightObj != null ? Integer.valueOf(heightObj.toString()).intValue() : AdsConstants.HOME_FOCUS_IMAGE_AD_HEIGHT_DEFAULT);
                                    model.setWidth(widthObj != null ? Integer.valueOf(widthObj.toString()).intValue() : AdsConstants.HOME_FOCUS_IMAGE_AD_WIDTH_DEFAULT);
                                    model.setWidgetType(WidgetType.ITEM_FOCUS_IMAGE_AD);
                                    model.setItemType(ItemDataType.FOCUS_IMAGE_AD);
                                    model.setWidgetChangeStatus(WidgetChangeStatus.InitChange);
                                    ClickThroughType clickThroughType = cupidAd.getClickThroughType();
                                    if (clickThroughType == null) {
                                        LogUtils.m1577w(TAG, "parseAdJson, clickThroughType is null.");
                                    } else {
                                        String clickInfo = cupidAd.getClickThroughUrl();
                                        switch (clickThroughType) {
                                            case WEBVIEW:
                                            case DEFAULT:
                                                model.setAdClickType(AdClickType.H5);
                                                break;
                                            case IMAGE:
                                                model.setAdClickType(AdClickType.IMAGE);
                                                GetInterfaceTools.getIAdProcessingUtils().downloadImage(clickInfo);
                                                break;
                                            case VIDEO:
                                                Matcher plidMatcher = Pattern.compile("//plid=(.*)").matcher(clickInfo);
                                                boolean isFindPlid = plidMatcher.find();
                                                Matcher videoMatcher = Pattern.compile("//albumId=(.*)&tvId=(.*)").matcher(clickInfo);
                                                boolean isFindVideo = videoMatcher.find();
                                                if (!isFindPlid) {
                                                    if (!isFindVideo) {
                                                        LogUtils.m1577w(TAG, "clickThroughType is " + ClickThroughType.VIDEO + "(play a video in the APP)" + "but the info of jumping is illegal, " + "info : " + clickInfo);
                                                        model.setAdClickType(AdClickType.VIDEO_ILLEGAL);
                                                        break;
                                                    }
                                                    String albumId = videoMatcher.group(1);
                                                    String tvId = videoMatcher.group(2);
                                                    model.setAlbumId(albumId);
                                                    model.setTvId(tvId);
                                                    model.setAdClickType(AdClickType.VIDEO);
                                                    break;
                                                }
                                                model.setPlId(plidMatcher.group(1));
                                                model.setAdClickType(AdClickType.PLAY_LIST);
                                                break;
                                            case CAROUSEL_STATION:
                                                if (!Project.getInstance().getControl().isOpenCarousel()) {
                                                    LogUtils.m1568d(TAG, "Dynamic interface , not support carousel, filter carousel ad");
                                                    break;
                                                }
                                                Matcher carouselMatcher = Pattern.compile("//carouselId=(.*)&carouselNo=(.*)&carouselName=(.*)").matcher(clickInfo);
                                                if (!carouselMatcher.find()) {
                                                    LogUtils.m1577w(TAG, "clickThroughType is " + ClickThroughType.CAROUSEL_STATION + " but the info of jumping is illegal, info : " + clickInfo);
                                                    model.setAdClickType(AdClickType.CAROUSEL_ILLEGAL);
                                                    break;
                                                }
                                                String carouselId = carouselMatcher.group(1);
                                                String carouselNo = carouselMatcher.group(2);
                                                String carouselName = carouselMatcher.group(3);
                                                model.setCarouselId(carouselId);
                                                model.setCarouselNo(carouselNo);
                                                model.setCarouselName(carouselName);
                                                model.setAdClickType(AdClickType.CAROUSEL);
                                                break;
                                            default:
                                                LogUtils.m1577w(TAG, "can not parse ClickThroughType: " + clickThroughType + " value No. is " + clickThroughType.value());
                                                break;
                                        }
                                        this.mAdCount++;
                                        model.setAdIndex(this.mAdCount);
                                        this.mAdInfoList.add(model);
                                    }
                                }
                            } else {
                                return;
                            }
                        }
                        continue;
                    }
                }
            }
        } catch (JSONException e) {
            LogUtils.m1572e(TAG, "parseAdJson, JSONException", e);
        } catch (NumberFormatException e2) {
            LogUtils.m1572e(TAG, "parseAdJson, NumberFormatException", e2);
        } catch (Exception e3) {
            LogUtils.m1572e(TAG, "parseAdJson, exception", e3);
        }
    }

    private synchronized void downloadAdFocusItemImage(List<HomeFocusImageAdModel> models) {
        if (!ListUtils.isEmpty((List) models)) {
            this.mStartTime = SystemClock.elapsedRealtime();
            this.mAdImageLoadedCount = models.size();
            for (final HomeFocusImageAdModel model : models) {
                FileRequest imageRequest = new FileRequest(model.getImageUrl());
                imageRequest.setShouldBeKilled(false);
                this.mDownloader.loadFile(imageRequest, new IFileCallback() {
                    public void onSuccess(FileRequest imageRequest, String s) {
                        HomeFocusImageAdRequestTask.this.mAdResultList.add(model);
                        if (HomeFocusImageAdRequestTask.access$706(HomeFocusImageAdRequestTask.this) == 0) {
                            LogUtils.m1568d(HomeFocusImageAdRequestTask.TAG, "download focus ad item images finished ");
                            HomePingbackFactory.instance().createPingback(CommonPingback.LOAD_FINISHED_PINGBACK).addItem(Keys.LDTYPE, "ad_focus").addItem("td", String.valueOf(SystemClock.elapsedRealtime() - HomeFocusImageAdRequestTask.this.mStartTime)).addItem(Keys.f2035T, "11").addItem("ct", "160602_load").setOthersNull().post();
                            HomeFocusImageAdProvider.getInstance().notifyAdData(HomeFocusImageAdRequestTask.this.mAdResultList);
                        }
                    }

                    public void onFailure(FileRequest imageRequest, Exception e) {
                        LogUtils.m1578w(HomeFocusImageAdRequestTask.TAG, "downloadAdFocusItemImage, IFileCallback--- onFailure, imageUrl = " + imageRequest.getUrl(), e);
                        if (HomeFocusImageAdRequestTask.access$706(HomeFocusImageAdRequestTask.this) == 0) {
                            LogUtils.m1568d(HomeFocusImageAdRequestTask.TAG, "download focus ad item images finished ");
                            HomePingbackFactory.instance().createPingback(CommonPingback.LOAD_FINISHED_PINGBACK).addItem(Keys.LDTYPE, "ad_focus").addItem("td", String.valueOf(SystemClock.elapsedRealtime() - HomeFocusImageAdRequestTask.this.mStartTime)).addItem(Keys.f2035T, "11").addItem("ct", "160602_load").setOthersNull().post();
                            HomeFocusImageAdProvider.getInstance().notifyAdData(HomeFocusImageAdRequestTask.this.mAdResultList);
                        }
                    }
                });
            }
        }
    }
}
