package com.gala.video.lib.share.ifimpl.openplay.service.data;

import android.os.Bundle;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifimpl.openplay.service.tools.OpenApiChannelMap;
import com.gala.video.lib.share.ifimpl.openplay.service.tools.OpenApiCrypt;
import com.gala.video.lib.share.ifimpl.openplay.service.tools.OpenApiDebug;
import com.qiyi.tv.client.data.Channel;

public class LocalChannel extends Channel {
    private static final String TAG = "LocalChannel";
    private static final long serialVersionUID = 1;

    public LocalChannel(Channel channel) {
        if (channel instanceof LocalChannel) {
            throw new IllegalArgumentException("Cannot create LocalChannel from LocalChannel!");
        }
        if (channel instanceof Channel) {
            Channel impl = channel;
            setId(OpenApiChannelMap.decodeChannelId(impl.getId()));
            setName(impl.getName());
            setPicUrl(impl.getPicUrl());
            OpenApiCrypt.decrpyt(getUserTags(), impl.getUserTags());
            setIconUrl(impl.getIconUrl());
        }
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "LocalChannel(" + channel + ") " + toString());
        }
    }

    public Channel getSdkChannel() {
        Channel sdkChannel = new Channel();
        sdkChannel.setId(OpenApiChannelMap.encodeChannelId(getId()));
        sdkChannel.setName(getName());
        sdkChannel.setPicUrl(getPicUrl());
        sdkChannel.setIconUrl(getIconUrl());
        OpenApiCrypt.encrypt(sdkChannel.getUserTags(), getUserTags());
        return sdkChannel;
    }

    protected void readBundle(Bundle bundle) {
        throw new RuntimeException("Not support parcalable!");
    }

    protected void writeBundle(Bundle bundle) {
        throw new RuntimeException("Not support parcalable!");
    }

    public String toString() {
        return "LocalChannel(mId=" + getId() + ", mName=" + getName() + ", mIconUrl=" + getIconUrl() + ", mPicUrl=" + getPicUrl() + ", mUserTags=" + OpenApiDebug.toString(getUserTags()) + ")";
    }
}
