package com.xiaomi.mistatistic.sdk.controller;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.text.TextUtils;
import com.gala.tvapi.tv2.model.Album;
import com.xiaomi.mistatistic.sdk.data.C2144h;

public class C2118i {
    private static C2125p f2205a;

    public static C2144h m1815a(Cursor cursor) {
        C2144h c2144h = new C2144h();
        long j = cursor.getLong(2);
        String string = cursor.getString(4);
        String string2 = cursor.getString(5);
        String string3 = cursor.getString(1);
        String string4 = cursor.getString(3);
        String string5 = cursor.getString(6);
        C2144h c2144h2 = new C2144h();
        c2144h2.f2242a = string3;
        c2144h2.f2244c = string4;
        c2144h2.f2246e = string;
        c2144h2.f2243b = j;
        c2144h2.f2245d = string2;
        c2144h2.f2247f = string5;
        return c2144h2;
    }

    public static void m1816a() {
        f2205a = new C2125p(C2111a.m1779a());
    }

    public C2144h m1817a(String str, String str2) {
        Cursor cursor;
        Throwable th;
        Cursor cursor2 = null;
        try {
            C2144h a;
            Cursor query = f2205a.getReadableDatabase().query("mistat_event", null, "category=? AND key=?", new String[]{str, str2}, null, null, null);
            if (query != null) {
                try {
                    if (query.moveToFirst()) {
                        a = C2118i.m1815a(query);
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

    public void m1818a(long j) {
        try {
            f2205a.getWritableDatabase().delete("mistat_event", "ts<=?", new String[]{String.valueOf(j)});
        } catch (SQLiteException e) {
        }
    }

    public void m1819a(C2144h c2144h) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("category", c2144h.f2242a);
        contentValues.put(Album.KEY, TextUtils.isEmpty(c2144h.f2244c) ? "" : c2144h.f2244c);
        contentValues.put("ts", Long.valueOf(c2144h.f2243b));
        contentValues.put("type", TextUtils.isEmpty(c2144h.f2245d) ? "" : c2144h.f2245d);
        contentValues.put("value", TextUtils.isEmpty(c2144h.f2246e) ? "" : c2144h.f2246e);
        contentValues.put("extra", TextUtils.isEmpty(c2144h.f2247f) ? "" : c2144h.f2247f);
        try {
            f2205a.getWritableDatabase().insert("mistat_event", "", contentValues);
        } catch (Throwable e) {
            new C2124o().m1841a("Error to insert data into DB, key=" + c2144h.f2244c, e);
        }
    }

    public void m1820a(String str, String str2, String str3) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("value", str3);
        try {
            f2205a.getWritableDatabase().update("mistat_event", contentValues, "category=? AND key=?", new String[]{str2, str});
        } catch (Throwable e) {
            new C2124o().m1841a("Error to update data from DB, key=" + str, e);
        }
    }

    public Cursor m1821b() {
        try {
            return f2205a.getReadableDatabase().query("mistat_event", null, null, null, null, null, "ts DESC");
        } catch (Throwable e) {
            new C2124o().m1841a("Error while reading data from DB", e);
            return null;
        }
    }

    public void m1822c() {
        long currentTimeMillis = System.currentTimeMillis() - 259200000;
        try {
            f2205a.getWritableDatabase().delete("mistat_event", "ts<=? and category <> ?", new String[]{String.valueOf(currentTimeMillis), "mistat_basic"});
        } catch (SQLiteException e) {
        }
    }

    public int m1823d() {
        Cursor cursor;
        Throwable th;
        Cursor cursor2 = null;
        try {
            Cursor query = f2205a.getReadableDatabase().query("mistat_event", new String[]{"count(*)"}, null, null, null, null, null);
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
