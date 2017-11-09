package com.gala.albumprovider.logic.set;

import com.gala.albumprovider.AlbumProviderApi;
import com.gala.albumprovider.base.IAlbumCallback;
import com.gala.albumprovider.model.QLayoutKind;
import com.gala.albumprovider.p001private.C0045h;
import com.gala.albumprovider.p001private.C0062b;
import com.gala.albumprovider.p001private.C0063c;
import com.gala.albumprovider.p001private.C0064d;
import com.gala.albumprovider.util.ThreadUtils;
import com.gala.albumprovider.util.USALog;
import com.gala.tvapi.vrs.IVrsCallback;
import com.gala.tvapi.vrs.VrsHelper;
import com.gala.tvapi.vrs.result.ApiResultPlayListQipu;
import com.gala.video.api.ApiException;
import java.util.List;

public class AlbumPlayListSet extends C0045h {
    private int f116a = 0;
    private QLayoutKind f117a = QLayoutKind.PORTRAIT;
    private String f118a = "";
    private boolean f119a = false;
    private QLayoutKind f120b = QLayoutKind.PORTRAIT;
    private String f121b = "";
    private boolean f122b = true;
    private String f123c = "";
    private boolean f124c = false;
    private String f125d;

    private class ApiCallback implements IVrsCallback<ApiResultPlayListQipu> {
        private int f112a;
        private IAlbumCallback f113a;
        final /* synthetic */ AlbumPlayListSet f114a;
        private int f115b;

        ApiCallback(AlbumPlayListSet albumPlayListSet, IAlbumCallback albumCallback, int pageNo, int pageSize) {
            this.f114a = albumPlayListSet;
            this.f113a = albumCallback;
            this.f112a = pageNo;
            this.f115b = pageSize;
        }

        public void onSuccess(ApiResultPlayListQipu albumList) {
            if (albumList == null || albumList.getPlayListQipu() == null) {
                this.f113a.onFailure(this.f112a, new ApiException("data is null!"));
            } else if (albumList.data != null) {
                if (albumList.data.imageStyle.equals("0")) {
                    this.f114a.f117a = QLayoutKind.LANDSCAPE;
                } else {
                    this.f114a.f117a = QLayoutKind.PORTRAIT;
                }
                this.f114a.f123c = albumList.getPlayListQipu().tvBackgroundUrl;
                if (this.f112a != 0) {
                    this.f114a.f116a = SetTool.trimAlbumSetCount(1, this.f115b, albumList.getAlbumList(), albumList.data.size);
                } else if (albumList.getAlbumList() != null) {
                    this.f114a.f116a = albumList.getAlbumList().size();
                }
                if ((this.f112a == 0 || this.f112a == 1) && C0064d.m122a().m126a(this.f114a.f116a)) {
                    C0062b a = C0064d.m122a().m126a(this.f114a.f116a);
                    if (a != null && a.m104a(this.f114a.f118a)) {
                        a = C0064d.m122a().m127a(this.f114a.f116a, true);
                        USALog.m147d((Object) "Add cache play list data");
                        C0063c c0063c = new C0063c();
                        c0063c.m120a(this.f114a.f123c);
                        c0063c.m118a(this.f114a.f116a);
                        c0063c.m119a(this.f114a.f116a);
                        c0063c.m121a(albumList.getAlbumList());
                        a.m109a(this.f114a.f118a, c0063c);
                    }
                }
                this.f113a.onSuccess(this.f112a, albumList.getAlbumList());
            }
        }

        public void onException(ApiException ex) {
            this.f113a.onFailure(1, ex);
        }
    }

    public AlbumPlayListSet(String channelId, String playListId, String name, boolean isRun, boolean isFree) {
        this.f118a = playListId;
        this.f121b = name;
        this.f119a = isRun;
        this.f122b = isFree;
        this.f125d = channelId;
    }

    public AlbumPlayListSet(String channelId, String playListId, String name, boolean isRun, boolean isFree, QLayoutKind layout) {
        this.f118a = playListId;
        this.f121b = name;
        this.f119a = isRun;
        this.f122b = isFree;
        this.f120b = layout;
        this.f125d = channelId;
    }

    public AlbumPlayListSet(String channelId, String playListId, String name, boolean isRun, boolean isFree, QLayoutKind layout, boolean isUpdateLayout) {
        this.f118a = playListId;
        this.f121b = name;
        this.f119a = isRun;
        this.f122b = isFree;
        this.f120b = layout;
        this.f124c = isUpdateLayout;
        this.f125d = channelId;
    }

    public QLayoutKind getLayoutKind() {
        if (this.f124c) {
            return this.f117a;
        }
        return this.f120b;
    }

    public boolean isRunPlayList() {
        return this.f119a;
    }

    public String getBackgroundImage() {
        return this.f123c;
    }

    public void loadDataAsync(final int pageNo, final int pageSize, final IAlbumCallback callback) {
        ThreadUtils.execute(new Runnable(this) {
            final /* synthetic */ AlbumPlayListSet f106a;

            public void run() {
                this.f106a.m44a(pageNo, pageSize, callback);
            }
        });
    }

    private void m44a(final int i, int i2, final IAlbumCallback iAlbumCallback) {
        if (iAlbumCallback == null) {
            throw new NullPointerException("A callback is needed for AlbumProvider");
        }
        if ((i == 0 || i == 1) && C0064d.m122a().m126a(this.f125d)) {
            C0062b a = C0064d.m122a().m126a(this.f125d);
            if (a != null && a.m104a(this.f118a)) {
                C0063c a2 = C0064d.m122a().m127a(this.f125d, false).m104a(this.f118a);
                if (a2 != null) {
                    final List a3 = a2.m114a();
                    if (a3 != null && a3.size() > 0) {
                        USALog.m147d((Object) "Get cache play list data");
                        this.f117a = a2.m114a();
                        this.f123c = a2.m114a();
                        this.f116a = a2.m114a();
                        AlbumProviderApi.getAlbumProvider().getProperty().getExecutorService().execute(new Runnable(this) {
                            final /* synthetic */ AlbumPlayListSet f110a;

                            public void run() {
                                iAlbumCallback.onSuccess(i, a3);
                            }
                        });
                        return;
                    }
                }
            }
        }
        String str = this.f122b ? "1" : "0";
        if (i == 0) {
            VrsHelper.playListQipu.call(new ApiCallback(this, iAlbumCallback, i, Integer.MAX_VALUE), this.f118a, str);
        } else {
            VrsHelper.playListQipuPage.call(new ApiCallback(this, iAlbumCallback, i, i2), this.f118a, String.valueOf(i), String.valueOf(i2), str);
        }
    }

    public String getTagId() {
        return this.f118a;
    }

    public String getTagName() {
        return this.f121b;
    }

    public int getAlbumCount() {
        return this.f116a;
    }

    public String getBackground() {
        return this.f123c;
    }

    public int getSearchCount() {
        return this.f116a;
    }
}
