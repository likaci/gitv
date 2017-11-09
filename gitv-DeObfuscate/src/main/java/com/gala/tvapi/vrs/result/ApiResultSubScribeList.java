package com.gala.tvapi.vrs.result;

import com.gala.tvapi.tools.TVApiTool;
import com.gala.tvapi.tv2.model.Album;
import com.gala.tvapi.tv2.model.VipInfo;
import com.gala.tvapi.type.ContentType;
import com.gala.tvapi.vrs.model.Subscribe;
import com.gala.tvapi.vrs.model.SubscribeList;
import com.gala.video.api.ApiResult;
import com.gala.video.app.epg.ui.albumlist.utils.AlbumEnterFactory;
import java.util.ArrayList;
import java.util.List;

public class ApiResultSubScribeList extends ApiResult {
    public SubscribeList data;

    public List<Album> getAlbumList() {
        if (this.data == null || this.data.list == null || this.data.list.size() <= 0) {
            return null;
        }
        List<Album> arrayList = new ArrayList(this.data.list.size());
        for (Subscribe subscribe : this.data.list) {
            Album album = new Album();
            album.qpId = subscribe.qipuId;
            album.type = subscribe.isSeries;
            album.sourceCode = subscribe.sourceCode;
            album.chnId = subscribe.channelId;
            album.isSeries = subscribe.isSeries;
            album.name = subscribe.title;
            album.tvsets = subscribe.totalCnt;
            album.tvCount = subscribe.currentCnt;
            album.score = subscribe.score;
            album.len = subscribe.duration;
            album.is3D = subscribe.is3D ? 1 : 0;
            album.initIssueTime = subscribe.issueTime;
            album.exclusive = subscribe.exclusive;
            album.chnName = subscribe.chnName;
            album.tvPic = subscribe.tvPic;
            album.pic = subscribe.pic;
            album.shortName = subscribe.shortName;
            album.end = subscribe.end;
            album.drm = m842a(subscribe.drms);
            album.contentType = TVApiTool.getContentType(subscribe.contentType).getValue();
            if (album.contentType == ContentType.FEATURE_FILM.getValue()) {
                album.tvQid = subscribe.tvQipuId;
            } else {
                album.tvQid = subscribe.targetQipuId;
            }
            if (!(subscribe.publishDate == null || subscribe.publishDate.isEmpty())) {
                album.time = subscribe.publishDate.replace("-", "");
            }
            if (subscribe.boss != null) {
                VipInfo vipInfo = new VipInfo();
                int i;
                int i2;
                if (subscribe.isSeries == 1) {
                    vipInfo.payMark = subscribe.payMark;
                    if (subscribe.boss.is_support_monthly) {
                        i = 1;
                    } else {
                        i = 0;
                    }
                    vipInfo.isVip = i;
                    if (subscribe.boss.is_support_tvod) {
                        i = 1;
                    } else {
                        i = 0;
                    }
                    vipInfo.isTvod = i;
                    if (subscribe.boss.is_support_tvod_coupon) {
                        i2 = 1;
                    } else {
                        i2 = 0;
                    }
                    vipInfo.isCoupon = i2;
                } else {
                    vipInfo.epPayMark = subscribe.payMark;
                    if (subscribe.boss.is_support_monthly) {
                        i = 1;
                    } else {
                        i = 0;
                    }
                    vipInfo.epIsVip = i;
                    if (subscribe.boss.is_support_tvod) {
                        i = 1;
                    } else {
                        i = 0;
                    }
                    vipInfo.epIsTvod = i;
                    if (subscribe.boss.is_support_tvod_coupon) {
                        i2 = 1;
                    } else {
                        i2 = 0;
                    }
                    vipInfo.epIsCoupon = i2;
                }
                album.vipInfo = vipInfo;
            }
            arrayList.add(album);
        }
        return arrayList;
    }

    private static String m842a(String str) {
        if (str == null || str.isEmpty()) {
            return "1";
        }
        String replace = str.replace("[", "").replace(AlbumEnterFactory.SIGN_STR, "");
        if (replace == null || replace.isEmpty()) {
            return "1";
        }
        String str2 = "";
        String[] split = replace.split(",");
        if (split != null && split.length > 0) {
            for (int i = 0; i < split.length; i++) {
                if (split[i].equals("3")) {
                    str2 = str2 + "2,";
                } else if (split[i].equals("5")) {
                    str2 = str2 + "3,";
                }
            }
            if (str2.length() > 1) {
                return str2.substring(0, str2.length() - 1);
            }
        }
        return "1";
    }
}
