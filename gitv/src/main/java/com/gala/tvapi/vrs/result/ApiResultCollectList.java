package com.gala.tvapi.vrs.result;

import com.gala.tvapi.b.a;
import com.gala.tvapi.tools.TVApiTool;
import com.gala.tvapi.tv2.model.Album;
import com.gala.tvapi.tv2.model.VipInfo;
import com.gala.tvapi.type.AlbumType;
import com.gala.tvapi.vrs.model.CollectAlbum;
import com.gala.tvapi.vrs.model.CollectListData;
import com.gala.video.api.ApiResult;
import java.util.ArrayList;
import java.util.List;

public class ApiResultCollectList extends ApiResult {
    private List<Album> a = null;
    public CollectListData data = null;

    public void setData(CollectListData collectData) {
        this.data = collectData;
    }

    public CollectListData getCollectListData() {
        return this.data;
    }

    public List<Album> getAlbumList() {
        this.a = new ArrayList();
        if (this.data != null && this.data.getCollectAlbums().size() > 0) {
            for (CollectAlbum collectAlbum : this.data.getCollectAlbums()) {
                if (!a.a(collectAlbum.tvIdQipu)) {
                    int i;
                    Long valueOf = Long.valueOf(collectAlbum.tvIdQipu);
                    if (valueOf.longValue() <= 200000000 || ((int) (valueOf.longValue() % 100)) != 9) {
                        i = 0;
                    } else {
                        i = 1;
                    }
                    if (i == 0) {
                        Album album = new Album();
                        album.name = collectAlbum.albumName;
                        album.pic = collectAlbum.videoImageUrl;
                        album.len = collectAlbum.videoDuration;
                        album.tvsets = collectAlbum.allSets;
                        album.is3D = collectAlbum.is3D;
                        album.type = collectAlbum.subType == 7 ? 0 : 1;
                        album.tvQid = collectAlbum.tvIdQipu;
                        album.vid = collectAlbum.videoId;
                        album.qpId = collectAlbum.albumIdQipu;
                        album.chnId = collectAlbum.channelId;
                        if (collectAlbum.charge == 2) {
                            i = 1;
                        } else {
                            i = 0;
                        }
                        album.isPurchase = i;
                        if (collectAlbum.subType == 2 || collectAlbum.subType == 1) {
                            album.tvPic = "".equals(collectAlbum.postImage) ? collectAlbum.albumImageUrl : collectAlbum.postImage;
                        } else {
                            String str = (collectAlbum.subType != 7 || collectAlbum.channelId == 1) ? collectAlbum.postImage : collectAlbum.videoImageUrl;
                            album.tvPic = str;
                        }
                        album.tvName = collectAlbum.subType == 7 ? collectAlbum.videoName : collectAlbum.albumName;
                        album.sourceCode = collectAlbum.sourceId;
                        album.order = collectAlbum.videoOrder;
                        if (collectAlbum.charge == 2 && collectAlbum.purchaseType == 2) {
                            i = 1;
                        } else {
                            i = 0;
                        }
                        album.indiviDemand = i;
                        album.subKey = collectAlbum.subKey;
                        album.subType = collectAlbum.subType;
                        album.payMarkType = TVApiTool.getPayMarkType(collectAlbum.payMark);
                        if (collectAlbum.is1080P == 1) {
                            if (album.stream.length() == 0) {
                                album.stream += "1080P";
                            } else {
                                album.stream += ",1080P";
                            }
                        }
                        VipInfo vipInfo = new VipInfo();
                        if (album.type == AlbumType.ALBUM.getValue()) {
                            if (collectAlbum.charge == 2 && collectAlbum.purchaseType == 1) {
                                i = 1;
                            } else {
                                i = 0;
                            }
                            vipInfo.isVip = i;
                            if (collectAlbum.charge == 2 && collectAlbum.purchaseType == 2) {
                                i = 1;
                            } else {
                                i = 0;
                            }
                            vipInfo.isTvod = i;
                            album.exclusive = collectAlbum.albumExclusive;
                        } else {
                            if (collectAlbum.charge == 2 && collectAlbum.purchaseType == 1) {
                                i = 1;
                            } else {
                                i = 0;
                            }
                            vipInfo.epIsVip = i;
                            if (collectAlbum.charge == 2 && collectAlbum.purchaseType == 2) {
                                i = 1;
                            } else {
                                i = 0;
                            }
                            vipInfo.epIsTvod = i;
                            album.exclusive = collectAlbum.exclusive;
                            album.drm = TVApiTool.getDrmType(collectAlbum.supportedDrmTypes);
                            album.dynamicRanges = TVApiTool.getHDRType(collectAlbum.dynamicRanges);
                        }
                        album.vipInfo = vipInfo;
                        if (collectAlbum.sourceId == null || collectAlbum.sourceId.isEmpty() || collectAlbum.sourceId.equals("0")) {
                            album.qpId = collectAlbum.albumIdQipu;
                            album.isSeries = collectAlbum.isSeries;
                        } else {
                            album.isSeries = 1;
                            album.qpId = collectAlbum.sourceId;
                        }
                        if (collectAlbum.reminder != null) {
                            album.tvCount = collectAlbum.reminder.mpd == 0 ? collectAlbum.allSets : collectAlbum.reminder.mpd;
                        } else {
                            album.tvCount = collectAlbum.allSets;
                        }
                        this.a.add(album);
                    }
                }
            }
        }
        return this.a;
    }
}
