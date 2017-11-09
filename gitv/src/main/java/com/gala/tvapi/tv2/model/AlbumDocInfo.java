package com.gala.tvapi.tv2.model;

import java.util.ArrayList;
import java.util.List;

public class AlbumDocInfo extends Model {
    private static final long serialVersionUID = 1;
    public String albumAlias = "";
    public String albumHImage = "";
    public int albumId = 0;
    public String albumImg = "";
    public String albumLink = "";
    public String albumTitle = "";
    public String albumVImage = "";
    public int album_type = 0;
    public String channel = "";
    public int contentType = 0;
    public String description = "";
    public String director = "";
    public boolean is3D = false;
    public boolean isDownload = false;
    public int isPurchase = 0;
    public int itemTotalNumber = 0;
    public String platform = "";
    public String playTime = "";
    public int qitanId = 0;
    public String qpid = "";
    public String region = "";
    public String releaseDate = "";
    public float score = 0.0f;
    public String season = "";
    public boolean series = false;
    public String siteId = "";
    public String siteName = "";
    public String sourceCode = "";
    public String star = "";
    public String stragyTime = "";
    public String threeCategory = "";
    public String tvFocus = "";
    public int videoDocType = 0;
    public List<VideoInfo> videoinfos;

    public List<Album> getVideoInfos() {
        List<Album> arrayList = new ArrayList();
        for (VideoInfo videoInfo : this.videoinfos) {
            Album album = new Album();
            album.name = videoInfo.itemTitle;
            album.pic = videoInfo.itemHImage;
            album.len = String.valueOf(videoInfo.timeLength);
            album.order = videoInfo.playedNumber;
            album.initIssueTime = videoInfo.initialIssueTime;
            album.tvQid = videoInfo.tvId == 0 ? String.valueOf(videoInfo.qipu_id) : String.valueOf(videoInfo.tvId);
            album.vid = videoInfo.vid;
            album.focus = videoInfo.vFocus;
            album.tvName = videoInfo.subTitle;
            String valueOf = this.qpid.equals("") ? videoInfo.albumId == 0 ? album.tvQid : String.valueOf(videoInfo.albumId) : this.qpid;
            album.qpId = valueOf;
            if (videoInfo.is1080p) {
                if (album.stream.length() == 0) {
                    album.stream += "1080P";
                } else {
                    album.stream += ",1080P";
                }
            }
            album.shortName = videoInfo.itemshortTitle;
            VipInfo vipInfo = new VipInfo();
            vipInfo.isVip = videoInfo.is_vip ? 1 : 0;
            album.vipInfo = vipInfo;
            if (videoInfo.is_dolby) {
                if (album.stream.length() == 0) {
                    album.stream += "720p_dolby";
                } else {
                    album.stream += ",720p_dolby";
                }
            }
            album.time = String.valueOf(videoInfo.year);
            arrayList.add(album);
        }
        return arrayList;
    }
}
