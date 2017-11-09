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
import com.gala.albumprovider.private.b;
import com.gala.albumprovider.private.d;
import com.gala.albumprovider.private.g;
import com.gala.albumprovider.private.i;
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

public class AlbumChannelSource extends i {
    private QChannel a = null;
    private QLayoutKind f166a = QLayoutKind.PORTRAIT;
    private final String f167a = "AlbumProvider";
    private List<TwoLevelTag> f168a = new ArrayList();
    private boolean f169a = false;
    private String b = null;
    private List<Tag> f170b = new ArrayList();
    private boolean f171b = false;
    private String c;
    private boolean f172c = false;
    private String d = "";
    private boolean f173d = false;
    private String e = "";
    private boolean f174e = true;
    private String f = null;
    private boolean f175f = true;
    private String g = "";
    private boolean f176g = false;
    private boolean h = false;

    private class ChannelLabelCallback implements IVrsCallback<ApiResultChannelLabels> {
        private ITagCallback a;
        final /* synthetic */ AlbumChannelSource f180a;

        ChannelLabelCallback(AlbumChannelSource albumChannelSource, ITagCallback callback) {
            this.f180a = albumChannelSource;
            this.a = callback;
        }

        public void onException(ApiException arg0) {
            this.a.onFailure(arg0);
        }

        public void onSuccess(ApiResultChannelLabels result) {
            if (result == null || result.getChannelLabels() == null) {
                this.a.onFailure(new ApiException(null, "-100", ErrorEvent.HTTP_CODE_SUCCESS, null));
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
                this.f180a.f170b.add(new Tag(resourceItem2.id, resourceItem2.title, SourceTool.LABEL_CHANNEL_TAG, qLayoutKind));
            }
            this.f180a.f170b;
            this.a.onSuccess(this.f180a.f170b);
        }
    }

    private class VipChannelLabelCallback implements IVrsCallback<ApiResultChannelLabels> {
        private ITagCallback a;
        final /* synthetic */ AlbumChannelSource f181a;

        VipChannelLabelCallback(AlbumChannelSource albumChannelSource, ITagCallback callback) {
            this.f181a = albumChannelSource;
            this.a = callback;
        }

        public void onException(ApiException arg0) {
            this.a.onFailure(arg0);
        }

        @SuppressLint({"DefaultLocale"})
        public void onSuccess(ApiResultChannelLabels result) {
            if (result == null || result.getChannelLabels() == null) {
                this.a.onFailure(new ApiException(null, "-100", ErrorEvent.HTTP_CODE_SUCCESS, null));
                return;
            }
            List arrayList = new ArrayList(5);
            List channelLabelList = result.getChannelLabels().getChannelLabelList();
            USALog.d("channel label size = " + channelLabelList.size());
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
                        USALog.d("level1-" + tag.getName() + "-" + tag.getID() + "-" + tag.getLayout());
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
                                    USALog.d("level2-" + tag2.getName() + "-" + tag2.getID() + "-" + tag2.getLayout());
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
            this.a.onSuccess(arrayList);
        }
    }

    public AlbumChannelSource(String id, boolean isFree) {
        this.b = id;
        this.a = g.a().a(id);
        if (this.a != null) {
            a(this.a);
        }
        this.f174e = isFree;
    }

    public AlbumChannelSource(String id, boolean isFree, boolean isHasLive) {
        this.b = id;
        this.a = g.a().a(id);
        if (this.a != null) {
            a(this.a);
        }
        this.f174e = isFree;
        this.h = isHasLive;
    }

    public AlbumChannelSource(String id, boolean isFree, boolean isHasLive, boolean isSupportInitMovie) {
        this.b = id;
        this.a = g.a().a(id);
        if (this.a != null) {
            a(this.a);
        }
        this.f174e = isFree;
        this.h = isHasLive;
    }

    public AlbumChannelSource(String id, String fromId, boolean isFree) {
        this.b = id;
        this.a = g.a().a(id);
        if (this.a != null) {
            a(this.a);
        }
        this.f174e = isFree;
    }

    public AlbumChannelSource(boolean isFree) {
        this.f174e = isFree;
    }

    private void a(QChannel qChannel) {
        if (qChannel != null) {
            this.c = qChannel.name;
            this.f168a = qChannel.tags;
            this.f169a = qChannel.hasChannelLabels();
            this.f171b = qChannel.hasRecommendList();
            this.f172c = qChannel.hasPlayList();
            this.f173d = qChannel.isRun();
            this.d = qChannel.recTag;
            this.e = qChannel.focus;
            this.f = qChannel.recRes;
            this.f175f = qChannel.isChannel();
            this.f176g = qChannel.isVirtual();
            this.g = qChannel.qipuId;
            this.f166a = qChannel.getLayoutKind();
        }
    }

    public String getChannelId() {
        return this.b;
    }

    public String getChannelName() {
        return this.c;
    }

    public Tag getDefaultTag() {
        if (this.b == null || !this.f176g) {
            return null;
        }
        return new Tag("0", LibString.DefaultTagName, SourceTool.VIRTUAL_CHANNEL_TAG);
    }

    public boolean HasResourceItems() {
        return this.f169a;
    }

    public boolean HasRecommand() {
        return this.f171b;
    }

    public boolean HasPlayList() {
        return this.f172c;
    }

    public boolean isRunPlayList() {
        return this.f173d;
    }

    public List<TwoLevelTag> getMultiTags() {
        return this.f168a;
    }

    public void getTags(final ITagCallback callback) {
        ThreadUtils.execute(new Runnable(this) {
            final /* synthetic */ AlbumChannelSource f177a;

            public void run() {
                this.f177a.a(callback);
            }
        });
    }

    private void a(ITagCallback iTagCallback) {
        if (iTagCallback == null) {
            throw new NullPointerException("A callback is needed for AlbumProvider");
        }
        if (this.a == null) {
            this.a = g.a().a(this.b);
            if (this.a != null) {
                a(this.a);
            }
        }
        this.f170b.clear();
        USALog.d((Object) "tag clear finished");
        if (this.a != null) {
            switch (this.a.type) {
                case 0:
                    USALog.d((Object) "实体频道");
                    b(iTagCallback);
                    return;
                case 1:
                    USALog.d((Object) "主题频道");
                    this.f170b.add(new Tag("0", LibString.DefaultTagName, "-100", this.a.getLayoutKind()));
                    a(iTagCallback, this.f170b);
                    return;
                case 3:
                    USALog.d((Object) "功能频道");
                    IVrsServer iVrsServer = VrsHelper.channelLabels;
                    IVrsCallback vipChannelLabelCallback = new VipChannelLabelCallback(this, iTagCallback);
                    String[] strArr = new String[2];
                    strArr[0] = this.a.qipuId;
                    strArr[1] = this.f174e ? "1" : "0";
                    iVrsServer.call(vipChannelLabelCallback, strArr);
                    return;
                default:
                    USALog.e("未知频道 channel =  " + this.a.toString());
                    a(iTagCallback, this.f170b);
                    return;
            }
        }
        USALog.e((Object) "channel == null");
        a(iTagCallback, this.f170b);
    }

    private void b(ITagCallback iTagCallback) {
        if (d.a().a(this.b)) {
            b a = d.a().a(this.b, false);
            if (a.a() != null && a.a().size() > 0) {
                USALog.d("get tag list from cache. channelID = " + this.b);
                a(iTagCallback, a.a());
                return;
            }
        }
        if (this.f171b) {
            USALog.d((Object) "有推荐标签1");
            if (this.b.equals("5")) {
                USALog.d((Object) "有推荐标签1>音乐频道--横图");
                this.f170b.add(new Tag(this.e, LibString.RecommendTagName, SourceTool.REC_CHANNEL_TAG, QLayoutKind.LANDSCAPE));
            } else {
                USALog.d((Object) "有推荐标签1>非音乐频道--竖图");
                this.f170b.add(new Tag(this.e, LibString.RecommendTagName, SourceTool.REC_CHANNEL_TAG, QLayoutKind.PORTRAIT));
            }
        }
        if (!(this.f == null || this.f.equals(""))) {
            USALog.d((Object) "有推荐标签2");
            this.f170b.add(new Tag(this.f, LibString.RecommendTagName, SourceTool.REC_CHANNEL_TAG, QLayoutKind.PORTRAIT));
        }
        if (this.f175f && this.f168a != null && this.f168a.size() > 0) {
            String str = "";
            QLayoutKind layoutKind = SourceTool.setLayoutKind(this.b);
            for (TwoLevelTag twoLevelTag : this.f168a) {
                List list = twoLevelTag.tags;
                if (list != null && list.size() > 0) {
                    int i = 0;
                    while (i < list.size()) {
                        String str2;
                        if (((ThreeLevelTag) list.get(i)).v.equals("11;sort")) {
                            USALog.d((Object) "有最热标签");
                            this.f170b.add(new Tag(str + "11;sort", LibString.HotTagName, "-100", layoutKind));
                            str2 = str;
                        } else if (((ThreeLevelTag) list.get(i)).v.equals("4;sort")) {
                            USALog.d((Object) "有最新标签");
                            this.f170b.add(new Tag(str + "4;sort", LibString.NewestTagName, "-100", layoutKind));
                            str2 = str;
                        } else if (i == 0) {
                            str2 = str + ((ThreeLevelTag) twoLevelTag.tags.get(0)).v + ",";
                        } else {
                            str2 = str;
                        }
                        i++;
                        str = str2;
                    }
                }
            }
        }
        if (this.f172c) {
            USALog.d((Object) "有专题标签");
            this.f170b.add(new Tag(this.b, LibString.ChannelPlayListTagName, SourceTool.PLAY_CHANNEL_TAG, this.a.getLayoutKind()));
        }
        if (this.f169a) {
            USALog.d((Object) "有其他播单标签-即自定义标签");
            IVrsServer iVrsServer = VrsHelper.channelLabelsFilter;
            IVrsCallback channelLabelCallback = new ChannelLabelCallback(this, iTagCallback);
            String[] strArr = new String[3];
            strArr[0] = this.d;
            strArr[1] = "2";
            strArr[2] = this.f174e ? "1" : "0";
            iVrsServer.call(channelLabelCallback, strArr);
            return;
        }
        a();
        a(iTagCallback, this.f170b);
    }

    private void a(final ITagCallback iTagCallback, final List<Tag> list) {
        AlbumProviderApi.getAlbumProvider().getProperty().getExecutorService().execute(new Runnable(this) {
            final /* synthetic */ AlbumChannelSource f178a;

            public void run() {
                iTagCallback.onSuccess(list);
            }
        });
    }

    public IAlbumSet getAlbumSet(Tag tag) {
        if (tag == null || this.b == null || this.a == null) {
            USALog.e("tag==null || channelId == null || channel==null----tag=" + tag + " ;channelId = " + this.b + " ;channel = " + this.a);
            return null;
        }
        switch (this.a.type) {
            case 0:
                String type = tag.getType();
                if (type.equals(SourceTool.REC_CHANNEL_TAG)) {
                    USALog.d("实体频道-推荐: channelId = " + this.b);
                    return new ChannelResourceSet(this.b, false);
                } else if (type.equals("-100")) {
                    USALog.d("实体频道-MULTI_CHANNEL_TAG: channelId = " + this.b);
                    return new AlbumMultiChannelSet(this.b, "", this.c, tag, 0);
                } else if (type.equals(SourceTool.PLAY_CHANNEL_TAG)) {
                    USALog.d("实体频道-专题: channelId = " + this.b);
                    return new ChannelPlayListSet(this.b, this.f173d, this.f174e);
                } else if (type.equals(SourceTool.LABEL_CHANNEL_TAG)) {
                    USALog.d("实体频道-播单标签: channelId = " + this.b);
                    return new AlbumPlayListSet(this.b, tag.getID(), tag.getName(), false, this.f174e, tag.getLayout(), true);
                }
                break;
            case 1:
                USALog.d("主题频道:channelId =  " + this.b);
                return new AlbumPlayListSet(this.b, this.g, tag.getName(), false, this.f174e, this.f166a, false);
            case 2:
                USALog.d("虚拟频道: channelId = " + this.b);
                return new AlbumMultiChannelSet(tag.getID(), this.b, this.c, tag, 1);
            case 3:
                if (tag.getResourceType().toLowerCase().equals("resource")) {
                    USALog.d("功能频道 -资源位: channelId = " + this.b);
                    return new ChannelResourceSet(this.b, false, tag);
                } else if (tag.getResourceType().toLowerCase().equals(SourceTool.TVTAG)) {
                    USALog.d("功能频道 -TVTAG: channelId = " + this.b);
                    return new AlbumMultiChannelSet(this.b, "", this.c, tag, 2);
                } else if (tag.getResourceType().toLowerCase().equals("album")) {
                    USALog.d("功能频道 -剧集  :channelId = " + this.b);
                    return new AlbumVideoSet(tag);
                } else if (tag.getResourceType().toLowerCase().equals(SourceTool.PLAYLIST_TYPE)) {
                    USALog.d("功能频道 -播单: channelId =  " + this.b);
                    return new AlbumPlayListSet(this.b, tag.getID(), tag.getName(), false, this.f174e, tag.getLayout(), false);
                }
                break;
        }
        USALog.e((Object) "channel is null");
        return null;
    }

    private void a() {
        if (d.a().a(this.b)) {
            USALog.d((Object) "Add Cache channel Label tag");
            b a = d.a().a(this.b, true);
            if (a != null) {
                a.a(this.f170b);
            }
        }
    }
}
