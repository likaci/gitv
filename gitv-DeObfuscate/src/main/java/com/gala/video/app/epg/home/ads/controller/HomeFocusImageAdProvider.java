package com.gala.video.app.epg.home.ads.controller;

import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.databus.IDataBus;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.HomeFocusImageAdModel;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.homefocusimagead.IHomeFocusImageAdProvider.Wrapper;
import java.util.ArrayList;
import java.util.List;

public class HomeFocusImageAdProvider extends Wrapper {
    private static final String TAG = "HomeFocusImageAdController";
    private static HomeFocusImageAdProvider sInstance = new HomeFocusImageAdProvider();
    private List<HomeFocusImageAdModel> mFocusAdModelList = new ArrayList();

    private HomeFocusImageAdProvider() {
    }

    public static HomeFocusImageAdProvider getInstance() {
        return sInstance;
    }

    public void notifyAdData(List<HomeFocusImageAdModel> models) {
        LogUtils.m1568d(TAG, "get focus ad info, size = " + models.size());
        this.mFocusAdModelList = models;
        GetInterfaceTools.getDataBus().postStickyEvent(IDataBus.FOCUS_IMAGE_AD_DOWNLOAD_COMPLETE);
    }

    public List<HomeFocusImageAdModel> getFocusAdModelList() {
        return this.mFocusAdModelList;
    }
}
