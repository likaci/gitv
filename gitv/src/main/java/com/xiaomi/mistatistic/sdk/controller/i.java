package com.xiaomi.mistatistic.sdk.controller;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.text.TextUtils;
import com.gala.tvapi.tv2.model.Album;
import com.xiaomi.mistatistic.sdk.data.h;

public class i {
    private static p a;

    public static h a(Cursor cursor) {
        h hVar = new h();
        long j = cursor.getLong(2);
        String string = cursor.getString(4);
        String string2 = cursor.getString(5);
        String string3 = cursor.getString(1);
        String string4 = cursor.getString(3);
        String string5 = cursor.getString(6);
        h hVar2 = new h();
        hVar2.a = string3;
        hVar2.c = string4;
        hVar2.e = string;
        hVar2.b = j;
        hVar2.d = string2;
        hVar2.f = string5;
        return hVar2;
    }

    public static void a() {
        a = new p(a.a());
    }

    public h a(String str, String str2) {
        Cursor cursor;
        Throwable th;
        Cursor cursor2 = null;
        try {
            h a;
            Cursor query = a.getReadableDatabase().query("mistat_event", null, "category=? AND key=?", new String[]{str, str2}, null, null, null);
            if (query != null) {
                try {
                    if (query.moveToFirst()) {
                        a = a(query);
                        if (query != null) {
                            return a;
                        }
                        query.close();
                        return a;
                    }
                } catch (SQLiteException e) {
                    cursor = query;
                    if (cursor != null) {
                        return null;
                    }
                    cursor.close();
                    return null;
                } catch (Throwable th2) {
                    th = th2;
                    cursor2 = query;
                    if (cursor2 != null) {
                        cursor2.close();
                    }
                    throw th;
                }
            }
            a = null;
            if (query != null) {
                return a;
            }
            query.close();
            return a;
        } catch (SQLiteException e2) {
            cursor = null;
            if (cursor != null) {
                return null;
            }
            cursor.close();
            return null;
        } catch (Throwable th3) {
            th = th3;
            if (cursor2 != null) {
                cursor2.close();
            }
            throw th;
        }
    }

    public void a(long j) {
        try {
            a.getWritableDatabase().delete("mistat_event", "ts<=?", new String[]{String.valueOf(j)});
        } catch (SQLiteException e) {
        }
    }

    public void a(h hVar) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("category", hVar.a);
        contentValues.put(Album.KEY, TextUtils.isEmpty(hVar.c) ? "" : hVar.c);
        contentValues.put("ts", Long.valueOf(hVar.b));
        contentValues.put("type", TextUtils.isEmpty(hVar.d) ? "" : hVar.d);
        contentValues.put("value", TextUtils.isEmpty(hVar.e) ? "" : hVar.e);
        contentValues.put("extra", TextUtils.isEmpty(hVar.f) ? "" : hVar.f);
        try {
            a.getWritableDatabase().insert("mistat_event", "", contentValues);
        } catch (Throwable e) {
            new o().a("Error to insert data into DB, key=" + hVar.c, e);
        }
    }

    public void a(String str, String str2, String str3) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("value", str3);
        try {
            a.getWritableDatabase().update("mistat_event", contentValues, "category=? AND key=?", new String[]{str2, str});
        } catch (Throwable e) {
            new o().a("Error to update data from DB, key=" + str, e);
        }
    }

    public Cursor b() {
        try {
            return a.getReadableDatabase().query("mistat_event", null, null, null, null, null, "ts DESC");
        } catch (Throwable e) {
            new o().a("Error while reading data from DB", e);
            return null;
        }
    }

    public void c() {
        long currentTimeMillis = System.currentTimeMillis() - 259200000;
        try {
            a.getWritableDatabase().delete("mistat_event", "ts<=? and category <> ?", new String[]{String.valueOf(currentTimeMillis), "mistat_basic"});
        } catch (SQLiteException e) {
        }
    }

    public int d() {
        Cursor cursor;
        Throwable th;
        Cursor cursor2 = null;
        try {
            Cursor query = a.getReadableDatabase().query("mistat_event", new String[]{"count(*)"}, null, null, null, null, null);
            if (query != null) {
                try {
                    if (query.moveToFirst()) {
                        int i = query.getInt(0);
                        if (query == null) {
                            return i;
                        }
                        query.close();
                        return i;
                    }
                } catch (SQLiteException e) {
                    cursor = query;
                    if (cursor != null) {
                        cursor.close();
                    }
                    return 0;
                } catch (Throwable th2) {
                    th = th2;
                    cursor2 = query;
                    if (cursor2 != null) {
                        cursor2.close();
                    }
                    throw th;
                }
            }
            if (query != null) {
                query.close();
            }
        } catch (SQLiteException e2) {
            cursor = null;
            if (cursor != null) {
                cursor.close();
            }
            return 0;
        } catch (Throwable th3) {
            th = th3;
            if (cursor2 != null) {
                cursor2.close();
            }
            throw th;
        }
        return 0;
    }
}
