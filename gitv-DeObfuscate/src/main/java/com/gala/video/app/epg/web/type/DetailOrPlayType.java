package com.gala.video.app.epg.web.type;

import com.alibaba.fastjson.JSONObject;
import com.gala.sdk.player.PlayParams;
import com.gala.sdk.player.SourceType;
import com.gala.tvapi.tv2.model.Album;
import com.gala.video.app.epg.ui.albumlist.utils.ItemUtils;
import com.gala.video.app.epg.web.model.WebBaseTypeParams;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.common.configs.WebConstants;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.web.model.WebViewDataImpl;
import com.gala.video.lib.share.utils.DataUtils;
import com.gala.video.webview.utils.WebSDKConstants;
import java.util.ArrayList;

public class DetailOrPlayType implements IWebBaseClickType {
    private static final String TAG = "EPG/web/DetailOrPlayType";
    private String mParamJson;
    private WebViewDataImpl mWebViewDataImpl;

    public void onClick(WebBaseTypeParams params) {
        this.mParamJson = params.getJsonString();
        this.mWebViewDataImpl = params.getWebViewDataImpl();
        LogUtils.m1568d(TAG, "gotoDetailOrPlay params:" + this.mParamJson);
        JSONObject jsonObject = DataUtils.parseToJsonObject(this.mParamJson);
        if (jsonObject != null) {
            try {
                String plId = this.mWebViewDataImpl.getPLId();
                String resGroupId = this.mWebViewDataImpl.getResGroupId();
                String plName = jsonObject.getString(WebSDKConstants.PARAM_KEY_PL_NAME);
                if (StringUtils.isEmpty((CharSequence) plName)) {
                    plName = this.mWebViewDataImpl.getPLName();
                }
                String from = jsonObject.getString("from");
                String albumJson = jsonObject.getString("album");
                String albumListJson = jsonObject.getString(WebConstants.KEY_PLAY_ALBUMLIST);
                String playH5Type = jsonObject.getString("play_h5_type");
                CharSequence toPlay = jsonObject.getString(WebSDKConstants.PARAM_KEY_TO_PLAY);
                Album albumInfo = DataUtils.parseToAlbum(albumJson);
                PlayParams playParams = new PlayParams();
                playParams.sourceType = SourceType.BO_DAN;
                if (StringUtils.isEmpty((CharSequence) resGroupId)) {
                    resGroupId = plId;
                }
                playParams.playListId = resGroupId;
                playParams.playListName = plName;
                playParams.h5PlayType = playH5Type;
                ArrayList<Album> albumList = DataUtils.parseToAlbumList(albumListJson);
                playParams.continuePlayList = albumList;
                if (albumList != null) {
                    int i = 0;
                    while (i < albumList.size()) {
                        if (!StringUtils.isEmpty(((Album) albumList.get(i)).tvQid) && ((Album) albumList.get(i)).tvQid.equals(albumInfo.tvQid)) {
                            playParams.playIndex = i;
                            break;
                        }
                        i++;
                    }
                }
                LogUtils.m1571e(TAG, "gotoDetailOrPlay playParams: -> " + playParams);
                if (StringUtils.isEmpty(toPlay)) {
                    playParams.isPicVertical = true;
                    ItemUtils.openDetailOrPlayForBodan(params.getContext(), albumInfo, from, playParams, "");
                    return;
                }
                LogUtils.m1568d(TAG, "onClick() -> ItemUtils.openPlayForBodan toPlay:" + toPlay);
                ItemUtils.openPlayForBodan(params.getContext(), albumInfo, from, playParams, "");
            } catch (Exception e) {
                e.printStackTrace();
                LogUtils.m1571e(TAG, "goto detail or play error:" + e);
            }
        }
    }
}
