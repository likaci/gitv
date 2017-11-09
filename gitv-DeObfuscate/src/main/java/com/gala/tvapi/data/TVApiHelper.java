package com.gala.tvapi.data;

import android.annotation.SuppressLint;
import com.gala.tvapi.tv2.TVApiBase;
import com.gala.tvapi.tv2.result.ApiResultAlbumList;
import com.gala.tvapi.vrs.IVrsCallback;
import com.gala.tvapi.vrs.VrsHelper;
import com.gala.tvapi.vrs.model.ChannelLabel;
import com.gala.tvapi.vrs.result.ApiResultChannelLabels;
import com.gala.tvapi.vrs.result.ApiResultPlayListQipu;
import com.gala.video.api.ApiException;
import com.gala.video.api.IApiCallback;
import com.gala.video.lib.share.uikit.data.data.Model.ErrorEvent;
import com.qiyi.tv.client.feature.common.MediaFactory;
import java.util.ArrayList;
import java.util.List;

public class TVApiHelper {
    private static final TVApiHelper f918a = new TVApiHelper();

    class C02592 implements IVrsCallback<ApiResultChannelLabels> {
        private /* synthetic */ IApiCallback f916a;

        C02592(IApiCallback iApiCallback) {
            this.f916a = iApiCallback;
        }

        @SuppressLint({"DefaultLocale"})
        public void onSuccess(ApiResultChannelLabels result) {
            if (!(result == null || result.getChannelLabels() == null)) {
                List<ChannelLabel> channelLabelList = result.getChannelLabels().getChannelLabelList();
                if (channelLabelList != null && channelLabelList.size() > 0) {
                    List arrayList = new ArrayList();
                    for (ChannelLabel channelLabel : channelLabelList) {
                        if (channelLabel.itemType.equals(MediaFactory.TYPE_ALBUM) || channelLabel.itemType.equals(MediaFactory.TYPE_VIDEO)) {
                            arrayList.add(channelLabel.getVideo());
                        }
                    }
                    ApiResultAlbumList apiResultAlbumList = new ApiResultAlbumList();
                    apiResultAlbumList.data = arrayList;
                    this.f916a.onSuccess(apiResultAlbumList);
                    return;
                }
            }
            this.f916a.onException(new ApiException("data is blank", "-100", ErrorEvent.HTTP_CODE_SUCCESS, null));
        }

        public void onException(ApiException e) {
            this.f916a.onException(e);
        }
    }

    class C02603 implements IVrsCallback<ApiResultPlayListQipu> {
        private /* synthetic */ IApiCallback f917a;

        C02603(IApiCallback iApiCallback) {
            this.f917a = iApiCallback;
        }

        public void onSuccess(ApiResultPlayListQipu result) {
            if (result == null || result.getAlbumList() == null || result.getAlbumList().size() <= 0) {
                this.f917a.onException(new ApiException("data is blank", "-100", ErrorEvent.HTTP_CODE_SUCCESS, null));
                return;
            }
            ApiResultAlbumList apiResultAlbumList = new ApiResultAlbumList();
            apiResultAlbumList.data = result.getAlbumList();
            this.f917a.onSuccess(apiResultAlbumList);
        }

        public void onException(ApiException e) {
            this.f917a.onException(e);
        }
    }

    private TVApiHelper() {
    }

    public static TVApiHelper get() {
        return f918a;
    }

    public void vipChannelData(final IApiCallback<ApiResultAlbumList> callback, String id, final int pageNo, final int pageSize) {
        VrsHelper.channelLabelsFilter.call(new IVrsCallback<ApiResultChannelLabels>(this) {
            private /* synthetic */ TVApiHelper f913a;

            @SuppressLint({"DefaultLocale"})
            public void onSuccess(ApiResultChannelLabels result) {
                if (!(result == null || result.getChannelLabels() == null)) {
                    List<ChannelLabel> channelLabelList = result.getChannelLabels().getChannelLabelList();
                    String str = "";
                    String str2 = "";
                    if (channelLabelList != null && channelLabelList.size() > 0) {
                        for (ChannelLabel channelLabel : channelLabelList) {
                            String str3;
                            if (channelLabel.isVipTags() && channelLabel.itemName.equals("推荐")) {
                                str2 = channelLabel.itemKvs.vip_dataId;
                                str3 = channelLabel.itemKvs.vip_dataType;
                            } else {
                                str3 = str2;
                                str2 = str;
                            }
                            str = str2;
                            str2 = str3;
                        }
                        if (str.isEmpty()) {
                            str = ((ChannelLabel) channelLabelList.get(0)).itemKvs.vip_dataId;
                            str2 = ((ChannelLabel) channelLabelList.get(0)).itemKvs.vip_dataType;
                        }
                        if (str2.equals("RESOURCE")) {
                            TVApiHelper.m625a(callback, str);
                            return;
                        } else if (str2.equals("COLLECTION")) {
                            VrsHelper.playListQipuPage.call(new C02603(callback), str, String.valueOf(pageNo), String.valueOf(pageSize), "0");
                            return;
                        }
                    }
                }
                callback.onException(new ApiException("data error", "-100", ErrorEvent.HTTP_CODE_SUCCESS, null));
            }

            public void onException(ApiException e) {
                callback.onException(e);
            }
        }, id, "2", "0");
    }

    static /* synthetic */ void m625a(IApiCallback iApiCallback, String str) {
        String str2 = "1.0";
        if (TVApiBase.getTVApiProperty().isShowLive()) {
            str2 = "3.0";
        }
        VrsHelper.channelLabelsLive.call(new C02592(iApiCallback), str, "0", str2);
    }
}
