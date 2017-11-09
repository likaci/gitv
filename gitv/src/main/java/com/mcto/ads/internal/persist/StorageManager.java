package com.mcto.ads.internal.persist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.mcto.ads.internal.common.CupidUtils;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class StorageManager {
    private static DBHelper dbHelper;
    private static AtomicInteger openCounter = new AtomicInteger();
    private SQLiteDatabase database;

    public synchronized void initialize(Context context) {
        try {
            if (dbHelper == null) {
                dbHelper = new DBHelper(context);
            }
        } catch (Exception ex) {
            Log.d("a71_ads_client", "initialize(): " + ex);
        }
        openDataBase();
    }

    public synchronized void openDataBase() {
        this.database = dbHelper.getWritableDatabase();
    }

    public synchronized void closeDataBase() {
        if (openCounter.decrementAndGet() == 0 && this.database.isOpen()) {
            this.database.close();
        }
    }

    public boolean insertNativeVideoItem(ContentValues contentValues) {
        Log.d("a71_ads_client", "insertNativeVideoItem():");
        if (contentValues == null) {
            return false;
        }
        try {
            Log.d("a71_ads_client", "insertNativeVideoItem(): result: " + this.database.insertOrThrow(DBConstants.DB_NATIVE_TABLE_NAME, null, contentValues));
            return true;
        } catch (Exception ex) {
            Log.d("a71_ads_client", "insertNativeVideoItem(): " + ex);
            return false;
        }
    }

    public boolean deleteNativeVideoItems(List<String> items) {
        if (items == null || items.isEmpty()) {
            return false;
        }
        Log.d("a71_ads_client", "deleteNativeVideoItems(): size:" + items.size());
        try {
            for (String key : items) {
                this.database.delete(DBConstants.DB_NATIVE_TABLE_NAME, "identifier=?", new String[]{key});
            }
            return true;
        } catch (Exception ex) {
            Log.d("a71_ads_client", "deleteNativeVideoItems(): " + ex);
            return false;
        }
    }

    public boolean updateNativeVideoItem(String identifier, ContentValues contentValues) {
        if (!CupidUtils.isValidStr(identifier) || contentValues == null) {
            return false;
        }
        try {
            this.database.update(DBConstants.DB_NATIVE_TABLE_NAME, contentValues, "identifier=?", new String[]{identifier});
            return true;
        } catch (Exception ex) {
            Log.d("a71_ads_client", "updateNativeVideoItem(): " + ex);
            return false;
        }
    }

    public Map<String, Object> getNativeVideoItem(String identifier) {
        Map<String, Object> rtn = new HashMap();
        if (CupidUtils.isValidStr(identifier)) {
            try {
                Cursor cursor = this.database.rawQuery("select * from native where identifier=?", new String[]{identifier});
                if (cursor.moveToFirst()) {
                    rtn.put(DBConstants.DB_KEY_PLAY_TYPE, Integer.valueOf(cursor.getInt(cursor.getColumnIndex(DBConstants.DB_KEY_PLAY_TYPE))));
                    rtn.put(DBConstants.DB_KEY_PLAY_COUNT, Integer.valueOf(cursor.getInt(cursor.getColumnIndex(DBConstants.DB_KEY_PLAY_COUNT))));
                    rtn.put(DBConstants.DB_KEY_SEND_RECORD, Integer.valueOf(cursor.getInt(cursor.getColumnIndex(DBConstants.DB_KEY_SEND_RECORD))));
                    rtn.put(DBConstants.DB_KEY_LAST_UPDATE_TIME, Integer.valueOf(cursor.getInt(cursor.getColumnIndex(DBConstants.DB_KEY_LAST_UPDATE_TIME))));
                }
                cursor.close();
            } catch (Exception ex) {
                Log.d("a71_ads_client", "getNativeVideoItems(): identifier: " + identifier + ", ex: " + ex);
            }
        } else {
            Log.d("a71_ads_client", "invalid identifier.");
        }
        return rtn;
    }

    public void checkValidityOfNativeVideoItems() {
        Log.d("a71_ads_client", "checkValidityOfNativeVideoItems():");
        int currentTime = (int) (new Date().getTime() / 1000);
        List<String> items = new ArrayList();
        try {
            Cursor cursor = this.database.rawQuery("select * from native", null);
            while (cursor.moveToNext()) {
                if (currentTime - cursor.getInt(cursor.getColumnIndex(DBConstants.DB_KEY_LAST_UPDATE_TIME)) > DBConstants.DB_NATIVE_CACHE_HOLD_TIME) {
                    items.add(cursor.getString(cursor.getColumnIndex(DBConstants.DB_KEY_IDENTIFIER)));
                }
            }
            cursor.close();
            deleteNativeVideoItems(items);
        } catch (Exception ex) {
            Log.d("a71_ads_client", "checkValidityOfNativeVideoItems(): " + ex);
        }
    }
}
