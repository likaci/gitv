package com.gala.tvapi.vrs.result;

import com.alibaba.fastjson.JSON;
import com.gala.tvapi.p008b.C0214a;
import com.gala.tvapi.tools.TVApiTool;
import com.gala.tvapi.tv2.model.Album;
import com.gala.tvapi.tv2.model.VipInfo;
import com.gala.tvapi.type.AlbumFrom;
import com.gala.tvapi.type.AlbumType;
import com.gala.tvapi.vrs.model.Attribute;
import com.gala.tvapi.vrs.model.Categorie;
import com.gala.tvapi.vrs.model.Entity;
import com.gala.tvapi.vrs.model.MixinVideo;
import com.gala.tvapi.vrs.model.Theme;
import com.gala.video.api.ApiResult;
import com.gala.video.app.epg.ui.albumlist.utils.AlbumEnterFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApiResultRecommendListQipu extends ApiResult {
    public Attribute attribute;
    public List<MixinVideo> mixinVideos;
    public List<Theme> themes;
    public int to = 0;
    public Map<String, MixinVideo> videos;

    public void setMixinVideos(List<MixinVideo> list) {
        this.mixinVideos = list;
    }

    public List<MixinVideo> getMixinVideos() {
        return this.mixinVideos;
    }

    public List<Album> getAlbumList() {
        if (this.mixinVideos == null || this.mixinVideos.size() <= 0) {
            return null;
        }
        List<Album> arrayList = new ArrayList();
        for (MixinVideo a : this.mixinVideos) {
            arrayList.add(m841a(a));
        }
        return arrayList;
    }

    public Map<String, Album> getALbumMap() {
        if (this.videos == null || this.videos.size() <= 0) {
            return null;
        }
        Map<String, Album> hashMap = new HashMap();
        for (String str : this.videos.keySet()) {
            hashMap.put(str, m841a((MixinVideo) this.videos.get(str)));
        }
        return hashMap;
    }

    public List<String> getTags(String id) {
        if (this.videos != null && this.videos.size() > 0) {
            MixinVideo mixinVideo = (MixinVideo) this.videos.get(id);
            if (mixinVideo.categories != null && mixinVideo.categories.size() > 0) {
                List<String> arrayList = new ArrayList();
                for (Categorie categorie : mixinVideo.categories) {
                    arrayList.add(categorie.name);
                }
                return arrayList;
            }
        }
        return null;
    }

    private Album m841a(MixinVideo mixinVideo) {
        int i;
        int i2 = 1;
        Album album = new Album();
        album.albumFrom = AlbumFrom.RECOMMAND;
        album.sourceCode = mixinVideo.sourceId;
        String str = (C0214a.m592a(mixinVideo.sourceId) || mixinVideo.sourceId.equals("0")) ? mixinVideo.albumName : mixinVideo.sourceName;
        album.name = str;
        album.tvName = mixinVideo.name;
        album.desc = mixinVideo.description;
        album.tvQid = mixinVideo.qipuId;
        album.vid = mixinVideo.vid;
        album.pCount = mixinVideo.playCount;
        str = (C0214a.m592a(mixinVideo.albumQipuId) || mixinVideo.albumQipuId.equals("0")) ? mixinVideo.qipuId : mixinVideo.albumQipuId;
        album.qpId = str;
        album.len = mixinVideo.duration;
        album.time = mixinVideo.period;
        if (mixinVideo.mode720p == 1) {
            if (album.stream.length() == 0) {
                album.stream += "720P";
            } else {
                album.stream += ",720P";
            }
        }
        if (mixinVideo.mode1080p == 1) {
            if (album.stream.length() == 0) {
                album.stream += "1080P";
            } else {
                album.stream += ",1080P";
            }
        }
        if (mixinVideo.dolby == 1) {
            if (album.stream.length() == 0) {
                album.stream += "720P_dolby";
            } else {
                album.stream += ",720P_dolby";
            }
        }
        album.initIssueTime = mixinVideo.issueTime;
        album.isPurchase = mixinVideo.isPurchase;
        album.exclusive = mixinVideo.exclusive;
        album.isDownload = mixinVideo.downloadAllowed;
        album.isSeries = mixinVideo.series;
        album.pic = C0214a.m592a(mixinVideo.albumImageUrl) ? mixinVideo.videoImageUrl : mixinVideo.albumImageUrl;
        album.videoImageUrl = mixinVideo.videoImageUrl;
        album.tvPic = mixinVideo.posterUrl;
        album.chnId = mixinVideo.channelId;
        album.order = mixinVideo.order == 0 ? -1 : mixinVideo.order;
        album.sourceCode = mixinVideo.sourceId;
        album.focus = mixinVideo.focus;
        album.tvCount = mixinVideo.latestOrder;
        album.tvsets = mixinVideo.videoCount;
        album.type = 0;
        album.shortName = mixinVideo.shortTitle;
        album.score = mixinVideo.score;
        if (mixinVideo.dimension == 3) {
            i = 1;
        } else {
            i = 0;
        }
        album.is3D = i;
        if (this.attribute != null) {
            album.area = this.attribute.area;
            album.bkt = this.attribute.bucket;
            album.eventId = this.attribute.eventId;
            if (!(this.attribute.rawData == null || this.attribute.rawData.isEmpty())) {
                try {
                    album.source = JSON.parseObject((String) this.attribute.rawData.get(mixinVideo.albumQipuId)).getString("source");
                } catch (Exception e) {
                }
            }
        }
        if (mixinVideo.categories != null && mixinVideo.categories.size() > 0) {
            StringBuilder stringBuilder = new StringBuilder();
            for (Categorie categorie : mixinVideo.categories) {
                stringBuilder.append(categorie.name + ",");
            }
            album.tag = stringBuilder.toString();
        }
        if (album.tag.length() > 1) {
            album.tag = album.tag.substring(0, album.tag.length());
        }
        album.isFlower = mixinVideo.contentType == 1 ? 0 : 1;
        album.contentType = mixinVideo.contentType;
        album.type = AlbumType.ALBUM.getValue();
        if (album.tvQid.equals(album.qpId)) {
            album.type = AlbumType.VIDEO.getValue();
        }
        if (album.isSourceType()) {
            album.type = AlbumType.VIDEO.getValue();
            album.isSeries = 1;
            album.qpId = album.sourceCode;
        }
        if (mixinVideo.isPurchase == 2 && mixinVideo.purchaseType == 2) {
            i = 1;
        } else {
            i = 0;
        }
        album.indiviDemand = i;
        VipInfo vipInfo = new VipInfo();
        if (album.type == 1) {
            if (mixinVideo.isPurchase == 2 && mixinVideo.purchaseType == 1) {
                i = 1;
            } else {
                i = 0;
            }
            vipInfo.isVip = i;
            if (!(mixinVideo.isPurchase == 2 && mixinVideo.purchaseType == 2)) {
                i2 = 0;
            }
            vipInfo.isTvod = i2;
        } else {
            if (mixinVideo.isPurchase == 2 && mixinVideo.purchaseType == 1) {
                i = 1;
            } else {
                i = 0;
            }
            vipInfo.epIsVip = i;
            if (mixinVideo.isPurchase == 2 && mixinVideo.purchaseType == 2) {
                i = 1;
            } else {
                i = 0;
            }
            vipInfo.epIsTvod = i;
            if (mixinVideo.drmTypes == null || mixinVideo.drmTypes.isEmpty()) {
                album.drm = "1";
            } else {
                String replace = mixinVideo.drmTypes.replace("[", "").replace(AlbumEnterFactory.SIGN_STR, "");
                if (replace == null || replace.isEmpty()) {
                    album.drm = "1";
                } else {
                    str = "";
                    String[] split = replace.split(",");
                    if (split != null && split.length > 0) {
                        for (int i3 = 0; i3 < split.length; i3++) {
                            if (split[i3].equals("3")) {
                                str = str + "2,";
                            } else if (split[i3].equals("5")) {
                                str = str + "3,";
                            }
                        }
                        if (str.length() > 1) {
                            album.drm = str.substring(0, str.length() - 1);
                        }
                    }
                }
            }
        }
        album.vipInfo = vipInfo;
        if (this.themes != null && this.themes.size() > 0) {
            for (Theme theme : this.themes) {
                if (theme.entities != null && theme.entities.size() > 0) {
                    for (Entity entity : theme.entities) {
                        if (entity.id != null && entity.id.equals(album.tvQid)) {
                            album.score = entity.iscore;
                            break;
                        }
                    }
                }
            }
        }
        album.payMarkType = TVApiTool.getPayMarkType(mixinVideo.payMark);
        return album;
    }
}
