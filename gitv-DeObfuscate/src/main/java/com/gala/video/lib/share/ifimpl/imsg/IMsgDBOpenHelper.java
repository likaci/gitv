package com.gala.video.lib.share.ifimpl.imsg;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.gala.tvapi.tv2.TVApiBase;
import com.gala.video.lib.share.ifimpl.imsg.utils.IMsgUtils;
import com.gala.video.lib.share.ifimpl.imsg.utils.IMsgUtils.DBColumns;
import com.gala.video.lib.share.ifmanager.bussnessIF.imsg.IMsgContent;
import java.util.List;

class IMsgDBOpenHelper extends SQLiteOpenHelper {
    IMsgDBOpenHelper() {
        super(IMsgUtils.sContext, DBColumns.DB_NAME, null, 9);
    }

    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        System.out.println("oncreate");
        sqLiteDatabase.execSQL("create table if not exists imsg(_id integer primary key autoincrement, tid integer, title text, level integer, type integer, version text, pic text, des text, bname text, detail integer, jump integer, url text, plid text, aid text, tvid text, read integer, tv_type integer, series integer, source text, channel integer, VIP text, MSG_ID integer, localTime integer, album text, show integer, msgContent text, style integer, url_window text, coupon_key text, coupon_sign text, valid_till integer)");
    }

    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            try {
                IMsgDBUpgradeHelper updateHp = new IMsgDBUpgradeHelper();
                List<IMsgContent> list = updateHp.query(sqLiteDatabase);
                sqLiteDatabase.execSQL("DROP TABLE IF EXISTS imsg");
                onCreate(sqLiteDatabase);
                if (list != null && list.size() > 0) {
                    long invalidTime = TVApiBase.getTVApiProperty().getCurrentTime() + IMsgUtils.DEFAULT_INVALID_TIME;
                    for (IMsgContent content : list) {
                        if (content.valid_till == 0) {
                            content.valid_till = invalidTime;
                        }
                        updateHp.removeData(sqLiteDatabase, content);
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                sqLiteDatabase.execSQL("DROP TABLE IF EXISTS imsg");
                onCreate(sqLiteDatabase);
            }
        }
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion == 0 || oldVersion > newVersion) {
            db.execSQL("DROP TABLE IF EXISTS imsg");
            onCreate(db);
            return;
        }
        onUpgrade(db, oldVersion, newVersion);
    }

    public SQLiteDatabase getReadableDatabase() {
        try {
            return super.getReadableDatabase();
        } catch (Exception e) {
            return null;
        }
    }

    public SQLiteDatabase getWritableDatabase() {
        try {
            return super.getWritableDatabase();
        } catch (Exception e) {
            return null;
        }
    }
}
