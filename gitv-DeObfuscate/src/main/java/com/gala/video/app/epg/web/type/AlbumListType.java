package com.gala.video.app.epg.web.type;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gala.video.app.epg.ui.albumlist.AlbumUtils;
import com.gala.video.app.epg.web.model.WebBaseTypeParams;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.common.configs.WebConstants;

public class AlbumListType implements IWebBaseClickType {
    private static final String TAG = "EPG/web/AlbumListType";
    private String mParamJson;

    public void onClick(WebBaseTypeParams params) {
        this.mParamJson = params.getJsonString();
        LogUtils.m1568d(TAG, "gotoAlbumList params:" + this.mParamJson);
        if (this.mParamJson != null && !this.mParamJson.equalsIgnoreCase("undefined") && !this.mParamJson.equalsIgnoreCase("null")) {
            try {
                JSONObject jsonObject = JSON.parseObject(this.mParamJson);
                int chnId = jsonObject.getInteger("chnId").intValue();
                AlbumUtils.startChannelPage(params.getContext(), jsonObject.getString(WebConstants.KEY_FIRSTLABEL_LOCATION_ID), chnId, jsonObject.getString("from"));
            } catch (Exception e) {
                e.printStackTrace();
                LogUtils.m1571e(TAG, "gotoAlbumList error:" + e);
            }
        }
    }
}
