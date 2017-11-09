package com.gala.video.app.player.ui;

import android.text.TextUtils;
import com.gala.tvapi.tv2.model.Album;
import com.gala.video.app.player.C1291R;
import com.gala.video.lib.framework.core.utils.DeviceUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.album.IAlbumInfoHelper.AlbumKind;
import com.gala.video.lib.share.utils.ResourceUtil;
import com.tvos.apps.utils.DateUtil;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.cybergarage.soap.SOAP;

public class GalleryCornerHelper {
    private static final String TAG = "Player/UI/GalleryCornerHelper";

    public enum Corner {
        DOLBY,
        VIP,
        EXCLUSIVE,
        THREED,
        BUY
    }

    public static int getCornerResId(Corner corner) {
        switch (corner) {
            case DOLBY:
                return C1291R.drawable.share_corner_dolby;
            case THREED:
                return C1291R.drawable.share_corner_3d;
            case VIP:
                return C1291R.drawable.share_corner_vip;
            case EXCLUSIVE:
                return C1291R.drawable.share_corner_dubo;
            case BUY:
                return C1291R.drawable.share_corner_fufeidianbo;
            default:
                return 0;
        }
    }

    public static StringBuffer getVideoLength(long playLength) {
        StringBuffer append;
        StringBuffer sb = new StringBuffer();
        int second = (int) (playLength / 1000);
        int minutes = second / 60;
        int hour = minutes / 60;
        minutes %= 60;
        second %= 60;
        if (hour > 0) {
            (hour < 10 ? sb.append("0") : sb).append(hour).append(SOAP.DELIM);
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
        return sb;
    }

    public static String getOnlineTime(String issueTime) {
        LogUtils.m1571e(TAG, "getOnlineTime:issueTime=" + issueTime);
        if (StringUtils.isEmpty((CharSequence) issueTime)) {
            return null;
        }
        String onlineTime;
        long time = DeviceUtils.getServerTimeMillis() - parse(issueTime, -1);
        LogUtils.m1571e(TAG, "getOnlineTime:Time=" + time);
        if (time < 60000 && time >= 0) {
            onlineTime = "1分钟前";
        } else if (time >= 60000 && time < 3600000) {
            onlineTime = (time / 60000) + "分钟前";
        } else if (time < 3600000 || time > 86400000) {
            onlineTime = new SimpleDateFormat(DateUtil.PATTERN_STANDARD10H).format(new Date(parse(issueTime, -1)));
        } else {
            onlineTime = (time / 3600000) + "小时前";
        }
        return onlineTime;
    }

    public static long parse(String str, int defaultValue) {
        long value = (long) defaultValue;
        try {
            value = Long.parseLong(str);
        } catch (NumberFormatException e) {
            LogUtils.m1577w(TAG, "parse(" + str + ") error!");
        }
        return value;
    }

    public static String getSeriesDesc(Album album) {
        String str = "";
        if (GetInterfaceTools.getAlbumInfoHelper().getAlbumType(album).equals(AlbumKind.SERIES_ALBUM)) {
            if (album.tvsets != album.tvCount && album.tvCount != 0) {
                return ResourceUtil.getStr(C1291R.string.album_item_tvcount, Integer.valueOf(album.tvCount));
            } else if (album.tvsets != album.tvCount || album.tvsets == 0) {
                return str;
            } else {
                return ResourceUtil.getStr(C1291R.string.album_item_tvset, Integer.valueOf(album.tvCount));
            }
        } else if (album.isSourceType()) {
            String issueTime = album.getInitIssueTime();
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(TAG, "getSeriesDesc(" + issueTime + ")!");
            }
            return ResourceUtil.getStr(C1291R.string.album_item_update, issueTime);
        } else if (!TextUtils.isEmpty(str)) {
            return str;
        } else {
            int i = C1291R.string.album_item_tvset;
            Object[] objArr = new Object[1];
            objArr[0] = Integer.valueOf(album.tvsets > album.tvCount ? album.tvsets : album.tvCount);
            return ResourceUtil.getStr(i, objArr);
        }
    }

    public static boolean isVerticalType(int channelId) {
        return GetInterfaceTools.getCornerProvider().isSpecialChannel(channelId);
    }
}
