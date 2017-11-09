package com.gala.albumprovider.logic.set;

import com.gala.albumprovider.AlbumProviderApi;
import com.gala.albumprovider.base.IChannelPlayListSet;
import com.gala.albumprovider.logic.source.SourceTool;
import com.gala.albumprovider.model.QLayoutKind;
import com.gala.albumprovider.model.Tag;
import com.gala.albumprovider.p001private.C0045h;
import com.gala.albumprovider.p001private.C0062b;
import com.gala.albumprovider.p001private.C0064d;
import com.gala.albumprovider.util.ThreadUtils;
import com.gala.albumprovider.util.USALog;
import com.gala.tvapi.vrs.IVrsCallback;
import com.gala.tvapi.vrs.VrsHelper;
import com.gala.tvapi.vrs.model.ChannelPlayListLabel;
import com.gala.tvapi.vrs.result.ApiResultChannelPlayList;
import com.gala.video.api.ApiException;
import java.util.ArrayList;
import java.util.List;

public class ChannelPlayListSet extends C0045h implements IChannelPlayListSet {
    private int f157a = 0;
    private String f158a = "";
    private List<Tag> f159a = new ArrayList();
    private boolean f160a = false;

    private class PlayListCallback implements IVrsCallback<ApiResultChannelPlayList> {
        private int f153a = 0;
        final /* synthetic */ ChannelPlayListSet f154a;
        private IVrsCallback<ApiResultChannelPlayList> f155a;
        private int f156b = 0;

        PlayListCallback(ChannelPlayListSet channelPlayListSet, IVrsCallback<ApiResultChannelPlayList> callback, int pageNo, int pageSize) {
            this.f154a = channelPlayListSet;
            this.f155a = callback;
            this.f153a = pageNo;
            this.f156b = pageSize;
        }

        public void onException(ApiException arg0) {
            if (this.f155a != null) {
                this.f155a.onException(arg0);
            }
        }

        public void onSuccess(ApiResultChannelPlayList result) {
            if (this.f155a != null) {
                if (result != null) {
                    this.f154a.f157a = SetTool.trimAlbumSetCount(this.f153a, this.f156b, result.getPlayListLabels(), result.count);
                    if (this.f153a == 1) {
                        this.f154a.f157a.clear();
                    }
                    if (result.getPlayListLabels() != null && result.getPlayListLabels().size() > 0) {
                        for (ChannelPlayListLabel channelPlayListLabel : result.getPlayListLabels()) {
                            QLayoutKind qLayoutKind = QLayoutKind.LANDSCAPE;
                            if (this.f154a.f157a) {
                                qLayoutKind = QLayoutKind.PLAY;
                            } else if (channelPlayListLabel.imageStyle == 1) {
                                qLayoutKind = QLayoutKind.PORTRAIT;
                            }
                            this.f154a.f157a.add(new Tag(channelPlayListLabel.id, channelPlayListLabel.name, SourceTool.PLAYLIST_TAG, qLayoutKind));
                        }
                    }
                }
                if (this.f153a == 1 && C0064d.m122a().m126a(this.f154a.f157a)) {
                    C0062b a = C0064d.m122a().m127a(this.f154a.f157a, true);
                    if (!(a == null || a.m101a() == null || !a.m101a().m131a())) {
                        USALog.m147d((Object) "Add cache channel play list data");
                        a.m101a().m133a(this.f154a.f157a);
                        a.m101a().m134a(result.getPlayListLabels());
                    }
                }
                this.f155a.onSuccess(result);
            }
        }
    }

    public ChannelPlayListSet(String channelId, boolean isRun, boolean isFree) {
        this.f158a = channelId;
        this.f160a = isRun;
    }

    public boolean isRunPlayList() {
        return this.f160a;
    }

    public int getPlayListCount() {
        return this.f157a;
    }

    public void loadDataAsync(final int pageNo, final int pageSize, final IVrsCallback<ApiResultChannelPlayList> callback) {
        ThreadUtils.execute(new Runnable(this) {
            final /* synthetic */ ChannelPlayListSet f147a;

            public void run() {
                this.f147a.m63a(pageNo, pageSize, callback);
            }
        });
    }

    private void m63a(int i, int i2, final IVrsCallback<ApiResultChannelPlayList> iVrsCallback) {
        if (iVrsCallback == null) {
            throw new NullPointerException("A callback is needed for AlbumProvider");
        }
        if (i == 1 && C0064d.m122a().m126a(this.f158a)) {
            C0062b a = C0064d.m122a().m127a(this.f158a, false);
            if (!(a == null || a.m101a() == null)) {
                final ApiResultChannelPlayList apiResultChannelPlayList = new ApiResultChannelPlayList();
                apiResultChannelPlayList.data = a.m101a().m131a();
                if (apiResultChannelPlayList.data != null && apiResultChannelPlayList.data.size() > 0) {
                    this.f157a = a.m101a().m131a();
                    for (ChannelPlayListLabel channelPlayListLabel : apiResultChannelPlayList.getPlayListLabels()) {
                        QLayoutKind qLayoutKind = QLayoutKind.LANDSCAPE;
                        if (this.f160a) {
                            qLayoutKind = QLayoutKind.PLAY;
                        } else if (channelPlayListLabel.imageStyle == 1) {
                            qLayoutKind = QLayoutKind.PORTRAIT;
                        }
                        this.f159a.add(new Tag(channelPlayListLabel.id, channelPlayListLabel.name, SourceTool.PLAYLIST_TAG, qLayoutKind));
                    }
                    AlbumProviderApi.getAlbumProvider().getProperty().getExecutorService().execute(new Runnable(this) {
                        final /* synthetic */ ChannelPlayListSet f150a;

                        public void run() {
                            USALog.m147d((Object) "Get cache channel play list data");
                            iVrsCallback.onSuccess(apiResultChannelPlayList);
                        }
                    });
                    return;
                }
            }
        }
        VrsHelper.channelPlayList.call(new PlayListCallback(this, iVrsCallback, i, i2), this.f158a, String.valueOf(i), String.valueOf(i2));
    }

    public String getTagId() {
        return this.f158a;
    }

    public String getTagName() {
        return "播单";
    }

    public int getAlbumCount() {
        return this.f157a;
    }

    public QLayoutKind getLayoutKind() {
        return QLayoutKind.LANDSCAPE;
    }

    public List<Tag> getTagList() {
        return this.f159a;
    }

    public int getSearchCount() {
        return this.f157a;
    }
}
