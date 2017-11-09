package com.gala.video.app.epg.ui.albumlist.desktop.searchresult;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SearchResultDBHelper extends SQLiteOpenHelper {
    public static final String COLUMN_ALBUMID = "search_albumid";
    private static final String COLUMN_ID = "_id";
    public static final String COLUMN_TIME = "time";
    public static final String DB_NAME = "search_album_times.db";
    private static final int DB_VERSION = 1;
    public static final String TABLE_NAME = "search_album";

    public SearchResultDBHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    public void onCreate(SQLiteDatabase db) {
        StringBuilder sb = new StringBuilder("CREATE TABLE IF NOT EXISTS ");
        sb.append(TABLE_NAME).append('(').append(COLUMN_ID).append(" INTEGER PRIMARY KEY,").append(COLUMN_ALBUMID).append(" TEXT,").append("time").append(" INTEGER").append(')');
        db.execSQL(sb.toString());
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS search_album");
        onCreate(db);
    }
}
