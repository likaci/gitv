package com.gala.albumprovider.logic.set;

import android.annotation.SuppressLint;
import android.content.SharedPreferences.Editor;
import com.gala.albumprovider.base.IAlbumCallback;
import com.gala.albumprovider.logic.source.SourceTool;
import com.gala.albumprovider.model.QChannel;
import com.gala.albumprovider.model.QLayoutKind;
import com.gala.albumprovider.p001private.C0067g;
import com.gala.albumprovider.util.USALog;
import com.gala.video.api.ApiException;
import java.util.List;

public class SetTool {
    public static final int FUNCTION = 2;
    public static final String ID_1080P_MUSIC = "20039";
    public static final String ID_3D = "26";
    public static final String ID_4K = "10002";
    public static final String ID_CARTOON = "4";
    public static final String ID_DOLBY = "10003";
    public static final String ID_HD = "29";
    public static final String ID_KIDS = "62";
    public static final String ID_SHOW = "6";
    public static final int PHYSICAL = 0;
    public static final int VIRTUAL = 1;
    public static String gPageTo = "";

    public static boolean isCorrectParams(IAlbumCallback callback, int pageIndex, int pageSize) {
        if (callback == null) {
            return false;
        }
        if (pageIndex < 0) {
            callback.onFailure(pageIndex, new ApiException("PageIndex is negative!"));
            return false;
        } else if (pageSize > 0) {
            return true;
        } else {
            callback.onFailure(pageIndex, new ApiException("Pagesize is zero or negative!"));
            return false;
        }
    }

    public static boolean isOutOfRange(IAlbumCallback callback, int itemsCount, int pageNum, int pageSize) {
        if (itemsCount == 0 || pageNum <= (itemsCount / pageSize) + 1) {
            return false;
        }
        callback.onFailure(pageNum, new ApiException("Request exceeds the maximum range of pages!"));
        return true;
    }

    public static QLayoutKind setSearchLayoutKind(String channelId) {
        QChannel a = C0067g.m139a().m141a(channelId);
        if (a == null || a.isAggregation()) {
            return QLayoutKind.MIXING;
        }
        return a.getLayoutKind();
    }

    public static QLayoutKind setLayoutKind(String tagId) {
        QChannel a = C0067g.m139a().m141a(tagId);
        if (a == null) {
            return QLayoutKind.PORTRAIT;
        }
        if (a.id.equals("6")) {
            return QLayoutKind.PORTRAIT;
        }
        return a.getLayoutKind();
    }

    public static boolean isCartoonOrKids(String id) {
        if (id.equals("4") || id.equals(ID_KIDS)) {
            return true;
        }
        return false;
    }

    public static QLayoutKind getLayoutKind(int layout) {
        switch (layout) {
            case 1:
                return QLayoutKind.LANDSCAPE;
            case 2:
                return QLayoutKind.PORTRAIT;
            case 5:
                return QLayoutKind.PLAY;
            default:
                return QLayoutKind.PORTRAIT;
        }
    }

    public static int getLayoutValue(QLayoutKind kind) {
        if (kind == QLayoutKind.LANDSCAPE) {
            return 1;
        }
        if (kind == QLayoutKind.PORTRAIT || kind != QLayoutKind.PLAY) {
            return 2;
        }
        return 5;
    }

    @SuppressLint({"InlinedApi"})
    public static void setInitFlagForAnonymous(boolean flag) {
        if (SourceTool.gContext != null) {
            Editor edit = SourceTool.gContext.getSharedPreferences("my_movie_sp", 4).edit();
            edit.putBoolean("my_movie_init", flag);
            edit.commit();
        }
    }

    @SuppressLint({"InlinedApi"})
    public static boolean isInitForAnonymous() {
        if (SourceTool.gContext != null) {
            return SourceTool.gContext.getSharedPreferences("my_movie_sp", 4).getBoolean("my_movie_init", false);
        }
        return false;
    }

    @SuppressLint({"InlinedApi"})
    public static void setInitCount(String key, int count) {
        if (SourceTool.gContext != null) {
            Editor edit = SourceTool.gContext.getSharedPreferences("my_movie_sp", 4).edit();
            edit.putInt(key, count);
            edit.commit();
        }
    }

    @SuppressLint({"InlinedApi"})
    public static int getInitCount(String key) {
        if (SourceTool.gContext != null) {
            return SourceTool.gContext.getSharedPreferences("my_movie_sp", 4).getInt(key, 0);
        }
        return 0;
    }

    public static int trimAlbumSetCount(int pageIndex, int pageSize, List list, int count) {
        if (pageIndex > 1 || pageSize <= 0 || list == null || list.size() == 0) {
            return count;
        }
        int size = list.size();
        if (count <= 0 || count >= pageSize || size >= count) {
            return count;
        }
        USALog.m147d("trimAlbumSetCount count: " + count + ", realSize: " + size);
        return size;
    }
}
