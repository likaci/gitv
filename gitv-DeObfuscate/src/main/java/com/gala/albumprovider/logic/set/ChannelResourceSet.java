package com.gala.albumprovider.logic.set;

import com.gala.albumprovider.AlbumProviderApi;
import com.gala.albumprovider.base.IChannelLabelsCallback;
import com.gala.albumprovider.model.QChannel;
import com.gala.albumprovider.model.QLayoutKind;
import com.gala.albumprovider.model.Tag;
import com.gala.albumprovider.p001private.C0045h;
import com.gala.albumprovider.p001private.C0062b;
import com.gala.albumprovider.p001private.C0064d;
import com.gala.albumprovider.p001private.C0067g;
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

public class ChannelResourceSet extends C0045h {
    private int f172a = 0;
    private QChannel f173a;
    private Tag f174a;
    private String f175a = "";
    private boolean f176a = false;
    private String f177b = "";
    private String f178c = "AlbumProvider";

    private class MultiChannelLabelsCallback implements IVrsCallback<ApiResultMultiChannelLabels> {
        private IChannelLabelsCallback f166a;
        final /* synthetic */ ChannelResourceSet f167a;
        private String[] f168a;

        MultiChannelLabelsCallback(ChannelResourceSet channelResourceSet, IChannelLabelsCallback callback, String... id) {
            this.f167a = channelResourceSet;
            this.f166a = callback;
            this.f168a = id;
        }

        public void onException(ApiException arg0) {
            this.f166a.onFailure(arg0);
        }

        public void onSuccess(ApiResultMultiChannelLabels result) {
            if (result == null || result.getData() == null || this.f168a.length != result.getData().size()) {
                this.f166a.onFailure(new ApiException(null, "-100", ErrorEvent.HTTP_CODE_SUCCESS, null));
                return;
            }
            C0062b a;
            if (C0064d.m122a().m126a(this.f167a.f173a)) {
                a = C0064d.m122a().m127a(this.f167a.f173a, true);
            } else {
                a = null;
            }
            List arrayList = new ArrayList(6);
            Collection arrayList2 = new ArrayList();
            String[] strArr = this.f168a;
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
                        this.f166a.onFailure(new ApiException("error with tempList", "-100", ErrorEvent.HTTP_CODE_SUCCESS, null));
                        return;
                    }
                    if (str.equals(this.f167a.f173a.recRes)) {
                        if (arrayList3.size() < 6) {
                            for (int size = arrayList3.size(); size < 6; size++) {
                                arrayList3.add(new ChannelLabel());
                            }
                        }
                        arrayList.addAll(arrayList3.subList(0, 6));
                        if (!(a == null || a.m101a() == null || !a.m101a().m136a(str))) {
                            USALog.m147d("Add Cache Recommend Data size:" + arrayList.size());
                            a.m101a().m137a(str, arrayList);
                        }
                    } else {
                        arrayList2.addAll(arrayList3);
                        if (!(a == null || a.m101a() == null || !a.m101a().m136a(str))) {
                            USALog.m147d("Add Cache Recommend Data size:" + arrayList2.size());
                            a.m101a().m137a(str, arrayList2);
                        }
                    }
                    i++;
                } else {
                    this.f166a.onFailure(new ApiException("error with multiChannalLabel List ", "-100", ErrorEvent.HTTP_CODE_SUCCESS, null));
                    return;
                }
            }
            arrayList.addAll(arrayList2);
            if (arrayList == null || arrayList.size() <= 0) {
                this.f166a.onFailure(new ApiException(null, "-100", ErrorEvent.HTTP_CODE_SUCCESS, null));
            } else if (this.f167a.f173a.equals("1000004")) {
                this.f167a.m68a(this.f166a, arrayList);
            } else {
                this.f166a.onSuccess(arrayList);
            }
        }
    }

    private class ViewerShipCallback implements IVrsCallback<ApiResultViewership> {
        private IChannelLabelsCallback f169a;
        final /* synthetic */ ChannelResourceSet f170a;
        private List<ChannelLabel> f171a;

        public ViewerShipCallback(ChannelResourceSet channelResourceSet, IChannelLabelsCallback callback, List<ChannelLabel> list) {
            this.f170a = channelResourceSet;
            this.f169a = callback;
            this.f171a = list;
        }

        public void onException(ApiException e) {
            USALog.m147d((Object) "get Viewership failed");
            this.f169a.onSuccess(this.f171a);
        }

        public void onSuccess(ApiResultViewership result) {
            if (!(result == null || result.data == null || result.data.size() <= 0)) {
                int i = 0;
                while (i < result.data.size() && i < this.f171a.size()) {
                    ChannelLabel channelLabel = (ChannelLabel) this.f171a.get(i);
                    channelLabel.viewerShip = (String) ((Map) result.data.get(i)).get(channelLabel.itemId);
                    i++;
                }
            }
            this.f169a.onSuccess(this.f171a);
        }
    }

    public ChannelResourceSet(String channelId, boolean isRunPlayList) {
        this.f175a = channelId;
        this.f176a = isRunPlayList;
        this.f173a = C0067g.m139a().m141a(this.f175a);
    }

    public ChannelResourceSet(String channelId, boolean isRunPlayList, Tag tag) {
        this.f175a = channelId;
        this.f176a = isRunPlayList;
        this.f174a = tag;
        this.f173a = C0067g.m139a().m141a(this.f175a);
    }

    public boolean isRunPlayList() {
        return this.f176a;
    }

    public QLayoutKind getLayoutKind() {
        if (this.f174a != null) {
            return this.f174a.getLayout();
        }
        return SetTool.setLayoutKind(this.f175a);
    }

    public int getAlbumCount() {
        return this.f172a;
    }

    public void loadDataAsync(final IChannelLabelsCallback callback, final Tag[] tag, String version) {
        ThreadUtils.execute(new Runnable(this) {
            final /* synthetic */ ChannelResourceSet f164a;

            public void run() {
                int i = 0;
                if (callback == null) {
                    throw new NullPointerException("A callback is needed for AlbumProvider");
                } else if (tag == null || tag.length <= 0) {
                    throw new NullPointerException("one or more Tags are needed for AlbumProvider");
                } else {
                    if (C0064d.m122a().m126a(this.f164a.f173a)) {
                        C0062b a = C0064d.m122a().m127a(this.f164a.f173a, false);
                        if (!(a == null || a.m101a() == null)) {
                            final List cacheData = this.f164a.getCacheData(a, tag);
                            if (cacheData != null && cacheData.size() > 0) {
                                USALog.m147d("Get Cache recommed data" + cacheData.size());
                                AlbumProviderApi.getAlbumProvider().getProperty().getExecutorService().execute(new Runnable(this) {
                                    final /* synthetic */ C00541 f161a;

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
                        USALog.m147d("call id [" + i + "]: " + strArr[i]);
                        i++;
                    }
                    VrsHelper.multiChannelLabels.call(new MultiChannelLabelsCallback(this.f164a, callback, strArr), strArr);
                }
            }
        });
    }

    public List<ChannelLabel> getCacheData(C0062b data, Tag[] tag) {
        List<ChannelLabel> arrayList = new ArrayList();
        Collection arrayList2 = new ArrayList();
        for (Tag tag2 : tag) {
            Collection a = data.m101a().m136a(tag2.getID());
            if (a != null && a.size() > 0) {
                if (tag2.getID().equals(this.f173a.recRes)) {
                    arrayList.addAll(a);
                } else {
                    arrayList2.addAll(a);
                }
            }
        }
        arrayList.addAll(arrayList2);
        return arrayList;
    }

    private void m68a(IChannelLabelsCallback iChannelLabelsCallback, List<ChannelLabel> list) {
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
            USALog.m147d((Object) "need Viewership");
            VrsHelper.viewership.call(new ViewerShipCallback(this, iChannelLabelsCallback, list), str.replaceFirst(",", ""));
            return;
        }
        iChannelLabelsCallback.onSuccess(list);
    }

    public String getBackground() {
        return this.f177b;
    }

    public int getSearchCount() {
        return this.f172a;
    }
}
