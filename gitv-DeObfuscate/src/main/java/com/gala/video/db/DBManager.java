package com.gala.video.db;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import com.gala.appmanager.utils.C0109b;

public class DBManager {
    private static DBManager f2009a = null;
    private SQLiteDatabase f2010a = null;
    private C1586a f2011a;
    private final String f2012a = "DBManager";

    private DBManager(Context context, boolean eableWAL) {
        this.f2011a = C1586a.m1567a(context, eableWAL);
    }

    public static DBManager getInstance(Context context) {
        if (f2009a == null) {
            f2009a = new DBManager(context, true);
        }
        return f2009a;
    }

    public void open() {
        if (this.f2010a == null || !this.f2010a.isOpen()) {
            try {
                this.f2010a = this.f2011a.getWritableDatabase();
            } catch (SQLiteException e) {
                C0109b.m239c("DBManager", e.toString());
            } catch (Exception e2) {
                C0109b.m239c("DBManager", e2.toString());
            }
        }
    }

    public SQLiteDatabase getDatabase() {
        return this.f2011a.getWritableDatabase();
    }

    public boolean createTable(String sql) {
        try {
            this.f2010a = this.f2011a.getWritableDatabase();
            this.f2010a.execSQL(sql);
            return true;
        } catch (SQLException e) {
            C0109b.m239c("DBManager", "insert----" + e.toString());
            return false;
        } catch (Exception e2) {
            C0109b.m239c("DBManager", "insert----" + e2.toString());
            return false;
        }
    }

    public boolean insert(String sql) {
        try {
            if (this.f2010a == null) {
                C0109b.m238b("DBManager", "delete----database object is null.");
                return false;
            }
            this.f2010a.execSQL(sql);
            return true;
        } catch (SQLException e) {
            C0109b.m239c("DBManager", "insert----" + e.toString());
            return false;
        } catch (Exception e2) {
            C0109b.m239c("DBManager", "insert----" + e2.toString());
            return false;
        }
    }

    public void delete(String sql) {
        try {
            if (this.f2010a == null) {
                C0109b.m239c("DBManager", "delete----database object is null.");
            } else {
                this.f2010a.execSQL(sql);
            }
        } catch (SQLException e) {
            C0109b.m239c("DBManager", "delete----" + e.toString());
        } catch (Exception e2) {
            C0109b.m239c("DBManager", "delete----" + e2.toString());
        }
    }

    public void update(String sql) {
        try {
            if (this.f2010a == null) {
                C0109b.m239c("DBManager", "update-----database object is null.");
            } else {
                this.f2010a.execSQL(sql);
            }
        } catch (SQLException e) {
            C0109b.m239c("DBManager", "update----" + e.toString());
        } catch (Exception e2) {
            C0109b.m239c("DBManager", "update----" + e2.toString());
        }
    }

    public Cursor rawQuery(String sql, String[] selectionArgs) {
        Cursor cursor = null;
        if (this.f2010a != null) {
            try {
                cursor = this.f2010a.rawQuery(sql, selectionArgs);
            } catch (Exception e) {
                C0109b.m239c("DBManager", e.toString());
            }
        }
        return cursor;
    }

    public void close() {
        if (this.f2010a != null) {
            this.f2010a.close();
            this.f2010a = null;
        }
    }

    public void closeDB() {
        if (this.f2011a != null) {
            this.f2011a.close();
        }
    }
}
