package com.gala.video.lib.share.ifimpl.openplay.service.data;

import android.os.Bundle;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifimpl.openplay.service.tools.OpenApiChannelMap;
import com.gala.video.lib.share.ifimpl.openplay.service.tools.OpenApiCrypt;
import com.qiyi.tv.client.data.Album;

public class LocalAlbum extends Album {
    private static final String TAG = "LocalAlbum";
    private static final long serialVersionUID = 1;

    public LocalAlbum(Album album) {
        if (album instanceof LocalAlbum) {
            throw new IllegalArgumentException("Cannot create LocalAlbum from LocalAlbum!");
        }
        if (album instanceof Album) {
            Album impl = album;
            setName(impl.getName());
            setPicUrl(impl.getPicUrl());
            OpenApiCrypt.decrpyt(getUserTags(), impl.getUserTags());
            setCornerHint(impl.getCornerHint());
            setFromSdk(impl.fromSdk());
            if (fromSdk()) {
                setId(OpenApiCrypt.decrypt(impl.getId()));
                setChannelId(OpenApiChannelMap.decodeChannelId(impl.getChannelId()));
                setVideoId(OpenApiCrypt.decrypt(impl.getVideoId()));
            } else {
                setId(impl.getId());
                setChannelId(impl.getChannelId());
                setVideoId(impl.getVideoId());
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
            LogUtils.m1568d(TAG, "LocalAlbum(" + album + ") " + toString());
        }
    }

    public Album getSdkAlbum() {
        Album sdkAlbum = new Album();
        sdkAlbum.setId(OpenApiCrypt.encrypt(getId()));
        sdkAlbum.setVideoId(OpenApiCrypt.encrypt(getVideoId()));
        sdkAlbum.setName(getName());
        sdkAlbum.setPicUrl(getPicUrl());
        sdkAlbum.setChannelId(OpenApiChannelMap.encodeChannelId(getChannelId()));
        sdkAlbum.setComment(getComment());
        sdkAlbum.setIsSeries(isSeries());
        sdkAlbum.setStartTime(getStartTime());
        sdkAlbum.setPlayOrder(getPlayOrder());
        sdkAlbum.setCornerHint(getCornerHint());
        sdkAlbum.setSourceCode(getSourceCode());
        sdkAlbum.setItemPrompt(getItemPrompt());
        sdkAlbum.setTitle(getTitle());
        sdkAlbum.setPlayTime(getPlayTime());
        sdkAlbum.setTotalTime(getTotalTime());
        sdkAlbum.setFocus(getFocus());
        sdkAlbum.setScore(getScore());
        sdkAlbum.setPlayCount(getPlayCount());
        sdkAlbum.setHistoryAddTime(getHistoryAddTime());
        sdkAlbum.setTvCount(getTvCount());
        OpenApiCrypt.encrypt(sdkAlbum.getUserTags(), getUserTags());
        return sdkAlbum;
    }

    protected void readBundle(Bundle bundle) {
        throw new RuntimeException("Not support parcalable!");
    }

    protected void writeBundle(Bundle bundle) {
        throw new RuntimeException("Not support parcalable!");
    }

    public String toString() {
        return "LocalAlbum(mType=" + getType() + ", mId=" + getId() + ", mName=" + getName() + ", mChannelId=" + getChannelId() + ", mVideoId=" + getVideoId() + ", mComment=" + getComment() + ", mIsSeries=" + isSeries() + ", mStartTime=" + getStartTime() + ", mCornerHint=" + getCornerHint() + ", mPicUrl=" + getPicUrl() + ", sourceCode=" + getSourceCode() + ", fromSdk=" + fromSdk() + ", prompt=" + getItemPrompt() + ", title=" + getTitle() + ", score=" + getScore() + ", playCount=" + getPlayCount() + ", focus = " + getFocus() + ", playtime = " + getPlayTime() + ", Total time = " + getTotalTime() + (", add history time = " + getHistoryAddTime()) + ")";
    }
}
