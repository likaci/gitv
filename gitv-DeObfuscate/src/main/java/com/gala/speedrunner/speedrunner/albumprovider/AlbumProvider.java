package com.gala.speedrunner.speedrunner.albumprovider;

import android.util.Log;
import com.gala.speedrunner.speedrunner.C0183a;
import com.gala.speedrunner.speedrunner.IOneAlbumProvider;
import com.gala.speedrunner.speedrunner.albumprovider.C0196a.C0189a;
import com.gala.speedrunner.speedrunner.albumprovider.C0196a.C01951;
import com.gala.tvapi.tv2.TVApi;
import com.gala.tvapi.tv2.TVApiBase;
import com.gala.tvapi.tv2.model.Album;
import com.gala.tvapi.tv2.result.ApiResultCode;
import com.gala.tvapi.vrs.IVrsCallback;
import com.gala.tvapi.vrs.UserHelper;
import com.gala.tvapi.vrs.VrsHelper;
import com.gala.tvapi.vrs.model.ChannelLabel;
import com.gala.tvapi.vrs.result.ApiResultChannelLabels;
import com.gala.tvapi.vrs.result.ApiResultHistoryList;
import com.gala.video.api.ApiException;
import com.gala.video.api.ApiResult;
import com.gala.video.api.IApiCallback;
import java.util.List;

public class AlbumProvider implements IOneAlbumProvider {
    private C0196a f778a = null;
    private Album f779a = null;
    private String f780a = null;
    private String f781b = null;

    class C01945 implements IApiCallback<ApiResultCode> {
        private /* synthetic */ C0183a f776a;
        private /* synthetic */ Album f777a;

        C01945(C0183a c0183a, Album album) {
            this.f776a = c0183a;
            this.f777a = album;
        }

        public final void onException(ApiException e) {
            this.f776a.onFailure(e);
        }

        public final /* synthetic */ void onSuccess(Object obj) {
            this.f776a.mo804a(this.f777a);
        }
    }

    public AlbumProvider(Album album) {
        this.f779a = album;
    }

    public AlbumProvider(String cookie, String userId, String str, String str2, String str3) {
        Log.d("SpeedRunner", "cookie-" + cookie + " userId-" + userId);
        this.f780a = cookie;
        this.f781b = userId;
    }

    public void pickOneAlbum(final C0183a callback) {
        if (this.f779a != null) {
            callback.mo804a(this.f779a);
            return;
        }
        TVApi.deviceCheck.call(new C01951(new C0189a(this) {
            private /* synthetic */ AlbumProvider f769a;

            class C01881 implements C0183a {
                private /* synthetic */ C01901 f767a;

                C01881(C01901 c01901) {
                    this.f767a = c01901;
                }

                public final void onFailure(Exception failure) {
                    callback.onFailure(failure);
                }

                public final void mo804a(Album album) {
                    callback.mo804a(album);
                }
            }

            public final void onFailure(Exception failure) {
                callback.onFailure(failure);
            }

            public final void mo817a(C0196a c0196a) {
                Log.d("SpeedRunner", "device check");
                this.f769a.f778a = c0196a;
                AlbumProvider.m506a(this.f769a, new C01881(this));
            }
        }), new String[0]);
    }

    private void m505a(final C0183a c0183a) {
        if (this.f781b == null || this.f781b.equals("")) {
            m508b(c0183a);
            return;
        }
        Log.d("SpeedRunner", "get history anonymity");
        UserHelper.historyListForAnonymity.call(new IVrsCallback<ApiResultHistoryList>(this) {
            private /* synthetic */ AlbumProvider f773a;

            public final /* synthetic */ void onSuccess(ApiResult apiResult) {
                ApiResultHistoryList apiResultHistoryList = (ApiResultHistoryList) apiResult;
                if (apiResultHistoryList == null || apiResultHistoryList.getAlbumList().size() <= 0) {
                    this.f773a.m508b(c0183a);
                    return;
                }
                for (Album album : apiResultHistoryList.getAlbumList()) {
                    if (album.isPurchase == 0) {
                        TVApi.playCheck.call(new C01945(c0183a, album), album.tvQid);
                        return;
                    }
                }
            }

            public final void onException(ApiException apiException) {
                this.f773a.m508b(c0183a);
            }
        }, TVApiBase.getTVApiProperty().getAnonymity(), "10", "", "0");
    }

    private void m508b(final C0183a c0183a) {
        Log.d("SpeedRunner", "get history Recommend");
        if (this.f778a.m511a() == null || this.f778a.m511a().equals("")) {
            c0183a.onFailure(new Exception("Recommend album is null"));
            return;
        }
        VrsHelper.channelLabelsFilter.call(new IVrsCallback<ApiResultChannelLabels>(this) {
            private /* synthetic */ AlbumProvider f775a;

            public final /* synthetic */ void onSuccess(ApiResult apiResult) {
                ApiResultChannelLabels apiResultChannelLabels = (ApiResultChannelLabels) apiResult;
                if (apiResultChannelLabels == null || apiResultChannelLabels.getChannelLabels() == null) {
                    c0183a.onFailure(new Exception("Recommend album is null"));
                    return;
                }
                List<ChannelLabel> channelLabelList = apiResultChannelLabels.getChannelLabels().getChannelLabelList();
                if (channelLabelList != null && channelLabelList.size() > 0) {
                    for (ChannelLabel channelLabel : channelLabelList) {
                        if (!channelLabel.getVideo().isPurchase()) {
                            TVApi.playCheck.call(new C01945(c0183a, channelLabel.getVideo()), channelLabel.getVideo().tvQid);
                            return;
                        }
                    }
                }
            }

            public final void onException(ApiException arg0) {
                c0183a.onFailure(arg0);
            }
        }, this.f778a.m511a(), "1", "1");
    }

    static /* synthetic */ void m506a(AlbumProvider albumProvider, final C0183a c0183a) {
        if (albumProvider.f780a == null || albumProvider.f780a.equals("")) {
            albumProvider.m505a(c0183a);
            return;
        }
        Log.d("SpeedRunner", "get history");
        UserHelper.historyList.call(new IVrsCallback<ApiResultHistoryList>(albumProvider) {
            private /* synthetic */ AlbumProvider f771a;

            public final /* synthetic */ void onSuccess(ApiResult apiResult) {
                ApiResultHistoryList apiResultHistoryList = (ApiResultHistoryList) apiResult;
                if (apiResultHistoryList == null || apiResultHistoryList.getAlbumList().size() <= 0) {
                    this.f771a.m505a(c0183a);
                    return;
                }
                for (Album album : apiResultHistoryList.getAlbumList()) {
                    if (album.isPurchase == 0) {
                        TVApi.playCheck.call(new C01945(c0183a, album), album.tvQid);
                        return;
                    }
                }
            }

            public final void onException(ApiException apiException) {
                this.f771a.m505a(c0183a);
            }
        }, albumProvider.f780a, "10", "", "0");
    }
}
