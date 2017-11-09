package com.gala.appmanager.p003b;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import com.gala.appmanager.utils.SerializableList;
import com.gala.video.db.DBManager;

public class C0106a {
    private DBManager f368a;

    public C0106a(Context context) {
        this.f368a = DBManager.getInstance(context);
        m226b();
    }

    public void m228a() {
        if (this.f368a != null) {
            this.f368a.open();
        }
    }

    public void m229a(String str) {
        if (this.f368a != null) {
            this.f368a.insert("INSERT INTO order_list VALUES('" + str + "'," + (m225a() + 1) + ")");
        }
    }

    public void m231b(String str) {
        if (this.f368a != null) {
            this.f368a.delete("DELETE FROM order_list WHERE package_name='" + str + "'");
        }
    }

    public void m232c(String str) {
        if (this.f368a != null) {
            this.f368a.update("UPDATE order_list SET sequence=" + (m225a() + 1) + " WHERE " + "package_name" + "='" + str + "'");
        }
    }

    public SerializableList<String> m227a() {
        SerializableList<String> serializableList = new SerializableList();
        if (this.f368a != null) {
            Cursor rawQuery = this.f368a.rawQuery("SELECT package_name FROM order_list ORDER BY sequence DESC", null);
            if (rawQuery != null) {
                while (rawQuery.moveToNext()) {
                    try {
                        String string = rawQuery.getString(rawQuery.getColumnIndex("package_name"));
                        serializableList.add(string);
                        Log.d("DBManager", "DB: " + string);
                    } catch (Exception e) {
                        Log.d("DBManager", e.toString());
                    }
                }
                rawQuery.close();
            }
        }
        return serializableList;
    }

    public boolean m230a(String str) {
        if (this.f368a != null) {
            m225a();
            Cursor rawQuery = this.f368a.rawQuery("SELECT sequence FROM order_list WHERE package_name='" + str + "'", null);
            if (rawQuery != null) {
                if (rawQuery.moveToNext()) {
                    rawQuery.close();
                    return true;
                }
                rawQuery.close();
            }
        }
        return false;
    }

    private void m226b() {
        try {
            this.f368a.createTable("CREATE TABLE IF NOT EXISTS order_list (package_name VARCHAR(256) PRIMARY KEY, sequence INTEGER)");
        } catch (SQLiteException e) {
            Log.e("DBManager", e.toString());
        } catch (Exception e2) {
            Log.e("DBManager", e2.toString());
        }
    }

    private int m225a() {
        int i = -1;
        if (this.f368a != null) {
            Cursor rawQuery = this.f368a.rawQuery("SELECT sequence FROM order_list ORDER BY sequence DESC LIMIT 0,1", null);
            if (rawQuery != null) {
                try {
                    if (rawQuery.moveToNext()) {
                        i = rawQuery.getInt(rawQuery.getColumnIndex("sequence"));
                    }
                } catch (Exception e) {
                    Log.e("DBManager", e.toString());
                }
                rawQuery.close();
            }
        }
        return i;
    }
}
