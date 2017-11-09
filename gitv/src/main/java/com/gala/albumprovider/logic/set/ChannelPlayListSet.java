package com.gala.albumprovider.logic.set;

import com.gala.albumprovider.AlbumProviderApi;
import com.gala.albumprovider.base.IChannelPlayListSet;
import com.gala.albumprovider.logic.source.SourceTool;
import com.gala.albumprovider.model.QLayoutKind;
import com.gala.albumprovider.model.Tag;
import com.gala.albumprovider.private.b;
import com.gala.albumprovider.private.d;
import com.gala.albumprovider.private.h;
import com.gala.albumprovider.util.ThreadUtils;
import com.gala.albumprovider.util.USALog;
import com.gala.tvapi.vrs.IVrsCallback;
import com.gala.tvapi.vrs.VrsHelper;
import com.gala.tvapi.vrs.model.ChannelPlayListLabel;
import com.gala.tvapi.vrs.result.ApiResultChannelPlayList;
import com.gala.video.api.ApiException;
import java.util.ArrayList;
import java.util.List;

public class ChannelPlayListSet extends h implements IChannelPlayListSet {
    private int a = 0;
    private String f124a = "";
    private List<Tag> f125a = new ArrayList();
    private boolean f126a = false;

    private class PlayListCallback implements IVrsCallback<ApiResultChannelPlayList> {
        private int a = 0;
        final /* synthetic */ ChannelPlayListSet f131a;
        private IVrsCallback<ApiResultChannelPlayList> f132a;
        private int b = 0;

        PlayListCallback(ChannelPlayListSet channelPlayListSet, IVrsCallback<ApiResultChannelPlayList> callback, int pageNo, int pageSize) {
            this.f131a = channelPlayListSet;
            this.f132a = callback;
            this.a = pageNo;
            this.b = pageSize;
        }

        public void onException(ApiException arg0) {
            if (this.f132a != null) {
                this.f132a.onException(arg0);
            }
        }

        public void onSuccess(ApiResultChannelPlayList result) {
            if (this.f132a != null) {
                if (result != null) {
                    this.f131a.a = SetTool.trimAlbumSetCount(this.a, this.b, result.getPlayListLabels(), result.count);
                    if (this.a == 1) {
                        this.f131a.a.clear();
                    }
                    if (result.getPlayListLabels() != null && result.getPlayListLabels().size() > 0) {
                        for (ChannelPlayListLabel channelPlayListLabel : result.getPlayListLabels()) {
                            QLayoutKind qLayoutKind = QLayoutKind.LANDSCAPE;
                            if (this.f131a.a) {
                                qLayoutKind = QLayoutKind.PLAY;
                            } else if (channelPlayListLabel.imageStyle == 1) {
                                qLayoutKind = QLayoutKind.PORTRAIT;
                            }
                            this.f131a.a.add(new Tag(channelPlayListLabel.id, channelPlayListLabel.name, SourceTool.PLAYLIST_TAG, qLayoutKind));
                        }
                    }
                }
                if (this.a == 1 && d.a().a(this.f131a.a)) {
                    b a = d.a().a(this.f131a.a, true);
                    if (!(a == null || a.a() == null || !a.a().a())) {
                        USALog.d((Object) "Add cache channel play list data");
                        a.a().a(this.f131a.a);
                        a.a().a(result.getPlayListLabels());
                    }
                }
                this.f132a.onSuccess(result);
            }
        }
    }

    public ChannelPlayListSet(String channelId, boolean isRun, boolean isFree) {
        this.f124a = channelId;
        this.f126a = isRun;
    }

    public boolean isRunPlayList() {
        return this.f126a;
    }

    public int getPlayListCount() {
        return this.a;
    }

    public void loadDataAsync(final int pageNo, final int pageSize, final IVrsCallback<ApiResultChannelPlayList> callback) {
        ThreadUtils.execute(new Runnable(this) {
            final /* synthetic */ ChannelPlayListSet f127a;

            public void run() {
                this.f127a.a(pageNo, pageSize, callback);
            }
        });
    }

    private void a(int i, int i2, final IVrsCallback<ApiResultChannelPlayList> iVrsCallback) {
        if (iVrsCallback == null) {
            throw new NullPointerException("A callback is needed for AlbumProvider");
        }
        if (i == 1 && d.a().a(this.f124a)) {
            b a = d.a().a(this.f124a, false);
            if (!(a == null || a.a() == null)) {
                final ApiResultChannelPlayList apiResultChannelPlayList = new ApiResultChannelPlayList();
                apiResultChannelPlayList.data = a.a().a();
                if (apiResultChannelPlayList.data != null && apiResultChannelPlayList.data.size() > 0) {
                    this.a = a.a().a();
                    for (ChannelPlayListLabel channelPlayListLabel : apiResultChannelPlayList.getPlayListLabels()) {
                        QLayoutKind qLayoutKind = QLayoutKind.LANDSCAPE;
                        if (this.f126a) {
                            qLayoutKind = QLayoutKind.PLAY;
                        } else if (channelPlayListLabel.imageStyle == 1) {
                            qLayoutKind = QLayoutKind.PORTRAIT;
                        }
                        this.f125a.add(new Tag(channelPlayListLabel.id, channelPlayListLabel.name, SourceTool.PLAYLIST_TAG, qLayoutKind));
                    }
                    AlbumProviderApi.getAlbumProvider().getProperty().getExecutorService().execute(new Runnable(this) {
                        final /* synthetic */ ChannelPlayListSet a;

                        public void run() {
                            USALog.d((Object) "Get cache channel play list data");
                            iVrsCallback.onSuccess(apiResultChannelPlayList);
                        }
                    });
                    return;
                }
            }
        }
        VrsHelper.channelPlayList.call(new PlayListCallback(this, iVrsCallback, i, i2), this.f124a, String.valueOf(i), String.valueOf(i2));
    }

    public String getTagId() {
        return this.f124a;
    }

    public String getTagName() {
        return "播单";
    }

    public int getAlbumCount() {
        return this.a;
    }

    public QLayoutKind getLayoutKind() {
        return QLayoutKind.LANDSCAPE;
    }

    public List<Tag> getTagList() {
        return this.f125a;
    }

    public int getSearchCount() {
        return this.a;
    }
}
