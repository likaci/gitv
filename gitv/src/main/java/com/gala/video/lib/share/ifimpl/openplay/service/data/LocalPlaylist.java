package com.gala.video.lib.share.ifimpl.openplay.service.data;

import android.os.Bundle;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifimpl.openplay.service.tools.OpenApiCrypt;
import com.gala.video.lib.share.ifimpl.openplay.service.tools.OpenApiDebug;
import com.qiyi.tv.client.data.Playlist;

public class LocalPlaylist extends Playlist {
    private static final String TAG = "LocalPlaylist";
    private static final long serialVersionUID = 1;

    public LocalPlaylist(Playlist playlist) {
        if (playlist instanceof LocalPlaylist) {
            throw new IllegalArgumentException("Cannot create LocalPlaylist from LocalPlaylist!");
        }
        if (playlist instanceof Playlist) {
            Playlist impl = playlist;
            setId(OpenApiCrypt.decrypt(impl.getId()));
            setName(impl.getName());
            setPicUrl(impl.getPicUrl());
            OpenApiCrypt.decrpyt(getUserTags(), impl.getUserTags());
            setCornerHint(impl.getCornerHint());
            setSourceCode(impl.getSourceCode());
            setFromSdk(impl.fromSdk());
            setItemPrompt(impl.getItemPrompt());
            setTitle(impl.getTitle());
            setPlaylistDetail(impl.getPlaylistDetail());
        }
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "LocalPlaylist(" + playlist + ") " + toString());
        }
    }

    public Playlist getSdkPlaylist() {
        Playlist sdkPlaylist = new Playlist();
        sdkPlaylist.setId(OpenApiCrypt.encrypt(getId()));
        sdkPlaylist.setName(getName());
        sdkPlaylist.setPicUrl(getPicUrl());
        sdkPlaylist.setCornerHint(getCornerHint());
        sdkPlaylist.setSourceCode(getSourceCode());
        sdkPlaylist.setFromSdk(fromSdk());
        sdkPlaylist.setItemPrompt(getItemPrompt());
        sdkPlaylist.setTitle(getTitle());
        sdkPlaylist.setPlaylistDetail(getPlaylistDetail());
        OpenApiCrypt.encrypt(sdkPlaylist.getUserTags(), getUserTags());
        return sdkPlaylist;
    }

    protected void readBundle(Bundle bundle) {
        throw new RuntimeException("Not support parcalable!");
    }

    protected void writeBundle(Bundle bundle) {
        throw new RuntimeException("Not support parcalable!");
    }

    public String toString() {
        return "LocalPlaylist(mType=" + getType() + ", mId=" + getId() + ", mName=" + getName() + ", mCornerHint=" + getCornerHint() + ", mPicUrl=" + getPicUrl() + ", sourceCode=" + getSourceCode() + ", fromSdk=" + fromSdk() + ", prompt=" + getItemPrompt() + ", mTitle=" + getTitle() + ", mUserTags=" + OpenApiDebug.toString(getUserTags()) + ")";
    }
}
