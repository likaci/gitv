package com.gala.video.app.epg.ui.albumlist;

import android.text.TextUtils;
import com.gala.albumprovider.model.QLayoutKind;
import com.gala.tvapi.tv2.model.Album;
import com.gala.tvapi.type.AlbumType;
import com.gala.tvapi.type.PayMarkType;
import com.gala.tvapi.type.ResourceType;
import com.gala.tvapi.vrs.model.ChannelLabel;
import com.gala.video.app.epg.R;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.common.configs.AppClientUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.album.IAlbumInfoHelper.AlbumKind;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.album.ICornerProvider.Wrapper;
import com.gala.video.lib.share.utils.ResourceUtil;
import java.util.List;
import org.cybergarage.soap.SOAP;

public class EPGCornerProvider extends Wrapper {
    private static final String TAG = "EPGCornerProvider";

    public String getTitle(Album album, QLayoutKind layout) {
        if (album == null) {
            return "";
        }
        String itemName = "";
        AlbumKind albumType = GetInterfaceTools.getAlbumInfoHelper().getAlbumType(album);
        if (!GetInterfaceTools.getAlbumInfoHelper().isSingleType(album)) {
            return getSubTitle(album);
        }
        if (album.getType() == AlbumType.PEOPLE) {
            return getSubTitle(album);
        }
        if (albumType == AlbumKind.SIGLE_VIDEO) {
            if (isSpecialChannel(album.chnId)) {
                return getSubTitle(album);
            }
            return TextUtils.isEmpty(album.shortName) ? album.getAlbumSubTvName() : album.shortName;
        } else if (albumType == AlbumKind.SIGLE_UNIT) {
            return QLayoutKind.LANDSCAPE.equals(layout) ? album.tvName : album.name;
        } else if (albumType == AlbumKind.SIGLE_SERIES) {
            return album.tvName;
        } else {
            return itemName;
        }
    }

    public String getTitle(ChannelLabel label, QLayoutKind layout) {
        if (label == null) {
            return "";
        }
        Album album = getRealAlbum(label);
        if (album == null) {
            return label.name;
        }
        ResourceType type = label.getType();
        if (ResourceType.DIY.equals(type)) {
            return TextUtils.isEmpty(label.itemName) ? label.name : label.itemName;
        } else {
            if (ResourceType.COLLECTION.equals(type)) {
                if (!StringUtils.isEmpty(label.itemShortDisplayName)) {
                    return label.itemShortDisplayName;
                }
                if (StringUtils.isEmpty(label.shortTitle)) {
                    return label.name;
                }
                return label.shortTitle;
            } else if (ResourceType.LIVE.equals(type)) {
                return getChannelLabelTitle(label);
            } else {
                return getTitle(album, layout);
            }
        }
    }

    public String getDescRB(Album album, QLayoutKind layout) {
        if (album == null || album.getType() == AlbumType.PEOPLE || album.getType() == AlbumType.PLAYLIST) {
            return "";
        }
        String str = "";
        AlbumKind albumType = GetInterfaceTools.getAlbumInfoHelper().getAlbumType(album);
        if (albumType == AlbumKind.SIGLE_VIDEO) {
            if (isSpecialChannel(album.chnId) || !QLayoutKind.LANDSCAPE.equals(layout)) {
                return str;
            }
            return getLength(album);
        } else if (albumType == AlbumKind.SIGLE_UNIT) {
            if (!QLayoutKind.PORTRAIT.equals(layout) || album.order <= 0) {
                return str;
            }
            return ResourceUtil.getStr(R.string.offline_album_play_order, Integer.valueOf(order));
        } else if (albumType == AlbumKind.SIGLE_SERIES) {
            if (StringUtils.isEmpty(getDateShort(album))) {
                return str;
            }
            return ResourceUtil.getStr(R.string.album_item_update, format);
        } else if (albumType == AlbumKind.SERIES_ALBUM) {
            if (album.tvsets != album.tvCount && album.tvCount != 0) {
                return ResourceUtil.getStr(R.string.album_item_tvcount, Integer.valueOf(album.tvCount));
            } else if (album.tvsets != album.tvCount || album.tvsets == 0) {
                return str;
            } else {
                return ResourceUtil.getStr(R.string.album_item_tvset, Integer.valueOf(album.tvsets));
            }
        } else if (albumType != AlbumKind.SOURCE_ALBUM) {
            return str;
        } else {
            if (StringUtils.isEmpty(getDateShort(album))) {
                return str;
            }
            return ResourceUtil.getStr(R.string.super_album_item_time, format);
        }
    }

    public String getDescLB(Album album, QLayoutKind layout) {
        if (album == null || !QLayoutKind.LANDSCAPE.equals(layout) || album.getType() == AlbumType.PLAYLIST) {
            return "";
        }
        String str = "";
        if (GetInterfaceTools.getAlbumInfoHelper().getAlbumType(album) != AlbumKind.SIGLE_VIDEO || isSpecialChannel(album.chnId)) {
            return str;
        }
        return album.getInitIssueTimeFormat();
    }

    public String getScoreRB(Album album) {
        if (album == null || album.getType() == AlbumType.PEOPLE || album.getType() == AlbumType.PLAYLIST) {
            return "";
        }
        if (!isSpecialChannel(album.chnId) || GetInterfaceTools.getAlbumInfoHelper().getAlbumType(album) != AlbumKind.SIGLE_VIDEO) {
            return "";
        }
        String score = album.score;
        if (TextUtils.isEmpty(score) || "0.0".equals(score)) {
            return "";
        }
        return score;
    }

    public String getSubTitle(Album album) {
        if (album == null) {
            return "";
        }
        CharSequence tvName = album.getAlbumSubTvName();
        String name = album.getAlbumSubName();
        if (album.getType() != AlbumType.ALBUM) {
            if (!StringUtils.isEmpty(tvName)) {
                CharSequence name2 = tvName;
            }
            return name;
        } else if (StringUtils.isEmpty((CharSequence) name)) {
            return tvName;
        } else {
            return name;
        }
    }

    public String getBigViewTitle(ChannelLabel label) {
        String title = "";
        if (label == null) {
            return title;
        }
        if (!StringUtils.isEmpty(label.itemPrompt)) {
            title = label.itemPrompt;
        } else if (!StringUtils.isEmpty(label.prompt)) {
            title = label.prompt;
        } else if (StringUtils.isEmpty(label.itemShortDisplayName)) {
            title = label.itemName;
        } else {
            title = label.itemShortDisplayName;
        }
        return title;
    }

    public String getChannelLabelTitle(ChannelLabel label) {
        String title = "";
        if (label == null) {
            return title;
        }
        if (!StringUtils.isEmpty(label.itemShortDisplayName)) {
            title = label.itemShortDisplayName;
        } else if (!StringUtils.isEmpty(label.itemName)) {
            title = label.itemName;
        }
        if (label.getType() != ResourceType.RESOURCE_GROUP && StringUtils.isEmpty((CharSequence) title)) {
            if (StringUtils.isEmpty(label.shortTitle)) {
                title = label.name;
            } else {
                title = label.shortTitle;
            }
        }
        return title;
    }

    public String getDateShort(Album album) {
        String date = album.time;
        if (TextUtils.isEmpty(date)) {
            return "";
        }
        date = date.trim();
        if (TextUtils.isEmpty(date) || date.length() < 8) {
            return "";
        }
        StringBuilder format = new StringBuilder(10);
        char[] dateArr = date.toCharArray();
        String mark = "-";
        int length = dateArr.length;
        int i = 0;
        while (i < length) {
            format.append(dateArr[i]);
            if (i == 3 || i == 5) {
                format.append("-");
            }
            i++;
        }
        if (format.length() < 10) {
            return "";
        }
        return format.toString();
    }

    public String getLength(Album album) {
        if (album == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        if (TextUtils.isEmpty(album.len)) {
            return "";
        }
        try {
            StringBuilder append;
            int second = Integer.parseInt(album.len);
            int minutes = second / 60;
            int hour = minutes / 60;
            minutes %= 60;
            second %= 60;
            String string = "0";
            String string2 = SOAP.DELIM;
            if (hour > 0) {
                if (hour < 10) {
                    append = sb.append("0");
                } else {
                    append = sb;
                }
                append.append(hour).append(SOAP.DELIM);
            }
            if (minutes < 10) {
                append = sb.append("0");
            } else {
                append = sb;
            }
            append.append(minutes + SOAP.DELIM);
            if (second < 10) {
                append = sb.append("0");
            } else {
                append = sb;
            }
            append.append(second);
            return sb.toString();
        } catch (Exception e) {
            return "";
        }
    }

    public boolean getCornerInfo(ChannelLabel label, int type) {
        if (label == null) {
            return false;
        }
        switch (type) {
            case 6:
                if (ResourceType.RESOURCE_GROUP.equals(label.getType()) || ResourceType.COLLECTION.equals(label.getType())) {
                    return true;
                }
                return false;
            default:
                return getCornerInfo(getRealAlbum(label), type);
        }
    }

    public Album getRealAlbum(ChannelLabel label) {
        if (label == null) {
            return new Album();
        }
        Album album = null;
        if (ResourceType.LIVE.equals(label.getType())) {
            List liveList = label.getLiveAlbumList();
            if (ListUtils.isEmpty(liveList)) {
                LogUtils.e(TAG, "getRealAlbum ---ResourceType.LIVE--- liveList = null");
            } else {
                album = (Album) liveList.get(0);
            }
            if (album == null) {
                LogUtils.e(TAG, "getRealAlbum ---ResourceType.LIVE--- album = null");
            }
        } else {
            album = label.getVideo();
        }
        return album == null ? new Album() : album;
    }

    public boolean getCornerInfo(Album album, int type) {
        boolean z = true;
        if (album == null) {
            return false;
        }
        switch (type) {
            case 0:
                if (album.getPayMarkType() != PayMarkType.VIP_MARK) {
                    z = false;
                }
                return z;
            case 1:
                if (album.getPayMarkType() != PayMarkType.PAY_ON_DEMAND_MARK) {
                    z = false;
                }
                return z;
            case 2:
                return album.isExclusivePlay();
            case 3:
                return false;
            case 4:
                if (AppClientUtils.isSupportDolby()) {
                    return album.isDolby();
                }
                return false;
            case 5:
                return album.is3D();
            case 6:
                return AlbumType.PLAYLIST.equals(album.getType());
            case 7:
                if (album.getPayMarkType() != PayMarkType.COUPONS_ON_DEMAND_MARK) {
                    z = false;
                }
                return z;
            case 8:
                return album.end;
            default:
                return false;
        }
    }

    public boolean isSpecialChannel(int channelId) {
        if (channelId == 1 || channelId == 2 || channelId == 4 || channelId == 15) {
            return true;
        }
        return false;
    }
}
