package com.gala.tvapi.vrs.model;

import android.annotation.SuppressLint;
import com.alibaba.fastjson.annotation.JSONField;
import com.gala.tvapi.b.a;
import com.gala.tvapi.tools.DateLocalThread;
import com.gala.tvapi.tools.TVApiTool;
import com.gala.tvapi.tv2.TVApiBase;
import com.gala.tvapi.tv2.model.Album;
import com.gala.tvapi.tv2.model.TVChannelCarousel;
import com.gala.tvapi.tv2.model.VipInfo;
import com.gala.tvapi.type.AlbumFrom;
import com.gala.tvapi.type.ContentType;
import com.gala.tvapi.type.LivePlayingType;
import com.gala.tvapi.type.ResourceType;
import com.gala.video.app.epg.ui.albumlist.utils.AlbumEnterFactory;
import com.qiyi.tv.client.feature.common.MediaFactory;
import java.util.ArrayList;
import java.util.List;

@SuppressLint({"DefaultLocale", "SimpleDateFormat"})
public class ChannelLabel extends Model {
    public static final int HORICAONTAL_LAYOUT = 0;
    public static final int VIRTUAL_LAYOUT = 1;
    private static final long serialVersionUID = 1;
    private final String ALBUM = MediaFactory.TYPE_ALBUM;
    private final String CHANNEL = "CHANNEL";
    private final String COLLECTION = "COLLECTION";
    private final String DIY = "DIY";
    private final String FIRST = "first";
    private final String LIVE_CHANNEL = "LIVE_CHANNEL";
    private final String LIVE_CHANNEL_TYPE = "LIVE_CHANNEL";
    private final String LIVE_PROGRAM = "LIVE_PROGRAM";
    private final String PERSON = "PEOPLE";
    private final String PSEUDO = "PSEUDO";
    private final String RESOURCE = "RESOURCE";
    private final String RESOURCE_GROUP = "RESOURCEGROUP";
    private final String SECOND = "second";
    private final String TVTAG = "TVTAG";
    private final String VIDEO = MediaFactory.TYPE_VIDEO;
    public AlbumFrom albumFrom = AlbumFrom.DEFAULT;
    public String albumImage = "";
    public String albumName = "";
    public String albumQipuId = "";
    public int boss = 0;
    public String cateName = "";
    public int categoryId = 0;
    public List<String> categoryNames;
    public int channelId = 0;
    public String channelName = "";
    public List<LiveAlbum> channels;
    public String contentType = "FEATURE_FILM";
    public String currentPeriod = "";
    public String desc = "";
    public String drmTypes;
    public String duration = "";
    public String dynamicRange = "";
    public long eTime = 0;
    public String endTime = "";
    public int exclusive = 0;
    public String fromTagId = "";
    public String id = "";
    public int imageStyle = 0;
    public String imageUrl = "";
    public int is1080P = 0;
    public int isD3 = 0;
    public int isDubi = 0;
    public int isSeries = 0;
    public String issueTime = "";
    public String issueTimeStamp = "";
    public String itemId = "";
    public String itemImageUrl = "";
    public ItemKvs itemKvs = null;
    public String itemName = "";
    public String itemOrder = "";
    public String itemPageUrl = "";
    public String itemPrompt = "";
    public String itemShortDisplayName = "";
    public String itemSubordinateTitle = "";
    public String itemType = "";
    public int latestOrder = 0;
    public String liveType = "";
    public String logo;
    public String name = "";
    public int order = -1;
    public int payMark;
    public String period = "";
    public PltRegionCtrls pltRegionCtrls;
    public String postImage = "";
    public String prompt = "";
    public int purchaseType = 0;
    public long sTime = 0;
    public String score = "0";
    public int season = 0;
    public String shortTitle = "";
    public String sourceId = "";
    public String startTime = "";
    public String subordinateTitle = "";
    public int tableNo;
    public int tvCount = 0;
    public String tvQipuId = "";
    public String type = "";
    public String vid = "";
    public Video video = null;
    public List<LiveAlbum> videos;
    public String viewerShip = "";

    enum TVType {
        ALBUM,
        VIDEO,
        SOURCE
    }

    @JSONField(name = "1080P")
    public void set1080P(int i) {
        this.is1080P = i;
    }

    public boolean is1080P() {
        return this.is1080P != 0;
    }

    @SuppressLint({"DefaultLocale"})
    public ResourceType getType() {
        if (this.itemKvs != null && this.itemKvs.vip_dataType.equals("CHANNEL")) {
            return ResourceType.CHANNEL;
        }
        if (this.itemType.equals(MediaFactory.TYPE_VIDEO)) {
            return ResourceType.VIDEO;
        }
        if (this.itemType.equals(MediaFactory.TYPE_ALBUM)) {
            return ResourceType.ALBUM;
        }
        if (this.itemType.equals("COLLECTION")) {
            return ResourceType.COLLECTION;
        }
        if (this.itemType.equals("LIVE_PROGRAM")) {
            if (this.channels == null || this.channels.size() <= 0 || !((LiveAlbum) this.channels.get(0)).liveType.equals("PSEUDO")) {
                return ResourceType.LIVE;
            }
            return ResourceType.PSEUDO;
        } else if (this.itemType.equals("PEOPLE")) {
            return ResourceType.PERSON;
        } else {
            if (this.itemType.equals("DIY")) {
                return ResourceType.DIY;
            }
            if (this.itemType.equals("LIVE_CHANNEL") && this.type.equals("LIVE_CHANNEL") && this.liveType.equals("PSEUDO")) {
                return ResourceType.LIVE_CHANNEL;
            }
            if (this.itemType.equals("RESOURCEGROUP")) {
                return ResourceType.RESOURCE_GROUP;
            }
            return ResourceType.DEFAULT;
        }
    }

    public Album getVideo() {
        Album album;
        String str;
        Album album2;
        int i;
        VipInfo vipInfo;
        ContentType contentType;
        int i2 = 1;
        Album album3 = new Album();
        if ((getTVType(this.sourceId, this.itemType) == TVType.ALBUM || getTVType(this.sourceId, this.itemType) == TVType.SOURCE) && this.video != null) {
            album3.tvQid = this.video.qipuId;
            album3.vid = this.video.vid;
            album = album3;
        } else {
            album3.tvQid = this.tvQipuId;
            album3.vid = this.vid;
            if (a.a(this.albumQipuId)) {
                str = this.tvQipuId;
                album2 = album3;
                album2.qpId = str;
                album3.exclusive = this.exclusive;
                if (this.is1080P == 1) {
                    if (album3.stream.length() != 0) {
                        album3.stream += "1080P";
                    } else {
                        album3.stream += ",1080P";
                    }
                }
                if (this.isDubi == 1) {
                    if (album3.stream.length() != 0) {
                        album3.stream += "720p_dolby";
                    } else {
                        album3.stream += ",720p_dolby";
                    }
                }
                album3.order = this.order;
                album3.chnId = this.channelId;
                album3.name = this.albumName;
                album3.tvPic = this.postImage;
                album3.pic = this.imageUrl;
                album3.sourceCode = this.sourceId;
                if (getType() != ResourceType.VIDEO) {
                    i = 0;
                } else {
                    i = 1;
                }
                album3.type = i;
                album3.isSeries = this.isSeries;
                if (this.boss != 2) {
                    i = 1;
                } else {
                    i = 0;
                }
                album3.isPurchase = i;
                album3.tvName = this.name;
                album3.score = this.score;
                album3.tvsets = this.tvCount;
                album3.is3D = this.isD3;
                album3.tvCount = this.latestOrder;
                if (this.issueTimeStamp != null || this.issueTimeStamp.equals("")) {
                    album3.initIssueTime = this.issueTime;
                } else {
                    album3.initIssueTime = this.issueTimeStamp;
                }
                album3.chnName = this.channelName;
                album3.len = this.duration;
                if (this.categoryNames != null && this.categoryNames.size() > 0) {
                    for (String str2 : this.categoryNames) {
                        album3.tag += str2 + ",";
                    }
                    if (!album3.tag.isEmpty()) {
                        album3.tag = album3.tag.substring(0, album3.tag.length() - 1);
                    }
                }
                album3.shortName = this.shortTitle;
                i = (this.boss == 2 || this.purchaseType != 2) ? 0 : 1;
                album3.indiviDemand = i;
                album3.score = this.score;
                album3.time = this.currentPeriod;
                vipInfo = new VipInfo();
                album3.payMarkType = TVApiTool.getPayMarkType(this.payMark);
                if (album3.type != 1) {
                    if (this.boss != 2) {
                        i = 1;
                    } else {
                        i = 0;
                    }
                    album3.isPurchase = i;
                    if (this.boss == 2 || this.purchaseType != 1) {
                        i = 0;
                    } else {
                        i = 1;
                    }
                    vipInfo.isVip = i;
                    if (this.boss == 2 || this.purchaseType != 2) {
                        i = 0;
                    } else {
                        i = 1;
                    }
                    vipInfo.isTvod = i;
                    if (this.boss == 2 || this.purchaseType != 3) {
                        i = 0;
                    } else {
                        i = 1;
                    }
                    vipInfo.isCoupon = i;
                } else {
                    album3.tvIsPurchase = this.boss != 2 ? 1 : 0;
                    if (this.boss == 2 || this.purchaseType != 1) {
                        i = 0;
                    } else {
                        i = 1;
                    }
                    vipInfo.epIsVip = i;
                    if (this.boss == 2 || this.purchaseType != 2) {
                        i = 0;
                    } else {
                        i = 1;
                    }
                    vipInfo.epIsTvod = i;
                    if (this.boss == 2 || this.purchaseType != 3) {
                        i = 0;
                    } else {
                        i = 1;
                    }
                    vipInfo.epIsCoupon = i;
                    if (!(this.drmTypes == null || this.drmTypes.isEmpty() || this.drmTypes.equals("[]"))) {
                        album3.drm = TVApiTool.getDrmType(this.drmTypes);
                    }
                    if (!(this.dynamicRange == null || this.dynamicRange.isEmpty() || this.dynamicRange.equals("[]"))) {
                        album3.dynamicRanges = TVApiTool.getHDRType(this.dynamicRange);
                    }
                }
                album3.vipInfo = vipInfo;
                contentType = getContentType();
                if (contentType != ContentType.TRAILER) {
                    i2 = 0;
                }
                album3.isFlower = i2;
                album3.contentType = contentType.getValue();
                return album3;
            }
            album = album3;
        }
        album2 = album;
        str2 = this.albumQipuId;
        album2.qpId = str2;
        album3.exclusive = this.exclusive;
        if (this.is1080P == 1) {
            if (album3.stream.length() != 0) {
                album3.stream += ",1080P";
            } else {
                album3.stream += "1080P";
            }
        }
        if (this.isDubi == 1) {
            if (album3.stream.length() != 0) {
                album3.stream += ",720p_dolby";
            } else {
                album3.stream += "720p_dolby";
            }
        }
        album3.order = this.order;
        album3.chnId = this.channelId;
        album3.name = this.albumName;
        album3.tvPic = this.postImage;
        album3.pic = this.imageUrl;
        album3.sourceCode = this.sourceId;
        if (getType() != ResourceType.VIDEO) {
            i = 1;
        } else {
            i = 0;
        }
        album3.type = i;
        album3.isSeries = this.isSeries;
        if (this.boss != 2) {
            i = 0;
        } else {
            i = 1;
        }
        album3.isPurchase = i;
        album3.tvName = this.name;
        album3.score = this.score;
        album3.tvsets = this.tvCount;
        album3.is3D = this.isD3;
        album3.tvCount = this.latestOrder;
        if (this.issueTimeStamp != null) {
        }
        album3.initIssueTime = this.issueTime;
        album3.chnName = this.channelName;
        album3.len = this.duration;
        while (r2.hasNext()) {
            album3.tag += str2 + ",";
        }
        if (album3.tag.isEmpty()) {
            album3.tag = album3.tag.substring(0, album3.tag.length() - 1);
        }
        album3.shortName = this.shortTitle;
        if (this.boss == 2) {
        }
        album3.indiviDemand = i;
        album3.score = this.score;
        album3.time = this.currentPeriod;
        vipInfo = new VipInfo();
        album3.payMarkType = TVApiTool.getPayMarkType(this.payMark);
        if (album3.type != 1) {
            if (this.boss != 2) {
            }
            album3.tvIsPurchase = this.boss != 2 ? 1 : 0;
            if (this.boss == 2) {
            }
            i = 0;
            vipInfo.epIsVip = i;
            if (this.boss == 2) {
            }
            i = 0;
            vipInfo.epIsTvod = i;
            if (this.boss == 2) {
            }
            i = 0;
            vipInfo.epIsCoupon = i;
            album3.drm = TVApiTool.getDrmType(this.drmTypes);
            album3.dynamicRanges = TVApiTool.getHDRType(this.dynamicRange);
        } else {
            if (this.boss != 2) {
                i = 0;
            } else {
                i = 1;
            }
            album3.isPurchase = i;
            if (this.boss == 2) {
            }
            i = 0;
            vipInfo.isVip = i;
            if (this.boss == 2) {
            }
            i = 0;
            vipInfo.isTvod = i;
            if (this.boss == 2) {
            }
            i = 0;
            vipInfo.isCoupon = i;
        }
        album3.vipInfo = vipInfo;
        contentType = getContentType();
        if (contentType != ContentType.TRAILER) {
            i2 = 0;
        }
        album3.isFlower = i2;
        album3.contentType = contentType.getValue();
        return album3;
    }

    public ContentType getContentType() {
        return TVApiTool.getContentType(this.contentType);
    }

    @SuppressLint({"DefaultLocale"})
    private TVType getTVType(String sourceId, String itemType) {
        if (!a.a(sourceId) && Long.parseLong(sourceId) > 0) {
            return TVType.SOURCE;
        }
        if (itemType.equalsIgnoreCase(MediaFactory.TYPE_VIDEO)) {
            return TVType.VIDEO;
        }
        if (itemType.equalsIgnoreCase(MediaFactory.TYPE_ALBUM)) {
            return TVType.ALBUM;
        }
        return TVType.VIDEO;
    }

    public IChannelItem getResourceItem() {
        IChannelItem iChannelItem = new IChannelItem();
        iChannelItem.id = this.id;
        iChannelItem.channelId = String.valueOf(this.channelId);
        if (this.imageStyle == 0) {
            iChannelItem.style = 1;
        } else if (this.imageStyle == 1) {
            iChannelItem.style = 2;
        }
        iChannelItem.plId = this.itemId;
        iChannelItem.title = this.itemName.equals("") ? this.name : this.itemName;
        iChannelItem.image = this.imageUrl;
        iChannelItem.itemImageUrl = this.itemImageUrl;
        return iChannelItem;
    }

    public boolean checkLive() {
        if (this.channels == null || this.channels.size() <= 0 || getLiveFlowerList() == null || getLiveFlowerList().size() <= 0 || getLivePlayingType() == LivePlayingType.END) {
            return false;
        }
        return true;
    }

    public List<Album> getLiveAlbumList() {
        if (this.channels == null || this.channels.size() <= 0) {
            return null;
        }
        List<Album> arrayList = new ArrayList();
        for (LiveAlbum liveAlbum : this.channels) {
            if (getType() == ResourceType.LIVE) {
                Album album = new Album();
                album.qpId = this.itemId;
                album.tvQid = this.itemId;
                album.live_channelId = liveAlbum.id;
                String str = this.itemShortDisplayName.equals("") ? this.itemName.equals("") ? this.name : this.itemName : this.itemShortDisplayName;
                album.name = str;
                album.shortName = this.itemPrompt.equals("") ? ((LiveAlbum) this.channels.get(0)).name : this.itemPrompt;
                album.isLive = 1;
                album.type = 0;
                album.sliveTime = this.startTime;
                album.eliveTime = this.endTime;
                album.viewerShip = this.viewerShip;
                if (this.itemKvs != null) {
                    album.tv_livecollection = this.itemKvs.tv_livecollection;
                    album.tv_livebackground = this.itemKvs.tv_livebackground;
                    album.desc = this.itemKvs.tv_livedesc;
                    album.mLivePlayingType = getLivePlayingType();
                    album.sliveTime = this.sTime == 0 ? this.startTime : String.valueOf(this.sTime);
                    album.eliveTime = this.eTime == 0 ? this.endTime : String.valueOf(this.eTime);
                }
                album.liveType = getLiveType(liveAlbum.type);
                album.tvPic = this.itemImageUrl;
                album.pic = this.imageUrl;
                if (liveAlbum.boss == 2) {
                    album.isPurchase = 1;
                    VipInfo vipInfo = new VipInfo();
                    if (liveAlbum.purType == 1) {
                        vipInfo.epIsVip = 1;
                    } else if (liveAlbum.purType == 2) {
                        vipInfo.epIsTvod = 1;
                    }
                    album.vipInfo = vipInfo;
                }
                album.payMarkType = TVApiTool.getPayMarkType(liveAlbum.payMark);
                album.chnId = this.channelId;
                arrayList.add(album);
            }
        }
        return arrayList;
    }

    public List<Album> getLiveFlowerList() {
        if (this.videos == null || this.videos.size() <= 0) {
            return null;
        }
        List<Album> arrayList = new ArrayList();
        for (LiveAlbum liveAlbum : this.videos) {
            Album album = new Album();
            album.qpId = liveAlbum.albumId;
            album.tvQid = liveAlbum.id;
            album.name = liveAlbum.name;
            album.len = liveAlbum.duration;
            album.vid = liveAlbum.vid;
            album.isLive = 0;
            album.isFlower = 1;
            album.type = 0;
            arrayList.add(album);
        }
        return arrayList;
    }

    private int getLiveType(String type) {
        if (!(type == null || type.isEmpty())) {
            if (type.equals("SATELLITE_CHNNEL")) {
                return 1;
            }
            if (type.equals("LOCAL_CHANNEL")) {
                return 2;
            }
            if (type.equals("CCTV_CHANNEL")) {
                return 3;
            }
            if (type.equals("FOREIGN_CHANNEL")) {
                return 4;
            }
            if (type.equals("IQIYI_LIVE_CHANNEL")) {
                return 5;
            }
            if (type.equals("TEMPORARY_SPORT_EVENTS")) {
                return 6;
            }
            if (type.equals("TEMPORARY_VARIETY_SHOW")) {
                return 7;
            }
            if (type.equals("TEMPORARY_CONFERENCE")) {
                return 8;
            }
            if (type.equals("TEMPORARY_CONCERT")) {
                return 9;
            }
            if (type.equals("TEMPORARY_ELECTRONIC_SPORTS")) {
                return 10;
            }
            if (type.equals("CONFERENCE_PROGRAM")) {
                return 11;
            }
        }
        return 0;
    }

    public ItemKvs getItemKvs() {
        return this.itemKvs;
    }

    public String getPrompt() {
        if (!this.itemPrompt.equals("")) {
            return this.itemPrompt;
        }
        if (!this.prompt.equals("")) {
            return this.prompt;
        }
        if (this.itemShortDisplayName.equals("")) {
            return this.itemName;
        }
        return this.itemShortDisplayName;
    }

    public boolean isVipTags() {
        if (this.itemKvs == null || this.itemKvs.vip_dataType == null || this.itemKvs.vip_dataType.isEmpty() || this.itemKvs.vip_tagClass == null || this.itemKvs.vip_tagClass.isEmpty() || ((!this.itemKvs.vip_tagClass.equals("first") && !this.itemKvs.vip_tagClass.equals("second")) || (!this.itemKvs.vip_dataType.equals("COLLECTION") && !this.itemKvs.vip_dataType.equals("RESOURCE") && !this.itemKvs.vip_dataType.equals("TVTAG") && !this.itemKvs.vip_dataType.equals(MediaFactory.TYPE_ALBUM)))) {
            return false;
        }
        return true;
    }

    public boolean isFirstLevelTag() {
        if (this.itemKvs == null || this.itemKvs.vip_dataType == null || this.itemKvs.vip_dataType.isEmpty() || !this.itemKvs.vip_tagClass.equals("first")) {
            return false;
        }
        return true;
    }

    public boolean isSecondLevelTag() {
        if (this.itemKvs == null || this.itemKvs.vip_dataType == null || this.itemKvs.vip_dataType.isEmpty() || !this.itemKvs.vip_tagClass.equals("second")) {
            return false;
        }
        return true;
    }

    public boolean isChannelData() {
        if (this.itemKvs == null || this.itemKvs.is_Channel == null || !this.itemKvs.is_Channel.equals("1")) {
            return false;
        }
        return true;
    }

    public String getChannelId() {
        String str = "";
        if (this.itemKvs != null) {
            return this.itemKvs.vip_chnId;
        }
        return str;
    }

    public LivePlayingType getLivePlayingType() {
        if (!getType().equals(ResourceType.LIVE)) {
            return LivePlayingType.DEFAULT;
        }
        long currentTime;
        if (this.itemKvs != null) {
            if (!a.a(this.itemKvs.LiveEpisode_StartTime)) {
                if (!a.a(this.itemKvs.LiveEpisode_EndTime)) {
                    this.sTime = formatTime(this.itemKvs.LiveEpisode_StartTime);
                    this.eTime = formatTime(this.itemKvs.LiveEpisode_EndTime);
                    currentTime = TVApiBase.getTVApiProperty().getCurrentTime();
                    if (currentTime < this.sTime) {
                        return LivePlayingType.BEFORE;
                    }
                    if (currentTime >= this.eTime) {
                        return LivePlayingType.PLAYING;
                    }
                    return LivePlayingType.END;
                }
            }
        }
        this.sTime = Long.parseLong(this.startTime);
        this.eTime = Long.parseLong(this.endTime);
        currentTime = TVApiBase.getTVApiProperty().getCurrentTime();
        if (currentTime < this.sTime) {
            return LivePlayingType.BEFORE;
        }
        if (currentTime >= this.eTime) {
            return LivePlayingType.END;
        }
        return LivePlayingType.PLAYING;
    }

    public long formatTime(String time) {
        try {
            return DateLocalThread.parseYH(time).getTime();
        } catch (Exception e) {
            com.gala.tvapi.log.a.b("Channellabel", "please check itemKvs.LiveEpisode_StartTime&&itemKvs.LiveEpisode_EndTime, e = " + e);
            return 0;
        }
    }

    public ChannelCarousel getChannelCarousel() {
        if (this.channels == null || this.channels.size() <= 0) {
            return null;
        }
        ChannelCarousel channelCarousel = new ChannelCarousel();
        try {
            LiveAlbum liveAlbum = (LiveAlbum) this.channels.get(0);
            channelCarousel.id = Long.valueOf(liveAlbum.id).longValue();
            channelCarousel.name = liveAlbum.name;
            return channelCarousel;
        } catch (Exception e) {
            return null;
        }
    }

    public TVChannelCarousel getTVChannelCarousel() {
        if (this.channels == null || this.channels.size() <= 0) {
            return null;
        }
        TVChannelCarousel tVChannelCarousel = new TVChannelCarousel();
        try {
            LiveAlbum liveAlbum = (LiveAlbum) this.channels.get(0);
            tVChannelCarousel.id = Long.valueOf(liveAlbum.id).longValue();
            tVChannelCarousel.name = liveAlbum.name;
            return tVChannelCarousel;
        } catch (Exception e) {
            return null;
        }
    }

    public String toString() {
        return "ChannelLabel [subordinateTitle=" + this.subordinateTitle + ", itemPrompt=" + this.itemPrompt + ", desc=" + this.desc + ", imageUrl=" + this.imageUrl + ", exclusive=" + this.exclusive + ", itemShortDisplayName=" + this.itemShortDisplayName + ", name=" + this.name + ", vid=" + this.vid + ", channelName=" + this.channelName + ", channelId=" + this.channelId + ", itemSubordinateTitle=" + this.itemSubordinateTitle + ", itemType=" + this.itemType + ", itemName=" + this.itemName + ", tvQipuId=" + this.tvQipuId + ", boss=" + this.boss + ", issueTime=" + this.issueTime + ", itemImageUrl=" + this.itemImageUrl + ", itemId=" + this.itemId + ", itemOrder=" + this.itemOrder + ", duration=" + this.duration + ", season=" + this.season + ", shortTitle=" + this.shortTitle + ", is1080P=" + this.is1080P + ", albumQipuId=" + this.albumQipuId + ", id=" + this.id + ", sourceId=" + this.sourceId + ", isSeries=" + this.isSeries + ", video=" + this.video + ", itemKvs=" + this.itemKvs + ", imageStyle=" + this.imageStyle + ", prompt=" + this.prompt + ", postImage=" + this.postImage + ", isDubi=" + this.isDubi + ", categoryId=" + this.categoryId + ", cateName=" + this.cateName + ", albumName=" + this.albumName + ", score=" + this.score + ", tvCount=" + this.tvCount + ", categoryNames=" + this.categoryNames + ", isD3=" + this.isD3 + ", latestOrder=" + this.latestOrder + ", albumImage=" + this.albumImage + ", period=" + this.period + ", currentPeriod=" + this.currentPeriod + ", itemPageUrl=" + this.itemPageUrl + ", pltRegionCtrls=" + this.pltRegionCtrls + ", contentType=" + this.contentType + ", endTime=" + this.endTime + ", startTime=" + this.startTime + ", channels=" + this.channels + ", videos=" + this.videos + ", viewerShip=" + this.viewerShip + ", sTime=" + this.sTime + ", eTime=" + this.eTime + ", type=" + this.type + ", liveType=" + this.liveType + ", purchaseType=" + this.purchaseType + ", issueTimeStamp=" + this.issueTimeStamp + ", fromTagId=" + this.fromTagId + ", payMark=" + this.payMark + ", tableNo=" + this.tableNo + ", is1080P()=" + is1080P() + ", getContentType()=" + getContentType() + ", getType()=" + getType() + ", getVideo()=" + getVideo() + ", getResourceItem()=" + getResourceItem() + ", checkLive()=" + checkLive() + ", getLiveAlbumList()=" + getLiveAlbumList() + ", getLiveFlowerList()=" + getLiveFlowerList() + ", getItemKvs()=" + getItemKvs() + ", getPrompt()=" + getPrompt() + ", isVipTags()=" + isVipTags() + ", isFirstLevelTag()=" + isFirstLevelTag() + ", isSecondLevelTag()=" + isSecondLevelTag() + ", isChannelData()=" + isChannelData() + ", getChannelId()=" + getChannelId() + ", getLivePlayingType()=" + getLivePlayingType() + ", getChannelCarousel()=" + getChannelCarousel() + AlbumEnterFactory.SIGN_STR;
    }
}
