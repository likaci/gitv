package com.gala.video.app.epg.web.subject.play;

import com.alibaba.fastjson.JSONObject;
import com.gala.sdk.player.ISdkError;
import com.gala.sdk.player.PlayParams;
import com.gala.sdk.player.SourceType;
import com.gala.sdk.player.data.IVideo;
import com.gala.video.app.epg.web.model.WebInfo;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.common.configs.IntentConfig2;
import com.gala.video.lib.share.common.configs.WebConstants;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.web.model.WebViewDataImpl;
import com.gala.video.lib.share.utils.DataUtils;

public class DianBoPlayControl extends PlayBaseControl {
    private static final String TAG = "EPG/Web/DianBoPlayControl";

    public DianBoPlayControl(WebInfo webInfo) {
        super(webInfo);
    }

    public boolean onErrorPlay(IVideo video, ISdkError error) {
        LogUtils.e(TAG, "onErrorPlay: error=" + error);
        if (PlayerErrorUtils.onDianBoError(error)) {
            LogUtils.d(TAG, "onErrorPlay is previewError");
            this.mIsVipError = true;
        }
        return false;
    }

    public void onResumePlay() {
        goPlay();
    }

    public void initPlay(JSONObject playParams) {
        String albumListJson = playParams.getString(WebConstants.KEY_PLAY_ALBUMLIST);
        String albumJson = playParams.getString("album");
        this.mPlayList = DataUtils.parseToAlbumList(albumListJson);
        this.mPlayIndex = findPlayIndex(DataUtils.parseToAlbum(albumJson).tvQid);
        goPlay();
    }

    public void goPlay() {
        LogUtils.d(TAG, ">>goPlay");
        if (!ListUtils.isEmpty(this.mPlayList)) {
            PlayParams params = new PlayParams();
            params.continuePlayList = this.mPlayList;
            params.playListId = !StringUtils.isEmpty(this.mWebInfo.getResGroupId()) ? this.mWebInfo.getResGroupId() : this.mPlId;
            params.playListName = this.mPlName;
            params.h5PlayType = this.mH5PlayType;
            params.sourceType = SourceType.BO_DAN;
            this.mIntent.putExtra("videoType", SourceType.BO_DAN);
            this.mIntent.putExtra("play_list_info", params);
            this.mIntent.putExtra(IntentConfig2.INTENT_PARAM_TAB_SOURCE, this.mTabSource);
            params.playIndex = this.mPlayIndex;
            createVideoPlayer(this.mIntent.getExtras());
        } else if (LogUtils.mIsDebug) {
            LogUtils.e(TAG, "<<goPlay playList is empty");
        }
    }

    public WebViewDataImpl generateJsonObject(WebViewDataImpl jsonParam) {
        this.mPlId = this.mWebInfo.getId();
        this.mPlName = this.mWebInfo.getName();
        jsonParam.putTagLIVE(-1);
        jsonParam.putPLId(this.mPlId);
        jsonParam.putPLName(this.mPlName);
        jsonParam.putResGroupId(this.mWebInfo.getResGroupId());
        jsonParam.putAlbumList(this.mWebInfo.getAlbumListJson());
        return jsonParam;
    }
}
