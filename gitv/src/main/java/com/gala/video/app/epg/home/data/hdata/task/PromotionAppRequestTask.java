package com.gala.video.app.epg.home.data.hdata.task;

import android.text.TextUtils;
import com.alibaba.fastjson.JSON;
import com.gala.video.app.epg.home.promotion.local.PromotionCache;
import com.gala.video.app.epg.home.utils.PromotionUtil;
import com.gala.video.lib.framework.core.utils.io.HttpUtil;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.databus.IDataBus;
import com.gala.video.lib.share.ifmanager.bussnessIF.dynamic.IDynamicResult;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.appdownload.PromotionAppInfo;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.appdownload.PromotionMessage;
import com.gala.video.lib.share.project.Project;
import java.util.HashMap;

public class PromotionAppRequestTask extends BaseRequestTask {
    public void invoke() {
        if (Project.getInstance().getBuild().isSupportRecommendApp()) {
            IDynamicResult dynamicQDataModel = GetInterfaceTools.getIDynamicQDataProvider().getDynamicQDataModel();
            String chinaPokerUrl = dynamicQDataModel.getChinaPokerAppUrl();
            String childAppUrl = dynamicQDataModel.getChildAppUrl();
            if (PromotionUtil.isAppSupport(chinaPokerUrl)) {
                updateCache(chinaPokerUrl, PromotionCache.CHINAPOKER_APP_TAG);
            }
            if (PromotionUtil.isAppSupport(childAppUrl)) {
                updateCache(childAppUrl, PromotionCache.CHILD_APP_TAG);
            }
        }
    }

    private void updateCache(String remoteUrl, String key) {
        PromotionCache cache = PromotionCache.instance();
        HashMap<String, PromotionMessage> localPromotionMessages = cache.get();
        PromotionMessage remoteChildPromotion = getPromotion(remoteUrl);
        if (isUpdateCache(localPromotionMessages, remoteChildPromotion, key)) {
            localPromotionMessages.put(key, remoteChildPromotion);
            cache.save(localPromotionMessages);
            GetInterfaceTools.getDataBus().postStickyEvent(IDataBus.UPDATE_PROMOTION_APP);
        }
    }

    private boolean isUpdateCache(HashMap<String, PromotionMessage> cacheMap, PromotionMessage remote, String key) {
        if (cacheMap == null) {
            return true;
        }
        PromotionMessage localMeg = (PromotionMessage) cacheMap.get(key);
        PromotionAppInfo localAppInfo = PromotionUtil.getPromotionAppInfo(localMeg);
        if (localAppInfo == null) {
            return true;
        }
        String localAppName = PromotionUtil.getPromotionDocument(localMeg).getEntryDocument();
        String localAppPckName = localAppInfo.getAppPckName();
        String localDownloadUrl = localAppInfo.getAppDownloadUrl();
        if (remote == null) {
            return false;
        }
        PromotionAppInfo remoteAppInfo = PromotionUtil.getPromotionAppInfo(remote);
        if (remoteAppInfo == null) {
            return false;
        }
        String remoteAppName = PromotionUtil.getPromotionDocument(remote).getEntryDocument();
        String remotePckName = remoteAppInfo.getAppPckName();
        String remoteDownloadUrl = remoteAppInfo.getAppDownloadUrl();
        if (TextUtils.isEmpty(remotePckName) && TextUtils.isEmpty(remoteDownloadUrl) && TextUtils.isEmpty(remoteAppName)) {
            return false;
        }
        if (remotePckName.equals(localAppPckName) && remoteAppName.equals(localAppName) && remoteDownloadUrl.equals(localDownloadUrl)) {
            return false;
        }
        return true;
    }

    private PromotionMessage getPromotion(String url) {
        try {
            return (PromotionMessage) JSON.parseObject(new HttpUtil(url).get(), PromotionMessage.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void onOneTaskFinished() {
    }
}
