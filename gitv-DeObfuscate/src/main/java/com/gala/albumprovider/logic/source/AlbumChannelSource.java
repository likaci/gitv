package com.gala.albumprovider.logic.source;

import android.annotation.SuppressLint;
import com.gala.albumprovider.AlbumProviderApi;
import com.gala.albumprovider.base.IAlbumSet;
import com.gala.albumprovider.base.ITagCallback;
import com.gala.albumprovider.logic.set.AlbumMultiChannelSet;
import com.gala.albumprovider.logic.set.AlbumPlayListSet;
import com.gala.albumprovider.logic.set.AlbumVideoSet;
import com.gala.albumprovider.logic.set.ChannelPlayListSet;
import com.gala.albumprovider.logic.set.ChannelResourceSet;
import com.gala.albumprovider.model.LibString;
import com.gala.albumprovider.model.QChannel;
import com.gala.albumprovider.model.QLayoutKind;
import com.gala.albumprovider.model.Tag;
import com.gala.albumprovider.p001private.C0060i;
import com.gala.albumprovider.p001private.C0062b;
import com.gala.albumprovider.p001private.C0064d;
import com.gala.albumprovider.p001private.C0067g;
import com.gala.albumprovider.util.ThreadUtils;
import com.gala.albumprovider.util.USALog;
import com.gala.tvapi.tv2.model.ThreeLevelTag;
import com.gala.tvapi.tv2.model.TwoLevelTag;
import com.gala.tvapi.vrs.IVrsCallback;
import com.gala.tvapi.vrs.VrsHelper;
import com.gala.tvapi.vrs.core.IVrsServer;
import com.gala.tvapi.vrs.model.ChannelLabel;
import com.gala.tvapi.vrs.model.IChannelItem;
import com.gala.tvapi.vrs.model.ItemKvs;
import com.gala.tvapi.vrs.result.ApiResultChannelLabels;
import com.gala.video.api.ApiException;
import com.gala.video.lib.share.uikit.data.data.Model.ErrorEvent;
import java.util.ArrayList;
import java.util.List;

public class AlbumChannelSource extends C0060i {
    private QChannel f220a = null;
    private QLayoutKind f221a = QLayoutKind.PORTRAIT;
    private final String f222a = "AlbumProvider";
    private List<TwoLevelTag> f223a = new ArrayList();
    private boolean f224a = false;
    private String f225b = null;
    private List<Tag> f226b = new ArrayList();
    private boolean f227b = false;
    private String f228c;
    private boolean f229c = false;
    private String f230d = "";
    private boolean f231d = false;
    private String f232e = "";
    private boolean f233e = true;
    private String f234f = null;
    private boolean f235f = true;
    private String f236g = "";
    private boolean f237g = false;
    private boolean f238h = false;

    private class ChannelLabelCallback implements IVrsCallback<ApiResultChannelLabels> {
        private ITagCallback f216a;
        final /* synthetic */ AlbumChannelSource f217a;

        ChannelLabelCallback(AlbumChannelSource albumChannelSource, ITagCallback callback) {
            this.f217a = albumChannelSource;
            this.f216a = callback;
        }

        public void onException(ApiException arg0) {
            this.f216a.onFailure(arg0);
        }

        public void onSuccess(ApiResultChannelLabels result) {
            if (result == null || result.getChannelLabels() == null) {
                this.f216a.onFailure(new ApiException(null, "-100", ErrorEvent.HTTP_CODE_SUCCESS, null));
                return;
            }
            for (ChannelLabel resourceItem : result.getChannelLabels().getChannelLabelList()) {
                IChannelItem resourceItem2 = resourceItem.getResourceItem();
                QLayoutKind qLayoutKind = QLayoutKind.MIXING;
                if (resourceItem2.style == 1) {
                    qLayoutKind = QLayoutKind.LANDSCAPE;
                } else if (resourceItem2.style == 2) {
                    qLayoutKind = QLayoutKind.PORTRAIT;
                }
                this.f217a.f226b.add(new Tag(resourceItem2.id, resourceItem2.title, SourceTool.LABEL_CHANNEL_TAG, qLayoutKind));
            }
            this.f217a.f226b;
            this.f216a.onSuccess(this.f217a.f226b);
        }
    }

    private class VipChannelLabelCallback implements IVrsCallback<ApiResultChannelLabels> {
        private ITagCallback f218a;
        final /* synthetic */ AlbumChannelSource f219a;

        VipChannelLabelCallback(AlbumChannelSource albumChannelSource, ITagCallback callback) {
            this.f219a = albumChannelSource;
            this.f218a = callback;
        }

        public void onException(ApiException arg0) {
            this.f218a.onFailure(arg0);
        }

        @SuppressLint({"DefaultLocale"})
        public void onSuccess(ApiResultChannelLabels result) {
            if (result == null || result.getChannelLabels() == null) {
                this.f218a.onFailure(new ApiException(null, "-100", ErrorEvent.HTTP_CODE_SUCCESS, null));
                return;
            }
            List arrayList = new ArrayList(5);
            List channelLabelList = result.getChannelLabels().getChannelLabelList();
            USALog.m147d("channel label size = " + channelLabelList.size());
            for (int i = 0; i < channelLabelList.size(); i++) {
                ChannelLabel channelLabel = (ChannelLabel) channelLabelList.get(i);
                if (channelLabel.isVipTags() && channelLabel.isFirstLevelTag()) {
                    Tag tag;
                    ItemKvs itemKvs = channelLabel.getItemKvs();
                    QLayoutKind qLayoutKind = QLayoutKind.PORTRAIT;
                    if (itemKvs.vip_listStyle.equals("1")) {
                        tag = new Tag(itemKvs.vip_dataId, channelLabel.itemName, SourceTool.LABEL_CHANNEL_TAG, qLayoutKind, itemKvs.vip_chnId, String.valueOf(channelLabel.channelId));
                    } else if (itemKvs.vip_listStyle.equals("2")) {
                        tag = new Tag(itemKvs.vip_dataId, channelLabel.itemName, SourceTool.LABEL_CHANNEL_TAG, QLayoutKind.LANDSCAPE, itemKvs.vip_chnId, String.valueOf(channelLabel.channelId));
                    } else if (itemKvs.vip_listStyle.equals("3")) {
                        tag = new Tag(itemKvs.vip_dataId, channelLabel.itemName, SourceTool.REC_CHANNEL_TAG, qLayoutKind, itemKvs.vip_chnId, String.valueOf(channelLabel.channelId));
                    } else if (itemKvs.vip_listStyle.equals("4")) {
                        tag = new Tag(itemKvs.vip_dataId, channelLabel.itemName, SourceTool.CARD_TAG, qLayoutKind, itemKvs.vip_chnId, String.valueOf(channelLabel.channelId));
                    } else {
                        tag = null;
                    }
                    if (tag != null) {
                        tag.setSource(channelLabel.sourceId);
                        tag.setResourceType(itemKvs.vip_dataType);
                        tag.setIcon(itemKvs.vip_tagIcon);
                        tag.setFocusIcon(itemKvs.vip_tagIconFocus);
                        tag.setLevel(1);
                        if (!(itemKvs == null || itemKvs.available == null)) {
                            tag.setAvailable(itemKvs.available);
                        }
                        USALog.m147d("level1-" + tag.getName() + "-" + tag.getID() + "-" + tag.getLayout());
                        List arrayList2 = new ArrayList();
                        for (int i2 = i + 1; i2 < channelLabelList.size(); i2++) {
                            channelLabel = (ChannelLabel) channelLabelList.get(i2);
                            if (channelLabel.isVipTags() && channelLabel.isFirstLevelTag()) {
                                break;
                            }
                            if (channelLabel.isVipTags() && channelLabel.isSecondLevelTag()) {
                                ItemKvs itemKvs2 = channelLabel.getItemKvs();
                                qLayoutKind = QLayoutKind.PORTRAIT;
                                Tag tag2 = null;
                                if (itemKvs2.vip_listStyle.equals("1")) {
                                    tag2 = new Tag(itemKvs2.vip_dataId, channelLabel.itemName, SourceTool.LABEL_CHANNEL_TAG, qLayoutKind, itemKvs2.vip_chnId, String.valueOf(channelLabel.channelId));
                                } else if (itemKvs2.vip_listStyle.equals("2")) {
                                    tag2 = new Tag(itemKvs2.vip_dataId, channelLabel.itemName, SourceTool.LABEL_CHANNEL_TAG, QLayoutKind.LANDSCAPE, itemKvs2.vip_chnId, String.valueOf(channelLabel.channelId));
                                } else if (itemKvs2.vip_listStyle.equals("3")) {
                                    tag2 = new Tag(itemKvs2.vip_dataId, channelLabel.itemName, SourceTool.REC_CHANNEL_TAG, qLayoutKind, itemKvs2.vip_chnId, String.valueOf(channelLabel.channelId));
                                } else if (itemKvs.vip_listStyle.equals("4")) {
                                    tag2 = new Tag(itemKvs2.vip_dataId, channelLabel.itemName, SourceTool.CARD_TAG, qLayoutKind, itemKvs2.vip_chnId, String.valueOf(channelLabel.channelId));
                                }
                                if (tag2 != null) {
                                    tag2.setSource(channelLabel.sourceId);
                                    USALog.m147d("level2-" + tag2.getName() + "-" + tag2.getID() + "-" + tag2.getLayout());
                                    tag2.setResourceType(itemKvs2.vip_dataType);
                                    tag2.setIcon(itemKvs2.vip_tagIcon);
                                    tag2.setFocusIcon(itemKvs2.vip_tagIconFocus);
                                    tag2.setLevel(2);
                                    tag2.setParentName(tag.getName());
                                    if (!(itemKvs2 == null || itemKvs2.available == null)) {
                                        tag2.setAvailable(itemKvs2.available);
                                    }
                                    arrayList2.add(tag2);
                                }
                            }
                        }
                        tag.setTagsList(arrayList2);
                        arrayList.add(tag);
                    }
                }
            }
            this.f218a.onSuccess(arrayList);
        }
    }

    public AlbumChannelSource(String id, boolean isFree) {
        this.f225b = id;
        this.f220a = C0067g.m139a().m141a(id);
        if (this.f220a != null) {
            m90a(this.f220a);
        }
        this.f233e = isFree;
    }

    public AlbumChannelSource(String id, boolean isFree, boolean isHasLive) {
        this.f225b = id;
        this.f220a = C0067g.m139a().m141a(id);
        if (this.f220a != null) {
            m90a(this.f220a);
        }
        this.f233e = isFree;
        this.f238h = isHasLive;
    }

    public AlbumChannelSource(String id, boolean isFree, boolean isHasLive, boolean isSupportInitMovie) {
        this.f225b = id;
        this.f220a = C0067g.m139a().m141a(id);
        if (this.f220a != null) {
            m90a(this.f220a);
        }
        this.f233e = isFree;
        this.f238h = isHasLive;
    }

    public AlbumChannelSource(String id, String fromId, boolean isFree) {
        this.f225b = id;
        this.f220a = C0067g.m139a().m141a(id);
        if (this.f220a != null) {
            m90a(this.f220a);
        }
        this.f233e = isFree;
    }

    public AlbumChannelSource(boolean isFree) {
        this.f233e = isFree;
    }

    private void m90a(QChannel qChannel) {
        if (qChannel != null) {
            this.f228c = qChannel.name;
            this.f223a = qChannel.tags;
            this.f224a = qChannel.hasChannelLabels();
            this.f227b = qChannel.hasRecommendList();
            this.f229c = qChannel.hasPlayList();
            this.f231d = qChannel.isRun();
            this.f230d = qChannel.recTag;
            this.f232e = qChannel.focus;
            this.f234f = qChannel.recRes;
            this.f235f = qChannel.isChannel();
            this.f237g = qChannel.isVirtual();
            this.f236g = qChannel.qipuId;
            this.f221a = qChannel.getLayoutKind();
        }
    }

    public String getChannelId() {
        return this.f225b;
    }

    public String getChannelName() {
        return this.f228c;
    }

    public Tag getDefaultTag() {
        if (this.f225b == null || !this.f237g) {
            return null;
        }
        return new Tag("0", LibString.DefaultTagName, SourceTool.VIRTUAL_CHANNEL_TAG);
    }

    public boolean HasResourceItems() {
        return this.f224a;
    }

    public boolean HasRecommand() {
        return this.f227b;
    }

    public boolean HasPlayList() {
        return this.f229c;
    }

    public boolean isRunPlayList() {
        return this.f231d;
    }

    public List<TwoLevelTag> getMultiTags() {
        return this.f223a;
    }

    public void getTags(final ITagCallback callback) {
        ThreadUtils.execute(new Runnable(this) {
            final /* synthetic */ AlbumChannelSource f212a;

            public void run() {
                this.f212a.m86a(callback);
            }
        });
    }

    private void m86a(ITagCallback iTagCallback) {
        if (iTagCallback == null) {
            throw new NullPointerException("A callback is needed for AlbumProvider");
        }
        if (this.f220a == null) {
            this.f220a = C0067g.m139a().m141a(this.f225b);
            if (this.f220a != null) {
                m90a(this.f220a);
            }
        }
        this.f226b.clear();
        USALog.m147d((Object) "tag clear finished");
        if (this.f220a != null) {
            switch (this.f220a.type) {
                case 0:
                    USALog.m147d((Object) "实体频道");
                    m91b(iTagCallback);
                    return;
                case 1:
                    USALog.m147d((Object) "主题频道");
                    this.f226b.add(new Tag("0", LibString.DefaultTagName, "-100", this.f220a.getLayoutKind()));
                    m87a(iTagCallback, this.f226b);
                    return;
                case 3:
                    USALog.m147d((Object) "功能频道");
                    IVrsServer iVrsServer = VrsHelper.channelLabels;
                    IVrsCallback vipChannelLabelCallback = new VipChannelLabelCallback(this, iTagCallback);
                    String[] strArr = new String[2];
                    strArr[0] = this.f220a.qipuId;
                    strArr[1] = this.f233e ? "1" : "0";
                    iVrsServer.call(vipChannelLabelCallback, strArr);
                    return;
                default:
                    USALog.m150e("未知频道 channel =  " + this.f220a.toString());
                    m87a(iTagCallback, this.f226b);
                    return;
            }
        }
        USALog.m150e((Object) "channel == null");
        m87a(iTagCallback, this.f226b);
    }

    private void m91b(ITagCallback iTagCallback) {
        if (C0064d.m122a().m126a(this.f225b)) {
            C0062b a = C0064d.m122a().m127a(this.f225b, false);
            if (a.m101a() != null && a.m101a().size() > 0) {
                USALog.m147d("get tag list from cache. channelID = " + this.f225b);
                m87a(iTagCallback, a.m101a());
                return;
            }
        }
        if (this.f227b) {
            USALog.m147d((Object) "有推荐标签1");
            if (this.f225b.equals("5")) {
                USALog.m147d((Object) "有推荐标签1>音乐频道--横图");
                this.f226b.add(new Tag(this.f232e, LibString.RecommendTagName, SourceTool.REC_CHANNEL_TAG, QLayoutKind.LANDSCAPE));
            } else {
                USALog.m147d((Object) "有推荐标签1>非音乐频道--竖图");
                this.f226b.add(new Tag(this.f232e, LibString.RecommendTagName, SourceTool.REC_CHANNEL_TAG, QLayoutKind.PORTRAIT));
            }
        }
        if (!(this.f234f == null || this.f234f.equals(""))) {
            USALog.m147d((Object) "有推荐标签2");
            this.f226b.add(new Tag(this.f234f, LibString.RecommendTagName, SourceTool.REC_CHANNEL_TAG, QLayoutKind.PORTRAIT));
        }
        if (this.f235f && this.f223a != null && this.f223a.size() > 0) {
            String str = "";
            QLayoutKind layoutKind = SourceTool.setLayoutKind(this.f225b);
            for (TwoLevelTag twoLevelTag : this.f223a) {
                List list = twoLevelTag.tags;
                if (list != null && list.size() > 0) {
                    int i = 0;
                    while (i < list.size()) {
                        String str2;
                        if (((ThreeLevelTag) list.get(i)).f1016v.equals("11;sort")) {
                            USALog.m147d((Object) "有最热标签");
                            this.f226b.add(new Tag(str + "11;sort", LibString.HotTagName, "-100", layoutKind));
                            str2 = str;
                        } else if (((ThreeLevelTag) list.get(i)).f1016v.equals("4;sort")) {
                            USALog.m147d((Object) "有最新标签");
                            this.f226b.add(new Tag(str + "4;sort", LibString.NewestTagName, "-100", layoutKind));
                            str2 = str;
                        } else if (i == 0) {
                            str2 = str + ((ThreeLevelTag) twoLevelTag.tags.get(0)).f1016v + ",";
                        } else {
                            str2 = str;
                        }
                        i++;
                        str = str2;
                    }
                }
            }
        }
        if (this.f229c) {
            USALog.m147d((Object) "有专题标签");
            this.f226b.add(new Tag(this.f225b, LibString.ChannelPlayListTagName, SourceTool.PLAY_CHANNEL_TAG, this.f220a.getLayoutKind()));
        }
        if (this.f224a) {
            USALog.m147d((Object) "有其他播单标签-即自定义标签");
            IVrsServer iVrsServer = VrsHelper.channelLabelsFilter;
            IVrsCallback channelLabelCallback = new ChannelLabelCallback(this, iTagCallback);
            String[] strArr = new String[3];
            strArr[0] = this.f230d;
            strArr[1] = "2";
            strArr[2] = this.f233e ? "1" : "0";
            iVrsServer.call(channelLabelCallback, strArr);
            return;
        }
        m85a();
        m87a(iTagCallback, this.f226b);
    }

    private void m87a(final ITagCallback iTagCallback, final List<Tag> list) {
        AlbumProviderApi.getAlbumProvider().getProperty().getExecutorService().execute(new Runnable(this) {
            final /* synthetic */ AlbumChannelSource f214a;

            public void run() {
                iTagCallback.onSuccess(list);
            }
        });
    }

    public IAlbumSet getAlbumSet(Tag tag) {
        if (tag == null || this.f225b == null || this.f220a == null) {
            USALog.m150e("tag==null || channelId == null || channel==null----tag=" + tag + " ;channelId = " + this.f225b + " ;channel = " + this.f220a);
            return null;
        }
        switch (this.f220a.type) {
            case 0:
                String type = tag.getType();
                if (type.equals(SourceTool.REC_CHANNEL_TAG)) {
                    USALog.m147d("实体频道-推荐: channelId = " + this.f225b);
                    return new ChannelResourceSet(this.f225b, false);
                } else if (type.equals("-100")) {
                    USALog.m147d("实体频道-MULTI_CHANNEL_TAG: channelId = " + this.f225b);
                    return new AlbumMultiChannelSet(this.f225b, "", this.f228c, tag, 0);
                } else if (type.equals(SourceTool.PLAY_CHANNEL_TAG)) {
                    USALog.m147d("实体频道-专题: channelId = " + this.f225b);
                    return new ChannelPlayListSet(this.f225b, this.f231d, this.f233e);
                } else if (type.equals(SourceTool.LABEL_CHANNEL_TAG)) {
                    USALog.m147d("实体频道-播单标签: channelId = " + this.f225b);
                    return new AlbumPlayListSet(this.f225b, tag.getID(), tag.getName(), false, this.f233e, tag.getLayout(), true);
                }
                break;
            case 1:
                USALog.m147d("主题频道:channelId =  " + this.f225b);
                return new AlbumPlayListSet(this.f225b, this.f236g, tag.getName(), false, this.f233e, this.f221a, false);
            case 2:
                USALog.m147d("虚拟频道: channelId = " + this.f225b);
                return new AlbumMultiChannelSet(tag.getID(), this.f225b, this.f228c, tag, 1);
            case 3:
                if (tag.getResourceType().toLowerCase().equals("resource")) {
                    USALog.m147d("功能频道 -资源位: channelId = " + this.f225b);
                    return new ChannelResourceSet(this.f225b, false, tag);
                } else if (tag.getResourceType().toLowerCase().equals(SourceTool.TVTAG)) {
                    USALog.m147d("功能频道 -TVTAG: channelId = " + this.f225b);
                    return new AlbumMultiChannelSet(this.f225b, "", this.f228c, tag, 2);
                } else if (tag.getResourceType().toLowerCase().equals("album")) {
                    USALog.m147d("功能频道 -剧集  :channelId = " + this.f225b);
                    return new AlbumVideoSet(tag);
                } else if (tag.getResourceType().toLowerCase().equals(SourceTool.PLAYLIST_TYPE)) {
                    USALog.m147d("功能频道 -播单: channelId =  " + this.f225b);
                    return new AlbumPlayListSet(this.f225b, tag.getID(), tag.getName(), false, this.f233e, tag.getLayout(), false);
                }
                break;
        }
        USALog.m150e((Object) "channel is null");
        return null;
    }

    private void m85a() {
        if (C0064d.m122a().m126a(this.f225b)) {
            USALog.m147d((Object) "Add Cache channel Label tag");
            C0062b a = C0064d.m122a().m127a(this.f225b, true);
            if (a != null) {
                a.m110a(this.f226b);
            }
        }
    }
}
