package com.gala.video.app.epg.web.subject.play;

import android.os.Bundle;
import android.util.Log;
import com.alibaba.fastjson.JSONObject;
import com.gala.sdk.player.ISdkError;
import com.gala.sdk.player.PlayParams;
import com.gala.sdk.player.SourceType;
import com.gala.sdk.player.data.IVideo;
import com.gala.video.app.epg.web.model.WebInfo;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.common.configs.IntentConfig2;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.web.model.WebViewDataImpl;

public class LivePlayControl extends PlayBaseControl {
    private static final String TAG = "EPG/Web/LivePlayControl";
    private boolean mIsChecked;

    public LivePlayControl(WebInfo webInfo) {
        super(webInfo);
        initData();
    }

    private void initData() {
        this.mAlbum = this.mWebInfo.getAlbum();
        this.mFlowerList = this.mWebInfo.getFlowerList();
    }

    public boolean onErrorPlay(IVideo video, ISdkError error) {
        this.mIsErrorState = true;
        return false;
    }

    public WebViewDataImpl generateJsonObject(WebViewDataImpl webJsonParam) {
        webJsonParam.putTagLIVE(this.mWebInfo.getType());
        webJsonParam.putAlbum(this.mWebInfo.getAlbumJson());
        webJsonParam.putFlowerList(this.mWebInfo.getFlowerListJson());
        webJsonParam.putPLId(this.mAlbum != null ? this.mAlbum.tv_livecollection : "");
        webJsonParam.putEventid(this.mEventId);
        return webJsonParam;
    }

    public void onResumePlay() {
        startLivePlay();
    }

    public void initPlay(JSONObject playParams) {
        LogUtils.m1568d(TAG, "initPlay()");
        startLivePlay();
    }

    public void startLivePlay() {
        Log.d(TAG, ">>startLivePlay");
        if (this.mAlbum != null && this.mIntent != null) {
            PlayParams params = new PlayParams();
            params.h5PlayType = this.mH5PlayType;
            Bundle extras = this.mIntent.getExtras();
            extras.putSerializable("videoType", SourceType.LIVE);
            extras.putSerializable("albumInfo", this.mAlbum);
            extras.putString("from", this.mFrom);
            extras.putString("buy_source", this.mBuySource);
            extras.putString("eventId", this.mEventId);
            extras.putSerializable("play_list_info", params);
            extras.putString(IntentConfig2.INTENT_PARAM_TAB_SOURCE, this.mTabSource);
            extras.putSerializable("playlist", this.mFlowerList);
            createVideoPlayer(extras);
        } else if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "startLivePlay mAlbum or mIntent is empty");
        }
    }

    public void loadVipInfo() {
        if (this.mAlbum != null && this.mIsChecked) {
            checkLiveInfo(null);
        }
        this.mIsChecked = true;
    }
}
