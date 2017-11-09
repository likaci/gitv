package com.gala.video.app.epg.web.type;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gala.video.app.epg.ui.albumlist.AlbumUtils;
import com.gala.video.app.epg.web.model.WebBaseTypeParams;
import com.gala.video.lib.framework.core.utils.LogUtils;

public class SearchResultType implements IWebBaseClickType {
    private static final String TAG = "EPG/web/SearchResultType";

    public void onClick(WebBaseTypeParams params) {
        String paramJson = params.getJsonString();
        LogUtils.d(TAG, "gotoSearchResult params:" + paramJson);
        if (paramJson != null && !paramJson.equalsIgnoreCase("undefined") && !paramJson.equalsIgnoreCase("null")) {
            try {
                JSONObject jsonObject = JSON.parseObject(paramJson);
                String key = jsonObject.getString("keyword");
                AlbumUtils.startSearchResultPage(params.getContext(), jsonObject.getIntValue("chnId"), key, 0, "", jsonObject.getString("chnName"));
            } catch (Exception e) {
                e.printStackTrace();
                LogUtils.e(TAG, "gotoSearchResult error:" + e);
            }
        }
    }
}
