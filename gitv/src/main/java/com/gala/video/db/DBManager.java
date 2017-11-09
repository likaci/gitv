package com.gala.video.db;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import com.gala.appmanager.utils.b;

public class DBManager {
    private static DBManager a = null;
    private SQLiteDatabase f243a = null;
    private a f244a;
    private final String f245a = "DBManager";

    private DBManager(Context context, boolean eableWAL) {
        this.f244a = a.a(context, eableWAL);
    }

    public static DBManager getInstance(Context context) {
        if (a == null) {
            a = new DBManager(context, true);
        }
        return a;
    }

    public void open() {
        if (this.f243a == null || !this.f243a.isOpen()) {
            try {
                this.f243a = this.f244a.getWritableDatabase();
            } catch (SQLiteException e) {
                b.c("DBManager", e.toString());
            } catch (Exception e2) {
                b.c("DBManager", e2.toString());
            }
        }
    }

    public SQLiteDatabase getDatabase() {
        return this.f244a.getWritableDatabase();
    }

    public boolean createTable(String sql) {
        try {
            this.f243a = this.f244a.getWritableDatabase();
            this.f243a.execSQL(sql);
            return true;
        } catch (SQLException e) {
            b.c("DBManager", "insert----" + e.toString());
            return false;
        } catch (Exception e2) {
            b.c("DBManager", "insert----" + e2.toString());
            return false;
        }
    }

    public boolean insert(String sql) {
        try {
            if (this.f243a == null) {
                b.b("DBManager", "delete----database object is null.");
                return false;
            }
            this.f243a.execSQL(sql);
            return true;
        } catch (SQLException e) {
            b.c("DBManager", "insert----" + e.toString());
            return false;
        } catch (Exception e2) {
            b.c("DBManager", "insert----" + e2.toString());
            return false;
        }
    }

    public void delete(String sql) {
        try {
            if (this.f243a == null) {
                b.c("DBManager", "delete----database object is null.");
            } else {
                this.f243a.execSQL(sql);
            }
        } catch (SQLException e) {
            b.c("DBManager", "delete----" + e.toString());
        } catch (Exception e2) {
            b.c("DBManager", "delete----" + e2.toString());
        }
    }

    public void update(String sql) {
        try {
            if (this.f243a == null) {
                b.c("DBManager", "update-----database object is null.");
            } else {
                this.f243a.execSQL(sql);
            }
        } catch (SQLException e) {
            b.c("DBManager", "update----" + e.toString());
        } catch (Exception e2) {
            b.c("DBManager", "update----" + e2.toString());
        }
    }

    public Cursor rawQuery(String sql, String[] selectionArgs) {
        Cursor cursor = null;
        if (this.f243a != null) {
            try {
                cursor = this.f243a.rawQuery(sql, selectionArgs);
            } catch (Exception e) {
                b.c("DBManager", e.toString());
            }
        }
        return cursor;
    }

    public void close() {
        if (this.f243a != null) {
            this.f243a.close();
            this.f243a = null;
        }
    }

    public void closeDB() {
        if (this.f244a != null) {
            this.f244a.close();
        }
    }
}
