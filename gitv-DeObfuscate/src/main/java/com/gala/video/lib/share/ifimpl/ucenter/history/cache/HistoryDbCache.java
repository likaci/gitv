package com.gala.video.lib.share.ifimpl.ucenter.history.cache;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import com.gala.tvapi.tv2.model.VipInfo;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.common.configs.AppClientUtils;
import com.gala.video.lib.share.ifimpl.ucenter.history.impl.HistoryInfoHelper;
import com.gala.video.lib.share.ifmanager.bussnessIF.ucenter.history.HistoryInfo;
import com.gala.video.lib.share.ifmanager.bussnessIF.ucenter.history.HistoryInfo.Builder;
import java.util.ArrayList;
import java.util.List;

public class HistoryDbCache {
    private static final String[] COLUMS = new String[]{"albumid", "tvid", COLUM_PLAY_ORDER, COLUM_PLAY_TIME, COLUM_ADD_TIME, "uploadtime", COLUM_TOKEN, COLUM_VID, "name", "duration", "pic", COLUM_TV_PIC, COLUM_TV_SETS, COLUM_IS3D, COLUM_ISSERIES, COLUM_EXCLUSIVE, COLUM_ISPURCHASE, COLUM_CHANNEL_ID, "is1080p", COLUM_SOURCECODE, "time", COLUM_TV_NAME, "type", COLUM_INDIVI_DEMAND, COLUM_IS_COMMITTED, COLUM_IS_VIP, COLUM_IS_COUPON, COLUM_IS_TVOD, COLUM_IS_PACKAGE, COLUM_EP_IS_VIP, COLUM_EP_IS_COUPON, COLUM_EP_IS_TVOD, COLUM_EP_IS_PACKAGE, COLUM_EP_PAYMARK, COLUM_TV_COUNT, COLUM_DRM, COLUM_SHORT_NAME, COLUM_CONTENT_TYPE, COLUM_HDR};
    private static final String COLUM_ADD_TIME = "addtime";
    private static final String COLUM_ALBUM_ID = "albumid";
    private static final String COLUM_CHANNEL_ID = "channelid";
    private static final String COLUM_CONTENT_TYPE = "reserved3";
    private static final String COLUM_DRM = "reserved1";
    private static final String COLUM_DURATION = "duration";
    private static final String COLUM_EP_IS_COUPON = "episcoupon";
    private static final String COLUM_EP_IS_PACKAGE = "epispackage";
    private static final String COLUM_EP_IS_TVOD = "epistvod";
    private static final String COLUM_EP_IS_VIP = "episvip";
    private static final String COLUM_EP_PAYMARK = "eppaymark";
    private static final String COLUM_EXCLUSIVE = "exclusive";
    private static final String COLUM_HDR = "hdr";
    private static final String COLUM_INDIVI_DEMAND = "individemand";
    private static final String COLUM_IS1080P = "is1080p";
    private static final String COLUM_IS3D = "is3d";
    private static final String COLUM_ISPURCHASE = "ispurchase";
    private static final String COLUM_ISSERIES = "isseries";
    private static final String COLUM_IS_COMMITTED = "committed";
    private static final String COLUM_IS_COUPON = "iscoupon";
    private static final String COLUM_IS_PACKAGE = "ispackage";
    private static final String COLUM_IS_TVOD = "istvod";
    private static final String COLUM_IS_VIP = "isvip";
    private static final String COLUM_NAME = "name";
    private static final String COLUM_PIC = "pic";
    private static final String COLUM_PLAY_ORDER = "playorder";
    private static final String COLUM_PLAY_TIME = "playtime";
    private static final String COLUM_SHORT_NAME = "reserved2";
    private static final String COLUM_SOURCECODE = "soucecode";
    private static final String COLUM_TIME = "time";
    private static final String COLUM_TOKEN = "token";
    private static final String COLUM_TV_COUNT = "tvcount";
    private static final String COLUM_TV_ID = "tvid";
    private static final String COLUM_TV_NAME = "tvname";
    private static final String COLUM_TV_PIC = "tvpic";
    private static final String COLUM_TV_SETS = "tvsets";
    private static final String COLUM_TYPE = "type";
    private static final String COLUM_UPLOAD_TIME = "uploadtime";
    private static final String COLUM_VID = "vid";
    private static final String DATABASE_NAME = "local_cache.db";
    private static final int DATABASE_VERSION = 5;
    private static final int INDEX_ADD_TIME = 4;
    private static final int INDEX_ALBUM_ID = 0;
    private static final int INDEX_CHANNEL_ID = 17;
    private static final int INDEX_CONTENT_TYPE = 37;
    private static final int INDEX_DRM = 35;
    private static final int INDEX_DURATION = 9;
    private static final int INDEX_EP_IS_COUPON = 30;
    private static final int INDEX_EP_IS_PACKAGE = 32;
    private static final int INDEX_EP_IS_PAYMARK = 33;
    private static final int INDEX_EP_IS_TVOD = 31;
    private static final int INDEX_EP_IS_VIP = 29;
    private static final int INDEX_EXCLUSIVE = 15;
    private static final int INDEX_HDR = 38;
    private static final int INDEX_IS1080P = 18;
    private static final int INDEX_IS3D = 13;
    private static final int INDEX_ISSERIES = 14;
    private static final int INDEX_IS_COMMITTED = 24;
    private static final int INDEX_IS_COUPON = 26;
    private static final int INDEX_IS_PACKAGE = 28;
    private static final int INDEX_IS_PURCHASE = 16;
    private static final int INDEX_IS_TVOD = 27;
    private static final int INDEX_IS_VIP = 25;
    private static final int INDEX_NAME = 8;
    private static final int INDEX_PIC = 10;
    private static final int INDEX_PLAY_ORDER = 2;
    private static final int INDEX_PLAY_TIME = 3;
    private static final int INDEX_SHORT_NAME = 36;
    private static final int INDEX_SOURCECODE = 19;
    private static final int INDEX_TIME = 20;
    private static final int INDEX_TOKEN = 6;
    private static final int INDEX_TVPIC = 11;
    private static final int INDEX_TVSET = 12;
    private static final int INDEX_TV_COUNT = 34;
    private static final int INDEX_TV_ID = 1;
    private static final int INDEX_TV_INDIVIDEMAND = 23;
    private static final int INDEX_TV_NAME = 21;
    private static final int INDEX_TV_TYPE = 22;
    private static final int INDEX_UPLOAD_TIME = 5;
    private static final int INDEX_VID = 7;
    private static final String NULL_COLUMN = "uploadtime";
    private static final String SQL_CLEAR_TABLE = "DELETE FROM history WHERE token=?;";
    private static final String SQL_CLEAR_TABLE_ALL = "DELETE FROM history;";
    private static final String SQL_CLEAR_TABLE_OF_LOGIN = "DELETE FROM history WHERE token!=?;";
    private static final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS history;";
    private static final String TABLE_NAME = "history";
    private static final String TAG = "HistoryDbCache";
    private final int mCapacity;
    private final Context mContext = AppRuntimeEnv.get().getApplicationContext();
    private SQLiteOpenHelper mHelper;
    private SQLiteStatement mInsertStatement;

    private class HistoryOpenHelper extends SQLiteOpenHelper {
        public HistoryOpenHelper(Context context, String name, int version) {
            super(context, name, null, version);
        }

        public void onCreate(SQLiteDatabase db) {
            LogUtils.m1571e(HistoryDbCache.TAG, "database on create!");
            db.execSQL(HistoryDbCache.SQL_DROP_TABLE);
            db.execSQL(HistoryDbCache.buildCreateTable());
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            LogUtils.m1571e(HistoryDbCache.TAG, "history database upgrade!" + oldVersion + " to " + newVersion);
            if (oldVersion == 4 && newVersion == 5) {
                try {
                    db.execSQL("ALTER TABLE history ADD COLUMN hdr TEXT");
                    return;
                } catch (SQLException e) {
                    LogUtils.m1572e(HistoryDbCache.TAG, "on upgrade database exception " + oldVersion + " to " + newVersion, e);
                    return;
                }
            }
            try {
                db.execSQL(HistoryDbCache.SQL_DROP_TABLE);
                db.execSQL(HistoryDbCache.buildCreateTable());
            } catch (SQLException e2) {
                LogUtils.m1572e(HistoryDbCache.TAG, "on upgrade database exception ", e2);
            }
        }

        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            LogUtils.m1571e(HistoryDbCache.TAG, "history database downgrade!");
            try {
                db.execSQL(HistoryDbCache.SQL_DROP_TABLE);
                db.execSQL(HistoryDbCache.buildCreateTable());
            } catch (SQLException e) {
                LogUtils.m1572e(HistoryDbCache.TAG, "on downgrade database exception ", e);
            }
        }
    }

    public HistoryDbCache(int capacity) {
        this.mCapacity = capacity;
        this.mHelper = new HistoryOpenHelper(this.mContext, DATABASE_NAME, 5);
    }

    private synchronized void initInsertStatement() {
        if (this.mInsertStatement == null) {
            this.mInsertStatement = this.mHelper.getWritableDatabase().compileStatement(buildInsertSql());
        }
    }

    private static String buildInsertSql() {
        int index;
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO ").append("history").append("(");
        for (index = 0; index < COLUMS.length - 1; index++) {
            sql.append(COLUMS[index]).append(",");
        }
        sql.append(COLUM_HDR);
        sql.append(")").append("VALUES").append("(");
        for (index = 0; index < COLUMS.length - 1; index++) {
            sql.append("?,");
        }
        sql.append("?").append(");");
        return sql.toString();
    }

    public static String buildDropTableSql() {
        return SQL_DROP_TABLE;
    }

    public static String buildCreateTable() {
        StringBuilder sql = new StringBuilder("CREATE TABLE IF NOT EXISTS ");
        sql.append("history").append("(").append("albumid").append(" TEXT,").append("tvid").append(" TEXT,").append(COLUM_PLAY_ORDER).append(" INTEGER,").append(COLUM_PLAY_TIME).append(" INTEGER,").append(COLUM_ADD_TIME).append(" INTEGER,").append("uploadtime").append(" INTEGER,").append(COLUM_TOKEN).append(" TEXT,").append(COLUM_VID).append(" TEXT,").append("name").append(" TEXT,").append("duration").append(" INTEGER,").append("pic").append(" TEXT,").append(COLUM_TV_PIC).append(" TEXT,").append(COLUM_TV_SETS).append(" INTEGER,").append(COLUM_IS3D).append(" INTEGER,").append(COLUM_ISSERIES).append(" INTEGER,").append(COLUM_EXCLUSIVE).append(" INTEGER,").append(COLUM_ISPURCHASE).append(" INTEGER,").append(COLUM_CHANNEL_ID).append(" INTEGER,").append("is1080p").append(" INTEGER,").append(COLUM_SOURCECODE).append(" TEXT,").append("time").append(" TEXT,").append(COLUM_TV_NAME).append(" TEXT,").append("type").append(" INTEGER,").append(COLUM_INDIVI_DEMAND).append(" INTEGER,").append(COLUM_IS_COMMITTED).append(" INTEGER,").append(COLUM_IS_VIP).append(" INTEGER,").append(COLUM_IS_COUPON).append(" INTEGER,").append(COLUM_IS_TVOD).append(" INTEGER,").append(COLUM_IS_PACKAGE).append(" INTEGER,").append(COLUM_EP_IS_VIP).append(" INTEGER,").append(COLUM_EP_IS_COUPON).append(" INTEGER,").append(COLUM_EP_IS_TVOD).append(" INTEGER,").append(COLUM_EP_IS_PACKAGE).append(" INTEGER,").append(COLUM_EP_PAYMARK).append(" INTEGER,").append(COLUM_TV_COUNT).append(" INTEGER,").append(COLUM_DRM).append(" TEXT,").append(COLUM_SHORT_NAME).append(" TEXT,").append(COLUM_CONTENT_TYPE).append(" TEXT,").append(COLUM_HDR).append(" TEXT,").append("PRIMARY KEY(").append("albumid").append(",").append(COLUM_TOKEN).append(")").append(")");
        return sql.toString();
    }

    public void put(HistoryInfo info) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "put(" + info + ")");
        }
        ContentValues values = new ContentValues();
        values.put("albumid", info.getAlbum().qpId);
        values.put("tvid", info.getTvId());
        values.put(COLUM_PLAY_ORDER, Integer.valueOf(info.getPlayOrder()));
        values.put(COLUM_PLAY_TIME, Long.valueOf((long) info.getPlayTime()));
        values.put(COLUM_ADD_TIME, Long.valueOf(info.getAddTime()));
        values.put("uploadtime", Long.valueOf(info.getUploadTime()));
        values.put(COLUM_TOKEN, info.getCookie());
        values.put(COLUM_VID, info.getAlbum().vid);
        values.put("name", info.getAlbum().name);
        values.put("duration", info.getAlbum().len);
        values.put("pic", info.getAlbum().pic);
        values.put(COLUM_TV_PIC, info.getAlbum().tvPic);
        values.put(COLUM_TV_SETS, Integer.valueOf(info.getAlbum().tvsets));
        values.put(COLUM_IS3D, Integer.valueOf(info.getAlbum().is3D));
        values.put(COLUM_ISSERIES, Integer.valueOf(info.getAlbum().isSeries));
        values.put(COLUM_EXCLUSIVE, Integer.valueOf(info.getAlbum().exclusive));
        values.put(COLUM_ISPURCHASE, Integer.valueOf(info.getAlbum().isPurchase));
        values.put(COLUM_CHANNEL_ID, Integer.valueOf(info.getAlbum().chnId));
        values.put("is1080p", info.getAlbum().stream);
        values.put(COLUM_SOURCECODE, info.getAlbum().sourceCode);
        values.put("time", info.getAlbum().time);
        values.put(COLUM_TV_NAME, info.getAlbum().tvName);
        values.put("type", Integer.valueOf(info.getAlbum().type));
        values.put(COLUM_INDIVI_DEMAND, Integer.valueOf(info.getAlbum().indiviDemand));
        if (info.getAlbum().vipInfo != null) {
            values.put(COLUM_IS_VIP, Integer.valueOf(info.getAlbum().vipInfo.isVip));
            values.put(COLUM_IS_COUPON, Integer.valueOf(info.getAlbum().vipInfo.isCoupon));
            values.put(COLUM_IS_TVOD, Integer.valueOf(info.getAlbum().vipInfo.isTvod));
            values.put(COLUM_IS_PACKAGE, Integer.valueOf(info.getAlbum().vipInfo.isPkg));
            values.put(COLUM_EP_IS_VIP, Integer.valueOf(info.getAlbum().vipInfo.epIsVip));
            values.put(COLUM_EP_IS_COUPON, Integer.valueOf(info.getAlbum().vipInfo.epIsCoupon));
            values.put(COLUM_EP_IS_TVOD, Integer.valueOf(info.getAlbum().vipInfo.epIsTvod));
            values.put(COLUM_EP_IS_PACKAGE, Integer.valueOf(info.getAlbum().vipInfo.epIsPkg));
            values.put(COLUM_EP_PAYMARK, Integer.valueOf(HistoryInfoHelper.convertPayMarkForDb(info.getAlbum().getPayMarkType())));
        }
        values.put(COLUM_TV_COUNT, Integer.valueOf(info.getAlbum().tvCount));
        values.put(COLUM_DRM, info.getAlbum().drm);
        values.put(COLUM_SHORT_NAME, info.getAlbum().shortName);
        values.put(COLUM_CONTENT_TYPE, String.valueOf(info.getAlbum().contentType));
        values.put(COLUM_HDR, info.getAlbum().dynamicRanges);
        long count = Long.MIN_VALUE;
        SQLiteDatabase db = this.mHelper.getWritableDatabase();
        try {
            db.beginTransaction();
            count = this.mHelper.getWritableDatabase().replace("history", "uploadtime", values);
            db.setTransactionSuccessful();
            db.endTransaction();
        } catch (SQLiteException e) {
            LogUtils.m1572e(TAG, "put(" + info + ") error! ", e);
        }
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "put(" + info + ") add " + count);
        }
    }

    public HistoryInfo getTv(String tvId) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "getFromTv(" + tvId + ")");
        }
        HistoryInfo info = null;
        try {
            Cursor cursor = this.mHelper.getWritableDatabase().query("history", COLUMS, "tvid=? and token=? ", new String[]{tvId, AppClientUtils.getCookie(this.mContext)}, null, null, null);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    info = createInfoFromCursor(cursor);
                }
                cursor.close();
            }
        } catch (SQLiteException e) {
            LogUtils.m1572e(TAG, "getFromTv(" + tvId + ") error!", e);
        }
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "getFromTv(" + tvId + ") return " + info);
        }
        return info;
    }

    public HistoryInfo getAlbum(String albumId) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "getFromAlbum(" + albumId + ")");
        }
        HistoryInfo info = null;
        try {
            Cursor cursor = this.mHelper.getWritableDatabase().query("history", COLUMS, "albumid=? and token=? ", new String[]{albumId, AppClientUtils.getCookie(this.mContext)}, null, null, null);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    info = createInfoFromCursor(cursor);
                }
                cursor.close();
            }
        } catch (SQLiteException e) {
            LogUtils.m1572e(TAG, "getFromAlbum(" + albumId + ") error!", e);
        }
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "getFromAlbum(" + albumId + ") return " + info);
        }
        return info;
    }

    public void removeAlbum(String albumId) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "removeAlbum(" + albumId + ")");
        }
        int count = Integer.MIN_VALUE;
        try {
            count = this.mHelper.getWritableDatabase().delete("history", "albumid=? and token=? ", new String[]{albumId, AppClientUtils.getCookie(this.mContext)});
        } catch (SQLiteException e) {
            LogUtils.m1572e(TAG, "removeAlbum(" + albumId + ") error!", e);
        }
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "removeAlbum(" + albumId + ") " + count);
        }
    }

    public List<HistoryInfo> reload(String cookie) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "reload()" + cookie);
        }
        List<HistoryInfo> list = new ArrayList();
        try {
            Cursor cursor = this.mHelper.getWritableDatabase().query("history", COLUMS, "token=? ", new String[]{cookie}, null, null, null);
            if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
                do {
                    list.add(createInfoFromCursor(cursor));
                } while (cursor.moveToNext());
                cursor.close();
            }
        } catch (SQLiteException e) {
            LogUtils.m1572e(TAG, "load() error! ", e);
        }
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "reload() return " + list.size());
        }
        return list;
    }

    private HistoryInfo createInfoFromCursor(Cursor cursor) {
        String albumid = cursor.getString(0);
        String tvid = cursor.getString(1);
        int playOrder = cursor.getInt(2);
        int playTime = (int) cursor.getLong(3);
        long addTime = cursor.getLong(4);
        long uploadTime = cursor.getLong(5);
        String cookie = cursor.getString(6);
        String vid = cursor.getString(7);
        String name = cursor.getString(8);
        String duration = cursor.getString(9);
        String pic = cursor.getString(10);
        String tvPic = cursor.getString(11);
        int tvsets = cursor.getInt(12);
        int is3D = cursor.getInt(13);
        int isSeries = cursor.getInt(14);
        int exclusive = cursor.getInt(15);
        int isPurchase = cursor.getInt(16);
        int channelId = cursor.getInt(17);
        String stream = cursor.getString(18);
        String sourceCode = cursor.getString(19);
        String time = cursor.getString(20);
        String tvName = cursor.getString(21);
        int type = cursor.getInt(22);
        int indiviDemand = cursor.getInt(23);
        int isCommitted = cursor.getInt(24);
        int isVip = cursor.getInt(25);
        int isCoupon = cursor.getInt(26);
        int isTvod = cursor.getInt(27);
        int isPackage = cursor.getInt(28);
        int epIsVip = cursor.getInt(29);
        int epIsCoupon = cursor.getInt(30);
        int epIsTvod = cursor.getInt(31);
        int epIsPackage = cursor.getInt(32);
        int paymark = cursor.getInt(33);
        int tvCount = cursor.getInt(34);
        String drm = cursor.getString(35);
        String shortName = cursor.getString(36);
        int contentType = -1;
        if (!StringUtils.isEmpty(cursor.getString(37))) {
            contentType = StringUtils.parse(cursor.getString(37), -1);
        }
        HistoryInfo info = new Builder(cookie).albumId(albumid).tvId(tvid).playOrder(playOrder).playTime(playTime).addedTime(addTime).uploadTime(uploadTime).vid(vid).name(name).duration(duration).pic(pic).tvPic(tvPic).tvSets(tvsets).is3D(is3D).isSeries(isSeries).exclusive(exclusive).isPurchase(isPurchase).channelId(channelId).stream(stream).sourceCode(sourceCode).time(time).tvName(tvName).type(type).indivDemand(indiviDemand).isCommitted(isCommitted).isVip(isVip).isCoupon(isCoupon).isTvod(isTvod).isPackage(isPackage).epIsVip(epIsVip).epIsCoupon(epIsCoupon).epIsTvod(epIsTvod).epIsPackage(epIsPackage).payMark(paymark).tvCount(tvCount).drm(drm).shortName(shortName).contentType(contentType).hdr(cursor.getString(38)).build();
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "createInfoFromCursor() return " + info);
        }
        return info;
    }

    public void update(List<HistoryInfo> list) {
        if (list != null && !list.isEmpty()) {
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(TAG, "update() " + list.size());
            }
            initInsertStatement();
            String token = ((HistoryInfo) list.get(0)).getCookie();
            SQLiteDatabase db = this.mHelper.getWritableDatabase();
            try {
                db.beginTransaction();
                db.execSQL(SQL_CLEAR_TABLE, new String[]{token});
            } catch (SQLiteException e) {
                LogUtils.m1572e(TAG, "update() error!", e);
            }
            for (HistoryInfo info : list) {
                try {
                    this.mInsertStatement.bindString(1, info.getAlbum().qpId == null ? "" : info.getAlbum().qpId);
                    this.mInsertStatement.bindString(2, info.getTvId() == null ? "" : info.getTvId());
                    this.mInsertStatement.bindLong(3, (long) info.getPlayOrder());
                    this.mInsertStatement.bindLong(4, (long) info.getPlayTime());
                    this.mInsertStatement.bindLong(5, info.getAddTime());
                    this.mInsertStatement.bindLong(6, info.getUploadTime());
                    this.mInsertStatement.bindString(7, info.getCookie() == null ? "" : info.getCookie());
                    this.mInsertStatement.bindString(8, info.getAlbum().vid == null ? "" : info.getAlbum().vid);
                    this.mInsertStatement.bindString(9, info.getAlbum().name == null ? "" : info.getAlbum().name);
                    this.mInsertStatement.bindString(10, info.getAlbum().len == null ? "" : info.getAlbum().len);
                    this.mInsertStatement.bindString(11, info.getAlbum().pic == null ? "" : info.getAlbum().pic);
                    this.mInsertStatement.bindString(12, info.getAlbum().tvPic == null ? "" : info.getAlbum().tvPic);
                    this.mInsertStatement.bindLong(13, (long) info.getAlbum().tvsets);
                    this.mInsertStatement.bindLong(14, (long) info.getAlbum().is3D);
                    this.mInsertStatement.bindLong(15, (long) info.getAlbum().isSeries);
                    this.mInsertStatement.bindLong(16, (long) info.getAlbum().exclusive);
                    this.mInsertStatement.bindLong(17, (long) info.getAlbum().isPurchase);
                    this.mInsertStatement.bindLong(18, (long) info.getAlbum().chnId);
                    this.mInsertStatement.bindString(19, info.getAlbum().stream == null ? "" : info.getAlbum().stream);
                    this.mInsertStatement.bindString(20, info.getAlbum().sourceCode == null ? "" : info.getAlbum().sourceCode);
                    this.mInsertStatement.bindString(21, info.getAlbum().time == null ? "" : info.getAlbum().time);
                    this.mInsertStatement.bindString(22, info.getAlbum().tvName == null ? "" : info.getAlbum().tvName);
                    this.mInsertStatement.bindLong(23, (long) info.getAlbum().type);
                    this.mInsertStatement.bindLong(24, (long) info.getAlbum().indiviDemand);
                    this.mInsertStatement.bindLong(25, (long) info.isCommitted());
                    VipInfo vipInfo = info.getAlbum().vipInfo;
                    this.mInsertStatement.bindLong(26, vipInfo != null ? (long) vipInfo.isVip : 0);
                    this.mInsertStatement.bindLong(27, vipInfo != null ? (long) vipInfo.isCoupon : 0);
                    this.mInsertStatement.bindLong(28, vipInfo != null ? (long) vipInfo.isTvod : 0);
                    this.mInsertStatement.bindLong(29, vipInfo != null ? (long) vipInfo.isPkg : 0);
                    this.mInsertStatement.bindLong(30, vipInfo != null ? (long) vipInfo.epIsVip : 0);
                    this.mInsertStatement.bindLong(31, vipInfo != null ? (long) vipInfo.epIsTvod : 0);
                    this.mInsertStatement.bindLong(32, vipInfo != null ? (long) vipInfo.epIsTvod : 0);
                    this.mInsertStatement.bindLong(33, vipInfo != null ? (long) vipInfo.epIsPkg : 0);
                    this.mInsertStatement.bindLong(34, (long) HistoryInfoHelper.convertPayMarkForDb(info.getAlbum().getPayMarkType()));
                    this.mInsertStatement.bindLong(35, (long) info.getAlbum().tvCount);
                    this.mInsertStatement.bindString(36, info.getAlbum().drm);
                    this.mInsertStatement.bindString(37, info.getAlbum().shortName);
                    this.mInsertStatement.bindString(38, String.valueOf(info.getAlbum().contentType));
                    this.mInsertStatement.bindString(39, info.getAlbum().dynamicRanges);
                    long count = this.mInsertStatement.executeInsert();
                    if (LogUtils.mIsDebug) {
                        LogUtils.m1568d(TAG, "update() insert count " + count);
                    }
                } catch (Exception e2) {
                    LogUtils.m1572e(TAG, "update() error! " + info, e2);
                }
            }
            try {
                db.setTransactionSuccessful();
                db.endTransaction();
            } catch (SQLiteException e3) {
                LogUtils.m1572e(TAG, "update() error!", e3);
            }
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(TAG, "update() end.");
            }
        }
    }

    public void merge(List<HistoryInfo> list) {
        if (list != null && !list.isEmpty()) {
            SQLiteDatabase db = this.mHelper.getWritableDatabase();
            try {
                db.beginTransaction();
                for (HistoryInfo info : list) {
                    ContentValues values = new ContentValues();
                    values.put("albumid", info.getAlbum().qpId);
                    values.put("tvid", info.getTvId());
                    values.put(COLUM_PLAY_ORDER, Integer.valueOf(info.getPlayOrder()));
                    values.put(COLUM_PLAY_TIME, Long.valueOf((long) info.getPlayTime()));
                    values.put(COLUM_ADD_TIME, Long.valueOf(info.getAddTime()));
                    values.put("uploadtime", Long.valueOf(info.getUploadTime()));
                    values.put(COLUM_TOKEN, info.getCookie());
                    values.put(COLUM_VID, info.getAlbum().vid);
                    values.put("name", info.getAlbum().name);
                    values.put("duration", info.getAlbum().len);
                    values.put("pic", info.getAlbum().pic);
                    values.put(COLUM_TV_PIC, info.getAlbum().tvPic);
                    values.put(COLUM_TV_SETS, Integer.valueOf(info.getAlbum().tvsets));
                    values.put(COLUM_IS3D, Integer.valueOf(info.getAlbum().is3D));
                    values.put(COLUM_ISSERIES, Integer.valueOf(info.getAlbum().isSeries));
                    values.put(COLUM_EXCLUSIVE, Integer.valueOf(info.getAlbum().exclusive));
                    values.put(COLUM_ISPURCHASE, Integer.valueOf(info.getAlbum().isPurchase));
                    values.put(COLUM_CHANNEL_ID, Integer.valueOf(info.getAlbum().chnId));
                    values.put("is1080p", info.getAlbum().stream);
                    values.put(COLUM_SOURCECODE, info.getAlbum().sourceCode);
                    values.put("time", info.getAlbum().time);
                    values.put(COLUM_TV_NAME, info.getAlbum().tvName);
                    values.put("type", Integer.valueOf(info.getAlbum().type));
                    values.put(COLUM_INDIVI_DEMAND, Integer.valueOf(info.getAlbum().indiviDemand));
                    if (info.getAlbum().vipInfo != null) {
                        values.put(COLUM_IS_VIP, Integer.valueOf(info.getAlbum().vipInfo.isVip));
                        values.put(COLUM_IS_COUPON, Integer.valueOf(info.getAlbum().vipInfo.isCoupon));
                        values.put(COLUM_IS_TVOD, Integer.valueOf(info.getAlbum().vipInfo.isTvod));
                        values.put(COLUM_IS_PACKAGE, Integer.valueOf(info.getAlbum().vipInfo.isPkg));
                        values.put(COLUM_EP_IS_VIP, Integer.valueOf(info.getAlbum().vipInfo.epIsVip));
                        values.put(COLUM_EP_IS_COUPON, Integer.valueOf(info.getAlbum().vipInfo.epIsCoupon));
                        values.put(COLUM_EP_IS_TVOD, Integer.valueOf(info.getAlbum().vipInfo.epIsTvod));
                        values.put(COLUM_EP_IS_PACKAGE, Integer.valueOf(info.getAlbum().vipInfo.epIsPkg));
                        values.put(COLUM_EP_PAYMARK, Integer.valueOf(HistoryInfoHelper.convertPayMarkForDb(info.getAlbum().getPayMarkType())));
                    }
                    values.put(COLUM_TV_COUNT, Integer.valueOf(info.getAlbum().tvCount));
                    values.put(COLUM_DRM, info.getAlbum().drm);
                    values.put(COLUM_SHORT_NAME, info.getAlbum().shortName);
                    values.put(COLUM_CONTENT_TYPE, String.valueOf(info.getAlbum().contentType));
                    values.put(COLUM_HDR, info.getAlbum().dynamicRanges);
                    this.mHelper.getWritableDatabase().replace("history", "uploadtime", values);
                }
                db.setTransactionSuccessful();
                db.endTransaction();
            } catch (SQLiteException e) {
            }
        }
    }

    public void clearDbForUser() {
        String cookie = AppClientUtils.getCookie(this.mContext);
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "clear db for when user login out cookie=" + cookie);
        }
        try {
            this.mHelper.getWritableDatabase().execSQL(SQL_CLEAR_TABLE_OF_LOGIN, new String[]{cookie});
        } catch (SQLiteException e) {
            LogUtils.m1572e(TAG, "clear() error!", e);
        }
    }

    public void clear() {
        String cookie = AppClientUtils.getCookie(this.mContext);
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "clear db cookie=" + cookie);
        }
        try {
            this.mHelper.getWritableDatabase().execSQL(SQL_CLEAR_TABLE, new String[]{cookie});
        } catch (SQLiteException e) {
            LogUtils.m1572e(TAG, "clear() error!", e);
        }
    }
}
