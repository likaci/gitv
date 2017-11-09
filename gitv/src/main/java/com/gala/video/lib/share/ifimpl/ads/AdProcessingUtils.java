package com.gala.video.lib.share.ifimpl.ads;

import android.content.Context;
import android.content.Intent;
import com.gala.download.DownloaderAPI;
import com.gala.download.base.FileRequest;
import com.gala.download.base.IFileCallback;
import com.gala.download.base.IGifCallback;
import com.gala.sdk.player.PlayParams;
import com.gala.sdk.player.SourceType;
import com.gala.tvapi.tv2.model.Album;
import com.gala.tvapi.vrs.model.ChannelCarousel;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.common.model.player.BasePlayParamBuilder;
import com.gala.video.lib.share.common.model.player.CarouselPlayParamBuilder;
import com.gala.video.lib.share.common.widget.QToast;
import com.gala.video.lib.share.ifimpl.interaction.ActionSet;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.ads.AdsConstants.AdClickType;
import com.gala.video.lib.share.ifmanager.bussnessIF.ads.IAdProcessingUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.ads.IAdProcessingUtils.Wrapper;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.web.model.WebIntentParams;
import com.gala.video.lib.share.ifmanager.bussnessIF.screensaver.model.CupidAdModel;
import com.gala.video.lib.share.pingback.HomeAdPingbackModel;
import com.gala.video.lib.share.project.Project;
import com.gala.video.lib.share.utils.PageIOUtils;
import com.mcto.ads.CupidAd;
import com.mcto.ads.constants.ClickThroughType;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import pl.droidsonroids.gif.GifDrawable;

public class AdProcessingUtils extends Wrapper implements IAdProcessingUtils {
    private static final String TAG = "ifimpl/ads/AdProcessingUtils";

    public void onClickAd(Context context, CupidAdModel cupidAdModel, HomeAdPingbackModel pingbackModel) {
        if (cupidAdModel == null) {
            LogUtils.e(TAG, "onClickAd, cupid ad model is null!!!");
            return;
        }
        if (pingbackModel == null) {
            LogUtils.w(TAG, "onClickAd, home ad pingback model is null");
        }
        AdClickType adClickType = cupidAdModel.getAdClickType();
        if (adClickType == null) {
            LogUtils.e(TAG, "onClickAd, ad click type is null!!!");
            onClickForNotOpenAdData(context);
            return;
        }
        try {
            WebIntentParams params;
            switch (adClickType) {
                case DEFAULT:
                    CharSequence defaultClickInfo = cupidAdModel.getDefault();
                    if (StringUtils.isEmpty(defaultClickInfo)) {
                        onClickForNotOpenAdData(context);
                        return;
                    }
                    params = new WebIntentParams();
                    params.pageUrl = defaultClickInfo;
                    params.enterType = pingbackModel != null ? pingbackModel.getH5EnterType() : HomeAdPingbackModel.DEFAULT_H5_ENTER_TYPE;
                    params.from = pingbackModel != null ? pingbackModel.getH5From() : "";
                    params.tabSrc = pingbackModel != null ? pingbackModel.getH5TabSrc() : "";
                    GetInterfaceTools.getWebEntry().gotoCommonWebActivity(context, params);
                    return;
                case H5:
                    CharSequence h5Url = cupidAdModel.getH5Url();
                    if (StringUtils.isEmpty(h5Url)) {
                        onClickForNotOpenAdData(context);
                        return;
                    }
                    params = new WebIntentParams();
                    params.pageUrl = h5Url;
                    params.enterType = pingbackModel != null ? pingbackModel.getH5EnterType() : HomeAdPingbackModel.DEFAULT_H5_ENTER_TYPE;
                    params.from = pingbackModel != null ? pingbackModel.getH5From() : "";
                    params.tabSrc = pingbackModel != null ? pingbackModel.getH5TabSrc() : "";
                    GetInterfaceTools.getWebEntry().gotoCommonWebActivity(context, params);
                    return;
                case IMAGE:
                    CharSequence imgUrl = cupidAdModel.getJumpingShowImageUrl();
                    if (StringUtils.isEmpty(imgUrl)) {
                        onClickForNotOpenAdData(context);
                        return;
                    }
                    CharSequence localImagePath = DownloaderAPI.getDownloader().getLocalPath(new FileRequest(imgUrl));
                    LogUtils.w(TAG, "onClickAd, local image path, " + localImagePath);
                    if (StringUtils.isEmpty(localImagePath)) {
                        onClickForNotOpenAdData(context);
                        return;
                    }
                    Intent intent = new Intent(ActionSet.ACT_AD_IMAGE_SHOW);
                    intent.putExtra("adimageUrl", imgUrl);
                    PageIOUtils.activityIn(context, intent);
                    return;
                case PLAY_LIST:
                    params = new WebIntentParams();
                    params.id = cupidAdModel.getPlId();
                    params.from = pingbackModel != null ? pingbackModel.getPlFrom() : "";
                    params.tabSrc = pingbackModel != null ? pingbackModel.getPlTabSrc() : "";
                    GetInterfaceTools.getWebEntry().gotoSubject(context, params);
                    return;
                case VIDEO:
                    Album albumInfo = new Album();
                    albumInfo.qpId = cupidAdModel.getAlbumId();
                    albumInfo.tvQid = cupidAdModel.getTvId();
                    BasePlayParamBuilder builder = new BasePlayParamBuilder();
                    PlayParams playParam = new PlayParams();
                    playParam.sourceType = SourceType.OUTSIDE;
                    playParam.isHomeAd = true;
                    builder.setPlayParams(playParam);
                    builder.setAlbumInfo(albumInfo);
                    builder.setPlayOrder(0);
                    builder.setClearTaskFlag(false);
                    builder.setFrom(pingbackModel != null ? pingbackModel.getVideoFrom() : "");
                    builder.setBuySource(pingbackModel != null ? pingbackModel.getVideoBuySource() : "");
                    builder.setTabSource(pingbackModel != null ? pingbackModel.getVideoTabSource() : "");
                    GetInterfaceTools.getPlayerPageProvider().startBasePlayerPage(context, builder);
                    return;
                case CAROUSEL:
                    ChannelCarousel channelCarousel = new ChannelCarousel();
                    channelCarousel.tableNo = Long.parseLong(cupidAdModel.getCarouselNo());
                    channelCarousel.id = Long.parseLong(cupidAdModel.getCarouselId());
                    channelCarousel.name = cupidAdModel.getCarouselName();
                    LogUtils.d(TAG, "onClickAd, channelCarousel: id = " + channelCarousel.id + ", tableNo = " + channelCarousel.tableNo + ", name = " + channelCarousel.name);
                    CarouselPlayParamBuilder carouselPlayParamBuilder = new CarouselPlayParamBuilder();
                    carouselPlayParamBuilder.mChannelCarousel = channelCarousel;
                    carouselPlayParamBuilder.setFrom(pingbackModel != null ? pingbackModel.getCarouselFrom() : "");
                    carouselPlayParamBuilder.setTabSource(pingbackModel != null ? pingbackModel.getCarouselTabSource() : "");
                    GetInterfaceTools.getPlayerPageProvider().startCarouselPlayerPage(context, carouselPlayParamBuilder);
                    return;
                case VIDEO_ILLEGAL:
                    LogUtils.w(TAG, "onClickAd, video illegal");
                    onClickForNotOpenAdData(context);
                    return;
                case CAROUSEL_ILLEGAL:
                    LogUtils.w(TAG, "onClickAd, carousel illegal");
                    onClickForNotOpenAdData(context);
                    return;
                default:
                    LogUtils.w(TAG, "onClickAd, not support ClickThroughType, ClickThroughType = " + cupidAdModel.getClickThroughType() + " AdClickType = " + cupidAdModel.getAdClickType() + " ClickThroughInfo  = " + cupidAdModel.getClickThroughInfo());
                    onClickForNotOpenAdData(context);
                    return;
            }
        } catch (NumberFormatException e) {
            LogUtils.e(TAG, "onClickAd, NumberFormatException: " + e);
        } catch (Exception e2) {
            LogUtils.e(TAG, "onClickAd, exception: " + e2);
        }
    }

    public void parseAdRawData(CupidAd cupidAdRaw, CupidAdModel cupidAdModel) {
        if (cupidAdRaw == null) {
            LogUtils.w(TAG, "parseAdRawData, raw ad data is null");
        } else if (cupidAdModel == null) {
            LogUtils.w(TAG, "parseAdRawData, CupidAdModel is null");
        } else {
            ClickThroughType clickThroughType = cupidAdRaw.getClickThroughType();
            CharSequence clickInfo = cupidAdRaw.getClickThroughUrl();
            if (clickThroughType == null) {
                LogUtils.w(TAG, "parseAdRawData, ad click type is null");
            } else if (StringUtils.isEmpty(clickInfo)) {
                LogUtils.w(TAG, "parseAdRawData, ad click info is null");
            } else {
                cupidAdModel.setClickThroughType(clickThroughType);
                cupidAdModel.setClickThroughInfo(clickInfo);
                switch (clickThroughType) {
                    case DEFAULT:
                        cupidAdModel.setAdClickType(AdClickType.DEFAULT);
                        cupidAdModel.setDefault(clickInfo);
                        return;
                    case WEBVIEW:
                        cupidAdModel.setAdClickType(AdClickType.H5);
                        cupidAdModel.setH5Url(clickInfo);
                        return;
                    case IMAGE:
                        cupidAdModel.setAdClickType(AdClickType.IMAGE);
                        cupidAdModel.setJumpingShowImageUrl(clickInfo);
                        downloadImage(clickInfo);
                        return;
                    case VIDEO:
                        Matcher plidMatcher = Pattern.compile("//plid=(.*)").matcher(clickInfo);
                        boolean isFindPlid = plidMatcher.find();
                        Matcher videoMatcher = Pattern.compile("//albumId=(.*)&tvId=(.*)").matcher(clickInfo);
                        boolean isFindVideo = videoMatcher.find();
                        if (isFindPlid) {
                            cupidAdModel.setPlId(plidMatcher.group(1));
                            cupidAdModel.setAdClickType(AdClickType.PLAY_LIST);
                            return;
                        } else if (isFindVideo) {
                            String albumId = videoMatcher.group(1);
                            String tvId = videoMatcher.group(2);
                            cupidAdModel.setAlbumId(albumId);
                            cupidAdModel.setTvId(tvId);
                            cupidAdModel.setAdClickType(AdClickType.VIDEO);
                            return;
                        } else {
                            LogUtils.w(TAG, "clickThroughType is " + ClickThroughType.VIDEO + "(play a video in the APP)" + " but the jumping info format is illegal, " + "info : " + clickInfo);
                            cupidAdModel.setAdClickType(AdClickType.VIDEO_ILLEGAL);
                            return;
                        }
                    case CAROUSEL_STATION:
                        if (Project.getInstance().getControl().isOpenCarousel()) {
                            Matcher carouselMatcher = Pattern.compile("//carouselId=(.*)&carouselNo=(.*)&carouselName=(.*)").matcher(clickInfo);
                            if (carouselMatcher.find()) {
                                String carouselId = carouselMatcher.group(1);
                                String carouselNo = carouselMatcher.group(2);
                                String carouselName = carouselMatcher.group(3);
                                cupidAdModel.setCarouselId(carouselId);
                                cupidAdModel.setCarouselNo(carouselNo);
                                cupidAdModel.setCarouselName(carouselName);
                                cupidAdModel.setAdClickType(AdClickType.CAROUSEL);
                                return;
                            }
                            LogUtils.w(TAG, "clickThroughType is " + ClickThroughType.CAROUSEL_STATION + " but the jumping info format is illegal, info : " + clickInfo);
                            cupidAdModel.setAdClickType(AdClickType.CAROUSEL_ILLEGAL);
                            return;
                        }
                        LogUtils.d(TAG, "Dynamic interface , not support carousel, filter carousel ad");
                        return;
                    default:
                        LogUtils.w(TAG, "can not parse ClickThroughType: " + clickThroughType + " value No. is " + clickThroughType.value());
                        return;
                }
            }
        }
    }

    public void downloadImage(String imageUrl) {
        if (!StringUtils.isEmpty((CharSequence) imageUrl)) {
            FileRequest fileRequest = new FileRequest(imageUrl);
            CharSequence localImagePath = DownloaderAPI.getDownloader().getLocalPath(fileRequest);
            LogUtils.w(TAG, "downloadImage, local image path, " + localImagePath);
            if (StringUtils.isEmpty(localImagePath)) {
                String suffixName = "";
                if (StringUtils.getLength(imageUrl) > 4) {
                    suffixName = imageUrl.substring(imageUrl.length() - 4, imageUrl.length());
                }
                if (".gif".equals(suffixName)) {
                    DownloaderAPI.getDownloader().loadGif(fileRequest, new IGifCallback() {
                        public void onSuccess(FileRequest fileRequest, GifDrawable gifDrawable) {
                            LogUtils.d(AdProcessingUtils.TAG, "downloadImage, download ad  GIF image(show in the second page after clicking Ad) success");
                        }

                        public void onFailure(FileRequest fileRequest, Exception e) {
                            LogUtils.w(AdProcessingUtils.TAG, "downloadImage, download ad GIF image(show in the second page after clicking Ad) failed,  url = " + fileRequest.getUrl());
                        }
                    });
                } else {
                    DownloaderAPI.getDownloader().loadFile(fileRequest, new IFileCallback() {
                        public void onSuccess(FileRequest fileRequest, String s) {
                            LogUtils.d(AdProcessingUtils.TAG, "downloadImage, download ad image(show in the second page after clicking Ad) success");
                        }

                        public void onFailure(FileRequest fileRequest, Exception e) {
                            LogUtils.w(AdProcessingUtils.TAG, "downloadImage, download ad image(show in the second page after clicking Ad) failed,  url = " + fileRequest.getUrl());
                        }
                    });
                }
            }
        }
    }

    public void onClickForNotOpenAdData(Context context) {
        QToast.makeTextAndShow(context, (CharSequence) "抱歉，暂无法打开此内容~", 3000);
    }
}
