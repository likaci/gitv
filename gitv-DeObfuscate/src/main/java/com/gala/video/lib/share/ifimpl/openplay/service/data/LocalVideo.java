package com.gala.video.lib.share.ifimpl.openplay.service.data;

import android.os.Bundle;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifimpl.openplay.service.tools.OpenApiChannelMap;
import com.gala.video.lib.share.ifimpl.openplay.service.tools.OpenApiCrypt;
import com.gala.video.lib.share.ifimpl.openplay.service.tools.OpenApiDebug;
import com.qiyi.tv.client.data.Video;

public class LocalVideo extends Video {
    private static final String TAG = "LocalVideo";
    private static final long serialVersionUID = 1;

    public LocalVideo(Video video) {
        if (video instanceof LocalVideo) {
            throw new IllegalArgumentException("Cannot create LocalVideo from LocalVideo!");
        }
        if (video instanceof Video) {
            Video impl = video;
            setName(impl.getName());
            setPicUrl(impl.getPicUrl());
            OpenApiCrypt.decrpyt(getUserTags(), impl.getUserTags());
            setCornerHint(impl.getCornerHint());
            setFromSdk(impl.fromSdk());
            if (fromSdk()) {
                setId(OpenApiCrypt.decrypt(impl.getId()));
                setChannelId(OpenApiChannelMap.decodeChannelId(impl.getChannelId()));
                setAlbumId(OpenApiCrypt.decrypt(impl.getAlbumId()));
            } else {
                setId(impl.getId());
                setChannelId(impl.getChannelId());
                setAlbumId(impl.getAlbumId());
            }
            setComment(impl.getComment());
            setIsSeries(impl.isSeries());
            setStartTime(impl.getStartTime());
            setPlayOrder(impl.getPlayOrder());
            setSourceCode(impl.getSourceCode());
            setItemPrompt(impl.getItemPrompt());
            setTitle(impl.getTitle());
            setPlayTime(impl.getPlayTime());
            setTotalTime(impl.getTotalTime());
            setFocus(impl.getFocus());
            setHistoryAddTime(impl.getHistoryAddTime());
            setScore(impl.getScore());
            setPlayCount(impl.getPlayCount());
        }
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "LocalVideo(" + video + ") " + toString());
        }
    }

    public Video getSdkVideo() {
        Video sdkVideo = new Video();
        sdkVideo.setId(OpenApiCrypt.encrypt(getId()));
        sdkVideo.setName(getName());
        sdkVideo.setPicUrl(getPicUrl());
        sdkVideo.setChannelId(OpenApiChannelMap.encodeChannelId(getChannelId()));
        sdkVideo.setAlbumId(OpenApiCrypt.encrypt(getAlbumId()));
        sdkVideo.setComment(getComment());
        sdkVideo.setIsSeries(isSeries());
        sdkVideo.setStartTime(getStartTime());
        sdkVideo.setPlayOrder(getPlayOrder());
        sdkVideo.setCornerHint(getCornerHint());
        sdkVideo.setSourceCode(getSourceCode());
        sdkVideo.setItemPrompt(getItemPrompt());
        sdkVideo.setTitle(getTitle());
        sdkVideo.setPlayTime(getPlayTime());
        sdkVideo.setTotalTime(getTotalTime());
        sdkVideo.setFocus(getFocus());
        sdkVideo.setScore(getScore());
        sdkVideo.setPlayCount(getPlayCount());
        sdkVideo.setHistoryAddTime(getHistoryAddTime());
        OpenApiCrypt.encrypt(sdkVideo.getUserTags(), getUserTags());
        return sdkVideo;
    }

    protected void readBundle(Bundle bundle) {
        throw new RuntimeException("Not support parcalable!");
    }

    protected void writeBundle(Bundle bundle) {
        throw new RuntimeException("Not support parcalable!");
    }

    public String toString() {
        return "LocalVideo(mType=" + getType() + ", mId=" + getId() + ", mName=" + getName() + ", mChannelId=" + getChannelId() + ", mAlbumId=" + getAlbumId() + ", comment=" + getComment() + ", mIsSeries=" + isSeries() + ", mStartTime=" + getStartTime() + ", mCornerHint=" + getCornerHint() + ", mPicUrl=" + getPicUrl() + ", sourceCode=" + getSourceCode() + ", fromSdk=" + fromSdk() + ", prompt=" + getItemPrompt() + ", title=" + getTitle() + ", score=" + getScore() + ", playCount=" + getPlayCount() + ", focus = " + getFocus() + ", playtime = " + getPlayTime() + ", Total time = " + getTotalTime() + (", add history time = " + getHistoryAddTime()) + ", mUserTags=" + OpenApiDebug.toString(getUserTags()) + ")";
    }
}
