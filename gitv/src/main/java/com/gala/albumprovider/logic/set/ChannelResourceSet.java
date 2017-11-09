package com.gala.albumprovider.logic.set;

import com.gala.albumprovider.AlbumProviderApi;
import com.gala.albumprovider.base.IChannelLabelsCallback;
import com.gala.albumprovider.model.QChannel;
import com.gala.albumprovider.model.QLayoutKind;
import com.gala.albumprovider.model.Tag;
import com.gala.albumprovider.private.b;
import com.gala.albumprovider.private.d;
import com.gala.albumprovider.private.g;
import com.gala.albumprovider.private.h;
import com.gala.albumprovider.util.ThreadUtils;
import com.gala.albumprovider.util.USALog;
import com.gala.tvapi.type.ResourceType;
import com.gala.tvapi.vrs.IVrsCallback;
import com.gala.tvapi.vrs.VrsHelper;
import com.gala.tvapi.vrs.model.ChannelLabel;
import com.gala.tvapi.vrs.model.MultiChannelLabels;
import com.gala.tvapi.vrs.result.ApiResultMultiChannelLabels;
import com.gala.tvapi.vrs.result.ApiResultViewership;
import com.gala.video.api.ApiException;
import com.gala.video.lib.share.uikit.data.data.Model.ErrorEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class ChannelResourceSet extends h {
    private int a = 0;
    private QChannel f134a;
    private Tag f135a;
    private String f136a = "";
    private boolean f137a = false;
    private String b = "";
    private String c = "AlbumProvider";

    private class MultiChannelLabelsCallback implements IVrsCallback<ApiResultMultiChannelLabels> {
        private IChannelLabelsCallback a;
        final /* synthetic */ ChannelResourceSet f141a;
        private String[] f142a;

        MultiChannelLabelsCallback(ChannelResourceSet channelResourceSet, IChannelLabelsCallback callback, String... id) {
            this.f141a = channelResourceSet;
            this.a = callback;
            this.f142a = id;
        }

        public void onException(ApiException arg0) {
            this.a.onFailure(arg0);
        }

        public void onSuccess(ApiResultMultiChannelLabels result) {
            if (result == null || result.getData() == null || this.f142a.length != result.getData().size()) {
                this.a.onFailure(new ApiException(null, "-100", ErrorEvent.HTTP_CODE_SUCCESS, null));
                return;
            }
            b a;
            if (d.a().a(this.f141a.f134a)) {
                a = d.a().a(this.f141a.f134a, true);
            } else {
                a = null;
            }
            List arrayList = new ArrayList(6);
            Collection arrayList2 = new ArrayList();
            String[] strArr = this.f142a;
            int length = strArr.length;
            int i = 0;
            while (i < length) {
                String str = strArr[i];
                MultiChannelLabels multiChannelLabels = (MultiChannelLabels) result.getData().get(str);
                if (multiChannelLabels != null) {
                    List<ChannelLabel> channelLabelList = multiChannelLabels.getChannelLabels().getChannelLabelList();
                    Collection arrayList3 = new ArrayList();
                    if (channelLabelList != null && channelLabelList.size() > 0) {
                        for (ChannelLabel channelLabel : channelLabelList) {
                            if (channelLabel.getType() != ResourceType.LIVE || channelLabel.checkLive()) {
                                arrayList3.add(channelLabel);
                            }
                        }
                    }
                    if (arrayList3 == null || arrayList3.size() <= 0) {
                        this.a.onFailure(new ApiException("error with tempList", "-100", ErrorEvent.HTTP_CODE_SUCCESS, null));
                        return;
                    }
                    if (str.equals(this.f141a.f134a.recRes)) {
                        if (arrayList3.size() < 6) {
                            for (int size = arrayList3.size(); size < 6; size++) {
                                arrayList3.add(new ChannelLabel());
                            }
                        }
                        arrayList.addAll(arrayList3.subList(0, 6));
                        if (!(a == null || a.a() == null || !a.a().a(str))) {
                            USALog.d("Add Cache Recommend Data size:" + arrayList.size());
                            a.a().a(str, arrayList);
                        }
                    } else {
                        arrayList2.addAll(arrayList3);
                        if (!(a == null || a.a() == null || !a.a().a(str))) {
                            USALog.d("Add Cache Recommend Data size:" + arrayList2.size());
                            a.a().a(str, arrayList2);
                        }
                    }
                    i++;
                } else {
                    this.a.onFailure(new ApiException("error with multiChannalLabel List ", "-100", ErrorEvent.HTTP_CODE_SUCCESS, null));
                    return;
                }
            }
            arrayList.addAll(arrayList2);
            if (arrayList == null || arrayList.size() <= 0) {
                this.a.onFailure(new ApiException(null, "-100", ErrorEvent.HTTP_CODE_SUCCESS, null));
            } else if (this.f141a.f134a.equals("1000004")) {
                this.f141a.a(this.a, arrayList);
            } else {
                this.a.onSuccess(arrayList);
            }
        }
    }

    private class ViewerShipCallback implements IVrsCallback<ApiResultViewership> {
        private IChannelLabelsCallback a;
        final /* synthetic */ ChannelResourceSet f143a;
        private List<ChannelLabel> f144a;

        public ViewerShipCallback(ChannelResourceSet channelResourceSet, IChannelLabelsCallback callback, List<ChannelLabel> list) {
            this.f143a = channelResourceSet;
            this.a = callback;
            this.f144a = list;
        }

        public void onException(ApiException e) {
            USALog.d((Object) "get Viewership failed");
            this.a.onSuccess(this.f144a);
        }

        public void onSuccess(ApiResultViewership result) {
            if (!(result == null || result.data == null || result.data.size() <= 0)) {
                int i = 0;
                while (i < result.data.size() && i < this.f144a.size()) {
                    ChannelLabel channelLabel = (ChannelLabel) this.f144a.get(i);
                    channelLabel.viewerShip = (String) ((Map) result.data.get(i)).get(channelLabel.itemId);
                    i++;
                }
            }
            this.a.onSuccess(this.f144a);
        }
    }

    public ChannelResourceSet(String channelId, boolean isRunPlayList) {
        this.f136a = channelId;
        this.f137a = isRunPlayList;
        this.f134a = g.a().a(this.f136a);
    }

    public ChannelResourceSet(String channelId, boolean isRunPlayList, Tag tag) {
        this.f136a = channelId;
        this.f137a = isRunPlayList;
        this.f135a = tag;
        this.f134a = g.a().a(this.f136a);
    }

    public boolean isRunPlayList() {
        return this.f137a;
    }

    public QLayoutKind getLayoutKind() {
        if (this.f135a != null) {
            return this.f135a.getLayout();
        }
        return SetTool.setLayoutKind(this.f136a);
    }

    public int getAlbumCount() {
        return this.a;
    }

    public void loadDataAsync(final IChannelLabelsCallback callback, final Tag[] tag, String version) {
        ThreadUtils.execute(new Runnable(this) {
            final /* synthetic */ ChannelResourceSet f138a;

            public void run() {
                int i = 0;
                if (callback == null) {
                    throw new NullPointerException("A callback is needed for AlbumProvider");
                } else if (tag == null || tag.length <= 0) {
                    throw new NullPointerException("one or more Tags are needed for AlbumProvider");
                } else {
                    if (d.a().a(this.f138a.f134a)) {
                        b a = d.a().a(this.f138a.f134a, false);
                        if (!(a == null || a.a() == null)) {
                            final List cacheData = this.f138a.getCacheData(a, tag);
                            if (cacheData != null && cacheData.size() > 0) {
                                USALog.d("Get Cache recommed data" + cacheData.size());
                                AlbumProviderApi.getAlbumProvider().getProperty().getExecutorService().execute(new Runnable(this) {
                                    final /* synthetic */ AnonymousClass1 a;

                                    public void run() {
                                        callback.onSuccess(cacheData);
                                    }
                                });
                                return;
                            }
                        }
                    }
                    String[] strArr = new String[tag.length];
                    while (i < tag.length) {
                        strArr[i] = tag[i].getID();
                        USALog.d("call id [" + i + "]: " + strArr[i]);
                        i++;
                    }
                    VrsHelper.multiChannelLabels.call(new MultiChannelLabelsCallback(this.f138a, callback, strArr), strArr);
                }
            }
        });
    }

    public List<ChannelLabel> getCacheData(b data, Tag[] tag) {
        List<ChannelLabel> arrayList = new ArrayList();
        Collection arrayList2 = new ArrayList();
        for (Tag tag2 : tag) {
            Collection a = data.a().a(tag2.getID());
            if (a != null && a.size() > 0) {
                if (tag2.getID().equals(this.f134a.recRes)) {
                    arrayList.addAll(a);
                } else {
                    arrayList2.addAll(a);
                }
            }
        }
        arrayList.addAll(arrayList2);
        return arrayList;
    }

    private void a(IChannelLabelsCallback iChannelLabelsCallback, List<ChannelLabel> list) {
        int i = 0;
        int i2 = 0;
        String str = "";
        while (i < 50 && i < list.size()) {
            int i3;
            ChannelLabel channelLabel = (ChannelLabel) list.get(i);
            str = str + "," + channelLabel.itemId;
            if (i2 == 0 && channelLabel.getType() == ResourceType.LIVE) {
                i3 = 1;
            } else {
                i3 = i2;
            }
            i++;
            i2 = i3;
        }
        if (i2 != 0) {
            USALog.d((Object) "need Viewership");
            VrsHelper.viewership.call(new ViewerShipCallback(this, iChannelLabelsCallback, list), str.replaceFirst(",", ""));
            return;
        }
        iChannelLabelsCallback.onSuccess(list);
    }

    public String getBackground() {
        return this.b;
    }

    public int getSearchCount() {
        return this.a;
    }
}
