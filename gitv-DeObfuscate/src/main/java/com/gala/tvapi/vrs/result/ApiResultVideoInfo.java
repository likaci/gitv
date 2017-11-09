package com.gala.tvapi.vrs.result;

import com.gala.tvapi.tv2.model.Album;
import com.gala.tvapi.tv2.model.VipInfo;
import com.gala.video.api.ApiResult;

public class ApiResultVideoInfo extends ApiResult {
    public String albumQipuId;
    public String an;
    public int bossStatus;
    public int f1358c;
    public int endTime;
    public int exclusive;
    public int is3D;
    public String plg;
    public String sid;
    public int startTime;
    public int subType;
    public int upOrder;
    public String vid;
    public String videoQipuId;
    public String vn;

    public Album getAlbum() {
        int i = 0;
        Album album = new Album();
        album.qpId = this.albumQipuId;
        album.tvQid = this.videoQipuId;
        album.type = 0;
        album.name = this.an;
        album.tvName = this.vn;
        album.order = this.upOrder;
        album.is3D = this.is3D;
        album.exclusive = this.exclusive;
        album.chnId = this.f1358c;
        if (this.subType != 0) {
            i = 1;
        }
        album.isSeries = i;
        album.vid = this.vid;
        album.sourceCode = this.sid;
        album.startTime = this.startTime;
        album.endTime = this.endTime;
        album.len = this.plg;
        if (this.bossStatus == 2) {
            album.isPurchase = 1;
            VipInfo vipInfo = new VipInfo();
            vipInfo.epIsVip = 1;
            album.vipInfo = vipInfo;
        }
        return album;
    }
}
