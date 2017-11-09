package com.gala.video.app.epg.openapi.feature.data;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import com.gala.albumprovider.model.QLayoutKind;
import com.gala.tvapi.tv2.TVApi;
import com.gala.tvapi.tv2.model.Album;
import com.gala.tvapi.tv2.model.Channel;
import com.gala.tvapi.tv2.result.ApiResultAlbumList;
import com.gala.tvapi.tv2.result.ApiResultChannelList;
import com.gala.tvapi.type.AlbumType;
import com.gala.video.api.ApiException;
import com.gala.video.api.IApiCallback;
import com.gala.video.lib.framework.core.pingback.PingBack;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.PicSizeUtils;
import com.gala.video.lib.framework.core.utils.PicSizeUtils.PhotoSize;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.ifimpl.openplay.service.OpenApiUtils;
import com.gala.video.lib.share.ifimpl.openplay.service.ServerParamsHelper;
import com.gala.video.lib.share.ifimpl.openplay.service.feature.MaxCommand;
import com.gala.video.lib.share.ifimpl.openplay.service.feature.ResultListHolder;
import com.gala.video.lib.share.ifimpl.openplay.service.tools.OpenApiChannelMap;
import com.gala.video.lib.share.ifimpl.openplay.service.tools.OpenApiNetwork;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.album.IAlbumInfoHelper.AlbumKind;
import com.gala.video.lib.share.pingback.PingBackParams;
import com.gala.video.lib.share.project.Project;
import com.qiyi.tv.client.data.Media;
import com.qiyi.tv.client.impl.Params.DataType;
import java.util.HashMap;
import java.util.List;

public class GetSearchMediaCommand extends MaxCommand<List<Media>> {
    private static final long GALACLIENT_LIMIT_GET_CHANNEL_LIST_TIME_INTERVAL = 3600000;
    private static final String TAG = "GetSearchMediaCommand";
    private static long mLastGetChannelListTime = 0;
    private int mChannelId = 0;
    private HashMap<String, Integer> mChannelMap = new HashMap();

    private class MyChannelListener extends ResultListHolder<Media> implements IApiCallback<ApiResultChannelList> {
        private MyChannelListener() {
        }

        public void onException(ApiException exception) {
            String str;
            if (LogUtils.mIsDebug) {
                LogUtils.m1577w(GetSearchMediaCommand.TAG, "onException(" + exception + ")");
            }
            PingBackParams params = new PingBackParams();
            PingBackParams add = params.add("ec", "315008");
            String str2 = "pfec";
            if (exception == null) {
                str = "";
            } else {
                str = exception.getCode();
            }
            add.add(str2, str);
            PingBack.getInstance().postPingBackToLongYuan(params.build());
        }

        public void onSuccess(ApiResultChannelList channelList) {
            if (LogUtils.mIsDebug) {
                LogUtils.m1577w(GetSearchMediaCommand.TAG, "onSuccess(" + channelList + ")");
            }
            List<Channel> mChannelList = channelList.data;
            if (mChannelList != null && !mChannelList.isEmpty()) {
                GetSearchMediaCommand.mLastGetChannelListTime = System.currentTimeMillis();
                GetSearchMediaCommand.this.putChannelSpecToMap(mChannelList);
            }
        }
    }

    private class MyListener extends ResultListHolder<Media> implements IApiCallback<ApiResultAlbumList> {
        public MyListener(int maxCount) {
            super(maxCount);
        }

        public void onException(ApiException exception) {
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(GetSearchMediaCommand.TAG, "onException(" + exception + ")");
            }
            setNetworkValid(!OpenApiNetwork.isNetworkInvalid(exception));
            setCode(7);
        }

        public void onSuccess(ApiResultAlbumList result) {
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(GetSearchMediaCommand.TAG, "onSuccess(" + result + ")");
            }
            setNetworkValid(true);
            if (result != null && result.data != null) {
                for (Album info : result.data) {
                    Media media = OpenApiUtils.createSdkMedia(info);
                    if (media != null) {
                        int spec = GetSearchMediaCommand.this.getChannelSpec(GetSearchMediaCommand.this.mChannelId);
                        String image = GetSearchMediaCommand.this.getImageUrl(info, spec);
                        String textContent = GetSearchMediaCommand.this.getTitle(info, spec);
                        String textPrompt = GetSearchMediaCommand.this.getPrompt(info, spec);
                        if (image != null) {
                            media.setPicUrl(image);
                        }
                        if (textContent != null) {
                            media.setTitle(textContent);
                        }
                        if (textPrompt != null) {
                            media.setItemPrompt(textPrompt);
                        }
                        add(media);
                    } else {
                        LogUtils.m1577w(GetSearchMediaCommand.TAG, "onSuccess() cannot translate to sdk media!!!");
                    }
                    if (isReachMax()) {
                        LogUtils.m1577w(GetSearchMediaCommand.TAG, "onSuccess() reach max size !!!" + this);
                        return;
                    }
                }
            }
        }
    }

    public GetSearchMediaCommand(Context context, int maxCount) {
        super(context, 10008, 20003, DataType.DATA_MEDIA_LIST, maxCount);
        setNeedNetwork(true);
    }

    public Bundle onProcess(Bundle params) {
        int pageNo = ServerParamsHelper.parsePageNo(params);
        int pageSize = ServerParamsHelper.parsePageSize(params);
        int maxCount = ServerParamsHelper.parseMaxCount(params);
        if (maxCount > getMaxCount()) {
            maxCount = getMaxCount();
        }
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "process() maxCount=" + maxCount);
        }
        MyListener listener = new MyListener(maxCount);
        String keyword = ServerParamsHelper.parseKeyword(params);
        int channelId = ServerParamsHelper.parseChannelId(params);
        if (channelId <= 0) {
            this.mChannelId = 0;
        } else {
            this.mChannelId = OpenApiChannelMap.decodeChannelId(channelId);
            if (isShouldGetNewChannelList()) {
                MyChannelListener channelListener = new MyChannelListener();
                TVApi.channelList.callSync(channelListener, Project.getInstance().getBuild().getVersionString(), "1", "60");
            }
        }
        TVApi.albumSearch.callSync(listener, keyword, "" + this.mChannelId, "" + pageNo, "" + pageSize);
        if (listener.isNetworkValid()) {
            increaseAccessCount();
        }
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "process() keyword=" + keyword + ", pageNo=" + pageNo + ", pageSize=" + pageSize + ", maxCount=" + maxCount);
        }
        return listener.getResult();
    }

    private String getImageUrl(Album album, int spec) {
        switch (spec) {
            case 1:
                if (album.getType() != AlbumType.PEOPLE) {
                    return PicSizeUtils.getUrlWithSize(PhotoSize._320_180, album.pic);
                }
                return PicSizeUtils.getUrlWithSize(PhotoSize._180_101, StringUtils.isEmpty(album.tvPic) ? album.pic : album.tvPic);
            default:
                if (album.getType() == AlbumType.PEOPLE) {
                    return StringUtils.isEmpty(album.tvPic) ? album.pic : album.tvPic;
                } else {
                    if (GetInterfaceTools.getAlbumInfoHelper().getAlbumType(album) == AlbumKind.SIGLE_VIDEO || !GetInterfaceTools.getAlbumInfoHelper().isSingleType(album)) {
                        return PicSizeUtils.getUrlWithSize(PhotoSize._260_360, StringUtils.isEmpty(album.tvPic) ? album.pic : album.tvPic);
                    }
                    return PicSizeUtils.getUrlWithSize(PhotoSize._195_260, StringUtils.isEmpty(album.pic) ? album.tvPic : album.pic);
                }
        }
    }

    private String getTitle(Album album, int spec) {
        if (album == null) {
            return null;
        }
        String title;
        if (spec == 1) {
            title = GetInterfaceTools.getCornerProvider().getTitle(album, QLayoutKind.LANDSCAPE);
        } else if (spec != 2) {
            return "";
        } else {
            title = GetInterfaceTools.getCornerProvider().getTitle(album, QLayoutKind.PORTRAIT);
        }
        if (TextUtils.isEmpty(title)) {
            return album.getAlbumSubName();
        }
        return title;
    }

    private String getPrompt(Album album, int spec) {
        if (spec == 1) {
            return GetInterfaceTools.getCornerProvider().getDescRB(album, QLayoutKind.LANDSCAPE);
        }
        if (spec == 2) {
            return GetInterfaceTools.getCornerProvider().getDescRB(album, QLayoutKind.PORTRAIT);
        }
        return null;
    }

    private boolean isShouldGetNewChannelList() {
        if (this.mChannelMap == null || this.mChannelMap.isEmpty() || Math.abs(System.currentTimeMillis() - mLastGetChannelListTime) >= 3600000) {
            return true;
        }
        return false;
    }

    private void putChannelSpecToMap(List<Channel> channelList) {
        this.mChannelMap.clear();
        for (Channel channel : channelList) {
            this.mChannelMap.put(channel.id, Integer.valueOf(channel.spec));
        }
    }

    private int getChannelSpec(int channelId) {
        int spec = 2;
        if (channelId > 0 && this.mChannelMap.containsKey(String.valueOf(channelId))) {
            spec = ((Integer) this.mChannelMap.get(String.valueOf(channelId))).intValue();
            if (LogUtils.mIsDebug) {
                LogUtils.m1577w(TAG, "found channel spec : " + spec + " channelId : " + channelId);
            }
        }
        return spec;
    }
}
