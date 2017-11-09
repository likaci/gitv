package com.gala.video.lib.share.ifimpl.dynamic;

import android.annotation.SuppressLint;
import com.gala.download.base.FileRequest;
import com.gala.tvapi.tv2.TVApiBase;
import com.gala.tvapi.tv2.model.Cnf;
import com.gala.tvapi.tv2.model.DeviceCheck;
import com.gala.tvapi.tv2.property.TVApiProperty;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.ifimpl.dynamic.DynaimicImageDownLoadTask.IDynamicDownLoadCallback;
import com.gala.video.lib.share.ifmanager.bussnessIF.dynamic.IDynamicQDataProvider;
import com.gala.video.lib.share.ifmanager.bussnessIF.dynamic.IDynamicQDataProvider.ILoadDynamicDataCallback;
import com.gala.video.lib.share.ifmanager.bussnessIF.dynamic.IDynamicQDataProvider.Wrapper;
import com.gala.video.lib.share.ifmanager.bussnessIF.dynamic.IDynamicResult;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

class DynamicQDataProvider extends Wrapper implements IDynamicQDataProvider {
    private static final String TAG = "DynamicQDataProvider";
    private final int MAX_IMAGE_COUNT = 10;
    @SuppressLint({"UseSparseArrays"})
    private final Map<Integer, String> mDynamicImageUrlsMap = new HashMap();

    DynamicQDataProvider() {
        long start = System.currentTimeMillis();
        DynamicResult result = new DynamicResult();
        result.getSerializableData();
        LogUtils.d(TAG, "read cost : " + (System.currentTimeMillis() - start) + "ms");
        this.mDynamicImageUrlsMap.put(Integer.valueOf(0), result.getStartLoading());
        this.mDynamicImageUrlsMap.put(Integer.valueOf(1), result.getPlayLoading());
        this.mDynamicImageUrlsMap.put(Integer.valueOf(2), result.getBootUrlString());
        this.mDynamicImageUrlsMap.put(Integer.valueOf(24), result.getStartUrl());
        this.mDynamicImageUrlsMap.put(Integer.valueOf(25), result.getStartUrl());
        this.mDynamicImageUrlsMap.put(Integer.valueOf(3), result.getHeadUrl());
        this.mDynamicImageUrlsMap.put(Integer.valueOf(23), result.getHeadLogoUrl());
        this.mDynamicImageUrlsMap.put(Integer.valueOf(4), result.getDefUrlString());
        this.mDynamicImageUrlsMap.put(Integer.valueOf(5), result.getPlayUrlString());
        this.mDynamicImageUrlsMap.put(Integer.valueOf(6), result.getServUrlString());
        this.mDynamicImageUrlsMap.put(Integer.valueOf(7), result.getWaterUrlString());
        this.mDynamicImageUrlsMap.put(Integer.valueOf(8), result.getISeeUrlString());
        this.mDynamicImageUrlsMap.put(Integer.valueOf(9), result.getPlayerLogoString());
        this.mDynamicImageUrlsMap.put(Integer.valueOf(10), result.getPlayerBackColour());
        this.mDynamicImageUrlsMap.put(Integer.valueOf(11), result.getCodeUrlString());
        this.mDynamicImageUrlsMap.put(Integer.valueOf(12), result.getVideoSourceUrlString());
        this.mDynamicImageUrlsMap.put(Integer.valueOf(13), result.getJstvString());
        this.mDynamicImageUrlsMap.put(Integer.valueOf(15), result.getPlayNewUrlString());
        this.mDynamicImageUrlsMap.put(Integer.valueOf(14), result.getPpsUrlString());
        this.mDynamicImageUrlsMap.put(Integer.valueOf(16), result.getVipHeadUrlString());
        this.mDynamicImageUrlsMap.put(Integer.valueOf(17), result.getDefaultCarouselUrlString());
        this.mDynamicImageUrlsMap.put(Integer.valueOf(18), result.getPlayerBackColourUrlString());
        this.mDynamicImageUrlsMap.put(Integer.valueOf(19), result.getPurchaseGuideTipUrlString());
        this.mDynamicImageUrlsMap.put(Integer.valueOf(20), result.getLivePurchaseGuideTipImageUrlString());
        this.mDynamicImageUrlsMap.put(Integer.valueOf(21), result.getScreenWeChatInteractive());
        this.mDynamicImageUrlsMap.put(Integer.valueOf(26), result.getHdrGuideBgImgUrls());
        this.mDynamicImageUrlsMap.put(Integer.valueOf(27), result.get4kGuideBgImgUrls());
        this.mDynamicImageUrlsMap.put(Integer.valueOf(28), result.get1080pGuideBgImgUrls());
        this.mDynamicImageUrlsMap.put(Integer.valueOf(29), result.getVipMonthOperateImageUrls());
        this.mDynamicImageUrlsMap.put(Integer.valueOf(30), result.getDetailFreeVideoOperateImageUrls());
    }

    private void prepareForDownLoad(Cnf cnfData) {
        LogUtils.d(TAG, "prepareForDownLoad...");
        DynamicResult result = new DynamicResult();
        this.mDynamicImageUrlsMap.clear();
        this.mDynamicImageUrlsMap.put(Integer.valueOf(0), cnfData.startLoading);
        this.mDynamicImageUrlsMap.put(Integer.valueOf(1), cnfData.playLoading);
        this.mDynamicImageUrlsMap.put(Integer.valueOf(2), cnfData.bootUrl);
        this.mDynamicImageUrlsMap.put(Integer.valueOf(24), cnfData.startUrl);
        this.mDynamicImageUrlsMap.put(Integer.valueOf(25), cnfData.bug_vip_tip_pic);
        this.mDynamicImageUrlsMap.put(Integer.valueOf(3), cnfData.headUrl);
        this.mDynamicImageUrlsMap.put(Integer.valueOf(23), cnfData.headLogoUrl);
        this.mDynamicImageUrlsMap.put(Integer.valueOf(4), cnfData.defUrl);
        this.mDynamicImageUrlsMap.put(Integer.valueOf(5), cnfData.playUrl);
        this.mDynamicImageUrlsMap.put(Integer.valueOf(6), cnfData.servUrl);
        this.mDynamicImageUrlsMap.put(Integer.valueOf(7), cnfData.waterUrl);
        this.mDynamicImageUrlsMap.put(Integer.valueOf(8), cnfData.iseeUrl);
        this.mDynamicImageUrlsMap.put(Integer.valueOf(9), cnfData.playerLogo);
        this.mDynamicImageUrlsMap.put(Integer.valueOf(10), cnfData.playerBackColour);
        this.mDynamicImageUrlsMap.put(Integer.valueOf(11), cnfData.codeURL);
        this.mDynamicImageUrlsMap.put(Integer.valueOf(12), cnfData.videoSourceUrl);
        this.mDynamicImageUrlsMap.put(Integer.valueOf(13), cnfData.jstvUrl);
        this.mDynamicImageUrlsMap.put(Integer.valueOf(15), cnfData.playNewUrl);
        this.mDynamicImageUrlsMap.put(Integer.valueOf(14), cnfData.ppsUrl);
        this.mDynamicImageUrlsMap.put(Integer.valueOf(16), cnfData.vipHead);
        this.mDynamicImageUrlsMap.put(Integer.valueOf(17), cnfData.carouselDefaultPicSmall);
        this.mDynamicImageUrlsMap.put(Integer.valueOf(18), cnfData.playerBackColourUrl);
        this.mDynamicImageUrlsMap.put(Integer.valueOf(19), cnfData.purchase_guide_tip_image);
        this.mDynamicImageUrlsMap.put(Integer.valueOf(20), cnfData.live_purchase_guide_tip_image);
        this.mDynamicImageUrlsMap.put(Integer.valueOf(21), cnfData.screenWeChatInteractive);
        this.mDynamicImageUrlsMap.put(Integer.valueOf(22), cnfData.exitAppImageUrl);
        this.mDynamicImageUrlsMap.put(Integer.valueOf(26), cnfData.guide_bg_hdr_url);
        this.mDynamicImageUrlsMap.put(Integer.valueOf(27), cnfData.guide_bg_4k_url);
        this.mDynamicImageUrlsMap.put(Integer.valueOf(28), cnfData.guide_bg_1080p_url);
        this.mDynamicImageUrlsMap.put(Integer.valueOf(29), cnfData.detail_month_vip_bg);
        this.mDynamicImageUrlsMap.put(Integer.valueOf(30), cnfData.detail_free_video_bg);
    }

    public synchronized void loadDynamicQData(DeviceCheck deviceCheckData, ILoadDynamicDataCallback callback) {
        boolean z = true;
        synchronized (this) {
            if (deviceCheckData == null) {
                LogUtils.e(TAG, "loadDynamicQData, DeviceCheck data is null");
            } else {
                DynamicResult result = new DynamicResult();
                result.setVipGuideInfo(deviceCheckData.getVipGuideInfo());
                result.setABTest(deviceCheckData.getABTest());
                result.setLogResident(deviceCheckData.getLogResident());
                result.setIsDisableSafeMode(deviceCheckData.isDisableNativePlayerSafeMode());
                result.setPayBeforePreview(deviceCheckData.isPayBeforePreview());
                result.setPayAfterPreview(deviceCheckData.isPayAfterPreview());
                result.setIsPushVideoByTvPlatform(deviceCheckData.isPushVideoByTVPlatform());
                result.setIsDisableAdCache(deviceCheckData.isDisableAdCache());
                result.setDisableNativePlayerAdvancedMode(deviceCheckData.isDisableNativePlayerAdvanceMode());
                result.setIsOpenHcdn(deviceCheckData.isOpenCDN());
                result.setAppCard(deviceCheckData.getAppCard());
                result.setHasRecommend(deviceCheckData.isHasRecommend());
                result.setIsSupportCarousel(deviceCheckData.isEnableCarousel());
                result.setRunMan3TabAvailable(deviceCheckData.isRunMan3TabAvailable());
                result.setPlayerConfig(deviceCheckData.getPlayerConfig2());
                result.setIsHomeRequestOnlyForLaunch(deviceCheckData.isRefeshHomePageOnlyForLaunch());
                result.setIsHomeRequestForLaunchAndEvent(deviceCheckData.isRefeshHomePageOnlyForLaunchAndEvent());
                result.setIsDisableCrosswalk(deviceCheckData.isDisableCrosswalk());
                result.setRetryTimesBeforeStarted(deviceCheckData.getRetryTimesBerforeStarted());
                result.setRetryTimesAfterStarted(deviceCheckData.getRetryTimesAfterStarted());
                result.setSupport4k(deviceCheckData.isSupport4K());
                result.setIsDisableP2PUpload(deviceCheckData.isDisableP2PUpload());
                result.setIsDisableNDUpload(deviceCheckData.isDisableNDUpload());
                result.setMsgDialogIsOutAPP(deviceCheckData.isMsgDialogOutAPP());
                result.setMsgDialogPos(deviceCheckData.getMsgDialogPos());
                result.setIsShowExitAppDialog(deviceCheckData.isShowExitAppDialog());
                result.setIsDisableHCDNPreDeploy(deviceCheckData.isDisableHCDNPreDeploy());
                result.setIsCardSort(deviceCheckData.isCardSort());
                result.setAllTagPosition(deviceCheckData.getAllTagPosition());
                result.setIsSupportGif(deviceCheckData.isSupportGif());
                result.setIsOpenRootCheck(deviceCheckData.isOpenRootCheck());
                result.setIsShowGuideLogin(deviceCheckData.isShowGuideLogin());
                result.setIsOpenAdVipGuide(deviceCheckData.isOpenAdVipGuide());
                result.setIsSupportH265(deviceCheckData.isSupportH265());
                result.setIsEnablePlayerLocalServer(deviceCheckData.isEnablePlayerLocalServer());
                result.setAdSkipFrequency(deviceCheckData.adSkipFrequency());
                result.setEnablePlayerLocalServerF4v2Hls(deviceCheckData.enablePlayerLocalServerF4v2Hls());
                result.setEnablePlayerLocalServerStream(deviceCheckData.enablePlayerLocalServerStream());
                result.setDisableHcdnDaemon(deviceCheckData.disableHcdnDaemon());
                result.setSubChannelPlayerConfig(deviceCheckData.getSubChannelPlayerConfig());
                result.setChildAppUrl(deviceCheckData.getChildAppUrl());
                result.setChinaPokerAppUrl(deviceCheckData.getChinaPokerAppUrl());
                result.setIsCheckInFun(deviceCheckData.isOpenSignin());
                result.setIsCheckInRecommend(deviceCheckData.isOpenSigninRecommend());
                Cnf cnfData = deviceCheckData.cnf;
                if (cnfData == null) {
                    LogUtils.e(TAG, "loadDynamicQData, Cnf data is null");
                } else {
                    boolean z2;
                    prepareForDownLoad(cnfData);
                    checkIfDelete();
                    result.setAd(cnfData.ad);
                    result.setCtime(cnfData.ctime);
                    result.setDesc(cnfData.desc);
                    result.setDevErr(cnfData.devErr);
                    result.setExit(cnfData.exit);
                    result.setFaq(cnfData.faq);
                    result.setFree(cnfData.free);
                    result.setFtinfo(cnfData.ftinfo);
                    result.setIChn(cnfData.iChn);
                    result.setMulCtr(cnfData.mulCtr);
                    result.setMulVip(cnfData.mulVip);
                    result.setName(cnfData.name);
                    result.setNcinfo(cnfData.ncinfo);
                    result.setOther(cnfData.other);
                    result.setPhone(cnfData.phone);
                    result.setPinfo(cnfData.pinfo);
                    result.setPlatCnt(cnfData.platCnt);
                    result.setPreOver(cnfData.preOver);
                    result.setUtime(cnfData.utime);
                    result.setVerErr(cnfData.verErr);
                    result.setStartLoading(cnfData.startLoading);
                    result.setPlayLoading(cnfData.playLoading);
                    result.setBootUrlString(cnfData.bootUrl);
                    result.setStartUrl(cnfData.startUrl);
                    result.setBugVipTipPicUrl(cnfData.bug_vip_tip_pic);
                    result.setHeadUrl(cnfData.headUrl);
                    result.setHeadLogoUrl(cnfData.headLogoUrl);
                    result.setDefUrlString(cnfData.defUrl);
                    result.setPlayUrlString(cnfData.playUrl);
                    result.setServUrlString(cnfData.servUrl);
                    result.setWaterUrlString(cnfData.waterUrl);
                    result.setISeeUrlString(cnfData.iseeUrl);
                    result.setPhoneTips(cnfData.phoneTips);
                    result.setPlayerLogoString(cnfData.playerLogo);
                    result.setPlayerBackColour(cnfData.playerBackColour);
                    result.setDocument(cnfData.document);
                    result.setCodeUrlString(cnfData.codeURL);
                    result.setFeedbackInfoTip(cnfData.feedbackInfoTip);
                    result.setVideoSourceUrlString(cnfData.videoSourceUrl);
                    result.setJstvString(cnfData.jstvUrl);
                    result.setPpsUrlString(cnfData.ppsUrl);
                    result.setJstvList(cnfData.jstvList);
                    result.setPPSList(cnfData.ppsList);
                    result.setDailyLabels(cnfData.dailyTags);
                    result.setDailyIds(cnfData.dailyTagsId2);
                    result.setDailyIcon(cnfData.informationUrl);
                    result.setPlayNewUrlString(cnfData.playNewUrl);
                    result.setHdrGuideBgImageUrls(cnfData.guide_bg_hdr_url);
                    result.set4kGuideBgImageUrls(cnfData.guide_bg_4k_url);
                    result.setVipMonthOperateImageUrls(cnfData.detail_month_vip_bg);
                    result.setDetailFreeVideoOperateImageUrls(cnfData.detail_free_video_bg);
                    result.set1080pGuideBgImageUrls(cnfData.guide_bg_1080p_url);
                    result.setHdrGuideBottomDesc(cnfData.guide_hdr_bottom_desc);
                    result.set4kGuideBottomDesc(cnfData.guide_4k_bottom_desc);
                    result.setOperationImageResourceIds(cnfData.operation_pic_resource_ids);
                    result.setDailyName(cnfData.todayInformation);
                    result.setIseUrlString(cnfData.iseUrl);
                    result.setVipHeadUrlString(cnfData.vipHead);
                    result.setAcrossCode(cnfData.acrossCode);
                    result.setLoginCode(cnfData.loginCode);
                    result.setOnlyIsee(cnfData.onlyIsee);
                    result.setCanntJumpAdvertising(cnfData.canntJumpAdvertising);
                    result.setVipResourceId(cnfData.vipResourceId);
                    result.setVipPushPreviewTip(cnfData.vipPushPreviewTip);
                    result.setVipPushPreviewEndTip(cnfData.vipPushPreviewEndTip);
                    result.setDefaultCarouselUrlString(cnfData.carouselDefaultPicSmall);
                    result.setDailyInfoCornerMark(deviceCheckData.getDailyNewsIcons());
                    result.setPlayerBackColourUrlString(cnfData.playerBackColourUrl);
                    result.setModifyPwdQRCode(cnfData.modifyPwdQRCode);
                    result.setPurchaseGuideTipText(cnfData.purchase_guide_tip_text);
                    result.setPurchaseGuideTipUrlString(cnfData.purchase_guide_tip_image);
                    result.setLivePurchaseGuideTipText(cnfData.live_purchase_guide_tip_text);
                    result.setLivePurchaseGuideTipImageUrlString(cnfData.live_purchase_guide_tip_image);
                    result.setScreenWeChatInteractive(cnfData.screenWeChatInteractive);
                    result.setPurchaseButtonTxt(cnfData.purchase_button_text);
                    result.setCarouselLoadingInfo(cnfData.carouselloadinginfo);
                    result.setAdInfo(cnfData.adinfo);
                    result.setHomeHeaderVipText(cnfData.home_header_vip_text);
                    result.setHomeHeaderVipUrl(cnfData.home_header_vip_url);
                    result.setDetailExitDialogResId(cnfData.detailExitDialogResId);
                    result.setAdGuideBecomeVipText(cnfData.ad_guide_become_vip_text);
                    result.setLoginButtonBelowText(cnfData.signin_login_text);
                    result.setLoginPageLeftPicUrl(cnfData.signin_login_pic_url);
                    result.setPlayerTipCollections(cnfData.player_tip_collections);
                    result.saveDataToLocal();
                    if (callback != null) {
                        callback.onSuccess();
                    }
                    TVApiBase.getTVApiProperty().setShowVipFlag(true);
                    TVApiProperty tVApiProperty = TVApiBase.getTVApiProperty();
                    if (deviceCheckData.isPushVideoByTVPlatform()) {
                        z2 = false;
                    } else {
                        z2 = true;
                    }
                    tVApiProperty.setPlayerAreaControlByPhone(z2);
                    TVApiProperty tVApiProperty2 = TVApiBase.getTVApiProperty();
                    if (deviceCheckData.isPushVideoByTVPlatform()) {
                        z = false;
                    }
                    tVApiProperty2.setPlayerAuthVipByPhone(z);
                }
            }
        }
    }

    private List<FileRequest> createImageRequest(int tag, String imageUrl) {
        LogUtils.d(TAG, "createImageRequest, image tag = " + tag + " imageUrl = " + imageUrl);
        List<FileRequest> requests = new ArrayList();
        if (!StringUtils.isEmpty((CharSequence) imageUrl)) {
            if (imageUrl.contains(";")) {
                imageUrl = imageUrl.replace(";", ",");
            }
            String[] imageUrls = imageUrl.split(",");
            if (imageUrls != null && imageUrls.length > 0) {
                for (String url : imageUrls) {
                    String url2;
                    try {
                        url2 = URLDecoder.decode(url2, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    FileRequest imageRequest = new FileRequest(url2, Integer.valueOf(tag));
                    imageRequest.setShouldBeKilled(false);
                    imageRequest.setLasting(true);
                    requests.add(imageRequest);
                }
            }
        }
        return requests;
    }

    public boolean isSupportVip() {
        return true;
    }

    public boolean isLogResident() {
        boolean isLogResident;
        IDynamicResult result = getDynamicQDataModel();
        if (result == null || !"1".equals(result.getLogResident())) {
            isLogResident = false;
        } else {
            isLogResident = true;
        }
        LogUtils.d(TAG, "is LogResident() = " + isLogResident);
        return isLogResident;
    }

    public IDynamicResult getDynamicQDataModel() {
        return new DynamicResult();
    }

    private void checkIfDelete() {
        DynamicResult result = new DynamicResult();
        for (Entry<Integer, String> entry : this.mDynamicImageUrlsMap.entrySet()) {
            int key = ((Integer) entry.getKey()).intValue();
            if (StringUtils.isEmpty((String) this.mDynamicImageUrlsMap.get(Integer.valueOf(key))) && !ListUtils.isEmpty(result.getImagePaths(key))) {
                LogUtils.d(TAG, "delete local image ,key = " + key);
                delete(key);
            }
        }
    }

    public void checkImageURLUpdate(int tag) {
        CharSequence imageUrls = (String) this.mDynamicImageUrlsMap.get(Integer.valueOf(tag));
        LogUtils.d(TAG, "checkImageURLUpdate, tag = " + tag + " imageUrls = " + imageUrls);
        if (!ListUtils.isEmpty(this.mDynamicImageUrlsMap) && !StringUtils.isEmpty(imageUrls)) {
            final DynamicResult result = new DynamicResult();
            List<String> onLineImageNameList = generateOnlineImageNameList(imageUrls, tag == 22 ? 10 : 0);
            LogUtils.d(TAG, "checkImageURLUpdate, onLineImageNameList = " + onLineImageNameList);
            List<String> localImageNameList = generateLocalPathImageNameList(result.getImagePaths(tag));
            LogUtils.d(TAG, "checkImageURLUpdate, localImageNameList = " + localImageNameList);
            if (check(onLineImageNameList, localImageNameList, result.getImagePaths(tag))) {
                LogUtils.d(TAG, "checkImageURLUpdate, Do not need to update");
                return;
            }
            DynaimicImageDownLoadTask dynamicImageDownLoadTask = new DynaimicImageDownLoadTask(tag, createImageRequest(tag, imageUrls));
            dynamicImageDownLoadTask.setCallBack(new IDynamicDownLoadCallback() {
                public void onDownLoadTaskFinished(int cookie, boolean isSuccess, List<String> listResult) {
                    if (isSuccess) {
                        LogUtils.d(DynamicQDataProvider.TAG, "IDynamicDownLoadCallback, download successfully  cookie = " + cookie);
                        result.addImagePaths(cookie, listResult);
                        result.saveDataToLocal();
                        return;
                    }
                    LogUtils.d(DynamicQDataProvider.TAG, "IDynamicDownLoadCallback, download failed cookie = " + cookie);
                }
            });
            dynamicImageDownLoadTask.download();
        }
    }

    private void delete(int tag) {
        DynamicResult result = new DynamicResult();
        result.removeImagePath(tag);
        result.saveDataToLocal();
    }

    private boolean check(List<String> onLineImageNameList, List<String> localImageNameList, List<String> localImagePathList) {
        LogUtils.d(TAG, "compare onLine images with local images by name ...");
        if (ListUtils.isEmpty((List) onLineImageNameList)) {
            return true;
        }
        if (ListUtils.isEmpty((List) localImageNameList)) {
            return false;
        }
        if (onLineImageNameList.size() != localImageNameList.size()) {
            return false;
        }
        for (CharSequence str : localImagePathList) {
            if (!StringUtils.isEmpty(str) && !new File(str).exists()) {
                LogUtils.d(TAG, "local dynamic image has not exist, repeat download");
                return false;
            }
        }
        Collections.sort(onLineImageNameList);
        Collections.sort(localImageNameList);
        Iterator<String> iteratorA = onLineImageNameList.iterator();
        Iterator<String> iteratorB = localImageNameList.iterator();
        while (iteratorA.hasNext() && iteratorB.hasNext()) {
            if (!((String) iteratorA.next()).equals(iteratorB.next())) {
                return false;
            }
        }
        return true;
    }

    private List<String> generateOnlineImageNameList(String imageUrl, int limit) {
        List<String> onLineImageNamesList = new ArrayList();
        LogUtils.d(TAG, "generateOnlineImageNameList, on line imageUrl = " + imageUrl);
        if (!StringUtils.isEmpty((CharSequence) imageUrl)) {
            if (imageUrl.contains(";")) {
                imageUrl = imageUrl.replace(";", ",");
            }
            String[] imageUris = imageUrl.split(",");
            if (imageUris != null && imageUris.length > 0) {
                int size = imageUris.length;
                if (limit > 0 && size > limit) {
                    size = limit;
                }
                int i = 0;
                while (i < size) {
                    try {
                        String url = URLDecoder.decode(imageUris[i], "UTF-8");
                        CharSequence name = url.substring(url.lastIndexOf("/") + 1);
                        if (StringUtils.isEmpty(name)) {
                            break;
                        }
                        onLineImageNamesList.add(name);
                        i++;
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return onLineImageNamesList;
    }

    private List<String> generateLocalPathImageNameList(List<String> imagePathList) {
        List<String> localImageNamesList = new ArrayList();
        LogUtils.d(TAG, "generateLocalPathImageNameList, local imagePathList = " + imagePathList);
        if (!ListUtils.isEmpty((List) imagePathList)) {
            for (String path : imagePathList) {
                CharSequence name = path.substring(path.lastIndexOf("/") + 1);
                if (StringUtils.isEmpty(name)) {
                    break;
                }
                localImageNamesList.add(name);
            }
        }
        return localImageNamesList;
    }
}
