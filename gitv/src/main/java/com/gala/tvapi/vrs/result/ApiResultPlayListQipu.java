package com.gala.tvapi.vrs.result;

import com.gala.tvapi.b.a;
import com.gala.tvapi.tools.TVApiTool;
import com.gala.tvapi.tv2.model.Album;
import com.gala.tvapi.tv2.model.Cast;
import com.gala.tvapi.tv2.model.VipInfo;
import com.gala.tvapi.type.AlbumFrom;
import com.gala.tvapi.type.ContentType;
import com.gala.tvapi.vrs.model.Contributors;
import com.gala.tvapi.vrs.model.PlayAlbum;
import com.gala.tvapi.vrs.model.PlayListQipu;
import com.gala.video.api.ApiResult;
import java.util.ArrayList;
import java.util.List;
import org.cybergarage.http.HTTP;

public class ApiResultPlayListQipu extends ApiResult {
    public PlayListQipu data = null;
    public String timestamp = "";

    public void setData(PlayListQipu playList) {
        this.data = playList;
    }

    public PlayListQipu getPlayListQipu() {
        return this.data;
    }

    public List<Album> getAlbumList() {
        if (this.data == null || this.data.plst == null) {
            return null;
        }
        List<Album> arrayList = new ArrayList();
        for (PlayAlbum playAlbum : this.data.plst) {
            int i;
            Album album = new Album();
            album.albumFrom = AlbumFrom.PLAYLIST;
            album.sourceCode = playAlbum.sourceId;
            album.name = playAlbum.albumName;
            album.pic = playAlbum.picUrl;
            album.tvPic = playAlbum.postImage;
            album.desc = playAlbum.tvDesc;
            album.vid = playAlbum.vid;
            album.tvsets = playAlbum.tvCount;
            album.type = playAlbum.tvType == 1 ? 0 : 1;
            album.len = playAlbum.playLength;
            album.isPurchase = playAlbum.isPurchase;
            album.chnId = playAlbum.categoryId;
            String str = (a.a(playAlbum.albumQipuId) || playAlbum.albumQipuId.equals("0")) ? playAlbum.tvQipuId : playAlbum.albumQipuId;
            album.qpId = str;
            album.tvQid = playAlbum.tvQipuId;
            if (playAlbum.is1080P == 1) {
                if (album.stream.length() == 0) {
                    album.stream += "1080P";
                } else {
                    album.stream += ",1080P";
                }
            }
            if (playAlbum.isDubi == 1) {
                if (album.stream.length() == 0) {
                    album.stream += "720P_dolby";
                } else {
                    album.stream += ",720P_dolby";
                }
            }
            album.exclusive = playAlbum.isExclusive;
            album.isSeries = playAlbum.isSeries;
            album.isDownload = playAlbum.isDownload;
            album.order = playAlbum.order;
            if (playAlbum.is720P == 1) {
                if (album.stream.length() == 0) {
                    album.stream += "720P";
                } else {
                    album.stream += ",720P";
                }
            }
            album.tvCount = playAlbum.latestOrder;
            album.tvName = playAlbum.contentName;
            album.score = playAlbum.score;
            album.is3D = playAlbum.isD3;
            album.cast = a(playAlbum);
            if (playAlbum.categoryNames != null && playAlbum.categoryNames.size() > 0) {
                for (String str2 : playAlbum.categoryNames) {
                    album.tag += str2 + ",";
                }
                if (!album.tag.isEmpty()) {
                    album.tag = album.tag.substring(0, album.tag.length() - 1);
                }
            }
            album.initIssueTime = playAlbum.issueTime;
            album.focus = playAlbum.prompt;
            album.shortName = playAlbum.shortTitle;
            int i2 = (playAlbum.boss == 2 && playAlbum.purchaseType == 2) ? 1 : 0;
            album.indiviDemand = i2;
            album.time = playAlbum.currentPeriod;
            album.payMarkType = TVApiTool.getPayMarkType(playAlbum.payMark);
            VipInfo vipInfo = new VipInfo();
            if (album.type == 1) {
                if (playAlbum.boss == 2 && playAlbum.purchaseType == 1) {
                    i2 = 1;
                } else {
                    i2 = 0;
                }
                vipInfo.isVip = i2;
                if (playAlbum.boss == 2 && playAlbum.purchaseType == 2) {
                    i2 = 1;
                } else {
                    i2 = 0;
                }
                vipInfo.isTvod = i2;
            } else {
                if (playAlbum.boss == 2 && playAlbum.purchaseType == 1) {
                    i2 = 1;
                } else {
                    i2 = 0;
                }
                vipInfo.epIsVip = i2;
                if (playAlbum.boss == 2 && playAlbum.purchaseType == 2) {
                    i2 = 1;
                } else {
                    i2 = 0;
                }
                vipInfo.epIsTvod = i2;
                album.drm = TVApiTool.getDrmType(playAlbum.drmTypes);
                album.dynamicRanges = TVApiTool.getHDRType(playAlbum.dynamicRange);
            }
            album.vipInfo = vipInfo;
            ContentType contentType = TVApiTool.getContentType(playAlbum.contentType);
            album.contentType = contentType.getValue();
            if (contentType == ContentType.TRAILER) {
                i = 1;
            } else {
                i = 0;
            }
            album.isFlower = i;
            arrayList.add(album);
        }
        return arrayList;
    }

    private static Cast a(PlayAlbum playAlbum) {
        Cast cast = new Cast();
        if (playAlbum.contributors != null) {
            for (Contributors contributors : playAlbum.contributors) {
                if (!(contributors == null || contributors.roles == null || contributors.roles.size() <= 0)) {
                    for (String str : contributors.roles) {
                        if (str.equals(HTTP.HOST)) {
                            cast.host += contributors.name + ",";
                        }
                        if (str.equals("GUEST")) {
                            cast.guest += contributors.name + ",";
                        }
                        if (str.equals("DUBBER")) {
                            cast.dubber += contributors.name + ",";
                        }
                        if (str.equals("ACTOR") || str.equals("GUEST") || str.equals("SPEAKER")) {
                            cast.actor += contributors.name + ",";
                        }
                        if (str.equals("MAIN_CHARACTER") || str.equals("STAR") || str.equals("SINGER") || str.equals(HTTP.HOST) || str.equals("SPEAKER")) {
                            cast.mainActor += contributors.name + ",";
                        }
                        if (str.equals("STAR") || str.equals("GUEST")) {
                            cast.star += contributors.name + ",";
                        }
                    }
                }
            }
        }
        if (playAlbum.creators != null) {
            for (Contributors contributors2 : playAlbum.creators) {
                if (!(contributors2 == null || contributors2.roles == null || contributors2.roles.size() <= 0)) {
                    for (String str2 : contributors2.roles) {
                        if (str2.equals("DIRECTOR")) {
                            cast.director += contributors2.name + ",";
                        }
                        if (str2.equals("PRODUCER")) {
                            cast.producer += contributors2.name + ",";
                            cast.maker += contributors2.name + ",";
                        }
                        if (str2.equals("SCREENWRITER")) {
                            cast.writer += contributors2.name + ",";
                        }
                        if (str2.equals("SONG_WRITER")) {
                            cast.songWriter += contributors2.name + ",";
                        }
                        if (str2.equals("COMPOSER")) {
                            cast.composer += contributors2.name + ",";
                        }
                    }
                }
            }
        }
        if (!cast.host.isEmpty()) {
            cast.host = cast.host.substring(0, cast.host.length() - 1);
        }
        if (!cast.guest.isEmpty()) {
            cast.guest = cast.guest.substring(0, cast.guest.length() - 1);
        }
        if (!cast.dubber.isEmpty()) {
            cast.dubber = cast.dubber.substring(0, cast.dubber.length() - 1);
        }
        if (!cast.actor.isEmpty()) {
            cast.actor = cast.actor.substring(0, cast.actor.length() - 1);
        }
        if (!cast.mainActor.isEmpty()) {
            cast.mainActor = cast.mainActor.substring(0, cast.mainActor.length() - 1);
        }
        if (!cast.director.isEmpty()) {
            cast.director = cast.director.substring(0, cast.director.length() - 1);
        }
        if (!cast.producer.isEmpty()) {
            cast.producer = cast.producer.substring(0, cast.producer.length() - 1);
        }
        if (!cast.writer.isEmpty()) {
            cast.writer = cast.writer.substring(0, cast.writer.length() - 1);
        }
        if (!cast.star.isEmpty()) {
            cast.star = cast.star.substring(0, cast.star.length() - 1);
        }
        if (!cast.songWriter.isEmpty()) {
            cast.songWriter = cast.songWriter.substring(0, cast.songWriter.length() - 1);
        }
        if (!cast.composer.isEmpty()) {
            cast.composer = cast.composer.substring(0, cast.composer.length() - 1);
        }
        return cast;
    }
}
