package com.gala.speedrunner.speedrunner.albumprovider;

import android.util.Log;
import com.gala.speedrunner.speedrunner.IOneAlbumProvider;
import com.gala.speedrunner.speedrunner.a;
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
    private a a = null;
    private Album f391a = null;
    private String f392a = null;
    private String b = null;

    class AnonymousClass5 implements IApiCallback<ApiResultCode> {
        private /* synthetic */ a a;
        private /* synthetic */ Album f397a;

        AnonymousClass5(a aVar, Album album) {
            this.a = aVar;
            this.f397a = album;
        }

        public final void onException(ApiException e) {
            this.a.onFailure(e);
        }

        public final /* synthetic */ void onSuccess(Object obj) {
            this.a.a(this.f397a);
        }
    }

    public AlbumProvider(Album album) {
        this.f391a = album;
    }

    public AlbumProvider(String cookie, String userId, String str, String str2, String str3) {
        Log.d("SpeedRunner", "cookie-" + cookie + " userId-" + userId);
        this.f392a = cookie;
        this.b = userId;
    }

    public void pickOneAlbum(final a callback) {
        if (this.f391a != null) {
            callback.a(this.f391a);
            return;
        }
        TVApi.deviceCheck.call(new com.gala.speedrunner.speedrunner.albumprovider.a.AnonymousClass1(new a.a(this) {
            private /* synthetic */ AlbumProvider f393a;

            public final void onFailure(Exception failure) {
                callback.onFailure(failure);
            }

            public final void a(a aVar) {
                Log.d("SpeedRunner", "device check");
                this.f393a.a = aVar;
                AlbumProvider.a(this.f393a, new a(this) {
                    private /* synthetic */ AnonymousClass1 a;

                    {
                        this.a = r1;
                    }

                    public final void onFailure(Exception failure) {
                        callback.onFailure(failure);
                    }

                    public final void a(Album album) {
                        callback.a(album);
                    }
                });
            }
        }), new String[0]);
    }

    private void a(final a aVar) {
        if (this.b == null || this.b.equals("")) {
            b(aVar);
            return;
        }
        Log.d("SpeedRunner", "get history anonymity");
        UserHelper.historyListForAnonymity.call(new IVrsCallback<ApiResultHistoryList>(this) {
            private /* synthetic */ AlbumProvider f395a;

            public final /* synthetic */ void onSuccess(ApiResult apiResult) {
                ApiResultHistoryList apiResultHistoryList = (ApiResultHistoryList) apiResult;
                if (apiResultHistoryList == null || apiResultHistoryList.getAlbumList().size() <= 0) {
                    this.f395a.b(aVar);
                    return;
                }
                for (Album album : apiResultHistoryList.getAlbumList()) {
                    if (album.isPurchase == 0) {
                        TVApi.playCheck.call(new AnonymousClass5(aVar, album), album.tvQid);
                        return;
                    }
                }
            }

            public final void onException(ApiException apiException) {
                this.f395a.b(aVar);
            }
        }, TVApiBase.getTVApiProperty().getAnonymity(), "10", "", "0");
    }

    private void b(final a aVar) {
        Log.d("SpeedRunner", "get history Recommend");
        if (this.a.a() == null || this.a.a().equals("")) {
            aVar.onFailure(new Exception("Recommend album is null"));
            return;
        }
        VrsHelper.channelLabelsFilter.call(new IVrsCallback<ApiResultChannelLabels>(this) {
            private /* synthetic */ AlbumProvider f396a;

            public final /* synthetic */ void onSuccess(ApiResult apiResult) {
                ApiResultChannelLabels apiResultChannelLabels = (ApiResultChannelLabels) apiResult;
                if (apiResultChannelLabels == null || apiResultChannelLabels.getChannelLabels() == null) {
                    aVar.onFailure(new Exception("Recommend album is null"));
                    return;
                }
                List<ChannelLabel> channelLabelList = apiResultChannelLabels.getChannelLabels().getChannelLabelList();
                if (channelLabelList != null && channelLabelList.size() > 0) {
                    for (ChannelLabel channelLabel : channelLabelList) {
                        if (!channelLabel.getVideo().isPurchase()) {
                            TVApi.playCheck.call(new AnonymousClass5(aVar, channelLabel.getVideo()), channelLabel.getVideo().tvQid);
                            return;
                        }
                    }
                }
            }

            public final void onException(ApiException arg0) {
                aVar.onFailure(arg0);
            }
        }, this.a.a(), "1", "1");
    }

    static /* synthetic */ void a(AlbumProvider albumProvider, final a aVar) {
        if (albumProvider.f392a == null || albumProvider.f392a.equals("")) {
            albumProvider.a(aVar);
            return;
        }
        Log.d("SpeedRunner", "get history");
        UserHelper.historyList.call(new IVrsCallback<ApiResultHistoryList>(albumProvider) {
            private /* synthetic */ AlbumProvider f394a;

            public final /* synthetic */ void onSuccess(ApiResult apiResult) {
                ApiResultHistoryList apiResultHistoryList = (ApiResultHistoryList) apiResult;
                if (apiResultHistoryList == null || apiResultHistoryList.getAlbumList().size() <= 0) {
                    this.f394a.a(aVar);
                    return;
                }
                for (Album album : apiResultHistoryList.getAlbumList()) {
                    if (album.isPurchase == 0) {
                        TVApi.playCheck.call(new AnonymousClass5(aVar, album), album.tvQid);
                        return;
                    }
                }
            }

            public final void onException(ApiException apiException) {
                this.f394a.a(aVar);
            }
        }, albumProvider.f392a, "10", "", "0");
    }
}
