package com.gala.video.app.epg.ui.star.utils;

import android.util.Log;
import com.gala.tvapi.tv2.model.Album;
import com.gala.tvapi.type.AlbumType;
import com.gala.tvapi.type.ResourceType;
import com.gala.tvapi.vrs.model.ChannelLabel;
import com.gala.tvapi.vrs.model.ChannelPlayListLabel;
import com.gala.video.app.epg.ui.albumlist.data.type.AlbumData;
import com.gala.video.app.epg.ui.albumlist.data.type.ChannelLabelData;
import com.gala.video.app.epg.ui.albumlist.data.type.ChannelPlayListData;
import com.gala.video.app.epg.ui.albumlist.pingback.core.ICommonValue.BLOCK;
import com.gala.video.app.epg.ui.albumlist.pingback.core.ICommonValue.QTCURL;
import com.gala.video.app.epg.ui.albumlist.utils.QAPingback;
import com.gala.video.app.epg.ui.star.model.StarsInfoModel;
import com.gala.video.lib.framework.core.pingback.PingBack;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.album.IData;
import com.gala.video.lib.share.pingback.PingBackParams;
import com.gala.video.lib.share.pingback.PingBackParams.Keys;

public class StarsPingbackUtil {
    private static final String TAG = "EPG/StarsPingbackUtil";

    public static class PageShowModel {
        public long consumedTime;
        public StarsInfoModel infoModel;
    }

    public static void sendPageItemClick(IData data, String bolck, int line, int rseat, String s1, String e) {
        if (data != null) {
            String r = data.getField(1);
            String c1 = getc1(data);
            String plid = getplid(data);
            Log.i(TAG, "sendPageItemClick c1: " + c1 + ",r:" + r);
            PingBackParams params = new PingBackParams();
            params.add("r", r).add("block", bolck).add("rt", "i").add("rseat", String.valueOf(rseat)).add("rpage", "3").add("c1", c1).add("plid", plid).add("qtcurl", QTCURL.QTCURL_VALUE_STAR).add("s1", s1).add(Keys.T, "20").add("line", String.valueOf(line)).add("e", e);
            PingBack.getInstance().postPingBackToLongYuan(params.build());
        }
    }

    private static String getc1(IData data) {
        int i = 101221;
        String c1 = "";
        if (data == null) {
            return c1;
        }
        if (data instanceof AlbumData) {
            Album album = data.getAlbum();
            if (album != null) {
                if (album.isLive != 1) {
                    i = album.chnId;
                }
                c1 = String.valueOf(i);
            }
        } else if (data instanceof ChannelLabelData) {
            ChannelLabel label = (ChannelLabel) data.getData();
            if (label != null) {
                c1 = ResourceType.LIVE.equals(label.getType()) ? String.valueOf(101221) : ResourceType.COLLECTION.equals(label.getType()) ? String.valueOf(label.categoryId) : String.valueOf(label.channelId);
            }
        } else if (data instanceof ChannelPlayListData) {
            ChannelPlayListLabel playListLabel = (ChannelPlayListLabel) data.getData();
            if (playListLabel != null) {
                c1 = String.valueOf(playListLabel.channelId);
            }
        }
        return c1;
    }

    private static String getplid(IData data) {
        String plid = "";
        if (data == null) {
            return plid;
        }
        if (data instanceof AlbumData) {
            Album album = data.getAlbum();
            if (album != null && AlbumType.PLAYLIST.equals(album.getType())) {
                plid = album.qpId;
            }
        } else if (data instanceof ChannelLabelData) {
            ChannelLabel label = (ChannelLabel) data.getData();
            if (label != null && ResourceType.COLLECTION.equals(label.getType())) {
                plid = label.id;
            }
        } else if (data instanceof ChannelPlayListData) {
            ChannelPlayListLabel playListLabel = (ChannelPlayListLabel) data.getData();
            if (playListLabel != null) {
                plid = playListLabel.id;
            }
        }
        return plid;
    }

    public static void sendPageDetailClick(StarsInfoModel infoModel) {
        PingBackParams params = new PingBackParams();
        params.add("block", BLOCK.BLOCK_VALUE_INITRE).add("rt", "i").add("r", "").add("rseat", BLOCK.BLOCK_VALUE_INITRE).add("rpage", "3").add(Keys.T, "20").add("qtcurl", QTCURL.QTCURL_VALUE_STAR).add("s1", infoModel != null ? infoModel.getBuySource() : "").add("e", infoModel != null ? infoModel.getE() : "");
        PingBack.getInstance().postPingBackToLongYuan(params.build());
    }

    public static void sendPageShow(PageShowModel pageShowModel) {
        if (pageShowModel == null) {
            LogUtils.e(TAG, "sendPageShow --- pageShowModel == null");
            return;
        }
        StarsInfoModel infoModel = pageShowModel.infoModel;
        String block = "";
        String s1 = "";
        String s2 = "";
        String e = "";
        if (infoModel != null) {
            block = infoModel.getSearchModel().getKeyWord();
            s1 = infoModel.getBuySource();
            s2 = infoModel.getFrom();
            e = infoModel.getE();
            LogUtils.e(TAG, "sendPageShow --- e" + e);
        }
        QAPingback.sendMyPageShow("", QTCURL.QTCURL_VALUE_STAR, "", "", null, String.valueOf(pageShowModel.consumedTime), block, "", "", s2, null, "", "", e, s1);
    }
}
