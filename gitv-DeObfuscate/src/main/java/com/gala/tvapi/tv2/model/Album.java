package com.gala.tvapi.tv2.model;

import android.annotation.SuppressLint;
import com.alibaba.fastjson.JSONObject;
import com.gala.tvapi.log.C0262a;
import com.gala.tvapi.p008b.C0214a;
import com.gala.tvapi.tools.DateLocalThread;
import com.gala.tvapi.tools.TVApiTool;
import com.gala.tvapi.type.AlbumFrom;
import com.gala.tvapi.type.AlbumType;
import com.gala.tvapi.type.ContentType;
import com.gala.tvapi.type.CornerMark;
import com.gala.tvapi.type.LivePlayingType;
import com.gala.tvapi.type.PayMarkType;
import com.gala.video.lib.share.ifimpl.imsg.utils.IMsgUtils.DBColumns;
import com.gala.video.lib.share.pingback.PingBackParams.Values;
import com.gala.video.webview.utils.WebSDKConstants;
import com.tvos.appdetailpage.client.Constants;
import java.util.ArrayList;
import java.util.List;

@SuppressLint({"DefaultLocale", "SimpleDateFormat"})
public class Album extends Model implements Cloneable {
    public static final String CTG_NAME = "ctgName";
    public static final String KEY = "key";
    public static final String PAGE_NUM = "pageNum";
    public static final String PAGE_SIZE = "pageSize";
    public static final String SITE = "site";
    private static final long serialVersionUID = 1;
    public List<Director> actor;
    public String addTime = "";
    public AlbumFrom albumFrom = AlbumFrom.DEFAULT;
    public String area = "";
    public String bkt = "";
    public int c1 = -1;
    public SearchCard card;
    public Cast cast;
    public int chnId = 0;
    public String chnName = "";
    public String cnPubDate = "";
    public int contentType = 1;
    public String desc = "";
    public List<Director> director;
    public String doc_id = "";
    public String docs = "";
    public String drm = "";
    public String dynamicRanges = "";
    public String eliveTime = "";
    public boolean end = false;
    public int endTime = 0;
    public String epProbation = "0";
    public String eventId = "";
    public int exclusive = 0;
    public String focus = "";
    public String himg = "";
    public String imageGif = "";
    public int indiviDemand = 0;
    public String initIssueTime = "";
    public int is3D = 0;
    public int isDownload = 0;
    public int isFlower = 0;
    public int isIntent = -1;
    public int isLive = 0;
    public int isNew = -1;
    public int isPurchase = 0;
    public int isSeries = 0;
    public String key = "";
    public String len = "";
    public int liveType = 0;
    public String live_channelId = "";
    public LivePlayingType mLivePlayingType = LivePlayingType.DEFAULT;
    public String name = "";
    public int order = -1;
    public String pCount = "";
    public PayMarkType payMarkType = PayMarkType.DEFAULT;
    public String pic = "";
    public int playTime = -1;
    public String playUrl;
    public int pos = -1;
    public List<Album> prevues;
    public String program_id = "";
    public String qisoURL = "";
    public String qisost = "";
    public String qpId = "";
    public List<Recom> recoms;
    public String score = "";
    public String searchtime = "";
    public String shortName = "";
    public String site = "";
    public String sliveTime = "";
    public String source = "";
    public String sourceCode = "";
    public int startTime = 0;
    public String strategy = "";
    public String stream = "";
    public String subKey = "";
    public int subType = 0;
    public int superId = 0;
    public String superName = "";
    public String tag = "";
    public String time = "";
    public int tvCount = 0;
    public int tvIsPurchase = 0;
    public String tvName = "";
    public String tvPic = "";
    public String tvQid = "";
    public String tv_livebackground = "";
    public String tv_livecollection = "";
    public int tvsets = 0;
    public int type = 0;
    public String url = "";
    public int vType;
    public String vid = "";
    public String videoImageUrl = "";
    public List<Video> videos;
    public List<TVid> vidl;
    public String viewerShip = "";
    public String vimg = "";
    public VipInfo vipInfo;

    public ContentType getContentType() {
        return TVApiTool.getContentType(this.contentType);
    }

    public PayMarkType getPayMarkType() {
        if (this.payMarkType == PayMarkType.DEFAULT && this.vipInfo != null) {
            if (getType() == AlbumType.ALBUM) {
                if (this.vipInfo.payMark.equals("NONE_MARK")) {
                    return PayMarkType.NONE_MARK;
                }
                if (this.vipInfo.payMark.equals("VIP_MARK")) {
                    return PayMarkType.VIP_MARK;
                }
                if (this.vipInfo.payMark.equals("PAY_ON_DEMAND_MARK")) {
                    return PayMarkType.PAY_ON_DEMAND_MARK;
                }
                if (this.vipInfo.payMark.equals("COUPONS_ON_DEMAND_MARK")) {
                    return PayMarkType.COUPONS_ON_DEMAND_MARK;
                }
            } else if (getType() == AlbumType.VIDEO) {
                if (this.vipInfo.epPayMark.equals("NONE_MARK")) {
                    return PayMarkType.NONE_MARK;
                }
                if (this.vipInfo.epPayMark.equals("VIP_MARK")) {
                    return PayMarkType.VIP_MARK;
                }
                if (this.vipInfo.epPayMark.equals("PAY_ON_DEMAND_MARK")) {
                    return PayMarkType.PAY_ON_DEMAND_MARK;
                }
                if (this.vipInfo.epPayMark.equals("COUPONS_ON_DEMAND_MARK")) {
                    return PayMarkType.COUPONS_ON_DEMAND_MARK;
                }
            }
        }
        return this.payMarkType;
    }

    public int getDrmType() {
        int i = 0;
        if (this.drm == null || "".equals(this.drm)) {
            return 1;
        }
        String[] split = this.drm.split(",");
        if (C0214a.m592a(split)) {
            return 1;
        }
        for (String a : split) {
            int a2 = C0214a.m580a(a);
            if (a2 == 2) {
                return 2;
            }
            if (a2 == 3) {
                i = 1;
            }
        }
        if (i != 0) {
            return 3;
        }
        return 1;
    }

    public SearchCard getcard() {
        this.card = new SearchCard();
        if (isLiveProgram()) {
            this.card.type = 9;
        } else if (getType() != AlbumType.ALBUM || !isSeries()) {
            if (getType() != AlbumType.OFFLINE) {
                if (getType() != AlbumType.PLAYLIST) {
                    if (getType() != AlbumType.PEOPLE) {
                        switch (this.chnId) {
                            case 1:
                                this.card.type = 1;
                                break;
                            case 4:
                            case 15:
                                if (getType() != AlbumType.VIDEO || isSeries()) {
                                    if (getType() != AlbumType.ALBUM || !isSeries()) {
                                        this.card.type = 99;
                                        break;
                                    }
                                    this.card.type = 4;
                                    break;
                                }
                                this.card.type = 3;
                                break;
                                break;
                            default:
                                this.card.type = 99;
                                break;
                        }
                    }
                    this.card.type = 8;
                } else {
                    this.card.type = 7;
                }
            } else {
                this.card.type = 2;
            }
        } else {
            if (C0214a.m592a(this.sourceCode)) {
                this.card.type = 4;
            } else {
                this.card.type = 6;
            }
        }
        initCard();
        return this.card;
    }

    public void initCard() {
        this.card.shortName = this.shortName;
        if (this.cast != null) {
            this.card.director = this.cast.director;
            this.card.mainActor = this.cast.mainActor;
        }
        this.card.time = this.time;
        this.card.len = this.len;
        if (this.prevues != null) {
            this.card.prevues = this.prevues;
        }
        this.card.tag = this.tag;
        this.card.tvCount = this.tvCount;
        this.card.tvsets = this.tvsets;
        this.card.strategy = this.strategy;
        this.card.isNew = this.isNew;
        this.card.name = this.name;
        this.card.tvName = this.tvName;
        if (this.videos != null) {
            this.card.videos = this.videos;
        }
        if (this.recoms != null) {
            this.card.recoms = this.recoms;
        }
        this.card.chnId = this.chnId;
        if (this.director != null) {
            this.card.directorList = this.director;
        }
        if (this.actor != null) {
            this.card.actor = this.actor;
        }
        this.card.cnPubDate = this.cnPubDate;
        this.card.sliveTime = this.sliveTime;
        this.card.viewerShip = this.viewerShip;
    }

    public String getSource() {
        return this.source;
    }

    public String getInitIssueTime() {
        if (!(this.initIssueTime == null || this.initIssueTime.isEmpty())) {
            if (this.initIssueTime.length() > 8) {
                String[] split = this.initIssueTime.split(" ");
                if (split != null && split.length > 0) {
                    return split[0];
                }
            }
            if (this.initIssueTime.length() == 8) {
                return this.initIssueTime.substring(0, 4) + "-" + this.initIssueTime.substring(4, 6) + "-" + this.initIssueTime.substring(6, 8);
            }
        }
        return this.initIssueTime;
    }

    @SuppressLint({"SimpleDateFormat"})
    public String getInitIssueTimeFormat() {
        if (!(this.initIssueTime == null || this.initIssueTime.isEmpty())) {
            long time = DateLocalThread.getTime(this.initIssueTime);
            if (time != 0) {
                return DateLocalThread.parseIssueTime(time);
            }
        }
        return this.initIssueTime;
    }

    public String getSourceLastOrder() {
        if (this.time == null || this.time.length() != 8) {
            return this.time;
        }
        return this.time.substring(0, 4) + "-" + this.time.substring(4, 6) + "-" + this.time.substring(6, 8);
    }

    public AlbumType getType() {
        switch (this.type) {
            case 0:
                return AlbumType.VIDEO;
            case 1:
                return AlbumType.ALBUM;
            case 2:
                return AlbumType.PLAYLIST;
            case 14:
                return AlbumType.OFFLINE;
            case 99:
                return AlbumType.PEOPLE;
            default:
                return AlbumType.VIDEO;
        }
    }

    public CornerMark getCornerMark() {
        CornerMark cornerMark = CornerMark.CORNERMARK_NO;
        if (this.isLive == 1) {
            return CornerMark.CORNERMARK_LIVE;
        }
        if (this.indiviDemand == 1) {
            return CornerMark.CORNERMARK_INDIVIDEMAND;
        }
        if (this.isPurchase != 0) {
            return CornerMark.CORNERMARK_VIP;
        }
        if (this.exclusive == 1) {
            return CornerMark.CORNERMARK_EXCLUSIVEPLAY;
        }
        if (this.is3D == 1) {
            return CornerMark.CORNERMARK_3D;
        }
        if (hasStream("4k_dolby", "1080p_dolby", "720p_dolby", "1000_dolby", Values.value16, "15", "14", "13", "600_dolby", "20")) {
            return CornerMark.CORNERMARK_DOLBY;
        }
        return cornerMark;
    }

    public CornerMark getCornerMark(List<CornerMark> notSupportStream) {
        if (this.isLive == 1 && (notSupportStream == null || notSupportStream.size() <= 0 || !notSupportStream.contains(CornerMark.CORNERMARK_LIVE))) {
            return CornerMark.CORNERMARK_LIVE;
        }
        if (this.indiviDemand == 1 && (notSupportStream == null || notSupportStream.size() <= 0 || !notSupportStream.contains(CornerMark.CORNERMARK_INDIVIDEMAND))) {
            return CornerMark.CORNERMARK_INDIVIDEMAND;
        }
        if (this.isPurchase != 0 && (notSupportStream == null || notSupportStream.size() <= 0 || !notSupportStream.contains(CornerMark.CORNERMARK_VIP))) {
            return CornerMark.CORNERMARK_VIP;
        }
        if (this.exclusive == 1 && (notSupportStream == null || notSupportStream.size() <= 0 || !notSupportStream.contains(CornerMark.CORNERMARK_EXCLUSIVEPLAY))) {
            return CornerMark.CORNERMARK_EXCLUSIVEPLAY;
        }
        if (this.is3D == 1 && (notSupportStream == null || notSupportStream.size() <= 0 || !notSupportStream.contains(CornerMark.CORNERMARK_3D))) {
            return CornerMark.CORNERMARK_3D;
        }
        if (!hasStream("4k_dolby", "1080p_dolby", "720p_dolby", "1000_dolby", Values.value16, "15", "14", "13", "600_dolby", "20") || (notSupportStream != null && notSupportStream.size() > 0 && notSupportStream.contains(CornerMark.CORNERMARK_DOLBY))) {
            return CornerMark.CORNERMARK_NO;
        }
        return CornerMark.CORNERMARK_DOLBY;
    }

    public CornerMark getHighestCornerMark(CornerMark mark) {
        C0262a.m629a("CornerMark()", "level mark:" + mark);
        List<CornerMark> arrayList = new ArrayList();
        if (this.isLive == 1) {
            arrayList.add(CornerMark.CORNERMARK_LIVE);
        }
        if (this.indiviDemand == 1) {
            arrayList.add(CornerMark.CORNERMARK_INDIVIDEMAND);
        }
        if (this.isPurchase == 1) {
            arrayList.add(CornerMark.CORNERMARK_VIP);
        }
        if (this.exclusive == 1) {
            arrayList.add(CornerMark.CORNERMARK_EXCLUSIVEPLAY);
        }
        if (this.is3D == 1) {
            arrayList.add(CornerMark.CORNERMARK_3D);
        }
        if (hasStream("4k_dolby", "1080p_dolby", "720p_dolby", "1000_dolby", Values.value16, "15", "14", "13")) {
            arrayList.add(CornerMark.CORNERMARK_DOLBY);
        }
        for (CornerMark cornerMark : arrayList) {
            if (cornerMark.getValue() <= mark.getValue()) {
                return cornerMark;
            }
        }
        return CornerMark.CORNERMARK_NO;
    }

    public CornerMark getHighestCornerMark(CornerMark mark, List<CornerMark> notSupportStream) {
        C0262a.m629a("CornerMark()", "level mark:" + mark);
        List<CornerMark> arrayList = new ArrayList();
        if (this.isLive == 1 && (notSupportStream == null || notSupportStream.size() <= 0 || !notSupportStream.contains(CornerMark.CORNERMARK_LIVE))) {
            arrayList.add(CornerMark.CORNERMARK_LIVE);
        }
        if (this.indiviDemand == 1 && (notSupportStream == null || notSupportStream.size() <= 0 || !notSupportStream.contains(CornerMark.CORNERMARK_INDIVIDEMAND))) {
            arrayList.add(CornerMark.CORNERMARK_INDIVIDEMAND);
        }
        if (this.isPurchase == 1 && (notSupportStream == null || notSupportStream.size() <= 0 || !notSupportStream.contains(CornerMark.CORNERMARK_VIP))) {
            arrayList.add(CornerMark.CORNERMARK_VIP);
        }
        if (this.exclusive == 1 && (notSupportStream == null || notSupportStream.size() <= 0 || !notSupportStream.contains(CornerMark.CORNERMARK_EXCLUSIVEPLAY))) {
            arrayList.add(CornerMark.CORNERMARK_EXCLUSIVEPLAY);
        }
        if (this.is3D == 1 && (notSupportStream == null || notSupportStream.size() <= 0 || !notSupportStream.contains(CornerMark.CORNERMARK_3D))) {
            arrayList.add(CornerMark.CORNERMARK_3D);
        }
        if (hasStream("4k_dolby", "1080p_dolby", "720p_dolby", "1000_dolby", Values.value16, "15", "14", "13") && (notSupportStream == null || notSupportStream.size() <= 0 || !notSupportStream.contains(CornerMark.CORNERMARK_DOLBY))) {
            arrayList.add(CornerMark.CORNERMARK_DOLBY);
        }
        for (CornerMark cornerMark : arrayList) {
            if (cornerMark.getValue() <= mark.getValue()) {
                return cornerMark;
            }
        }
        return CornerMark.CORNERMARK_NO;
    }

    private boolean hasStream(String... names) {
        if (this.stream == null || this.stream.isEmpty()) {
            return false;
        }
        String[] split = this.stream.split(",");
        if (split == null || split.length <= 0) {
            return false;
        }
        for (String str : split) {
            for (String equalsIgnoreCase : names) {
                if (str.equalsIgnoreCase(equalsIgnoreCase)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean hasHDRStream() {
        if (this.dynamicRanges == null || this.dynamicRanges.isEmpty()) {
            return false;
        }
        if (this.dynamicRanges.contains(",")) {
            String[] split = this.dynamicRanges.split(",");
            if (split != null && split.length > 0) {
                for (String equalsIgnoreCase : split) {
                    if (equalsIgnoreCase.equalsIgnoreCase("unknown")) {
                        return false;
                    }
                }
                return true;
            }
        }
        if (this.dynamicRanges.equalsIgnoreCase("unknown")) {
            return false;
        }
        return true;
    }

    public Album copy() {
        Album album;
        CloneNotSupportedException e;
        try {
            album = (Album) super.clone();
            try {
                if (this.vipInfo != null) {
                    VipInfo vipInfo = new VipInfo();
                    vipInfo.epIsCoupon = this.vipInfo.epIsCoupon;
                    vipInfo.epIsTvod = this.vipInfo.epIsTvod;
                    vipInfo.epIsVip = this.vipInfo.epIsVip;
                    vipInfo.epIsPkg = this.vipInfo.epIsPkg;
                    vipInfo.isTvod = this.vipInfo.isTvod;
                    vipInfo.isCoupon = this.vipInfo.isCoupon;
                    vipInfo.isVip = this.vipInfo.isVip;
                    vipInfo.isPkg = this.vipInfo.isPkg;
                    vipInfo.epPayMark = this.vipInfo.epPayMark;
                    vipInfo.payMark = this.vipInfo.payMark;
                    album.vipInfo = vipInfo;
                }
                if (this.cast != null) {
                    Cast cast = new Cast();
                    cast.actor = this.cast.actor;
                    cast.composer = this.cast.composer;
                    cast.director = this.cast.director;
                    cast.directorIds = this.cast.directorIds;
                    cast.dubber = this.cast.dubber;
                    cast.guest = this.cast.guest;
                    cast.guestIds = this.cast.guestIds;
                    cast.host = this.cast.host;
                    cast.hostIds = this.cast.hostIds;
                    cast.mainActor = this.cast.mainActor;
                    cast.mainActorIds = this.cast.mainActorIds;
                    cast.maker = this.cast.maker;
                    cast.producer = this.cast.producer;
                    cast.songWriter = this.cast.songWriter;
                    cast.star = this.cast.star;
                    cast.writer = this.cast.writer;
                    album.cast = cast;
                }
            } catch (CloneNotSupportedException e2) {
                e = e2;
                e.printStackTrace();
                return album;
            }
        } catch (CloneNotSupportedException e3) {
            CloneNotSupportedException cloneNotSupportedException = e3;
            album = null;
            e = cloneNotSupportedException;
            e.printStackTrace();
            return album;
        }
        return album;
    }

    public boolean isUpdateCompleted() {
        if (this.isSeries == 1 && this.tvCount == this.tvsets) {
            return true;
        }
        return false;
    }

    public boolean isSourceType() {
        return (C0214a.m592a(this.sourceCode) || this.sourceCode.equals("0")) ? false : true;
    }

    public boolean isPurchase() {
        return (this.isPurchase == 0 && this.tvIsPurchase == 0) ? false : true;
    }

    public boolean isSeries() {
        return this.isSeries == 1;
    }

    public boolean canDownload() {
        return this.isDownload == 1;
    }

    @SuppressLint({"DefaultLocale"})
    public String getAlbumSubName() {
        if (C0214a.m592a(this.name)) {
            return "";
        }
        if (this.name.endsWith("(1080P)") || this.name.endsWith("(超清)")) {
            return this.name.substring(0, this.name.lastIndexOf("("));
        } else if (!this.name.endsWith("（1080P）") && !this.name.endsWith("（超清）")) {
            return this.name;
        } else {
            return this.name.substring(0, this.name.lastIndexOf("（"));
        }
    }

    public String getAlbumSubTvName() {
        if (C0214a.m592a(this.tvName)) {
            return "";
        }
        if (this.tvName.endsWith("(1080P)") || this.tvName.endsWith("(超清)")) {
            return this.tvName.substring(0, this.tvName.lastIndexOf("("));
        } else if (!this.tvName.endsWith("（1080P）") && !this.tvName.endsWith("（超清）")) {
            return this.tvName;
        } else {
            return this.tvName.substring(0, this.tvName.lastIndexOf("（"));
        }
    }

    public boolean isFlower() {
        return this.isFlower == 1;
    }

    public boolean isIndividualVip() {
        return this.indiviDemand == 1;
    }

    public boolean isExclusivePlay() {
        return this.exclusive == 1;
    }

    public boolean is3D() {
        return this.is3D == 1;
    }

    public boolean isDolby() {
        if (hasStream("4k_dolby", "1080p_dolby", "720p_dolby", "1000_dolby", Values.value16, "15", "14", "13")) {
            return true;
        }
        return false;
    }

    public boolean isVipForAccount() {
        if (this.vipInfo != null) {
            if (this.type == 1 && this.vipInfo.isVip == 1) {
                return true;
            }
            if (this.type == 0 && this.vipInfo.epIsVip == 1) {
                return true;
            }
        }
        return false;
    }

    public boolean isSinglePay() {
        if (this.vipInfo != null) {
            if (this.type == 1 && (this.vipInfo.isTvod == 1 || this.vipInfo.isPkg == 1)) {
                return true;
            }
            if (this.type == 0 && (this.vipInfo.epIsTvod == 1 || this.vipInfo.epIsPkg == 1)) {
                return true;
            }
        }
        return false;
    }

    public boolean isCoupon() {
        if (this.vipInfo != null) {
            if (this.type == 1 && this.vipInfo.isCoupon == 1) {
                return true;
            }
            if (this.type == 0 && this.vipInfo.epIsCoupon == 1) {
                return true;
            }
        }
        return false;
    }

    public boolean isH265() {
        if (hasStream("720p_h265", "1080p_h265", Values.value17, Values.value18)) {
            return true;
        }
        return false;
    }

    public String getScore() {
        if (C0214a.m592a(this.score) || this.score.contains(".")) {
            return this.score;
        }
        return this.score + ".0";
    }

    public boolean isVipVideo() {
        if (this.vipInfo == null) {
            return false;
        }
        if (this.vipInfo.epIsCoupon == 1 || this.vipInfo.epIsPkg == 1 || this.vipInfo.epIsTvod == 1 || this.vipInfo.epIsVip == 1) {
            return true;
        }
        return false;
    }

    public boolean isLiveProgram() {
        return this.isLive == 1;
    }

    public int getLiveType() {
        return this.liveType;
    }

    public Album(Episode e) {
        this.tvQid = e.tvQid;
        this.vid = e.vid;
        this.isFlower = e.type == 1 ? 0 : 1;
        this.contentType = e.type == 1 ? 1 : 3;
        this.name = e.albumName;
        this.len = e.len;
        this.order = e.order;
        this.pic = e.pic;
        this.focus = e.focus;
        this.tvName = e.name;
        this.time = e.year;
        this.type = 0;
        this.isSeries = 1;
        this.desc = e.desc;
        this.shortName = e.shortName;
        this.cast = new Cast();
        this.tvIsPurchase = e.tvIsPurchase;
        this.vipInfo = e.vipInfo;
        this.drm = e.drm;
        this.dynamicRanges = e.dynamicRanges;
        if (e.cast != null) {
            this.cast.director = e.cast.director;
            this.cast.mainActor = e.cast.mainActor;
            this.cast.writer = e.cast.writer;
        }
    }

    public String toString() {
        return "qpId=" + this.qpId + " name=" + this.name + " type=" + getType() + " focus=" + this.focus + " pic=" + this.pic + " tvPic=" + this.tvPic + " score=" + this.score + " pCount=" + this.pCount + " desc=" + this.desc + " key=" + this.key + " isSeries=" + isSeries() + " chnName=" + this.chnName + " chnId=" + this.chnId + " tag=" + this.tag + " exclusive=" + this.exclusive + " isDownload=" + canDownload() + " is3D=" + this.is3D + " tvsets=" + this.tvsets + " order=" + this.order + " time=" + this.time + " tvCount=" + this.tvCount + " stream=" + this.stream + " isPurchase=" + this.isPurchase + " tvQid=" + this.tvQid + " vid=" + this.vid + " len=" + this.len + " sourceCode=" + this.sourceCode + " vType=" + this.vType + " playUrl=" + this.playUrl + " hdr=" + this.dynamicRanges;
    }

    public String buildJson() {
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("qpId", this.qpId);
        jSONObject.put(WebSDKConstants.PARAM_KEY_PL_NAME, this.name);
        jSONObject.put("type", Integer.valueOf(this.type));
        jSONObject.put("focus", this.focus);
        jSONObject.put(DBColumns.PIC, this.pic);
        jSONObject.put("tvPic", this.tvPic);
        jSONObject.put("score", this.score);
        jSONObject.put("pCount", this.pCount);
        jSONObject.put(Constants.USERGAME_ORDER_DESC, this.desc);
        jSONObject.put(KEY, this.key);
        jSONObject.put("isSeries", Integer.valueOf(this.isSeries));
        jSONObject.put("chnName", this.chnName);
        jSONObject.put("chnId", Integer.valueOf(this.chnId));
        jSONObject.put("tag", this.tag);
        jSONObject.put("cast", this.cast);
        jSONObject.put("exclusive", Integer.valueOf(this.exclusive));
        jSONObject.put("isDownload", Integer.valueOf(this.isDownload));
        jSONObject.put("sourceCode", this.sourceCode);
        jSONObject.put("is3D", Integer.valueOf(this.is3D));
        jSONObject.put("tvsets", Integer.valueOf(this.tvsets));
        jSONObject.put("order", Integer.valueOf(this.order));
        jSONObject.put("time", this.time);
        jSONObject.put("tvCount", Integer.valueOf(this.tvCount));
        jSONObject.put("stream", this.stream);
        jSONObject.put("isPurchase", Integer.valueOf(this.isPurchase));
        jSONObject.put("tvQid", this.tvQid);
        jSONObject.put("vid", this.vid);
        jSONObject.put("len", this.len);
        jSONObject.put("tvName", this.tvName);
        jSONObject.put("initIssueTime", this.initIssueTime);
        jSONObject.put("vidl", this.vidl);
        jSONObject.put("vipInfo", this.vipInfo);
        jSONObject.put("playTime", Integer.valueOf(this.playTime));
        jSONObject.put("startTime", Integer.valueOf(this.startTime));
        jSONObject.put("endTime", Integer.valueOf(this.endTime));
        jSONObject.put("isFlower", Integer.valueOf(this.isFlower));
        jSONObject.put("tvIsPurchase", Integer.valueOf(this.tvIsPurchase));
        jSONObject.put("shortName", this.shortName);
        jSONObject.put("contentType", Integer.valueOf(this.contentType));
        jSONObject.put("drm", this.drm);
        return jSONObject.toJSONString();
    }
}
