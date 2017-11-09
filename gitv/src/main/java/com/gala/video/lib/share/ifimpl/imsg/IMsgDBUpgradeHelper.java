package com.gala.video.lib.share.ifimpl.imsg;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.gala.video.lib.share.ifimpl.imsg.utils.IMsgUtils;
import com.gala.video.lib.share.ifimpl.imsg.utils.IMsgUtils.DBColumns;
import com.gala.video.lib.share.ifmanager.bussnessIF.imsg.IMsgContent;
import java.util.ArrayList;
import java.util.List;

class IMsgDBUpgradeHelper {
    IMsgDBUpgradeHelper() {
    }

    void removeData(SQLiteDatabase db, IMsgContent content) {
        if (content != null) {
            int isRead;
            int isSeries;
            int isShow;
            ContentValues initialValues = new ContentValues();
            initialValues.put(DBColumns.TEMPLATE_ID, Integer.valueOf(content.msg_template_id));
            initialValues.put("title", content.msg_title);
            initialValues.put(DBColumns.LEVEL, Integer.valueOf(content.msg_level));
            initialValues.put("type", Integer.valueOf(content.msg_type));
            initialValues.put("version", content.min_support_version);
            initialValues.put(DBColumns.PIC, content.pic_url);
            initialValues.put(DBColumns.DESCRIPTION, content.description);
            initialValues.put(DBColumns.BUTTON_NAME, content.button_name);
            initialValues.put("detail", Integer.valueOf(content.is_detailpage));
            initialValues.put("jump", Integer.valueOf(content.page_jumping));
            initialValues.put("url", content.url);
            initialValues.put("plid", content.related_plids);
            initialValues.put("aid", content.related_aids);
            initialValues.put("tvid", content.related_vids);
            if (content.isRead) {
                isRead = 1;
            } else {
                isRead = 0;
            }
            initialValues.put("read", Integer.valueOf(isRead));
            initialValues.put(DBColumns.TV_TYPE, Integer.valueOf(content.tv_type));
            if (content.isSeries) {
                isSeries = 1;
            } else {
                isSeries = 0;
            }
            initialValues.put("series", Integer.valueOf(isSeries));
            initialValues.put("source", content.sourceCode);
            initialValues.put("channel", Integer.valueOf(content.channelId));
            initialValues.put(DBColumns.MSG_ID, Integer.valueOf(content.msg_id));
            initialValues.put(DBColumns.LOCAL_TIME, Long.valueOf(content.localTime));
            initialValues.put("album", content.album);
            if (content.isShowDialog) {
                isShow = 1;
            } else {
                isShow = 0;
            }
            initialValues.put(DBColumns.IS_NEED_SHOW, Integer.valueOf(isShow));
            initialValues.put(DBColumns.CONTENT, content.content);
            initialValues.put("style", Integer.valueOf(content.style));
            initialValues.put(DBColumns.URL_WINDOW, content.url_window);
            initialValues.put(DBColumns.COUPON_KEY, content.coupon_key);
            initialValues.put(DBColumns.COUPON_SIGN, content.coupon_sign);
            initialValues.put(DBColumns.VALID_TILL, Long.valueOf(content.valid_till));
            if (db != null) {
                db.beginTransaction();
                db.insert("imsg", null, initialValues);
                db.setTransactionSuccessful();
                db.endTransaction();
            }
            Log.d(IMsgUtils.TAG, "remove msg:" + content.toString());
        }
    }

    List<IMsgContent> query(SQLiteDatabase db) {
        List<IMsgContent> list = new ArrayList();
        if (db != null) {
            db.beginTransaction();
            try {
                Cursor cursor = db.query("imsg", null, null, null, null, null, DBColumns.DEFAULT_SORT_ORDER);
                if (cursor != null) {
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast()) {
                        list.add(query(cursor));
                        cursor.moveToNext();
                    }
                    cursor.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            db.setTransactionSuccessful();
            db.endTransaction();
        }
        return list;
    }

    private IMsgContent query(Cursor cursor) {
        boolean z;
        boolean z2 = true;
        IMsgContent content = new IMsgContent();
        content.msg_template_id = cursor.getInt(cursor.getColumnIndex(DBColumns.TEMPLATE_ID));
        content.msg_title = cursor.getString(cursor.getColumnIndex("title"));
        content.msg_level = cursor.getInt(cursor.getColumnIndex(DBColumns.LEVEL));
        content.msg_type = cursor.getInt(cursor.getColumnIndex("type"));
        content.min_support_version = cursor.getString(cursor.getColumnIndex("version"));
        content.pic_url = cursor.getString(cursor.getColumnIndex(DBColumns.PIC));
        content.description = cursor.getString(cursor.getColumnIndex(DBColumns.DESCRIPTION));
        content.button_name = cursor.getString(cursor.getColumnIndex(DBColumns.BUTTON_NAME));
        content.is_detailpage = cursor.getInt(cursor.getColumnIndex("detail"));
        content.page_jumping = cursor.getInt(cursor.getColumnIndex("jump"));
        content.url = cursor.getString(cursor.getColumnIndex("url"));
        content.related_plids = cursor.getString(cursor.getColumnIndex("plid"));
        content.related_aids = cursor.getString(cursor.getColumnIndex("aid"));
        content.related_vids = cursor.getString(cursor.getColumnIndex("tvid"));
        if (cursor.getInt(cursor.getColumnIndex("read")) == 1) {
            z = true;
        } else {
            z = false;
        }
        content.isRead = z;
        content.tv_type = cursor.getInt(cursor.getColumnIndex(DBColumns.TV_TYPE));
        if (cursor.getInt(cursor.getColumnIndex("series")) != 1) {
            z2 = false;
        }
        content.isSeries = z2;
        content.sourceCode = cursor.getString(cursor.getColumnIndex("source"));
        content.channelId = cursor.getInt(cursor.getColumnIndex("channel"));
        content.msg_id = cursor.getInt(cursor.getColumnIndex(DBColumns.MSG_ID));
        content.localTime = cursor.getLong(cursor.getColumnIndex(DBColumns.LOCAL_TIME));
        content.album = cursor.getString(cursor.getColumnIndex("album"));
        return content;
    }
}
